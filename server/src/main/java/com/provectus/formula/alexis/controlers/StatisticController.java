package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.models.statistic.AmountStatisticReturn;
import com.provectus.formula.alexis.models.statistic.StatisticPageReturn;
import com.provectus.formula.alexis.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@CrossOrigin
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @GetMapping("/api/alexa/quiz/statistic/inprogress/{groupId}")
    public ResponseEntity inprogressStatistic(Principal principal,
                                              @RequestParam int pageNumber,
                                              @PathVariable("groupId") long groupId) {
        StatisticPageReturn inprogressList = statisticService.getStatisticList(groupId, principal.getName(), pageNumber, false);
        if (inprogressList.getStatus().equals("OK"))
            return new ResponseEntity(inprogressList, HttpStatus.OK);
        throw new StatisticExceptionException(inprogressList.getStatus());

    }

    @GetMapping("/api/alexa/quiz/statistic/learned/{groupId}")
    public ResponseEntity learnedStatistic(Principal principal,
                                           @RequestParam int pageNumber,
                                           @PathVariable("groupId") long groupId) {
        StatisticPageReturn learnedList = statisticService.getStatisticList(groupId, principal.getName(), pageNumber, true);
        if (learnedList.getStatus().equals("OK"))
            return new ResponseEntity(learnedList, HttpStatus.OK);
        throw new StatisticExceptionException(learnedList.getStatus());
    }

    @GetMapping("/api/alexa/quiz/statistic/amount/{groupId}")
    public ResponseEntity amountStatistic(
            Principal principal, @PathVariable("groupId") long groupId) {
        AmountStatisticReturn amountStatistic = statisticService.getAmountStatistic(groupId, principal.getName());
        if (amountStatistic.getStatus().equals("OK"))
            return new ResponseEntity(amountStatistic, HttpStatus.OK);
        throw new StatisticExceptionException(amountStatistic.getStatus());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class StatisticExceptionException extends RuntimeException {
        public StatisticExceptionException(String status) {
            super(status);
        }
    }
}
