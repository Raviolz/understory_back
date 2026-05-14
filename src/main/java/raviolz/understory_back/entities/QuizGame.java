package raviolz.understory_back.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raviolz.understory_back.enums.QuizAnswerOption;
import raviolz.understory_back.exceptions.ValidationException;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "quiz_games")
public class QuizGame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "experience_id", nullable = false, unique = true)
    private Experience experience;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    @Column(name = "answer_a", nullable = false, length = 500)
    private String answerA;

    @Column(name = "answer_b", nullable = false, length = 500)
    private String answerB;

    @Column(name = "answer_c", nullable = false, length = 500)
    private String answerC;

    @Column(name = "answer_d", nullable = false, length = 500)
    private String answerD;

    @Enumerated(EnumType.STRING)
    @Column(name = "correct_answer", nullable = false, length = 20)
// potrebbe essere tranquillamente 1 ma lascio alzo in caso si decida di cambiare enum di riferimento
    private QuizAnswerOption correctAnswer;

    @Column(name = "explanation_text", nullable = false, length = 1500)
    private String explanationText;

    public QuizGame(Experience experience, String questionText, String answerA, String answerB, String answerC, String answerD, QuizAnswerOption correctAnswer, String explanationText
    ) {
        setExperience(experience);
        setQuestionText(questionText);
        setAnswerA(answerA);
        setAnswerB(answerB);
        setAnswerC(answerC);
        setAnswerD(answerD);
        setCorrectAnswer(correctAnswer);
        setExplanationText(explanationText);
    }

    private void setExperience(Experience experience) {
        if (experience == null) {
            throw new ValidationException("Experience is required");
        }
        this.experience = experience;
    }

    private void setQuestionText(String questionText) {
        if (questionText == null || questionText.isBlank()) {
            throw new ValidationException("Question text is required");
        }
        this.questionText = questionText;
    }

    private void setAnswerA(String answerA) {
        if (answerA == null || answerA.isBlank()) {
            throw new ValidationException("Answer A is required");
        }
        this.answerA = answerA;
    }

    private void setAnswerB(String answerB) {
        if (answerB == null || answerB.isBlank()) {
            throw new ValidationException("Answer B is required");
        }
        this.answerB = answerB;
    }

    private void setAnswerC(String answerC) {
        if (answerC == null || answerC.isBlank()) {
            throw new ValidationException("Answer C is required");
        }
        this.answerC = answerC;
    }

    private void setAnswerD(String answerD) {
        if (answerD == null || answerD.isBlank()) {
            throw new ValidationException("Answer D is required");
        }
        this.answerD = answerD;
    }

    private void setCorrectAnswer(QuizAnswerOption correctAnswer) {
        if (correctAnswer == null) {
            throw new ValidationException("Correct answer is required");
        }

        this.correctAnswer = correctAnswer;
    }

    private void setExplanationText(String explanationText) {
        if (explanationText == null || explanationText.isBlank()) {
            throw new ValidationException("Explanation text is required");
        }
        this.explanationText = explanationText;
    }

    // domain methods


    public boolean isCorrectAnswer(QuizAnswerOption selectedAnswer) {
        return selectedAnswer != null && this.correctAnswer == selectedAnswer;
    }


    @Override
    public String toString() {
        return "QuizGame{" +
                "id=" + id +
                ", experience=" + (experience != null ? experience.getTitle() : "N/A") +
                ", questionText='" + questionText + '\'' +
                ", answerA='" + answerA + '\'' +
                ", answerB='" + answerB + '\'' +
                ", answerC='" + answerC + '\'' +
                ", answerD='" + answerD + '\'' +
                ", correctAnswer=" + correctAnswer +
                ", explanationText='" + explanationText + '\'' +
                '}';
    }
}