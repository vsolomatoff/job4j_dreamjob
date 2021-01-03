package store;

import model.Candidate;
import model.Post;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "", new Date()));
        posts.put(2, new Post(2, "Middle Java Job", "", new Date()));
        posts.put(3, new Post(3, "Senior Java Job", "", new Date()));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }
}
