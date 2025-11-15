package interface_adapter.akinator;

import use_case.akinator.AkinatorInputBoundary;

public class AkinatorController {

    private final AkinatorInputBoundary interactor;

    public AkinatorController(AkinatorInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void start() {
        interactor.start();
    }

    public void reset() {
        interactor.reset();
    }

    public void answerYes() {
        interactor.answerYes();
    }

    public void answerNo() {
        interactor.answerNo();
    }

    public void answerUnknown() {
        interactor.answerUnknown();
    }

    public void confirmGuess(boolean correct) {
        interactor.confirmGuess(correct);
    }
}
