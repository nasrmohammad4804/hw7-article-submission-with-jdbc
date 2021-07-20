package service;

import domain.Article;
import domain.User;
import repository.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Connection connection;
    private static Scanner scannerForString;
    private static  Scanner scannerForInteger;

    private static UserRepository userTable;
    private static CategoryRepository categoryTable;
    private static ArticleRepository articleRepository;
    private static TagRepository tagTable;
    private static TempArticleTagRepository tempArticleTag;
    private static AccountRepository accountRepository;

    public App() throws SQLException, ClassNotFoundException {
        scannerForString = new Scanner(System.in);
        scannerForInteger=new Scanner(System.in);

        connection = DriverManager.getConnection("jdbc:mysql://localhost/hw7",
                "root", "MohammadN@sr13804804");

        //  Class.forName("com.mysql.jdbc.Driver");

        accountRepository = new AccountRepository();
        userTable = new UserRepository();
        categoryTable = new CategoryRepository();
        articleRepository = new ArticleRepository();
        tagTable = new TagRepository();
        tempArticleTag = new TempArticleTagRepository();

    }

    public void start() throws SQLException {


        userTable.createTable(connection);
        accountRepository.createAccount(connection);
        categoryTable.createTable(connection);
        articleRepository.createTable(connection);
        tagTable.createTable(connection);
        tempArticleTag.createTable(connection);

        if (userTable.size(connection) == 0)
            userTable.addDefault(connection);

        if (categoryTable.size(connection) == 0)
            categoryTable.addDefault(connection);


        menu();

    }

    public void menu() throws SQLException {
        System.out.println("enter 1 to login ");
        System.out.println("enter 2 to register");
        System.out.println("enter 3 to show article witch published without register or login");
        System.out.println("enter 4 to show article witch published and free");
        System.out.println("enter 5 to show article witch published and dont free");
        System.out.println("enter 6 to block account of user");
        System.out.println("enter 7 to unBlock account of user ... ");
        System.out.println("enter 8 to charge of account for user !!!");
        System.out.println("enter 9 to exit the program");

        int number = scannerForInteger.nextInt();

        switch (number) {
            case 1 -> login();

            case 2 -> register();

            case 3 -> {
                articleRepository.showAll(connection, null);
                System.out.println("-".repeat(80));
                menu();
            }

            case 4 -> {
                articleRepository.showAllFreeArticle(connection);
                System.out.println("-".repeat(80));
                ;
                menu();
            }

            case 5 -> {
                articleRepository.showAllNonFreeArticle(connection);
                System.out.println("-".repeat(80));
                ;
                menu();
            }

            case 6 -> {
                blockOfUser();
                System.out.println("-".repeat(80));
                menu();
            }

            case 7 -> {
                unBlockOfUser();
                System.out.println("-".repeat(80));
                menu();
            }

            case 8 -> {
                chargeAccountOfUser();
                System.out.println("-".repeat(80));
                menu();
            }

            case 9 -> {
                System.out.println("have nice day by ...!!");
                System.exit(0);
            }
            default -> {
                System.out.println("your data not valid try again ...");
                menu();
            }


        }

    }

    public void chargeAccountOfUser() throws SQLException {

        System.out.println("enter your userName ...");
        String userName = scannerForString.nextLine();
        User user = userTable.checkExistsUser(userName, connection);
        if (accountRepository.checkAccountBlockedUserForLogin(user,connection)) { // userTable.hasUserWitchUnBlockAccount(user, connection)
            System.out.println("enter how many balance to add your account ...\n");
            int balance = scannerForInteger.nextInt();
            accountRepository.chargeAccount(user, balance, connection);
        } else System.out.println("this user not exists or blocked accounted -_-\n");
    }

    public void blockOfUser() throws SQLException {
        System.out.println("enter userName");
        String userName = scannerForString.nextLine();

        User user = userTable.checkExistsUser(userName, connection);

        UserAdmin.blockAccount(user, connection);
    }

    public void unBlockOfUser() throws SQLException {
        System.out.println("enter userName");
        String userName = scannerForString.nextLine();

        User user = userTable.checkExistsUser(userName, connection);
        UserAdmin.unBlockAccount(user, connection);
    }

    public void login() throws SQLException {
        System.out.println("enter username ##");
        String userName = scannerForString.nextLine();

        System.out.println("enter password");
        String password = scannerForString.nextLine();
        User user = userTable.checkExistsUser(userName, password, connection);

        if (user == null) {
            System.out.println("not exists username with this password .. may be wrong username or password try again ...\n");
            login();
        } else {
            if (accountRepository.checkAccountBlockedUserForLogin(user,connection)) {  // userTable.hasUserWitchUnBlockAccount(user, connection)
                changingPassword(user);
                userPanel(user);
            } else {
                System.out.println("account of user blocked you have not access to account ...\n");
                menu();
            }

        }
    }

    public void register() throws SQLException {

        System.out.println("enter an username");
        String userName = scannerForString.nextLine();
        System.out.println("enter nationalCode .. ");
        String nationalCode = scannerForString.nextLine();
        System.out.println("enter birthday");
        Date birthday = Date.valueOf(scannerForString.nextLine());


        User us = new User(userName, nationalCode, birthday);
        us =userTable.checkExistsUser(us.getUserName(), connection);

        if (us == null && UserAdmin.confirmToRegisterUser()) {
            userTable.add(connection, us);
            us.setId(userTable.size(connection));
            System.out.println("!! registered new user by username : " + userName + "\n");
            changingPassword(us);
            userPanel(us);
        } else if (us == null && !UserAdmin.confirmToRegisterUser())
            System.out.println("user admin not allow to register");

        else {
            System.out.println("this user already exists in database back to menu ");
            menu();

        }

    }

    public void userPanel(User user) throws SQLException {

        System.out.println("1. showAllArticle");
        System.out.println("2. update of article");
        System.out.println("3. create new article");
        System.out.println("4. back to firstPage");
        System.out.println("------------------------------------------------");
        switch (scannerForInteger.nextInt()) {

            case 1:
                tempArticleTag.showAll(connection, user);
                userPanel(user);
                break;

            case 2:
                System.out.println("enter id of article");
                int id = scannerForInteger.nextInt();
                if (articleRepository.ExistsArticleId(connection, id, user)) {
                    articleRepository.changeArticleOfUser(connection, user, id);
                    userPanel(user);
                } else {
                    System.out.println("this articleId not exists or not for this user !!!");
                    userPanel(user);
                    break;
                }

            case 3:
                createArticle(user);
                userPanel(user);
                break;

            case 4:
                start();
        }
    }

    private void changingPassword(User user) throws SQLException {
        System.out.println("if you want changing password  " +
                "press #1 else press #2");

        switch (scannerForInteger.nextInt()) {
            case 1 -> {
                System.out.println("enter new password");

                String pass = scannerForString.nextLine();
                userTable.changePasswordOfUser(connection, user, pass);
            }

            case 2 -> userPanel(user);

            default -> {
                System.out.println("your data not valid back to menu .. .");
                menu();
            }

        }

    }

    public void createArticle(User user) throws SQLException {
        //TODO ---------------------------//

        int result = checkCategoryExists();


        System.out.println("enter a title for article ");
        String title = scannerForString.nextLine();

        System.out.println("enter brief");
        String brief = scannerForString.nextLine();

        System.out.println("enter a content");
        String content = scannerForString.nextLine();

        System.out.println("enter a create date");
        Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());


        Article article = new Article(title, brief, content);
        article.setCreateDate(createDate);
        article.setStateOfMoney(" ");
        article.setPublished(false);
        article.setLastUpdate(null);
        article.setPublishDate(null);


        connection.setAutoCommit(false);
        articleRepository.addArticle(connection, article, user, result);
        article.setId(articleRepository.size(connection));

        tempArticleTag.add(connection, article, result, tagTable);
        connection.commit();

    }

    public int checkCategoryExists() throws SQLException {

        categoryTable.showAll(connection, null);
        System.out.println("enter one category for your article");
        String title = scannerForString.nextLine();

        int result = 0;
        List<String> list = new LinkedList<>();

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("select  * from category");

        while (resultSet.next()) {
            list.add(resultSet.getString("title"));
            if (resultSet.getString("title").equals(title))
                result = resultSet.getInt("id");

        }

        if (!list.contains(title)) {

            categoryTable.add(connection, title);
            categoryTable.showAll(connection, null);
            return list.size() + 1;


        } else return result;
    }

    public void closeOfResource() throws SQLException {
        connection.close();
    }
}

