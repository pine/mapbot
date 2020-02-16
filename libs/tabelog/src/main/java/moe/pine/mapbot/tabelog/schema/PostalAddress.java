package moe.pine.mapbot.tabelog.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * The mailing address
 *
 * @see <a href="https://schema.org/PostalAddress">PostalAddress - schema.org Type</a>
 * @see <a href="https://tabelog.com/tokyo/A1304/A130401/13004352/">讃岐うどん大使 東京麺通団 （とうきょうめんつうだん） - 新宿西口/うどん [食べログ]</a>
 */
@Value
public class PostalAddress {
    @JsonProperty("@type")
    String type;

    @Nullable
    String addressCountry;

    @Nullable
    String addressLocality;

    /**
     * The region in which the {@link #addressLocality} is, and which is in the {@link #addressCountry}.
     *
     * E.g. 東京都
     */
    @Nullable
    String addressRegion;

    @Nullable
    String postalCode;

    /**
     * 	The street address.
     *
     * 	E.g. 西新宿7-9-15 ダイカンプラザ　ビジネス清田ビル１Ｆ
     */
    @Nullable
    String streetAddress;

    public String getDomesticAddress() {
        return StringUtils.join(addressRegion, addressLocality, streetAddress);
    }
}
