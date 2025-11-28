package com.arthurscheper.professorcuradortoolkit.service;

import dev.langchain4j.data.message.PdfFileContent;
import jakarta.enterprise.context.ApplicationScoped;
import org.primefaces.model.file.UploadedFile;

import java.util.Base64;

@ApplicationScoped
public class FileService {

    public PdfFileContent converterArquivo(UploadedFile file) {
        byte[] fileContent = file.getContent();
        String base64Content = Base64.getEncoder().encodeToString(fileContent);

        return PdfFileContent.from(base64Content, "application/pdf");
    }
}
