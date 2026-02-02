package in.lifehive.quiz_service.dao;

import in.lifehive.quiz_service.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, Long> {
}
