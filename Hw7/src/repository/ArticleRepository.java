package repository;

import domain.Category;
import mapper.ArticleMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
