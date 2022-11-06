package org.webp.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorEntityTest extends EntityTestBase {

    @Test
    public void testValidName(){

        //error due to null name
        assertFalse(persistInATransaction(createAuthor(null, past)));

        //error due to blank name
        assertFalse(persistInATransaction(createAuthor("", past)));

        //error due to long name
        assertFalse(persistInATransaction(createAuthor(longName(129), past)));

        //no error
        assertTrue(persistInATransaction(createAuthor("foo", past)));

        //no error when duplicate names
        assertTrue(persistInATransaction(createAuthor("foo", new Date())));
    }

    @Test
    public void testValidBirthDate(){

        Date future = new Date(new Date().getTime() + 1000*60*60*24*7); // one week after

        assertFalse(persistInATransaction(createAuthor("foo", future)));
        assertTrue(persistInATransaction(createAuthor("foo", new Date())));
        assertTrue(persistInATransaction(createAuthor("foo", past)));

    }

    @Test
    public void testBio(){

        //no error with null bio
        assertTrue(persistInATransaction(createAuthor("a", past)));
        //no error with blank bio
        assertTrue(persistInATransaction(createAuthor("a", past, "")));
        //no error between 0-1024 chars
        assertTrue(persistInATransaction(createAuthor("a", past, longName(1024))));
        //error when more than 1024 chars
        assertFalse(persistInATransaction(createAuthor("a", past, longName(1025))));

    }

}