package wang.ismy.seeaw;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Utils {

    public static DTO msgToDTO(String msg){

        DTO dto=new DTO();
        try {
            JSONObject jsonObject=new JSONObject(msg);
            dto.setMsg(jsonObject.optString("msg"));

            Map<String,String> tmp=new HashMap<String, String>();

            Iterator<String> iterator=jsonObject.getJSONObject("params").keys();

            while(iterator.hasNext()){
                String key=iterator.next();

                tmp.put(key,jsonObject.getJSONObject("params").optString(key));
            }
            dto.setParams(tmp);
            return dto;
        } catch (JSONException e) {
            dto.setMsg("");
            dto.setParams(new HashMap<String, String>());
            return dto;
        }

    }


}
