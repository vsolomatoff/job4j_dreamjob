package store;

import model.Candidate;
import model.Post;

import java.sql.Timestamp;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();

        store.save(new Post(0, "Java Job", "Description", new Timestamp(System.currentTimeMillis())));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        store.save(new Candidate(0, "Java Candidate", 1, "Волгоград"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }

        System.out.println("    store.findByPostId(1) = " + store.findByPostId(1));
        System.out.println("    store.findByCandidateId(1) = " + store.findByCandidateId(1));
    }
}