package com.showvars.fugaframework.router;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.RequestMethod;
import static com.showvars.fugaframework.router.Tokenizer.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RouteMapLoader {

    private Tokenizer t;
    private final List<String> uses = new ArrayList<>();

    public RouteMapLoader() {
        uses.add("com.showvars.fugaframework.foundation.controllers.DefaultController");
    }

    private Class tryLoadClass(String name) {
        try {
            return Class.forName(name);
        } catch (Exception ex) {
            return null;
        }
    }

    private RouteParameter parameter() throws Exception {
        String value;
        String type = "String";

        if (t.ttype != TK_INTEGER && t.ttype != TK_STRCONST) {
            throw new Exception("Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
        }

        boolean isConst = t.ttype == TK_STRCONST;

        value = t.sval;
        t.next();

        if (t.ttype == ':') {
            t.next();
            if (t.ttype != TK_WORD) {
                throw new Exception("Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
            }
            type = t.sval;
            t.next();
        }
        if (isConst) {
            return new RouteParameter(value, type);
        } else {
            return new RouteParameter(Integer.parseInt(value), type);
        }
    }

    private Route route() throws Exception {
        String classMethodFull = t.sval;
        t.next();
        if (t.ttype != '(') {
            throw new Exception("Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
        }
        t.next();

        List<RouteParameter> parameters = new ArrayList<>();

        for (;;) {
            if (t.ttype == ')') {
                t.next();
                break;
            }
            parameters.add(parameter());

            if (t.ttype == ',') {
                t.next();
            }
        }

        if (classMethodFull.trim().isEmpty()) {
            throw new Exception("Method name cannot be empty!");
        }
        int lio = classMethodFull.lastIndexOf('.');
        String className = "";
        String classMethod;

        if (lio < 0) {
            classMethod = classMethodFull;
        } else {
            className = classMethodFull.substring(0, lio);
            classMethod = classMethodFull.substring(lio + 1);
        }

        Class c = null;
        for (String use : uses) {
            if ((c = tryLoadClass(use + "." + className)) != null) {
                break;
            }
        }

        if (c == null) {
            c = tryLoadClass(className);
        }

        if (c == null) {
            throw new Exception("Class not found: " + className);
        }

        Class[] classes = new Class[parameters.size() + 1];
        classes[0] = Context.class;

        for (int i = 1; i < classes.length; i++) {
            classes[i] = getClassTypeBySimpleName(parameters.get(i - 1).getDataType());
        }

        Method m = c.getMethod(classMethod, classes);

        return new Route(m, parameters);
    }

    private Extension extension() throws Exception {
        RequestMethod method = null;
        Pattern pattern = null;

        for (;;) {
            if (t.ttype == TK_METHOD) {
                method = RequestMethod.valueOf(t.sval);
                t.next();
            } else if (t.ttype == TK_PATTERN) {
                pattern = Pattern.compile(t.sval);
                t.next();
            } else if (t.ttype == '{') {
                t.next();
                return new Extension(method, pattern, extensionList());
            } else if (t.ttype == TK_WORD) {
                return new Extension(method, pattern, route());
            }
        }
    }

    private List<Extension> extensionList() throws Exception {
        List<Extension> list = new ArrayList<>();
        while (t.ttype != Tokenizer.TK_EOF) {
            switch (t.ttype) {
                case TK_USE: {
                    t.next();
                    if (t.ttype != TK_WORD) {
                        throw new Exception("Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
                    }
                    uses.add(t.sval);
                    t.next();
                    break;
                }
                case TK_METHOD:
                case TK_PATTERN:
                case TK_WORD:
                case '{': {
                    list.add(extension());
                    break;
                }
                case '}': {
                    t.next();
                    return list;
                }
            }
            //t.next();
        }
        return list;
    }

    public List<Extension> load(InputStream input) throws Exception {
        t = new Tokenizer(new InputStreamReader(input));

        t.next();
        
        return extensionList();

    }

    public static Class getClassTypeBySimpleName(String name) throws ClassNotFoundException {
        switch (name) {
            case "String":
            case "string":
                return String.class;
            case "int":
            case "integer":
                return int.class;
            case "long":
                return long.class;
            case "short":
                return short.class;
            case "byte":
                return byte.class;
            case "char":
                return char.class;
            case "boolean":
            case "bool":
                return boolean.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            default:
                throw new ClassNotFoundException(name);

        }
    }

}
