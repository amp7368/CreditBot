package apple.credit_bot.discord.commands;

import apple.credit_bot.sheets.SheetsConstants;
import apple.credit_bot.sheets.SheetsRanges;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;

public class CommandCreditSum implements DoCommand {

    @Override
    public void dealWithCommand(MessageReceivedEvent event) {
        try {
            final Object creditsObject = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, SheetsRanges.statSheet + SheetsRanges.creditSummation).execute().getValues().get(0).get(0);
            int credits;
            if (creditsObject instanceof Integer) {
                credits = (int) creditsObject;
            } else {
                credits = Integer.parseInt(creditsObject.toString());
                // this might throw a number format exception to tell the method above that there is not a number in the highest color section of the player's
            }
            event.getChannel().sendMessage("Everyone together has a total of " + credits + " credits").queue();
        } catch (IOException e) {
            event.getChannel().sendMessage("There was an issue processing that request.").queue();
            e.printStackTrace();
        }
    }
}
