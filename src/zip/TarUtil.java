package zip;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TarUtil {

    public static void main(String[] args) throws IOException {
        String src="C:\\Users\\EDZ\\Desktop\\1\\";
        String tarPath="C:\\Users\\EDZ\\Desktop\\2\\";
        String[] loanNos={"3","4"};
        File dic=new File(src);
        File[] files=dic.listFiles();
        for (String dateo:loanNos){
            for (File file:files){
                if (file.getName().contains(dateo)){
                    System.out.println(file.getAbsolutePath());
                    String tarFileName="JSB"+dateo+".tar";
                    if (!TargzFilesNew(file.getAbsolutePath(),tarPath+tarFileName)){
                        return ;
                    }
                    file.delete();
                    break;
                }
            }
        }

    }

    public static boolean TargzFilesNew(String src,String desc)throws IOException{
        File inputFile=new File(src);

        if (null!=inputFile&&inputFile.isFile()){
            TarOutputStream out =null;
            try {
                out=new TarOutputStream(new FileOutputStream(new File(desc)));
                packFile(out,inputFile,inputFile.getName());
            } catch (IOException e) {
                return false;
            }finally {
                if (out!=null){
                    out.close();
                }
            }
        }
        return true;
    }
    public static void packFile(TarOutputStream out, File inputFile,String base) throws IOException{
        TarEntry tarEntry=new TarEntry(base);
        tarEntry.setSize(inputFile.length());
        FileInputStream in=null;
        byte[] B_ARRAY=new byte[4096];
        try {
            out.putNextEntry(tarEntry);
            in=new FileInputStream(inputFile);
            int b=0;
            while ((b=in.read(B_ARRAY,0,4096))!=-1){
                out.write(B_ARRAY,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (null!=in){
                    in.close();
                }
                if (null!=out){
                    out.closeEntry();
                }
            }catch (IOException e){

            }
        }

    }
}
