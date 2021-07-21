package repository;

import domain.Article;
import domain.Category;
import domain.User;
import mapper.ArticleMapper;
import service.ApplicationContext;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArticleRepository implements BaseRepository {
    private Scanner scannerForString = new Scanner(System.in);
    private Scanner scannerForInteger = new Scanner(System.in);

    @Override
    public final <T> void showAll(T... value) throws SQLException {

        Statement statement = ApplicationContext.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery("select id, title, brief  from article where isPublished=1 ;");
        List<Integer> list = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println("id : " + resultSet.getInt("id") + "   title : " + resultSet.getString("title") + "   brief : " +
                    resultSet.getString("brief"));
            list.add(resultSet.getInt("id"));
        }
        System.out.println("enter number want you article id");
        int number = scannerForInteger.nextInt();

        if (!list.contains(number)) {
            System.out.println("sorry this article not exists ...");

        } else {

            PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("select * from article where isPublished=1 and id=?");
            preparedStatement.setInt(1, number);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {

                domain.Article article = ArticleMapper.mapToArticleObject(result);
                article.setCategory(new Category(result.getInt("category_id"),
                        CategoryRepository.getTitleOfCategory(result.getInt("category_id"),
                                ApplicationContext.getConnection().createStatement())));


                article.print();
                System.out.println("*".repeat(80));
            }
        }

    }


    public void showAllFreeArticle() throws SQLException {

        Statement statement = ApplicationContext.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery("select  * from article  where isPublished=1 and state_money='free'");

        while (resultSet.next()) {
            Article article = ArticleMapper.mapToArticleObject(resultSet);
            article.setCategory(new Category(resultSet.getInt("category_id"), CategoryRepository.getTitleOfCategory(resultSet.getInt("category_id"),
                    ApplicationContext.getConnection().createStatement())));


            article.print();
            System.out.println("*".repeat(80));
        }

    }

    public void showAllNonFreeArticle() throws SQLException {

        Statement statement = ApplicationContext.getConnection().createStatement();

        ResultSet result = statement.executeQuery("select  * from article as a where isPublished=1 and state_money='nonFree' ");

        while (result.next()) {

            Article article = ArticleMapper.mapToArticleObject(result);
            article.setCategory(new Category(result.getInt("category_id"), CategoryRepository.getTitleOfCategory(result.getInt("category_id"),
                    ApplicationContext.getConnection().createStatement())));


            article.print();
            System.out.println("*".repeat(80));
        }
    }


    @Override
    public void createTable() throws SQLException {
        Statement statement = ApplicationContext.getConnection().createStatement();

        statement.executeUpdate("create table if not exists article " +
                "(id int primary key auto_increment , title varchar(50) not null ," +
                "brief varchar (50) not null , content text   , " +
                "createDate timestamp , isPublished tinyint(1)  , state_money varchar(20) , " +
                "lastUpdateDate timestamp  , publishDate timestamp , " +
                "user_id int , category_id int , " +
                "foreign key  (user_id) references user(id) , " +
                "foreign key (category_id) references category(id))");
    }

    @Override
    public int size() throws SQLException {
        int number = 0;
        Statement statement = ApplicationContext.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from article ");

        while (resultSet.next())
            number++;

        return number;
    }

    public void changeArticleOfUser(User user, int idOfArticle) throws SQLException {

        Scanner sc = new Scanner(System.in);
        // showArticleOfUser(connection, userDomain);

        PreparedStatement preparedStatement;

        System.out.println("1. change title\n2.change brief\n3.change content\n4.isPublish\n5.state_money");

        int result = scannerForInteger.nextInt();

        switch (result) {

            case 1:

                System.out.println("enter new title");
                String title = scannerForString.nextLine();
                preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set a.title=?, lastUpdateDate=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, title);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("title of row is changed ..\n");
                break;


            case 2:

                System.out.println("enter new brief");
                String brief = scannerForString.nextLine();
                preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set a.brief=? , lastUpdateDate=? where a.id=? and a.user_id=?");
                preparedStatement.setString(1, brief);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("brief or row is changed ..\n");
                break;


            case 3:

                System.out.println("enter new content");
                String content = scannerForString.nextLine();
                preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set a.content=? ,lastUpdateDate=? where a.id=?  and a.user_id=?");
                preparedStatement.setString(1, content);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setInt(3, idOfArticle);
                preparedStatement.setInt(4, user.getId());

                preparedStatement.executeUpdate();
                System.out.println("content of row changed ...\n");

                break;

            case 4:
                preparedStatement = ApplicationContext.getConnection().prepareStatement("select isPublished from article as a where a.id=? and a.user_id=?");
                preparedStatement.setInt(1, idOfArticle);
                preparedStatement.setInt(2, user.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                boolean bool = false;
                while (resultSet.next())
                    bool = resultSet.getBoolean("isPublished");
                if (bool) {
                    preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set isPublished=? , publishDate=?  where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setDate(2, null);
                    preparedStatement.setInt(3, idOfArticle);
                    preparedStatement.setInt(4, user.getId());

                    preparedStatement.executeUpdate();
                    System.out.println("filed of isPublished changed to false ...\n");

                } else {
                    preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set isPublished=?  , publishDate=? where a.id=? and a.user_id=?");
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    preparedStatement.setInt(3, idOfArticle);
                    preparedStatement.setInt(4, user.getId());

                    preparedStatement.executeUpdate();

                    specifyTheStatusArticle(idOfArticle, user);
                    System.out.println("filed of isPublished changed to true  ...\n");

                }
                break;
            case 5:
                PreparedStatement preparedStatement1 = ApplicationContext.getConnection().prepareStatement("select * from article as a where a.id=? and a.user_id=? and a.isPublished=? ;");

                int counter = 0;
                preparedStatement1.setInt(1, idOfArticle);
                preparedStatement1.setInt(2, user.getId());
                preparedStatement1.setBoolean(3, true);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                String str = "";
                while (resultSet1.next()) {
                    counter++;
                    str = resultSet1.getString("state_money");
                }


                if (counter > 0) {
                    Scanner scanner = new Scanner(System.in);
                    switch (str) {
                        case "free" -> {
                            System.out.println("this article state is free are you want to nonFree enter nonFree");
                            String test = scanner.nextLine();
                            if (!test.equals("nonFree"))
                                changeArticleOfUser(user, idOfArticle);

                            PreparedStatement preparedStatement2 = ApplicationContext.getConnection().prepareStatement("update article set state_money=? where id=? and user_id=?");
                            preparedStatement2.setString(1, "nonFree");
                            preparedStatement2.setInt(2, idOfArticle);
                            preparedStatement2.setInt(3, user.getId());
                            preparedStatement2.executeUpdate();
                            preparedStatement2.close();
                            System.out.println("state_money changed of free to nonFree");
                        }
                        case "nonFree" -> {
                            System.out.println("this article state is nonFree are you want to free enter free");
                            String test = scanner.nextLine();
                            if (!test.equals("free"))
                                changeArticleOfUser(user, idOfArticle);

                            PreparedStatement preparedStatement3 = ApplicationContext.getConnection().prepareStatement("update article as a set a.state_money=? where a.id=? and a.user_id=?");
                            preparedStatement3.setString(1, "free");
                            preparedStatement3.setInt(2, idOfArticle);
                            preparedStatement3.setInt(3, user.getId());
                            preparedStatement3.executeUpdate();
                            preparedStatement3.close();
                            System.out.println("state_money changed of nonFree to free");
                        }

                    }

                }
        }

    }

    private void specifyTheStatusArticle(int articleId, User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("are you want article is free or noneFree if free enter free else enter noneFree ... ");

        switch (scanner.nextLine()) {
            case "free" -> {
                PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set state_money=? where a.id=? and a.user_id=? ");
                preparedStatement.setString(1, "free");
                preparedStatement.setInt(2, articleId);
                preparedStatement.setInt(3, user.getId());
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }
            case "nonFree" -> {
                PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("update article as a set state_money=? where a.id=? and a.user_id=? ");
                preparedStatement.setString(1, "nonFree");
                preparedStatement.setInt(2, articleId);
                preparedStatement.setInt(3, user.getId());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            default -> System.out.println("not valid your input ... ");

        }
    }

    public boolean ExistsArticleId(int id, User user) throws SQLException {

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("select * from article as a where a.id=? and a.user_id=? ");
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

    public void addArticle(domain.Article article, User user, int categoryId) throws SQLException {
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement("insert into article(title,brief,content, createDate,isPublished" +
                ",lastUpdateDate,publishDate,user_id,category_id,state_money) values  (?,?,?,?,?,?,?,?,?,?)");

        preparedStatement.setString(1, article.getTitle());
        preparedStatement.setString(2, article.getBrief());
        preparedStatement.setString(3, article.getContent());
        preparedStatement.setTimestamp(4, article.getCreateDate());
        preparedStatement.setBoolean(5, article.isPublished());
        preparedStatement.setTimestamp(6, article.getLastUpdate());
        preparedStatement.setTimestamp(7, article.getPublishDate());
        preparedStatement.setInt(8, user.getId());
        preparedStatement.setInt(9, categoryId);
        preparedStatement.setString(10, article.getStateOfMoney());


        preparedStatement.executeUpdate();


        System.out.println("article added .... ");
        System.out.println("*".repeat(80));
        preparedStatement.close();
    }
}
