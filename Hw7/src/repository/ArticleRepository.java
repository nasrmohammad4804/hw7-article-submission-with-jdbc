package repository;

import domain.Article;
import domain.Category;
import domain.User;
import mapper.ArticleMapper;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class ArticleRepository implements BaseRepository{

    @Override
    public final <T> void showAll(Connection connection, T... value) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select * from article where isPublished=1 ");

        while (resultSet.next()) {

            domain.Article article = ArticleMapper.mapToArticleObject(resultSet);
            article.setCategory(new Category(resultSet.getInt("category_id"),
                    CategoryRepository.getTitleOfCategory(resultSet.getInt("category_id"), connection.createStatement())));
            article.setFree(resultSet.getBoolean("isFree"));


            article.print();
            System.out.println("*".repeat(80));
        }
    }


    public void showAllFreeArticle(Connection connection) throws SQLException {

        Statement statement=connection.createStatement();

       ResultSet resultSet =  statement.executeQuery("select  * from article  where isPublished=1 and isFree=1");

       while (resultSet.next()){
           Article article = ArticleMapper.mapToArticleObject(resultSet);
           article.setCategory(new Category(resultSet.getInt("category_id"),CategoryRepository.getTitleOfCategory(resultSet.getInt("category_id"),
                   connection.createStatement())));
           article.setFree(resultSet.getBoolean("isFree"));

           article.print();
           System.out.println("*".repeat(80));
       }

    }
    public void showAllNonFreeArticle(Connection connection) throws SQLException{

        Statement statement=connection.createStatement();

        ResultSet result =  statement.executeQuery("select  * from article as a where isPublished=1 and isFree=0 ");

        while ( result.next() ){

            Article article = ArticleMapper.mapToArticleObject(result);
            article.setCategory(new Category(result.getInt("category_id"),CategoryRepository.getTitleOfCategory(result.getInt("category_id"),
                    connection.createStatement())));
            article.setFree(result.getBoolean("isFree"));

            article.print();
            System.out.println("*".repeat(80));
        }
    }


    @Override
    public void createTable(Connection connection) throws SQLException {
        Statement statement=connection.createStatement();

        statement.executeUpdate("create table if not exists article " +
                "(id int primary key auto_increment , title varchar(50) not null ," +
                "brief varchar (50) not null , content text   , " +
                "createDate date , isPublished tinyint(1)  , isFree tinyint(1) , " +
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
                preparedStatement = connection.prepareStatement("update article as a set a.title=?, lastUpdateDate=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, title);
                preparedStatement.setDate(2,Date.valueOf(LocalDate.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("title of row is changed ..\n");
                return;


            case 2:
                sc.nextLine();
                System.out.println("enter new brief");
                String brief = sc.nextLine();
                preparedStatement = connection.prepareStatement("update article as a set a.brief=? , lastUpdateDate=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, brief);
                preparedStatement.setDate(2,Date.valueOf(LocalDate.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("brief or row is changed ..\n");
                return;


            case 3:
                sc.nextLine();
                System.out.println("enter new content");
                String content = sc.nextLine();
                preparedStatement = connection.prepareStatement("update article as a set a.content=? ,lastUpdateDate=? where a.id=?  and a.user_id=?");
                preparedStatement.setString(1, content);
                preparedStatement.setDate(2,Date.valueOf(LocalDate.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());
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
                    preparedStatement = connection.prepareStatement("update article as a set isPublished=? , publishDate=?  where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setDate(2,null);
                    preparedStatement.setInt(3, idOfArticle);
                    preparedStatement.setInt(4, user.getId());

                    preparedStatement.executeUpdate();
                    System.out.println("filed of isPublished changed to false ...\n");

                } else {
                    preparedStatement = connection.prepareStatement("update article as a set isPublished=?  , publishDate=? where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.setDate(2,Date.valueOf(LocalDate.now()));
                    preparedStatement.setInt(3, idOfArticle);
                    preparedStatement.setInt(4, user.getId());

                    preparedStatement.executeUpdate();

                    specifyTheStatusArticle(connection,idOfArticle,user);
                    System.out.println("filed of isPublished changed to true  ...\n");

                }


        }


    }
    private void specifyTheStatusArticle(Connection connection,int articleId, User user) throws SQLException{
        Scanner scanner=new Scanner(System.in);
        System.out.println("are you want article is free or noneFree if free enter free else enter noneFree ... ");

        switch (scanner.nextLine()){
            case "free" -> {
                PreparedStatement preparedStatement=connection.prepareStatement("update article as a set isFree=? where a.id=? and a.user_id=? ");
                preparedStatement.setBoolean(1,true);
                preparedStatement.setInt(2,articleId);
                preparedStatement.setInt(3,user.getId());
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
            default -> {
                System.out.println("not valid try again ");
                specifyTheStatusArticle(connection,articleId,user);
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
    public void addArticle(Connection connection, domain.Article article, User user, int categoryId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into article(title,brief,content, createDate,isPublished" +
                ",lastUpdateDate,publishDate,user_id,category_id) values  (?,?,?,?,?,?,?,?,?)");

        preparedStatement.setString(1, article.getTitle());
        preparedStatement.setString(2, article.getBrief());
        preparedStatement.setString(3, article.getContent());
        preparedStatement.setTimestamp(4, article.getCreateDate());
        preparedStatement.setBoolean(5, article.isPublished());
        preparedStatement.setTimestamp(6, article.getLastUpdate());
        preparedStatement.setTimestamp(7, article.getPublishDate());
        preparedStatement.setInt(8, user.getId());
        preparedStatement.setInt(9, categoryId);


        preparedStatement.executeUpdate();


        System.out.println("article added .... ");
        System.out.println("*".repeat(80));
        preparedStatement.close();
    }
}
