package store;

import model.Candidate;
import model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    Post findByPostId(int id);

    Candidate findByCandidateId(int id);

    void deleteCandidate(int id);
}
