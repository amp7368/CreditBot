package apple.credit_bot.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class DiscordUtils {
    public static Member getMemberFromName(String nameToGet, MessageReceivedEvent event) {
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
            return null;
        } else if (membersWithNameLength == 1) {
            // we found the person
            return membersWithName.get(0);
        } else {
            // ask the user to narrow their search
            event.getChannel().sendMessage(String.format("There are %d people that have '%s' in their name. Please refine your search.", membersWithNameLength, nameToGet)).queue();
            return null;
        }
    }
}
