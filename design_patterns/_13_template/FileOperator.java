package design_patterns._13_template;

import java.io.File;

public abstract class FileOperator implements FileOperations{
    private File myFile;
    public File getMyFile() {
        return myFile;
    }
    public FileOperator(String path) {
        myFile=new File(path);
    }
    @Override
    public void prepareData() {
        System.out.println("Common prepare file impl");
    }
    @Override
    public void openFile() {
        System.out.println("Common open file impl");
    }
    @Override
    public void writeFooter() {
        System.out.println("Common write footer impl");
    }
    @Override
    public void closeFile() {
        System.out.println("Common close file impl");
    }

    @Override
    public void performExecution() {
        prepareData();
        openFile();
        writeHeader();
        writeDataRows();
        writeFooter();
        closeFile();
    }
}
