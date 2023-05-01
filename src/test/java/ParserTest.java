import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

public class ParserTest {
    DatabaseHandler handler = new DatabaseHandler();

    /**
     * 1. commas within element
     * 2. element within quotes
     * 3. element within escape quote
     * 4. special characters within element
     * 5. null element
     * 6. empty string
     * 7. empty primary key
     */

    @org.junit.Test
    public void parseFile1() {
        DatabaseHandler.getHandler().executeAction("DROP TABLE TEST");
        handler.getHandler().parseFile("CSVFiles/testData.csv","TEST");
        System.out.println(handler.getHandler().getElement("TEST","C2","Aaron, Judge"));
        assertEquals(handler.getHandler().getElement("TEST","NAME","Aaron, Judge"),"Aaron, Judge");
    }

    @org.junit.Test
    public void parseFile2() {
        assertEquals(handler.getHandler().getElement("TEST","C3","Paul Goldschmidt"),"108");
        }

    @org.junit.Test
    public void parseFile3() {
        assertEquals(handler.getHandler().getElement("TEST","C8","Paul Goldschmidt"),"\"0\"");
    }

    @org.junit.Test
    public void parseFile4() {
        assertEquals(handler.getHandler().getElement("TEST","C5","Yordan Alvarez"),"7/4");
    }

    @org.junit.Test
    public void parseFile5() {
        assertEquals(handler.getHandler().getElement("TEST","C8","Rafael Devers"),null);
    }

    @org.junit.Test
    public void parseFile6() {
        assertEquals(handler.getHandler().getElement("TEST","C2","Austin Riley"),"");
    }

    @org.junit.Test
    public void parseFile7() {
        assertEquals(handler.getHandler().getElement("TEST","NAME",""),"");
        try {
            DatabaseHandler.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}