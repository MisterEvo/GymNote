package com.sommerfeld.gymnote.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Workout implements Parcelable {

    private String title;
    private String color;
    private ArrayList<String> exercise;
    private ArrayList<String> weight;
    private ArrayList<String> repsS1;
    private ArrayList<String> repsS2;
    private ArrayList<String> repsS3;

    public Workout(String title, String color, ArrayList<String> exercise, ArrayList<String> weight, ArrayList<String> repsS1, ArrayList<String> repsS2, ArrayList<String> repsS3) {
        this.title = title;
        this.color = color;
        this.exercise = exercise;
        this.weight = weight;
        this.repsS1 = repsS1;
        this.repsS2 = repsS2;
        this.repsS3 = repsS3;
    }

    public Workout() {
    }

    protected Workout(Parcel in) {
        title = in.readString();
        color = in.readString();
        exercise = in.createStringArrayList();
        weight = in.createStringArrayList();
        repsS1 = in.createStringArrayList();
        repsS2 = in.createStringArrayList();
        repsS3 = in.createStringArrayList();

    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    public String getColor() {
        return color;
    }

    public ArrayList<String> getWeight() {
        return weight;
    }

    public void setWeight(ArrayList<String> weight) {
        this.weight = weight;
    }

    public void setColor(String color) {
        this.color = color;
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

    public ArrayList<String> getRepsS1() {
        return repsS1;
    }

    public void setRepsS1(ArrayList<String> repsS1) {
        this.repsS1 = repsS1;
    }

    public ArrayList<String> getRepsS2() {
        return repsS2;
    }

    public void setRepsS2(ArrayList<String> repsS2) {
        this.repsS2 = repsS2;
    }

    public ArrayList<String> getRepsS3() {
        return repsS3;
    }

    public void setRepsS3(ArrayList<String> repsS3) {
        this.repsS3 = repsS3;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", exercise=" + exercise +
                ", weight=" + weight +
                ", repsS1=" + repsS1 +
                ", repsS2=" + repsS2 +
                ", repsS3=" + repsS3 +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(color);
        dest.writeStringList(exercise);
        dest.writeStringList(weight);
        dest.writeStringList(repsS1);
        dest.writeStringList(repsS2);
        dest.writeStringList(repsS3);
    }


}
