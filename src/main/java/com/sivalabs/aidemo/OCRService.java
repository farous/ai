package com.sivalabs.aidemo;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;

@Profile("doc")
@Service
public class OCRService {

    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();
        //tesseract.setDatapath("C:\\Users\\fares\\IdeaProjects\\spring-ai-openai\\src\\main\\resources\\eng.traineddata"); // Set this to the path where your Tesseract data is stored
//        tesseract.setDatapath("eng.traineddata"); // Set this to the path where your Tesseract data is stored
        // Optionally set the language
        tesseract.setLanguage("fra");
    }

    public String performOCR(File imageFile) throws TesseractException {
        return tesseract.doOCR(imageFile);
    }
}