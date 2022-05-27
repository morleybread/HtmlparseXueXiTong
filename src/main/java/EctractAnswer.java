import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
public class EctractAnswer {

    //抽取答案

    private  Document document;

    public EctractAnswer(String s){
        try {
             document= Jsoup.parse(new File(s),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public Elements getTopic(){
//        return  document.body().getElementsByClass("mark_item");
//    }


    public void getTopicAndAnswer() {
        for (Element e : document.body().getElementsByClass("marBom60")) {
//     System.out.println(e.getElementsByTag("h3").text()+" "
//             +e.getElementsByTag("ul").text()+e.getElementsByClass("colorDeep marginRight40 fl").text());
            System.out.println(e.getElementsByTag("h3").text()+"\n"+" --答案为："+
                    getanswer(e.getElementsByTag("ul").text(),
                            e.getElementsByClass("colorDeep marginRight40 fl").text())+"\n");
        }

    }

    public StringBuilder getanswer(String list,String c){
        String lest=c.substring((c.lastIndexOf(":")+1));//答案 为字符串
        String newl=list.replace(". ",".");
        String[] df=newl.split("\\s+");   //以空格分割字符串

        StringBuilder stringBuilder=new StringBuilder();
        for (String s:df) {  //遍历选项
//            if (s.contains(lest)) {  //如果选项包含答案
//                stringBuilder.append(s);//则此选项为答案
//            }
            for(char ans:lest.toCharArray()){
                if(s.contains(ans+"")){
                    stringBuilder.append(s);
                    stringBuilder.append("   ");
                }
            }
        }
            return  stringBuilder;
    }


        public static void main (String[]args){
           EctractAnswer ectractAnswer=new EctractAnswer("src/main/resources/hi.html");
         ectractAnswer.getTopicAndAnswer();
//            String s="A. 接纳改变 B. 和情绪共处 C. 思维策略 D. 放松技术";
//            String c="我的答案:ABCD";
//            String lest=c.substring((c.lastIndexOf(":")+1))+"";
////            System.out.println(ectractAnswer.getanswer(s,o));
//            System.out.println(lest);


//            try {
//                Document  document= Jsoup.parse(new File("src/main/resources/hi.html"),"UTF-8");
//
//                for (Element e : document.body().getAllElements()){
//                    System.out.println("++++++++++");
//                    System.out.println(e.text());
//                    System.out.println("++++++++++");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }




        }
}
