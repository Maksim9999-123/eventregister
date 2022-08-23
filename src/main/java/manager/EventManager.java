package manager;

import db.DBConnectorProvider;
import model.Event;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private Connection connection = DBConnectorProvider.getProvider().getConnection();

    public void addEvent(Event event) {
        try {
            String query = "INSERT INTO `event` (name,place,is_online,price,event_type) " +
                    "VALUES(?,?,?,?,?);";
            PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, event.getName());
            pStatement.setString(2, event.getPlace());
            pStatement.setObject(3, event.isOnline());
            pStatement.setDouble(4, event.getPrice());
            pStatement.setString(5, event.getEventType().name());
            System.out.println(query);
            pStatement.executeUpdate();
            ResultSet generatedKeys = pStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                event.setId(id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Event> showEvents() {
        String sql = "SELECT * from event";
        List<Event> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Event event = Event.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .place(resultSet.getString(3))
                        .isOnline(resultSet.getBoolean(4))
                        .price(resultSet.getDouble(5))
                        .build();
                result.add(event);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

}
