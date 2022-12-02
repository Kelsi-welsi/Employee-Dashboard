public class LoginInfo {
    private String username;
    private String password;
    private String isManager;
    private String code;

    public LoginInfo(String username, String password, String isManager, String code){
        this.username = username;
        this.password = password;
        this.isManager = isManager;
        this.code = code;
    }

    public String getUsername(){
        return  this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getisManager(){
        return this.isManager;
    }

    public String getCoder(){
        return this.code;
    }
}


