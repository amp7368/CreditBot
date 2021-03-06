package apple.credit_bot.sheets;

import apple.credit_bot.CreditMain;
import com.google.api.services.sheets.v4.Sheets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SheetsConstants {
    public static String spreadsheetId = "";

    static {
        List<String> list = Arrays.asList(CreditMain.class.getProtectionDomain().getCodeSource().getLocation().getPath().split("/"));
        String SHEET_ID_FILE_PATH = String.join("/", list.subList(0, list.size() - 1)) + "/data/sheetId.data";
        File file = new File(SHEET_ID_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            System.err.println("Please fill in the id for the sheet in '" + SHEET_ID_FILE_PATH + "'");
            System.exit(1);
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            spreadsheetId = reader.readLine();
            reader.close();
        } catch (IOException e) {
            System.err.println("Please fill in the id for the sheet in '" + SHEET_ID_FILE_PATH + "'");
            System.exit(1);
        }
    }

    public static Sheets.Spreadsheets.Values sheets = CreditMain.service.spreadsheets().values();
}
