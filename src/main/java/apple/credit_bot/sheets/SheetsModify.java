package apple.credit_bot.sheets;

import apple.credit_bot.discord.data.WynncraftClass;
import apple.credit_bot.wynncraft.GetPlayerStats;
import com.google.api.services.sheets.v4.model.ValueRange;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SheetsModify {
    public static int addPoints(Member member, int pointsToAdd) throws IOException, NumberFormatException {
        int rowToModify = verifyProfile(member, GetPlayerStats.get(member.getEffectiveName()));
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
     * finds the row corresponding to the id given
     *
     * @param myDiscordId the unique discord id of the member we're finding
     * @return if discord id is found: the row corresponding to the given id as if row 2 (where data starts) was row 0
     * otherwise if discord id is not found: -1
     * @throws IOException from google sheets
     */
    static int findRowFromDiscord(String myDiscordId) throws IOException {
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

    public static int verifyProfile(Member member, JSONObject playerObject) throws IOException {
        List<String> roles = new ArrayList<>();
        for (Role role : member.getRoles()) {
            roles.add(role.getName());
        }
        JSONObject playerData = (JSONObject) ((JSONArray) playerObject.get("data")).get(0);

        int rowToWrite = findRowFromDiscord(member.getId());
        boolean isListed = true;
        if (rowToWrite == -1) {
            isListed = false;
            ValueRange idValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId,
                    SheetsRanges.dataSheet + SheetsRanges.discordIds).setMajorDimension("COLUMNS").execute();
            if (idValueRange.getValues() == null) {
                rowToWrite = 0;
            } else {
                rowToWrite = idValueRange.getValues().size();
            }
        }

        String outputRange = SheetsRanges.dataSheet + SheetsUtils.addA1Notation(SheetsRanges.playerRow1, 0, rowToWrite)
                + ":" + SheetsUtils.addA1Notation(SheetsRanges.playerRow2, 0, rowToWrite);
        ValueRange playerRowValueRange = SheetsConstants.sheets.get(SheetsConstants.spreadsheetId, outputRange).execute();
        List<Object> playerValues = new ArrayList<>();
        playerValues.add(member.getEffectiveName());
        playerValues.add(member.getId());
        if (!isListed) {
            playerValues.add(0);
        } else {
            try {
                playerValues.add(playerRowValueRange.getValues().get(0).get(2));
            } catch (NullPointerException | IndexOutOfBoundsException e) {e.printStackTrace();
                playerValues.add(0);
            }
        }
        playerValues.add(member.getColorRaw());
        playerValues.add(String.join(", ", roles));

        JSONObject guildData = (JSONObject) playerData.get("guild");
        String guildName;
        String guildRank;
        if (guildData.isNull("name")) {
            guildName = "No guild";
            guildRank = "No rank";
        } else {
            guildName = guildData.getString("name");

            guildRank = guildData.getString("rank");
        }
        // guild
        playerValues.add(guildName);

        // guild rank
        playerValues.add(guildRank);

        JSONArray classes = (JSONArray) playerData.get("classes");
        List<WynncraftClass> myClasses = new ArrayList<>();
        for (Object element : classes) {
            myClasses.add(new WynncraftClass((JSONObject) element));
        }
        myClasses.sort((o1, o2) -> o2.combat.getInt("level") - o1.combat.getInt("level"));
        int myClassesLength = myClasses.size();
        // class type and level
        for (int i = 0; i <= 6; i++) {
            if (i >= myClassesLength) {
                playerValues.add("None");
            } else {
                playerValues.add(myClasses.get(i).combat.getInt("level") + " " + myClasses.get(i).classType);
            }
        }

        if (myClassesLength != 0) {
            WynncraftClass profClass = myClasses.get(0);
            for (int i = 1; i < myClassesLength; i++) {
                WynncraftClass other = myClasses.get(i);
                if (profClass.fishing.getInt("level") < other.fishing.getInt("level")) {
                    profClass.fishing = other.fishing;
                }
                if (profClass.scribing.getInt("level") < other.scribing.getInt("level")) {
                    profClass.scribing = other.scribing;
                }
                if (profClass.mining.getInt("level") < other.mining.getInt("level")) {
                    profClass.mining = other.mining;
                }
                if (profClass.weaponsmithing.getInt("level") < other.weaponsmithing.getInt("level")) {
                    profClass.weaponsmithing = other.weaponsmithing;
                }
                if (profClass.tailoring.getInt("level") < other.tailoring.getInt("level")) {
                    profClass.tailoring = other.tailoring;
                }
                if (profClass.woodcutting.getInt("level") < other.woodcutting.getInt("level")) {
                    profClass.woodcutting = other.woodcutting;
                }
                if (profClass.woodworking.getInt("level") < other.woodworking.getInt("level")) {
                    profClass.woodworking = other.woodworking;
                }
                if (profClass.farming.getInt("level") < other.farming.getInt("level")) {
                    profClass.farming = other.farming;
                }
                if (profClass.armouring.getInt("level") < other.armouring.getInt("level")) {
                    profClass.armouring = other.armouring;
                }
                if (profClass.alchemism.getInt("level") < other.alchemism.getInt("level")) {
                    profClass.alchemism = other.alchemism;
                }
                if (profClass.cooking.getInt("level") < other.cooking.getInt("level")) {
                    profClass.cooking = other.cooking;
                }
                if (profClass.jeweling.getInt("level") < other.jeweling.getInt("level")) {
                    profClass.jeweling = other.jeweling;
                }
            }
            playerValues.add(String.valueOf(profClass.mining.getInt("level")));
            playerValues.add(String.valueOf(profClass.woodcutting.getInt("level")));
            playerValues.add(String.valueOf(profClass.farming.getInt("level")));
            playerValues.add(String.valueOf(profClass.fishing.getInt("level")));
            playerValues.add(String.valueOf(profClass.armouring.getInt("level")));
            playerValues.add(String.valueOf(profClass.tailoring.getInt("level")));
            playerValues.add(String.valueOf(profClass.weaponsmithing.getInt("level")));
            playerValues.add(String.valueOf(profClass.woodworking.getInt("level")));
            playerValues.add(String.valueOf(profClass.jeweling.getInt("level")));
            playerValues.add(String.valueOf(profClass.scribing.getInt("level")));
            playerValues.add(String.valueOf(profClass.cooking.getInt("level")));
            playerValues.add(String.valueOf(profClass.alchemism.getInt("level")));

        }
        playerRowValueRange.setValues(Collections.singletonList(playerValues));

        SheetsConstants.sheets.update(SheetsConstants.spreadsheetId, playerRowValueRange.getRange(), playerRowValueRange).setValueInputOption("USER_ENTERED").execute();
        return rowToWrite;
    }
}
