package com.devmeetups.app.registrations.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateRegistrationRequest(@Email @NotBlank String email) {

}
