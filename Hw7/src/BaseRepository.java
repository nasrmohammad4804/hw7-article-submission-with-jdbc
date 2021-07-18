import java.sql.Connection;
import java.sql.SQLException;

public interface BaseRepository {


    void showAll(Connection connection, Object object) throws SQLException;

    void createTable(Connection connection)throws SQLException;

    default void add(Connection connection, Object str) throws SQLException {
        //...........
    }

    default void addDefault(Connection connection) throws SQLException {
        // .........
    }

    int  size(Connection connection) throws SQLException;

}
