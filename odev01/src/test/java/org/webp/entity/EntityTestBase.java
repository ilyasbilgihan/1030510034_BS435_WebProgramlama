package org.webp.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Date;

public abstract class EntityTestBase {

    private EntityManagerFactory factory;
    protected EntityManager em;

    protected Date past = new Date(new Date().getTime() - 1000*60*60*24*7); // one week age

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("Hibernate");
        em = factory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        factory.close();
    }

    protected boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }

    protected String longName(int size){
        String []arr = new String [size];
        Arrays.fill(arr, "x");
        return String.join("", arr);
    }

    protected Book createBook(String name, Author author, Section section){


        Book book = new Book();
        book.setName(name);
        book.setSection(section);
        book.setAuthor(author);

        persistInATransaction(book);

        section.getBooks().add(book);
        author.getBooks().add(book);

        persistInATransaction(section, author);
        return book;

    }

    protected Author createAuthor(String name, Date birthDate){
        Author author = new Author();

        author.setName(name);
        author.setBirthDate(birthDate);

        return author;

    }

    protected Author createAuthor(String name, Date birthDate, String bio){
        Author author = createAuthor(name, birthDate);
        author.setBio(bio);
        return author;
    }

    protected Section createSection(String name){
        Section section = new Section();
        section.setName(name);
        return section;
    }

    protected Member createMember(String name){
        Member member = new Member();
        member.setName(name);
        return member;
    }
}
