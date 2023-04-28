/*
 * CustomerRequest.java
 *
 * (c) Copyright iC Consult GmbH, 2021
 * All Rights reserved.
 *
 * iC Consult GmbH
 * 45128 Essen
 * Germany
 *
 */

package com.icconsult.interview.usermanagement.api.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CustomerRequest {

    @NotBlank
    private final String givenName;

    @NotBlank
    private final String familyName;

    @NotBlank
    @Email
    private final String email;

    public CustomerRequest(String givenName, String familyName, String email) {
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerRequest that = (CustomerRequest) o;
        return Objects.equals(givenName, that.givenName) && Objects.equals(familyName, that.familyName) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(givenName, familyName, email);
    }
}
