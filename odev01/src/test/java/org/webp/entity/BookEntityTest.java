package org.webp.entity;

import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookEntityTest extends EntityTestBase {


    @Test
    public void testBook(){

        Section section = createSection("Sci-Fi");
        assertTrue(persistInATransaction(section));

        Author author = createAuthor("Isac Asimov", past);
        assertTrue(persistInATransaction(author));


        //error due to missing section
        Book book1 = new Book();
        book1.setName("Foundation");
        book1.setAuthor(author);
        assertFalse(persistInATransaction(book1));


        //error due to missing author
        Book book2 = new Book();
        book2.setName("Foundation");
        book2.setSection(section);
        assertFalse(persistInATransaction(book2));


        //error due to long name
        Book book3 = new Book();
        book3.setName(longName(257));
        book3.setAuthor(author);
        book3.setSection(section);
        assertFalse(persistInATransaction(book3));


        //no error
        Book book4 = new Book();
        book4.setName("Foundation");
        book4.setAuthor(author);
        book4.setSection(section);
        assertTrue(persistInATransaction(book4));

    }

    @Test
    public void testQueries(){

        /*

        BOOKS
        ---------------------------------
        |   Name  |  Author  |  Section |
        ---------------------------------
        |   bA    |    aA    |    sA    |
        |   bB    |    aA    |    sA    |
        |   bC    |    aA    |    sB    |
        |   bD    |    aB    |    sB    |
        |   bE    |    aC    |    sB    |
        ---------------------------------

        */

        // Sections
        Section sA = createSection("sA");
        Section sB = createSection("sB");

        // Authors
        Author aA = createAuthor("aA", past);
        Author aB = createAuthor("aB", past);
        Author aC = createAuthor("aC", past);

        assertTrue(persistInATransaction(sA, sB, aA, aB, aC));

        // Books
        createBook("bA", aA, sA);
        createBook("bB", aA, sA);
        createBook("bC", aA, sB);
        createBook("bD", aB, sB);
        createBook("bE", aC, sB);


        // Query All books
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
        List<Book> books = query.getResultList();
        assertEquals(5, books.size());

        // Query books by author
        query = em.createQuery("SELECT b FROM Book b WHERE b.author.name = 'aA'", Book.class);
        books = query.getResultList();
        assertEquals(3, books.size());

        query = em.createQuery("SELECT b FROM Book b WHERE b.author.name = 'aB'", Book.class);
        books = query.getResultList();
        assertEquals(1, books.size());

        query = em.createQuery("SELECT b FROM Book b WHERE b.author.name = 'aC'", Book.class);
        books = query.getResultList();
        assertEquals(1, books.size());

        // Query books by section
        query = em.createQuery("SELECT b FROM Book b WHERE b.section.name = 'sA'", Book.class);
        books = query.getResultList();
        assertEquals(2, books.size());

        query = em.createQuery("SELECT b FROM Book b WHERE b.section.name = 'sB'", Book.class);
        books = query.getResultList();
        assertEquals(3, books.size());



        // Query All sections
        TypedQuery<Section> query2 = em.createQuery("SELECT s FROM Section s", Section.class);
        List<Section> sections = query2.getResultList();
        assertEquals(2, sections.size());

        // Query sections by section name
        query2 = em.createQuery("SELECT s FROM Section s WHERE s.name = 'sA'", Section.class);
        Section section = query2.getSingleResult();
        assertEquals(2, section.getBooks().size());

        query2 = em.createQuery("SELECT s FROM Section s WHERE s.name = 'sB'", Section.class);
        section = query2.getSingleResult();
        assertEquals(3, section.getBooks().size());



        // Query All authors
        TypedQuery<Author> query3 = em.createQuery("SELECT a FROM Author a", Author.class);
        List<Author> authors = query3.getResultList();
        assertEquals(3, authors.size());

        // Query authors by author name
        query3 = em.createQuery("SELECT a FROM Author a WHERE a.name = 'aA'", Author.class);
        Author author = query3.getSingleResult();
        assertEquals(3, author.getBooks().size());

        query3 = em.createQuery("SELECT a FROM Author a WHERE a.name = 'aB'", Author.class);
        author = query3.getSingleResult();
        assertEquals(1, author.getBooks().size());

        query3 = em.createQuery("SELECT a FROM Author a WHERE a.name = 'aC'", Author.class);
        author = query3.getSingleResult();
        assertEquals(1, author.getBooks().size());




    }


}