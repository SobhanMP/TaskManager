package sharif.Taskmanager.manager;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sharif.Taskmanager.data.MembershipRequestRepository;
import sharif.Taskmanager.data.UserRepository;
import sharif.Taskmanager.entity.LoginResponse;
import sharif.Taskmanager.entity.MembershipRequest;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.User;

import javax.xml.ws.http.HTTPException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by amirmhp on 12/11/2018.
 */
@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MembershipRequestRepository membershipRequestRepository;
    private HashMap<String, Long> tokens = new HashMap<>();  //<token, userId>
    private final Long adminUID = 1000L;

    public UserManager(UserRepository userRepository, MembershipRequestRepository membershipRequestRepository) {
        this.userRepository = userRepository;
        this.membershipRequestRepository = membershipRequestRepository;
    }


    public String addUser(RequestObject requestObject) {
        User userToAdd = ((User) requestObject.getContent());
        if (!validateUser(userToAdd)) {
            throw new HTTPException(400);
        }
        userToAdd.setTaskPoint(0);
        return userRepository.save(userToAdd).getID().toString();
    }


    private boolean validateUser(User user) {
        User dupUser = userRepository.findByUserName(user.getUserName());
        if (dupUser != null)
            return false;
        return true;
    }

    public User updateUserTasks(User user) {
        return userRepository.save(user);
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
        try {
            if (!tokens.containsKey(token)) {
                throw new HTTPException(401);
            }
            Long userIdOfToken = tokens.get(token);
            if (userIdOfToken == id || userIdOfToken == adminUID) {
                return true;
            }
            if (userIdOfToken.equals(adminUID) && id == -1L) {
                return true;
            }
        } catch (Exception e) {
            throw new HTTPException(401);
        }
        throw new HTTPException(401);
    }

    public Long getUserIdOfToken(String token) {
        if (!tokens.containsKey(token)) {
            throw new HTTPException(401);
        }
        Long userId = tokens.get(token);
        return userId;
    }


    public User getUser(RequestObject requestObject) {
        Long id = ((User) (requestObject.getContent())).getID();
        if (!checkTokenAccessToUser(id, requestObject.getToken())) {
            throw new HTTPException(401);
        }
        return getUser(id);

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

    public User removeUser(RequestObject requestObject) {
        Long userToRemoveId = ((User) (requestObject.getContent())).getID();
        if (!checkTokenAccessToUser(userToRemoveId, requestObject.getToken())) {
            throw new HTTPException(401);
        }
        User user = userRepository.findById(userToRemoveId).get();
        userRepository.deleteById(userToRemoveId);
        return user;
    }

    public List<User> getAllUsers(RequestObject requestObject) {
        if (!checkTokenAccessToUser(-1L, requestObject.getToken())) {
            throw new HTTPException(401);
        }
        return Lists.newArrayList(userRepository.findAll());
    }

    public void addMember(RequestObject requestObject) {
        MembershipRequest request = (MembershipRequest) requestObject.getContent();
        User assignee = userRepository.findByUserName(request.getUsername());
        request = membershipRequestRepository.save(request);
        assignee.getMembershipRequests().add(request);
        userRepository.save(assignee);
    }

//    public List<String> getUsernames(List<Long> userIds, String token) {
//        ArrayList<String> usernames = new ArrayList<>();
//        Long userId = getUserIdOfToken(token);
//        User user = userRepository.findById(userId).get();
//        User tempUser;
//        for (Long aLong : user.getMembersUid()) {
//            tempUser = userRepository.findById(aLong).get();
//            if (tempUser != null) {
//                usernames.add(tempUser.getUserName());
//            }
//        }
//        return usernames;
//    }

    public void answerMembership(RequestObject requestObject, boolean accept) {
        MembershipRequest membershipRequest = (MembershipRequest) requestObject.getContent();
        membershipRequest = membershipRequestRepository.findById(membershipRequest.getId()).get();
        User assignee = userRepository.findByUserName(membershipRequest.getUsername());
        if (accept) {
            User requester = userRepository.findByUserName(membershipRequest.getRequesterUsername());
            boolean hasThatMember = false;
            for (User user : requester.getMembers()) {
                if (user.getID() == assignee.getID()) {
                    hasThatMember = true;
                    break;
                }
            }
            if (!hasThatMember) {
                requester.getMembers().add(assignee);
                userRepository.save(requester);
            }
        }
        MembershipRequest membershipRequestToDelete = null;
        boolean found = false;
        for (MembershipRequest request : assignee.getMembershipRequests()) {
            if (request.getId() == membershipRequest.getId()) {
                membershipRequestToDelete = request;
                found = true;
            }
        }
        if (found){
            assignee.getMembershipRequests().remove(membershipRequestToDelete);
            userRepository.save(assignee);
        }
    }


}
