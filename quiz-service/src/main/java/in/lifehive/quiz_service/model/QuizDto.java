package in.lifehive.quiz_service.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    @NotBlank
    private String categoryName;

    @Min(5)
    @Max(20)
    private int numOfQuestions;
    @NotBlank
    private String title;
}
