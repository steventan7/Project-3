package studentinfo.project3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the isValid() method of the Date class
 *
 * @author David Fabian, Steven Tan
 */
public class DateTest {

    /**
     * Tests whether the year is a leap year
     * Method should return false for leap day (February 29th) since the year is not a leap year
     */
    @Test
    public void isNotLeapDay() {
        Date date = new Date("2/29/2003");
        assertFalse(date.isValid());
    }

    /**
     * Tests whether the month actually has a 31st day
     * Method should return false since the day is the 31st while the month does not have 31 days in it.
     */
    @Test
    public void doesNotHaveAThirtyFirst() {
        Date date = new Date("4/31/2003");
        assertFalse(date.isValid());
    }

    /**
     * Tests whether the month is a valid month
     * Return false because, specifically the month, is not within the valid range as a positive integer less than or
     * equal to 12.
     */
    @Test
    public void monthRangeIsNotValid() {
        Date date = new Date("13/31/2000");
        assertFalse(date.isValid());
    }


    /**
     * Tests whether the day is a valid day
     * Return false because specifically the day is not within the valid range as a positive integer less than
     * or equal to 30 or 31, depending on the month.
     */
    @Test
    public void dayIsNotValid() {
        Date date = new Date("4/32/2003");
        assertFalse(date.isValid());
    }

    /**
     * Tests whether the day is a valid day
     * Return false because instance variables all must be within their corresponding ranges. For months, it is
     * between 1 and 12 inclusive.
     */
    @Test
    public void monthIsNotPositive() {
        Date date = new Date("-1/31/2003");
        assertFalse(date.isValid());
    }


    /**
     * Tests whether the year is a valid leap year
     * The method validates a date and return true if the date is leap day (2/29) and the year is divisible by 4
     * but not 100.
     */
    @Test
    public void yearIsLeapYear() {
        Date date = new Date("2/29/2004");
        assertTrue(date.isValid());
    }

    /**
     * Tests whether the month has 31 days
     * The method should return true if the day is the 31st only when the month has 31 days.
     */
    @Test
    public void monthHasThirtyOneDays() {
        Date date = new Date("3/31/1990");
        assertTrue(date.isValid());
    }
}

