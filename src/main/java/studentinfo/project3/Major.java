package studentinfo.project3;

/**
 * This enum contains methods that provide details of a student's major, majorID, and school name
 *
 * @author Steven Tan, David Fabian
 */
public enum Major {
    CS ("01:198", "SAS"),
    MATH ("01:640", "SAS"),
    EE ("14:332", "SOE"),
    ITI ("04:547", "SC&I"),
    BAIT ("33:136", "RBS");

    private String majorCode;
    private String schoolName;

    /**
     * Constructs a Major enum for a student.
     * Holds the major code, school name, and major name
     * @param majorCode the major code of the student's major
     * @param schoolName the name of the student's school
     */
    Major (String majorCode, String schoolName) {
        this.majorCode = majorCode;
        this.schoolName = schoolName;
    }

    /**
     * Gets name of the school of this Major instance.
     * @return a string representation of the name of school
     */
    public String schoolName() {
        return this.schoolName;
    }

    /**
     * Creates a string containing the major code, major, and student's school
     * String formatted as "Major-code Major School"
     * @return a string detailing information about the student's major
     */
    @Override
    public String toString() {
        return "(" + this.majorCode + " " + name()+ " " + this.schoolName + ")";
    }
}