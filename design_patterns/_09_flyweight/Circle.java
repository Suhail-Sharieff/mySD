package design_patterns._09_flyweight;


/* ============================= */
/*    Concrete Flyweights        */
/* ============================= */

class Circle implements Shape {

    // Intrinsic state (shared)
    private final String shapeType = "CIRCLE";
    @SuppressWarnings("unused")
    private final String const_attr_1 = "SOME CONSTANT VALUE";
    @SuppressWarnings("unused")
    private final int const_attr_2 = Integer.MAX_VALUE;

    public Circle() {
        System.out.println("Creating Circle Flyweight Object");
    }

    @Override
    public void draw(int varying_attr_1, String varying_attr_2) {
        System.out.println(
                shapeType +
                " | Hash=" + this.hashCode() +
                " | varying_attr_1=" + varying_attr_1 +
                " | varying_attr_2=" + varying_attr_2
        );
    }
}
