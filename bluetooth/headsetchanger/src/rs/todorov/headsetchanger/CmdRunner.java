package rs.todorov.headsetchanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdRunner {
    public static String runCommand(String command) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(command);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader((proc.getInputStream())));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader((proc.getErrorStream())));

            StringBuilder out = new StringBuilder();
            String s;
            while ((s = stdIn.readLine()) != null) {
                out.append(s).append("\n");
            }
            return out.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
