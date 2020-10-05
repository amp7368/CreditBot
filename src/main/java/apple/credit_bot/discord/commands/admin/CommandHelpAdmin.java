package apple.credit_bot.discord.commands.admin;

import apple.credit_bot.discord.commands.DoCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static apple.credit_bot.discord.DiscordBot.*;

public class CommandHelpAdmin implements DoCommand {
    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        event.getChannel().sendMessage("**" + PREFIX + PROFILE_COMMAND + " [player name]** - gives you that player's current stats\n" +
                "**" + PREFIX + ADD_COMMAND + " [player] [credits]** - adds that number of credits to that player\n" +
                "**" + PREFIX + SUB_COMMAND + " [player] [credits]** - subtracts that number of credits from that player\n" +
                "**" + PREFIX + LEADERBOARD_COMMAND + "** - gives a list of all the players with credits\n" +
                "**" + PREFIX + UPDATE_COMMAND + "** - updates the entire spreadsheet once (Shouldn't be needed much because " + PREFIX + PROFILE_COMMAND + " updates that player's stats)\n" +
                "**" + PREFIX + CREDIT_SUM_COMMAND + "** - gives the number of credits the community currently has\n"+
                "**" + PREFIX + REDEEM_COMMAND + " [pts] [a message]** - redeems that many points and dms Founder\n").queue();
    }
}
