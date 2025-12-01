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
        state.setStep(outputData.getStep());
        state.setPrompt(outputData.getPrompt());
        state.setStatus(outputData.getStatus());
        state.setAwaitingGuess(outputData.isAwaitingGuess());
        state.setAwaitingReveal(outputData.isAwaitingReveal());
        state.setRoundActive(outputData.isRoundActive());
        state.setQuestionsAsked(outputData.getQuestionsAsked());
        state.setQuestionLimit(outputData.getQuestionLimit());
        state.setRevealPromptId(outputData.getRevealPromptId());
        boolean showGuess = outputData.getStep() == AkinatorOutputData.Step.GUESS
                || (outputData.getStep() == AkinatorOutputData.Step.FINISHED
                && outputData.getGuessInfo() != null);
        state.setGuessVisible(showGuess);
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
