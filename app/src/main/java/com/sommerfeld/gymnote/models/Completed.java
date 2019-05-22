package com.sommerfeld.gymnote.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "completed")
public class Completed {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "exercise")
    private String exercise;

    @ColumnInfo(name = "weight")
    private float weight;

    @ColumnInfo(name = "repsS1")
    private int repsS1;

    @ColumnInfo(name = "repsS2")
    private int repsS2;

    @ColumnInfo(name = "repsS3")
    private int repsS3;

    public Completed(int id, String timestamp, String title, String exercise, float weight, int repsS1, int repsS2, int repsS3) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.exercise = exercise;
        this.weight = weight;
        this.repsS1 = repsS1;
        this.repsS2 = repsS2;
        this.repsS3 = repsS3;
    }

    @Ignore
    public Completed() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getRepsS1() {
        return repsS1;
    }

    public void setRepsS1(int repsS1) {
        this.repsS1 = repsS1;
    }

    public int getRepsS2() {
        return repsS2;
    }

    public void setRepsS2(int repsS2) {
        this.repsS2 = repsS2;
    }

    public int getRepsS3() {
        return repsS3;
    }

    public void setRepsS3(int repsS3) {
        this.repsS3 = repsS3;
    }

    @Override
    public String toString() {
        return "Completed{" +
                "id=" + id +
                ", timestamp='" + timestamp + '\'' +
                ", title='" + title + '\'' +
                ", exercise='" + exercise + '\'' +
                ", weight=" + weight +
                ", repsS1=" + repsS1 +
                ", repsS2=" + repsS2 +
                ", repsS3=" + repsS3 +
                '}';
    }
}
