package repository;

import domain.Article;
import domain.Category;
import domain.Tag;
import domain.User;
import mapper.ArticleMapper;
import service.ApplicationContext;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TempArticleTagRepository implements BaseRepository {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public final  <T extends Object> void showAll( T... value) throws SQLException {

        User user=null;

        if(value[0] instanceof User)
         user= (User) value[0];

        List<Integer> set = new LinkedList<>();
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("select * from article as a where user_id=?");


        preparedStatement.setInt(1, user.getId());


        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            set.add(resultSet.getInt("id"));
        }

        //System.out.println(set);

        for (int i = 0; i < set.size(); i++) {
            PreparedStatement preparedStatement1 = ApplicationContext.getConnection().prepareStatement("select a.* ,t.title as tagName, t.id as tid , c.id as  cat_id , c.title as cat_title  from temp_article_tag as tat join " +
                    "article as a on tat.article_id=a.id join tag as t on t.id=tat.tag_id join category as c on c.id=a.category_id  " +
                    "where a.id=? ;"
            );


            preparedStatement1.setInt(1, set.get(i));


            ResultSet resultSet1 = preparedStatement1.executeQuery();


            resultSet1.next();

            Article article = ArticleMapper.mapToArticleObject(resultSet1);
            article.addTag(new Tag(resultSet1.getInt("tid"), resultSet1.getString("tagName")));
            article.setCategory(new Category(resultSet1.getInt("cat_id"), resultSet1.getString("cat_title")));


            while (resultSet1.next()) {
                article.addTag(new Tag(resultSet1.getInt("tid"), resultSet1.getString("tagName")));
            }

            System.out.println(article);

            preparedStatement.close();
            preparedStatement1.close();

            System.out.println("*".repeat(90));

        }
    }


    @Override
    public <T> void add( T... str) throws SQLException {

        Article article = null;
        if( str[0] instanceof Article )
            article=(Article) str[0];

        int category_id =0;
        if( str[1] instanceof Integer)
            category_id=(int) str[1];

        TagRepository tagTable =null;
        if( str[2] instanceof TagRepository)
            tagTable=(TagRepository)str[2];

        System.out.println("enter number you want tag ...  ");

        int number = scanner.nextInt();
        scanner.nextLine();

        tagTable.showAll( category_id);
        for (int i = 1; i <= number; i++) {
            System.out.println("enter tagName ");
            String tagName = scanner.nextLine();
            int result = tagTable.checkTagExists( tagName);
            addRowToTempArticleTagTable( article.getId(), result);
        }
    }

    private void addRowToTempArticleTagTable( int articleId, int tagId) throws SQLException {
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("" +
                "insert into temp_article_tag(article_id , tag_id) values (?,?)");

        preparedStatement.setInt(1, articleId);
        preparedStatement.setInt(2, tagId);

        System.out.println("#".repeat(80));
        System.out.println("article Id : " + articleId);
        System.out.println("tag_id : " + tagId);
        System.out.println("#".repeat(80));

        preparedStatement.executeUpdate();

        preparedStatement.close();
    }



    @Override
    public void createTable() throws SQLException {
        Statement statement = ApplicationContext.getConnection().createStatement();

        statement.executeUpdate("create  table if not exists temp_article_tag(article_id int , tag_id int , " +
                "foreign key(article_id) references article(id) ," +
                "foreign key(tag_id) references  tag(id) )");
    }

    @Override
    public int size() throws SQLException {
       Statement statement=ApplicationContext.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
       int counter=0;
       ResultSet resultSet =statement.executeQuery("select  * from temp_article_tag");

       if(resultSet.last())
           counter=resultSet.getRow();

       statement.close();
       return counter;
    }
}
