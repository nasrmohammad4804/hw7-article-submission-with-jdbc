package domain;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class User {
    private int id;
    private String userName;
    private String nationalCode;
    private Date birthDay;
    private String password;
    private List<Article> list;

    public User(int id, String userName, String nationalCode, Date birthDay, String password) {
        this.id = id;
        this.userName = userName;
        this.nationalCode = nationalCode;
        this.birthDay = birthDay;
        this.password = password;
        list = new LinkedList<>();

    }

    public User(String userName, String nationalCode, Date birthDay, String password) {
        this.userName = userName;
        this.nationalCode = nationalCode;
        this.birthDay = birthDay;
        this.password = password;

    }
    public User(String userName,String nationalCode , Date birthDay){
        this.userName=userName;
        this.nationalCode=nationalCode;
        this.birthDay=birthDay;

        setPassword(nationalCode);
    }

    public void addArticle(Article article) {
        list.add(article);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Article> getList() {
        return list;
    }

    public void setList(List<Article> list) {
        this.list = list;
    }
}
