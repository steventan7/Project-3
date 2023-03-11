package studentinfo.project3;

/**
 * This class contains methods that calculate an international student's tuition
 *
 * @author Steven Tan, David Fabian
 */
public class International extends NonResident{

    private static final int STUDYABROADMAXCREDITS = 12;
    private boolean isStudyAbroad;
    /**
     * Constructs an International instance for an international student.
     * Holds details of the student's profile, major, creditCompleted, and whether they are studying abroad
     *
     * @param profile         the student's profile
     * @param major           the student's major
     * @param creditCompleted number of credits completed by student
     * @param isStudyAbroad   condition of whether the student is studying abroad
     */
    public International(Profile profile, Major major, int creditCompleted, boolean isStudyAbroad) {
        super(profile, major, creditCompleted);
        this.isStudyAbroad = isStudyAbroad;
    }

    /**
     * Processes the student's tuition as an international non-resident student with regard to whether they are a
     * full-time or part student, the number of credits they are taking, and if they are studying abroad
     * @param creditsEnrolled number of credits the student is enrolled in
     * @return the student's calculated tuition
     */
    @Override
    public double tuitionDue(int creditsEnrolled) {
        if (isStudyAbroad) {
            return UNIFEE + HEALTHINSURANCE;
        } else {
            if(creditsEnrolled > ADDITIONALCREDITHOURS) {
                return FTNONRESIDENTTUITION + UNIFEE + HEALTHINSURANCE + (creditsEnrolled - ADDITIONALCREDITHOURS)
                        * PTNONRESIDENTTUITION;
            }
            return 29737 + 3268 + 2650;
        }
    }

    /**
     * Check if this International student studies abroad.
     * @return true if this student studies abroad; false otherwise.
     */
    public boolean isStudyAbroad() {
        return isStudyAbroad;
    }

    /**
     * Checks whether the international student has a valid number of credits enrolled
     * @param creditEnrolled number of credits the student is enrolled in
     * @return true if the specified conditions are met, false otherwise
     */
    public boolean isValid(int creditEnrolled) {
        if(isStudyAbroad()) {
            if(creditEnrolled > STUDYABROADMAXCREDITS) {
                return false;
            }
        } else {
            if(creditEnrolled < MINCREDITS || creditEnrolled > MAXCREDITS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a string containing the first, last name, DOB, student's major, student's standing, resident info
     * String formatted as "FirstName LastName DOB (MajorCode, MAJOR, SCHOOLNAME) Student-Standing "
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        if(isStudyAbroad) {
            return studentProfile() + " " + major() + " credits completed: " + creditsCompleted()  +
                    " (" + studentStanding() + ")" + "(non-resident)(international:study abroad)";
        }
        return studentProfile() + " " + major() + " credits completed: " + creditsCompleted()  +
                " (" + studentStanding() + ")" +"(non-resident)(international)";
    }
}
