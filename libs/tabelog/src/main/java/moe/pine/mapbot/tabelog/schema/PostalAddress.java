package moe.pine.mapbot.tabelog.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * The mailing address
 *
 * @see <a href="https://schema.org/PostalAddress">PostalAddress - schema.org Type</a>
 */
@Value
public class PostalAddress {
    @JsonProperty("@type")
    String type;

    @Nullable
    String addressCountry;

    @Nullable
    String addressLocality;

    @Nullable
    String addressRegion;

    @Nullable
    String postalCode;

    @Nullable
    String streetAddress;

    public String getDomesticAddress() {
        return StringUtils.defaultString(addressRegion) +
                StringUtils.defaultString(addressLocality) +
                StringUtils.defaultString(streetAddress);
    }
}
