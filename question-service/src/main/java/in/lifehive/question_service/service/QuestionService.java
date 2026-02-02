package in.lifehive.question_service.service;

import in.lifehive.question_service.dao.QuestionDao;
import in.lifehive.question_service.model.Question;
import in.lifehive.question_service.model.QuestionWrapper;
import in.lifehive.question_service.model.QuizResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    public QuestionService(QuestionDao dao) {
        this.dao = dao;
    }

    private QuestionDao dao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        if(dao.count() != 0) {
            return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<List<Question>> getAllQuestionsByCategory(String category) {
        if(dao.count() != 0) {
            return new ResponseEntity<>(dao.findAllQuestionsByCategory(category), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            dao.save(question);
            return new ResponseEntity<>("Addition Done!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> updateQuestion(Long id,Question question) {
        if(dao.existsById(id)) {
            dao.save(question);
            return new ResponseEntity<>("Updation done!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not found!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteQuestion(Long id) {
        if(dao.existsById(id)) {
            dao.deleteById(id);
            return new ResponseEntity<>("deletion done!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not found!", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Long>> getQuestionsForQuiz(String categoryName, int numQuestions) {
        List<Long> questions = dao.findRandomQuestionsByCategory(categoryName,numQuestions);
        return ResponseEntity.status(HttpStatus.CREATED).body(questions);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Long> questionIds) {

        List<QuestionWrapper> wrappers = new ArrayList<>();

        for (Long questionId : questionIds) {

            Question question = dao.findById(questionId)
                    .orElseThrow(() ->
                            new RuntimeException("Question not found with id: " + questionId)
                    );

            wrappers.add(new QuestionWrapper(
                    question.getId(),
                    question.getQuestionTitle(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4()
            ));
        }

        return ResponseEntity.ok(wrappers);
    }

    public ResponseEntity<Integer> getScore(List<QuizResponse> responseList) {

        int right = 0;
        for(QuizResponse response: responseList) {
            Question question = dao.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                right++;
        }
        return ResponseEntity.ok().body(right);
    }
}
