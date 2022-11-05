package org.webp.ejb;

import org.webp.entity.Author;
import org.webp.entity.Book;
import org.webp.entity.Section;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class LibraryEjb {

    @PersistenceContext
    private EntityManager em;

    public Long createSection(String name){

        Section section = new Section();
        section.setName(name);

        em.persist(section);

        return section.getId();
    }

    public Long createAuthor(String name){

        Author author = new Author();
        author.setName(name);

        em.persist(author);

        return author.getId();
    }

    public Long createBook(long authorId, long sectionId, String name){

        Author author = em.find(Author.class, authorId);
        Section section = em.find(Section.class, sectionId);
        if(author == null){
            throw new IllegalArgumentException("Author with id "+authorId+" does not exist");
        }
        if(section == null){
            throw new IllegalArgumentException("Section with id "+sectionId+" does not exist");
        }

        Book book = new Book();
        book.setName(name);
        book.setSection(section);
        book.setAuthor(author);

        em.persist(book);

        section.getBooks().add(book);
        author.getBooks().add(book);

        em.persist(section);
        em.persist(author);

        return book.getId();
    }

    public Book getBook(long id){
        Book book = em.find(Book.class, id);

        return book;
    }


    public List<Section> getAllSections(boolean withBooks){

        TypedQuery<Section> query = em.createQuery("select s from Section s", Section.class);
        List<Section> sections = query.getResultList();

        if(withBooks){
            //force loading
            sections.forEach(s -> s.getBooks().size());
        }

        return sections;
    }

    public Section getSection(long id, boolean withBooks){

        Section section = em.find(Section.class, id);
        if(withBooks && section != null){
            section.getBooks().size();
        }

        return section;
    }

    public List<Author> getAllAuthors(boolean withBooks){

        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        List<Author> authors = query.getResultList();

        if(withBooks){
            //force loading
            authors.forEach(a -> a.getBooks().size());
        }

        return authors;
    }

    public Author getAuthor(long id, boolean withBooks){

        Author author = em.find(Author.class, id);
        if(withBooks && author != null){
            author.getBooks().size();
        }

        return author;
    }

}
