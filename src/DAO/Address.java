package DAO;

public class Address {
    private int id;
    private String streetNr, streetName, postCode, postTown;

    public Address(int id, String streetNr, String streetName, String postCode, String postTown) {
        this.id = id;
        this.streetNr = streetNr;
        this.streetName = streetName;
        this.postCode = postCode;
        this.postTown = postTown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostTown() {
        return postTown;
    }

    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }
}
