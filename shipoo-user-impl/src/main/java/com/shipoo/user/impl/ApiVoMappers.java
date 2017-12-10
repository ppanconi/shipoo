package com.shipoo.user.impl;

import com.shipoo.user.vo.ShipooUser;

import java.util.Optional;

public class ApiVoMappers {

    public static ShipooUser toApi(PShipooUser user) {
        return new ShipooUser(user.getId(), user.getEmail(),
                user.getFirstName(), user.getFamilyName(), user.getUsername());

    }
}
