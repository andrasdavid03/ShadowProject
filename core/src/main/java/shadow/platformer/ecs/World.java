package shadow.platformer.ecs;

import shadow.platformer.ecs.entities.Entity;
import shadow.platformer.ecs.systems.System;
import shadow.platformer.events.EventBus;

import java.util.ArrayList;
import java.util.List;


public class World {
    public final List<Entity> entities = new ArrayList<>();
    private final List<System> logicSystems = new ArrayList<>();
    private final List<System> renderSystems = new ArrayList<>();
    public final EventBus bus = new EventBus();

    private boolean paused = false;

    public void addLogicSystem(System s) {
        logicSystems.add(s);

        // TODO: Implement system sorting in the future if needed
        // logicSystems.sort(Comparator.comparingInt(System::priority));
    }

    public void addRenderSystem(System s) {
        renderSystems.add(s);

        // TODO: Implement system sorting in the future if needed
        //renderSystems.sort(Comparator.comparingInt(System::priority));
    }

    public void update(float delta) {
        if (paused) return;

        for (System s : logicSystems) {
            s.update(delta, entities);
        }
    }

    public void render(float delta) {
        for (System s : renderSystems) {
            s.update(delta, entities);
        }
    }

    public void setPaused(boolean p) { this.paused = p; }
    public boolean isPaused() { return paused; }

    public void dispose() {
        bus.clear();
        entities.clear();
        logicSystems.clear();
        renderSystems.clear();
    }
}
