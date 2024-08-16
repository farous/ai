package com.sivalabs.aidemo;

import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

@Profile("doc")
@Configuration
class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    private final PgVectorStore vectorStore;

    @Value("classpath:faq.csv")
    private Resource resourceCsv;

    @Value("classpath:cv.pdf")
    private Resource resourcePdf;

    public AppConfig(PgVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }


    @Bean
    ApplicationRunner runner(VectorStore vectorStore, JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.update("delete from vector_store");
//            var textReader = new TextReader(resource);
//            textReader.setCharset(Charset.defaultCharset());
//            List<Document> documents = textReader.get();
//            documents.stream().
//            vectorStore.accept(documents);
            try (CSVReader reader = new CSVReader(new InputStreamReader(resourceCsv.getInputStream()))) {
                List<String[]> csvData = reader.readAll();
                List<Document> documents = convertCsvToDocuments(csvData);
                documents.addAll(convertPdfToDocuments(resourcePdf));
                vectorStore.add(documents);

            }
//            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.defaultConfig();
//            PagePdfDocumentReader documents = new PagePdfDocumentReader(resource, config);
//            TokenTextSplitter tokenSplitter = new TokenTextSplitter();
//            List<Document> split = tokenSplitter.split(documents.get());
//            vectorStore.accept(split);
        };
    }

    //http://localhost:8080/query?question=dis%20moi%20combien%20il%20y%20a%20de%20types%20de%20baccalaur%C3%A9at
    //http://localhost:8080/query?question=dis%20moi%20qu%27est%20ce%20que%20le%20Centre%20National%20d%27Enseignement
    private List<Document> convertCsvToDocuments(List<String[]> csvData) {
        List<Document> documents = new ArrayList<>();
        for (String[] row : csvData) {
            String content = String.join(", ", row);
            Map<String, Object> metadata = Map.of("original_row", row);
            Document document = new Document(content, metadata);
            documents.add(document);
        }
        return documents;
    }


    //http://localhost:8080/query?question=dis%20moi%20quels%20sont%20les%20environnements%20techniques%20pr%C3%A9sents%20dans%20le%20cv%20de%20rachid%20ghazi
    private List<Document> convertPdfToDocuments(Resource pdf) {
        var config = PdfDocumentReaderConfig
                .builder()
                .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                        .withNumberOfBottomTextLinesToDelete(3)
                        .build())
                .build();

        var pdfReader = new PagePdfDocumentReader(pdf, config);
        var textSplitter = new TokenTextSplitter();
        return textSplitter.apply(pdfReader.get());
    }

}
