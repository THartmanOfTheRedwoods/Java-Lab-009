/*
*Bridget Acosta
* 10/26/2022
 */

//Create a Class called User that has 2 String instance variables
public class User {
    private String username;
    private String passHash;

    //Create a User constructor that has 2 parameters **username** and **passHash**
    public User(String username, String passHash) {

        //assigns these parameters to its instance variables
        this.username = username;
        this.passHash = passHash;

    }

    //Create method getUsername that return the instance variable respectively
    public String getUsername() {
        return username;
    }
    //Create method getPassHash that return the instance variable respectively
    public String getPassHash () {
        return passHash;
    }
}
