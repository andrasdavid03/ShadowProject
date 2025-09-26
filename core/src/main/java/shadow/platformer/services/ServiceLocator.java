package shadow.platformer.services;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    private ServiceLocator() {}

    public static <T> void register(Class<T> cls, T service) {
        services.put(cls, service);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> cls) {
        return (T) services.get(cls);
    }

    public static void clear() {
        services.clear();
    }
}
