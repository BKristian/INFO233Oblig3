public class Main {
    public static void main(String args[]) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Feil i lasting av JDBC-driver " + e);
        }
        DatabaseSingleton db = DatabaseSingleton.getInstance();
    }
}
