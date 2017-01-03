package log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author tanlw
 * @version $ID: LogCounter.java, v 0.1 2016-12-06 18:52
 */
public class LogCounter {
    private static Map<String, LogModel> allLog = new HashMap<>();
    private static final int TRUNCATE_END = 23;

    /**
     * 插入
     *
     * @param key_lines
     * @param filepath
     * @param lineNo
     */
    public static void put(List<String> key_lines, String filepath, int lineNo) {
        LogModel model = getElement(allLog, key_lines);
        if (model == null) {
            model = new LogModel(filepath, lineNo, listToString(key_lines), 1);
        } else {
            model.setCount(model.getCount() + 1);
        }
        putElement(allLog, key_lines, model);
    }

    /**
     * 插入元素
     * 多行时 忽略第一行
     *
     * @return
     */
    private static void putElement(Map<String, LogModel> allLog, List<String> key_lines, LogModel model) {
        if (key_lines.size() == 1) {
            allLog.put(key_lines.toString().substring(TRUNCATE_END, key_lines.toString().length()), model);
        }
        if (key_lines.size() > 1) {
            allLog.put(key_lines.subList(1, key_lines.size()).toString(), model);
        }
    }

    private static String listToString(List<String> key_lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key_lines.size(); i++) {
            sb.append(key_lines.get(i));
            sb.append(Constants.LINE_SEPARATOR);
        }
        return sb.toString();
    }

    /**
     * 获取元素
     * 多行时 忽略第一行
     *
     * @param allLog
     * @param key_lines
     * @return
     */
    private static LogModel getElement(Map<String, LogModel> allLog, List<String> key_lines) {
        if (key_lines.size() == 1)
            return allLog.get(key_lines.toString().substring(TRUNCATE_END, key_lines.toString().length()));
        if (key_lines.size() > 0) {
            return allLog.get(key_lines.subList(1, key_lines.size()).toString());
        }
        return null;
    }

    /**
     * 获取结果
     *
     * @return
     */
    public static String getElements() {
        Iterator iterator = allLog.entrySet().iterator();
        StringBuilder content = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, LogModel> entry = (Map.Entry<String, LogModel>) iterator.next();
            content.append(entry.getValue().toString());
            content.append(Constants.LINE_SEPARATOR);
        }
        return content.toString();
    }

    /**
     * 清空
     */
    public static void clear() {
        allLog.clear();
    }
}
