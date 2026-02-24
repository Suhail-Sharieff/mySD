package design_patterns._15_visitor._02_example;

public class DrawVisitor implements ShapeVisitor{

    @Override
    public void visitCircle(Circle circle) {
        System.out.println("Drawing Cirlce....");        
    }

    @Override
    public void visitRectangle(Rectangle rectangle) {
        System.out.println("Drawing Rectangle...");        
    }
    
}
