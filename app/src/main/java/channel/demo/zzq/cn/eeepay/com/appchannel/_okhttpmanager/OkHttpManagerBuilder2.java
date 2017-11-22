package channel.demo.zzq.cn.eeepay.com.appchannel._okhttpmanager;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Request;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：升级版2.0 二次封装OKHTTP 请求构建
 * <p>
 * update zhuangzeqin 2017年11月22日 11:42:28 为了不影响之前的影响； 在此新增一个2.0类进行修改；在过程中使用； 在过程中完美升级；
 * <p>
 * 使用的时候
 * new OkHttpManagerBuilder2.Builder().
 * requestPath(url).
 * setParams(params).
 * setTag(tag).
 * isListData(true).
 * setResultCallBack(new OkHttpManagerBuilder2.ResultCallBack<List<A>>())
 * .build().start();
 * 作者：zhuangzeqin
 * 时间: 2017/11/6-19:09
 * 邮箱：zzq@eeepay.cn
 */
public final class OkHttpManagerBuilder2 {
    private Map<String, String> mParams;//参数封装
    private String mUrl;//设置请求的url
    private Object mTag;//设置请求的tag
    private boolean isSynchronization = false;//是否同步请求；默认是false
    private boolean isListData = false;//是否 data 字段是是否未List 也就是jsonArray []
    private ResultCallBack mResultCallBack;//监听的返回接口

    /**
     * 私有的构造函数
     *
     * @param mUrl
     * @param mParams
     * @param mTag
     * @param isSynchronization
     * @param mResultCallBack
     */
    private OkHttpManagerBuilder2(String mUrl, Map<String, String> mParams, Object mTag, boolean isSynchronization, ResultCallBack mResultCallBack, boolean isListData) {
        this.mParams = mParams;
        this.mUrl = mUrl;
        this.mTag = mTag;
        this.isSynchronization = isSynchronization;
        this.mResultCallBack = mResultCallBack;
        this.isListData = isListData;//是否list
    }

    public static class Builder {
        private Map<String, String> mParams;//参数封装
        private String mUrl;//设置请求的url
        private Object mTag;//设置请求的tag
        private boolean isSynchronization = false;//是否同步请求；默认是false
        private boolean isListData = false;//是否 data 字段是是否未List 也就是jsonArray []
        private ResultCallBack mResultCallBack;//监听的返回接口

        public Builder() {
            mParams = new HashMap<>();//ApiUtil.getParams(MyApplication.getInstance().getApplicationContext());//公共参数的请求
        }

        /**
         * 设置请求的url
         *
         * @param url
         * @return
         */
        public OkHttpManagerBuilder2.Builder requestPath(@NonNull String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * 设置请求的参数
         *
         * @param params
         * @return
         */
        public OkHttpManagerBuilder2.Builder setParams(Map<String, String> params) {
            //add by zhuangzeqin 2017年11月7日 14:55:49
            // putAll可以合并两个MAP，只不过如果有相同的key那么用后面的覆盖前面的
            if (params != null && params.size() > 0)
                mParams.putAll(params);
            return this;
        }

        /**
         * 设置请求的tag
         *
         * @param tag
         * @return
         */
        public OkHttpManagerBuilder2.Builder setTag(Object tag) {
            this.mTag = tag;
            return this;
        }

        /**
         * 是否同步
         *
         * @param isSynchronization
         * @return
         */
        public OkHttpManagerBuilder2.Builder isSynchronization(boolean isSynchronization) {
            this.isSynchronization = isSynchronization;
            return this;
        }

        /**
         * data 为 array  的情况{"status":200,"msg":"success","data":[]}
         * 是否返回list 数据
         *
         * @param isListData
         * @return
         */
        public OkHttpManagerBuilder2.Builder isListData(boolean isListData) {
            this.isListData = isListData;
            return this;
        }

        /**
         * 设置监听回调接口
         *
         * @param resultCallBack
         */
        public OkHttpManagerBuilder2.Builder setResultCallBack(@NonNull ResultCallBack resultCallBack) {
            this.mResultCallBack = resultCallBack;
            return this;
        }

        /**
         * 静态内部类调用外部类的构造函数，来构造外部类
         *
         * @return
         */
        public OkHttpManagerBuilder2 build() {
            return new OkHttpManagerBuilder2(mUrl, mParams, mTag, isSynchronization, mResultCallBack, isListData);
        }
    }

    /**
     * 开始请求数据
     */
    public void start() {
        //add by zhuangzeqin 2017年11月11日 15:37:34 当有非法参数时；向外层子业务抛出异常
        if (TextUtils.isEmpty(mUrl))
            throw new IllegalStateException("===url is null===");
        if (!URLUtil.isNetworkUrl(mUrl))
            throw new IllegalStateException(mUrl + "===The url is Illegal address.===");
        if (mResultCallBack == null)
            throw new IllegalStateException("===ResultCallBack is null,you can must implement.===");
        if (isSynchronization) //同步请求
            OkHttpClientManager.getAsyn(mUrl, mParams, resultCallback, mTag);
        else//异步请求
            OkHttpClientManager.postAsyn(mUrl, mParams, resultCallback, mTag);
    }

    /**
     * OKHttp 回调接口
     */
    private final OkHttpClientManager.ResultCallback resultCallback = new OkHttpClientManager.ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {
            //LogUtils.d(mTag + "-----request----" + e.getMessage());
            //mResultCallBack.onFailure(mTag, MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.network_error, mTag));
        }

        @Override
        public void onResponse(String response) {
            // response = "{\"status\":200,\"msg\":\"失败\",\"data\":[{\"cardNo\":\"100000000000091\",\"accNo\":\"6225768749734024\",\"accName\":\"卢紫俊\",\"bankName\":\"招商银行\",\"accNoT\":\"4024\"}]}";
//            LogUtils.d("response  : " + response);
            if (TextUtils.isEmpty(response)) {
                //mResultCallBack.onFailure(mTag, String.format(MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.exception_getdata), mTag));
                return;
            }
            Result result;//后台返回的结果处理
            try {
                //判断是否是json结构---后台有可能返回的不是json结构的数据，而引起解析异常奔溃
                boolean isjsonFormat = ParseJsonUtils.isCheckFormat(response);
                if (!isjsonFormat) {
//                    mResultCallBack.onFailure(mTag, String.format(MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.exception_getdata), mTag));
                    return;//如果是后台返回的不是json格式，直接返回；不做任何操作
                }
                Class javaBeanclass = mResultCallBack.getJavaBeanclass();
                if (isListData)//如果Data 是json 数组
                    result = ParseJsonUtils.fromJsonArray(response, javaBeanclass);//由子业务来告诉具体泛型类型
                else//如果Data 是jsonOBject
                    result = ParseJsonUtils.fromJsonObject(response, javaBeanclass);//由子业务来告诉具体泛型类型
                if (result != null) {
                    if (result.status != HttpURLConnection.HTTP_OK) {
                        String message = result.msg;
                        if (!TextUtils.isEmpty(message)) {//不为空的；提示错误信息
                            mResultCallBack.onFailure(mTag, message);
                        }
                        return;
                    }
                    mResultCallBack.onSucceed(mTag, result.data);
                } else {
//                    mResultCallBack.onFailure(mTag, String.format(MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.exception_getdata), mTag));
                }
            } catch (JsonSyntaxException e) {//解析异常，调用不同的解析方式； 说明是array数组 or 是对象
                Class javaBeanclass = mResultCallBack.getJavaBeanclass();
                if (isListData)//如果Data 是json 数组
                    result = ParseJsonUtils.fromJsonObject(response, javaBeanclass);//由子业务来告诉具体泛型类型
                else//如果Data 是jsonOBject
                    result = ParseJsonUtils.fromJsonArray(response, javaBeanclass);//由子业务来告诉具体泛型类型
                mResultCallBack.onSucceed(mTag, result.data);
            } catch (Exception ex) {
                ex.printStackTrace();
//                mResultCallBack.onFailure(mTag, String.format(MyApplication.getInstance().getApplicationContext().getResources().getString(R.string.exception_getdata), mTag));
            }
        }
    };

    /**
     * 将结果返回给外部调用者使用
     **/
    public interface ResultCallBack<T> {//外围实现这个接口的时候； 传的就是具体的Data 数据 比如 ResultCallBack<List<A>>

        void onSucceed(Object tag, T data);//成功

        void onFailure(Object tag, String msg);//失败

        @NonNull
        Class<T> getJavaBeanclass();//由子业务来告诉具体泛型类型
    }
}
