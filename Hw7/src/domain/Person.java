package domain;

public class Person {
    protected int id;
    protected String userName;
    protected String password;

    public Person(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    public Person(String userName, String password){

        this.userName=userName;
        this.password=password;
    }
    public Person(String userName){
        this.userName=userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
