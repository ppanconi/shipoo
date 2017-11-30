package com.shipoo.ui.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.definition.CommonProfileDefinition;

import javax.annotation.Nullable;

@Value.Immutable
@ImmutableStyle
@JsonSerialize(as=ShipooUiUser.class)
@JsonDeserialize(as=ShipooUiUser.class)
public interface AbstractShipooUiUser extends Jsonable {

    public String id();

    public String typedId();

    @Nullable
    public String email();

    @Nullable
    public String firstName();

    /**
     * Return the family name of the user.
     *
     * @return the family name of the user
     */
    @Nullable
    public String familyName();

    /**
     * Return the displayed name of the user. It can be the username or the first and last names (separated by a space).
     *
     * @return the displayed name of the user
     */
    @Nullable
    public String displayName();

    /**
     * Return the username of the user. It can be a login or a specific username.
     *
     * @return the username of the user
     */
    @Nullable
    public String username();

    /**
     * Return the gender of the user.
     *
     * @return the gender of the user
     */
    @Nullable
    public String gender();

    /**
     * Return the locale of the user.
     *
     * @return the locale of the user
     */
    @Nullable
    public String locale();

    /**
     * Return the url of the picture of the user.
     *
     * @return the url of the picture of the user.
     */
    @Nullable
    public String pictureUrl();

    /**
     * Return the url of the profile of the user.
     *
     * @return the url of the profile of the user.
     */
    @Nullable
    public String profileUrl();

    /**
     * Return the location of the user.
     *
     * @return the location of the user
     */
    @Nullable
    public String location();

    default boolean isAnonymous() {
        return id().equals("anonymous");
    }

    default String asJason() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this);
        return json;
    }

    static AbstractShipooUiUser fromCommonProfile(CommonProfile commonProfile) {

        return ShipooUiUser.builder()
                .id(commonProfile.getId())
                .typedId(commonProfile.getTypedId())
                .displayName(commonProfile.getDisplayName())
                .email(commonProfile.getEmail())
                .familyName(commonProfile.getFamilyName())
                .firstName(commonProfile.getFirstName())
                .gender("" + commonProfile.getAttribute(CommonProfileDefinition.GENDER))
//                .locale("" + commonProfile.getLocale())
                .location(commonProfile.getLocation())
//                .pictureUrl("" + commonProfile.getPictureUrl())
//                .profileUrl("" + commonProfile.getProfileUrl())
                .build();
    }

}
