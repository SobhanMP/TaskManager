package sharif.Taskmanager.manager;

import sharif.Taskmanager.data.*;
import sharif.Taskmanager.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!validateUser(userToAdd)){
            throw new HTTPException(400);
        }
        return userRepository.save(userToAdd).getID().toString();
    }

    private boolean validateUser (User user){
        User dupUser = userRepository.findByUserName(user.getUserName());
        if (dupUser != null)
            return false;

        return true;
    }
    //ret value = token
    public String login(RequestObject requestObject) {
        User userToCheck = ((User) requestObject.getContent());
        User userByUserName = userRepository.findByUserName(userToCheck.getUserName());
        if (userToCheck.getHashedPassword().equals(userByUserName.getHashedPassword())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            tokens.put(token, userToCheck.getID());
            return token;
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

    public void saveUser(User user){
        userRepository.save(user);
    }

    public boolean logout(String token) {
        if (tokens.containsKey(token)) {
            tokens.remove(token);
            return true;
        } else throw new HTTPException(401);
    }
}
