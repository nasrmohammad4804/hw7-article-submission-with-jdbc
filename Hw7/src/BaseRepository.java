import java.sql.Connection;
import java.util.List;

public interface BaseRepository {


    void showAll(Connection connection, Object object);

    void createTable(Connection connection);

    private void add(Connection connection, Object str) {
        //...........
    }

    private void addDefault(Connection connection) {
        // .........
    }

    int  size(Connection connection);

}
