package log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author tanlw
 * @version $ID: LogExtractor.java, v 0.1 2016-12-06 17:19
 * @reference http://www.cnblogs.com/LiuChunfu/p/5651956.html  Apache下的FileUtils.listFiles方法简单使用技巧
 */
public class LogExtractor {

    public static final String LOG = "log";
    public static final String EXIT = "N";
    public static final String INFO = "INFO";
    public static final String DEBUG = "DEBUG";
    public static final String WARN = "WARN";
    public static final String ERROR = "ERROR";
    public static final String OUTPUT_SUFFIX = "err";
    public static final String TARGET_SEPARATOR = ERROR;
    public static final List<String> SEPARATORS = new ArrayList<String>() {{
        add(INFO);
        add(DEBUG);
        add(WARN);
        add(ERROR);
    }};
    private static final int LOG_LIMIT = 40;//日志行数限制

    public static void main(String[] args) {

        welcome();
        boolean exits;
        do {
            String filepath = getInput();
            List<File> files = getFilesPath(filepath);
            System.out.println("文件个数:" + files.size());
            extractLog(files, TARGET_SEPARATOR);
            System.out.println("处理完成");
            exits = exit();
        } while (!exits);
    }


    /**
     * 抽取ERROR日志
     *
     * @param files
     * @param targetSeparator
     */
    private static void extractLog(List<File> files, String targetSeparator) {
        System.out.println("抽取ERROR日志(log" + OUTPUT_SUFFIX + ")...");
        File file;
        for (int i = 0; i < files.size(); i++) {
            try {
                file = files.get(i);
                LogCounter.clear();
                List<String> lines = FileUtils.readLines(file, Constants.UTF8);
                if (lines.size() == 0) {
                    System.out.println("文件无内容. filepath:" + file.getAbsolutePath());
                    continue;
                }
                int start = 0;//一条日志起始行,初始值:0
                int end;//一条日志终止行
                if (lines.size() == 1) {
                    if (contains(lines.get(start), SEPARATORS)) {
                        LogCounter.put(lines, file.getAbsolutePath(), start + 1);
                    }
                    continue;
                }
                //提取一条日志信息
                List<String> singleLogContent;//单条日志信息
                for (end = 1; end < lines.size(); end++) {
                    if (contains(lines.get(start), TARGET_SEPARATOR)) {
                        singleLogContent = extractLog(lines, start, end, LOG_LIMIT);
                        LogCounter.put(singleLogContent, file.getAbsolutePath(), start + 1);
                    }
                    if (contains(lines.get(end), SEPARATORS)) {
                        start = end;
                    }
                }
                String extractResult = LogCounter.getElements();
                File fileOutput = new File(file.getAbsolutePath() + OUTPUT_SUFFIX);
                FileUtils.write(fileOutput, extractResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("完成:" + (i + 1) + "/" + files.size());
        }
    }

    private static List<String> extractLog(List<String> lines, int start, int end, int limit) {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        List<String> error_lines = new ArrayList<>();
        for (int i = start; i < end; i++) {
            error_lines.add(lines.get(i));
            if (count++ > limit) {//限制行数
                break;
            }
        }
        return error_lines;
    }

    /**
     * 包含分隔符之一
     *
     * @param line
     * @param separators
     * @return
     */
    private static boolean contains(String line, List<String> separators) {
        for (int i = 0; i < separators.size(); i++) {
            if (line.contains(separators.get(i)))
                return true;
        }
        return false;
    }

    /**
     * 包含分隔符
     *
     * @param line
     * @param separator
     * @return
     */
    private static boolean contains(String line, String separator) {
        return line.contains(separator);
    }

    /**
     * 退出
     *
     * @return
     */
    private static boolean exit() {
        String str = getExit();
        if (EXIT.equals(str.toUpperCase())) {
            System.out.println("您已退出");
            return true;
        }
        return false;
    }

    /**
     * 获取日志文件
     *
     * @param filepath
     * @return
     */
    protected static List<File> getFilesPath(String filepath) {
        System.out.println("获取文件路径");
        while (1 == 1) {
            List<File> files = new ArrayList<>();
            File logFile = new File(filepath);
            if (!logFile.exists()) {
                System.out.println("文件路径不存在!");
                filepath = getInput();
                continue;
            }
            if (logFile.isFile()) {
                System.out.println("输入了文件路径.");
                files.add(new File(filepath));
                return files;
            }
            if (logFile.isDirectory()) {
                System.out.println("输入了文件夹路径. 过滤所有" + LOG + "文件.");
                files.addAll(FileUtils.listFiles(logFile, FileFilterUtils.suffixFileFilter(LOG), DirectoryFileFilter.INSTANCE));
                for (int i = 0; i < files.size(); i++) {
                    System.out.println(files.get(i).getAbsolutePath());
                }
                return files;
            }
        }
    }

    /**
     * 欢迎界面
     */
    public static void welcome() {
        System.out.println("Welcome to use error log extractor!");
        System.out.println("去重策略:截取日期(一行)");
        System.out.println("去重策略:截取第一行(多行)");
        System.out.println("默认编码格式:utf-8");
        System.out.println("结果格式:");
        System.out.println(new LogModel());

    }

    /**
     * 获取输入
     *
     * @return
     */
    public static String getInput() {
        //获取用户输入的字符串
        String str = null;
        while (1 == 1) {
            if (null != str) {
                break;
            }
            System.out.println("请输入日志所在文件夹/日志文件路径:");
            //创建输入对象
            Scanner sc = new Scanner(System.in);
            str = sc.nextLine();
            if (null == str || str.length() == 0) {
                System.out.println("输入有误");
                str = null;
            }
        }
        return str;
    }

    /**
     * 获取退出命令
     *
     * @return
     */
    public static String getExit() {
        //获取用户输入的字符串
        String str = null;
        while (1 == 1) {
            if (null != str) {
                break;
            }
            System.out.println("是否继续(n/N?)");
            //创建输入对象
            Scanner sc = new Scanner(System.in);
            str = sc.nextLine();
            if (null == str || str.length() == 0) {
                System.out.println("输入有误");
                str = null;
            }
        }
        return str;
    }
}
