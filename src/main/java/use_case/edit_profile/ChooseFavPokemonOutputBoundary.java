package use_case.edit_profile;

public interface ChooseFavPokemonOutputBoundary {
    /**
     * Prepares the success view for the Choose Favourite Pokemon Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ChooseFavPokemonOutputData outputData);

    /**
     * Prepares the failure view for the Choose Favourite Pokemon Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
