package dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class _04_search_engine {
    public static void main(String[] args) {
        DocumentStore data=new DocumentStore(List.of(
            new Document(1, "Title1", "A fox jumps around the wall"),
            new Document(2, "Title2", "My cat simply roams around my dog, which also roams around me"),
            new Document(3, "Title3", "Earth rotates around the sun")
        ));

        SearchEngine se=new SearchEngine(data, new SearchByFrequencyStrategy());

        System.out.println(se.getDocumentsWithWord("around"));

    }
}

class Document {
    private final int id;
    private final String title;
    private final String content;
    private final HashMap<String,Integer>wordFrequency;

    public String getContent() {
        return content;
    }

    public Document(int id, String title, String content) {
        this.title = title;
        this.id = id;
        this.content = content;
        wordFrequency=new HashMap<>();
        for(String s:getWordsInDocument()) wordFrequency.put(s, wordFrequency.getOrDefault(s, 0)+1);
    }

    public int getFrequencyOf(String word){
        return wordFrequency.getOrDefault(word, 0);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getWordsInDocument(){
        return List.of(getContent().split(" ")).stream().map(e->e.toLowerCase()).toList();
    }

    @Override
    public String toString() {
        return "[" + id + "," + title + ","+content+"]";
    }
}

class DocumentStore {
    private final HashMap<Integer, Document> myDocuments;

    public DocumentStore(List<Document> documents) {
        myDocuments = new HashMap<>();
        for (Document e : documents)
            addDocument(e);
    }

    public List<Document> getAllDocuments(){
        return myDocuments.values().stream().toList();
    }
    public synchronized void addDocument(Document doc) {
        myDocuments.put(doc.getId(), doc);
    }

    public List<Document> getDocumentsWithIds(List<Integer> ids) {
        List<Document> ans = new ArrayList<>();
        for (int i : ids)
            ans.add(myDocuments.get(i));
        return ans;
    }

   

}

interface SearchStrategy {
    List<Document> searchDocuments(String word, HashMap<String, List<Integer>> wordDocumentId,
            DocumentStore documentStore);
}

class SearchByFrequencyStrategy implements SearchStrategy {

    @Override
    public List<Document> searchDocuments(String word, HashMap<String, List<Integer>> wordDocumentId,
            DocumentStore documentStore) {
        List<Integer> ids = wordDocumentId.get(word);
        List<Document>ans=documentStore.getDocumentsWithIds(ids);
        Collections.sort(ans,(x,y)->y.getFrequencyOf(word)-x.getFrequencyOf(word));
        return ans;
    }

}

class SearchEngine {
    private final DocumentStore documentStore;
    private final SearchStrategy strategy;
    private final HashMap<String, List<Integer>> wordDocumentId;

    public SearchEngine(DocumentStore documentStore, SearchStrategy strategy) {
        this.documentStore = documentStore;
        this.strategy = strategy;
        wordDocumentId = new HashMap<>();
        List<Document>docs=documentStore.getAllDocuments();
        for(Document d:docs){
            for(String word:d.getWordsInDocument()) wordDocumentId.computeIfAbsent(word, e->new ArrayList<>()).add(d.getId());
        }

    }

    public List<Document> getDocumentsWithWord(String word) {
        return strategy.searchDocuments(word, wordDocumentId, documentStore);
    }

}