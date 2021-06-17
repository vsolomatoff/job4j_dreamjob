package store;

import model.Candidate;
import model.City;
import model.Post;
import model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private static final Logger LOGGER = LogManager.getLogger(PsqlStore.class.getName());

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        String nameFile = "db.properties";
        ClassLoader classLoader = getClass().getClassLoader();
        URL urlDbProperties = classLoader.getResource("db.properties");
        if (urlDbProperties != null) {
            nameFile = urlDbProperties.getFile();
        } else {
            LOGGER.error("File db.properties not found!");
        }
        File file = new File(nameFile);

        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader(file))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post ORDER BY id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"), it.getString("description"), it.getTimestamp("created")));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        System.out.println("Started PsqlStore.findAllCandidates");
        List<Candidate> allCandidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT c.id, c.name, c.city_id, c1.name AS city FROM candidate c JOIN city c1 ON c.city_id = c1.id")
        ) {
            try (ResultSet it = statement.executeQuery()) {
                while (it.next()) {
                    allCandidates.add(new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getInt("city_id"),
                            it.getString("city")
                    ));
                }
            }
        } catch (SQLException se) {
            LOGGER.error(se.toString(), se);
        }
        System.out.println("Finished PsqlStore.findAllCandidates");
        return allCandidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, description, created) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return post;
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "INSERT INTO candidate (name, city_id, city) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getCityId());
            statement.setString(3, candidate.getCity());
            statement.execute();
            try (ResultSet id = statement.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException s) {
            LOGGER.error(s.toString(), s);
        }
        return candidate;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = ?, description = ? WHERE id = ?")
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ?, city_id = ?, city = ? WHERE id = ?")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setString(3, candidate.getCity());
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public Post findByPostId(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = new Post(it.getInt("id"), it.getString("name"), it.getString("description"), it.getTimestamp("created"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return post;
    }

    @Override
    public Candidate findByCandidateId(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = new Candidate(it.getInt("id")
                            , it.getString("name")
                            , it.getInt("city_id")
                            , it.getString("city"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return candidate;
    }

    @Override
    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "DELETE FROM candidate WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void saveUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "INSERT INTO user_dreamjob (name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.execute();
            try (ResultSet id = statement.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (SQLException se) {
            LOGGER.error(se.toString(), se);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM user_dreamjob WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException se) {
            LOGGER.error(se.toString(), se);
        }
        return user;
    }

    @Override
    public User findUserByName(String name) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM user_dreamjob WHERE name = ?")) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException se) {
            LOGGER.error(se.toString(), se);
        }
        return user;
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> allCities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement("SELECT * FROM city")) {
            try (ResultSet it = statement.executeQuery()) {
                while (it.next()) {
                    allCities.add(new City(
                            it.getInt("id"),
                            it.getString("name")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString(), e);
        }
        return allCities;
    }

    @Override
    public City findCityById(int id) {
        City city = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "SELECT * FROM city WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    city = new City(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.toString(), e);
        }
        return city;
    }

}