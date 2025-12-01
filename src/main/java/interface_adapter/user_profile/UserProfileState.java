package interface_adapter.user_profile;

import entity.User;

public class UserProfileState {

    private String username = "";
    private String password = "";
    private int score = 0;
    private String bio = "";
    private String fav_pokemon = "";
    private String name = "";
    private String profilePhotoPath = "";
    private String bannerPath = "";
    private String profileError;

    public UserProfileState(UserProfileState copy) {
        username = copy.username;
        password = copy.password;
        score = copy.score;
        bio = copy.bio;
        fav_pokemon = copy.fav_pokemon;
        name = copy.name;
        profilePhotoPath = copy.profilePhotoPath;
        bannerPath = copy.bannerPath;
        profileError = copy.profileError;
    }

    public UserProfileState(User u) {
        if (u != null) {
            username = u.getName();
            password = u.getPassword();
            score = u.getScore();
            bio = u.getBio();
            fav_pokemon = u.getFavPokemon();
            name = u.getName();
            profilePhotoPath = u.getProfilePhotoPath() != null ? u.getProfilePhotoPath() : "";
            bannerPath = u.getBannerPath() != null ? u.getBannerPath() : "";
        }
    }

    public UserProfileState() {
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public void setBio(String bio) { this.bio = bio; }

    public String getBio() { return bio; }

    public void setFav_pokemon(String fav_pokemon) { this.fav_pokemon = fav_pokemon; }

    public String getFav_pokemon() { return fav_pokemon; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath; }

    public String getProfilePhotoPath() { return profilePhotoPath; }

    public void setBannerPath(String bannerPath) { this.bannerPath = bannerPath; }

    public String getBannerPath() { return bannerPath; }

    public void setProfileError(String error) {
        this.profileError = error;
    }

    public String getProfileError() { return profileError; }
}

