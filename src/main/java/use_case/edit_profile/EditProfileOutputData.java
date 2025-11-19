package use_case.edit_profile;

public class EditProfileOutputData {

    private final String username;

    public EditProfileOutputData(String username) { this.username = username; }

    public String getUsername() {
        return username;
    }
}
