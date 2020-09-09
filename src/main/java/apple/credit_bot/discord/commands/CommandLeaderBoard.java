package apple.credit_bot.discord.commands;

import apple.credit_bot.discord.data.Profile;
import apple.credit_bot.sheets.SheetsQuery;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class CommandLeaderBoard implements DoCommand {

    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        List<Profile> profiles;
        try {
            profiles = SheetsQuery.getLeaderBoard();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        profiles.sort((o1, o2) -> {
            int compare = o2.credits - o1.credits;
            if (compare == 0)
                return o1.name.compareTo(o2.name);
            return compare;
        });
        int i = 1;
        StringBuilder message = new StringBuilder("```glsl\n");
        boolean first = true;
        for (Profile profile : profiles) {
            if (profile.credits == 0) // we're done because everyone else has no credits
                break;
            if (i % 5 == 1) {
                message.append("-".repeat(49)).append("\n");
            }
            if (i % 25 == 1) {
                if (!first) {
                    message.append("```");
                    event.getChannel().sendMessage(message).queue();
                    message = new StringBuilder("```glsl\n");
                    message.append("-".repeat(49)).append("\n");
                } else {
                    first = false;
                }
                message.append(String.format("|%4s| %-30s | %7s |\n", "", "Name", "Credits")).append("-".repeat(49)).append("\n");
            }
            message.append(String.format("|%4d| %-30s | %7d |\n", i++, profile.name, profile.credits));


        }
        message.append("-".repeat(49)).append("\n```");
        event.getChannel().sendMessage(message).queue();
    }
}
