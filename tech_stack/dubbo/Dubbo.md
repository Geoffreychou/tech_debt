Dubbo



多版本支持



主机绑定



集群容错

1. failover
2. failsafe
3. failfast
4. failback
5. forking
6. broadcast



服务降级

人工降级

自动降级



非核心功能人工降级

故障降级

限流降级



Mock





配置优先级别

服务提供的配置，通过 URL 经由注册中心传递给消费方



extension

ExtensionLoader

从cacheAdaptiveInstance 这个内存缓存中获得一个对象实例

如果实例为空，说明是第一次加载，则通过双重检查锁的方式去创建一个适配器扩展点

getAdaptiveExtension

​	createAdaptiveExtension

​		getAdaptiveExtensionClass

​			getExtensionClasses  //  加载所有路径下的扩展点

​				cacheDefaultExtensionName

​				loadDirectory

​					loadClass // @Adaptive如果是加在类上， 表示当前类是一个自定义的自适应扩展点,如果是加在方法级别上，表示需要动态创建一个自适应扩展点

​			createAdaptiveExtensionClass // 动态创建一个扩展点

​		injectExtension

​			objectFactory.getExtension(pt, property);



Protocol protocol =
ExtensionLoader.getExtensionLoader(Protocol.class). getAdaptiveExtension();

```java
//从url或扩展接口获取扩展接口实现类的名称
//根据名称，获取实现类ExtensionLoader.getExtensionLoader(扩展接口类).getExtension(扩展接口实现类名称)，然后调用实现类的方法
public class Protocol$Adaptive implements com.alibaba.dubbo.rpc.Protocol {
    public void destroy() {
        throw new UnsupportedOperationException("method public abstract void com.alibaba.dubbo.rpc.Protocol.destroy() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
    }

    public int getDefaultPort() {
        throw new UnsupportedOperationException("method public abstract int com.alibaba.dubbo.rpc.Protocol.getDefaultPort() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
    }

    public com.alibaba.dubbo.rpc.Invoker refer(java.lang.Class arg0, com.alibaba.dubbo.common.URL arg1) throws com.alibaba.dubbo.rpc.RpcException {
        if (arg1 == null) throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg1;
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
        com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
        return extension.refer(arg0, arg1);
    }

    public com.alibaba.dubbo.rpc.Exporter export(com.alibaba.dubbo.rpc.Invoker arg0) throws com.alibaba.dubbo.rpc.RpcException {
        if (arg0 == null) throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
        if (arg0.getUrl() == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
        com.alibaba.dubbo.common.URL url = arg0.getUrl();
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
        com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
        return extension.export(arg0);
    }
}

```







export

org.apache.dubbo.config.spring.ServiceBean#onApplicationEvent

​	export()

​		super.export();

​			doExport()

​				doExportUrls()

​					doExportUrlsFor1Protocol

