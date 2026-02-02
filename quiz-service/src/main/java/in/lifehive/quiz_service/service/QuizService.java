package in.lifehive.quiz_service.service;

import in.lifehive.quiz_service.dao.QuizDao;
import in.lifehive.quiz_service.feign.QuizInterface;
import in.lifehive.quiz_service.model.QuestionWrapper;
import in.lifehive.quiz_service.model.Quiz;
import in.lifehive.quiz_service.model.QuizResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    QuizDao dao;
    QuizInterface quizInterface;

    public QuizService(QuizDao dao, QuizInterface quizInterface) {
        this.dao = dao;
        this.quizInterface = quizInterface;
    }

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Long> questionIds = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setQuestionsList(questionIds);
        quiz.setTitle(title);
        dao.save(quiz);
        return ResponseEntity.ok().body("Success");
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id) {
        List<Long> questionIds = dao.findById(id).get().getQuestionsList();
        return quizInterface.getQuestionsFromId(questionIds);
    }

    public ResponseEntity<Integer> calculateResult(Long id, List<QuizResponse> quizResponse) {
         return quizInterface.getScore(quizResponse);
    }
}
