package apple.credit_bot.discord.commands.normal;

import apple.credit_bot.discord.commands.DoCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static apple.credit_bot.discord.DiscordBot.*;

public class CommandHelp implements DoCommand {
    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        event.getChannel().sendMessage("**" + PREFIX + PROFILE_COMMAND + "** - gives you your current stats").queue();
    }
}
