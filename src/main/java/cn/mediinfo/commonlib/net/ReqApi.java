package cn.mediinfo.commonlib.net;

/**
 * @author yansy
 * @date 2018/10/15
 * @TODO 接口地址
 */
public class ReqApi {
    /**
     * 贵航：http://192.168.18.173:9002
     * 公司：http://172.19.20.72:9002
      */
    public static final String BASE_URL = "http://172.19.20.72:9002";
    public static String tk;
    public static final String AppSecret = "SzSvSEZLIBNljh5L";//获取token的参数
    public static final String tokenUrl = "http://192.168.1.146:31112/connect/token";//获取token
    //公共数据
    public static final String getCommonData = "/HIS-GongYong/V1/GYShuJuZD/GetShuJuZDList";

}
