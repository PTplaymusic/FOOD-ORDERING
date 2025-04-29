/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils.service;

import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;

public class ImageUtil {

    // Convert Part to byte[]
    public static byte[] partToBytes(Part part) throws IOException {
        try (InputStream inputStream = part.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    // ✅ Chuẩn hóa lại saveFile: thêm try-catch tự động
    public static String saveFile(Part part, String uploadDir, String fileName) throws IOException {
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File targetFile = new File(uploadDirectory, fileName);
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, targetFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        return targetFile.getAbsolutePath();
    }

    // Encode byte[] to Base64 (for displaying image in JSP)
    public static String encodeToBase64(byte[] imageBytes) {
        return java.util.Base64.getEncoder().encodeToString(imageBytes);
    }
}
