package org.webp.ejb;

import org.webp.entity.*;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class LibraryEjb {

    @PersistenceContext
    private EntityManager em;

    public Long createBook(String name, long authorId, long sectionId){

        Author author = getAuthor(authorId, true);
        Section section = getSection(sectionId, true);

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
        if(book == null){
            throw new IllegalArgumentException("Book with id "+id+" does not exist");
        }

        return book;
    }

    // Section Service
    public Long createSection(String name){

        Section section = new Section();
        section.setName(name);

        em.persist(section);

        return section.getId();
    }

    public List<Section> getAllSections(boolean withBooks){

        TypedQuery<Section> query = em.createQuery("select s from Section s", Section.class);
        List<Section> sections = query.getResultList();
        if(sections.size() == 0){
            throw new IllegalArgumentException("There is no section in database");
        }

        if(withBooks){
            //force loading
            sections.forEach(s -> s.getBooks().size());
        }

        return sections;
    }

    public Section getSection(long id, boolean withBooks){

        Section section = em.find(Section.class, id);
        if(section == null){
            throw new IllegalArgumentException("Section with id "+id+" does not exist");
        }

        if(withBooks){
            section.getBooks().size();
        }

        return section;
    }

    // Author Service
    public Long createAuthor(String name){

        Author author = new Author();
        author.setName(name);

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

    // Member Service
    public Long createMember(String name){

        Member member = new Member();
        member.setName(name);

        em.persist(member);

        return member.getId();
    }

    public List<Member> getAllMembers(){
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> members = query.getResultList();
        if(members.size() == 0){
            throw new IllegalArgumentException("There is no member in database");
        }

        return members;
    }

    public Member getMember(long id){
        Member member = em.find(Member.class, id);
        if(member == null){
            throw new IllegalArgumentException("Member with id "+id+" does not exist");
        }

        return member;
    }

    public void borrowBook(long memberId, long bookId){
        Book book = getBook(bookId);
        Member member = getMember(memberId);

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
