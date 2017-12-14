package com.shipoo.tenant.impl;


import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity.*;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import org.pcollections.PMap;
import org.pcollections.PSequence;

import java.util.UUID;

import static com.shipoo.tenant.impl.ShipooTenantRole.CREATOR;
import static com.shipoo.tenant.impl.ShipooTenantRole.MANAGER;

@Value
public class PShipooTenantState implements Jsonable {

    /**
     * The tenant id.
     */
    private final UUID id;

    /**
     * The user that created the item.
     */
    private final UUID creator;

    /**
     * The tenant date that the user can provides at creation time
     * or during update
     */
    private final ShipooTenantUserData tenant;

    /**
     * The collection of members of this tenant
     */
    private final PMap<UUID, ShipooTenantMember> members;

    /**
     * Verify and persist when it's ok the PutTenantMenber
     */
    public Persist acceptPutTenantMenber(PShipooTenantCommand.PutTenantMember cmd,
                                         CommandContext<ShipooTenantMember> ctx) {

        ShipooTenantMember commander = members.get(cmd.getCommander());
        if (commander != null || commander.getRole() != MANAGER || commander.getRole() != CREATOR) {
            ctx
        }


        ShipooTenantMember member = new ShipooTenantMember(cmd.getMember(), cmd.getRole());


    }

}
