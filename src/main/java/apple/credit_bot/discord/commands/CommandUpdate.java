package apple.credit_bot.discord.commands;

import apple.credit_bot.sheets.SheetsModify;
import apple.credit_bot.wynncraft.GetPlayerStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandUpdate implements DoCommand {
    private static final int NUM_OF_CHARS_PROGRESS = 20;

    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        Member author = event.getMember();
        if (author != null && author.hasPermission(Permission.MANAGE_SERVER)) {
            List<Member> members = event.getGuild().getMembers();
            List<String> nicknamesNotFound = new ArrayList<>();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription(getProgressBar(0.0));
            Message progressBar = event.getChannel().sendMessage(embed.build()).complete();
            int membersLength = members.size();
            for (int i = 0; i < membersLength; i++) {
                Member member = members.get(i);
                if (!member.getUser().isBot()) {
                    try {
                        SheetsModify.verifyProfile(member, GetPlayerStats.get(member.getEffectiveName()));
                    } catch (IOException | JSONException e){
                        nicknamesNotFound.add(member.getEffectiveName());
                        // if the nickname was not found
                    }
                    embed = new EmbedBuilder();
                    embed.setDescription(getProgressBar((i + 1.0) / membersLength));
                    progressBar.editMessage(embed.build()).queue();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            progressBar.delete().queue();
            if (nicknamesNotFound.isEmpty()) {
                event.getChannel().sendMessage("Everyone's name was found and their statistics updated accordingly.").queue();
            } else
                event.getChannel().sendMessage(String.join(", ", nicknamesNotFound) + " were not found").queue();
        } else
            event.getChannel().sendMessage("You need the permission 'Manage Server' to use this command").queue();
    }

    private String getProgressBar(double percentage) {
        StringBuilder result = new StringBuilder();
        int length = (int) (percentage * NUM_OF_CHARS_PROGRESS);
        for (int i = 0; i < length; i++)
            result.append('\u2588');
        length = NUM_OF_CHARS_PROGRESS - length;
        for (int i = 0; i < length; i++)
            result.append('\u2591');
        return result.toString();
    }
}

