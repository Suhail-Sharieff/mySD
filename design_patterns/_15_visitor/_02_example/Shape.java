package design_patterns._15_visitor._02_example;

// public interface Shape {
//     void draw();
//     double calculateArea();
//     String exportAsSvg();
//     String toJson();
// }
public interface Shape {
    void accept(ShapeVisitor visitor);
}