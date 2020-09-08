package apple.credit_bot.discord.data;

import java.util.List;

public class Profile {
    public String name;
    public String credits;
    public int highestColor;
    public String roles;
    public String guild;
    public String guildRank;

    public Profile(List<Object> playerRowValues) throws NumberFormatException {
        name = playerRowValues.get(0).toString();
        credits = playerRowValues.get(2).toString();
        if (playerRowValues.get(3) instanceof Integer) {
            highestColor = (int) playerRowValues.get(3);
        } else {
            highestColor = Integer.parseInt(playerRowValues.get(3).toString());
            // this might throw a number format exception to tell the method above that there is not a number in the highest color section of the player's
        }
        roles = playerRowValues.get(4).toString();
        guild = playerRowValues.get(5).toString();
        guildRank = playerRowValues.get(6).toString();
    }
}
