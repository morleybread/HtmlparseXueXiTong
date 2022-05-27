import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import com.google.protobuf.util.JsonFormat;
import okhttp3.*;

import java.awt.List;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


//请求体 分为 辅字段1 + 自定义参数 +辅字段2 + 辅助字段3   //必须按照这个格式 服务器才接收请求
public class OkkHttpTry {


    private final byte[] fu1 =new byte[]{0x08, 0x03, 0x12, 0x06};
    private final byte[] fu2 =new byte[]{0x1A, 0x00, 0x20, 0x00, 0x28, 0x00,0x30, 0x01, 0x4A ,0x1E };


//    private final ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//         .tlsVersions(TlsVersion.TLS_1_2)
//         .build();
   private final OkHttpClient client =new OkHttpClient();
//           .newBuilder()
//            .connectionSpecs(Collections.singletonList(spec))
//            .build();



private String[] stringtoheader(String ps) throws FileNotFoundException {//把头文件键 值 键 值 的字符数组
    File f=new File(ps);
    Scanner scanner=new Scanner(f);
    ArrayList<String> stes=new ArrayList<>();
    while (scanner.hasNextLine()){
        String[] s= scanner.nextLine().split(": ");
        stes.addAll(Arrays.asList(s));
    }
//    String [] sts=stes.toArray(new String[0]);
  return stes.toArray(new String[0]);

}

@Deprecated
public  Map<String,String> jsonstringtoamp(String ps) throws FileNotFoundException { //文件路径
    Scanner scanner=new Scanner(new File(ps));
    StringBuilder jsonstring= new StringBuilder();
    while (scanner.hasNextLine()){
        jsonstring.append(scanner.nextLine());
    }
    Gson gson=new Gson();
    Type stringtype=new TypeToken<Map<String,Object>>(){}.getType();
    return gson.fromJson(jsonstring.toString(),stringtype);
}


public LinkedList<Byte> bytescombin(byte[]...bs ){ //合并多个byte数组
    LinkedList<Byte> bytes=new LinkedList<>();
    for (byte[] b:bs){
       for (byte i:b){
           bytes.add(i);
       }
    }
    return  bytes;
}



public String parserespose(byte[] bs) throws IOException { //解析返回体 返回字符串
    Xuexiaoyi.RespOfSearch xue=Xuexiaoyi.RespOfSearch.parseFrom(bs);
    return JsonFormat.printer().print(xue);
//    return TextFormat.printer().escapingNonAscii(false).printToString(xue);
}


private  byte[] setbinpostbody(String ps) throws IOException { //传入自定义字段  进行 辅字段1 + 自定义参数 +辅字段2 + 辅助字段3 二进制请求体
//    byte[] fu3 =  getFu3().getBytes();
    byte[] fu3="1249509491345470-1653555546374".getBytes();
    ArrayList<Integer> asp=stringtobinsparameter(ps);
      byte[] bis = new byte[asp.size()]; //自定意参数
      int i=0,j=0;
      for (int bs:asp) bis[i++] = (byte) bs;
      LinkedList<Byte> ints=bytescombin(fu1,bis,fu2,fu3);
      byte[] bys=new byte[ints.size()];
      for (int s:ints) bys[j++] = (byte) s;
      return  bys;
     // fu1+bis+fu2+fu3

}


    public String run(String s) throws Exception {//s为请求参数 //run为主方法
     byte[] postbody=setbinpostbody(s);
//        byte[] postbody=binfiletobyte("src/main/resources/bin");
       Headers.Builder builder=new Headers.Builder();
       Headers headers= builder.addAll(Headers.of(stringtoheader("src/main/resources/request.txt"))).build();//生成请求头
       Request request = new Request.Builder() //开始请求
               .url("https://xxy.51xuexiaoyi.com/el/v0/sou/search?iid=3206614682965950&device_id=655737355241264&ac=wifi&channel=xiaomi_199563&aid=199563&app_name=xxy&version_code=10300&version_name=1.3.0&device_platform=android&os=android&ssmix=a&device_type=M2004J7AC&device_brand=Redmi&language=zh&os_api=29&os_version=10&openudid=dafd35b356fd35c0&manifest_version_code=10300&resolution=1080*2201&dpi=440&update_version_code=1030004&_rticket=1653555665504&cdid=e24fad64-23b7-41f7-a884-577c714f8778&uuid=655737355241264&oaid=476f5f89aa723343&el_app_version=10300")
               .headers(headers)
               .post(RequestBody.create(postbody))
               .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.headers());
            return  parserespose(Objects.requireNonNull(response.body()).bytes());

        }

}


    public  static byte[] binfiletobyte(String fp) throws IOException {//把二进制文件转 byte数组
    FileInputStream fi=new FileInputStream(new File(fp));
    ArrayList<Integer> ints=new ArrayList<>();
    int value;
    while ((value=fi.read())!=-1){
        ints.add(value);
    }
        byte[] bys=new byte[ints.size()];
        int i=0;
        for (int s:ints) bys[i++] = (byte) s;
   return  bys;
    }

/*
    private void parsebin() throws IOException {//生成请求体辅字段 f1 和 f2
        FileInputStream fileInputStream=new FileInputStream(new File("src/main/resources/request_body.bin"));
    int value;
    ArrayList<Integer> bi=new ArrayList<>();//bi存储 二进制 数
    while ((value=fileInputStream.read())!=-1){
        bi.add(value);
    }

    for(int i=0;i<4;i++){
        fu1.add(bi.get(i));
    }
    for (int j=10;j<bi.size();j++){
        fu2.add(bi.get(j));
    }
    }
*/

public  void write(byte [] bb) throws IOException {
    FileOutputStream fileOutputStream=new FileOutputStream(new File("src/main/resources/Try"));
    fileOutputStream.write(bb);
    fileOutputStream.close();
}

 private ArrayList<Integer> stringtobinsparameter(String s){
         ArrayList<Integer> ints=new ArrayList<>();
    byte[] bt=s.getBytes();
        for (byte b : bt) {
            String temp = Integer.toHexString(b);
            if(temp.length()<=2){
                ints.add(Integer.valueOf(temp,16));
                continue;
            }
            String ss=temp.substring(6).toUpperCase(Locale.ROOT);
            ints.add(Integer.valueOf(ss,16));
        }
    return ints;
    }

@Deprecated
     public  byte[] getparameterfromproto(String para){
         Xuexiaoyi.RespOfReportUserInfo xue=Xuexiaoyi.RespOfReportUserInfo.newBuilder()
                 .setErrNo(3)
                 .setErrTips(para).
                 build();
         return xue.toByteArray();
     }


private String getFu3(){
    long id= (int) (Math.random() * 50) + 1650940846249L;
    return  "1249509491345470-"+id;
}

public  String getjsonparse(String repath) throws FileNotFoundException {
    File f=new File(repath);
    Scanner scanner=new Scanner(f);
    StringBuilder s= new StringBuilder();
    while (scanner.hasNextLine()){
        s.append(scanner.nextLine());
    }
    return s.toString();
}
private String getresultstring(String respoString){
    JsonObject je= JsonParser.parseString(respoString).getAsJsonObject().getAsJsonObject("result");
    StringBuilder s= new StringBuilder();
    try {
        for (JsonElement j : je.getAsJsonArray("items")) {
            s.append(j.getAsJsonObject().getAsJsonObject("questionAnswer").getAsJsonPrimitive("answerPlainText"));
            s.append("\n");
        }
    }catch (NullPointerException e){
        System.out.println(respoString);
    }
    return s.toString();
}
    public  void start(String questionandoption) throws Exception {
        System.out.println(getresultstring(run(questionandoption)));

    }


    public static void main(String[] args) throws Exception {
//        System.setProperty("http.proxyHost","127.0.0.1");
//        System.setProperty("https.proxyHost","127.0.0.1");
//        System.setProperty("http.proxyPort","8888");
//        System.setProperty("https.proxyPort","8888");
        OkkHttpTry okkHttpTry=new OkkHttpTry();
        System.out.println(okkHttpTry.run("孟子"));
//        okkHttpTry.write(okkHttpTry.setbinpostbody("孔子"));
//        System.out.println(okkHttpTry.parserespose(OkkHttpTry.binfiletobyte("src/main/resources/bin")));
  //      okkHttpTry.write( okkHttpTry.getparameterfromproto(""));
     //   System.out.println(okkHttpTry.bytescombin(new byte[]{1,2,3,},new byte[]{1,2,3,4},new byte[]{1,2,3,9}));

    }
}
