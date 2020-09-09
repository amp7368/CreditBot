package apple.credit_bot.discord.data;

import org.json.JSONObject;

public class WynncraftClass {
    public JSONObject fishing;
    public JSONObject scribing;
    public JSONObject combat;
    public JSONObject mining;
    public JSONObject weaponsmithing;
    public JSONObject tailoring;
    public JSONObject woodcutting;
    public JSONObject woodworking;
    public JSONObject farming;
    public JSONObject armouring;
    public JSONObject alchemism;
    public JSONObject cooking;
    public JSONObject jeweling;
    public String classType;

    public WynncraftClass(JSONObject element) {
        JSONObject professioons = (JSONObject) element.get("professions");
        classType =  element.getString("name");
        this.fishing = (JSONObject) professioons.get("fishing");
        this.scribing = (JSONObject) professioons.get("scribing");
        this.combat = (JSONObject) professioons.get("combat");
        this.mining = (JSONObject) professioons.get("mining");
        this.weaponsmithing = (JSONObject) professioons.get("weaponsmithing");
        this.tailoring = (JSONObject) professioons.get("tailoring");
        this.woodcutting = (JSONObject) professioons.get("woodcutting");
        this.woodworking = (JSONObject) professioons.get("woodworking");
        this.farming = (JSONObject) professioons.get("farming");
        this.armouring = (JSONObject) professioons.get("armouring");
        this.alchemism = (JSONObject) professioons.get("alchemism");
        this.cooking = (JSONObject) professioons.get("cooking");
        this.jeweling = (JSONObject) professioons.get("jeweling");
    }
}