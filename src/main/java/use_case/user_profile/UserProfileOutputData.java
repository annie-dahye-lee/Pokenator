package use_case.user_profile;

/**
 * The output data for the User Profile use case.
 */
public class UserProfileOutputData {

    private final String username;
    private final String name;
    private final String bio;
    private final String favPokemon;
    private final String profilePhotoPath;
    private final String bannerPath;

    public UserProfileOutputData(String username, String name, String bio, String favPokemon, 
                                String profilePhotoPath, String bannerPath) {
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.favPokemon = favPokemon;
        this.profilePhotoPath = profilePhotoPath;
        this.bannerPath = bannerPath;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getFavPokemon() {
        return favPokemon;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public String getBannerPath() {
        return bannerPath;
    }
}

