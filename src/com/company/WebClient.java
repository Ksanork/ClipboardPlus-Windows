package com.company;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Mikołaj on 2016-02-13.
 */
public class WebClient extends WebSocketClient {
    private String id = null;
    private Controller cc;

    public WebClient(URI serverURI, Controller cc) {
        super(serverURI);
        System.out.println(id);
        this.cc = cc;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("open connection");
        System.out.println(id);

        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("kk:mm, dd.MM.yyyy");

        if(id != null) sendConnect(id, ft.format(d));
    }

    @Override
    public void onMessage(String s) {
        JSONObject json = new JSONObject(s);

        System.out.println(json.getString("type"));


        switch(json.getString("type")) {
            case "add-result":
                String id = json.getJSONObject("content").getString("_id");
                System.out.println(id);
                cc.handleAddResult(id);
                break;
            case "process-execute":
                cc.handleProcessExecute(json.getString("content"));
                break;
            case "screenshot-execute":
                cc.handleScreenshotExecute();
                break;
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Rozłączenie...");
        //super.connect();
        //sendConnect(id);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error...");
        e.printStackTrace();
    }

    public void sendJSON(String type, String content) {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("content", content);

        send(json.toString());
    }

    public void sendPing() {
        sendJSON("ping", "");
    }


    public void sendJSON(String type, HashMap<String, String> content) {
        JSONObject json = new JSONObject();
        json.put("type", type);

        JSONObject contentjson = new JSONObject();
        for(Map.Entry<String, String> e : content.entrySet())
            contentjson.put(e.getKey(), e.getValue());

        json.put("content", contentjson);

        send(json.toString());
    }

    public void setID(String id) {
        this.id = id;
    }

    public void connect(String id) {
        super.connect();
        if(id != null) this.id = id;
    }

    private void sendConnect(String id, String date) {
        System.out.println("send connect");
        HashMap<String, String> content = new HashMap<String, String>();;
        content.put("_id", id);
        content.put("date", date);

        sendJSON("connect", content);
    }

}
