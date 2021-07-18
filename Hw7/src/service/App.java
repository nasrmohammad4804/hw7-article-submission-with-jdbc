package service;

import repository.*;

import java.sql.Connection;
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

    public void menu(){

    }
}
