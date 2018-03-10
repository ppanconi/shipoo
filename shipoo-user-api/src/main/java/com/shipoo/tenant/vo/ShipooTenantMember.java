package com.shipoo.tenant.vo;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenantMember {

    UUID ud;
    ShipooTenantMemberRole role;

    @JsonCreator
    @Builder
    public ShipooTenantMember(UUID ud, ShipooTenantMemberRole role) {
        this.ud = ud;
        this.role = role;
    }
}
