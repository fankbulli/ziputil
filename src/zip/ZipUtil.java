package zip;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Author: zh
 * Desc: 压缩包工具类
 * Date: Created in 2018/5/19 13:51
 */
public class ZipUtil {

    public static void main(String[] args) {
        List<File> srcFiles=new ArrayList<File>();
        String path="C:\\Users\\EDZ\\Desktop\\1\\";
        srcFiles.add(new File(path+"1.pdf"));
        srcFiles.add(new File(path+"2.pdf"));
        srcFiles.add(new File(path+"3.pdf"));
        srcFiles.add(new File(path+"4.pdf"));
        String zip="C:\\Users\\EDZ\\Desktop\\1\\zp.zip";


        toZip(srcFiles,new File(zip));
    }
    /**
     * 把文件集合打成zip压缩包
     * @param srcFiles 压缩文件集合
     * @param zipFile  zip文件名
     * @throws RuntimeException 异常
     */
    public static void toZip(List<File> srcFiles, File zipFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if(zipFile == null){
            return;
        }
        if(!zipFile.getName().endsWith(".zip")){
            return;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream out = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[4096];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.setComment("我是注释");
                zos.closeEntry();
                in.close();
                out.close();
            }
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
