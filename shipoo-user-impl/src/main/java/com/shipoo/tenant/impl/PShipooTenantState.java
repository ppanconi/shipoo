package com.shipoo.tenant.impl;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity.Persist;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;
import org.pcollections.PMap;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.shipoo.tenant.impl.PShipooTenantCommand.PutTenantMember;
import static com.shipoo.tenant.impl.PShipooTenantEvent.TenantMemberPutted;
import static com.shipoo.tenant.impl.ShipooTenantException.USER_CANT_PUT_MEMBER;
import static com.shipoo.tenant.impl.ShipooTenantException.USER_CANT_REMOVE_MEMBER;
import static com.shipoo.tenant.impl.ShipooTenantRole.CREATOR;
import static com.shipoo.tenant.impl.ShipooTenantRole.MANAGER;

@Value
public class PShipooTenantState implements Jsonable {

    @JsonCreator
    @Builder(toBuilder = true)
    public PShipooTenantState(UUID id, UUID creator, ShipooTenantUserData tenantData, PMap<UUID, ShipooTenantMember> members) {
        this.id = id;
        this.creator = creator;
        this.tenantData = tenantData;
        this.members = members;
    }

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
    private final ShipooTenantUserData tenantData;

    /**
     * The collection of members of this tenant
     */
    private final PMap<UUID, ShipooTenantMember> members;


    /**
     * Verify command and persist the event the TenantMenber when the command is ok
     */
    public Persist acceptPutTenantMemberCommand(PutTenantMember cmd,
                                                PShipooTenantEntity.CommandContext<Optional<CommandReply>> ctx) {

        ShipooTenantMember commander = members.get(cmd.getCommander());
        if (!verifyCommander(commander)) {
            ctx.commandFailed(USER_CANT_PUT_MEMBER);
            return ctx.done();
        }

        ShipooTenantMember member = new ShipooTenantMember(cmd.getMember(), cmd.getRole());

        ShipooTenantMember eventualPreset = getMembers().get(member.getUser());

        if (eventualPreset != null && eventualPreset.equals(member)) {
            // when the putted and current are equal there's no need to emit an event.
            // TODO use the Replay with the right code
            return ctx.done();
        }

        TenantMemberPutted event = TenantMemberPutted.builder()
                .id(UUID.randomUUID())
                .timestamp(Instant.now())
                .commander(commander.getUser())
                .memberPutted(member)
                .build();

        return ctx.thenPersist(event, e -> ctx.reply(
                Optional.of(CommandReply.Done.builder()
                        .id(event.getId())
                        .timestamp(event.getTimestamp())
                        .build())
        ));
    }

    /**
     * Applay event to add member
     * @param event
     * @return
     */
    public PShipooTenantState applyTenantMemberPuttedEvent(TenantMemberPutted event) {
        return this.toBuilder().members(members.plus(event.getMemberPutted().getUser(), event.getMemberPutted())).build();
    }


    public Persist acceptRemoveTenantMemberCommand(PShipooTenantCommand.RemoveTenantMember cmd,
                                                   PShipooTenantEntity.CommandContext<Optional<CommandReply>>  ctx) {


        ShipooTenantMember commander = members.get(cmd.getCommander());

        if ( !verifyCommander(commander) && cmd.getCommander() != cmd.getMember() ) {
            ctx.commandFailed(USER_CANT_REMOVE_MEMBER);
            return ctx.done();
        }

        ShipooTenantMember member = members.get(cmd.getMember());
        if (member == null) {
            ctx.reply(
                    Optional.of(CommandReply.NowDone.builder().build())
            );
            return ctx.done();
        }

        PShipooTenantEvent.TenantMemberRemoved event = PShipooTenantEvent.TenantMemberRemoved.builder()
                .id(UUID.randomUUID())
                .timestamp(Instant.now())
                .commander(commander.getUser())
                .memberRemoved(member)
                .build();

        return ctx.thenPersist(event, e -> ctx.reply(
                Optional.of(CommandReply.Done.builder()
                        .id(event.getId())
                        .timestamp(event.getTimestamp())
                        .build())
        ));
    }

    public PShipooTenantState applyTenantMemberRemovedEvent(PShipooTenantEvent.TenantMemberRemoved event) {
        return this.toBuilder().members(members.minus(event.getMemberRemoved().getUser())).build();
    }


    private boolean verifyCommander(ShipooTenantMember commander) {
        if (commander == null || (commander.getRole() != MANAGER && commander.getRole() != CREATOR )) {
            return false;
        }
        return true;
    }
}
