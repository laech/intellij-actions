package com.gitlab.lae.intellij.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_NEXT_WORD;

public final class CapitalizeRegionOrToWordEnd extends TextAction {

    private static final Edit capitalizeRegion =
            Edit.replacingSelection(
                    CapitalizeRegionOrToWordEnd::capitalize);

    private static final Edit capitalizeToWordEnd =
            Edit.replacingFromCaret(
                    ACTION_EDITOR_NEXT_WORD,
                    CapitalizeRegionOrToWordEnd::capitalize
            );

    private static final Pattern wordPattern =
            Pattern.compile("\\w+");

    private static String capitalize(String str) {
        Matcher matcher = wordPattern.matcher(str);
        StringBuffer buffer = null;
        while (matcher.find()) {
            if (buffer == null) {
                buffer = new StringBuffer(str.length());
            }
            String lower = matcher.group().toLowerCase();
            matcher.appendReplacement(
                    buffer,
                    lower.substring(0, 1).toUpperCase() +
                            lower.substring(1)
            );
        }
        return buffer == null
                ? str
                : matcher.appendTail(buffer).toString();
    }

    public CapitalizeRegionOrToWordEnd() {
        super(true, capitalizeRegion.ifNoSelection(capitalizeToWordEnd));
    }
}
