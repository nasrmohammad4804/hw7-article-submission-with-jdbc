package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseRepository {


    void showAll(Connection connection, Object object)throws SQLException;

    void createTable(Connection connection)throws SQLException;

    private void add(Connection connection, Object str) throws SQLException {
        //...........
    }

    private void addDefault(Connection connection) throws SQLException {
        // .........
    }

    int  size(Connection connection)throws SQLException;

}
