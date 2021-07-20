package service;

import repository.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationContext {
    private static Connection connection ;
    private static AccountRepository accountRepository;
    private static ArticleRepository articleRepository;
    private static CategoryRepository categoryRepository;
    private static TagRepository tagRepository ;
    private static TempArticleTagRepository tempArticleTagRepository;
    private static UserAdminRepository userAdminRepository;
    private static UserRepository userRepository;

    public ApplicationContext() throws SQLException {
        accountRepository=new AccountRepository();
        articleRepository=new ArticleRepository();
        categoryRepository=new CategoryRepository();
        tagRepository=new TagRepository();
        tempArticleTagRepository=new TempArticleTagRepository();
        userAdminRepository=new UserAdminRepository();
        userRepository=new UserRepository();
        connection = DriverManager.getConnection("jdbc:mysql://localhost/hw7",
                "root", "MohammadN@sr13804804");
    }

    public static AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public static ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public static CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public static TagRepository getTagRepository() {
        return tagRepository;
    }

    public static TempArticleTagRepository getTempArticleTagRepository() {
        return tempArticleTagRepository;
    }

    public static UserAdminRepository getUserAdminRepository() {
        return userAdminRepository;
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static Connection getConnection() {
        return connection;
    }
}
