package DAO;

import java.util.ArrayList;

public class Invoice {
    private int id, customerId;
    private String date;
    private ArrayList<Product> items;

    public Invoice(int id, int customerId, String date) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        items = new ArrayList<>();
    }

    public void add(Product product) {
        items.add(product);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    public String toString() {
        return id + "";
    }
}
