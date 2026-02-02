package in.lifehive.question_service.dao;

import in.lifehive.question_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Long> {

    List<Question> findAllQuestionsByCategory(String category);

    @Query(
            value = "SELECT q.id FROM question q WHERE q.category=:category ORDER BY RANDOM() LIMIT :numQ",
            nativeQuery = true
    )
    List<Long> findRandomQuestionsByCategory(String category, int numQ);
}
