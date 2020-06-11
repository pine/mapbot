package moe.pine.mapbot.jsonld.types;


import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * A restaurant
 *
 * @see <a href="https://schema.org/Restaurant">Restaurant - schema.org Type</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant extends Thing {
    public static final String TYPE = "Restaurant";
    public static final String ADDRESS_ATTR = "address";
    public static final String NAME_ATTR = "name";

    String name;
    PostalAddress address;

    public Restaurant(
            String context,
            String id,
            String name,
            PostalAddress address
    ) {
        super(context, id, TYPE);

        this.name = name;
        this.address = address;
    }
}
