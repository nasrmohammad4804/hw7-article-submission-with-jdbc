package service;

import domain.Article;
import domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static Scanner scannerForString;
    private static Scanner scannerForInteger;

    private static MyAdmin userAdminServices;

    public App() throws SQLException, ClassNotFoundException {
        scannerForString = new Scanner(System.in);
        scannerForInteger = new Scanner(System.in);


        userAdminServices = new MyAdmin();

    }

    public void start() throws SQLException {

        ApplicationContext app = new ApplicationContext();

        ApplicationContext.getUserRepository().createTable();


        ApplicationContext.getAccountRepository().createAccount();
        ApplicationContext.getCategoryRepository().createTable();
        ApplicationContext.getArticleRepository().createTable();
        ApplicationContext.getTagRepository().createTable();
        ApplicationContext.getTempArticleTagRepository().createTable();
        ApplicationContext.getUserAdminRepository().createTable();


        if (ApplicationContext.getUserRepository().size() == 0)
            ApplicationContext.getUserRepository().addDefault();

        if (ApplicationContext.getCategoryRepository().size() == 0)
            ApplicationContext.getCategoryRepository().addDefault();

        if (ApplicationContext.getUserAdminRepository().size() == 0)
            ApplicationContext.getUserAdminRepository().addDefaultAdmin();


        menu();

    }

    public void menu() throws SQLException {
        System.out.println("enter 1 to login ordinary user");
        System.out.println("enter 2 to register");
        System.out.println("enter 3 to show article witch published without register or login");
        System.out.println("enter 4 to show article witch published and free");
        System.out.println("enter 5 to show article witch published and dont free");
        System.out.println("enter 6 to login userAdmin ## ");
        System.out.println("enter 7 to charge of account for user !!!");
        System.out.println("enter 8 to exit the program");

        int number = scannerForInteger.nextInt();

        switch (number) {
            case 1 -> login();

            case 2 -> register();

            case 3 -> {
                ApplicationContext.getArticleRepository().showAll();
                System.out.println("-".repeat(80));
                menu();
            }

            case 4 -> {
                ApplicationContext.getArticleRepository().showAllFreeArticle();
                System.out.println("-".repeat(80));

                menu();
            }

            case 5 -> {
                ApplicationContext.getArticleRepository().showAllNonFreeArticle();
                System.out.println("-".repeat(80));

                menu();
            }

            case 6 -> {

                loginForUserAdmin();
                menu();
            }

            case 7 -> {

                chargeAccountOfUser();
                System.out.println("-".repeat(80));
                menu();
            }

            case 8 -> {
                System.out.println("have nice day bye ...!!");
                System.exit(0);

            }


            default -> {
                System.out.println("your data not valid try again ...");
                menu();
            }


        }

    }

    public void loginForUserAdmin() throws SQLException {

        System.out.println("enter userName  userAdmin .");
        String userName = scannerForString.nextLine();
        System.out.println("enter password userAdmin .");
        String password = scannerForString.nextLine();
        userAdminServices.loginUserAdmin(userName, password);
    }

    public void chargeAccountOfUser() throws SQLException {

        System.out.println("enter your userName ...");
        String userName = scannerForString.nextLine();
        User user = ApplicationContext.getUserRepository().checkExistsUser(userName);
        if (ApplicationContext.getAccountRepository().checkAccountBlockedUserForLogin(user)) { // userTable.hasUserWitchUnBlockAccount(user, connection)
            System.out.println("enter how many balance to add your account ...\n");
            int balance = scannerForInteger.nextInt();
            ApplicationContext.getAccountRepository().chargeAccount(user, balance);
        } else System.out.println("this user not exists or blocked accounted -_-\n");
    }


    public void login() throws SQLException {
        System.out.println("enter username ##");
        String userName = scannerForString.nextLine();

        System.out.println("enter password");
        String password = scannerForString.nextLine();
        User user = ApplicationContext.getUserRepository().checkExistsUser(userName, password);

        if (user == null) {
            System.out.println("not exists username with this password .. may be wrong username or password try again ...\n");
            login();
        } else {
            if (ApplicationContext.getAccountRepository().checkAccountBlockedUserForLogin(user)) {  // userTable.hasUserWitchUnBlockAccount(user, connection)
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

        User temp = ApplicationContext.getUserRepository().checkExistsUser(us.getUserName());


        if (temp == null) {
            userAdminServices.loginUserAdmin(us, this);
        } else {
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
                ApplicationContext.getTempArticleTagRepository().showAll(user);
                userPanel(user);
                break;

            case 2:
                System.out.println("enter id of article");
                int id = scannerForInteger.nextInt();
                if (ApplicationContext.getArticleRepository().ExistsArticleId(id, user)) {
                    ApplicationContext.getArticleRepository().changeArticleOfUser(user, id);
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
                menu();
        }
    }

    public void changingPassword(User user) throws SQLException {
        System.out.println("if you want changing password  " +
                "press #1 else press #2");

        switch (scannerForInteger.nextInt()) {
            case 1 -> {
                System.out.println("enter new password");

                String pass = scannerForString.nextLine();
                ApplicationContext.getUserRepository().changePasswordOfUser(user, pass);
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


        ApplicationContext.getConnection().setAutoCommit(false);
        ApplicationContext.getArticleRepository().addArticle(article, user, result);
        article.setId(ApplicationContext.getArticleRepository().size());

        ApplicationContext.getTempArticleTagRepository().add(article, result, ApplicationContext.getTagRepository());
        ApplicationContext.getConnection().commit();

    }

    public int checkCategoryExists() throws SQLException {

        ApplicationContext.getCategoryRepository().showAll();
        System.out.println("enter one category for your article");
        String title = scannerForString.nextLine();

        int result = 0;
        List<String> list = new LinkedList<>();

        Statement statement = ApplicationContext.getConnection().createStatement();

        ResultSet resultSet = statement.executeQuery("select  * from category");

        while (resultSet.next()) {
            list.add(resultSet.getString("title"));
            if (resultSet.getString("title").equals(title))
                result = resultSet.getInt("id");

        }

        if (!list.contains(title)) {

            ApplicationContext.getCategoryRepository().add(title);
            ApplicationContext.getCategoryRepository().showAll();
            return list.size() + 1;


        } else return result;
    }

    public void closeOfResource() throws SQLException {
        ApplicationContext.getConnection().close();
    }
}

