/**
 * Creates a person 
 * @author Epifanio Sarinana
 * @author Kayra Dominguez
 */
public class Person {
    private String name;
    private String address;

    /**
     * 
     * @param name person name
     * @param address person address
     */
    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * 
     * @return person's name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name name to give person
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return person's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address address to give to person
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
