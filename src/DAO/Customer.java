package DAO;

public class Customer {
    private int id, addressId;
    private String name, phone, account;
    private Address address;

    public Customer(int id, int addressId, String name, String phone, String account) {
        this.id = id;
        this.addressId = addressId;
        this.name = name;
        this.phone = phone;
        this.account = account;
        address = new AddressDAO().getAddress(addressId);
    }

    public Customer(int addressId, String name, String phone, String account) {
        this.addressId = addressId;
        this.name = name;
        this.phone = phone;
        this.account = account;
        address = new AddressDAO().getAddress(addressId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String toString() {
        return name;
    }
}
