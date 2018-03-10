package com.shipoo.security;


import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
public class ShipooTokenUser {

    @Builder
    public ShipooTokenUser(String id, String email, Optional<String> firstName, Optional<String> familyName,
                           Optional<String> displayName, Optional<String> username, Optional<String> gender,
                           Optional<String> locale, Optional<String> pictureUrl, Optional<String> profileUrl,
                           Optional<String> location) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.familyName = familyName;
        this.displayName = displayName;
        this.username = username;
        this.gender = gender;
        this.locale = locale;
        this.pictureUrl = pictureUrl;
        this.profileUrl = profileUrl;
        this.location = location;
    }

    @Builder
    public ShipooTokenUser(String id, String email) {
        this(id, email, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    String id;

    String email;

    Optional<String> firstName;

    Optional<String> familyName;

    Optional<String> displayName;

    Optional<String> username;

    Optional<String> gender;

    Optional<String> locale;

    Optional<String> pictureUrl;

    Optional<String> profileUrl;

    Optional<String> location;

}
