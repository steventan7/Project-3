package studentinfo.project3;
import java.util.Calendar;

/**
 * Date class used to create objects representing a date specifying a day, month, and year.
 * @author David Fabian, Steven Tan
 */
public class Date implements Comparable<Date> {
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int[] FULLMONTHS = new int[] {1, 3, 5, 7, 8, 10, 12};
    public static final int MAXDAYS = 31;
    public static final int MAXMONTHS = 12;
    public static final int FEBRUARY = 2;
    public static final int LEAPDAY = 29;
    private int year;
    private int month;
    private int day;

    /**
     * Creates a Date class representing the current date.
     */
    public Date () {
        Calendar currCalendarDate = Calendar.getInstance();
        this.year = currCalendarDate.get(Calendar.YEAR);
        this.month = currCalendarDate.get(Calendar.MONTH) + 1;
        this.day = currCalendarDate.get(Calendar.DATE);
    }
    /**
     * Constructs a Date instance using the formatted string as a date.
     * @param date String formatted as "mm/dd/yyyy."
     */
    public Date(String date) {
        String [] dateNum = date.split("/");
        this.year = Integer.parseInt(dateNum[2]);
        this.month = Integer.parseInt(dateNum[0]);
        this.day = Integer.parseInt(dateNum[1]);
    }

    /**
     * Forms a formatted string ("mm/dd/yyyy") of this Date instance.
     * @return a string formatted as above representing a date.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Compares this and the specified object.
     * The result is true if the object is a Date object
     * that represents the same calendar date as this Date instance.
     * @param obj object to compare this Date instance against.
     * @return true if the argument object is a Date object that is equivalent
     * to this Date; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Date) {
            Date otherDate = (Date) obj;
            return this.day == otherDate.day() &&
                    this.month == otherDate.month() &&
                    this.year == otherDate.year();
        }
        return false;
    }

    /**
     * Compares this and the specified Date object based on which is later.
     * The result is the difference between the years, months, OR days of the two Date objects,
     * whichever difference is computed first, if any, in the listed order.
     * @param otherDate the Date object to compare this Date against.
     * @return the difference between the Date instances as described above.
     * Returns 0 if the two Date objects are equivalent;
     * a value less than 0 if this Date is before the argument Date on the calendar;
     * a value greater than 0 if this Date is after the argument Date on the calendar.
     */
    @Override
    public int compareTo(Date otherDate) {
        int diff = this.year - otherDate.year();
        if(diff == 0) {
            diff = this.month - otherDate.month();
            if(diff == 0) {
                diff = this.day - otherDate.day();
            }
        }
        if(diff > 0) {
            return 1;
        } else if(diff < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * Validates whether this Date instance represents a valid date
     * that can be represented on a calendar.
     * @return true if the date represented is valid; false otherwise.
     */
    public boolean isValid() {
        boolean withinBounds = this.day > 0 && this.month > 0 &&
                this.day <= MAXDAYS && this.month <= MAXMONTHS;
        if(!withinBounds) {return false;}
        if(this.month == FEBRUARY) {
            return this.day < LEAPDAY || (this.day == LEAPDAY &&
                    this.year % QUADRENNIAL == 0 &&
                    (this.year % CENTENNIAL != 0 ||
                            this.year % QUATERCENTENNIAL == 0));
        }
        if(this.day < MAXDAYS) {
            return true;
        }
        for(int fullMonth : FULLMONTHS) {
            if(this.month == fullMonth) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the day of the month of this Date instance.
     * @return integer that represents this date's day of the month.
     */
    public int year() {
        return this.year;
    }

    /**
     * Gets the month of the year of this Date instance, from 1 to 12.
     * @return integer that represents this date's month of the year.
     */
    public int month() {
        return this.month;
    }

    /**
     * Gets the year of this Date instance.
     * @return integer that represents this date's year.
     */
    public int day() {
        return this.day;
    }
}
