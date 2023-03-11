package studentinfo.project3;

/**
 * This class contains methods that calculate a tristate student's tuition
 *
 * @author Steven Tan, David Fabian
 */
public class TriState extends NonResident{
    private static final int NYTUITIONDISCOUNT = 4000;
    private static final int CTTUITIONDISCOUNT = 5000;

    private String state;
    /**
     * Constructs a Tristate instance for a student from the tristate area.
     * Holds details of the student's profile, major, creditCompleted, and the state they are from
     *
     * @param profile         the student's profile
     * @param major           the student's major
     * @param creditCompleted number of credits completed by student
     * @param state           the state the student lives in
     */
    public TriState(Profile profile, Major major, int creditCompleted, String state) {
        super(profile, major, creditCompleted);
        this.state = state;
    }

    /**
     * Processes the student's tuition as a tristate nonresident student with regard to whether they are a full-time
     * or part student, the number of credits they are taking, and the location of their home state
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
            if(state.toUpperCase().equals("NY")) {
                return tuition - NYTUITIONDISCOUNT;
            } else if(state.toUpperCase().equals("CT")) {
                return tuition - CTTUITIONDISCOUNT;
            } else {
                return tuition;
            }
        }
    }

    /**
     * Retrieves the state which this Tri-state student is from.
     * @return the state this student is from as a String.
     */
    public String state() {
        return state;
    }

    /**
     * Creates a string containing the first, last name, DOB, student's major, student's standing, resident info
     * String formatted as "FirstName LastName DOB (MajorCode, MAJOR, SCHOOLNAME) Student-Standing "
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        return studentProfile() + " " + major() + " credits completed: " + creditsCompleted()  +
                " (" + studentStanding() + ")" + "(non-resident)(tri-state:" + this.state.toUpperCase() + ")";
    }
}
