package com.tagall.tipsnbills.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "сharacteristic")
public class Characteristic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money")
    private int money;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private int rating;

    public Characteristic() {}

    public Characteristic(int money,String review, int rating) {
        this.money = money;
        this.review = review;
        this.rating = rating;
    }
}
