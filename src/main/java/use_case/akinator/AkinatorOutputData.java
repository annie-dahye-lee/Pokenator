package use_case.akinator;
import data_access.PokeApiGateway;

public class AkinatorOutputData {
    public enum Step {QUESTION, GUESS, FINISHED}

    private final Step step;
    private final String prompt;
    private final String status;
    private final boolean awaitingGuess;
    private final PokeApiGateway.PokemonApiInfo guessInfo;

    public AkinatorOutputData(Step step,
                              String prompt,
                              String status,
                              boolean awaitingGuess,
                              PokeApiGateway.PokemonApiInfo guessInfo) {
        this.step = step;
        this.prompt = prompt;
        this.status = status;
        this.awaitingGuess = awaitingGuess;
        this.guessInfo = guessInfo;
    }

    public Step getStep() { return step; }
    public String getPrompt() { return prompt; }
    public String getStatus() { return status; }
    public boolean isAwaitingGuess() { return awaitingGuess; }
    public PokeApiGateway.PokemonApiInfo getGuessInfo() { return guessInfo; }
}
