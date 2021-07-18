package repository;

import domain.User;

import java.sql.*;

public class UserRepository implements BaseRepository {

    @Override
    public void showAll(Connection connection, Object object) throws SQLException {

    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table if not exists user (id int primary key auto_increment," +
                "username varchar (50) not null unique , nationalcode varchar (50) unique not null," +
                "birthday date ,  password varchar (50) not null, acount_id int not null unique , foreign key acount_id references acount(id))");
    }

    @Override
    public int size(Connection connection) throws SQLException {
        int number = 0;
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from user ;");
        while (resultSet.next())
            number++;

        return number;

    }
    @Override
    public void add(Connection connection, Object object) throws SQLException {
        User user=(User) object;
        PreparedStatement preparedStatement = connection.prepareStatement("insert into user(username, nationalcode , birthday, password) values (?,?,?,?)");


        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getNationalCode());

        preparedStatement.setDate(3, user.getBirthDay());

        preparedStatement.setString(4, user.getNationalCode());

        preparedStatement.executeUpdate();

        preparedStatement.close();

    }

}



