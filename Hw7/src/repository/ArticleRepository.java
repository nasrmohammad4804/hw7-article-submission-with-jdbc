package repository;

import domain.Category;
import domain.User;
import mapper.ArticleMapper;

import java.sql.*;
import java.util.Scanner;

public class ArticleRepository implements BaseRepository{
    @Override
    public void showAll(Connection connection, Object object) throws SQLException {

    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement=connection.createStatement();

        statement.executeUpdate("create table if not exists article " +
                "(id int primary key auto_increment , title varchar(50) not null ," +
                "brief varchar (50) not null , content text   , " +
                "createDate date , isPublished tinyint(1)  , isFree tinyint(1) " +
                "lastUpdateDate date , publishDate date , " +
                "user_id int , category_id int , " +
                "foreign key  (user_id) references user(id) , " +
                "foreign key (category_id) references category(id))");
    }

    @Override
    public int size(Connection connection) throws SQLException {
        int number = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from article ");

        while (resultSet.next())
            number++;

        return number;
    }
    public void changeArticleOfUser(Connection connection, User user, int idOfArticle) throws SQLException {

        Scanner sc = new Scanner(System.in);
        // showArticleOfUser(connection, userDomain);

        PreparedStatement preparedStatement;

        System.out.println("1. change title\n2.change brief\n3.change content\n4.isPublish");

        int result = sc.nextInt();

        switch (result) {

            case 1:
                sc.nextLine();
                System.out.println("enter new title");
                String title = sc.nextLine();
                preparedStatement = connection.prepareStatement("update article as a set a.title=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, title);
                preparedStatement.setInt(2, idOfArticle);
                preparedStatement.setInt(3, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("title of row is changed ..\n");
                return;


            case 2:
                sc.nextLine();
                System.out.println("enter new brief");
                String brief = sc.nextLine();
                preparedStatement = connection.prepareStatement("update article as a set a.brief=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, brief);
                preparedStatement.setInt(2, idOfArticle);
                preparedStatement.setInt(3, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("brief or row is changed ..\n");
                return;


            case 3:
                sc.nextLine();
                System.out.println("enter new content");
                String content = sc.nextLine();
                preparedStatement = connection.prepareStatement("update article as a set a.content=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, content);
                preparedStatement.setInt(2, idOfArticle);
                preparedStatement.setInt(3, user.getId());
                System.out.println("content of row changed ...\n");

                preparedStatement.executeUpdate();

                return;

            case 4:
                preparedStatement = connection.prepareStatement("select isPublished from article as a where a.id=? and a.user_id=?");
                preparedStatement.setInt(1, idOfArticle);
                preparedStatement.setInt(2, user.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                boolean bool = false;
                while (resultSet.next())
                    bool = resultSet.getBoolean("isPublished");
                if (bool) {
                    preparedStatement = connection.prepareStatement("update article as a set isPublished=? where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setInt(2, idOfArticle);
                    preparedStatement.setInt(3, user.getId());

                    preparedStatement.executeUpdate();
                    System.out.println("filed of isPublished changed to false ...\n");

                } else {
                    preparedStatement = connection.prepareStatement("update article as a set isPublished=? where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.setInt(2, idOfArticle);
                    preparedStatement.setInt(3, user.getId());

                    preparedStatement.executeUpdate();

                    System.out.println("filed of isPublished changed to true  ...\n");
                }


        }


    }
    public boolean ExistsArticleId(Connection connection, int id, User user) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("select * from article as a where a.id=? and a.user_id=? ");
        int counter = 0;
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, user.getId());

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            counter++;

        preparedStatement.close();

        if (counter == 0)
            return false;

        return true;


    }
}
