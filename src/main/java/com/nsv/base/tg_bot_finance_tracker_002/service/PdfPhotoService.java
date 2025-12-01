package com.nsv.base.tg_bot_finance_tracker_002.service;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.nsv.base.tg_bot_finance_tracker_002.data.TelegramGetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;

@Service
public class PdfPhotoService {

    @Value("${bot.token}")
    private String botToken;
    @Autowired
    private IdCollectorService idCollectorService;

    private final RestTemplate restTemplate = new RestTemplate();

    public void saveId(String key, List<PhotoSize> photoSizes){
        String fileId = getFileId(photoSizes);
        idCollectorService.addId(key, fileId);
    }

    /**
     * Конвертирует список PhotoSize (обычно из одного фото в разных разрешениях)
     * в PDF-файл с одним изображением (берётся самое большое).
     */
    public byte[] downloadFileAndCreatePdf(String key) throws Exception {
        List<String> fileIds = idCollectorService.claimIds(key);
        List<byte[]> bytes = fileIds.stream().map(this::downloadFile).toList();
        ByteArrayOutputStream baos = generatePdf(bytes);
        return baos.toByteArray();
    }

    private static String getFileId(List<PhotoSize> photoSizes) {
        String fileId = photoSizes.stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .orElse(photoSizes.get(0)).getFileId();
        return fileId;
    }

    private TelegramGetFileResponse getFilePath(String fileId) {
        String getFileUrl = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;
        TelegramGetFileResponse response = restTemplate.getForObject(getFileUrl, TelegramGetFileResponse.class);

        if (response == null || response.getResult() == null || response.getResult().getFilePath() == null) {
            throw new RuntimeException("Failed to get file path from Telegram");
        }
        return response;
    }

    private byte[] downloadFile(String fileId) {
        String filePath = getFilePath(fileId).getResult().getFilePath();
        String downloadUrl = "https://api.telegram.org/file/bot" + botToken + "/" + filePath;
        byte[] imageBytes = restTemplate.getForObject(downloadUrl, byte[].class);

        if (imageBytes == null) {
            throw new RuntimeException("Failed to download image");
        }
        return imageBytes;
    }

    private static ByteArrayOutputStream generatePdf(List<byte[]> imageBytes) {
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(pdfStream);
        PdfDocument pdf = new PdfDocument(writer);

        // Устанавливаем минимальные поля
        float margin = 20;
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(margin, margin, margin, margin);

        float availableWidth = PageSize.A4.getWidth() - 2 * margin;
        float availableHeight = PageSize.A4.getHeight() - 2 * margin;

        for (byte[] imageByte : imageBytes) {
            ImageData imageData = ImageDataFactory.create(imageByte);
            float imgWidth = imageData.getWidth();
            float imgHeight = imageData.getHeight();

            // Сохраняем пропорции и вписываем в доступную область
            float scaleFactor = Math.min(availableWidth / imgWidth, availableHeight / imgHeight);
            float scaledWidth = imgWidth * scaleFactor;
            float scaledHeight = imgHeight * scaleFactor;

            Image img = new Image(imageData)
                    .setWidth(scaledWidth)
                    .setHeight(scaledHeight)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(img);
            document.add(new AreaBreak()); // новая страница
        }
        document.close();
        return pdfStream;
    }

}