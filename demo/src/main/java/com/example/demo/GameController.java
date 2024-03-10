package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

    @RestController
    @RequestMapping("/")
    public class GameController {

    private Game game = new Game();
    private List<Question> questions;
    private Timer timer;
    private long timerStartTime;

    public GameController() {
        initializeQuestions();
    }

    private void initializeQuestions() {
        // Alustetaan kysymykset ja vastaukset
        questions = new ArrayList<>();
        questions.add(new Question("Mikä on maailman suurin valtio pinta-alaltaan?", "Venäjä"));
        questions.add(new Question("Mikä on pääkaupunki Japanissa?", "Tokio"));
        questions.add(new Question("Mikä planeetta on Aurinkokunnan suurin?", "Jupiter"));
        questions.add(new Question("Kuka oli ensimmäinen ihminen avaruudessa?", "Juri Gagarin"));
        questions.add(new Question("Mikä on maailman korkein vuori?", "Mount Everest"));
        questions.add(new Question("Mikä on suurin nisäkäs?", "Sinivalas"));
        questions.add(new Question("Mikä on maailman suurin lintu?", "Strutsi"));
    }

    // Tervetuloa-viesti
    @GetMapping("/")
    public String getInfo() {
        return "Tervetuloa tietovisaan! Käytä /start endpointtia aloittaaksesi pelin.";
    }
    
    //Palauttaa pistemäärän
    @GetMapping("/score")
    public ResponseEntity<Integer> getScore() {
        return ResponseEntity.ok(game.getScore());
    }

    

    // Palauttaa pelin tilan
    @GetMapping("/state")
    public ResponseEntity<Integer> getGameState() {
        return ResponseEntity.ok(game.getScore());
    }
    


    // Palauttaa satunnaisen kysymyksen ja käynnistää vastausajan laskennan
    @GetMapping("/question")
    public Question getQuestion() {
        startTimer();
        return getRandomQuestion();
    }

    // Käsittelee pelaajan vastauksen, lopettaa vastausajan laskennan ja antaa pisteet jotka määräytyvät siitä kuinka nopeaa pelaaja vastaa oikein
    @PostMapping("/answer")
    public ResponseEntity<String> submitAnswer(@RequestBody Map<String, String> request) {
        stopTimer();
        String answer = request.get("answer");
        boolean isCorrect = checkAnswer(answer);

        if (isCorrect) {
            long elapsedTime = timerElapsedSeconds();
            int points = calculatePoints(elapsedTime);
            game.play(points);
            return ResponseEntity.ok("Oikein! Sait pisteitä: " + points);
        } else {
            return ResponseEntity.ok("Väärin!");
        }
    }

    // Palauttaa satunnaisen kysymyksen
    private Question getRandomQuestion() {
        Random random = new Random();
        int index = random.nextInt(questions.size());
        return questions.get(index);
    }

    // Tarkistaa pelaajan vastauksen 
    private boolean checkAnswer(String playerAnswer) {
        Question currentQuestion = game.getCurrentQuestion();
        return currentQuestion != null && currentQuestion.getAnswer().equalsIgnoreCase(playerAnswer);
    }

    // Käynnistää vastausajan laskennan ja asettaa ajastimen
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.setCurrentQuestion(getRandomQuestion());
            }
        }, 10000); // 10 sekuntia vastausaikaa
        timerStartTime = System.currentTimeMillis();
    }

    // Pysäyttää ajastimen
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    // Laskee pelaajan saamat pisteet vastausajan perusteella
    private int calculatePoints(long elapsedTime) {
        int maxPoints = 10; // Maksimipistemäärä
        return Math.max(0, maxPoints - (int) elapsedTime);
    }

    // Palauttaa kuluneen ajan pelin alkamisesta
    private long timerElapsedSeconds() {
        if (timer != null) {
            long elapsedTime = (System.currentTimeMillis() - timerStartTime) / 1000;
            return elapsedTime;
        }
        return 0;
    }
}
