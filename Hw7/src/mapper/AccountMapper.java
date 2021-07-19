package mapper;

import domain.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper {
    public static Account mapToAccountObject(ResultSet resultSet) throws SQLException {
        Account account =new Account(resultSet.getInt("account_id"),
                resultSet.getInt("balance"),resultSet.getBoolean("isBlocked"));

        return  account;
    }
}
