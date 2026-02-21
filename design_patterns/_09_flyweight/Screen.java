package design_patterns._09_flyweight;

import java.util.ArrayList;
import java.util.List;

/* ============================= */
/*         Context Class         */
/* ============================= */

class Screen {

    private final List<ShapeContext> shapes = new ArrayList<>();
    private final ShapeFactory factory;

    public Screen(ShapeFactory factory) {
        this.factory = factory;
    }

    public void addShape(String type, int varying_attr_1, String varying_attr_2) {
        Shape shape = factory.getShape(type); // shared object
        shapes.add(new ShapeContext(shape, varying_attr_1, varying_attr_2));
    }

    public void render() {
        for (ShapeContext context : shapes) {
            context.render();
        }
    }
}
