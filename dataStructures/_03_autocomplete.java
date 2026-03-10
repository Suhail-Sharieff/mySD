package dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class _03_autocomplete {

    public static void main(String[] args) {


        List<String>data=List.of("app","apple","application","apply");

        AutoCommpleteService service=new AutoCommpleteService(new FrequencyRecommendationStrategy());

        for(String str:data) service.insert(str);


        System.out.println(service.getRecommendations("app",2));

        System.out.println(service.getRecommendations("apple",2));

        service.insert("appa");

        System.out.println(service.getRecommendations("app", 10));





       /*[[app, searches=1], [apple, searches=1]]
        [[app, searches=2], [apple, searches=2]]
        [[app, searches=3], [apple, searches=3], [application, searches=3], [apply, searches=3], [appa, searches=1]]*/




    }
}

class Recommendation {
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
    private int frequency;

    public int getFrequency() {
        return frequency;
    }
    public Node(char val) {
        this.val=val;
        this.ptrs=new Node[26];
        this.frequency=0;
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
        frequency++;
    }
    @Override
    public String toString() {
        return "{"+val+"=>"+frequency+"}";
    }
}

class Trie{
    private final Node root;

    public Node getRoot() {
        return root;
    }


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
}   


class AutoCommpleteService{
    private final Trie trie;
    private final RecommendationStrategy strategy;
    public AutoCommpleteService(RecommendationStrategy strategy) {
        trie=new Trie();
        this.strategy=strategy;
    }
    public void insert(String s){
        trie.insert(s);
    }
    public List<Recommendation> getRecommendations(String s,int limit){
        return strategy.getRecommendation(s, trie, limit);
    }

}


interface RecommendationStrategy{
    List<Recommendation>getRecommendation(String word,Trie trie,int limit);//can use PQ for better optimizations too
}

class FrequencyRecommendationStrategy implements RecommendationStrategy{//infuture can extend to MLStrategy,UserStrategy etc

    @Override
    public List<Recommendation> getRecommendation(String word, Trie trie,int limit) {
        List<Recommendation>ans=new ArrayList<>();
        collectRecommendations(ans, "", trie.getRoot());
        Collections.sort(ans,(o1, o2) ->   o2.getCount()-o1.getCount());
        return ans.subList(0, Math.min(ans.size(), limit));
    }

     

    
    private void collectRecommendations(List<Recommendation>ans,String s,Node root){
        if(root==null){
            return;
        }
        if(root.isEnd()){
            root.incr();//increment frequency of search of this word
            ans.add(new Recommendation(s, root.getFrequency()));
            //MISTAKE: added `return` here
        }
        for(char ch='a';ch<='z';ch++) {
            if(root.hasNodeFor(ch)){
                collectRecommendations(ans, s+ch, root.getNodeFor(ch));
            }
        }
    }

    
}