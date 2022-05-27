import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

///答案页 抽取题目和选项
public class Parsetopic {


    public static void main(String[] args) {
        try {
            int i=0;
            OkkHttpTry os=new OkkHttpTry();
            Document document= Jsoup.parse(new File("src/main/resources/整卷预览.html"),"UTF-8");
            for (Element e : document.body().getElementsByClass("questionLi")) {

                if (new Scanner(System.in).hasNextLine()) {
                    System.out.println("+++++++++++++++++++++++++");
                    System.out.println(++i + ". " + e.getElementsByTag("h3").text()
                            .substring(e.getElementsByTag("h3").
                                    text().
                                    indexOf(")") + 1));
                    os.start(e.getElementsByTag("h3").text()
                            .substring(e.getElementsByTag("h3").
                                    text().
                                    indexOf(")") + 1));
                    System.out.println(e.getElementsByClass("stem_answer").text());
                    System.out.println("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
