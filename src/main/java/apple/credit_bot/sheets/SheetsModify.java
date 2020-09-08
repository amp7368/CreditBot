package apple.credit_bot.sheets;

import apple.credit_bot.discord.DiscordBot;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsModify {
    public static void addPoints(Member member, int pointsToAdd) throws IOException, NumberFormatException {
        String myDiscordId = member.getId();
        int rowToModify = findRowFromDiscord(myDiscordId);
        if (rowToModify == -1) {
            rowToModify = addPlayer(member);
        }
        String creditsRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.credits, 0, rowToModify);
        ValueRange creditsValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                creditsRange).execute();

        try {
            int pointsThere;
            Object pointsThereObject = creditsValueRange.getValues().get(0).get(0);
            if (pointsThereObject instanceof Integer) {
                pointsThere = (int) pointsThereObject;
            } else {
                pointsThere = Integer.parseInt(pointsThereObject.toString());
                // this might throw a number format exception to tell the method above that there is not a number in the credits section of the player's
            }
            creditsValueRange.setValues(Collections.singletonList(Collections.singletonList(pointsThere + pointsToAdd)));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            creditsValueRange.setValues(Collections.singletonList(Collections.singletonList(pointsToAdd)));
        }
        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, creditsRange, creditsValueRange).setValueInputOption("USER_ENTERED").execute();

    }

    /**
     * adds the player to the spreadsheet
     *
     * @param member the discord member of the player
     * @return what row the player was added to
     */
    private static int addPlayer(Member member) throws IOException {
        List<String> roles = new ArrayList<>();
        for (Role role : member.getRoles()) {
            roles.add(role.getColorRaw() + "." + role.getName());
        }
        ValueRange idValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                SheetsRanges.dataSheet + SheetsRanges.discordIds).setMajorDimension("COLUMNS").execute();
        int rowToPutPlayer;
        if (idValueRange.getValues() == null) {
            rowToPutPlayer = 0;
        } else {
            rowToPutPlayer = idValueRange.getValues().size();
        }

        final String playerRowRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.playerRow, 0, rowToPutPlayer);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, playerRowRange).execute();

        List<Object> playerValues = new ArrayList<>();
        playerValues.add(member.getEffectiveName());
        playerValues.add(member.getId());
        playerValues.add(0);
        playerValues.add(String.join(",", roles));

        playerRowValueRange.setValues(Collections.singletonList(playerValues));

        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, playerRowRange, playerRowValueRange).setValueInputOption("USER_ENTERED").execute();
        return rowToPutPlayer;
    }

    /**
     * finds the row corresponding to the id given
     *
     * @param myDiscordId the unique discord id of the member we're finding
     * @return if discord id is found: the row corresponding to the given id as if row 2 (where data starts) was row 0
     * otherwise if discord id is not found: -1
     * @throws IOException from google sheets
     */
    private static int findRowFromDiscord(String myDiscordId) throws IOException {
        ValueRange idValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, SheetsRanges.dataSheet + SheetsRanges.discordIds).setMajorDimension("COLUMNS").execute();
        if (idValueRange.getValues() == null) {
            // there is no one in the spreadsheet
            return -1;
        }
        List<Object> allDiscordIds = idValueRange.getValues().get(0);
        final int size = allDiscordIds.size();
        for (int i = 0; i < size; i++) {
            Object discordId = allDiscordIds.get(i);
            if (discordId.toString().equals(myDiscordId)) {
                // we found row
                return i;
            }
        }
        return -1;
    }
}
