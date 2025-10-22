package com.example.Ecom_Project.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

// ✅ New Embedded class for the Shipping Address
@Getter
@Setter
@Embeddable
public class ShippingAddress {
    private String fullName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
