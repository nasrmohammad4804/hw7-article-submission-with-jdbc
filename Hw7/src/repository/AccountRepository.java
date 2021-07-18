package repository;

import domain.User;

import java.sql.*;

public class AccountRepository  {

    public static void createAccount(Connection connection) throws SQLException {
        Statement statement=connection.createStatement();

        statement.executeUpdate("create table account(id int unsigned primary key auto_increment," +
                "balance int , isBlocked tinyint(1))");

        statement.close();
    }
    public void addAccount(Connection connection) throws SQLException {
        PreparedStatement preparedStatement=connection.prepareStatement("insert into account(balance,isBlocked) values " +
                "(?,?)");
        preparedStatement.setInt(1,0);
        preparedStatement.setBoolean(2,false);
    }
    public static void blockAccount(User user,Connection connection) throws SQLException {
        boolean bool=true;
        PreparedStatement preparedStatement=connection.prepareStatement("select  u.* , a.isBlocked as block from user as u inner join account as a on a.id=u.account_id where u.id=?");
        preparedStatement.setInt(1,user.getId());
        int counter=0;
        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            bool=resultSet.getBoolean("block");
            counter++;
        }
        if (counter==0){
            System.out.println("this username not exists ... please after time enter correct username !!!");
            return;
        }

        if (!bool){
            PreparedStatement preparedStatement1=connection.prepareStatement("update account as a set isBlocked=? where exists( select * from user as u where u.id=? and u.account_id=a.id );");

            preparedStatement1.setBoolean(1,true);
            preparedStatement1.setInt(2,user.getId());

            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        }
        else System.out.println("this account already blocked !!! ");


    }
    public static void unBlockAccount(User user,Connection connection) throws SQLException {
        boolean bool=false;
        PreparedStatement preparedStatement=connection.prepareStatement("select  u.* , a.isBlocked as myblock from user as u inner join account as a on a.id=u.account_id where u.id=?");
        preparedStatement.setInt(1,user.getId());
        int counter=0;
        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            bool=resultSet.getBoolean("myblock");
            counter++;
        }
        if (counter==0){
            System.out.println("this username not exists ... please after time enter correct username !!!");
            return;
        }

        if (bool){
            PreparedStatement preparedStatement1=connection.prepareStatement("update account as a set isBlocked=? where exists( select * from user as u where u.id=? and u.account_id=a.id );");

            preparedStatement1.setBoolean(1,false);
            preparedStatement1.setInt(2,user.getId());

            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        }
        else System.out.println("this account already unblock !!!");
    }


}
