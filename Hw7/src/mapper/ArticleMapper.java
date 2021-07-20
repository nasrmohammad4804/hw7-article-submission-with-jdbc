package mapper;

import domain.Article;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleMapper {

    public static Article mapToArticleObject(ResultSet resultSet) throws SQLException {

        Article article = new Article(resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("brief"),
                resultSet.getString("content"));
        article.setCreateDate(resultSet.getTimestamp("createDate"));
        article.setPublished(resultSet.getBoolean("isPublished"));
        article.setLastUpdate(resultSet.getTimestamp("lastUpdateDate"));
        article.setPublishDate(resultSet.getTimestamp("publishDate"));
        article.setStateOfMoney("state_money");

        return article;
    }
}