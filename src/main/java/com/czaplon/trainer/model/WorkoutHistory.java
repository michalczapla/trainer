package com.czaplon.trainer.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@Entity
public class WorkoutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="workout_id")
    private Workout workout;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @NotNull(message = "Cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //yyyy-MM-dd
    private LocalDate date;

    @NotNull(message = "Cannot be empty")
    @Positive(message = "Weight needs to be positive")
    private Float weight;

    @NotNull(message = "Cannot be empty")
    @Positive(message = "Waist needs to be positive")
    private Float waist;

    @NotNull(message = "Cannot be empty")
    private boolean workoutMade;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public WorkoutHistory(@NotNull(message = "Cannot be empty") Workout workout, @NotNull(message = "Cannot be empty") LocalDate date, @NotNull(message = "Cannot be empty") @Positive(message = "Weight needs to be positive") Float weight, @NotNull(message = "Cannot be empty") @Positive(message = "Waist needs to be positive") Float waist, @NotNull(message = "Cannot be empty") boolean workoutMade, User user) {
        this.workout = workout;
        this.date = date;
        this.weight = weight;
        this.waist = waist;
        this.workoutMade = workoutMade;
        this.user = user;
    }

    public WorkoutHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getWaist() {
        return waist;
    }

    public void setWaist(Float waist) {
        this.waist = waist;
    }

    public boolean isWorkoutMade() {
        return workoutMade;
    }

    public void setWorkoutMade(boolean workoutMade) {
        this.workoutMade = workoutMade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WorkoutHistory{" +
                "id=" + id +
                ", workout=" + ((workout!=null) ? workout.getId() : null) +
                ", user=" + ((user!=null) ? user.getId() : null)  +
                ", date=" + date +
                ", weight=" + weight +
                ", waist=" + waist +
                ", workoutMade=" + workoutMade +
                ", image=" + image +
                '}';
    }
}
