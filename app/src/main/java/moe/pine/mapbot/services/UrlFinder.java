package moe.pine.mapbot.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlFinder {
    /**
     * @see <a href="https://urlregex.com/">The Perfect URL Regular Expression - Perfect URL Regex</a>
     */
    private final Pattern PATTERN =
        Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    public List<String> find(String text) {
        ArrayList<String> urls = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(text);

        int start = 0;
        while (matcher.find(start)) {
            urls.add(matcher.group());
            start = matcher.end();
        }

        return urls;
    }
}
