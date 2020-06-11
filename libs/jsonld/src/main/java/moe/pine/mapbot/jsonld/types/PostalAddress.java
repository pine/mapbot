package moe.pine.mapbot.jsonld.types;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

/**
 * The mailing address
 *
 * @see <a href="https://schema.org/PostalAddress">PostalAddress - schema.org Type</a>
 * @see <a href="https://tabelog.com/tokyo/A1304/A130401/13004352/">讃岐うどん大使 東京麺通団 （とうきょうめんつうだん） - 新宿西口/うどん [食べログ]</a>
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostalAddress extends Thing {
    public static final String TYPE = "PostalAddress";
    public static final String ADDRESS_LOCALITY_ATTR = "addressLocality";
    public static final String ADDRESS_REGION_ATTR = "addressRegion";
    public static final String STREET_ADDRESS_ATTR = "streetAddress";

    String addressLocality;

    /**
     * The region in which the {@link #addressLocality} is.
     * <p>
     * E.g. 東京都
     */
    String addressRegion;

    /**
     * The street address.
     * <p>
     * E.g. 西新宿7-9-15 ダイカンプラザ　ビジネス清田ビル１Ｆ
     */
    String streetAddress;

    public PostalAddress(
            String context,
            String id,
            String addressLocality,
            String addressRegion,
            String streetAddress
    ) {
        super(context, id, TYPE);

        this.addressLocality = addressLocality;
        this.addressRegion = addressRegion;
        this.streetAddress = streetAddress;
    }

    public String getDomesticAddress() {
        return StringUtils.join(addressRegion, addressLocality, streetAddress);
    }
}
