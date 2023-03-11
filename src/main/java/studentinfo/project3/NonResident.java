package studentinfo.project3;

/**
 * This class contains methods that calculate a nonresident student's tuition
 *
 * @author Steven Tan, David Fabian
 */
public class NonResident extends Student{

    /**
     * Constructs a NonResident instance for a nonresident student.
     * Holds the student's profile, major, and creditCompleted
     *
     * @param profile         the student's profile
     * @param major           the student's major
     * @param creditCompleted number of credits completed by student
     */
    public NonResident(Profile profile, Major major, int creditCompleted) {
        super(profile, major, creditCompleted);
    }

    /**
     * Processes the student's tuition as non-resident student with regard to whether they are a
     * full-time or part student and the number of credits they are taking
     * @param creditsEnrolled number of credits the student is enrolled in
     * @return the student's calculated tuition
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if(creditsEnrolled < MINCREDITS) {
            return PTNONRESIDENTTUITION * creditsEnrolled + 0.8 * UNIFEE;
        } else {
            double tuition = FTNONRESIDENTTUITION + UNIFEE;
            if (creditsEnrolled > ADDITIONALCREDITHOURS) {
                tuition += PTNONRESIDENTTUITION * (creditsEnrolled - ADDITIONALCREDITHOURS);
            }
            return tuition;
        }
    }

    /**
     * Checks whether the current class is a student resident
     * @return false since the student is not a resident
     */
    @Override
    public boolean isResident() {
        return false;
    }

    /**
     * Creates a string containing the first, last name, DOB, student's major, student's standing, resident info
     * String formatted as "FirstName LastName DOB (MajorCode, MAJOR, SCHOOLNAME) Student-Standing "
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        return studentProfile() + " " + major() + " credits completed: " + creditsCompleted()  +
                " (" + studentStanding() + ")" + "(non-resident)";
    }
}
