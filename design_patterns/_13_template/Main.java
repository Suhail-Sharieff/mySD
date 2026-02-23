package design_patterns._13_template;


//when some methods are still same and very few different, we implement common meths in abstract class and do varyting impl in core class


public class Main {
    public static void main(String[] args) {
        FileOperator fp=new PDF("suhail.pdf");
        fp.performExecution();
    }
}
/*Common prepare file impl
Common open file impl
Write header impl for PDF
Write data row impl for PDF
Common write footer impl
Common close file impl */