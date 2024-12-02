package genbank;
public class Person {
    
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNum;

    /**
     * Constructs a {@code Person} object with the specified personal details.
     *
     * @param fName    The first name of the person.
     * @param lName    The last name of the person.
     * @param dob      The date of birth of the person (in the format: Day-Month-Year).
     * @param address  The address of the person.
     * @param phoneNum The phone number of the person.
     */
    public Person(String fName, String lName, String dob, String address, String phoneNum) {
        firstName = fName;
        lastName = lName;
        dateOfBirth = dob;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    /**
     * Gets the first name of the person.
     *
     * @return The first name of the person.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the person.
     *
     * @return The last name of the person.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the full name of the person (first name followed by last name).
     *
     * @return The full name of the person.
     */
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Gets the date of birth of the person.
     *
     * @return The date of birth of the person (in the format: Day-Month-Year).
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the address of the person.
     *
     * @return The address of the person.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the phone number of the person.
     *
     * @return The phone number of the person.
     */
    public String getPhoneNum() {
        return phoneNum;
    }
}