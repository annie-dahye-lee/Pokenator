package use_case.leaderboard;

// Input data for the leaderboard use case.
public class LeaderboardInputData {

    private final boolean fired; // If the event for opening the leaderboard page was activated (e.g. button click).

    public LeaderboardInputData(boolean fired) {
        this.fired = fired;
    }

    boolean getFired() { return fired; }

}
