package apple.credit_bot.discord.commands;

import apple.credit_bot.discord.data.Profile;
import apple.credit_bot.sheets.SheetsQuery;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class CommandMyProfile implements DoCommand {
    @Override
    public void dealWithCommand(MessageReceivedEvent event) {

        // look for the player that the user entered
        final Member member = event.getMember();
        if (member == null) return;
        Profile profile;
        try {
            profile = SheetsQuery.getProfile(member);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(profile.name + " has " + profile.credits + " credit(s)");
        embedBuilder.setColor(profile.highestColor);
        embedBuilder.setDescription(
                "**Roles:** " + profile.roles + "\n" +
                        "**Guild:** " + profile.guildRank + " in " + profile.guild + "\n" +
                        "**Days Since Active:** " + profile.daysSinceLastActive + "\n" +
                        "**Classes:**\n" +
                        profile.class1 + "\n" +
                        profile.class2 + "\n" +
                        profile.class3 + "\n" +
                        profile.class4 + "\n" +
                        profile.class5 + "\n" +
                        profile.class6 + "\n"
        );

        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }
}
