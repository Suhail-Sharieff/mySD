package questions._10_file_system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Trie trie=new Trie("/");
        trie.exists("/api/suhail/sharieff");

    }
}

enum NodeType{
    FILE,
    FOLDER
}

abstract class Node{
    final String name;
    final NodeType type;
    public Node(String name,NodeType type){this.name=name;this.type=type;}
    @Override
    public String toString() {
        return "["+type+"-"+name+"]";
    }
}
class Trie{
    Node root;
    Trie(String rootName){
        root=new Folder(rootName);
    }
    void exists(String path){
        Node curr=root;
        String arr[]=path.split("/");
        System.out.println(Arrays.toString(arr));
    }
}

class File extends Node{
    public File(String name) {
        super(name, NodeType.FILE);
    }
}
class Folder extends Node{
    List<Node>children;
    public Folder(String name) {
        super(name, NodeType.FOLDER);
        children=new ArrayList<>();
    }
}

class FileSystem{

}

