package interface_adapter.akinator;

import use_case.akinator.AkinatorOutputBoundary;
import use_case.akinator.AkinatorOutputData;

public class AkinatorPresenter implements AkinatorOutputBoundary{

    private final AkinatorViewModel viewModel;

    public AkinatorPresenter(AkinatorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AkinatorOutputData outputData) {
        AkinatorState state = viewModel.getState();
        state.setPrompt(outputData.getPrompt());
        state.setStatus(outputData.getStatus());
        state.setAwaitingGuess(outputData.isAwaitingGuess());
        state.setGuessVisible(outputData.getStep() != AkinatorOutputData.Step.QUESTION
                && outputData.getGuessInfo() != null);
        state.setGuessInfo(outputData.getGuessInfo());
        state.setErrorMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        AkinatorState state = viewModel.getState();
        state.setErrorMessage(message);
        viewModel.setState(state);
        viewModel.firePropertyChange("error");
    }
}
