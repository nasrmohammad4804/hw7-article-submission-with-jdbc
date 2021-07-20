package mapper;

import domain.UserAdmin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAdminMapper {
    public static UserAdmin mapToUserAdminObject(ResultSet resultSet) throws SQLException {
        UserAdmin userAdmin=new UserAdmin(resultSet.getInt("id"),
                resultSet.getString("user_name"),
                resultSet.getString("password"));

        userAdmin.setName(resultSet.getString("name"));
        userAdmin.setFamily("family");
        userAdmin.setAge(resultSet.getInt("age"));

        return userAdmin;
    }
}
