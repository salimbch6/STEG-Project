package models;

public class User {
    private int account_id ,id_role,etat;
    private String firstname,lastname,username,email,password,profilePic,number;

    public User() {
    }

    public User(int account_id,int id_role, String firstname, String lastname, String username,String email, String password,String profilePic,Integer etat,String number) {
        this.account_id = account_id;
        this.id_role = id_role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email=email;
        this.password = password;
        this.profilePic=profilePic;
        this.etat=etat;
        this.number=number;

    }

    public User(String firstname, String lastname, String username,String email, String password, String profilePic,Integer etat,String number) {
        this.id_role = id_role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email=email;
        this.password = password;
        this.profilePic = profilePic;
        this.etat = etat;
        this.number = number;

    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "account_id=" + account_id +
                ", id_role=" + id_role +
                ", etat=" + etat +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
