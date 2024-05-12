package com.rise.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name="lunch_menu")
public class LunchMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MenuID")
    private Long id;

    @Column(name="Day")
    private String day;

    @Column(name = "Food1")
    private String food1;

    @Column(name = "Food2")
    private String food2;

    @Column(name = "Food3")
    private String food3;

    @Column(name = "Food4")
    private String food4;

    @Column(name = "Food5")
    private String food5;

    @Column(name = "Food6")
    private String food6;
}
