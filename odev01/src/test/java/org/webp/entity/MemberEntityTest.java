package org.webp.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberEntityTest extends EntityTestBase {

    @Test
    public void testValidName(){

        //error due to null name
        assertFalse(persistInATransaction(createMember(null)));

        //error due to blank name
        assertFalse(persistInATransaction(createMember("")));

        //error due to long name
        assertFalse(persistInATransaction(createMember(longName(129))));

        //no error
        assertTrue(persistInATransaction(createMember("foo")));

    }

    private boolean borrowBook(Member member, Book book){


        if(book.getBorrower() != null || book.getBorrowedUntil() != null){
            System.out.println("The book - "+book.getName()+" ("+book.getId()+") can not be borrowed!");
            return false;
        }

        if(member.getBorrowed() != null){
            System.out.println("The member - "+member.getName()+" ("+member.getId()+") has already borrowed a book named - "+member.getBorrowed().getName()+". Read that first :)");
            return false;
        }

        book.setBorrower(member);
        Date twoWeeksAfter = new Date(new Date().getTime() + 24*60*60*1000*14);
        book.setBorrowedUntil(twoWeeksAfter);
        persistInATransaction(book);

        member.setBorrowed(book);

        persistInATransaction(member);
        return true;

    }

    @Test
    public void testBorrowingBook(){

        Member foo = createMember("foo");
        Member bar = createMember("bar");

        Author a = createAuthor("a", past);
        Section s = createSection("s");

        Book bA = createBook("bA", a, s);
        Book bB = createBook("bB", a, s);


        //no error
        assertTrue(borrowBook(foo,bA));

        //error - member has already borrowed another book
        assertFalse(borrowBook(foo,bB));

        //error - can not borrow an already borrowed book
        assertFalse(borrowBook(bar, bA));

        //no error
        assertTrue(borrowBook(bar, bB));


    }

}