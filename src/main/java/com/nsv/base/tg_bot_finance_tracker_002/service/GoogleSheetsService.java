package com.nsv.base.tg_bot_finance_tracker_002.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {

    private final Sheets sheets;
    private final String spreadsheetId;

    public GoogleSheetsService(Sheets sheets, @Value("${google.sheets.spreadsheet-id}") String spreadsheetId) {
        this.sheets = sheets;
        this.spreadsheetId = spreadsheetId;
    }

    // Чтение данных из листа (например, "Sheet1!A1:C10")
    public List<List<Object>> readData(String range) throws IOException {
        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues();
    }

    // Запись данных в конец листа
    public void appendRow(List<Object> row, String sheetName) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(Collections.singletonList(row));

        sheets.spreadsheets().values()
                .append(spreadsheetId, sheetName, body)
                .setValueInputOption("RAW")
                .execute();
    }
}