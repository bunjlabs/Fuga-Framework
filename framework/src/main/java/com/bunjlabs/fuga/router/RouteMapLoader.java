/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fuga.router;

import com.bunjlabs.fuga.foundation.http.RequestMethod;
import com.bunjlabs.fuga.foundation.controllers.DefaultController;
import com.bunjlabs.fuga.resources.ResourceRepresenter;
import static com.bunjlabs.fuga.router.Tokenizer.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RouteMapLoader {

    private final Logger log = LogManager.getLogger(RouteMapLoader.class);

    private final ResourceRepresenter resourceRepresenter;

    private Tokenizer t;
    private final List<String> uses = new ArrayList<>();
    private final Class defaultController = DefaultController.class;

    private List<Extension> extensions;

    public RouteMapLoader(ResourceRepresenter resourceRepresenter) {
        this.resourceRepresenter = resourceRepresenter;
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
            throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
        }

        boolean isConst = t.ttype == TK_STRCONST;

        value = t.sval;
        t.next();

        if (t.ttype == ':') {
            t.next();
            if (t.ttype != TK_WORD) {
                throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
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
            throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
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
                throw new RoutesMapLoadException(t, "Unable to cast parameter " + i, ex);
            }
        }

        if (classMethodFull.trim().isEmpty()) {
            throw new RoutesMapLoadException(t, "Method name cannot be empty!");
        }

        int lio = classMethodFull.lastIndexOf('.');
        String className;
        String classMethod;

        if (lio < 0) {
            className = "";
            classMethod = classMethodFull;

            switch (classMethod) {
                case "view":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateView", classes), parameters);
                case "asset":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateAsset", classes), parameters);
                case "notFound":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateNotFound", classes), parameters);
                case "seeOther":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateSeeOther", classes), parameters);
                case "ok":
                    return new Route(defaultController, tryGetMethod(defaultController, "generateOk", classes), parameters);
            }
        } else {
            className = classMethodFull.substring(0, lio);
            classMethod = classMethodFull.substring(lio + 1);
        }

        Class c;
        Method m;
        for (String use : uses) {
            if ((c = tryLoadClass(className.isEmpty() ? use : use + "." + className)) != null) {
                if ((m = tryGetMethod(c, classMethod, classes)) != null) {
                    return new Route(c, m, parameters);
                }
            }
        }

        throw new RoutesMapLoadException(t, "Method not found: " + className + "." + classMethod);

    }

    private Extension extension() throws RoutesMapLoadException, RoutesMapSyntaxException {
        Set<RequestMethod> methods = EnumSet.noneOf(RequestMethod.class);
        Pattern pattern = null;
        Pattern host = null;
        boolean patternAccumulator = false;

        for (;;) {
            if (t.ttype == TK_METHOD) {
                methods.add(RequestMethod.valueOf(t.sval));
                t.next();
            } else if (t.ttype == TK_PATTERN) {
                if (t.sval.startsWith("!")) {
                    patternAccumulator = true;
                    pattern = Pattern.compile(t.sval.substring(1));
                } else {
                    pattern = Pattern.compile(t.sval);
                }

                t.next();
            } else if (t.ttype == TK_HOST) {
                t.next();
                if (t.ttype != TK_PATTERN) {
                    throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
                }
                host = Pattern.compile(t.sval);
                t.next();
            } else if (t.ttype == '{') {
                t.next();
                return new Extension(methods, pattern, host, patternAccumulator, extensionList());
            } else if (t.ttype == TK_WORD) {
                return new Extension(methods, pattern, host, patternAccumulator, route());
            } else {
                throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
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
                        throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
                    }
                    uses.add(t.sval);
                    t.next();
                    break;
                }
                case TK_INCLUDE: {
                    t.next();
                    if (t.ttype != TK_STRCONST) {
                        throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
                    }

                    try {
                        RouteMapLoader mapLoader = new RouteMapLoader(resourceRepresenter);
                        list.addAll(mapLoader.load(t.sval));
                    } catch (RoutesMapLoadException | RoutesMapSyntaxException | FileNotFoundException e) {
                        throw new RoutesMapLoadException(t, "Unable to include map: " + t.sval, e);
                    }

                    t.next();
                    break;
                }
                case TK_METHOD:
                case TK_PATTERN:
                case TK_WORD:
                case TK_HOST:
                case '{': {
                    list.add(extension());
                    break;
                }
                case '}': {
                    t.next();
                    return list;
                }
                default: {
                    throw new RoutesMapSyntaxException(t, "Unexpected token: " + (t.ttype >= 0 ? (char) t.ttype : t.sval));
                }
            }
        }
        return list;
    }

    public List<Extension> load(String path) throws RoutesMapLoadException, RoutesMapSyntaxException, FileNotFoundException {
        return load(resourceRepresenter.load(path));

    }

    public List<Extension> loadFromClasspath(String path) throws RoutesMapLoadException, RoutesMapSyntaxException, FileNotFoundException {
        return load(resourceRepresenter.loadFromClasspath(path));
    }

    public List<Extension> loadFromString(String input) throws NullPointerException, RoutesMapLoadException, RoutesMapSyntaxException {
        return load(new ByteArrayInputStream(input.getBytes()));
    }

    public List<Extension> load(InputStream input) throws RoutesMapLoadException, RoutesMapSyntaxException {
        t = new Tokenizer(new InputStreamReader(input));

        t.next();

        return extensionList();
    }

    public List<Extension> getExtensions() {
        return extensions;
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
