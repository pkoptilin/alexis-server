package com.provectus.formula.alexis.models.quiz;

import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.StatisticEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;
import com.provectus.formula.alexis.repository.StatisticRepository;
import com.provectus.formula.alexis.repository.WordRepository;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private StatisticRepository statisticRepository;
    private WordRepository wordRepository;

    private UsersEntity user;
    private GroupEntity group; // if = null, all groups
    private ArrayList<WordAnswer> answers;

    public Quiz() {
    }

    public Quiz(StatisticRepository statisticRepository, WordRepository wordRepository,
                UsersEntity user, GroupEntity group, ArrayList<WordAnswer> answers) {
        this.statisticRepository = statisticRepository;
        this.wordRepository = wordRepository;
        this.user = user;
        this.group = group;
        this.answers = answers;
    }

    public void cleanQuiz() {
        if (answers.size() == 0) {
            statisticRepository.cleanQuizByUserId(user.getId());
        }
    }

    public void updateQuizStatistic() {
        if (answers.size() != 0) {
            for (int i = 0; i < answers.size(); i++) {
                StatisticEntity statistic = statisticRepository.findByWordRelationId(answers.get(i).getRusWordId());
                if (answers.get(i).isAnswer())
                    statistic.setCorrectAnswers(statistic.getCorrectAnswers() + 1);
                statistic.setAllAnswers(statistic.getAllAnswers() + 1);
                statistic.setQuizCount(statistic.getQuizCount() + 1);
                statisticRepository.save(statistic);
            }
        }
    }

    public List<NextWord> getNextWords() {
        long[] wordsIDByTheSmallestStatistic;
        //get next words id
        if (group == null) { //all
            wordsIDByTheSmallestStatistic =
                    wordRepository.findWordIDByTheSmallestStatistic((long) user.getId());
        } else { //group
            wordsIDByTheSmallestStatistic =
                    wordRepository.findWordIDByTheWordGroupAndSmallestStatistic(
                            group.getId(), (long) user.getId());
        }

        //add answers to words
        List<NextWord> nextWords = new ArrayList<>();
        for (int i = 0; i < wordsIDByTheSmallestStatistic.length; i++) {
            WordEntity nextWord = wordRepository.getById(wordsIDByTheSmallestStatistic[i]);
            ArrayList answers = new ArrayList();

            //find answers to word
            String[] answersByWordId = wordRepository.findAnswersByWordId(wordsIDByTheSmallestStatistic[i]);
            for (String ans : answersByWordId) {
                answers.add(ans);
            }

            NextWord nextWordReturn = new NextWord(nextWord.getId(),
                    nextWord.getRuWordEntity().getRuWord(),
                    nextWord.getRuWordEntity().getFileName(),
                    answers);
            nextWords.add(nextWordReturn);
        }
        return nextWords;
    }

    public UsersEntity getUser() {
        return user;
    }

    public void setUser(UsersEntity user) {
        this.user = user;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public void setAnswers(ArrayList<WordAnswer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz quiz = (Quiz) o;

        if (user != null ? !user.equals(quiz.user) : quiz.user != null) return false;
        if (group != null ? !group.equals(quiz.group) : quiz.group != null) return false;
        return answers != null ? answers.equals(quiz.answers) : quiz.answers == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (answers != null ? answers.hashCode() : 0);
        return result;
    }
}
