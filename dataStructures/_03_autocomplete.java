package dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class _03_autocomplete {

    public static void main(String[] args) {
        Trie trie=new Trie();

        List<String>data=List.of("app","apple","application","apply");

        for(String str:data) trie.insert(str);


        System.out.println(trie.getRecommendations("appl"));

        trie.select("apply");

        System.out.println(trie.getRecommendations("app"));




    }
}

class Recommendation implements Comparable<Recommendation>{
    private final String s;
    private final int count;
    public int getCount() {
        return count;
    }
    public Recommendation(String s, int count) {
        this.s = s;
        this.count = count;
    }
    @Override
    public String toString() {
        return "["+s+", searches="+count+"]";
    }
    @Override
    public int compareTo(Recommendation o) {
        return Integer.compare(o.getCount(),this.count);
    }
    
}

class Node{
    private final char val;
    private boolean isEnd;
    public boolean isEnd() {
        return isEnd;
    }
    public void setIsEnd() {
        this.isEnd = true;
    }
    public char getVal() {
        return val;
    }
    private Node[]ptrs;
    private int count;

    public int getCount() {
        return count;
    }
    public Node(char val) {
        this.val=val;
        this.ptrs=new Node[26];
        this.count=0;
        this.isEnd=false;
    }
    void setNodeFor(char c){
        ptrs[c-'a']=new Node(c);
    }
    boolean hasNodeFor(char c){
        return ptrs[c-'a']!=null;
    }
    Node getNodeFor(char c){
        return ptrs[c-'a'];
    }
    void incr(){
        count++;
    }
    @Override
    public String toString() {
        return "{"+val+"=>"+count+"}";
    }
}

class Trie{
    private final Node root;

    public Trie() {
        root=new Node('#');
    }


    public void insert(String s){
        char arr[]=s.toCharArray();
        int i=0;
        Node ptr=root;
        while(i<arr.length){
            if(!ptr.hasNodeFor(arr[i])) ptr.setNodeFor(arr[i]);
            Node next=ptr.getNodeFor(arr[i++]);
            ptr=next;
        }
        ptr.setIsEnd();
    }

    public Node getEnd(String s){
        char arr[]=s.toCharArray();
        int i=0;
        Node ptr=root;    
        while(ptr!=null && i<arr.length){
            Node next=ptr.getNodeFor(arr[i++]);
            ptr=next;
        }
        return ptr;
    }

    public List<Recommendation>getRecommendations(String s){
        List<Recommendation>ans=new ArrayList<>();
        build(ans, "", getEnd(s));
        Collections.sort(ans);
        return ans;
    }

    public void select(String s){
        getEnd(s).incr();
    }

    public void build(List<Recommendation>ans,String s,Node root){
        if(root==null){
            return;
        }
        if(root.isEnd()){
            ans.add(new Recommendation(s, root.getCount()));
        }
        for(char ch='a';ch<='z';ch++) {
            if(root.hasNodeFor(ch)){
                build(ans, s+ch, root.getNodeFor(ch));
            }
        }
    }

}   