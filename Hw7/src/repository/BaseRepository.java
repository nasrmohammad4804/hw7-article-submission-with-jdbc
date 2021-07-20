



package repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface BaseRepository {


     <T extends Object > void showAll( T ... value) throws SQLException;

    void createTable( )throws SQLException;

    default <T extends Object> void add( T ... str) throws SQLException{
        //...........
    }

    default void addDefault() throws SQLException {
        // .........
    }

    int size()throws SQLException;
}
