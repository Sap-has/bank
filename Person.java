/**
 * Creates a person 
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Person {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNum;

    /**
     * 
     * @param name person name
     * @param address person address
     */
    public Person(String fName, String lName, String dob, String address, String phoneNum) {
        firstName = fName;
        lastName = lName;
        dateOfBirth = dob;
        this.address = address;
        this.phoneNum = phoneNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    
}
