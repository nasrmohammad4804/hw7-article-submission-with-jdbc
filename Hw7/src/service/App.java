package service;

import domain.Account;
import domain.User;
import repository.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private static Connection connection;
    private static Scanner scanner;

    private static UserRepository userTable;
    private static CategoryRepository categoryTable;
    private static ArticleRepository articleRepository;
    private static TagRepository tagTable;
    private static TempArticleTagRepository tempArticleTag;
    private static AccountRepository accountRepository;

    public App() throws SQLException, ClassNotFoundException {
        scanner = new Scanner(System.in);

        connection = DriverManager.getConnection("jdbc:mysql://localhost/hw7",
                "root", "MohammadN@sr13804804");

        //  Class.forName("com.mysql.jdbc.Driver");


        userTable = new UserRepository();
        categoryTable = new CategoryRepository();
        articleRepository = new ArticleRepository();
        tagTable = new TagRepository();
        tempArticleTag = new TempArticleTagRepository();
        accountRepository=new AccountRepository();
    }
    public void start() throws SQLException {

        userTable.createTable(connection);
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

        int number=scanner.nextInt();
        scanner.nextLine();
        switch (number){
            case 1 : login();
                     break;

            case 2 : register();
                     break;

            case 3 : articleRepository.showAll(connection,null);
                     menu();

            case 4 :  articleRepository.showAllFreeArticle(connection);
                      menu();

            case 5 : articleRepository.showAllNonFreeArticle(connection);

            case 6 : blockOfUser();
                     menu();

            case 7 : unBlockOfUser();
                     menu();

            case 8 : chargeAccountOfUser();
                    menu();

            case 9 :
                System.out.println("have nice day by ...!!");
                System.exit(0);


        }

    }
    public void chargeAccountOfUser() throws SQLException {

        System.out.println("enter your userName ...");
        String userName =scanner.nextLine();
        User user = userTable.checkExistsUser(userName,connection) ;
        if(userTable.hasUserWitchUnBlockAccount(user,connection) ){
            System.out.println("enter how many balance to add your account ...");
            int balance=scanner.nextInt();
            accountRepository.chargeAccount(user,balance,connection);
        }
        else System.out.println("this user not exists or blocked accounted -_-");
    }

    public void blockOfUser() throws SQLException {
        System.out.println("enter userName");
        String userName = scanner.nextLine();

      User user = userTable.checkExistsUser(userName,connection);

            UserAdmin.blockAccount(user,connection);
      }

      public void unBlockOfUser()throws SQLException{
          System.out.println("enter userName");
          String userName =scanner.nextLine();

          User user =userTable.checkExistsUser(userName,connection);
          UserAdmin.unBlockAccount(user,connection);
      }

    public void login() throws SQLException {
        System.out.println("enter username ##");
        String userName=scanner.nextLine();

        System.out.println("enter password");
        String password=scanner.nextLine();
        User user =userTable.checkExistsUser(userName,password,connection);
        if(user== null) {
            System.out.println("not exists username with this password .. may be wrong username or password try again ...");
            login();
        }
        else {
            if(accountRepository.checkAccountBlockedUserForLogin(user,connection))
                userPanel(user);

            else {
                System.out.println("account of user blocked you have not access to account . .");
                menu();
            }

        }
    }
    public void register() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter an username");
        String userName = sc.nextLine();
        System.out.println("enter nationalCode .. ");
        String nationalCode = sc.nextLine();
        System.out.println("enter birthday");
        Date birthday = Date.valueOf(sc.nextLine());


        User us = new User(userName, nationalCode, birthday);

        if(userTable.checkExistsUser(us.getUserName(),connection)==null && UserAdmin.confirmToRegisterUser()){
            userTable.add(connection,us);
            us.setId(userTable.size(connection));
            System.out.println("!! registered new user by username : " + userName + "\n");
            changingPassword(us);
            userPanel(us);
        }
        else if(userTable.checkExistsUser(us.getUserName(),connection)==null && !UserAdmin.confirmToRegisterUser())
            System.out.println("user admin not allow to register");

        else {
            System.out.println("this user already exists in database back to menu ");
            menu();

        }

    }

    public void userPanel(User user){

    }
    private void changingPassword(User user) throws SQLException {
        System.out.println("if you want changing password  " +
                "press #1 else press #2");

        switch (scanner.nextInt()) {
            case 1:
                System.out.println("enter new password");
                scanner.nextLine();
                String pass = scanner.nextLine();
                userTable.changePasswordOfUser(connection, user, pass);
                break;

            case 2:
                break;

            default:
                System.out.println("your data not valid back to menu .. .");
                menu();
        }

    }
}
