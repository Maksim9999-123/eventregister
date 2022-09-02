package manager;

import db.ConnectionPool;
import model.Event;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private ConnectionPool pool = new ConnectionPool(5);

    private Connection connection = null;

    private EventManager eventManager = new EventManager();

    public void add(User user) {
        String sql = "insert into user(name,surname,email,event_id) VALUES (?,?,?,?)";
        try {
            Connection connection = pool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getEvent().getId());

            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAll() {
        String sql = "select * from user";
        List<User> users = new ArrayList<>();
        try {
            Connection connection = pool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User getById(int id) {
        String sql = "select * from user where id = " + id;
        try {
            Connection connection = pool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setEmail(resultSet.getString("email"));
        int eventId = resultSet.getInt("event_id");
        Event event = eventManager.getById(eventId);
        user.setEvent(event);

        return user;
    }
}