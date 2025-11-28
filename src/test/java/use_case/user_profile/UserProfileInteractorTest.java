package use_case.user_profile;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameDashboard;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileInteractorTest {

    private UserProfileUserDataAccessInterface userRepository;
    private UserFactory userFactory;
    private GameDashboard dashboard;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new TestUserProfileDataAccessObject();
        userFactory = new UserFactory();
        dashboard = new TestGameDashboard();

        // Create a test user
        testUser = userFactory.create("testuser", "password", 100, "Old bio", "Pikachu",
                "path/to/photo.jpg", "path/to/banner.jpg");
        testUser.setName("Old Display Name");
        userRepository.updateUserProfile("testuser", testUser);
        ((TestGameDashboard) dashboard).setCurrentUser("testuser");
    }

    @Test
    void successTest_UpdateDisplayNameOnly() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "New bio", "Charizard", "New Display Name",
                "path/to/photo.jpg", "path/to/banner.jpg");

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                assertEquals("New Display Name", outputData.getName());
                assertEquals("New bio", outputData.getBio());
                assertEquals("Charizard", outputData.getFavPokemon());
                assertEquals("path/to/photo.jpg", outputData.getProfilePhotoPath());
                assertEquals("path/to/banner.jpg", outputData.getBannerPath());

                // Verify user was updated in repository
                User updatedUser = userRepository.get("testuser");
                assertNotNull(updatedUser);
                assertEquals("New Display Name", updatedUser.getName());
                assertEquals("New bio", updatedUser.getBio());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_UpdateWithNewUsername() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", "newusername", null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("newusername", outputData.getUsername());
                assertEquals("Display Name", outputData.getName());

                // Verify old username no longer exists
                assertNull(userRepository.get("testuser"));
                // Verify new username exists
                assertNotNull(userRepository.get("newusername"));
                assertEquals("newusername", ((TestGameDashboard) dashboard).getCurrentUser());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_UpdateWithNewPassword() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, "newpassword", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                User updatedUser = userRepository.get("testuser");
                assertNotNull(updatedUser);
                assertEquals("newpassword", updatedUser.getPassword());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_UpdateWithNewUsernameAndPassword() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", "newuser", "newpass", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("newuser", outputData.getUsername());
                User updatedUser = userRepository.get("newuser");
                assertNotNull(updatedUser);
                assertEquals("newpass", updatedUser.getPassword());
                assertEquals("newuser", ((TestGameDashboard) dashboard).getCurrentUser());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_DisplayNameIsNull() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "Bio", "Pikachu", null,
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_DisplayNameIsEmpty() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "Bio", "Pikachu", "",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_DisplayNameIsWhitespace() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "Bio", "Pikachu", "   ",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_DisplayNameTooLong() {
        String longName = "a".repeat(33); // 33 characters, exceeds 32 limit
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "Bio", "Pikachu", longName,
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Display name must be <= 32 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_BioTooLong() {
        String longBio = "a".repeat(501); // 501 characters, exceeds 500 limit
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                longBio, "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Bio must be <= 500 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_NewUsernameTooLong() {
        String longUsername = "a".repeat(33); // 33 characters, exceeds 32 limit
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", longUsername, null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Username must be <= 32 characters long.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_NewUsernameAlreadyExists() {
        // Create another user with the username we want to change to
        User existingUser = userFactory.create("existinguser", "pass", 50, "Bio", "Squirtle", null, null);
        userRepository.updateUserProfile("existinguser", existingUser);

        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", "existinguser", null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Username already exists.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_NewPasswordIsEmpty() {
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, "", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Password cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void failureTest_NewPasswordIsWhitespace() {
        // Test when newPassword is not null but contains only whitespace
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, "   ", 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary failurePresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Password cannot be empty.", error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, failurePresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_NewUsernameSameAsCurrent() {
        // When new username is same as current, it should not trigger username update
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", "testuser", null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                // Should still exist under original username
                assertNotNull(userRepository.get("testuser"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_NewUsernameIsWhitespace() {
        // Whitespace username should be treated as null (no change)
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", "   ", null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_BioIsNull() {
        // Null bio should be allowed
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                null, "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                assertNull(outputData.getBio());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_BioAtMaxLength() {
        // Bio at exactly 500 characters should be allowed
        String maxBio = "a".repeat(500);
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                maxBio, "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                assertEquals(500, outputData.getBio().length());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_DisplayNameAtMaxLength() {
        // Display name at exactly 32 characters should be allowed
        String maxName = "a".repeat(32);
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", null, null, 100,
                "Bio", "Pikachu", maxName,
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals("testuser", outputData.getUsername());
                assertEquals(32, outputData.getName().length());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    @Test
    void successTest_NewUsernameAtMaxLength() {
        // New username at exactly 32 characters should be allowed
        String maxUsername = "a".repeat(32);
        UserProfileInputData inputData = new UserProfileInputData(
                "testuser", "password", maxUsername, null, 100,
                "Bio", "Pikachu", "Display Name",
                null, null);

        UserProfileOutputBoundary successPresenter = new UserProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(UserProfileOutputData outputData) {
                assertEquals(maxUsername, outputData.getUsername());
                assertNotNull(userRepository.get(maxUsername));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected: " + error);
            }
        };

        UserProfileInputBoundary interactor = new UserProfileInteractor(
                userRepository, successPresenter, userFactory, dashboard);
        interactor.execute(inputData);
    }

    // Test implementation classes
    private static class TestUserProfileDataAccessObject implements UserProfileUserDataAccessInterface {
        private final Map<String, User> users = new java.util.HashMap<>();

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public void updateUserProfile(String username, User user) {
            users.put(username, user);
        }

        @Override
        public void updateUsername(String oldUsername, String newUsername, User user) {
            users.remove(oldUsername);
            users.put(newUsername, user);
        }
    }

    private static class TestGameDashboard extends GameDashboard {
        private String currentUser;

        public TestGameDashboard() {
            super(new interface_adapter.ViewManagerModel(), new interface_adapter.themes.ThemeManager());
        }

        @Override
        public String getCurrentUser() {
            return currentUser;
        }

        @Override
        public void setUser(String username) {
            this.currentUser = username;
        }

        public void setCurrentUser(String username) {
            this.currentUser = username;
        }
    }
}
