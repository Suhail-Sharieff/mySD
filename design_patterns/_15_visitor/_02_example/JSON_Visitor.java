package design_patterns._15_visitor._02_example;

public class JSON_Visitor implements ShapeVisitor{

    @Override
    public void visitCircle(Circle circle) {
        System.out.println("Exporting circle as JSON...");        
    }

    @Override
    public void visitRectangle(Rectangle rectangle) {
        System.out.println("Exporting rectanle as JSON...");        
    }
    
}
