package com.sommerfeld.gymnote.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "workouts")

public class Workout implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "color")
    private String color;

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

    public Workout(int id, String title, String color, String exercise, float weight, int repsS1, int repsS2, int repsS3) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.exercise = exercise;
        this.weight = weight;
        this.repsS1 = repsS1;
        this.repsS2 = repsS2;
        this.repsS3 = repsS3;
    }

    @Ignore
    public Workout() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
        return "Workout{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", exercise='" + exercise + '\'' +
                ", weight=" + weight +
                ", repsS1=" + repsS1 +
                ", repsS2=" + repsS2 +
                ", repsS3=" + repsS3 +
                '}';
    }

    protected Workout(Parcel in) {
        id = in.readInt();
        title = in.readString();
        color = in.readString();
        exercise = in.readString();
        weight = in.readFloat();
        repsS1 = in.readInt();
        repsS2 = in.readInt();
        repsS3 = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(color);
        dest.writeString(exercise);
        dest.writeFloat(weight);
        dest.writeInt(repsS1);
        dest.writeInt(repsS2);
        dest.writeInt(repsS3);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
