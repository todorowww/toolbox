package rs.todorov.headsetchanger;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothDevice {
    private String device;
    private String profile;
    private String description;

    private String cardId;
    private String cardName;
    private String sinkId;
    private String micId;
    private int baseVolume;
    private int baseVolumePercent;

    private String listSinks;
    private String listCards;

    public void setup() {
        this.getSinksAndCards();
        this.findWHDevice();
        this.fetchSinkInfo();
        this.fetchCardInfo();
    }

    private void getSinksAndCards() {
        this.listSinks = CmdRunner.runCommand("pacmd list-sinks");
        this.listCards = CmdRunner.runCommand("pacmd list-cards");
    }

    public void findWHDevice() {
        Pattern regex;
        Matcher match;
        String[] sinks = this.listCards.split("index:");
        sinks =  Arrays.copyOfRange(sinks, 1, sinks.length);
        for(String sink: sinks) {
            sink = "index:" + sink;
            regex = Pattern.compile("WH\\-1000XM");
            match =  regex.matcher(sink);
            if (match.find()) {
                regex = Pattern.compile("index: (\\d+)");
                match =  regex.matcher(sink);
                if (match.find()) {
                    this.cardId = match.group(1);
                }
            }
        }
    }

    private void fetchCardInfo() {
        int start = this.listCards.indexOf("index: " + this.cardId);
        int end = this.listCards.indexOf("index:", start + 1);
        String cardData;
        if (end > -1) {
            cardData = this.listCards.substring(start, end);
        } else {
            cardData = this.listCards.substring(start);
        }

        Pattern regex = Pattern.compile("name: <(.*)>");
        Matcher match =  regex.matcher(cardData);
        if (match.find()) {
            this.cardName = match.group(1);
        }

        regex = Pattern.compile("active profile: <(.*)>");
        match =  regex.matcher(cardData);
        if (match.find()) {
            this.profile = match.group(1);
        }

        regex = Pattern.compile("device.description = \"(.*)\"");
        match =  regex.matcher(cardData);
        if (match.find()) {
            this.description = match.group(1).trim();
        }

    }

    private void fetchSinkInfo() {
        int start = this.listSinks.indexOf("index: " + this.getDefaultSink());
        int end = this.listSinks.indexOf("index:", start + 1);
        String sinkData;
        if (end > -1) {
            sinkData = this.listSinks.substring(start, end);
        } else {
            sinkData = this.listSinks.substring(start);
        }

        Pattern regex = Pattern.compile("device.string = \"(.*)\"");
        Matcher match =  regex.matcher(sinkData);
        if (match.find()) {
            this.device = match.group(1).trim();
            this.sinkId = "bluez_sink." + device.replaceAll(":", "_");
            this.micId = "bluez_source." + device.replaceAll(":", "_");
        }

        regex = Pattern.compile("base volume: (\\d+) / (\\d+)% /.*");
        match =  regex.matcher(sinkData);
        if (match.find()) {
            this.baseVolume = Integer.parseInt(match.group(1).trim());
            this.baseVolumePercent = Integer.parseInt(match.group(2).trim());
        }
    }

    public String getDefaultSink() {
        if (this.listSinks.equals("")) {
            return "";
        }
        String retVal = "";
        Pattern regex = Pattern.compile("\\* index: (\\d+)");
        Matcher match =  regex.matcher(this.listSinks);
        if (match.find()) {
            retVal = match.group(1);
        }

        return retVal;
    }

    public void setCardProfile(String profile) {
        CmdRunner.runCommand("pacmd set-card-profile " + this.cardId + " " + profile);
    }

    public void setSinkVolume(int volume) {
        CmdRunner.runCommand("pacmd set-sink-volume " + this.sinkId + " " + String.valueOf(volume));
    }

    public void setMicVolume(int volume) {
        CmdRunner.runCommand("pacmd set-source-volume " + this.micId + " " + String.valueOf(volume));
    }

    public void setMicMute(boolean mute) {
        CmdRunner.runCommand("pacmd set-source-mute " + this.micId + " " + (mute ? "1" : "0") );
    }

    public void setAsDefaultMic() {
        CmdRunner.runCommand("pacmd set-default-source " + this.micId);
    }

    public void setAsDefaultSink() {
        CmdRunner.runCommand("pacmd set-default-sink " + this.sinkId);
    }

    public String getDevice() {
        return this.device;
    }

    public String getProfile() {
        return this.profile;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return this.cardName;
    }

    public String getSinkId() {
        return this.sinkId;
    }

    public void setSinkId(String sinkId) {
        this.sinkId = sinkId;
    }

    public String getMicId() {
        return this.micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }

    public int getBaseVolume() {
        return this.baseVolume;
    }

    public int getBaseVolumePercent() {
        return this.baseVolumePercent;
    }
}
