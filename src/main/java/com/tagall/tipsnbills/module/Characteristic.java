package com.tagall.tipsnbills.module;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "сharacteristic")
public class Characteristic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money")
    private Long money;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    public Characteristic() {}

    public Characteristic(Long money,String review, Integer rating) {
        this.money = money;
        this.review = review;
        this.rating = rating;
    }
}
