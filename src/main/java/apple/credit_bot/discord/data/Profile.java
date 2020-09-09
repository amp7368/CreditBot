package apple.credit_bot.discord.data;

import org.json.JSONObject;

import java.util.List;

public class Profile {
    public String name;
    public int credits;
    public int highestColor;
    public String roles;
    public String guild;
    public String guildRank;
    public String daysSinceLastActive;
    public String class1;
    public String class2;
    public String class3;
    public String class4;
    public String class5;
    public String class6;
    public String fishing;
    public String scribing;
    public String combat;
    public String mining;
    public String weaponsmithing;
    public String tailoring;
    public String woodcutting;
    public String woodworking;
    public String farming;
    public String armouring;
    public String alchemism;
    public String cooking;
    public String jeweling;

    public Profile(List<Object> playerRowValues) throws NumberFormatException {
        name = playerRowValues.get(0).toString();
        final Object creditsObject = playerRowValues.get(2);
        if (creditsObject instanceof Integer) {
            credits = (int) creditsObject;
        } else {
            credits = Integer.parseInt(creditsObject.toString());
            // this might throw a number format exception to tell the method above that there is not a number in the highest color section of the player's
        }
        final Object highestColorObject = playerRowValues.get(3);
        if (highestColorObject instanceof Integer) {
            highestColor = (int) highestColorObject;
        } else {
            highestColor = Integer.parseInt(highestColorObject.toString());
            // this might throw a number format exception to tell the method above that there is not a number in the highest color section of the player's
        }
        roles = playerRowValues.get(4).toString();
        guild = playerRowValues.get(5).toString();
        guildRank = playerRowValues.get(6).toString();
        daysSinceLastActive = playerRowValues.get(7).toString();

        class1 = playerRowValues.get(8).toString();
        class2 = playerRowValues.get(9).toString();
        class3 = playerRowValues.get(10).toString();
        class4 = playerRowValues.get(11).toString();
        class5 = playerRowValues.get(12).toString();
        class6 = playerRowValues.get(13).toString();
        fishing = playerRowValues.get(14).toString();
        scribing = playerRowValues.get(15).toString();
        mining = playerRowValues.get(16).toString();
        weaponsmithing = playerRowValues.get(17).toString();
        tailoring = playerRowValues.get(18).toString();
        woodcutting = playerRowValues.get(19).toString();
        woodworking = playerRowValues.get(20).toString();
        farming = playerRowValues.get(21).toString();
        armouring = playerRowValues.get(22).toString();
        alchemism = playerRowValues.get(23).toString();
        cooking = playerRowValues.get(24).toString();
        jeweling = playerRowValues.get(25).toString();
    }
}
