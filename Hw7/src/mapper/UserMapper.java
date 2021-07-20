package mapper;

import domain.Account;
import domain.User;


import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User mapTOUserObject(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getInt("id"),
                resultSet.getString("username"), resultSet.getString("nationalcode"),
                resultSet.getDate("birthday"), resultSet.getString("password"));

        user.setAccount(new Account( resultSet.getInt("account_id"),resultSet.getInt("balance"),
                resultSet.getBoolean("isBlocked")));

        return user;
    }

}
