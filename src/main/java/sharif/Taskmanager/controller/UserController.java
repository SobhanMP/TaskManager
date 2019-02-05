package sharif.Taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharif.Taskmanager.entity.LoginResponse;
import sharif.Taskmanager.entity.MembershipRequest;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.User;
import sharif.Taskmanager.manager.UserManager;

/**
 * Created by amirmhp on 12/15/2018.
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public boolean addUser(@RequestBody User user) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(user);
        return userManager.addUser(requestObject) != "-1";
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody User user)  {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(user);
        return userManager.login(requestObject);
    }


    @GetMapping(value = "/logout")
    public boolean logout(@RequestHeader(value = "token") String token) {
        System.out.println("got the message");
        return userManager.logout(token);
    }

    @GetMapping(value = "/{id}")
    public User getUserProfile(@PathVariable String id, @RequestHeader(value = "token") String token)  {
        RequestObject requestObject = new RequestObject();
        requestObject.setRequesterId(id);
        User user = new User();
        user.setID(Long.parseLong(id));
        requestObject.setContent(user);
        requestObject.setToken(token);
        return userManager.getUser(requestObject);
    }

    @GetMapping(value = "/answermembership/{id}")
    public void answerMembership(@PathVariable String id, @RequestHeader(value = "token") String token,@RequestHeader(value = "acpt") int accept) {
        boolean acceptBool = accept==1;
        RequestObject requestObject = new RequestObject();
        MembershipRequest membershipRequest = new MembershipRequest();
        membershipRequest.setId(Long.parseLong(id));
        requestObject.setContent(membershipRequest);
        requestObject.setToken(token);
        userManager.answerMembership(requestObject, acceptBool);
    }
    @CrossOrigin
    @RequestMapping(value = "/addmember", method = RequestMethod.POST)
    public void addMember(@RequestBody MembershipRequest membershipRequest, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(membershipRequest);
        requestObject.setToken(token);
        userManager.addMember(requestObject);
    }

//    @RequestMapping(value = "/getusernames", method = RequestMethod.POST)
//    public List<String> getUsernamesByUid(@RequestBody List<Long> userIds, @RequestHeader(value = "token") String token) {
//        return userManager.getUsernames(userIds, token);
//    }




}
