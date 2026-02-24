package design_patterns._15_visitor._02_example;

public class Rectangle implements Shape{
    private double length;
    private double breadth;
    public Rectangle(double length,double breadth){
        this.length=length;
        this.breadth=breadth;
    }
    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visitRectangle(this);        
    }
    public double getLength() {
        return length;
    }
    public double getBreadth() {
        return breadth;
    }
    
}
