package com.example.demo;

import java.util.List;

    //Pelin toiminnallisuuden ja tilan luokka
    public class Game {
        private int score;
        private String gameState;
        private int currentQuestionIndex;
        private List<Question> questions;
        private long startTime;
    
    //Alustaa pelin
    public void start(List<Question> questions) {
        this.questions = questions;
        score = 0;
        gameState = "STARTED";
        currentQuestionIndex = -1;
        startTime = System.currentTimeMillis();
    }

    public void play(int points) {
        score += points;
        gameState = "PLAYING";
    }
    //Palauttaa pisteet
    public int getScore() {
        return score;
    }
    //Palauttaa pelin tilan
    public String getGameState() {
        return gameState;
    }
    //Asettaa kysymyksen
    public void setCurrentQuestionIndex(int index) {
        this.currentQuestionIndex = index;
    }
    //Palauttaa kysymyksen
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    //Siirtyy seuraavaan kysymykseen
    public void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
        } else {
            gameState = "ALL_QUESTIONS_ANSWERED";
        }
        startTime = System.currentTimeMillis();
    }
    //Palauttaa nykyisen kysymyksen
    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }
    //Palauttaa kuluneen ajan pelin alkamisesta
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
    //Asettaa nykyisen kysymyksen
    public void setCurrentQuestion(Question question) {
        setCurrentQuestionIndex(questions.indexOf(question));
    }
}
