package wang.ismy.seeaw;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

            Iterator<Object> iterator=jsonObject.getJSONObject("params").keys();

            while(iterator.hasNext()){
                String key=iterator.next().toString();

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
