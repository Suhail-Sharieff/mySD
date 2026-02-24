package design_patterns._15_visitor._02_example;

/*

----------EARLIER (if we added functionalities using Strategy pattern)
interface Shape{
    void calculateArea();
}
class Circle implements Shape{ ..override.. }
class Rectangle implements Shape{ ..override.. }

class Main{
    psvm(){
        Shape myShape=new Circle(radius:23);
        myShape.calculateArea();//Area of circle=1661.902
        myShape=new Rectangle(length:2,breadth:4);
        myShape.calculateArea();//Area of rectanle=8.0
    }
}


But say we want to add more methods in Shape interface ie draw(),exportAsSVG(),exportAsJSON()...then we have to implement each eveytnme we add in Circle and Rectangle

Overtime it becomes bulky and violates Open-Closed prinicple 

So Solution is Visitor pattern

Step1: Create ShapeVisitor where , for each of Shape u want create visit method
interface ShapeVisitor{
    visitCircle(Circle circle);
    visitRectangle(Rectangle rectangle)
}

Step2: Now create Shape interface with only 1 method accept()
interface Shape{
    void accept(ShapeVisitor visitor);
}

Step3: Now say u wanted 
draw() -> DrawVisitor implements ShapeVisitor, there u can define how u want to draw both Circle and rectangle
exportAsSVG() -> ExportAsSVGVisitor implements ShapeVisitor, there u can define how u want to svgise both Circle and rectangle
json() -> JSON_Visitor implements ShapeVisitor, there u can define how u want to export both Circle and rectangle as json
calculateArea() -> AreaVisitor implements ShapeVisitor, there u can define how u want to calc area for both Circle and rectangle

Step4: See Main method below how to use

*/


import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Shape>shapes=List.of(
            new Circle(23),
            new Rectangle(2, 4)
        );

        //lets say i want to calculate area
        AreaVisitor areaVisitor=new AreaVisitor();
        for(Shape e:shapes) e.accept(areaVisitor);

        //now say i want to export as SVG,JSON, or draw
        ExportAsSVGVisitor svgVis=new ExportAsSVGVisitor();
        for(Shape e:shapes) e.accept(svgVis);

    }
}
