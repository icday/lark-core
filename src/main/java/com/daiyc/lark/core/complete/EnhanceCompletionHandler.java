package com.daiyc.lark.core.complete;

import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import jline.console.completer.CandidateListCompletionHandler;
import jline.internal.Ansi;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author daiyc
 */
public class EnhanceCompletionHandler extends CandidateListCompletionHandler {
    public static final String PART_CANDIDATE_POSTFIX = ":x-x";

    @Override
    public boolean complete(final ConsoleReader reader, final List<CharSequence> candidates, final int pos) throws
            IOException {
        if (candidates.size() != 1) {
            return super.complete(reader, candidates, pos);
        }

        CursorBuffer buf = reader.getCursorBuffer();

        CharSequence value = candidates.get(0);

        if (StringUtils.endsWith(value, PART_CANDIDATE_POSTFIX)) {
            String str = Ansi.stripAnsi(StringUtils.stripEnd(value.toString(), PART_CANDIDATE_POSTFIX));
            if (str.equals(buf.toString())) {
                return false;
            }

            setBuffer(reader, str, pos);

            return true;
        } else {
            return super.complete(reader, candidates, pos);
        }
    }
}
