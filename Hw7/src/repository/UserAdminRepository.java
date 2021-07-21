package repository;

import domain.UserAdmin;
import mapper.UserAdminMapper;
import service.ApplicationContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAdminRepository implements BaseRepository{
    @Override
    public <T> void showAll( T... value) throws SQLException {
       // ...
    }

    @Override
    public <T> void add(T... str) throws SQLException {
        UserAdmin admin=(UserAdmin) str[0];

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("insert into user_admin(name,family,age,user_name , password) values " +
                "(?,?,?,?,?)");
        preparedStatement.setString(1,admin.getName());
        preparedStatement.setString(2,admin.getFamily());
        preparedStatement.setInt(3,admin.getAge());
        preparedStatement.setString(4,admin.getUserName());
        preparedStatement.setString(5,admin.getPassword());
        preparedStatement.executeUpdate();
        System.out.printf("this userAdmin %s added to list of userAdmin ...\n\n",admin.getUserName());
        preparedStatement.close();

    }

    @Override
    public void createTable() throws SQLException {
        Statement statement=ApplicationContext.getConnection().createStatement();
        statement.executeUpdate("create table if not exists user_admin(name varchar (50) not null , " +
                "family varchar (50) not null, age int , id int primary key auto_increment , " +
                "user_name varchar (50) not null unique , password varchar (50) not null)");
    }

    @Override
    public int size() throws SQLException {
       Statement statement =ApplicationContext.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
       int counter=0;

       ResultSet resultSet=statement.executeQuery("select * from user_admin");

       if(resultSet.last()){
           counter=resultSet.getRow();
       }

       return counter;

    }
    public void addDefaultAdmin() throws SQLException {
        PreparedStatement preparedStatement= ApplicationContext.getConnection().prepareStatement("insert into user_admin" +
                "(name,family,age,user_name,password) values(?,?,?,?,?)");
        preparedStatement.setString(1,"mona");
        preparedStatement.setString(2,"ahmadi");
        preparedStatement.setInt(3,26);
        preparedStatement.setString(4,"mona6069");
        preparedStatement.setString(5,"6069mona");
        preparedStatement.executeUpdate();

        preparedStatement.setString(1,"mahdi");
        preparedStatement.setString(2,"zare");
        preparedStatement.setInt(3,26);
        preparedStatement.setString(4,"mahdi1111");
        preparedStatement.setString(5,"1111mahdi");
        preparedStatement.executeUpdate();
    }
    public List<UserAdmin> findAll() throws SQLException {
        List<UserAdmin> list=new ArrayList<>();

        Statement statement=ApplicationContext.getConnection().createStatement();
       ResultSet resultSet = statement.executeQuery("select  * from user_admin");

       while (resultSet.next()){
           list.add(UserAdminMapper.mapToUserAdminObject(resultSet));
       }
       return list;
    }

}
