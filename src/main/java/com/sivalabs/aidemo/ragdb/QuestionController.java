package com.sivalabs.aidemo.ragdb;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("db")
// QuestionController.java
@RestController
@RequestMapping("/api")
public class QuestionController {
    private final RagService ragService;

    public QuestionController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/query")
    public ResponseEntity<String> answerQuestion(@RequestParam String question) {
        String answer = ragService.answerQuestion(question);
        return ResponseEntity.ok(answer);
    }
}
