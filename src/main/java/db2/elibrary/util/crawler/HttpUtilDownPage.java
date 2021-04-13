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

@Slf4j
@Service
public class HttpUtilDownPage {
    private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

    private static String sendGet(String url){
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
            String xpath3 = "/body/div[3]/div[2]/div/div[1]/div[1]/div[1]/div[1]/div[2][text()[1]]";
            String xpath4 = "/body/div[3]/div[2]/div/div[1]/div[3]/div[1]/div[1]/div/p";
            String xpath5 = "/body/div[3]/div[2]/div/div[1]/div[3]/div[7]/div/span";
            var objArr = tagNode.evaluateXPath(xpath1);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String bookName = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("book name: "+bookName);
                }
            }
            objArr = tagNode.evaluateXPath(xpath2);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String author = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("author: "+author);
                }
            }
            objArr = tagNode.evaluateXPath(xpath3);
            if(objArr!=null&&objArr.length>0){
                // TODO: 获取出版商信息、出版日期、定价
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String publisher = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("publisher: "+publisher);
                }
            }
            objArr = tagNode.evaluateXPath(xpath4);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String bookDescription = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("book description: "+bookDescription);
                }
            }
            objArr = tagNode.evaluateXPath(xpath5);
            if(objArr!=null&&objArr.length>0){
                for (Object obj: objArr){
                    TagNode tagNode1 = (TagNode)obj;
                    String keywords = removeWhiteLabels(tagNode1.getText().toString());
                    log.info("keywords: "+keywords);
                }
            }


        }

    }

    private String removeWhiteLabels(String s){
        int l = s.length();
        int begin = 0;
        int end = l-1;
        for(int i=0;i<l;i++){
            if(s.charAt(i)!='\n'&&s.charAt(i)!='\r'
                    &&s.charAt(i)!='\t'&&s.charAt(i)!=' '){
                begin=i;
                break;
            }
        }
        for(int i=l-1;i>=0;i--){
            if(s.charAt(i)!='\n'&&s.charAt(i)!='\r'
                    &&s.charAt(i)!='\t'&&s.charAt(i)!=' '){
                end=i;
                break;
            }
        }
        if(begin>end)return "";
        return s.substring(begin,end+1);
    }

    private String addInfo(String s, String info){
        if(s.length()==0)return info;
        return s+" "+info;
    }
}
