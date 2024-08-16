package com.sivalabs.aidemo.ragdb;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.processing.Completion;
import java.util.List;
import java.util.Map;

@Profile("db")
// RagService.java
@Service
public class RagService {
    private final DatabaseService databaseService;
    private final OllamaApi ollamaApi;
    private final ChatClient chatClient;
    public RagService(DatabaseService databaseService, OllamaApi ollamaApi, ChatClient.Builder chatClientBuilder) {
        this.databaseService = databaseService;
        this.ollamaApi = ollamaApi;
        this.chatClient = chatClientBuilder.build();
    }

    public String answerQuestion(String question) {
        String relevantData = databaseService.retrieveRelevantData(question);

        String prompt = "Based on the following data:\n\n" + relevantData +
                "\n\nPlease answer the following question: " + question;

        var chatModel = new OllamaChatModel(ollamaApi,
                OllamaOptions.create()
                        .withModel("llama3")
                        .withTemperature(0f));

        ChatResponse response = chatModel.call(
                new Prompt(prompt));


        return response.getResult().getOutput().getContent();

//        var systemPromptTemplate = new SystemPromptTemplate("""
//                You are a helpful assistant.
//                Context information is below.
//                CONTEXT: {information}
//                Given the context information and not prior knowledge. Answer in the same langage as the question and in less than 20 words.
//                QUESTION: {question}
//                """);
//        var systemMessage = systemPromptTemplate.createMessage(
//                Map.of("information", relevantData, "question", question));
//
////        var outputConverter = new BeanOutputConverter<>(Person.class);
////        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
////                Tell me about {name} as if current date is {current_date}.
////
////                {format}
////                """);
////        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
////                {name}
////                """);
////        Map<String, Object> model = Map.of("name", name
////                ,
////                "format", outputConverter.getFormat()
////        );
////        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());
//
//        var prompt = new Prompt(List.of(systemMessage));
//
//
////        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
////        que est le poste de Guazi rachid dans le groupe M6 ?
////        """);
////        Map<String, Object> model = Map.of("name", name);
////        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());
//
////        var prompt = new Prompt(List.of(systemMessage));
//
//        return chatClient.prompt(prompt).call().content();
    }
}
