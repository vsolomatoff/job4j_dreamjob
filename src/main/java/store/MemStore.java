package store;

import model.Candidate;
import model.Post;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private static final AtomicInteger POSTID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATEID = new AtomicInteger(4);

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "", new Timestamp(System.currentTimeMillis())));
        posts.put(2, new Post(2, "Middle Java Job", "", new Timestamp(System.currentTimeMillis())));
        posts.put(3, new Post(3, "Senior Java Job", "", new Timestamp(System.currentTimeMillis())));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore instOf() {
        return INST;
    }


    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POSTID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATEID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public Post findByPostId(int id) {
        return posts.get(id);
    }

    @Override
    public Candidate findByCandidateId(int id) {
        return candidates.get(id);
    }

}
