package org.webp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.webp.ejb.*;
import org.webp.entity.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class EJBInJEEContainerTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(AuthorBean.class, BookBean.class, MemberBean.class, SectionBean.class, Author.class, Book.class, Member.class, Section.class)
                //sınıfların yanında kaynak da eklenir
                .addAsResource("META-INF/persistence.xml");
    }

    private final Date past = new Date(new Date().getTime() - 1000*60*60*24*7); // one week age


    @EJB
    private AuthorBean authorBean;
    @EJB
    private BookBean bookBean;
    @EJB
    private MemberBean memberBean;
    @EJB
    private SectionBean sectionBean;

    @Test
    public void testCreateEntity(){

        String aName = "author name";
        String mName = "member name";
        String sName = "section name";
        String bName = "book name";

        // no entries
        assertThrows(EJBException.class, ()-> authorBean.getAllAuthors(false));
        assertThrows(EJBException.class, ()-> memberBean.getAllMembers());
        assertThrows(EJBException.class, ()-> sectionBean.getAllSections(false));
        assertThrows(EJBException.class, ()-> bookBean.getAllBooks());

        // create entities
        Long aID = authorBean.createAuthor(aName, past);
        Long mID = memberBean.createMember(mName);
        Long sID = sectionBean.createSection(sName);
        Long bID = bookBean.createBook(bName, aID, sID);

        // trying to create section with same name
        assertThrows(EJBException.class, ()-> sectionBean.createSection(sName));
		
		// author is not available for creating book
        assertThrows(EJBException.class, ()-> bookBean.createBook(bName, 9999, sID));
	
		// section is not available for creating book
        assertThrows(EJBException.class, ()-> bookBean.createBook(bName, aID, 9999));


        assertEquals(1, authorBean.getAllAuthors(false).size());
        assertEquals(1, memberBean.getAllMembers().size());
        assertEquals(1, sectionBean.getAllSections(false).size());
        assertEquals(1, bookBean.getAllBooks().size());

        assertEquals(aName, authorBean.getAuthor(aID, false).getName());
        assertEquals(mName, memberBean.getMember(mID).getName());
        assertEquals(sName, sectionBean.getSection(sID, false).getName());
        assertEquals(bName, bookBean.getBook(bID).getName());





    }

    @Test
    public void testBorrowBook(){


        String aName = "author name for testing borrow book";
        String sName = "section name for testing borrow book";


        String m1Name = "member 1";
        String m2Name = "member 2";
        String b1Name = "book 1";
        String b2Name = "book 2";


        Long sID = sectionBean.createSection(sName);
        Long aID = authorBean.createAuthor(aName, past);

        // create books
        Long b1ID = bookBean.createBook(b1Name, aID, sID);
        Long b2ID = bookBean.createBook(b2Name, aID, sID);

        // create members
        Long m1ID = memberBean.createMember(m1Name);
        Long m2ID = memberBean.createMember(m2Name);


        // there are no borrowed books
        assertNull(bookBean.getBook(b1ID).getBorrower());
        assertNull(bookBean.getBook(b2ID).getBorrower());
        assertNull(memberBean.getMember(m1ID).getBorrowed());
        assertNull(memberBean.getMember(m2ID).getBorrowed());

        // "member 1" borrows "book 1"
        bookBean.borrowBook(m1ID, b1ID);

        // no member found with this ID
        assertThrows(EJBException.class, ()-> bookBean.borrowBook(9999, b2ID));
        // no book found with this ID
        assertThrows(EJBException.class, ()-> bookBean.borrowBook(m2ID, 9999));
        // "book 1" has already been borrowed
        assertThrows(EJBException.class, ()-> bookBean.borrowBook(m2ID, b1ID));
        // "member 1" has already borrowed a book
        assertThrows(EJBException.class, ()-> bookBean.borrowBook(m1ID, b2ID));

        // proof that "member 1" has borrowed "book 1"
        assertEquals(b1Name, memberBean.getMember(m1ID).getBorrowed().getName());
        assertEquals(m1Name, bookBean.getBook(b1ID).getBorrower().getName());

    }

}
