package apple.credit_bot.discord.commands;

import apple.credit_bot.CreditMain;
import apple.credit_bot.discord.DiscordBot;
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
        Member discordMember;
        final String nameToGet = eventContentSplit[1];
        final String nameToGetLower = nameToGet.toLowerCase();
        List<Member> membersInGuild = event.getGuild().getMembers();
        List<Member> membersWithName = new ArrayList<>();
        for (Member memberInGuild : membersInGuild) {
            if (memberInGuild.getEffectiveName().toLowerCase().contains(nameToGetLower)) {
                membersWithName.add(memberInGuild);
            }
        }
        final int membersWithNameLength = membersWithName.size();
        if (membersWithNameLength == 0) {
            // quit with an error message
            event.getChannel().sendMessage(String.format("Sorry, but nobody's name contains '%s'.", nameToGet)).queue();
            return;
        } else if (membersWithNameLength == 1) {
            // we found the person
            discordMember = membersWithName.get(0);
        } else {
            // ask the user to narrow their search
            event.getChannel().sendMessage(String.format("There are %d people that have '%s' in their name. Please refine your search.", membersWithNameLength, nameToGet)).queue();
            return;
        }

        // get the points that we are entering
        int pointsToAdd;
        try {
            pointsToAdd = Integer.parseInt(eventContentSplit[2]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.ADD_COMMAND + " [playerName] [pointsToAdd]\n'" + eventContentSplit[2] + "' is not a number.").queue();
            return;
        }

        try {
            SheetsModify.addPoints(discordMember, pointsToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
