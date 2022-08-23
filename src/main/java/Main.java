import manager.EventManager;
import manager.UserManager;
import model.Event;
import model.User;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        UserManager userManager = new UserManager();

        User user = new User();
        userManager.addUser(user);
        System.out.println(user);
        List<User> showUsers = userManager.showUsers();
        for (User users : showUsers) {
            System.out.println(users);
        }



        List<Event> showEvent = eventManager.showEvents();
        for (Event event : showEvent) {
            System.out.println(event);
        }

    }
}

