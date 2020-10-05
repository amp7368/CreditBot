package apple.credit_bot.discord.commands.normal;

import apple.credit_bot.discord.DiscordBot;
import apple.credit_bot.discord.commands.DoCommand;
import apple.credit_bot.sheets.SheetsModify;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static apple.credit_bot.discord.DiscordBot.PREFIX;
import static apple.credit_bot.discord.DiscordBot.REDEEM_COMMAND;

public class CommandRedeem implements DoCommand {

    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        String[] eventContentSplit = event.getMessage().getContentStripped().split(" ");
        if (eventContentSplit.length < 2) {
            event.getChannel().sendMessage("Usage: **" + PREFIX + REDEEM_COMMAND + " [pts] [a message]** - redeems that many points and dms Founder").queue();
            return;
        }
        // get the points that we are entering
        int points;
        try {
            points = -Integer.parseInt(eventContentSplit[1]);
            if (points > 0) {
                event.getChannel().sendMessage("You can't redeem negative credits").queue();
                return;
            } else if (points == 0) {
                event.getChannel().sendMessage("You've redeemed 0 credits. Nothing happens").queue();
                return;
            }
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Usage: " + DiscordBot.PREFIX + DiscordBot.REDEEM_COMMAND + " [pointsToRedeem]\n'" + eventContentSplit[1] + "' is not a number.").queue();
            return;
        }
        try {
            final Member member = event.getMember();
            if (member == null) return;
            SheetsModify.addPoints(member, points);
            if (eventContentSplit.length == 2) {
                event.getGuild().getOwner().getUser().openPrivateChannel().complete().sendMessage(
                        member.getEffectiveName() + " has redeemed " + -points + " credit(s)." +
                                "\n They said nothing in their message").queue();
            } else {
                event.getGuild().getOwner().getUser().openPrivateChannel().complete().sendMessage(
                        member.getEffectiveName() + " has redeemed " + -points + " credit(s).\n This was their message:\""
                                + String.join(" ", Arrays.stream(eventContentSplit).collect(Collectors.toList()).subList(2, eventContentSplit.length))
                                + "\"").queue();
            }
            event.getChannel().sendMessage("You have redeemed " + -points + " credit(s). Founder has been notified").queue();
        } catch (IOException e) {
            event.getChannel().sendMessage("There was some sort of internal IOException getting your profile").queue();
        } catch (IllegalArgumentException e) {
            event.getChannel().sendMessage("You don't have enough credits to redeem " + -points + " credit(s)").queue();
        }

    }
}
