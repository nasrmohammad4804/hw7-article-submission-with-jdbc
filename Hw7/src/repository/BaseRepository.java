



package repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseRepository {


     <T extends Object > void showAll(Connection connection, T ... value) throws SQLException;

    void createTable(Connection connection)throws SQLException;

    default <T extends Object> void add(Connection connection, T ... str) throws SQLException{
        //...........
    }

    default void addDefault(Connection connection) throws SQLException {
        // .........
    }

    int size(Connection connection)throws SQLException;
}
