package zip;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class LinuxTarUtil {

    public static void main(String[] args) {

        String src ="C:\\Users\\EDZ\\Desktop\\1\\";
        File file=new File(src);
        File[] files=file.listFiles();

        System.out.println(file.length());
//        runLinux("flume",".log","");
    }

    //第一个参数是文件名，第二个参数是文件名后缀（填 ".pdf"）,第三个是linux的pdf的文件地址（如果是当前位置生成可不填）
    public static String runLinux(String name,String sourceFix,String path){
        String line="";
        try {

            String tar=name+".tar";
            String file=name+sourceFix;
            if (null!=path&&!"".equals(path)){
                //注意path要到/
                tar=path+tar;
                file=path+file;
            }
            String cmd="tar -cvf "+tar+" "+file;
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (java.io.IOException e) {
            System.err.println("IOException " + e.getMessage());
        }
        return line;
    }

}
