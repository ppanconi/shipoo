package com.shipoo.user.vo;


import lombok.Value;

import java.util.Optional;

@Value
public class ShipooUserRegistration {

    private final String email;

    private final Optional<String> firstName;

    private final Optional<String> familyName;

    private final Optional<String> username;

}
