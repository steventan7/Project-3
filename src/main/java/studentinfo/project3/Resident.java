package studentinfo.project3;

/**
 * This class contains methods that calculate a resident student's tuition
 *
 * @author Steven Tan, David Fabian
 */
public class Resident extends Student{

    private int scholarship;

    /**
     * Constructs a Resident instance for a resident student.
     * Holds the student's profile, major, creditCompleted, and scholarship amount
     *
     * @param profile         the student's profile
     * @param major           the student's major
     * @param creditCompleted number of credits completed by student
     * @param scholarship     the student's scholarship
     */
    public Resident(Profile profile, Major major, int creditCompleted, int scholarship) {
        super(profile, major, creditCompleted);
        this.scholarship = scholarship;
    }

    /**
     * Processes the student's tuition as a resident with regard to whether they are a full-time or part student,
     * the number of credits they are taking, and their scholarship, if they obtained one
     * @param creditsEnrolled number of credits the student is enrolled in
     * @return the student's calculated tuition
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if(creditsEnrolled < MINCREDITS) {
            return PTRESIDENTTUITION * creditsEnrolled + 0.8 * UNIFEE;
        } else {
            double tuition = FTRESIDENTTUITION + UNIFEE - scholarship;
            if (creditsEnrolled > ADDITIONALCREDITHOURS) {
                tuition += PTRESIDENTTUITION * (creditsEnrolled - ADDITIONALCREDITHOURS);
            }
            return tuition;
        }
    }

    /**
     * Updates and replaces the scholarship amount awarded to this Resident student.
     * @param scholarship the scholarship amount to award this Resident student.
     */
    public void setScholarship(int scholarship) {
        this.scholarship = scholarship;
    }

    /**
     * Checks whether the current class is a student resident
     * @return true since the student is a resident
     */
    @Override
    public boolean isResident() {
        return true;
    }

    /**
     * Creates a string containing the first, last name, DOB, student's major, student's standing, resident info
     * String formatted as "FirstName LastName DOB (MajorCode, MAJOR, SCHOOLNAME) Student-Standing "
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        return studentProfile() + " " + major() + " credits completed: " + creditsCompleted()  +
                " (" + studentStanding() + ")" + "(resident)";
    }
}
