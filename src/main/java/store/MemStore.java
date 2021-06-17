package store;

import model.Candidate;
import model.City;
import model.Post;
import model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Integer, City> cities = new ConcurrentHashMap<>();

    private static final AtomicInteger POSTID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATEID = new AtomicInteger(4);

    private final Map<String, User> users = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "", new Timestamp(System.currentTimeMillis())));
        posts.put(2, new Post(2, "Middle Java Job", "", new Timestamp(System.currentTimeMillis())));
        posts.put(3, new Post(3, "Senior Java Job", "", new Timestamp(System.currentTimeMillis())));
        candidates.put(1, new Candidate(1, "Junior Java", 3, "Екатеринбург"));
        candidates.put(2, new Candidate(2, "Middle Java",2, "Воронеж"));
        candidates.put(3, new Candidate(3, "Senior Java", 1, "Волгоград"));
        cities.put(1, new City(1, "Волгоград"));
        cities.put(2, new City(2, "Воронеж"));
        cities.put(3, new City(3, "Екатеринбург"));
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

    @Override
    public void deleteCandidate(int id) {
        candidates.remove(id);
    }

    @Override
    public void saveUser(User user) {
        users.putIfAbsent(user.getName(), user);
    }

    @Override
    public User findUserByEmail(String email) {
        User result = null;
        List<User> list = new ArrayList<>(users.values());
        for (User user : list) {
            if (user.getEmail().equals(email)) {
                result = user;
                break;
            }
        }
        return result;
    }

    @Override
    public User findUserByName(String name) {
        User result = null;
        List<User> list = new ArrayList<>(users.values());
        for (User user : list) {
            if (user.getName().equals(name)) {
                result = user;
                break;
            }
        }
        return result;
    }

    @Override
    public Collection<City> findAllCities() {
        return cities.values();
    }

    @Override
    public City findCityById(int id) {
        return cities.get(id);
    }

}
