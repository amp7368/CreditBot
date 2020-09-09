package apple.credit_bot.discord.data;

import java.util.List;

public class Profile {
    public String name;
    public int credits;
    public int highestColor;
    public String roles;
    public String guild;
    public String guildRank;

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
    }
}
