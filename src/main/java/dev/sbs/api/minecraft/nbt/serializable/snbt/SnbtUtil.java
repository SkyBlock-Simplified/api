package dev.sbs.api.minecraft.nbt.serializable.snbt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SnbtUtil {

    public static @NotNull String escape(@NotNull String str) {
        StringBuilder sb = new StringBuilder(" ");
        char quote = 0;

        for (int i = 0; i < str.length(); ++i) {
            char current = str.charAt(i);

            if (current == '\\')
                sb.append('\\');
            else if (current == '"' || current == '\'') {
                if (quote == 0)
                    quote = current == '"' ? '\'' : '"';

                if (quote == current)
                    sb.append('\\');
            }

            sb.append(current);
        }

        if (quote == 0)
            quote = '"';

        sb.setCharAt(0, quote);
        sb.append(quote);

        return sb.toString();
    }

    public static @NotNull String multiplyIndent(int by, @NotNull SnbtConfig config) {
        return new String(new char[by * config.getIndentSpaces()]).replace("\0", " ");
    }

    public static @NotNull String[] getMatches(@NotNull Pattern regex, @NotNull String in) {
        List<String> matches = new ArrayList<>();
        Matcher matcher = regex.matcher(in);

        while (matcher.find())
            matches.add(matcher.group());

        return matches.toArray(new String[0]);
    }

}
