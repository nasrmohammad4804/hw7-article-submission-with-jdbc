package repository;

import java.sql.*;

public class TagRepository implements BaseRepository{
    @Override
    public void showAll(Connection connection, Object object) throws SQLException {

    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("create table if not exists " +
                "tag(id int primary key auto_increment, title varchar(50) not null )");

        statement.close();
    }

    @Override
    public int size(Connection connection) throws SQLException {
        int number = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tag;");
        while (resultSet.next())
            number++;

        statement.close();

        return number;
    }

    @Override
    public void add(Connection connection, Object str) throws SQLException {
        String tag=(String) str;
        PreparedStatement preparedStatement = connection.prepareStatement("insert into tag(title) value (?)");

        preparedStatement.setString(1, tag);

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }
}
