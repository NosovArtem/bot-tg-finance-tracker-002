package com.nsv.base.tg_bot_finance_tracker_002.data.comand;

import com.nsv.base.tg_bot_finance_tracker_002.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class ReadCommand implements Command {
    private final GoogleSheetsService sheetsService;

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        try {
            var data = sheetsService.readData("Sheet1!A1:C10");
            StringBuilder sb = new StringBuilder("Данные:\n");
            for (var row : data) {
                sb.append(String.join(", ", row.stream().map(Object::toString).toArray(String[]::new))).append("\n");
            }
            return createMsg(chatId, sb.toString());
        } catch (Exception e) {
            return createMsg(chatId, "Ошибка при чтении: " + e.getMessage());
        }
    }

    private SendMessage createMsg(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }
}
