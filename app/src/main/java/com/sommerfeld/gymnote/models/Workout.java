package com.sommerfeld.gymnote.models;

import java.util.ArrayList;

public class Workout {

    private String title;
    private ArrayList<String> exercise;
    private ArrayList<Integer> repsS1;
    private ArrayList<Integer> repsS2;
    private ArrayList<Integer> repsS3;

    public Workout(String title, ArrayList<String> exercise, ArrayList<Integer> repsS1, ArrayList<Integer> repsS2, ArrayList<Integer> repsS3) {
        this.title = title;
        this.exercise = exercise;
        this.repsS1 = repsS1;
        this.repsS2 = repsS2;
        this.repsS3 = repsS3;
    }

    public Workout() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getExercise() {
        return exercise;
    }

    public void setExercise(ArrayList<String> exercise) {
        this.exercise = exercise;
    }

    public ArrayList<Integer> getRepsS1() {
        return repsS1;
    }

    public void setRepsS1(ArrayList<Integer> repsS1) {
        this.repsS1 = repsS1;
    }

    public ArrayList<Integer> getRepsS2() {
        return repsS2;
    }

    public void setRepsS2(ArrayList<Integer> repsS2) {
        this.repsS2 = repsS2;
    }

    public ArrayList<Integer> getRepsS3() {
        return repsS3;
    }

    public void setRepsS3(ArrayList<Integer> repsS3) {
        this.repsS3 = repsS3;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "title='" + title + '\'' +
                ", exercise=" + exercise +
                ", repsS1=" + repsS1 +
                ", repsS2=" + repsS2 +
                ", repsS3=" + repsS3 +
                '}';
    }
}
