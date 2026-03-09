package dataStructures;

import java.util.HashMap;

/*
- Initillay i just wrote code for k,v of int type
- Messed up remove() function
- Fixed it
- think of now extending to genric datatype as well
- think of thread safety, just using synchronized is enough
- achie abstraction by making only get(K) and put(K,V) public and remaining private


*/

public class _01_LRU {
    public static void main(String[] args) {
        LRUCache<Integer,Integer> lru = new LRUCache<>(2);
        lru.put(1, 1);
        System.out.println(lru);

        lru.put(2, 2);
        System.out.println(lru);

        System.out.println(lru.get(1));
        System.out.println(lru);

        lru.put(3, 3);
        System.out.println(lru);

        System.out.println(lru.get(2));
        System.out.println(lru);

        lru.put(4, 4);
        System.out.println(lru);

        System.out.println(lru.get(1));
        System.out.println(lru);

        System.out.println(lru.get(3));
        System.out.println(lru);

        System.out.println(lru.get(4));
        System.out.println(lru);

    }

    static class LRUCache<K,V> {
        private class Node {
            K key;
            V val;
            Node next = null;
            Node prev = null;

            Node(K key, V val) {
                this.key = key;
                this.val = val;
            }

            @Override
            public String toString() {
                return "{" + key + "," + val + "}";
            }
        }

        private Node head;
        private Node tail;
        private HashMap<K, Node> hs;
        private final int limit;

        public LRUCache(int limit) {
            head = null;
            tail = null;
            hs = new HashMap<>();
            this.limit = limit;
        }

        boolean containsKey(K key) {
            return hs.containsKey(key);
        }

        private void remove(Node curr) {// DONT MESS THIS UP

            if (curr == head && head == tail) {
                head = tail = null;
            } else {
                if (curr == head) {
                    head = curr.next;
                    head.prev = null;
                } else if (curr == tail) {
                    tail = curr.prev;
                    tail.next = null;
                } else {
                    curr.prev.next = curr.next;
                    curr.next.prev = curr.prev;
                }
            }
            curr.next = curr.prev = null;// IMP
        }

        synchronized void put(K key, V val) {
            if (containsKey(key)) {
                Node curr = hs.get(key);
                curr.val = val;
                remove(curr);
                addLast(curr);
            } else {
                if (full())
                    popFirst();
                Node newNode = new Node(key, val);
                hs.put(key, newNode);
                addLast(newNode);
            }
        }

        private void popFirst() {
            hs.remove(head.key);
            if (head == tail) {
                head = tail = null;
            } else {
                Node next = head.next;
                head.next = null;
                next.prev = null;
                head = next;
            }
        }

        private void addLast(Node node) {
            if (head == null)
                head = tail = node;
            else {
                tail.next = node;
                node.prev = tail;
                tail = tail.next;
            }
        }

        synchronized V get(K key) {//thread safe
            if (!containsKey(key))
                return null;
            Node curr = hs.get(key);
            remove(curr);
            addLast(curr);
            return curr.val;
        }

        private boolean full() {
            return hs.size() == limit;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node ptr = head;
            while (ptr != null) {
                sb.append(ptr).append("->");
                ptr = ptr.next;
            }
            return sb.toString();
        }
    }

}
