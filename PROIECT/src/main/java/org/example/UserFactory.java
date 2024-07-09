package org.example;

public class UserFactory {
    public static User createUser(AccountType type, String username, String experience, User.Information info) {
        User user;
        switch (type) {
            case Regular:
                user = new Regular();
                break;
            case Contributor:
                user = new Contributor();
                break;
            case Admin:
                user = new Admin();
                break;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
        user.setName(username);
        user.setExperience(experience);
        user.setInformation(info);
        return user;
    }


}