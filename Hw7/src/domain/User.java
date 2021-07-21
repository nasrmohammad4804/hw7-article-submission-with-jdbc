package domain;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class User extends Person{

    private String nationalCode;
    private Date birthDay;
    private Account account;
    private List<Article> list;

    public User(int id, String userName, String nationalCode, Date birthDay, String password) {

        super(id,userName,password);
        this.nationalCode = nationalCode;
        this.birthDay = birthDay;
        list = new LinkedList<>();

    }

    public User(String userName , String nationalCode , Date birthDay){
        super(userName);
        this.nationalCode=nationalCode;
        this.birthDay=birthDay;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", nationalCode='" + nationalCode + '\'' +
                ", birthDay=" + birthDay +
                ", password='" + password + '\'' +
                ", account=" + account +
                '}';
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void addArticle(Article article) {
        list.add(article);
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

    public List<Article> getList() {
        return list;
    }

    public void setList(List<Article> list) {
        this.list = list;
    }
}
