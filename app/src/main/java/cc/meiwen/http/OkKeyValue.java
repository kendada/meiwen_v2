package cc.meiwen.http;

/**
 * Created by abc on 2017/7/29.
 * 公共参数存储
 */

public class OkKeyValue {

    public final String key;
    public final String value;

    public OkKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OkKeyValue keyValue = (OkKeyValue) o;

        return key == null ? keyValue.key == null : key.equals(keyValue.key);

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "KeyValue{" + "key='" + key + '\'' + ", value=" + value + '}';
    }

}
