package dataStructures;

import java.util.HashMap;

public class _01_LRU {
    public static void main(String[] args) {
        LinkedList lru=new LinkedList(2);
        lru.put(1, 1);
        System.out.println(lru);

        lru.put(2, 2);
        System.out.println(lru);

        System.out.println(lru.get(1));
        System.out.println(lru);


        // lru.put(3, 3);
        // System.out.println(lru);
    }
}





class Node{
    int key;
    int val;
    Node next;
    Node prev;
    Node(int key,int val){
        this.key=key;
        this.val=val;
    }
    @Override
    public String toString() {
        return "{"+key+","+val+"}";
    }
}

class LinkedList{
    Node head;
    Node tail;
    HashMap<Integer,Node>hs;
    int limit;
    public LinkedList(int limit) {
        head=null;
        tail=null;
        hs=new HashMap<>();
        this.limit=limit;
    }
    boolean containsKey(int key){
        return hs.containsKey(key);
    }

    void remove(Node curr){
       
        Node prev=curr.prev;
        Node next=curr.next;

        // p [bf] c [bf] n
        if(prev==null) head=next;
        else if(next==null) head=prev;
        else{
            prev.next=next;
            next.prev=prev;
            curr.next=curr.prev=null;
        }
    }


    void put(int key,int val){
        if(containsKey(key)){
            Node curr=hs.get(key);
            curr.val=val;
            remove(curr);
            addLast(curr);
        }else{
            if(full()) popFirst();  
            Node newNode=new Node(key,val);
            hs.put(key, newNode);
            addLast(newNode);
        }        
    }

    void popFirst(){
        hs.remove(head.key);
        head=head.next;
        head.prev=null;
    }

    void addLast(Node node){
        if(head==null) head=tail=node;
        else {
            tail.next=node;
            node.prev=tail;
            tail=tail.next;
        }
    }

    int get(int key){
        if(!containsKey(key)) return -1;
        Node curr=hs.get(key);
        remove(curr);
        addLast(curr);
        return curr.val;
    }
  
    boolean full(){
        return hs.size()==limit;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        Node ptr=head;
        while(ptr!=null){
            sb.append(ptr).append("->");
            ptr=ptr.next;
        }
        return sb.toString();
    }
}
