package api.allegro.util;

import java.net.http.HttpRequest;
import java.util.Map;

public final class Util {
    public static HttpRequest.Builder addRequestHeaders(HttpRequest.Builder builder, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.setHeader(entry.getKey(), entry.getValue());
        }

        return builder;
    }

    //@SuppressWarnings("unchecked")
    public static Map deepMerge(Map dst, Map src) {
        if (dst != null && src != null) {
            for (Object key : src.keySet()) {
                if (src.get(key) instanceof Map && dst.get(key) instanceof Map) {
                    Map originalChild = (Map) dst.get(key);
                    Map newChild = (Map) src.get(key);
                    dst.put(key, deepMerge(originalChild, newChild));
                } else {
                    dst.put(key, src.get(key));
                }
            }
        }

        return dst;
    }
}
