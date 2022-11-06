package org.webp.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SectionEntityTest extends EntityTestBase {

    @Test
    public void testValidName(){

        //error due to null name
        assertFalse(persistInATransaction(createSection(null)));

        //error due to blank name
        assertFalse(persistInATransaction(createSection("")));

        //error due to long name
        assertFalse(persistInATransaction(createSection(longName(257))));

        //no error
        assertTrue(persistInATransaction(createSection("foo")));

    }

    @Test
    public void testUniqueSection(){

        String name = "bar";
        assertTrue(persistInATransaction(createSection(name)));
        assertFalse(persistInATransaction(createSection(name)));

    }
}