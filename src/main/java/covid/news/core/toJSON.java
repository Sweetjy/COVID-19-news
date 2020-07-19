package covid.news.core;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import covid.news.entity.NewsInfo;

public class toJSON {
	
	public static String getJSON(NewsInfo art_info) throws JSONException{
		JSONObject map=new JSONObject();
//        HashMap<String, Object> map = new HashMap<>();
		map.put("url", art_info.getUrl());
		map.put("title", art_info.getTitle());
		map.put("source",art_info.getWebName());
		map.put("time", art_info.getPublishTime());
		map.put("content", art_info.getContent());
		return map.toString();
	}
}
