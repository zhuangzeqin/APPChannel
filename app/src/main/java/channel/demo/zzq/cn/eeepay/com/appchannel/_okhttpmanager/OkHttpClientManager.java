package channel.demo.zzq.cn.eeepay.com.appchannel._okhttpmanager;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zw on 2016/8/1 0001.
 */
public class OkHttpClientManager {
    //生产地址以后换这里，，，有问题找曾武-2017年11月10日 16:40:47
//    public static final String SERVICE_ADDRESS = "http://192.168.3.42/";
//    public static final String SERVICE_ADDRESS ="http://192.168.1.182:8020/repay/";//182 测试;
    public static final String SERVICE_ADDRESS = "http://repay.olvip.vip/";//生成域名

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private boolean isLog = true;
    private static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    //*************对外公布的方法************


    public static Response getAsyn(String url, Object tag) throws IOException {
        return getInstance()._getAsyn(url, tag);
    }


    public static String getAsString(String url, Object tag) throws IOException {
        return getInstance()._getAsString(url, tag);
    }

    public static void getAsyn(String url, ResultCallback callback, Object tag) {
        getInstance()._getAsyn(url, callback, tag);
    }

    public static void getAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag) {
        Param[] paramsArr = map2Params(params);
        String msg = "";
        for (Param param : paramsArr) {
            msg += "&" + param.key + "=" + param.value;
        }
        msg = url + "?" + msg.substring(1);
//        LogUtils.d(TAG, "url = " + msg);
        getInstance()._getAsyn(msg, callback, tag);
    }

    public static void postAsyn(String url, Map<String, String> params, final ResultCallback callback) {
        getInstance()._postAsyn(url, callback, params, "");
    }

    public static void postAsyn(String url, Map<String, String> params, final ResultCallback callback, Object tag) {
        getInstance()._postAsyn(url, callback, params, tag);
    }

    /**
     * 传递json
     *
     * @param url
     * @param params
     * @param json
     * @param callback
     * @param tag
     */
    public static void postAsyn(String url, Map<String, String> params, String json, final ResultCallback callback, Object tag) {
        getInstance()._postJsonAsyn(url, callback, params, json, tag);
    }

    public static void postAsyn(String url, File[] files, String[] fileKeys, Map<String, String> params, ResultCallback callback) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, "", map2Params(params));
    }


    public static void postAsyn(String url, File[] files, String[] fileKeys, Map<String, String> params, ResultCallback callback, Object tag) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, tag, map2Params(params));
    }

    public static void postAsyn(String url, File[] files, String[] fileKeys, byte[] bt, Map<String, String> params, ResultCallback callback, Object tag) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, bt, tag, map2Params(params));
    }


    public static void postAsyn(String url, File file, String fileKey, ResultCallback callback, Object tag) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, tag);
    }


    public static void postAsyn(String url, File file, String fileKey, Map<String, String> params, ResultCallback callback, Object tag) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, tag, map2Params(params));
    }

    public static void downloadAsyn(String url, String destDir, ResultCallback callback, Object tag) {
        getInstance()._downloadAsyn(url, destDir, callback, tag);
    }

    //*******************************

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _getAsyn(String url, Object tag) throws IOException {
        final Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url, Object tag) throws IOException {
        Response execute = _getAsyn(url, tag);
        return execute.body().string();
    }


    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback, Object tag) {
        final Request request = new Request.Builder()
                .tag(tag)
                .url(changeUrl(url))
                .build();
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求；上传json
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postJsonAsyn(String url, final ResultCallback callback, Map<String, String> params, String json, Object tag) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr, json, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params, Object tag) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Object tag, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params, tag);
        deliveryResult(callback, request);
    }

    /**
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param bt
     * @param tag
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, byte[] bt, Object tag, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files,
                fileKeys, params, bt, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Object tag) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Object tag, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params, tag);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback, Object tag) {
        final Request request = new Request.Builder()
                .tag(tag)
                .url(changeUrl(url))
                .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    //****************************

    /**
     * add by xqf 人脸识别加 20170815
     *
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @param tag
     * @return
     */
    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params, Object tag) {
        return buildMultipartFormRequest(url, files, fileKeys, params, null, tag);

    }

    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params, byte[] bt, Object tag) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (Param param : params) {
//            LogUtils.d(url + "" + param.key + "=" + param.value);
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value == null ? "" : param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
//                LogUtils.d(TAG, "fileName = " + fileName);
//                LogUtils.d(TAG, "fileKey = " + fileKeys[i]);
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }
        if (bt != null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                    bt);
            builder.addFormDataPart("file", "file", fileBody);
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .tag(tag)
                .url(changeUrl(url))
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private static Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";

    private Map<String, String> mSessions = new HashMap<String, String>();

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                if (e.toString().contains("Canceled")) {
                    //如果是主动取消的情况下就不回调了
                } else {
                    sendFailedStringCallback(request, e, callback);
                }
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
//                    LogUtils.d("OkHttpClientManager", "string:" + string);
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }


                } catch (IOException e) {
//                    LogUtils.d("okHttp IOException");
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
//                    LogUtils.d("okHttp Json解析的错误");
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Request buildPostRequest(String url, Param[] params, String json, Object tag) {
        RequestBody body = RequestBody.create(JSON, json);
        String msg = "";
        for (Param param : params) {
            msg += "&" + param.key + "=" + param.value;
        }
        msg = url + "?" + msg.substring(1);
//        LogUtils.d(TAG, "url = " + msg);
        return new Request.Builder()
                .tag(tag)
                .url(msg)
                .post(body)
                .build();
    }

    private Request buildPostRequest(String url, Param[] params, Object tag) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        String msg = "";
        for (Param param : params) {
            if (isLog) {
                msg += param.key + "=" + param.value + "&";
            }
            builder.add(param.key, param.value == null ? "" : param.value);
        }
//        LogUtils.d(TAG, "url = " + changeUrl(url) + "\n params = " + msg);
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .tag(tag)
                .url(changeUrl(url))
                .post(requestBody)
                .build();
    }


    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    private static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    /**
     * 结束请求
     *
     * @param tag 请求标识
     */
    public static void cancel(Object tag) {
        if (tag != null) {
//            LogUtils.d(TAG, "cancel : " + tag);
            getInstance().mOkHttpClient.cancel(tag);
        }
    }

    /**
     * 切换地址用
     *
     * @param url
     * @return
     */
    private String changeUrl(String url) {
//        switch (GApplication.changeKey) {
//            case -1:
//                return url;
//            case 0://生产
//            case 3://自定义
//            case 1://准
//            case 2://测试
//                String coreUrl = PreferenceUtils.getStringParam(ABConfig.CORE_URL);
//                return url.replace(SERVICE_ADDRESS, coreUrl);
//            default:
        return SERVICE_ADDRESS;
    }
}

