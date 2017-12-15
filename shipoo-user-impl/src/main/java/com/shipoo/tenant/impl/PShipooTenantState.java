package com.shipoo.tenant.impl;


import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity.CommandContext;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity.Persist;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import org.pcollections.PMap;

import java.util.Optional;
import java.util.UUID;

import static com.shipoo.tenant.impl.PShipooTenantCommand.PutTenantMember;
import static com.shipoo.tenant.impl.PShipooTenantEvent.TenantMemberPutted;
import static com.shipoo.tenant.impl.ShipooTenantException.USER_CANT_PUT_MEMBER;
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
     * Verify command and persist the event the TenantMenber when the command is ok
     */
    public Persist acceptPutTenantMenberEvent(PutTenantMember cmd,
                                              PShipooTenantEntity.CommandContext<ShipooTenantMember> ctx) {

        ShipooTenantMember commander = members.get(cmd.getCommander());
        if (commander != null || commander.getRole() != MANAGER || commander.getRole() != CREATOR) {
            ctx.commandFailed(USER_CANT_PUT_MEMBER);
            return ctx.done();
        }

        ShipooTenantMember member = new ShipooTenantMember(cmd.getMember(), cmd.getRole());

        ShipooTenantMember eventualPreset = getMembers().get(member.getUser());

        if (eventualPreset != null && eventualPreset.equals(member)) {
            // when the putted and current are equal there's no need to emit an event.
            return ctx.done();
        }

        TenantMemberPutted event = new TenantMemberPutted(commander.getUser(), member);

        return ctx.thenPersist(event, e -> ctx.reply(member));
    }
}
