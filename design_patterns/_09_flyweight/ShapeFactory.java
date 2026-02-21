package design_patterns._09_flyweight;

import java.util.HashMap;
import java.util.Map;

/* ============================= */
/*         Flyweight Factory     */
/* ============================= */

class ShapeFactory {

    // Cache based ONLY on intrinsic state
    private final Map<String, Shape> cache = new HashMap<>();

    public Shape getShape(String type) {

        cache.putIfAbsent(type, createShape(type));
        return cache.get(type);
    }

    private Shape createShape(String type) {
        switch (type) {
            case "CIRCLE":
                return new Circle();
            case "RECTANGLE":
                return new Rectangle();
            default:
                throw new IllegalArgumentException("Unknown shape type");
        }
    }

    public int totalObjectsCreated() {
        return cache.size();
    }
}
