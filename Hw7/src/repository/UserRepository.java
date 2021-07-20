package repository;

import domain.Article;
import domain.User;
import mapper.AccountMapper;
import mapper.ArticleMapper;
import mapper.UserMapper;

import java.sql.*;

public class UserRepository implements BaseRepository {



    @Override
    public final void showAll(Connection connection, Object... value) throws SQLException {
        // ............. no idea  specific implemention
    }


    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table if not exists user (id int  primary key auto_increment," +
                "username varchar(50) not null unique , nationalcode varchar(50) not null unique ," +
                "birthday date ,  password varchar(50) not null)");
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
    public <T> void add(Connection connection, T... str) throws SQLException {
        User user=(User) str[0];
        PreparedStatement preparedStatement = connection.prepareStatement("insert into user(username, nationalcode , birthday, password) values (?,?,?,?)");

        connection.setAutoCommit(false);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getNationalCode());

        preparedStatement.setDate(3, user.getBirthDay());

        preparedStatement.setString(4, user.getNationalCode());

        preparedStatement.executeUpdate();
        user.setAccount(AccountRepository.addAccount(connection));
        connection.commit();

        connection.setAutoCommit(true);

        preparedStatement.close();
    }

    private int getIdOfUser(String userName,Connection connection) throws SQLException {
        PreparedStatement preparedStatement =connection.prepareStatement("select id from user where username=?");
        preparedStatement.setString(1,userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        int counter=0;
        while (resultSet.next())
       counter = resultSet.getInt("id");

        return counter;
    }
    private User checkForRegister(String userName, Connection connection) throws SQLException {
        int counter = 0;

        int userId=getIdOfUser(userName,connection);
        PreparedStatement preparedStatement = connection.prepareStatement("select  u.* , a.id as account_id , a.balance , a.isBlocked from user as u  inner join account as a on a.id=?  "); //TODO

        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();


        User user = null;
        while (resultSet.next()) {
            counter++;

            user = UserMapper.mapTOUserObject(resultSet);
            user.setAccount(AccountMapper.mapToAccountObject(resultSet));
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

        PreparedStatement preparedStatement = connection.prepareStatement("select  u.* , a.id as account_id , a.balance , a.isBlocked from user as u inner join account as a on a.id=u.id where  username =? and password=? ");

        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);


        ResultSet resultSet = preparedStatement.executeQuery();

        User user = null;
        while (resultSet.next()) {
            number++;

            user = UserMapper.mapTOUserObject(resultSet);
            user.setAccount(AccountMapper.mapToAccountObject(resultSet));
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
    public void changePasswordOfUser(Connection connection, User user, String newPassword) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update user set password=? where id=?");
        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, user.getId());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        System.out.println("changing password is success !!!");
    }

    @Override
    public void addDefault(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into user(username,nationalcode,birthday,password) " +
                "values (?,?,?,?)");
        connection.setAutoCommit(false);

        preparedStatement.setString(1,"mmn4804");
        preparedStatement.setString(2,"1285672345");
        preparedStatement.setDate(3,Date.valueOf("1367-08-11"));
        preparedStatement.setString(4,"13804804");
        preparedStatement.executeUpdate();
        AccountRepository.addAccount(connection);

        connection.commit();
        connection.setAutoCommit(true);

        connection.setAutoCommit(false);

        preparedStatement.setString(1,"ali1507");
        preparedStatement.setString(2,"1273427234");
        preparedStatement.setDate(3,Date.valueOf("1381-02-26"));
        preparedStatement.setString(4,"1507ali");

        preparedStatement.executeUpdate();
        AccountRepository.addAccount(connection);
        connection.commit();

        connection.setAutoCommit(true);
        preparedStatement.close();
        System.out.println("default user added ....\n");
    }

}



