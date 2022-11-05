package org.webp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SectionEntityTest extends EntityTestBase {

    @Test
    public void testTooLongName(){

        String name = new String(new char[129]);

        //error due to long name
        Section section1 = new Section();
        section1.setName(name);
        assertFalse(persistInATransaction(section1));

        //error due to null name
        Section section2 = new Section();
        assertFalse(persistInATransaction(section2));

        //no error
        Section section3 = new Section();
        section3.setName("foo");
        assertTrue(persistInATransaction(section3));
    }

    @Test
    public void testUniqueSection(){

        String name = "bar";

        Section section = new Section();
        section.setName(name);

        assertTrue(persistInATransaction(section));

        Section another = new Section();
        another.setName(name);

        assertFalse(persistInATransaction(another));
    }
}