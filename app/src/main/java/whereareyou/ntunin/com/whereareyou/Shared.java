package whereareyou.ntunin.com.whereareyou;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nik on 17.02.2018.
 */

public class Shared {
    static Map<String, Object> _instance = new HashMap<>();

    static Map<String, Object> getBundle() {
        return _instance;
    }
}
