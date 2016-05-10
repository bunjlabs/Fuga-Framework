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
package com.bunjlabs.fugaframework.dependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DependencyManager {

    private static final Logger log = LogManager.getLogger(DependencyManager.class);

    private final Map<Class, Object> dependencies = new HashMap<>();

    public DependencyManager() {
    }

    public <T> T inject(Class<T> injectable) throws InjectException {
        Constructor<T> annotatedConstructor = getAnnotatedConstructor(injectable);

        if (annotatedConstructor != null) {
            return injectToConstructor(annotatedConstructor);
        }

        T obj;
        try {
            obj = injectable.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new InjectException("Unable to instatiate class by default constructor of " + injectable.getName(), e);
        }

        injectToFields(obj, getAnnotatedFields(injectable));

        return obj;
    }

    public <T> T injectToConstructor(Constructor<T> injactableConstructor) throws InjectException {
        Object[] parameters = new Object[injactableConstructor.getParameterCount()];
        Class[] parameterTypes = injactableConstructor.getParameterTypes();

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = getDependency(parameterTypes[i]);
        }
        try {
            return injactableConstructor.newInstance(parameters);
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InjectException("Unable to inject by constructor", ex);
        }
    }

    public void injectToFields(Object injectable, List<Field> injectableFields) throws InjectException {
        for (Field f : injectableFields) {
            injectField(injectable, f);
        }
    }

    public void injectField(Object injectable, Field injectableField) throws InjectException {
        try {
            Class cls = injectableField.getType();
            Object injectOnject = getDependency(cls);

            injectableField.set(injectable, injectOnject);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new InjectException("Unable to inject field", e);
        }

    }

    public Constructor getAnnotatedConstructor(Class injactable) {
        return Stream.of(injactable.getConstructors())
                .filter((Constructor c) -> c.isAnnotationPresent(Inject.class))
                .findFirst().orElse(null);
    }

    public List<Field> getAnnotatedFields(Class injectable) {
        return Stream.of(injectable.getFields())
                .filter((Field f) -> f.isAnnotationPresent(Inject.class))
                .collect(Collectors.toList());
    }

    public Object getDependency(Class cls) throws InjectException {
        if (dependencies.containsKey(cls)) {
            return getDependency(cls, dependencies.get(cls));
        } else {
            for (Map.Entry<Class, Object> e : dependencies.entrySet()) {
                if (cls.isAssignableFrom(e.getKey())) {
                    return getDependency(cls, e.getValue());
                }
            }
        }

        throw new InjectException("No suitable dependency for " + cls.getName());
    }
    
    private Object getDependency(Class cls, Object obj) throws InjectException {
        if(obj != null) return obj;
        
        return inject(cls);
    }

    public void registerDependency(Class cls, Object obj) {
        dependencies.put(cls, obj);
    }

    public void registerDependency(Object... objs) {
        for (Object obj : objs) {
            registerDependency(obj.getClass(), obj);
        }
    }

    public void registerDependency(Class... clss) {
        for (Class cls : clss) {
            registerDependency(cls, null);
        }
    }
}
