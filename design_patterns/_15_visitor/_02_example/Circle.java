package design_patterns._15_visitor._02_example;

public class Circle implements Shape{

    private double radius;
    public Circle(double radius) {
        this.radius=radius;
    }
    
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visitCircle(this);        
    }

    public double getRadius() {
        return radius;
    }
    
}
