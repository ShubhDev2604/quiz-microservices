package in.lifehive.quiz_service.controller;

import in.lifehive.quiz_service.model.QuestionWrapper;
import in.lifehive.quiz_service.model.QuizDto;
import in.lifehive.quiz_service.model.QuizResponse;
import in.lifehive.quiz_service.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    QuizService service;

    public QuizController(QuizService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestBody @Valid QuizDto quizDto) {
        return service.createQuiz(quizDto.getCategoryName(), quizDto.getNumOfQuestions(), quizDto.getTitle());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Long id) {
        return service.getQuizQuestions(id);
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Long id, @RequestBody List<QuizResponse> quizResponse) {
        return service.calculateResult(id, quizResponse);
    }
}
