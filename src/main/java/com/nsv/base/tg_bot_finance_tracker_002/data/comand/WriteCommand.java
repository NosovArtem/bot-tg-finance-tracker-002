package com.nsv.base.tg_bot_finance_tracker_002.data.comand;


import com.nsv.base.tg_bot_finance_tracker_002.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class WriteCommand implements Command {

    private final GoogleSheetsService sheetsService;

    @Override
    public SendMessage apply(Update update) {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        // Пример: /add Иван,25,engineer
        String[] parts = messageText.substring(5).split(",");
        try {
            sheetsService.appendRow(java.util.Arrays.asList(parts), "Sheet1");
            return createMsg(chatId, "Данные добавлены!");
        } catch (Exception e) {
            return createMsg(chatId, "Ошибка при записи: " + e.getMessage());
        }
    }

    private SendMessage createMsg(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }
}
