package service;

import domain.User;
import repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class UserAdmin {

    public static void blockAccount(User user, Connection connection)throws SQLException {
        AccountRepository.blockAccount(user,connection);
    }
    public static void unBlockAccount(User user,Connection connection) throws SQLException{
        AccountRepository.unBlockAccount(user,connection);
    }


}
