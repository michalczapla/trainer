package com.czaplon.trainer.model;

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

    private Float bmi;

    @Column(columnDefinition = "text")
    private String comment;

    @NotNull(message = "Cannot be empty")
    private boolean workoutMade;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    public WorkoutHistory(@NotNull(message = "Cannot be empty") Workout workout, @NotNull(message = "Cannot be empty") LocalDate date, @NotNull(message = "Cannot be empty") @Positive(message = "Weight needs to be positive") Float weight, @NotNull(message = "Cannot be empty") @Positive(message = "Waist needs to be positive") Float waist, @NotNull(message = "Cannot be empty") boolean workoutMade, User user, String comment) {
        this.workout = workout;
        this.date = date;
        this.weight = weight;
        this.waist = waist;
        this.workoutMade = workoutMade;
        this.user = user;
        this.bmi = 0.0f;
        this.comment = comment;
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

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comments) {
        this.comment = comments;
    }

    public static String describeBMI(Float bmi) {
        if (bmi<15) {
            return  "Very severely underweight (BMI < 15)";
        } else if (bmi<16) {
            return "Severely underweight (15 < BMI < 16)";
        } else if (bmi<18.5) {
            return "Underweight (16 < BMI < 18.5)";
        } else if (bmi<25) {
            return "Normal (healthy weight) (18.5 < BMI < 25)";
        } else if (bmi<30) {
            return "Overweight (25 < BMI < 30)";
        } else if (bmi<35) {
            return "Obese Class I (Moderately obese) (30 < BMI < 35)";
        } else if (bmi<40) {
            return "Obese Class II (Severely obese) (35 < BMI < 40)";
        } else if (bmi<45) {
            return "Obese Class III (Very severely obese) (40 < BMI < 45)";
        } else if (bmi<50) {
            return "Obese Class IV (Morbidly obese) (45 < BMI < 50)";
        } else if (bmi<60) {
            return "Obese Class V (Super obese) (50 < BMI < 60)";
        } else {
            return "Obese Class VI (Hyper obese) (60 < BMI)";
        }
    }

    public String describeBMI() {
        return this.describeBMI(getBmi());
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
                ", bmi=" + bmi +
                '}';
    }

}
