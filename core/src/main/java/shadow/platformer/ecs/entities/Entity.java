package shadow.platformer.ecs.entities;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private final Map<Class<?>, Object> components = new HashMap<>();

    public <T> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T> T getComponent(Class<T> cls) {
        return cls.cast(components.get(cls));
    }

    public boolean hasComponent(Class<?> cls) {
        return components.containsKey(cls);
    }   
}
