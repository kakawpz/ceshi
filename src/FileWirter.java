import java.io.*;

public class FileWirter {

    private static String filePath = "D:\\结对作业\\Exercises.txt";
    private static String filePath2 = "D:\\结对作业\\Answer.txt";

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     */
    public static boolean createFile(File fileName) throws Exception {
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 读取TXT内容
     *
     * @param file
     * @return
     */
    public static String readTxtFile(File file) {
        String result = "";
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "gbk");
            BufferedReader br = new BufferedReader(reader);
            String s = null;
            while ((s = br.readLine()) != null) {
                result = result + s;
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入TXT，覆盖原内容
     *
     * @param content
     * @param fileName
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(String content, File fileName) throws Exception {
        RandomAccessFile mm = null;
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(content.getBytes("gbk"));
            fileOutputStream.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 写入TXT，追加写入
     *
     * @param filePath
     * @param content
     */
    public static void fileChaseFW(String filePath, String content) {
        try {
            // 构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(filePath, true);

            fw.write(content+"\r\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("文件写入失败！" + e);
        }
    }

    public static void wirter(String args) throws Exception {
        File file = new File(filePath);
        createFile(file);
        //readTxtFile(file);
        // writeTxtFile("我是写入的内容11",file);
        fileChaseFW(filePath, args);
    }
    public static void wirter2(String args) throws Exception {
        File file = new File(filePath2);
        createFile(file);
        //readTxtFile(file);
        // writeTxtFile("我是写入的内容11",file);
        fileChaseFW(filePath2, args);
    }
}