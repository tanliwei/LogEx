package log;


/**
 * @author tanlw
 * @version $ID: LogModel.java, v 0.1 2016-12-06 18:46
 */
public class LogModel {
    private String filepath;
    private int lineNo;
    private String key;
    private int count;

    public LogModel() {
    }

    @Override
    public String toString() {
        return

                "[" + Constants.HIT+ "=]" + Constants.LINE_SEPARATOR + key
                        + "[COUNT=]" + Constants.LINE_SEPARATOR + count + Constants.LINE_SEPARATOR
                        //+ "[FILE_PATH=]'" + Constants.LINE_SEPARATOR + filepath + Constants.LINE_SEPARATOR
                        + "[LINE_NO=]" + Constants.LINE_SEPARATOR + lineNo + Constants.LINE_SEPARATOR;
    }

    public LogModel(String filepath, int lineNo, String key, int count) {
        this.filepath = filepath;
        this.lineNo = lineNo;
        this.key = key;
        this.count = count;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
