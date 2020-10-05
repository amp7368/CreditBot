package apple.credit_bot.sheets;

import apple.credit_bot.discord.data.Profile;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SheetsQuery {
    public static Profile getProfile(Member discordMember) throws IOException {
        int row = SheetsModify.verifyProfile(discordMember, discordMember.getEffectiveName());
        final String playerRowRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.playerRow1, 0, row) +
                ":" + SheetsUtils.addA1Notation(SheetsRanges.playerRow2, 0, row);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, playerRowRange).execute();
        List<Object> playerRowValues = playerRowValueRange.getValues().get(0);
        return new Profile(playerRowValues); //todo send error messages
    }

    public static List<Profile> getLeaderBoard() throws IOException {
        ValueRange playersRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                SheetsRanges.dataSheet + SheetsRanges.playerRow1 + ":" + SheetsRanges.playerColEnd).execute();
        List<Profile> leaderboard = new ArrayList<>();
        for (List<Object> playerRowValues : playersRowValueRange.getValues()) {
            leaderboard.add(new Profile(playerRowValues));
        }
        return leaderboard;
    }
}
