package org.webp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorEntityTest extends EntityTestBase {

    @Test
    public void testTooLongName(){

        String name = new String(new char[129]);

        //error due to long name
        Author author1 = new Author();
        author1.setName(name);
        assertFalse(persistInATransaction(author1));

        //error due to null name
        Author author2 = new Author();
        assertFalse(persistInATransaction(author2));

        //no error
        Author author3 = new Author();
        author3.setName("foo");
        assertTrue(persistInATransaction(author3));
    }

    @Test
    public void testUniqueAuthor(){

        String name = "bar";

        Author author = new Author();
        author.setName(name);

        assertTrue(persistInATransaction(author));

        Author another = new Author();
        another.setName(name);

        assertFalse(persistInATransaction(another));
    }
}