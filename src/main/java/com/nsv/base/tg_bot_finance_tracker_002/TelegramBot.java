package com.nsv.base.tg_bot_finance_tracker_002;


import com.nsv.base.tg_bot_finance_tracker_002.data.callback.CallbacksHandler;
import com.nsv.base.tg_bot_finance_tracker_002.data.comand.CommandsHandler;
import com.nsv.base.tg_bot_finance_tracker_002.config.BotProperties;
import com.nsv.base.tg_bot_finance_tracker_002.service.PdfPhotoService;
import com.nsv.base.tg_bot_finance_tracker_002.data.Consts;
import com.nsv.base.tg_bot_finance_tracker_002.service.OneShotSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final OneShotSchedulerService oneShotSchedulerService;
    public static String PDF = "Pdf";
    public static String PDF_COMMAND = "toPdf";
    private final PdfPhotoService pdfPhotoService;
    private final BotProperties botProperties;
    private final CommandsHandler commandsHandler;
    private final CallbacksHandler callbacksHandler;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (Objects.nonNull(update.getMessage()) && PDF.equalsIgnoreCase(update.getMessage().getCaption())) {
            Long chatId = update.getMessage().getChatId();
            List<PhotoSize> photo = update.getMessage().getPhoto();
            pdfPhotoService.saveId(getKeyForPdf(chatId), photo);
        }
        if (Objects.nonNull(update.getChannelPost()) && PDF.equalsIgnoreCase(update.getChannelPost().getCaption())) {
            Long chatId = update.getChannelPost().getChatId();
            List<PhotoSize> photos = update.getChannelPost().getPhoto();
            pdfPhotoService.saveId(getKeyForPdf(chatId), photos);
        }
        if (update.hasMessage() && update.getMessage().hasText() && PDF_COMMAND.equalsIgnoreCase(update.getMessage().getText())) {
            Long chatId = update.getMessage().getChatId();
            convertPhoto(chatId, getKeyForPdf(chatId));
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().getText().startsWith("/")) {
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chatId, Consts.CANT_UNDERSTAND.toString()));
            }
        } else if (update.hasCallbackQuery()) {
            sendMessage(callbacksHandler.handleCallbacks(update));
        }
    }

    private static String getKeyForPdf(Long chatId) {
        return chatId + "_" + PDF;
    }

    private void convertPhoto(Long chatId, String key) {
        try {
            byte[] pdfBytes = pdfPhotoService.downloadFileAndCreatePdf(key);

            SendDocument sendDoc = SendDocument.builder()
                    .chatId(chatId.toString())
                    .document(new InputFile(new ByteArrayInputStream(pdfBytes), "photo.pdf"))
                    .build();

            execute(sendDoc);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                execute(new SendMessage(chatId.toString(), "Ошибка при создании PDF"));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
