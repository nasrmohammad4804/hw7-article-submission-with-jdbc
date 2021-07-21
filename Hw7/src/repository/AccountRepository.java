package repository;

import domain.Account;
import domain.User;
import domain.UserAdmin;
import service.ApplicationContext;

import java.sql.*;

public class AccountRepository  {

    public  void createAccount() throws SQLException {

        Statement statement= ApplicationContext.getConnection().createStatement();

        statement.executeUpdate("create table if not exists account(id int  primary key auto_increment," +
                "balance int , isBlocked tinyint(1), foreign key (id) references user(id))");

        statement.close();
    }

    public static Account addAccount() throws SQLException {
        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("insert into account(balance,isBlocked) values " +
                "(?,?)");
        preparedStatement.setInt(1,0);
        preparedStatement.setBoolean(2,false);
        preparedStatement.executeUpdate();

        preparedStatement.close();

       return new Account(size(ApplicationContext.getConnection()),0,false);


    }
    public static int size(Connection connection) throws SQLException {
       Statement statement=  connection.createStatement();
      ResultSet resultSet=  statement.executeQuery("select  count(*) from account");

      resultSet.next();
      return resultSet.getInt(1);
    }


    public static void blockAccount(User user, UserAdmin admin) throws SQLException {
        boolean bool=true;
        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("select  u.* , a.isBlocked as block from user as u inner join account as a on a.id=u.id where u.id=?");
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
            PreparedStatement preparedStatement1=ApplicationContext.getConnection().prepareStatement("update account as a set isBlocked=? where exists( select * from user as u where u.id=? and u.id=a.id );");

            preparedStatement1.setBoolean(1,true);
            preparedStatement1.setInt(2,user.getId());

            preparedStatement1.executeUpdate();
            System.out.println("this account blocked by userAdmin : " + admin.getName() + "   " + admin.getFamily() + "   " + admin.getAge());
            preparedStatement1.close();
        }
        else System.out.println("this account already blocked !!! \n");


    }
    public static void unBlockAccount(User user,UserAdmin admin) throws SQLException {
        boolean bool=false;
        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("select  u.* , a.isBlocked as myblock from user as u inner join account as a on a.id=u.id where u.id=?");
        preparedStatement.setInt(1,user.getId());
        int counter=0;
        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            bool=resultSet.getBoolean("myblock");
            counter++;
        }
        if (counter==0){
            System.out.println("this username not exists ... please after time enter correct username !!!\n");
            return;
        }

        if (bool){
            PreparedStatement preparedStatement1=ApplicationContext.getConnection().prepareStatement("update account as a set isBlocked=? where exists( select * from user as u where u.id=? and u.id=a.id );");

            preparedStatement1.setBoolean(1,false);
            preparedStatement1.setInt(2,user.getId());

            preparedStatement1.executeUpdate();
            System.out.println("this account  unblocked  by user admin : " + admin.getName() + "   " + admin.getFamily() + "   " + admin.getAge());
            preparedStatement1.close();
        }
        else System.out.println("this account already unblock !!!\n");
    }

    public void chargeAccount(User user,int balance)throws SQLException{
        PreparedStatement preparedStatement =ApplicationContext.getConnection().prepareStatement("select a.balance , a.id as accountId from user as u  inner join account as a on a.id=? ;");
        preparedStatement.setInt(1,user.getAccount().getId());

        ResultSet resultSet =preparedStatement.executeQuery();
        resultSet.next();
        int myBalance =resultSet.getInt("balance");
        int accountId=resultSet.getInt("accountId");

        myBalance+=balance;

        PreparedStatement preparedStatement1=ApplicationContext.getConnection().prepareStatement("update account  set balance =? where id=? ");
        preparedStatement1.setInt(1,myBalance);
        preparedStatement1.setInt(2,accountId);
        preparedStatement1.executeUpdate();

        System.out.println(String.format("account is charged :  %d   your current balances is : %d \n",balance,myBalance));

        preparedStatement.close();
        preparedStatement1.close();


    }
    public boolean checkAccountBlockedUserForLogin(User user) throws SQLException {

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("" +
                "select  * from user as u inner join account as a on a.id=u.id where a.id=? and a.isBlocked=0 ");

        preparedStatement.setInt(1,user.getId());

        int counter=0;
        ResultSet resultSet =preparedStatement.executeQuery();

        while (resultSet.next())
            counter++;

        return counter>0 ? true : false;
    }


}
