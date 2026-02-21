package design_patterns._09_flyweight;



/* ============================= */
/*    Extrinsic State Holder     */
/* ============================= */

class ShapeContext {

    private final Shape shape;              // shared flyweight
    private final int varying_attr_1;       // extrinsic
    private final String varying_attr_2;    // extrinsic

    public ShapeContext(Shape shape,
                        int varying_attr_1,
                        String varying_attr_2) {

        this.shape = shape;
        this.varying_attr_1 = varying_attr_1;
        this.varying_attr_2 = varying_attr_2;
    }

    public void render() {
        shape.draw(varying_attr_1, varying_attr_2);
    }
}
