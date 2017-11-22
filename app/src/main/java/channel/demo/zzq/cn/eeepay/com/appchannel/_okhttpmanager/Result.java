package channel.demo.zzq.cn.eeepay.com.appchannel._okhttpmanager;

import java.io.Serializable;

/**
 * 描述：抽象出后台接口返回的数据格式
 * data 为 object 的情况{"status":200,"msg":"success","data":{}}
 * data 为 array  的情况{"status":200,"msg":"success","data":[]}
 * 作者：zhuangzeqin
 * 时间: 2017/11/22-9:29
 * 邮箱：zzq@eeepay.cn
 */
public class Result<T> implements Serializable {
    public int status;//200 标识成功
    public String msg;//提示语
    public T data;//泛型T 数据
}
