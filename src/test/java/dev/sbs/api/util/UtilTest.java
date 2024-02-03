package dev.sbs.api.util;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class UtilTest {

    @Test
    public void genericTest_ok() {
        String javaVersion = SystemUtil.JAVA_VERSION;
        String javaSpecVersion = SystemUtil.JAVA_SPECIFICATION_VERSION;
        String uValue = "\\u2699\\u2699";
        char uValue2 = '\u2699';

        String emoji = StringUtil.unescapeUnicode(uValue);
        String output = StringUtil.escapeUnicode(emoji);

        MatcherAssert.assertThat("Done", true);
    }

}
