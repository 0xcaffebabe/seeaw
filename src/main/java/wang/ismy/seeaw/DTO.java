package wang.ismy.seeaw;


import java.util.HashMap;
import java.util.Map;

public class DTO {

    private String msg;

    private Map<String,String> params=new HashMap<String, String>();

    public String getMsg() {
        return msg;
    }

    public DTO setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public DTO addParams(String key,String value){

        params.put(key,value);
        return this;
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("{'msg':'"+msg+"','params':{");
        for(String key:params.keySet()){
            sb.append("'"+key+"':'"+params.get(key)+"',");
        }
        String ret=sb.toString();
        if(params.keySet().size()!=0){
            ret=sb.substring(0,sb.length()-1);
        }

        ret+="}}";
        return ret;
    }
}
