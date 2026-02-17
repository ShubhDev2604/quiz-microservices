package in.lifehive.quiz_service.feign;

import in.lifehive.quiz_service.model.QuestionWrapper;
import in.lifehive.quiz_service.model.QuizResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback implementation for QuizInterface Feign client.
 * Returns dummy/default responses when Question Service is unavailable.
 */
@Component
public class QuizInterfaceFallback implements QuizInterface {

    @Override
    public ResponseEntity<List<Long>> getQuestionsForQuiz(String categoryName, int numQuestions) {
        // Return dummy question IDs when Question Service is down
        List<Long> dummyQuestionIds = new ArrayList<>();
        for (int i = 1; i <= numQuestions; i++) {
            dummyQuestionIds.add((long) i);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(dummyQuestionIds);
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Long> questionIds) {
        // Return dummy quiz questions when Question Service is down
        List<QuestionWrapper> dummyQuestions = new ArrayList<>();
        for (Long id : questionIds) {
            QuestionWrapper question = new QuestionWrapper();
            question.setId(id);
            question.setQuestionTitle("Service temporarily unavailable. Please try again later.");
            question.setOption1("Option A");
            question.setOption2("Option B");
            question.setOption3("Option C");
            question.setOption4("Option D");
            dummyQuestions.add(question);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(dummyQuestions);
    }

    @Override
    public ResponseEntity<Integer> getScore(List<QuizResponse> responseList) {
        // Return 0 score when Question Service is down
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(0);
    }
}
