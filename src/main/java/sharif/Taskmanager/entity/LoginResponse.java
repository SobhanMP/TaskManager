package sharif.Taskmanager.entity;

/**
 * Created by amirmhp on 1/27/2019.
 */
public class LoginResponse {
    String token;
    Long id;

    public LoginResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
