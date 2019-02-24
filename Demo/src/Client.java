package src;

public class Client {
    private String companyName;
    private String clientName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String email;
    private String phoneOne;
    private String phoneTwo;



    public Client (String name, String clientName, String address, String city, String state, String zip, String email, String firstPhone, String secPhone)
    {
        this.companyName = name;
        this.clientName = clientName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.email = email;
        this.phoneOne = firstPhone;
        this.phoneTwo = secPhone;
    }

    public String getCompanyName() {

        return companyName;
    }

    public String getName() {
        return clientName;
    }

    public String getAddress() {

        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() { return zip; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneOne() {
        return phoneOne;
    }

    public void setPhoneOne(String phoneOne) {
        this.phoneOne = phoneOne;
    }

    public String getPhoneTwo() {
        return phoneTwo;
    }

    public void setPhoneTwo(String phoneTwo) {
        this.phoneTwo = phoneTwo;
    }
}
