package com.nsv.base.tg_bot_finance_tracker_002.data.comand;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nsv.base.tg_bot_finance_tracker_002.data.Consts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class StartCommand implements Command {

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(Consts.START_MESSAGE.toString());

        List<String> allCities = List.of("One", "Two");

        try {
            addKeyboard(sendMessage, allCities);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return sendMessage;
    }

    private void addKeyboard(SendMessage sendMessage, List<String> stringList) throws JsonProcessingException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (var s : stringList) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(s);
            String jsonCallback = JsonHandler.toJson(s);
            inlineKeyboardButton.setCallbackData(jsonCallback);
            keyboardButtonsRow.add(inlineKeyboardButton);
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }

}