package repository;

import domain.Article;
import domain.User;
import mapper.ArticleMapper;
import mapper.UserMapper;

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
    private User checkForRegister(String userName, Connection connection) throws SQLException {
        int counter = 0;

        PreparedStatement preparedStatement = connection.prepareStatement("select  * from user where username =? ");

        preparedStatement.setString(1, userName);


        ResultSet resultSet = preparedStatement.executeQuery();


        User user = null;
        while (resultSet.next()) {
            counter++;

            user = UserMapper.mapTOUserObject(resultSet);
        }

        preparedStatement.close();
        if (counter == 0)
            return null;

        return user;
    }
    public User checkExistsUser(String userName, Connection connection) throws SQLException {

        return checkForRegister(userName, connection);

    }
    public User checkExistsUser(String userName, String password, Connection connection) throws SQLException {
        return checkForLogin(userName, password, connection);
    }
    public User checkForLogin(String userName, String password, Connection connection) throws SQLException {

        int number = 0;

        PreparedStatement preparedStatement = connection.prepareStatement("select  * from user where username =? and password=? ");

        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);


        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;
        while (resultSet.next()) {
            number++;

            user = UserMapper.mapTOUserObject(resultSet);
        }
        if (number == 0)
            return null;

        preparedStatement = connection.prepareStatement("select a.* from user as u join article as a on a.user_id=u.id where u.username=? and u.password=?");
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet1 = preparedStatement.executeQuery();

        while (resultSet1.next()) {

            Article article = ArticleMapper.mapToArticleObject(resultSet1);

            user.addArticle(article);

        }
        return user;
    }

}



