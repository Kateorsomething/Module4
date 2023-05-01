import java.sql.SQLException;

/**
 * eyo
 * the tests are based on testData.csv & the TEST table so don't touch that
 */

public class Main {

    public static void main (String[] args) {

        DatabaseHandler handler = new DatabaseHandler();

        DatabaseHandler.getHandler().parseFile("CSVFiles/testData.csv","TEST");
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            DatabaseHandler.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
