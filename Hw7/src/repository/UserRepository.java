package repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository implements BaseRepository{

    @Override
    public void showAll(Connection connection, Object object) throws SQLException {

    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement=connection.createStatement();
        statement.executeUpdate("create table if not exists user (id int primary key auto_increment," +
                "username varchar (50) not null unique , nationalcode varchar (50) unique not null," +
                "birthday date ,  password varchar (50) not null, acount_id int not null unique , foreign key acount_id references acount(id))");
    }

    @Override
    public int size(Connection connection)throws SQLException {
        int number = 0;
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from user ;");
        while (resultSet.next())
            number++;

        return number;

    }
    @Override
    public

}
