package api.allegro.converter;

import javafx.util.StringConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveHtmlTagsConverter extends StringConverter<String> {
    private final static Pattern STRIP_TAGS = Pattern.compile("<[^<]+?>");
    //private final static Pattern ONLY_ONE_SPACE = Pattern.compile("\\\\s+");

    @Override
    public String toString(String s) {
        return s;
    }

    @Override
    public String fromString(String s) {
        Matcher m = STRIP_TAGS.matcher(s);
        return m.replaceAll("");
    }
}
