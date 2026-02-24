package design_patterns._15_visitor._02_example;

public class ExportAsSVGVisitor implements ShapeVisitor{

    @Override
    public void visitCircle(Circle circle) {
        System.out.println("Exporting circle as SVG....");        
    }

    @Override
    public void visitRectangle(Rectangle rectangle) {
        System.out.println("Exporting rectangle as SVG...");        
    }
    
}
