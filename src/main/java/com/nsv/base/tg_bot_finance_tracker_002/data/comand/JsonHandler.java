package com.nsv.base.tg_bot_finance_tracker_002.data.comand;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
    }

    public static String toJson(String value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    public static List<String> toList(String data) {
        List<String> strings = List.of("");
        return strings;
    }
}
