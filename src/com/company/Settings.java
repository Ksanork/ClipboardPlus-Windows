package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Mikołaj on 2016-02-15.
 */

//czy zapisywać 'name' do configa?
public class Settings {
    private File config;

    public Settings(String path, String name) {
        File dir = new File(path);
        if(!dir.exists()) dir.mkdir();

        config = new File(path + "/" + name);
    }

    public String readFromSettings(String property) {
        String result = null;

        try {
            FileReader reader = new FileReader(config);
            Properties props = new Properties();
            props.load(reader);

            result = props.getProperty(property);

            reader.close();
        } catch (FileNotFoundException ex) {
            result = null;
        } catch (IOException ex) {
            result = null;
        }

        return result;
    }

    //zwraca wartości z config o nazwach z ArrayList
    //public HashMap<String, String> readAllFromSettings(ArrayList<String> property) {
    public HashMap<String, String> readAllFromSettingsBy(String[] property) {
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            FileReader reader = new FileReader(config);
            Properties props = new Properties();
            props.load(reader);

            for(String s : property)
                result.put(s, props.getProperty(s));

            reader.close();
        } catch (FileNotFoundException ex) {
            result = null;
        } catch (IOException ex) {
            result = null;
        }

        return result;
    }

    public HashMap<String, String> readAllFromSettings() {
        HashMap<String, String> result = new HashMap<String, String>();

        try {
            FileReader reader = new FileReader(config);
            Properties props = new Properties();
            props.load(reader);

            Set<Object> set = props.keySet();
            for(Object o : set)
                result.put((String) o, props.getProperty((String) o));


            reader.close();
        } catch (FileNotFoundException ex) {
            result = null;
        } catch (IOException ex) {
            result = null;
        }

        return result;
    }

    public void saveToSettings(HashMap<String, String> hm) {
        try {
            Properties props = new Properties();
            for(Map.Entry<String, String> m : hm.entrySet())
                props.setProperty(m.getKey(), m.getValue());

            FileWriter writer = new FileWriter(config);
            props.store(writer, "WebProcess setiings");
            writer.close();
        } catch (FileNotFoundException ex) {
            // file does not exist
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOneToSettings(String key, String value) {
        HashMap<String, String> values = readAllFromSettings();
        if(values.containsKey(key)) values.replace(key, value);
        else values.put(key, value);

        saveToSettings(values);

    }


}
