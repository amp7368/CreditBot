package apple.credit_bot.discord.commands.admin;

import apple.credit_bot.discord.DiscordBot;
import apple.credit_bot.discord.DiscordUtils;
import apple.credit_bot.discord.commands.DoCommand;
import apple.credit_bot.sheets.SheetsModify;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class CommandSub implements DoCommand {
    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        String[] eventContentSplit = event.getMessage().getContentStripped().toLowerCase().split(" ");

        // if this is not in the proper format, say as such
        if (eventContentSplit.length < 3) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.SUB_COMMAND + " [playerName] [creditsToAdd]").queue();
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
            pointsToAdd = -Integer.parseInt(eventContentSplit[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.SUB_COMMAND + " [playerName] [creditsToSubtract]\n'" + eventContentSplit[2] + "' is not a number.").queue();
            return;
        }

        try {
            int newPoints = SheetsModify.addPoints(discordMember, pointsToAdd);
            event.getChannel().sendMessage(discordMember.getEffectiveName() + " now has " + newPoints + " credit(s)").queue();
        } catch (IOException e) {
            event.getChannel().sendMessage("There was an error. Perhaps the name is not their ign.").queue();
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("There was an error reading the number of credits that "
                    + discordMember.getEffectiveName() + " has.").queue();
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("That player does not have more than or " + pointsToAdd + " credits").queue();
        }
    }
}
