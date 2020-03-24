package zip;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtils {

    private ZipUtils() {
        throw new IllegalStateException("Utility class");
    }

    //DEMO 示例
    public static void main(String[] args) {
//        List<File> srcFiles=new ArrayList<>();
        String path="C:\\Users\\EDZ\\Desktop\\1\\";
//        File file=new File(path);
//        System.out.println(file.isDirectory());
////        try {
////            zipFiles(new File(path),new File("C:\\Users\\EDZ\\Desktop\\zip.zip"));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        CompressedFiles_Gzip(path,"C:\\Users\\EDZ\\Desktop\\2.tar");
//
        File file=new File(path);
        if (file.isDirectory()){
            for (File file1:file.listFiles()){
                try {
                    System.out.println(file1.getName());
                    String name=file1.getName().split("\\.")[0];
                    System.out.println(name);
                    tarCompression(file1.getAbsolutePath(),"C:\\Users\\EDZ\\Desktop\\"+name+".tar");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 将文件/目录进行压缩
     * @param sourceFile 原文件/目录
     * @param targetZipFile 压缩后目标文件
     * @throws IOException
     */
    public static void zipFiles(File sourceFile, File targetZipFile) throws IOException {
        ZipOutputStream outputStream = null;
        try {
            outputStream = new ZipOutputStream(new FileOutputStream(targetZipFile));
            addEntry("", sourceFile, outputStream);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            outputStream.close();
        }
    }

    /**
     * 将文件写入到zip文件中
     * @param source
     * @param outputstream
     * @throws IOException
     */
    private static void addEntry(String base, File source, ZipOutputStream outputstream)
            throws IOException {
        FileInputStream is = null;
        try {
            String entry = base + source.getName();
            if (source.isDirectory()) {
                for (File file : source.listFiles()) {
                    System.out.println(file.getName());
                    // 递归导入文件
                    addEntry(entry + File.separator, file, outputstream);
                }
            } else {

                is = FileUtils.openInputStream(source);
                if (is != null) {
                    outputstream.putNextEntry(new ZipEntry(entry));

                    int len = 0;
                    byte[] buffer = new byte[10 * 1024];
                    while ((len = is.read(buffer)) > 0) {
                        outputstream.write(buffer, 0, len);
                        outputstream.flush();
                    }
                    outputstream.closeEntry();
                }
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }
    /* tar打包压缩

     * @param filesPathArray 要压缩的文件的全路径(数组)

     * @param resultFilePath 压缩后的文件全文件名(.tar)

     * @throws Exception

     */

    public static boolean tarCompression(String filePath, String resultFilePath) throws Exception {

        System.out.println(" tarCompression -> Compression start!");

        FileOutputStream fos = null;

        TarArchiveOutputStream taos = null;

        try {

            fos = new FileOutputStream(new File(resultFilePath));
            taos = new TarArchiveOutputStream(fos);
            BufferedInputStream bis = null;
            FileInputStream fis = null;
            try {
                File file = new File(filePath);
                TarArchiveEntry tae = new TarArchiveEntry(file);
                // 此处指明 每一个被压缩文件的名字,以便于解压时TarArchiveEntry的getName()方法获取到的直接就是这里指定的文件名
                // 以(左边的)GBK编码将file.getName()“打碎”为序列,再“组装”序列为(右边的)GBK编码的字符串
                tae.setName(new String(file.getName().getBytes("GBK"), "GBK"));
                taos.putArchiveEntry(tae);
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int count;
                byte data[] = new byte[1024];
                while ((count = bis.read(data, 0, 1024)) != -1) {
                    taos.write(data, 0, count);
                }
            } finally {
                taos.closeArchiveEntry();
                if (bis != null)
                    bis.close();
                if (fis != null)
                    fis.close();
                }
        } finally {
            if (taos != null)
                taos.close();
            if (fos != null)
                fos.close();
        }
        System.out.println(" tarCompression -> Compression end!");
        return true;

    }


    /**
     * 压缩文件成Gzip格式，Linux上可使用
     * 压缩文件夹生成后缀名为".gz"的文件并下载
     * @param folderPath,要压缩的文件夹的路径
     * */
    public static void CompressedFiles_Gzip(String folderPath, String targzipFilePath)
    {
        File srcPath =new File(folderPath);
        int length=srcPath.listFiles().length;
        byte[] buf = new byte[1024]; //设定读入缓冲区尺寸
        File[] files   =   srcPath.listFiles();
        try
        {
            //建立压缩文件输出流
            FileOutputStream fout=new FileOutputStream(targzipFilePath);
            //建立tar压缩输出流
            TarArchiveOutputStream tout=new TarArchiveOutputStream(fout);
            for(int i=0;i<length;i++)
            {
                String filename=srcPath.getPath()+File.separator+files[i].getName();
                //打开需压缩文件作为文件输入流
                FileInputStream fin=new FileInputStream(filename);   //filename是文件全路径
                TarArchiveEntry tarEn=new TarArchiveEntry(files[i]); //此处必须使用new TarEntry(File file);
                tarEn.setName(files[i].getName());  //此处需重置名称，默认是带全路径的，否则打包后会带全路径
                tout.putArchiveEntry(tarEn);
                int num;
                while ((num=fin.read(buf, 0, 1024)) != -1)
                {
                    tout.write(buf,0,num);
                }
                tout.closeArchiveEntry();
                fin.close();
            }
            tout.close();
            fout.close();

            //建立压缩文件输出流
            FileOutputStream gzFile=new FileOutputStream(targzipFilePath+".gz");
            //建立gzip压缩输出流
            GZIPOutputStream gzout=new GZIPOutputStream(gzFile);
            //打开需压缩文件作为文件输入流
            FileInputStream tarin=new FileInputStream(targzipFilePath);   //targzipFilePath是文件全路径
            int len;
            while ((len=tarin.read(buf, 0, 1024)) != -1)
            {
                gzout.write(buf,0,len);
            }
            gzout.close();
            gzFile.close();
            tarin.close();

            File f = new File(targzipFilePath);
            f.deleteOnExit();
        }catch(FileNotFoundException e)
        {
            System.out.println(e);
        }catch(IOException e)
        {
            System.out.println(e);
        }
    }
}