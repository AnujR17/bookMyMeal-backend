package com.rise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    //    @OneToOne
//    @JoinColumn(name = "user_id", unique = true)
    @OneToOne
    private User user;


    public void setOtp(int otp) {
        this.otp = otp;
    }


    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }
}
