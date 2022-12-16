package org.webp.ejb;

import org.webp.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

@Stateless
public class BookBean {

    @PersistenceContext
    private EntityManager em;
	
    public BookBean(){}
    @EJB
    private AuthorBean authorBean;
    @EJB
    private SectionBean sectionBean;
    @EJB
    private MemberBean memberBean;

    public Long createBook(String name, long authorId, long sectionId){


        Author author = authorBean.getAuthor(authorId, true);
        Section section = sectionBean.getSection(sectionId, true);


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

    public List<Book> getAllBooks(){

        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        List<Book> books = query.getResultList();
        if(books.size() == 0){
            throw new IllegalArgumentException("There is no book in database");
        }

        return books;
    }

    public Book getBook(long id){
        Book book = em.find(Book.class, id);
        if(book == null){
            throw new IllegalArgumentException("Book with id "+id+" does not exist");
        }

        return book;
    }

    public void borrowBook(long memberId, long bookId){
        Book book = getBook(bookId);
        Member member = memberBean.getMember(memberId);

        if(book.getBorrower() != null){
            throw new IllegalArgumentException("The book - "+book.getName()+" ("+bookId+") can not be borrowed!");
        }

        if(member.getBorrowed() != null){
            throw new IllegalArgumentException("The member - "+member.getName()+" ("+member.getId()+") has already borrowed a book named - "+member.getBorrowed().getName()+". Read that book first :)");
        }

        book.setBorrower(member);
        Date twoWeeksAfter = new Date(new Date().getTime() + 24*60*60*1000*14);
        book.setBorrowedUntil(twoWeeksAfter);
        em.persist(book);

        member.setBorrowed(book);

        em.persist(member);

    }

}
