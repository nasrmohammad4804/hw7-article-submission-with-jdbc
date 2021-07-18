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
        article.setCreateDate(resultSet.getDate("createDate"));
        article.setPublished(resultSet.getBoolean("isPublished"));
        article.setLastUpdate(resultSet.getDate("lastUpdateDate"));
        article.setPublishDate(resultSet.getDate("publishDate"));

        return article;
    }
}