package domain;

public class UserAdmin  extends Person{
    private String name;
    private String family;
    private int age;

    public UserAdmin(int id, String userName, String passWord){
        super(id,userName,passWord);

    }

    public UserAdmin(String name, String family, int age,  String userName, String passWord) {
        super(userName,passWord);
        this.name = name;
        this.family = family;
        this.age = age;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "UserAdmin{" +
                "name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", age=" + age +
                ", id=" + id+
                ", userName='" + userName + '\'' +
                ", passWord='" + password + '\'' +
                '}';
    }
}
