package questions._10_file_system;

import java.util.HashMap;
import java.util.Map;

/**
user will give operations like ls,mkdir,ls -l(detailed listing ith file metadata),pwd,echo,cat, mv,rm etc...
we need some kind of parser to parse these inputs into command, so we need to use Command Pattern
each command only handles  its own working so single resp priciple followed
file and Directorys are nodes , composite pattern
facade class FileSystem, provides apis about structure metadata like current working directory/Directory etc that command classes can use in thier implementations
 */
abstract class Node{
    final String id;
    final Directory parentDirectory;
    public Node(String id, Directory parentDirectory) {
        this.id = id;
        this.parentDirectory = parentDirectory;
    }
}
class File extends Node{
    final String content;
    public File(String id, Directory parentDirectory,String content) {
        super(id, parentDirectory);
        this.content=content;
    }
}
class Directory extends Node{//directory/folder both are same
    Map<String,Node>map;//only Directorys have childrens, using map instead of list for fast lookups
    public Directory(String id, Directory parentDirectory) {
        super(id, parentDirectory);
        map=new HashMap<>();
    }
    Node nodeAt(String path){return map.get(path);}
}
interface Command{
    void execute();
}
abstract class FileCommand implements Command{
    final FileSystem fs;
    public FileCommand(FileSystem fs) {
        this.fs=fs;
    }
    
}
class CD_Command extends FileCommand{
    final String destDir;
    public CD_Command(FileSystem fs,String destDir) {super(fs);this.destDir=destDir;}
    @Override public void execute() {fs.changeDirectoryTo(destDir);}
}
class TOUCH_Command extends FileCommand{
    final String destDir;
    final String newFileName;
    public TOUCH_Command(FileSystem fs,String destDir,String newFileName) {super(fs);this.destDir=destDir;this.newFileName=newFileName;}
    @Override public void execute() {fs.createNewFile(destDir,newFileName);}
}
class MKDIR_Command extends FileCommand{
    final String path;
    final String newDirName;
    public MKDIR_Command(FileSystem fs,String path,String newDirName) {super(fs);this.newDirName=newDirName;this.path=path;}
    @Override public void execute() {fs.createNewDir(path,newDirName);}
}
class CAT_Command extends FileCommand{
    final String filePath;
    public CAT_Command(FileSystem fs,String filePath) {super(fs);this.filePath=filePath;}
    @Override public void execute() {System.out.println(fs.contentOf(filePath));}
}
class PWD_Command extends FileCommand{
    public PWD_Command(FileSystem fs) {super(fs);}
    @Override public void execute() {System.out.println(fs.presentWorkingDirectory);}
}

interface TreePrinter{
    void printTree(Node root);
}
class StandardTreePrinter implements TreePrinter{
    @Override public void printTree(Node root){}
}
class DetailedTreePrinter implements TreePrinter{
    @Override public void printTree(Node root){}
}
class LS_Command extends FileCommand{
    final TreePrinter printer;
    final String path;
    public LS_Command(FileSystem fs,TreePrinter printerType,String path) {super(fs);this.printer=printerType;this.path=path;}
    @Override public void execute() {printer.printTree(fs.getNodeAt(path));}
}

class FileSystem{
    private FileSystem(){}
    private static FileSystem instance;
    static FileSystem getInstance(){if(instance==null) instance=new FileSystem();return instance;}
    Directory root;
    Directory presentWorkingDirectory;
    Node getNodeAt(String path){return presentWorkingDirectory.nodeAt(path);}
    void changeDirectoryTo(String path){}
    void createNewDir(String path, String newDirName) {}
    String contentOf(String path){return null;}
    void createNewFile(String path,String fileName){}
    void execute(FileCommand cmd){cmd.execute();}
}   
public class Main {
    public static void main(String[] args) {
        FileSystem instance=FileSystem.getInstance();
        instance.execute(new CD_Command(instance, "/src"));
        instance.execute(new TOUCH_Command(instance, "/src", "newfile.txt"));
        instance.execute(new CAT_Command(instance, "/src/newfile.txt"));
        instance.execute(new PWD_Command(instance));
        instance.execute(new LS_Command(instance, new StandardTreePrinter(), "/"));
    }    
}
/*
tis is enough for sde1/mid levels

what senior levels exepect improvements:
- currently FileSystem class is doing so much, they wd add FileSerice and DirService rather and split responsibilities, can also add CreationService, RemovalService etc
- they wud also add Metadata to file 
- currently my code cant handle args like ../../src, they wud create a PathResolver class that takes in path and returns Node at that path
- currently im manually injecting comands like new Command(...), in real world its passed as plain text args, he wud create CommandParser class that takes in args, calls PathResolver to get root and returns Command of that arg, like for "ls" ir wud return LS_Comamnd(..) liek that    
- wud add undo() option in command as well
- wud add event based code, like when "touch a.txt" is executed, it publishes a TocuhEvent and smthng like that event based, so we can perform metrics and logs
*/