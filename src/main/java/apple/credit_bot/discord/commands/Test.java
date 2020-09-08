package apple.credit_bot.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Test implements DoCommand{

    @Override
    public void dealWithCommand(MessageReceivedEvent eventObject) {
        eventObject.getChannel().sendMessage("o/").queue();
    }
}
