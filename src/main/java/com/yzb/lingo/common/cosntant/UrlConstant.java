package com.yzb.lingo.common.cosntant;

/**
 * @author wangban
 * @data 2019/10/30 14:09
 */
public class UrlConstant {

//    public static final String BASE_MC_URL = "http://192.168.35.201:7777/public/";
    public static final String BASE_MC_URL="http://118.31.54.117:7777/";

    /**
     * 获取工序api
     */
    public static final String PRODUCT_API = BASE_MC_URL + "api/index/product?mname=%s&ltype=%s&edition=%s";

    /**
     * 登录api
     */
    public static final String LOGIN_API = BASE_MC_URL + "api/index/login?username=%s&password=%s";

    /**
     * 所有工序列表
     */
    public static final String M_PRODUCT_API = BASE_MC_URL + "api/index/mproduct";

    /**
     * 上传接口
     */
//    private static final String BASE_URL = "http://192.168.35.201:9089/v1/";
    private static final String BASE_URL = "http://127.0.0.1:9089/v1/";

    /**
     * 上传数据
     */
    public static final String UPLOAD_PRODUCT_API = BASE_URL + "faProductLingo/save";
    public static final String UPLOAD_DELANDSAVE_PRODUCT_API = BASE_URL + "faProductLingo/delAndSave";

}
