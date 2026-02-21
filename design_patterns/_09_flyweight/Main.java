package design_patterns._09_flyweight;


/*
    FLYWEIGHT PATTERN
    ------------------
    Intrinsic state  -> stored inside flyweight (shared)
    Extrinsic state  -> passed during method call (not stored)
*/

public class Main {
    public static void main(String[] args) {

        ShapeFactory factory = new ShapeFactory();
        Screen screen = new Screen(factory);

        // Multiple circles with different varying attributes
        screen.addShape("CIRCLE", 23, "Circle1");
        screen.addShape("CIRCLE", 45, "Circle2");
        screen.addShape("CIRCLE", 99, "Circle3");

        // Multiple rectangles
        screen.addShape("RECTANGLE", 10, "Rectangle1");
        screen.addShape("RECTANGLE", 20, "Rectangle2");

        screen.render();

        System.out.println("\nTotal Flyweight Objects Created: "
                + factory.totalObjectsCreated());//OUtput:2, NOT 5 though v added 5 shapes, coz they share memeory
    }
}


