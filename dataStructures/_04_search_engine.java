package dataStructures;

import java.util.*;

public class _04_search_engine {
    public static void main(String[] args) {
        SearchEngine se = new SearchEngine(new SearchByFrequencyStrategy());

        se.addDocument(new Document(1, "Title1", "A fox jumps around the wall."));
        se.addDocument(new Document(2, "Title2", "My cat simply roams around my dog, which also roams around me!"));
        se.addDocument(new Document(3, "Title3", "Earth rotates around the sun."));

        System.out.println("--- Searching for 'around' ---");
        se.search("around").forEach(System.out::println);

        System.out.println("\n--- Searching for 'roams' ---");
        se.search("roams").forEach(System.out::println);
    }
}

// 1. DUMB DOCUMENT: Just holds data. No logic, no frequency maps.
class Document {
    private final int id;
    private final String title;
    private final String content;

    public Document(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }

    @Override
    public String toString() {
        return "[" + id + ", " + title + ", " + content + "]";
    }
}

// 2. DATA STORE: Just retrieves documents by ID in O(1) time.
class DocumentStore {
    private final Map<Integer, Document> store = new HashMap<>();

    public void addDocument(Document doc) {
        store.put(doc.getId(), doc);
    }

    public Document getDocument(int id) {
        return store.get(id);
    }
}

// 3. POSTING: Tracks the Document ID, Frequency, AND Exact Positions!
class Posting {
    private final int documentId;
    private final List<Integer> positions;

    public Posting(int documentId) {
        this.documentId = documentId;
        this.positions = new ArrayList<>();
    }

    public void addPosition(int position) {
        positions.add(position);
    }

    public int getDocumentId() { return documentId; }
    
    // Frequency is simply the number of times it appeared (size of positions list)
    public int getFrequency() { return positions.size(); }
    
    public List<Integer> getPositions() { return positions; }
}

// 4. SMART INDEX: Maps Word -> (DocumentID -> Posting)
class InvertedIndex {
    // Outer Map: Word. Inner Map: Document ID -> Posting details
    private final Map<String, Map<Integer, Posting>> index = new HashMap<>();

    public void addDocument(Document doc) {
        // Split by non-word characters to strip punctuation
        String[] words = doc.getContent().split("\\W+");
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (word.isBlank()) continue;

            // 1. Find the map for this word (create if missing)
            // 2. Find the Posting for this docId (create if missing)
            // 3. Add the exact index position to the posting
            index.computeIfAbsent(word, k -> new HashMap<>())
                 .computeIfAbsent(doc.getId(), id -> new Posting(id))
                 .addPosition(i);
        }
    }

    // Returns all Postings for a specific word in O(1) time
    public Collection<Posting> getPostingsFor(String word) {
        Map<Integer, Posting> docMap = index.get(word.toLowerCase());
        return docMap != null ? docMap.values() : Collections.emptyList();
    }
}

// 5. STRATEGY: Takes the Postings, sorts them, and fetches the Documents
interface SearchStrategy {
    List<Document> search(String word, InvertedIndex index, DocumentStore store);
}

class SearchByFrequencyStrategy implements SearchStrategy {
    @Override
    public List<Document> search(String word, InvertedIndex index, DocumentStore store) {
        return index.getPostingsFor(word).stream()
                // Sort Postings descending by frequency
                .sorted((p1, p2) -> Integer.compare(p2.getFrequency(), p1.getFrequency()))
                // Map the Posting's Document ID back to the actual Document payload
                .map(posting -> store.getDocument(posting.getDocumentId()))
                .toList();
    }
}

// 6. ENGINE: Orchestrates the flow
class SearchEngine {
    private final DocumentStore store;
    private final InvertedIndex index;
    private final SearchStrategy strategy;

    public SearchEngine(SearchStrategy strategy) {
        this.store = new DocumentStore();
        this.index = new InvertedIndex();
        this.strategy = strategy;
    }

    public void addDocument(Document doc) {
        store.addDocument(doc);
        index.addDocument(doc);
    }

    public List<Document> search(String word) {
        return strategy.search(word, index, store);
    }
}