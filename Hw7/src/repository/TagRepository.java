package repository;

import java.sql.*;

public class TagRepository implements BaseRepository{
    @Override
    public void showAll(Connection connection, Object object) throws SQLException {

        int category_id=(int) object;

        PreparedStatement preparedStatement=connection.prepareStatement("" +
                "select temp from (select t.title as temp from tag as t inner join temp_article_tag as tat on tat.tag_id=t.id inner  join article as a "+
                "on a.id=tat.article_id where a.category_id = ? ) as ptr group by temp ");

        System.out.println("categoryId : "+category_id);

        preparedStatement.setInt(1,category_id);
        ResultSet resultSet =preparedStatement.executeQuery();

        while (resultSet.next()){
            System.out.println(resultSet.getString("temp"));
        }

        preparedStatement.close();

        System.out.println("------------------------------------------------------------");
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("create table if not exists " +
                "tag(id int primary key auto_increment, title varchar(50) not null )");

        statement.close();
    }

    @Override
    public int size(Connection connection) throws SQLException {
        int number = 0;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tag;");
        while (resultSet.next())
            number++;

        statement.close();

        return number;
    }

    @Override
    public void add(Connection connection, Object str) throws SQLException {
        String tag=(String) str;
        PreparedStatement preparedStatement = connection.prepareStatement("insert into tag(title) value (?)");

        preparedStatement.setString(1, tag);

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }
}
