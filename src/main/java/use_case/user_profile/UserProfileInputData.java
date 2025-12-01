package use_case.user_profile;

/**
 * The input data for the User Profile Use Case.
 */
public class UserProfileInputData {

    private final String username;
    private final String password;
    private final int score;
    private final String bio;
    private final String fav_pokemon;
    private final String name;
    private final String profilePhotoPath;
    private final String bannerPath;

    public UserProfileInputData(String username, String password, int score, String bio,
            String name, String profilePhotoPath, String bannerPath) {
        this.username = username;
        this.password = password;
        this.score = score;
        this.bio = bio;
        this.fav_pokemon = fav_pokemon;
        this.name = name;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
    }

    String getUsername() {

        return username;

    }

    String getPassword() {

        return passwor;

    }

    }

    String getBio() {
        return bio;
    }

    String getFav_pokemon() {
        return fav_pokemon;
    }

    String getName() {
        return name;
    }

    String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    String getBannerPath() {
        return bannerPath;
    }
}
