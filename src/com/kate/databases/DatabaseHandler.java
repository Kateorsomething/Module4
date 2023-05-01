import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHandler {
    private static final String DBurl = "jdbc:derby:database/forum";
    public static Connection connection = null;  //change back to private
    private static Statement statement = null;
    public static DatabaseHandler handler;

    public DatabaseHandler() {
        createConnection();
    }

    public static DatabaseHandler getHandler() {
        if (handler == null) {
            handler = new DatabaseHandler();
            return handler;
        } else {
            return handler;
        }
    }

    private void createConnection() {
        try {
//            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(DBurl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean executeAction(String qu) {
        try {
            statement = connection.createStatement();
            statement.execute(qu);
            return true;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }

    public ResultSet executeQuery(String qu) {
        ResultSet resultset;
        try {
            statement = connection.createStatement();
            resultset = statement.executeQuery(qu);
            return resultset;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * helper method: create table if none exists
     * first column defaults to primary key
     * @param tableName must begin with letter
     * @param columns number of columns in the table created
     */
    private void createTable(String tableName, int columns,String[] headers) {

        try {
            statement = connection.createStatement();
            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet tables = dmd.getTables(null, null, tableName, null);

            if(tables.next()) {
                System.out.println("table " + tableName + " already exists");
            } else {
                String stm = "CREATE TABLE " + tableName + " ("
                        + headers[0] + " VARCHAR(200) primary key)";
                this.statement.execute(stm);

                for (int i = 1; i < columns ; i++) {
                    stm = "ALTER TABLE " + tableName + "\n"
                            + "ADD " + headers[i] + " VARCHAR(200)";
                    System.out.println(stm);
                    this.statement.execute(stm);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param tableName
     * @param columnName
     * @param primaryKey
     * @return an element (in string) given key & column
     */
    public static String getElement(String tableName, String columnName, String primaryKey) {
        try {
            statement = connection.createStatement();
            ResultSet primaryKeys = connection.getMetaData().getPrimaryKeys(null,null,tableName);
            String key = "";         //finding the primary key column name
            while (primaryKeys.next()) {
                key = primaryKeys.getString("COLUMN_NAME");
                break;
            }

            String stm = "SELECT " + columnName + " FROM " + tableName + " WHERE " + key + " = '" + primaryKey + "'";
            ResultSet resultset = statement.executeQuery(stm);

            if (resultset.next()) {
                return resultset.getString(columnName);
            } else {
                return "no element found";
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return "no element found";
    }

    /**
     * creates a table with first row as column names (no special characters)
     * inserts null by default if # of elements in value < # of columns
     * @param filePath path to CSV file
     * @param tableName name of table created
     */
    public void parseFile(String filePath, String tableName) {
        File file;
        Reader reader;
        Iterable<CSVRecord> CSVRecord;
        List<String[]> records;

        try {
            //pass CSV file into ArrayList records
            file = new File(filePath);
            reader = new FileReader(file);
            CSVRecord = CSVFormat.DEFAULT.parse(reader);
            records = new ArrayList<>();
                for (CSVRecord record : CSVRecord) {
                    records.add(record.values());
                }

            //create table
            int colNum = 1;
            for (String[] record : records) {
                if (record.length > colNum) {
                    colNum = record.length;
                }
            }

            createTable(tableName,colNum,records.get(0));

            //iterate through arrays to add value into table
            for (String[] record : records) {
                if (record != records.get(0)) {
                    String stm = "INSERT INTO " + tableName + "\n"
                            + "VALUES (";
                    for (String value : record) {
                    stm = stm + "'" + value + "', ";
                }
                    if (record.length < colNum) {
                    for (int i = 1 ; i <= colNum-record.length ; i++) {
                        stm = stm + "NULL ,";
                    }
                }
                    stm = stm.substring(0,stm.length()-2) + ")";
                    this.statement.execute(stm);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * write an existing table into a csv file of the same name in the CSVFiles folder
     * @param tableName
     */
    public void writeFile(String tableName) {
        try {
            FileWriter writer = new FileWriter("CSVFiles/" + tableName + ".csv");
            CSVPrinter printer = new CSVPrinter(writer,CSVFormat.DEFAULT);
            ResultSet resultset = executeQuery("SELECT * FROM " + tableName);
            int columnCount = resultset.getMetaData().getColumnCount();
            ArrayList<String> columnValue = new ArrayList<>();

            while (resultset.next()) {
                columnValue.clear();
                for (int i = 1; i <= columnCount; i++) {
                    if (resultset.getString(i) != null) {
                    columnValue.add(resultset.getString(i));
                    }
                }
                printer.printRecord(columnValue);
            }

            printer.close();

        } catch (IOException e) {
                throw new RuntimeException(e);
        } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

}


