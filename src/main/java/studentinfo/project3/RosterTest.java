package studentinfo.project3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class contains methods that test the add() method of the Roster class
 *
 * @author Steven Tan, David Fabian
 */
class RosterTest {
    Roster roster = new Roster();
    International student1 = new International(new Profile("Steven", "Tan", "12/30/2002"), Major.CS, 32, false);
    TriState student2 = new TriState(new Profile("David", "Fabian", "12/20/2002"), Major.CS, 32, "CT");

    /**
     * Tests whether an international student has been added correctly
     * Should assert true the first time the student is being added
     * Should assert false the second time the same is being added
     */
    @Test
    public void addingAnInternationalStudent() {
        assertTrue(roster.add(student1));
        assertFalse(roster.add(student1));
    }

    /**
     * Tests whether a triState student has been added correctly
     * Should assert true the first time the student is being added
     * Should assert false the second time the same is being added
     */
    @Test
    public void addingATriStateStudent() {
        assertTrue(roster.add(student2));
        assertFalse(roster.add(student2));
    }

    /**
     * Tests whether a triState student has been added correctly
     * Should assert true the first time the student is being added
     * Should assert false the second time the same is being added
     */
    @Test
    public void removingAnInternationalStudent() {
        roster.add(student1);
        assertTrue(roster.remove(student1));
        International student4 = new International(new Profile("Steven", "Tan", "12/30/2002"), Major.CS, 32, false);
        assertFalse(roster.remove(student4));
    }

    /**
     * Tests whether a triState student has been added correctly
     * Should assert true the first time the student is being added
     * Should assert false the second time the same is being added
     */
    @Test
    public void removingATriStateStudent() {
        roster.add(student2);
        assertTrue(roster.remove(student2));
        TriState student3 = new TriState(new Profile("David", "Fabian", "12/20/2002"), Major.CS, 32, "CT");
        assertFalse(roster.remove(student3));
    }
}