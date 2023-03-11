package studentinfo.project3;
/**
 Profile of an individual student, containing the first, last name and date of birth.
 @author David Fabian, Steven Tan
 */
public class Profile implements Comparable<Profile> {
    private String lname;
    private String fname;
    private Date dob;

    /**
     * Constructs a Profile instance for a student.
     * Holds first, last name and DOB.
     * @param first the first name of the student a string
     * @param last the last name of the student as a string
     * @param birthDate the date of birth of the student
     *                  formatted as "mm/dd/yyyy"
     */
    public Profile(String first, String last, String birthDate) {
        this.fname = first;
        this.lname = last;
        this.dob = new Date(birthDate);
    }

    /**
     * Creates a string containing the first, last name and DOB.
     * String formatted as "FirstName LastName DOB"
     * @return a string representation of this Profile instance.
     */
    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob;
    }

    /**
     * Compares this Profile to the specified object.
     * The result is true if the passed-in object is a Profile object,
     * the names in both profiles are the same (not case-sensitive),
     * and the DOB are the same calendar date.
     * @param obj object to compare this Profile instance against.
     * @return true if the object is a Profile object that is equivalent
     * to this Profile; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Profile) {
            Profile otherProfile = (Profile) obj;
            return this.fname.equalsIgnoreCase(otherProfile.firstName()) &&
                    this.lname.equalsIgnoreCase(otherProfile.lastName()) &&
                    this.dob.equals(otherProfile.birthDate());
        }
        return false;
    }

    /**
     * Compares this and the specified Profile object lexicographically,
     * based on their last name, first name, and their DOB in that order.
     * @param otherProfile Profile object to compare this Profile against.
     * @return the value 0 if the argument Profile is equal to this Profile;
     * a value less than 0 if this Profile is less than the argument Profile based on the comparison;
     * and a value greater than 0 if this Profile is greater than the argument Profile based on the comparison.
     */
    @Override
    public int compareTo(Profile otherProfile) {
        int compare = lname.compareToIgnoreCase(otherProfile.lastName());
        if(compare != 0) {return compare;}
        compare = fname.compareToIgnoreCase(otherProfile.firstName());
        if(compare != 0) {return compare;}
        return dob.compareTo(otherProfile.birthDate());
    }

    /**
     * Gets the last name of this Profile instance.
     * @return a string representation of this profile's last name.
     */
    public String lastName() {
        return lname;
    }

    /**
     * Gets the first name of this Profile instance.
     * @return a string represenation of this profile's first name.
     */
    public String firstName() {
        return fname;
    }

    /**
     * Gets the Date object the represents the DOB of this Profile.
     * @return a reference to the Date object representing this profile's DOB
     */
    public Date birthDate() {
        return dob;
    }
}
