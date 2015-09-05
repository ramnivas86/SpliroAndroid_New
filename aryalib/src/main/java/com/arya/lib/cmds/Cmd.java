package com.arya.lib.cmds;

import org.json.JSONObject;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public class Cmd {
    public static final String FLD_CMD = "cmd";

    protected JSONObject data = new JSONObject();

    public Cmd(String cmd){
        addData(FLD_CMD, cmd);
    }

    public void addData(String key, Object value){
        try{
            data.put(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Object getData(String key){
        try{
            return data.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJSONObject(){
        return data;
    }

    public String toJSONString(){
        return data.toString();
    }
}
