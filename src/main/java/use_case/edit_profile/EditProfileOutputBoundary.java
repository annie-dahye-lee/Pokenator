package use_case.edit_profile;

public interface EditProfileOutputBoundary {
    /**
     * Prepares the success view for the Edit Profile Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(EditProfileOutputData outputData);

    /**
     * Prepares the failure view for the Edit Profile Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
