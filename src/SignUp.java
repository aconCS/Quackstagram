public class SignUp {

    public SignUp(){
        //Some GUI code
    }

    public void createUser(String username, String email, String password){
        if(isUsernameValid(username)){
            if(isEmailValid(username, email)){ new User(username, email, password); }
        }
        else{
            System.out.println("This username already exists. Please enter a new username.");
        }
    }

    public boolean isUsernameValid(String username){ return !User.userInfo.containsKey(username); }
    public boolean isEmailValid(String username, String email){ return !User.userInfo.get(username)[0].equals(email); }
}
