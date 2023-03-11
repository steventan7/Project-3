package studentinfo.project3;

/**
 * This class contains methods that provide details of a student's first and last name, major, and credits completed.
 *
 * @author Steven Tan, David Fabian
 */
public abstract class Student implements Comparable<Student> {

    public static final int MINCREDITS = 12;
    public static final int MAXCREDITS = 24;
    public static final int ADDITIONALCREDITHOURS = 16;
    public static final int FTRESIDENTTUITION = 12536;
    public static final int FTNONRESIDENTTUITION = 29737;
    public static final int PTRESIDENTTUITION = 404;
    public static final int PTNONRESIDENTTUITION = 966;
    public static final int UNIFEE = 3268;
    public static final int HEALTHINSURANCE = 2650;
    private final int GRADCREDITS = 120;
    private Profile profile;
    private Major major;
    private int creditsCompleted;

    /**
     * Constructs a Student instance for a student.
     * Holds the student's profile, major, and creditCompleted
     * @param  profile  the student's profile
     * @param  major  the student's major
     * @param  creditCompleted  number of credits completed by student
     */
    public Student (Profile profile, Major major, int creditCompleted) {
        this.profile = profile;
        this.major = major;
        this.creditsCompleted = creditCompleted;
    }

    /**
     * Compares the profiles of the object and the specified Student object lexicographically,
     * based on their last name, first name, and their DOB in that order.
     * @param obj Student object to compare this Student against.
     * @return the value 0 if the argument Student is equal to this Student;
     * a value less than 0 if this Student is less than the argument Student based on the comparison;
     * and a value greater than 0 if this Student is greater than the argument Student based on the comparison.
     */
    @Override
    public int compareTo(Student obj) {
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
     * Compares this Student to the specified object.
     * The result is true if the passed-in object is a Student object,
     * the names in both profiles are the same (not case-sensitive),
     * and the DOB are the same calendar date.
     * @param obj object to compare this Student instance against.
     * @return true if the object is a Student object that is equivalent
     * to this Student; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Student) {
            Student student = (Student) obj;
            return student.studentProfile().equals(this.profile);
        }
        return false;
    }

    /**
     * Creates a string containing the first, last name, DOB, the student's major, and the student's standing
     * String formatted as "FirstName LastName DOB (MajorCode, MAJOR, SCHOOLNAME) Student-Standing "
     * @return a string representation of this String instance.
     */
    @Override
    public String toString() {
        return this.profile + " " + this.major + " credits completed: " + creditsCompleted  +
                " (" + studentStanding() + ")";
    }

    /**
     * Gets the student's academic standing
     * @return a string representation of the student's academic standing
     */
    public String studentStanding() {
        if (creditsCompleted < 30) {
            return "Freshman";
        } else if (creditsCompleted < 60) {
            return "Sophomore";
        } else if (creditsCompleted < 90) {
            return "Junior";
        } else {
            return "Senior";
        }
    }

    /**
     * Gets the student's academic standing
     * @return the number of credits completed by the student
     */
    public int creditsCompleted() {
        return this.creditsCompleted;
    }

    /**
     * Changes a student's major to the specified new major
     * This method uses a switch to change the student's current major to the newly specified one
     * @param major the student's new major
     */
    public void changeStudentMajor(String major) {
        String s = major.toUpperCase();
        switch(s) {
            case "CS":
                this.major = Major.CS;
                break;
            case "MATH":
                this.major = Major.MATH;
                break;
            case "EE":
                this.major = Major.EE;
                break;
            case "ITI":
                this.major = Major.ITI;
                break;
            case "BAIT":
                this.major = Major.BAIT;
                break;
            default:
                System.out.println("Major code invalid: " + major);
        }
    }

    /**
     * Gets the Profile object from the Student profile instance variable
     * @return a reference to the Profile object representing the student's profile
     */
    public Profile studentProfile() {
        return this.profile;
    }

    /**
     * Gets the Profile object from the Student profile instance variable
     * @return a reference to the Profile object representing the student's profile
     */
    public Major major() {
        return this.major;
    }

    /**
     * Checks whether the student has a valid number of credits enrolled
     * @param creditEnrolled number of credits the student is enrolled in
     * @return true if the student is enrolled between 3 and 24 credits, false if the student is not
     */
    public boolean isValid(int creditEnrolled) {
        return creditEnrolled >= 3 && creditEnrolled <= 24;
    }

    /**
     * Updates the number of completed credits after the end of the semester for this student.
     * @param semCredits the number of credits taken by this student in the completed semester.
     */
    public void updateCredits(int semCredits) {
        creditsCompleted += semCredits;
    }

    /**
     * Checks if this student can graduate with their current amount of completed credits.
     * @return true if this student can graduate; false otherwise.
     */
    public boolean canGraduate() {
        return creditsCompleted >= GRADCREDITS;
    }

    /**
     * eturns the student's total tuition due
     * @param creditsEnrolled number of credits the student is enrolled in
     * @return double value that represents the tuition due of this student.
     */
    public abstract double tuitionDue(int creditsEnrolled);

    /**
     * Checks whether a student is a resident student
     * @return true if this student is a Resident; false otherwise.
     */
    public abstract boolean isResident();
}
