package use_case.akinator;

public interface AkinatorOutputBoundary {
    void present(AkinatorOutputData outputData);
    void presentError(String message);
}
