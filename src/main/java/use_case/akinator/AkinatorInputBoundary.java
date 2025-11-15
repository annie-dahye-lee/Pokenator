package use_case.akinator;

public interface AkinatorInputBoundary {
    void start();
    void reset();
    void answerYes();
    void answerNo();
    void answerUnknown();
    void confirmGuess(boolean correct);
}
