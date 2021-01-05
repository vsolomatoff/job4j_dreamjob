package store;

import model.Candidate;
import model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();

        store.save(new Post(0, "Java Job"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }

        store.save(new Candidate(0, "Java Candidate"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }

        System.out.println("    store.findByPostId(1) = " + store.findByPostId(1));
        System.out.println("    store.findByCandidateId(1) = " + store.findByCandidateId(1));
    }
}