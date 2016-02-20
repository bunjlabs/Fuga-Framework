package com.bunjlabs.fugaframework.dependency;

import com.bunjlabs.fugaframework.FugaApp;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DependencyManager {

    private static final Logger log = LogManager.getLogger(DependencyManager.class);

    private final FugaApp app;
    private final Map<Class, Object> dependencies = new HashMap<>();

    public DependencyManager(FugaApp app) {
        this.app = app;
    }

    public void injectDependencies(Object injectable) throws InjectException {
        for (Field f : injectable.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Inject.class)) {
                try {
                    Class cls = f.getType();
                    Object injectOnject = getDependency(cls);

                    f.set(injectable, injectOnject);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new InjectException("Unable to inject field", e);
                }
            }
        }
    }

    public Object getDependency(Class cls) throws InjectException {
        if (dependencies.containsKey(cls)) {
            return dependencies.get(cls);
        } else {
            for (Map.Entry<Class, Object> e : dependencies.entrySet()) {
                if (e.getKey().isAssignableFrom(cls)) {
                    return e.getValue();
                }
            }
        }

        try {
            return cls.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        }

        throw new InjectException("No suitable dependency for " + cls.getName());
    }

    public void addDependency(Object... objs) {
        for (Object obj : objs) {
            dependencies.put(obj.getClass(), obj);
        }
    }

    public void addDependency(Class... clss) {
        for (Class cls : clss) {
            try {
                dependencies.put(cls, cls.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                log.error("Unable to add dependency", ex);
            }
        }
    }
}
