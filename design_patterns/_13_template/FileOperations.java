package design_patterns._13_template;

public interface FileOperations{
    void prepareData();
    void openFile();

    void writeHeader();
    void writeDataRows();

    void writeFooter();
    void closeFile();

    void performExecution();
}