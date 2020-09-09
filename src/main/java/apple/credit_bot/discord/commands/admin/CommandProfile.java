package apple.credit_bot.discord.commands.admin;

import apple.credit_bot.discord.DiscordBot;
import apple.credit_bot.discord.DiscordUtils;
import apple.credit_bot.discord.commands.DoCommand;
import apple.credit_bot.discord.data.Profile;
import apple.credit_bot.sheets.SheetsQuery;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class CommandProfile implements DoCommand {

    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        String[] eventContentSplit = event.getMessage().getContentStripped().toLowerCase().split(" ");

        // if this is not in the proper format, say as such
        if (eventContentSplit.length < 2) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.PROFILE_COMMAND + " [playerName]").queue();
            return;
        }

        // look for the player that the user entered
        final String nameToGet = eventContentSplit[1];
        Member discordMember = DiscordUtils.getMemberFromName(nameToGet, event);
        if (discordMember == null)
            return;
        Profile profile;
        try {
            profile = SheetsQuery.getProfile(discordMember);
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
