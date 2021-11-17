import org.junit.Test;
import util.StringUtil;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {
    @Test
    public void stringStandardTest() {
        String in = "  sendi   hello @target ";
        assertEquals("sendi hello @target", StringUtil.stringStandard(in));
    }

    @Test
    public void getPathFromCmdTest() {
        String in = "sendf F://text.txt @li";
        assertEquals("F://text.txt", StringUtil.getPathFromCmd(in));
    }

    @Test
    public void getNameFromPathTest() {
        String in = "F://text.txt";
        assertEquals("text.txt", StringUtil.getNameFromPath(in));
    }

    @Test
    public void generateSendFMsgTest() {
        String msg = "sendf F://text.txt @cheng";
        long   size = 1024;
        assertEquals("sendf text.txt @cheng -1024", StringUtil.generateSendFMsg(msg, size));
    }
}
