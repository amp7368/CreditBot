package apple.credit_bot.sheets;

import apple.credit_bot.discord.data.Profile;

import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsModify {
    public static int addPoints(Member member, int pointsToAdd) throws IOException, NumberFormatException {
        String myDiscordId = member.getId();
        int rowToModify = findRowFromDiscord(myDiscordId);
        if (rowToModify == -1) {
            rowToModify = addPlayer(member);
        }
        String creditsRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.credits, 0, rowToModify);
        ValueRange creditsValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                creditsRange).execute();
        int newPoints;
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
            newPoints = pointsThere + pointsToAdd;
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            creditsValueRange.setValues(Collections.singletonList(Collections.singletonList(pointsToAdd)));
            newPoints = pointsToAdd;
        }
        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, creditsRange, creditsValueRange).setValueInputOption("USER_ENTERED").execute();
        return newPoints;
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
            roles.add(role.getName());
        }
        ValueRange idValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                SheetsRanges.dataSheet + SheetsRanges.discordIds).setMajorDimension("COLUMNS").execute();
        int rowToPutPlayer;
        if (idValueRange.getValues() == null) {
            rowToPutPlayer = 0;
        } else {
            rowToPutPlayer = idValueRange.getValues().size();
        }

        final String playerRowRange = SheetsRanges.dataSheet +
                SheetsUtils.addA1Notation(SheetsRanges.playerRow1, 0, rowToPutPlayer) +
                ":" + SheetsUtils.addA1Notation(SheetsRanges.playerRow2, 0, rowToPutPlayer);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, playerRowRange).execute();
        List<Object> playerValues = new ArrayList<>();
        playerValues.add(member.getEffectiveName());
        playerValues.add(member.getId());
        playerValues.add(0);
        playerValues.add(member.getColorRaw());
        playerValues.add(String.join(", ", roles));

        playerRowValueRange.setValues(Collections.singletonList(playerValues));

        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, playerRowValueRange.getRange(), playerRowValueRange).setValueInputOption("USER_ENTERED").execute();
        return rowToPutPlayer;
    }

    private static void verifyProfile(Member member, int rowToPutPlayer) throws IOException {
        List<String> roles = new ArrayList<>();
        for (Role role : member.getRoles()) {
            roles.add(role.getName());
        }

        final String playerRowRange = SheetsRanges.dataSheet +
                SheetsUtils.addA1Notation(SheetsRanges.playerRow1, 0, rowToPutPlayer) +
                ":" + SheetsUtils.addA1Notation(SheetsRanges.playerRow2, 0, rowToPutPlayer);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, playerRowRange).execute();
        List<Object> playerValues = new ArrayList<>();
        playerValues.add(member.getEffectiveName());
        playerValues.add(member.getId());
        try {
            playerValues.add(playerRowValueRange.getValues().get(0).get(2));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            playerValues.add(0);
        }
        playerValues.add(member.getColorRaw());
        playerValues.add(String.join(", ", roles));

        playerRowValueRange.setValues(Collections.singletonList(playerValues));

        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, playerRowValueRange.getRange(), playerRowValueRange).setValueInputOption("USER_ENTERED").execute();
    }

    public static Profile getProfile(Member discordMember) throws IOException {
        int row = findRowFromDiscord(discordMember.getId());
        if (row == -1) {
            row = addPlayer(discordMember);
        } else {
            verifyProfile(discordMember, row);
        }
        final String playerRowRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.playerRow1, 0, row) +
                ":" + SheetsUtils.addA1Notation(SheetsRanges.playerRow2, 0, row);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, playerRowRange).execute();
        List<Object> playerRowValues = playerRowValueRange.getValues().get(0);
        System.out.println(playerRowValueRange.getValues().size());
        return new Profile(playerRowValues); //todo send error messages
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
