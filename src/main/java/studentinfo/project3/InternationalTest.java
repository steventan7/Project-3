package studentinfo.project3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class contains methods that test the tuitionDue() method of the International class
 *
 * @author Steven Tan, David Fabian
 */
public class InternationalTest {

    /**
     * Tests whether the student is studying abroad
     * Should assert true because the test international student is studying abroad
     */
    @Test
    public void isStudyAbroad() {
        International student = new International(new Profile("Steven", "Tan", "12/30/2002"), Major.CS, 32, true);
        assertEquals(student.tuitionDue(12), 5918.00, 0.0);
    }

    /**
     * Tests whether the student is studying abroad
     * Should assert false because the test international student is not studying abroad
     */
    @Test
    public void isNotStudyAbroad() {
        International student = new International(new Profile("Steven", "Tan", "12/30/2002"), Major.CS, 32, false);
        assertNotEquals(student.tuitionDue(12), 5918,0.0);
    }
}