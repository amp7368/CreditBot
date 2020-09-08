package apple.credit_bot.sheets;

import apple.credit_bot.CreditMain;
import com.google.api.services.sheets.v4.Sheets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SheetsConstants {
    private static final String SHEET_ID_FILE_PATH = "data/sheetId.data";
    public static String spreadsheetId = "";

    static {
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
