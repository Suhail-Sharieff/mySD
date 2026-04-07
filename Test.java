interface Shape{
    double accept(ShapeVisitor v);
}

interface ShapeVisitor{
    double visitCircle(Circle c);
    double visitRectangle(Rectangle r);
}


class AreaVisitor implements ShapeVisitor{
    @Override 
    public double visitCircle(Circle c){return Math.PI*c.r*c.r;}
    @Override 
    public double visitRectangle(Rectangle r){return r.l*r.b;}
}


class Circle implements Shape{
    double r;
    public Circle(double r){this.r=r;}
    @Override
    public double accept(ShapeVisitor s){return s.visitCircle(this);}
}
class Rectangle implements Shape{
    double l,b;
    public Rectangle(double l,double b){this.l=l;this.b=b;}
    @Override
    public double accept(ShapeVisitor s){return s.visitRectangle(this);}
}

public class Test {

    public static void main(String[] args) {
        Shape cir=new Circle(23);
        Shape  rect=new Rectangle(2,3);
        ShapeVisitor vis=new AreaVisitor();
        System.out.println(cir.accept(vis));
        System.out.println(rect.accept(vis));
    }
}