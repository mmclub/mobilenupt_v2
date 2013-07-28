package com.renren.api;

/**
 * 
 * @author Administrator
 *
 */
public class RenrenApiClient {
    private static RenrenApiClient instance = new RenrenApiClient();

    private RenrenApiInvoker       renrenApiInvoker; 
    // 获取状态的服务
    private StatusService          statusService;

    private RenrenApiClient() {
        this.renrenApiInvoker = new RenrenApiInvoker();
        this.initService();
    }

    public static RenrenApiClient getInstance() {
        return instance;
    }

    /**
     * 检测配置
     */
    private void checkConfig() {
        String renrenApiKey = RenrenApiConfig.renrenApiKey;
        String renrenApiSecret = RenrenApiConfig.renrenApiSecret;
        if (renrenApiKey == null || renrenApiKey.length() < 1 || renrenApiSecret == null
            || renrenApiSecret.length() < 1) {
            throw new RuntimeException(
                "Please init com.renren.api.client.RenrenApiConfig.renrenApiKey and com.renren.api.client.RenrenApiConfig.renrenApiSecret!");
        }
    }

    /**
     * 初始化
     */
    private void initService() {
        this.statusService = new StatusService(this.renrenApiInvoker);
    }

    public BaseService getService(String clazz){
        BaseService baseService=null;
        try {
            baseService=(BaseService) Class.forName(clazz).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return baseService;
    }
    
    public StatusService getStatusService() {
        return statusService;
    }

}
