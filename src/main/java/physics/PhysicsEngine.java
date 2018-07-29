package physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {
    private static final List<PhysicalBody> bodies = new ArrayList<>();

    public void update(long delta) {
        bodies.forEach(physicalBody -> physicalBody.update(delta));
    }
}
