package design_patterns._13_template;

public 
class CSV extends FileOperator{
    
    public CSV(String path) {
        super(path);
    }

    @Override
    public void writeDataRows() {
        System.out.println("Write data row impl for CSV");        
    }

    @Override
    public void writeHeader() {
        System.out.println("Write header impl for CSV");        
    }
    
}