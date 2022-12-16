package org.webp.ejb;

import org.webp.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class AuthorBean {

    @PersistenceContext
    private EntityManager em;
	
    public AuthorBean(){}

    // Author Service
    public Long createAuthor(String name, Date birthDate){

        Author author = new Author();
        author.setName(name);
        author.setBirthDate(birthDate);

        em.persist(author);

        return author.getId();
    }

    public List<Author> getAllAuthors(boolean withBooks){

        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        List<Author> authors = query.getResultList();
        if(authors.size() == 0){
            throw new IllegalArgumentException("There is no author in database");
        }

        if(withBooks){
            //force loading
            authors.forEach(a -> a.getBooks().size());
        }

        return authors;
    }

    public Author getAuthor(long id, boolean withBooks){


        Author author = em.find(Author.class, id);
        if(author == null){
            throw new IllegalArgumentException("Author with id "+id+" does not exist");
        }

        if(withBooks){
            author.getBooks().size();
        }

        return author;
    }

}
