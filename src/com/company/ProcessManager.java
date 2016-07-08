package com.company;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by Mikołaj on 2016-02-05.
 */
public class ProcessManager {
    private String path = "C:/";
    private String pathdrive = "C:";

    public ProcessManager() {}

    public String execute(String command) {
        Process p = null;
        String response = "";
        try {
            setPath(command);
            changePartition(command);

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.directory(new File(path));
            p = pb.start();

            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "CP852"));

            String line;
            while ((line = br.readLine()) != null)
                response += line + "\n";
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return "Wystąpił problem z przetwarzanuem komendy";
        }
        catch(Exception e) {
            e.printStackTrace();
            return "Wystąpił problem z przetwarzanuem komendy";
        }

        return response;
    }

    public void setStartPartition(String s) {
        pathdrive = s;
    }

    public void changePartition(String s) {
        if(s.endsWith(":") && s.length() == 2) pathdrive = s + "/";
    }

    private void setPath(String cd) {
        if(!path.startsWith(pathdrive)) path = pathdrive;

        if(cd.startsWith("cd")) {
            String path2 = cd.substring(3);

            if(path2.startsWith(pathdrive)) path = path2;
            else {
                if(path2.startsWith("/") || path2.startsWith("\\"))
                    path += path2;
                else path += "/" + path2;
            }

            System.out.println(path2);
            System.out.println(path);

        }
    }
}
