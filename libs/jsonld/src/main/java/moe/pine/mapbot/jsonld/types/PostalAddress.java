package moe.pine.mapbot.jsonld.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * The mailing address
 *
 * @see <a href="https://schema.org/PostalAddress">PostalAddress - schema.org Type</a>
 * @see <a href="https://tabelog.com/tokyo/A1304/A130401/13004352/">讃岐うどん大使 東京麺通団 （とうきょうめんつうだん） - 新宿西口/うどん [食べログ]</a>
 */
@Value
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("PostalAddress")
public class PostalAddress extends Thing {
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

    public String getDomesticAddress() {
        return StringUtils.join(addressRegion, addressLocality, streetAddress);
    }
}
