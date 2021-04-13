package db2.elibrary.util.crawler;

import org.htmlcleaner.XPatherException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilDownPageTest {
    @Test
    public void test() throws XPatherException {
        HttpUtilDownPage httpUtilDownPage = new HttpUtilDownPage();
        httpUtilDownPage.parseBookInfo("9787208167490");
    }
}
