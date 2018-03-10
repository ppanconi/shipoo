package com.shipoo.tenant.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Value;
import org.pcollections.PVector;

import java.util.UUID;

@Value
public class ShipooTenant {

    UUID id;
    UUID creator;
    String name;
    PVector<ShipooTenantMember> memebres;

    @JsonCreator
    @Builder
    public ShipooTenant(UUID id, UUID creator, UUID member, String name, PVector<ShipooTenantMember> memebres) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.memebres = memebres;
    }

}
