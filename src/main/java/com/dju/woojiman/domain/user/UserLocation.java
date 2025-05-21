package com.dju.woojiman.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private UserLocationType locationType;

    private String address;
    private String city;
    private String countryCode;

    private double latitude;
    private double longitude;

    @Builder
    public UserLocation(String userId, UserLocationType locationType, String address, String city, String countryCode, double latitude, double longitude) {
        this.userId = userId;
        this.locationType = locationType;
        this.address = address;
        this.city = city;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
