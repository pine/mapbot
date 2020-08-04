package moe.pine.mapbot.services.message;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
class UriExtractor {
    /**
     * @see <a href="https://urlregex.com/">The Perfect URL Regular Expression - Perfect URL Regex</a>
     */
    private final Pattern PATTERN =
            Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_]");

    public List<String> extract(String text) {
        List<String> urls = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(text);

        int start = 0;
        while (matcher.find(start)) {
            urls.add(matcher.group());
            start = matcher.end();
        }

        return urls.stream()
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }
}
