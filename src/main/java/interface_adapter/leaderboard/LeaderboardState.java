package interface_adapter.leaderboard;

public class LeaderboardState {

    private int page;

    public LeaderboardState() {
        this.page = 1;
    }

    public LeaderboardState(int page) {
        this.page = page;
    }

    public int getPage() { return page; }

    public void setPage(int page) { this.page = page; }

}
