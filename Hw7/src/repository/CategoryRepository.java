package repository;

import service.ApplicationContext;

import java.sql.*;
import java.util.Scanner;

public class CategoryRepository implements BaseRepository {
    @Override
    public final  <T> void showAll( T... value) throws SQLException {
        Statement statement = ApplicationContext.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select  * from category");


        while (resultSet.next()) {

            System.out.println("id : " + resultSet.getInt("id") + " , category_title : " + resultSet.getString("title")
                    + " , category_description : " + resultSet.getString("description"));
        }
    }


    @Override
    public void createTable() throws SQLException {
        Statement statement = ApplicationContext.getConnection().createStatement();
        statement.executeUpdate("create table if not exists category(id int primary  key auto_increment  , title varchar (50) not null unique , " +
                "description varchar (50) )");
    }

    @Override
    public int size() throws SQLException {
        Statement statement=ApplicationContext.getConnection().createStatement();
        int counter=0;
        ResultSet resultSet =statement.executeQuery("select * from category");

        while (resultSet.next())
            counter++;

        return counter;
    }

    @Override
    public void addDefault() throws SQLException {
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("insert into category(title, description)" +
                "values (?,?) ,(?,?)");

        preparedStatement.setString(1, "scientific");
        preparedStatement.setString(2, "javanebe scientific");
        preparedStatement.setString(3, "religious");
        preparedStatement.setString(4, null);

        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println("default category added ....\n");
    }

    @Override
    public <T> void add( T... str) throws SQLException {
        String title=(String) str[0];
        Scanner scanner = new Scanner(System.in);


        System.out.println("enter description of category");
        String description = scanner.nextLine();

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("insert into category(title,description) value (?,?)");

        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }


    public static String getTitleOfCategory(int categoryId, Statement statement) throws SQLException {

        ResultSet resultSet = statement.executeQuery("select * from category where id =" + categoryId);

        System.out.println("categoryID : " + categoryId);
        String title = null;
        while (resultSet.next()) {
            title = resultSet.getString("title");
        }

        System.out.println("title : " + title);
        return title;
    }
}
