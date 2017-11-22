package channel.demo.zzq.cn.eeepay.com.appchannel._okhttpmanager;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 描述：ParameterizedType 简单说来就是形如“ 类型<> ”的类型，如:Map<String,User>
 * 作者：zhuangzeqin
 * 时间: 2017/11/22-9:20
 * 邮箱：zzq@eeepay.cn
 */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;
    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }
    // 返回Map<String,User>里的String和User，所以这里返回[String.class,User.clas]
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }
    // Map<String,User>里的Map,所以返回值是Map.class
    @Override
    public Type getRawType() {
        return raw;
    }
    // 用于这个泛型上中包含了内部类的情况,一般返回null
    @Override
    public Type getOwnerType() {
        return null;
    }
}
