package com.nsv.base.tg_bot_finance_tracker_002.data.comand;

import com.nsv.base.tg_bot_finance_tracker_002.data.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@Slf4j
public class CommandsHandler {

    private final Map<String, Command> commands;

    public CommandsHandler(StartCommand startCommand,
                           WriteCommand writeCommand,
                           ReadCommand readCommand,
                           PdfCommand pdfCommand) {
        this.commands = Map.of(
                "/start", startCommand,
                "/add", writeCommand,
                "/read", readCommand,
                "/pdf", pdfCommand
        );
    }

    public SendMessage handleCommands(Update update) {
        String messageText = update.getMessage().getText();
        String commandAsText = messageText.split(" ")[0]; // до пробела - команда, через пробел могут быть аргументы
        long chatId = update.getMessage().getChatId();

        var command = commands.get(commandAsText);
        if (command != null) {
            return command.apply(update);
        } else {
            return new SendMessage(String.valueOf(chatId), Consts.UNKNOWN_COMMAND.toString());
        }
    }

}