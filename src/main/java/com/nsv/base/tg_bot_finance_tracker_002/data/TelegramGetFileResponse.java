package com.nsv.base.tg_bot_finance_tracker_002.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramGetFileResponse {

    private boolean ok;
    private FileResult result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public FileResult getResult() {
        return result;
    }

    public void setResult(FileResult result) {
        this.result = result;
    }

    public static class FileResult {

        @JsonProperty("file_path")
        private String filePath;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}