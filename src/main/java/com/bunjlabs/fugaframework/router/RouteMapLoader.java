package com.bunjlabs.fugaframework.router;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.RequestMethod;
import com.bunjlabs.fugaframework.foundation.controllers.DefaultController;
import static com.bunjlabs.fugaframework.router.Tokenizer.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class RouteMapLoader {

    private Tokenizer t;
    private final List<String> uses = new ArrayList<>();
    private static final Class defaultController = DefaultController.class;

    public RouteMapLoader() {
        uses.add("");
    }

    private Class tryLoadClass(String name) {
        try {
            return Class.forName(name.startsWith(".") ? name.substring(1) : name);
        } catch (Exception ex) {
            return null;
        }
    }

    private Method tryGetMethod(Class c, String name, Class[] classes) {
        try {
            return c.getMethod(name, classes);
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    private RouteParameter parameter() throws RoutesMapSyntaxException {
        String value;
        String type = "String";

        if (t.ttype != TK_INTEGER && t.ttype != TK_STRCONST) {
            throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
        }

        boolean isConst = t.ttype == TK_STRCONST;

        value = t.sval;
        t.next();

        if (t.ttype == ':') {
            t.next();
            if (t.ttype != TK_WORD) {
                throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
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

    private Route route() throws RoutesMapSyntaxException, RoutesMapLoadException {
        String classMethodFull = t.sval;
        t.next();
        if (t.ttype != '(') {
            throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
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

        Class[] classes = new Class[parameters.size()];

        for (int i = 0; i < classes.length; i++) {
            try {
                classes[i] = getClassTypeBySimpleName(parameters.get(i).getDataType());
            } catch (ClassNotFoundException ex) {
                throw new RoutesMapLoadException(ex);
            }
        }

        if (classMethodFull.trim().isEmpty()) {
            throw new RoutesMapLoadException("Method name cannot be empty!");
        }

        int lio = classMethodFull.lastIndexOf('.');
        String className;
        String classMethod;

        if (lio < 0) {
            classMethod = classMethodFull;

            switch (classMethod) {
                case "view":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateAssetView", classes), parameters);
                case "asset":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateAsset", classes), parameters);
                case "notFound":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateNotFound", classes), parameters);
                case "ok":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateOk", classes), parameters);
            }
        }

        className = classMethodFull.substring(0, lio);
        classMethod = classMethodFull.substring(lio + 1);

        Class c;
        Method m;
        for (String use : uses) {
            if ((c = tryLoadClass(className.isEmpty() ? use : use + "." + className)) != null) {
                if ((m = tryGetMethod(c, classMethod, classes)) != null) {
                    return new Route(c, m, parameters);
                }
            }
        }

        throw new RoutesMapLoadException("Method not found: " + className + "." + classMethod);

    }

    private Extension extension() throws RoutesMapLoadException, RoutesMapSyntaxException {
        Set<RequestMethod> methods = EnumSet.noneOf(RequestMethod.class);
        Pattern pattern = null;

        for (;;) {
            if (t.ttype == TK_METHOD) {
                methods.add(RequestMethod.valueOf(t.sval));
                t.next();
            } else if (t.ttype == TK_PATTERN) {
                pattern = Pattern.compile(t.sval);
                t.next();
            } else if (t.ttype == '{') {
                t.next();
                return new Extension(methods, pattern, extensionList());
            } else if (t.ttype == TK_WORD) {
                return new Extension(methods, pattern, route());
            } else {
                throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
            }
        }
    }

    private List<Extension> extensionList() throws RoutesMapSyntaxException, RoutesMapLoadException {
        List<Extension> list = new ArrayList<>();
        while (t.ttype != Tokenizer.TK_EOF) {
            switch (t.ttype) {
                case TK_USE: {
                    t.next();
                    if (t.ttype != TK_WORD) {
                        throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
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
                default: {
                    throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : ' '));
                }
            }
        }
        return list;
    }

    public List<Extension> load(InputStream input) throws RoutesMapLoadException, RoutesMapSyntaxException {
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
