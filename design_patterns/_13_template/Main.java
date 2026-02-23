package design_patterns._13_template;





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