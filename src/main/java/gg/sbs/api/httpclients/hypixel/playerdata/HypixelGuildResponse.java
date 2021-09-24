package gg.sbs.api.httpclients.hypixel.playerdata;

public class HypixelGuildResponse {
    private boolean success;
    private HypixelGuild guild;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HypixelGuild getGuild() {
        return guild;
    }

    public void setGuild(HypixelGuild guild) {
        this.guild = guild;
    }
}
