package gg.sbs.api.httpclients.hypixel.playerdata;

public class HypixelPlayerResponse {
    private boolean success;
    private HypixelPlayer player;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HypixelPlayer getPlayer() {
        return player;
    }

    public void setPlayer(HypixelPlayer player) {
        this.player = player;
    }
}
