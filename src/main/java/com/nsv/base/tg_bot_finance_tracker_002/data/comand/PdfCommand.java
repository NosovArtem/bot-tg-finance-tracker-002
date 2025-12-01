package com.nsv.base.tg_bot_finance_tracker_002.data.comand;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class PdfCommand implements Command {

//    private final String msg = """
//            <br>PDF</br>
//            <i>Для конвертирования jpeg в PDF</i>
//            <></>
//            """;

//    private final String msg = """
//            <b>Привет!</b>
//            Это <i>пример</i> форматированного текста.
//            Используйте <b>теги</b> для жирного шрифта
//            и <i>теги</i> для курсива.
//            Ссылка: <a href=\\"https://google.com\\">Google</a>
//            """;

    private final String msg = """
            PDF
            1) Загрузите нужное кол-во фото. (нужно подписать 'pdf')
            2) Отправьте текстом 'toPdf' для формирования pdf файла.
            """;

    @Override
    public SendMessage apply(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .parseMode(ParseMode.HTML)
                .build();
        return sendMessage;
    }
}
