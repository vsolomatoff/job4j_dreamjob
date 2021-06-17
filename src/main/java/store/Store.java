package store;

import model.Candidate;
import model.City;
import model.Post;
import model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    Post findByPostId(int id);

    Candidate findByCandidateId(int id);

    void deleteCandidate(int id);

    void saveUser(User user);

    User findUserByEmail(String email);

    User findUserByName(String email);

    Collection<City> findAllCities();

    City findCityById(int id);
}
