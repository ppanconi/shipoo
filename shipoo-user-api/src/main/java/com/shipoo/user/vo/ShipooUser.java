package com.shipoo.user.vo;


import lombok.Value;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Value
public final class ShipooUser {

    private final UUID id;

    private final String email;

    private final Optional<String> firstName;

    private final Optional<String> familyName;

    private final Optional<String> username;

}
