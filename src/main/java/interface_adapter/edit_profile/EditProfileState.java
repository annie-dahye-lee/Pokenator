package interface_adapter.edit_profile;

import entity.User;

public class EditProfileState {

    private String username = "";
    private String password = "";
    private int score = 0;
    private String bio = "";
    private String fav_pokemon = "";
    private String profileError;

    public EditProfileState(EditProfileState copy) {
        score = copy.score;
        bio = copy.bio;
        fav_pokemon = copy.fav_pokemon;
        profileError = copy.profileError;
    }

    public EditProfileState(User u) {
        if (u != null) {
            username = u.getName();
            password = u.getPassword();
            score = u.getScore();
            bio = u.getBio();
            fav_pokemon = u.getFavPokemon();
        }
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {}

    public String getPassword() { return password; }

    public void setPassword(String password) {}

    public void setScore(int score) { this.score = score; }

    public int getScore() { return score; }

    public void setBio(String bio) { this.bio = bio; }

    public String getBio() { return bio; }

    public void setFav_pokemon(String fav_pokemon) {  this.fav_pokemon = fav_pokemon; }

    public String getFav_pokemon() { return fav_pokemon; }

    public void setProfileError(String error) {
        this.profileError = error;
    }

    public String getProfileError() { return profileError; }
}
