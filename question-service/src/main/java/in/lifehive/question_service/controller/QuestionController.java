package in.lifehive.question_service.controller;

import in.lifehive.question_service.model.Question;
import in.lifehive.question_service.model.QuestionWrapper;
import in.lifehive.question_service.model.QuizResponse;
import in.lifehive.question_service.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>>  getAllQuestions() {
        return service.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getAllQuestionsByCategory(@PathVariable String category) {
        return service.getAllQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return service.addQuestion(question);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long id,@RequestBody Question question) {
        return service.updateQuestion(id, question);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long id) {
        return service.deleteQuestion(id);
    }


    //generate quiz
    @GetMapping("/generate")
    public ResponseEntity<List<Long>> getQuestionsForQuiz
    (@RequestParam String categoryName, @RequestParam int numQuestions) {
        return service.getQuestionsForQuiz(categoryName, numQuestions);
    }

    //getQuestions based on quiz id
    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Long> questionIds) {
        return service.getQuestionsFromId(questionIds);
    }

    //getScore
    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<QuizResponse> responseList) {
        return service.getScore(responseList);
    }

    @GetMapping("/")
    public ResponseEntity<String> checkingRunningStatus() {
        return ResponseEntity.ok().body("Hello Question service is running");
    }
}
