package channel.demo.zzq.cn.eeepay.com.appchannel._okhttpmanager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 描述：解析json 工具类
 * 作者：zhuangzeqin
 * 时间: 2017/11/22-9:35
 * 邮箱：zzq@eeepay.cn
 */
public class ParseJsonUtils {
    private static final Gson MGSON = new Gson();

    /**
     * 解析data是object的情况
     * data 为 object 的情况{"status":200,"msg":"success","data":{}}
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Result<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return MGSON.fromJson(json, type);
    }

    /**
     * 解析data是array的情况
     * data 为 array  的情况{"status":200,"msg":"success","data":[]}
     * 是Array的情况要比是Object的情况多那么一步。
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Result<List<T>> fromJsonArray(String json, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return MGSON.fromJson(json, type);
    }

    /**
     * 判断是否是json结构
     *
     * @param jsonData 字符串
     * @return
     */
    public static boolean isCheckFormat(final String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
        } catch (JSONException e) {
            return false;
        } finally {
            if (jsonObject != null)
                jsonObject = null;
        }
        return true;
    }

}
