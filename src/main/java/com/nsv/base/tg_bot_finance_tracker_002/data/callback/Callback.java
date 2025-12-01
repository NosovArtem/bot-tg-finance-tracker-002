package com.nsv.base.tg_bot_finance_tracker_002.data.callback;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Callback {

    String getCallbackType();

    String getData();

    SendMessage apply(Callback callback, Update update);
}
