package com.sivalabs.aidemo;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("doc")
@RestController
class RAGController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final OCRService ocrService;

    RAGController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore, OCRService ocrService) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.ocrService = ocrService;
    }




    @GetMapping("/queryImage")
    String chatWithRagImage() throws TesseractException {
        String contentImage = ocrService.performOCR(new File("C:\\Users\\fares\\IdeaProjects\\spring-ai-openai\\src\\main\\resources\\test.jpg"));

        // Prepare prompt for AI analysis
        String promptMessage = "Analyze the following text extracted from an image using OCR: \n\n" + contentImage +
                "\n\nProvide a summary of the main points and any key information. give the answer in french";
        //Count the number of words that start with a t
        Message systemMessage = new SystemPromptTemplate(promptMessage).createMessage();
        var prompt = new Prompt(List.of(systemMessage));
        return chatClient.prompt(prompt).call().content();

    }


    @GetMapping("/query")
    String chatWithRag(@RequestParam String question) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
        String information = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));
        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant.
                Context information is below.
                CONTEXT: {information}
                Given the context information and not prior knowledge. Answer in the same langage as the question and in 20 words.
                QUESTION: {question}
                """);
        var systemMessage = systemPromptTemplate.createMessage(
                Map.of("information", information, "question", question));

//        var outputConverter = new BeanOutputConverter<>(Person.class);
//        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
//                Tell me about {name} as if current date is {current_date}.
//
//                {format}
//                """);
//        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
//                {name}
//                """);
//        Map<String, Object> model = Map.of("name", name
//                ,
//                "format", outputConverter.getFormat()
//        );
//        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());

        var prompt = new Prompt(List.of(systemMessage));


//        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
//        que est le poste de Guazi rachid dans le groupe M6 ?
//        """);
//        Map<String, Object> model = Map.of("name", name);
//        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());

//        var prompt = new Prompt(List.of(systemMessage));

        var response = chatClient.prompt(prompt).call().content();

        return response;
//        return outputConverter.convert(response);
    }



    @Deprecated
    @GetMapping("/query2")
    String chatWithRag2(@RequestParam String question) {
        List<Document> similarDocuments = vectorStore.similaritySearch(question);
        //String information = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));

        String systemMessageTemplate = """
                Je vais te donner des questions réponses sous forme de FAQ.
                Répond aux questions en te basant sur le CONTEXT
                Si la réponse n'est pas dans le CONTEXT, répoond par "je ne sais pas".
                Réponds seulement en francais en 20 mots maximum.
                Voici le CONTEXT :
                  {CONTEXT}
                """;

//        Answer the following question based on the provided CONTEXT
//        If the answer is not in the context, respond "I dont't know".
//                Respond only in french and in a minimum number of words.
//        CONTEXT :
//        {CONTEXT}
//        """


Message systemMessage = new SystemPromptTemplate(systemMessageTemplate)
        .createMessage(Map.of("CONTEXT", similarDocuments));
UserMessage userMessage = new UserMessage(question);
var prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().content();
}

}
