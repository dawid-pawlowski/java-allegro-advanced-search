package api.allegro.filter;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class DecimalFilter implements UnaryOperator<TextFormatter.Change> {
    private final static Pattern PATTERN = Pattern.compile("\\d+(\\.\\d{1,2})?");

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        return PATTERN.matcher(change.getText()).matches() ? change : null;
    }
}
