package com.nsv.base.tg_bot_finance_tracker_002.data.callback;

import com.nsv.base.tg_bot_finance_tracker_002.data.comand.JsonHandler;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@NoArgsConstructor
public class CallbacksHandler {

//    private final Map<CallbackType, Callback> callbacks;
//
//    public CallbacksHandler( TypeChooseCallback typeChooseCallback) {`
//        this.callbacks = Map.of(CallbackType.TYPE_CHOOSE, typeChooseCallback);
//    }

    public SendMessage handleCallbacks(Update update) {
        List<String> list = JsonHandler.toList(update.getCallbackQuery().getData());
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        System.out.println();

//        SendMessage answer;
//        if (list.isEmpty()) {
//            answer = new SendMessage(String.valueOf(chatId), Consts.ERROR.getMessage());
//        } else {
//            Callback callback = Callback.builder().callbackType(CallbackType.valueOf(list.get(0))).data(list.get(1)).build();
//            Callback callbackBiFunction = callbacks.get(callback.getCallbackType());
//            answer = callbackBiFunction.apply(callback, update);
//        }

        return null;
    }

}