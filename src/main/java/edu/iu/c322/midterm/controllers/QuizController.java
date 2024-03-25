package edu.iu.c322.midterm.controllers;

import edu.iu.c322.midterm.model.Question;
import edu.iu.c322.midterm.model.Quiz;
import edu.iu.c322.midterm.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
public class QuizController {
    private FileRepository fileRepository;

    public QuizController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping
    public int addQuiz(@RequestBody Quiz quiz) throws IOException {
        try {
            List<Quiz> quizzes = fileRepository.findAllQuizzes();
            for (Quiz q : quizzes) {
                if (q.getTitle() == quiz.getTitle()) {
                    Quiz sameQuiz = fileRepository.updateQuiz(quiz.getId(), quiz);
                    return 0;
                }
            }
            return fileRepository.addQuiz(quiz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() throws IOException {
        try {
            return fileRepository.findAllQuizzes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer id) {
        try {
            Quiz quiz = fileRepository.getQuiz(id);
            if (quiz != null) {
                return ResponseEntity.ok(quiz);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable Integer id, @RequestBody Quiz quizInfo) {
        try {
            Quiz quiz = fileRepository.updateQuiz(id, quizInfo);
            if (quiz == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
            }
            return ResponseEntity.status(HttpStatus.FOUND).body("Quiz found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
