package repository;

import domain.Article;
import domain.User;
import mapper.AccountMapper;
import mapper.ArticleMapper;
import mapper.UserMapper;
import service.ApplicationContext;

import java.sql.*;

public class UserRepository implements BaseRepository {



    @Override
    public final void showAll( Object... value) throws SQLException {

        Statement statement=ApplicationContext.getConnection().createStatement();
       ResultSet resultSet = statement.executeQuery("select u.* , a.id as account_id ,balance ,isBlocked from user as u inner join account as a on a.id=u.id ");

       while (resultSet.next()){
           User user =UserMapper.mapTOUserObject(resultSet);
           user.setAccount(AccountMapper.mapToAccountObject(resultSet));
           System.out.println(user);
       }
    }


    @Override
    public void createTable() throws SQLException {
       Statement statement = ApplicationContext.getConnection().createStatement();

        statement.executeUpdate("create table if not exists user (id int  primary key auto_increment," +
                "username varchar(50) not null unique , nationalcode varchar(50) not null unique ," +
                "birthday date ,  password varchar(50) not null)");
    }

    @Override
    public int size() throws SQLException {
        int number = 0;
        Statement statement =  ApplicationContext.getConnection().createStatement();;

        ResultSet resultSet = statement.executeQuery("select * from user ;");
        while (resultSet.next())
            number++;

        return number;

    }

    @Override
    public <T> void add( T... str) throws SQLException {
        User user=(User) str[0];
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("insert into user(username, nationalcode , birthday, password) values (?,?,?,?)");

        ApplicationContext.getConnection().setAutoCommit(false);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getNationalCode());

        preparedStatement.setDate(3, user.getBirthDay());

        preparedStatement.setString(4, user.getNationalCode());

        preparedStatement.executeUpdate();
        user.setAccount(AccountRepository.addAccount());
        ApplicationContext.getConnection().commit();

        ApplicationContext.getConnection().setAutoCommit(true);

        preparedStatement.close();
    }

    private int getIdOfUser(String userName) throws SQLException {
        PreparedStatement preparedStatement =ApplicationContext.getConnection().prepareStatement("select id from user where username=?");
        preparedStatement.setString(1,userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        int counter=0;
        while (resultSet.next())
       counter = resultSet.getInt("id");

        return counter;
    }
    private User checkForRegister(String userName) throws SQLException {
        int counter = 0;

        int userId=getIdOfUser(userName);
        if(userId==0)
            return null;

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("select  u.* , a.id as account_id , a.balance , a.isBlocked from user as u  inner join account as a on a.id=u.id where a.id=?  ");

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
    public User checkExistsUser(String userName) throws SQLException {

        return checkForRegister(userName);

    }
    public User checkExistsUser(String userName, String password) throws SQLException {
        return checkForLogin(userName, password);
    }
    public User checkForLogin(String userName, String password) throws SQLException {

        int number = 0;

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("select  u.* , a.id as account_id , a.balance , a.isBlocked from user as u inner join account as a on a.id=u.id where  username =? and password=? ");

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

        preparedStatement = ApplicationContext.getConnection().prepareStatement("select a.* from user as u join article as a on a.user_id=u.id where u.username=? and u.password=?");
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

        ResultSet resultSet1 = preparedStatement.executeQuery();

        while (resultSet1.next()) {

            Article article = ArticleMapper.mapToArticleObject(resultSet1);

            user.addArticle(article);

        }
        return user;
    }
    public void changePasswordOfUser( User user, String newPassword) throws SQLException {
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("update user set password=? where id=?");
        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, user.getId());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        System.out.println("changing password is success !!!");
    }

    @Override
    public void addDefault() throws SQLException {
        PreparedStatement preparedStatement =  ApplicationContext.getConnection().prepareStatement("insert into user(username,nationalcode,birthday,password) " +
                "values (?,?,?,?)");
        ApplicationContext.getConnection().setAutoCommit(false);

        preparedStatement.setString(1,"mmn4804");
        preparedStatement.setString(2,"1285672345");
        preparedStatement.setDate(3,Date.valueOf("1367-08-11"));
        preparedStatement.setString(4,"13804804");
        preparedStatement.executeUpdate();
        AccountRepository.addAccount();

        ApplicationContext.getConnection().commit();
        ApplicationContext.getConnection().setAutoCommit(true);

        ApplicationContext.getConnection().setAutoCommit(false);

        preparedStatement.setString(1,"ali1507");
        preparedStatement.setString(2,"1273427234");
        preparedStatement.setDate(3,Date.valueOf("1381-02-26"));
        preparedStatement.setString(4,"1507ali");

        preparedStatement.executeUpdate();
        AccountRepository.addAccount();
        ApplicationContext.getConnection().commit();

        ApplicationContext.getConnection().setAutoCommit(true);
        preparedStatement.close();
        System.out.println("default user added ....\n");
    }

}



