package studentinfo.project3;

/**
 * This class contains methods that contain details of a student's profile and the number of students they are
 * enrolled in
 *
 * @author Steven Tan, David Fabian
 */
public class EnrollStudent {
    private Profile profile;
    private int creditsEnrolled;

    /**
     * Constructs an EnrollStudent instance for a student.
     * Holds the student's profile and creditsEnrolled
     * @param  profile  the student's profile
     * @param  creditsEnrolled  number of credits enrolled by student
     */
    public EnrollStudent(Profile profile, int creditsEnrolled) {
        this.profile = profile;
        this.creditsEnrolled = creditsEnrolled;
    }

    /**
     * Compares the profiles of the object and the specified EnrollStudent object lexicographically,
     * based on their last name, first name, and their DOB in that order.
     * @param obj Student object to compare this Student against.
     * @return the value 0 if the argument EnrollStudent is equal to this EnrollStudent;
     * a value less than 0 if this EnrollStudent is less than the argument EnrollStudent based on the comparison;
     * and a value greater than 0 if this EnrollStudent is greater than the argument EnrollStudent
     * based on the comparison.
     */
    public int compareTo(EnrollStudent obj) {
        Profile profile1 = this.profile;
        Profile profile2 = obj.studentProfile();
        int result = profile1.compareTo(profile2);
        if (result == 0) {
            return 0;
        } else if (result < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Creates a string containing the first, last name, DOB, and the number of credits enrolled
     * String formatted as "FirstName LastName DOB: credits enrolled: creditsEnrolled"
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        return this.profile + ": credits enrolled: " + creditsEnrolled;
    }

    /**
     * Compares this EnrollStudent to the specified object.
     * The result is true if the passed-in object is a EnrollStudent object,
     * the names in both profiles are the same (not case-sensitive),
     * and the DOB are the same calendar date.
     * @param obj object to compare this EnrollStudent instance against.
     * @return true if the object is a EnrollStudent object that is equivalent
     * to this EnrollStudent; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof EnrollStudent) {
            EnrollStudent student = (EnrollStudent) obj;
            return student.profile.equals(this.profile);
        }
        return false;
    }

    /**
     * Gets the Profile object from the EnrollStudent profile instance variable
     * @return a reference to the Profile object representing the student's profile
     */
    public Profile studentProfile() {
        return this.profile;
    }

    /**
     * Returns the current number of credits being taken by the enrolled student.
     * @return the number of credits taken by the enrolled student currently.
     */
    public int credits() {
        return creditsEnrolled;
    }

    /**
     * Updates the enrolled student's credits with the number of credits given.
     * @param newCredits the number of credits to replace the current credits.
     */
    public void updateCredits(int newCredits) {
        creditsEnrolled = newCredits;
    }
}
