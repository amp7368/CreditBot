package apple.credit_bot.discord.commands;

import apple.credit_bot.CreditMain;
import apple.credit_bot.discord.DiscordBot;
import apple.credit_bot.discord.DiscordUtils;
import apple.credit_bot.sheets.SheetsModify;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandAdd implements DoCommand {
    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        String[] eventContentSplit = event.getMessage().getContentStripped().toLowerCase().split(" ");

        // if this is not in the proper format, say as such
        if (eventContentSplit.length < 3) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.ADD_COMMAND + " [playerName] [pointsToAdd]").queue();
            return;
        }

        // look for the player that the user entered
        final String nameToGet = eventContentSplit[1];
        Member discordMember = DiscordUtils.getMemberFromName(nameToGet, event);
        if (discordMember == null)
            return;

        // get the points that we are entering
        int pointsToAdd;
        try {
            pointsToAdd = Integer.parseInt(eventContentSplit[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.ADD_COMMAND + " [playerName] [pointsToAdd]\n'" + eventContentSplit[2] + "' is not a number.").queue();
            return;
        }

        try {
            int newPoints = SheetsModify.addPoints(discordMember, pointsToAdd);
            event.getChannel().sendMessage(discordMember.getEffectiveName() + " now has " + newPoints + " point(s)").queue();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("There was an error reading the number of credits that "
                    + discordMember.getEffectiveName() + " has.").queue();
        }
    }
}
