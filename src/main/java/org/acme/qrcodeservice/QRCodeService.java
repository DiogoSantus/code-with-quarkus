package org.acme.qrcodeservice;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class QRCodeService {

    private final String storagePath = "qrcodes/";

    public Uni<String> createQRCode(String data, int width, int height) {
        // Input validation: Ensure data is not null or empty
        if (data == null || data.isEmpty()) {
            return Uni.createFrom().failure(new IllegalArgumentException("Invalid data input."));
        }

        // Sanitization: Clean data input before processing
        String sanitizedData = sanitizeInput(data);

        String imageFormat = "png";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;

        try {
            bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            return Uni.createFrom().failure(new RuntimeException("Failed to create QR code", e));
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, pngOutputStream);
        } catch (IOException e) {
            return Uni.createFrom().failure(new RuntimeException("Failed to write QR code image", e));
        }

        byte[] pngData = pngOutputStream.toByteArray();
        String fileName = System.currentTimeMillis() + ".png";
        try {
            Path path = Paths.get(storagePath + fileName);
            Files.write(path, pngData);
            return Uni.createFrom().item(fileName);
        } catch (IOException e) {
            return Uni.createFrom().failure(new RuntimeException("Failed to generate or save QR code", e));
        }
    }

    private String sanitizeInput(String input) {
        // Remove potentially harmful characters using a regular expression
        // For example, remove anything that's not alphanumeric or spaces
        String sanitizedInput = input.replaceAll("[^a-zA-Z0-9\\s]", "");

        return sanitizedInput;
    }

    public Uni<List<String>> getAllQRCodeFiles() {
        try {
            List<String> fileNames = new ArrayList<>();
            Files.list(Paths.get(storagePath))
                    .filter(Files::isRegularFile)
                    .forEach(file -> fileNames.add(file.getFileName().toString()));
            return Uni.createFrom().item(fileNames);
        } catch (IOException e) {
            return Uni.createFrom().failure(new RuntimeException("Failed to retrieve QR code files", e));
        }
    }
}
