package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.models.quiz.AlexaAnswer;
import com.provectus.formula.alexis.models.quiz.NextWordsReturn;
import com.provectus.formula.alexis.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("/api/alexa/quiz/groups/{groupName}")
    public ResponseEntity updateStatistic(
            @RequestBody AlexaAnswer alexaAnswer,
            @PathVariable("groupName") String groupName) {

        NextWordsReturn nextWordsReturn = quizService.nextWords(groupName, alexaAnswer);
        if (nextWordsReturn.getStatus().equals("OK"))
            return new ResponseEntity(nextWordsReturn.getAnswers(), HttpStatus.OK);
        throw new QuizException(nextWordsReturn.getStatus());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class QuizException extends RuntimeException {
        public QuizException(String status) {
            super(status);
        }
    }

}
