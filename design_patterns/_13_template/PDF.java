package design_patterns._13_template;

public class PDF extends FileOperator{
    public PDF(String filePath) {
        super(filePath);
    }

    @Override
    public void writeDataRows() {
        System.out.println("Write data row impl for PDF");        
    }

    @Override
    public void writeHeader() {
        System.out.println("Write header impl for PDF");        
    }
    
}
