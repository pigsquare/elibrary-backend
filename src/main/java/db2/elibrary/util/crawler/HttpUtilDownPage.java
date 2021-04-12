package db2.elibrary.util.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@Service
public class HttpUtilDownPage {
    private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    public static String sendGet(String url){
        // js出错不抛出异常
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // HTTP！=200不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage htmlPage = null;
        try{
            htmlPage=webClient.getPage(url);
        } catch (MalformedURLException e) {
            log.error("MalformedURLException");
        } catch (IOException e) {
            log.error("IOException");
        }

        // webClient.waitForBackgroundJavaScript(10000);视情况是否需要异步执行脚本
        if (htmlPage!=null){
            return htmlPage.asXml();
        }
        return null;

    }

    public void parseBookInfo(String isbn) throws XPatherException {
        String contents = HttpUtilDownPage.sendGet("https://book.douban.com/isbn/"+isbn);
        if(contents!=null&&!contents.isEmpty()){
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            TagNode tagNode = htmlCleaner.clean(contents);
            String xpath1 = "/body/div[3]/h1/span";
            String xpath2 = "/body/div[3]/div[2]/div/div[1]/div[1]/div[1]/div[1]/div[2]/span[1]/a";
            var objArr = tagNode.evaluateXPath(xpath1);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String bookName = tagNode1.getText().toString();
                    log.info("book name: "+bookName);
                }
            }
            objArr = tagNode.evaluateXPath(xpath2);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String author = tagNode1.getText().toString();
                    log.info("author: "+author);
                }
            }

        }

    }
}
