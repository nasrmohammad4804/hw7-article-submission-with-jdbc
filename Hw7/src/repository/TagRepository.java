package repository;

import service.ApplicationContext;

import java.sql.*;

public class TagRepository implements BaseRepository{
    @Override
    public final  <T> void showAll( T... value) throws SQLException {

        int category_id=0;

        if(value[0] instanceof Integer)
         category_id=(int) value[0];

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("" +
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
    public void createTable() throws SQLException {
        Statement statement = ApplicationContext.getConnection().createStatement();

        statement.executeUpdate("create table if not exists " +
                "tag(id int primary key auto_increment, title varchar(50) not null )");

        statement.close();
    }

    @Override
    public  int size() throws SQLException {
        int number = 0;
        Statement statement = ApplicationContext.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tag;");
        while (resultSet.next())
            number++;

        statement.close();

        return number;
    }

    @Override
    public <T> void add( T... str) throws SQLException {
        String tag=(String) str[0];
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("insert into tag(title) value (?)");

        preparedStatement.setString(1, tag);

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }


    public  int checkTagExists( String tagName) throws SQLException {

        int id = -1;
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("" +
                "select * from tag where title=? ");

        preparedStatement.setString(1, tagName);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("title").equals(tagName)) {
                id = resultSet.getInt("id");


            }
        }
        preparedStatement.close();
        if (id == -1) {
            add( tagName);
            id = size();

        }

        return id;
    }
}
