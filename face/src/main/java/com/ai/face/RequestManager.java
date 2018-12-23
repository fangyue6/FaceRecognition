package com.ai.face;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <b>Project:</b> FaceRecognition <br>
 * <b>Create Date:</b> 2018/12/23 <br>
 * <b>@author:</b> qy <br>
 * <b>Address:</b> qingyongai@gmail.com <br>
 * <b>Description:</b>  <br>
 */
public class RequestManager {

    public static void practice(String name, String pic, StringCallback callback) {
        PostRequest<String> request = OkGo.post("http://tx.fangyue.xyz:5000/calCulateDectionFace");
        JSONObject object = new JSONObject();
        try {
            object.put("person", name);
            object.put("photo", pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.upJson(object.toString());
        request.execute(callback);
    }

    public static void recogn(String name, String pic, StringCallback callback) {
        PostRequest<String> request = OkGo.post("http://tx.fangyue.xyz:5000/myFaceRecognition");
        JSONObject object = new JSONObject();
        try {
            object.put("person", name);
            object.put("photo", pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.upJson(object.toString());
        request.execute(callback);
    }
}
