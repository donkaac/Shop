/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Achi
 */
public class Property {
    private String path = "./src/form/Comms.dll";

    public Property() {
        try {
            File f = new File(path);
            if (f.exists() && !f.isDirectory()) {
                InputStream is = new FileInputStream(path);
                Properties p = new Properties();
                p.loadFromXML(is);
                Set<Map.Entry<Object, Object>> entrySet = p.entrySet();
                Context context = Context.getContext();
                context.setAttribute("config", entrySet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createProperty(String path) {
        try {
            if (path.isEmpty()) {
                File f = new File(this.path);
                if (f.exists() && !f.isDirectory()) {
                    f.delete();
                }
                OutputStream os = new FileOutputStream(path);
                Properties p = new Properties();
                p.storeToXML(os, "Config", "UTF-8");
                return true;
            } else {
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    f.delete();
                }
                OutputStream os = new FileOutputStream(path);
                Properties p = new Properties();
                p.storeToXML(os, "Config", "UTF-8");
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean removeProperty(String path) {
        try {
            File f = new File(path);
            if (f.exists() && !f.isDirectory()) {
                return f.delete();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
