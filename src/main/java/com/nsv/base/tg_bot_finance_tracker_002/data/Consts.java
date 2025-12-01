package com.nsv.base.tg_bot_finance_tracker_002.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Consts {
    START_MESSAGE("START_MESSAGE"),
    UNKNOWN_COMMAND("UNKNOWN_COMMAND"),
    ERROR("ERROR"),
    CANT_UNDERSTAND("CANT_UNDERSTAND"),
    PIN_ADD_MSG("PIN_ADD_MSG"),
    PIN_DONT_ADD_BYE("PIN_DONT_ADD_BYE");

    private final String message;
}
