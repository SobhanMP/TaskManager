package sharif.Taskmanager.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sharif.Taskmanager.data.UserRepository;
import sharif.Taskmanager.entity.LoginResponse;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.User;

import javax.xml.ws.http.HTTPException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by amirmhp on 12/11/2018.
 */
@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;
    private HashMap<String, Long> tokens = new HashMap<>();  //<token, userId>

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String addUser(RequestObject requestObject) {
        User userToAdd = ((User) requestObject.getContent());
        if (!validateUser(userToAdd)) {
            throw new HTTPException(400);
        }
        return userRepository.save(userToAdd).getID().toString();
    }


    private boolean validateUser(User user) {
        User dupUser = userRepository.findByUserName(user.getUserName());
        if (dupUser != null)
            return false;

        return true;
    }

    public void updateUserTasks(User user) {
        userRepository.save(user);
    }

    //ret value = token
    public LoginResponse login(RequestObject requestObject) {
        User userToCheck = ((User) requestObject.getContent());
        User userByUserName = userRepository.findByUserName(userToCheck.getUserName());
        if (userToCheck.getHashedPassword().equals(userByUserName.getHashedPassword())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            tokens.put(token, userByUserName.getID());
            return new LoginResponse(token, userByUserName.getID());
        }
        throw new HTTPException(401);
    }


    public boolean checkTokenAccessToUser(Long id, String token) {
        Long userIdOfToken = tokens.get(token);
        if (userIdOfToken == id /* or userIdOfToken == admin */) {
            return true;
        } else throw new HTTPException(401);
    }

    public Long getUserIdOfToken(String token) {
        Long userId = tokens.get(token);
        if (userId == null) {
            throw new HTTPException(401);
        }
        return userId;
    }


    public User getUser(RequestObject requestObject) {
        Long id = ((User) (requestObject.getContent())).getID();
        if (!checkTokenAccessToUser(id, requestObject.getToken())) {
            throw new HTTPException(401);
        } else {
            return getUser(id);
        }
    }

    public User getUser(Long userId) {
        User user = userRepository.findById(userId).get();
        if (user == null) {
            throw new HTTPException(404);
        }
        return user;
    }

    public boolean logout(String token) {
        if (tokens.containsKey(token)) {
            tokens.remove(token);
            return true;
        } else throw new HTTPException(401);
    }
}
