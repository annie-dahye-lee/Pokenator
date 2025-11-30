package data_access;

import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.leaderboard.UserListDataAccessInterface;
import use_case.user_profile.UserProfileUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        UserListDataAccessInterface,
        UserProfileUserDataAccessInterface {

    private static final String HEADER = "username,password,score,bio,fav_pokemon,profile_photo_path,banner_path";
    private static final String OLD_HEADER = "username,password,score,bio,fav_pokemon";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * 
     * @param csvPath     the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("score", 2);
        headers.put("bio", 3);
        headers.put("fav_pokemon", 4);
        headers.put("profile_photo_path", 5);
        headers.put("banner_path", 6);

        if (csvFile.length() == 0) {
            save();
        } else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();
                boolean isOldFormat = header.equals(OLD_HEADER);

                if (!header.equals(HEADER) && !isOldFormat) {
                    throw new RuntimeException(
                            String.format("header should be%n: %s%n or %s%n but was:%n%s", HEADER, OLD_HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final String score = String.valueOf(col[headers.get("score")]);
                    final String bio = String.valueOf(col[headers.get("bio")]);
                    final String fav_pokemon = String.valueOf(col[headers.get("fav_pokemon")]);

                    String profilePhotoPath = null;
                    String bannerPath = null;
                    if (!isOldFormat && col.length > headers.get("profile_photo_path")) {
                        profilePhotoPath = col.length > headers.get("profile_photo_path") &&
                                col[headers.get("profile_photo_path")] != null &&
                                !col[headers.get("profile_photo_path")].isEmpty()
                                        ? col[headers.get("profile_photo_path")]
                                        : null;
                        bannerPath = col.length > headers.get("banner_path") &&
                                col[headers.get("banner_path")] != null &&
                                !col[headers.get("banner_path")].isEmpty()
                                        ? col[headers.get("banner_path")]
                                        : null;
                    }

                    final User user = userFactory.create(username, password, Integer.parseInt(score), bio, fav_pokemon,
                            profilePhotoPath, bannerPath);
                    accounts.put(username, user);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            // Iterate over map entries to get both username (key) and user (value)
            // The key is the username, user.getName() is the display name
            for (Map.Entry<String, User> entry : accounts.entrySet()) {
                String username = entry.getKey(); // This is the actual username (map key)
                User user = entry.getValue();
                final String profilePhotoPath = user.getProfilePhotoPath() != null ? user.getProfilePhotoPath() : "";
                final String bannerPath = user.getBannerPath() != null ? user.getBannerPath() : "";
                final String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                        username, user.getPassword(), user.getScore(), user.getBio(),
                        user.getFavPokemon(), profilePhotoPath, bannerPath);
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        // When saving, we need to find the existing entry by username
        // Since user.getName() might be the display name, we need to find the entry
        // by checking which entry has this user object
        String usernameToUse = null;
        for (Map.Entry<String, User> entry : accounts.entrySet()) {
            if (entry.getValue() == user) {
                usernameToUse = entry.getKey();
                break;
            }
        }
        // If not found, use user.getName() as fallback (for new users)
        if (usernameToUse == null) {
            usernameToUse = user.getName();
        }
        accounts.put(usernameToUse, user);
        this.save();
    }

    @Override
    public ArrayList<User> getUserList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getName(), user);
        save();
    }

    public void editProfile(User user) {
        accounts.put(user.getName(), user);
        save();
    }

    @Override
    public void updateUserProfile(String username, User user) {
        // Use the provided username as the key
        accounts.put(username, user);
        save();
    }

    @Override
    public void updateUsername(String oldUsername, String newUsername, User user) {
        // Remove old entry
        accounts.remove(oldUsername);
        // Add new entry with new username
        accounts.put(newUsername, user);
        // Update current username if it matches
        if (currentUsername != null && currentUsername.equals(oldUsername)) {
            currentUsername = newUsername;
        }
        save();
    }
}
