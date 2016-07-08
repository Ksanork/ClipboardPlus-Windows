package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mikołaj on 2016-02-13.
 */
public class Controller {
    private WebClient wc;
    private Settings settings;
    private ProcessManager pm;

    private int interval = 5;          //minuty
    private String id = null;

    //private final String SERVER_URL = "ws://127.0.0.1:8000";
    private final String SERVER_URL = "ws://webprocess-ksanork.rhcloud.com:8000";
    private final String SETTINGS_PATH = "C:/Users/All Users/WebProcess";
    private final int CP_APP_PORT = 1099;

    public Controller() {
        try {
            pm = new ProcessManager();
            wc = new WebClient(new URI(SERVER_URL), this);
            settings = new Settings(SETTINGS_PATH, "config.proporties");
            readSettings();

            wc.connect(id);

            //wysyłanie 'pinga' co interval
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("knock knock");
                    wc.sendPing();
                    //toolkit.beep();
                }
            }, interval * 60 * 100, interval * 60 * 1000);


            //obłsuga komuniacji z CP
            new Thread() {
                public void run() {
                    try {
                        //new ServerSocket(1099).accept();
                        ServerSocket s = new ServerSocket(CP_APP_PORT);

                        int i = 1;
                        while (true) {
                            Socket incoming = s.accept();
                            System.out.println("i = " + i);
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("new client");
                                        InputStream inStream = incoming.getInputStream();

                                        BufferedReader input = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
                                        String line = "";

                                        //int i = 0;
                                        line = input.readLine();
                                        System.out.println(line);

                                        if(line.equals("stop")) System.exit(0);

                                        incoming.close();
                                    }
                                    catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            };
                            Thread t = new Thread(r);
                            t.start();
                            i++;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void handleAddResult(String id) {
        saveID(id);
        wc.connect(id);
    }

    public void handleProcessExecute(String command) {
        System.out.println("odebreano komendę " + command);

        //wykonywanie procesu


        wc.sendJSON("process-execute-result", pm.execute(command));
    }

    public void handleScreenshotExecute() {
        Screenshooter sc = new Screenshooter();
        sc.makeScreenshot();

        wc.sendJSON("screenshot-execute-result", sc.getBase64());
    }

    public void readSettings() {
        String s[] = {"id", "interval", "start-partition"};
        HashMap<String, String> values = settings.readAllFromSettingsBy(s);

        System.out.println(values.get("id"));
        wc.setID(values.get("id"));
        //interval = Integer.parseInt(values.get("interval"));
        pm.setStartPartition(values.get("start-partition"));
    }

    public void saveID(String id) {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("id", id);

        settings.saveToSettings(hm);
    }
}
