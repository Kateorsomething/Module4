import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class writerTest {

    DatabaseHandler handler = new DatabaseHandler();

    /**
     * 1. commas within element
     * 2. element within quotes & escape quotes
     * 3. special characters within element
     * 4. null element & empty strings
     * 5. empty primary key
     */

    @Test
    public void writeFile1() {
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            FileReader fileReader = new FileReader("CSVFiles/TEST.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            assertEquals(line,"\"Aaron, Judge\",NYY,112,420,96,126,19,0,46");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void writeFile2() {
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            FileReader fileReader = new FileReader("CSVFiles/TEST.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            assertEquals(line,"Paul Goldschmidt,STL,108,405,80,133,31,\"\"\"0\"\"\",28,89");

            DatabaseHandler.connection.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void writeFile3() {
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            FileReader fileReader = new FileReader("CSVFiles/TEST.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            assertEquals(line,"Yordan Alvarez,H&&%OU,99,345,7/4,102,15,2,31,75");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void writeFile4() {
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            FileReader fileReader = new FileReader("CSVFiles/TEST.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            assertEquals(line,"Rafael Devers,BOS,,,99,397,65");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void writeFile5() {
        DatabaseHandler.getHandler().writeFile("TEST");

        try {
            FileReader fileReader = new FileReader("CSVFiles/TEST.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line = bufferedReader.readLine();

            assertEquals(line,"\"\",STL,106,401,58,119,28,1,25,73");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}