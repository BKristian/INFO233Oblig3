package DAO;

import java.util.ArrayList;

public class InvoiceItems {
    ArrayList<String[]> list;

    public InvoiceItems() {
        list = new ArrayList<>();
    }

    public void add(String[] a) {
        list.add(a);
    }

    public ArrayList getList() {
        return list;
    }
}
