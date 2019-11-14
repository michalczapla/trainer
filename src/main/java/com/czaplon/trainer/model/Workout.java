package com.czaplon.trainer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be empty")
    @Size(min=1,message = "Name cannot be empty")
    private String name;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;



    public Workout(String name) {
        this.name = name;
    }

    public Workout() {
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + ((user!=null) ? user.getId() : null)+
                '}';
    }
}
