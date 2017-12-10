package com.shipoo.user.impl;

import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@Value
public class PShipooUser implements Jsonable {

    private final UUID id;

    private final String email;

    private final Optional<String> firstName;

    private final Optional<String> familyName;

    private final Optional<String> username;

}
