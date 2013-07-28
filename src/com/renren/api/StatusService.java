package com.renren.api;

import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



/**
 * 状态
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class StatusService extends BaseService {

    public StatusService(RenrenApiInvoker invoker) {
        super(invoker);
    }

    /**
     * 返回指定用户的状态列表，不包含回复内容
     * 注意：此方法需要用户授予 read_user_status 权限(在OAuth2.0授权中由scope参数指定)
     * @param page_id 状态信息所属用户id 0返回当前用户
     * @param page 分页		默认为1
     * @param count 每页个数 最大不能超过1000		默认为10
     * @return JSONArray 状态列表
     */
    public JSONArray getStatuses(long uid, int page, int count) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "status.gets");
        if (uid > 0) {//
            params.put("page_id", String.valueOf(uid));
        }
        //if (page != 1 || count != 10) {
        	params.put("page", String.valueOf(page));
            params.put("count", String.valueOf(count));
        //}
        return this.getResultJSONArray(params);
    }

    /**
     * 返回用户某条状态，不包含回复内容
     * 注意：此方法需要用户授予 read_user_status 权限(在OAuth2.0授权中由scope参数指定)
     * @param page_id 状态的id  0返回当前用户最新的状态
     * @param status_id 状态信息所属用户id，0前用户
     * @return
     */
    // public JSONObject getStatus(long status_id, long owner_id,Auth auth) {
    public JSONObject getStatus(long page_id, long status_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("method", "status.get");
        if (page_id > 0) {
            params.put("page_id", String.valueOf(page_id));
        }
        if (status_id > 0) {
            params.put("owner_id", String.valueOf(status_id));
        }
        return this.getResultJSONObject(params);
    }

}
