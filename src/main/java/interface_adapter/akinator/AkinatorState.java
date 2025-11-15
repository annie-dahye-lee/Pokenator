package interface_adapter.akinator;

import data_access.PokeApiGateway;

import java.util.List;

public class AkinatorState {
    private String prompt = "Press Start to begin.";
    private String status = "";
    private boolean guessVisible;
    private boolean awaitingGuess;
    private PokeApiGateway.PokemonApiInfo guessInfo;
    private String errorMessage;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isGuessVisible() {
        return guessVisible;
    }

    public void setGuessVisible(boolean guessVisible) {
        this.guessVisible = guessVisible;
    }

    public boolean isAwaitingGuess() {
        return awaitingGuess;
    }

    public void setAwaitingGuess(boolean awaitingGuess) {
        this.awaitingGuess = awaitingGuess;
    }

    public PokeApiGateway.PokemonApiInfo getGuessInfo() {
        return guessInfo;
    }

    public void setGuessInfo(PokeApiGateway.PokemonApiInfo guessInfo) {
        this.guessInfo = guessInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
