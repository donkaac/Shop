/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Class;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Achi
 */
public class Context {
    private static Context c;
    private Map<String, Object> map = new HashMap<>();

    public static Context getContext() {
        if (c == null) {
            c = new Context();
        }
        return c;
    }
    /**
     * 
     * @param key
     * @return 
     */
    public Object getAttribute(String key){
        return map.get(key);
    }
    /**
     * 
     * @param key
     * @param value 
     */
    public void setAttribute(String key,Object value){
        map.put(key, value);
    }
    /**
     * 
     * @param key 
     */
    public void remove(String key){
        map.remove(key);
    }
    /**
     * 
     * @return Set Object,Object
     */
    public Set getAll(){
        return map.entrySet();
    }
    /**
     * 
     * @return Integer
     */
    public int getSize(){
        return map.size();
    }
    /**
     * 
     * @return Boolean
     */
    public Boolean isEmpty(){
        return map.isEmpty();
    }
}
