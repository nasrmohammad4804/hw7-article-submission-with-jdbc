package service;

import domain.User;
import repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserAdmin {

    public static void blockAccount(User user, Connection connection)throws SQLException {
        AccountRepository.blockAccount(user,connection);
    }
    public static void unBlockAccount(User user,Connection connection) throws SQLException{
        AccountRepository.unBlockAccount(user,connection);
    }

    public static boolean confirmToRegisterUser(){
        Scanner scanner=new Scanner( System.in);

        System.out.println("userAdmin want you register this user\nif you enter yes if dont want enter no");
        String result=scanner.nextLine();

        return result.equals("yes");
        }

    }



