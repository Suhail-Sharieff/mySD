package design_patterns._15_visitor._02_example;

public class AreaVisitor implements ShapeVisitor {

    @Override
    public void visitCircle(Circle circle) {
        double area=Math.PI*circle.getRadius()*circle.getRadius();
        System.out.println("Area of circle = "+area);        
    }

    @Override
    public void visitRectangle(Rectangle rectangle) {
        double area=rectangle.getLength()*rectangle.getBreadth();
        System.out.println("Area of rectamgle="+area);
    }
    
}
