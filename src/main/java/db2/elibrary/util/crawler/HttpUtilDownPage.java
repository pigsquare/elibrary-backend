package db2.elibrary.util.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class HttpUtilDownPage {
    private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    private static String sendGet(String url) {
        // js出错不抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // HTTP！=200不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(url);
        } catch (MalformedURLException e) {
            log.error("MalformedURLException");
        } catch (IOException e) {
            log.error("IOException");
        }

        // webClient.waitForBackgroundJavaScript(10000);视情况是否需要异步执行脚本
        if (htmlPage != null) {
            return htmlPage.asXml();
        }
        return null;

    }

    public void parseBookInfo(String isbn) throws XPatherException {
        String contents = HttpUtilDownPage.sendGet("https://book.douban.com/isbn/" + isbn);
        if (contents != null && !contents.isEmpty()) {
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            TagNode tagNode = htmlCleaner.clean(contents);
            String xpath1 = "/body/div[3]/h1/span";
            String xpath2 = "/body/div[3]/div[2]/div/div[1]/div[1]/div[1]/div[1]/div[2]/span[1]/a";
            String xpath3 = "/body/div[3]/div[2]/div/div[1]/div[1]/div[1]/div[1]/div[2][text()[1]]";
            String xpath4 = "/body/div[3]/div[2]/div/div[1]/div[3]/div[1]/div[1]/div/p";
            String xpath5 = "/body/div[3]/div[2]/div/div[1]/div[3]/div[7]/div/span/a";
            // 获取书名
            var objArr = tagNode.evaluateXPath(xpath1);
            if (objArr != null && objArr.length > 0) {
                for (Object obj : objArr) {
                    TagNode tagNode1 = (TagNode) obj;
                    String bookName = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("book name: " + bookName);
                }
            }
            // 获取作者
            objArr = tagNode.evaluateXPath(xpath2);
            if (objArr != null && objArr.length > 0) {
                for (Object obj : objArr) {
                    TagNode tagNode1 = (TagNode) obj;
                    String author = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("author: " + author);
                }
            }
            // 获取出版社、出版日期、定价
            objArr = tagNode.evaluateXPath(xpath3);
            if (objArr != null && objArr.length > 0) {
                for (Object obj : objArr) {
                    TagNode tagNode1 = (TagNode) obj;
                    String rawStr = removeWhiteLabels(tagNode1.getText().toString());
                    String pattern = "出版社:\\s*(\\S+).*出版年:\\s*(\\S+).*定价:\\s*(\\S+)(\\s*(\\S+))";
                    Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
                    Matcher m = r.matcher(rawStr);
                    if (m.find()) {
                        String publisher = m.group(1);
                        log.info("publisher: " + publisher);
                        String publish_date = m.group(2);
                        log.info("publish_date: " + publish_date);
                        String priceStr = m.group(3) + m.group(4);
                        // FIXED: 金额格式混乱，有纯数字、CNY 99、￥21.9、29元 等格式
                        String priceRgx = "\\d+(?:\\.\\d+)?";
                        Matcher priceMatcher = Pattern.compile(priceRgx, Pattern.MULTILINE).matcher(priceStr);
                        if(priceMatcher.find()){
                            double price = Double.parseDouble(priceMatcher.group());
                            log.info("price: " + price);
                        }
//                        if (priceStr.contains("元"))
//                            priceStr = priceStr.substring(0, priceStr.length() - 1);
//                        double price = Double.parseDouble(priceStr);
//                        log.info("price: " + price);
                    }
                }
            }
            // 获取描述
            objArr = tagNode.evaluateXPath(xpath4);
            if (objArr != null && objArr.length > 0) {
                for (Object obj : objArr) {
                    TagNode tagNode1 = (TagNode) obj;
                    String bookDescription = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("book description: " + bookDescription);
                }
            }
            // 获取关键词
            objArr = tagNode.evaluateXPath(xpath5);
            if (objArr != null && objArr.length > 0) {
                for (Object obj : objArr) {
                    TagNode tagNode1 = (TagNode) obj;
                    String keywords = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("keywords: " + keywords);
                }
            }


        }

    }

    private String removeWhiteLabels(String s) {
        int l = s.length();
        int begin = 0;
        int end = l - 1;
        for (int i = 0; i < l; i++) {
            if (s.charAt(i) != '\n' && s.charAt(i) != '\r'
                    && s.charAt(i) != '\t' && s.charAt(i) != ' ') {
                begin = i;
                break;
            }
        }
        for (int i = l - 1; i >= 0; i--) {
            if (s.charAt(i) != '\n' && s.charAt(i) != '\r'
                    && s.charAt(i) != '\t' && s.charAt(i) != ' ') {
                end = i;
                break;
            }
        }
        if (begin > end) return "";
        return s.substring(begin, end + 1);
    }

    private String addInfo(String s, String info) {
        if (s.length() == 0) return info;
        return s + " " + info;
    }
}
