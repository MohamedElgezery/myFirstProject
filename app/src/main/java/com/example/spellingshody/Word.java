package com.example.spellingshody;

public class Word {
    public Word(int id, String word, String day, String week) {
        this.id = id;
        this.word = word;
        this.day = day;
        this.week = week;
    }

    public Word(String word, String day, String week) {
        this.day = day;
        this.week = week;
        this.word = word;
    }

    private int id;
    private String day;
    private String week;
    private String word;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
