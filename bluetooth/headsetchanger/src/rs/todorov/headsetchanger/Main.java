package rs.todorov.headsetchanger;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BluetoothDevice bt = new BluetoothDevice();
        String switchTo = "";

        // Pre-populate profiles with known WH-1000MX2 profiles
        List<String> profiles = new ArrayList<>();
        profiles.add("headset_head_unit");
        profiles.add("a2dp_sink");

        bt.setup();

        if (args.length > 0) {
            for (String arg: args) {
                if (arg.equals("unmute")) {
                    System.out.print("Unmuting mic ... ");
                    bt.setMicMute(false);
                    System.out.print("done.");
                    return;
                }
                if (arg.equals("mute")) {
                    System.out.print("Muting mic ... ");
                    bt.setMicMute(true);
                    System.out.print("done.");
                    return;
                }
                if (arg.equals("off")) { // We want A2DP
                    doA2DP(bt);
                    return;
                }
                if (arg.equals("on")) { // We want headset
                    doHeadset(bt);
                    return;
                }
            }
            return;
        }

        // Alternate between profiles
        for (String profile: profiles) {
            if (!profile.equals(bt.getProfile())) {
                switchTo = profile;
                break;
            }
        }

        // Fallback option
        if (switchTo.equals("")) {
            switchTo = "a2dp_sink";
        }

        if (switchTo.equals("a2dp_sink")) {
            doA2DP(bt);
        }
        if (switchTo.equals("headset_head_unit")) {
            doHeadset(bt);
        }

    }

    private static void doA2DP(BluetoothDevice bt) {
        String switchTo = "a2dp_sink";
        System.out.print("Switching " + bt.getDescription() + " to <" + switchTo + "> profile ... ");
        bt.setCardProfile(switchTo);
        System.out.println("done.");
        System.out.print("Setting volume ... ");
        bt.setSinkVolume(65535);
        System.out.println("done.");
        System.out.print("Setting sink [" + bt.getSinkId() + "] as default ... ");
        bt.setAsDefaultSink();
        System.out.println("done.");
    }

    private static void doHeadset(BluetoothDevice bt) {
        String switchTo = "headset_head_unit";
        System.out.print("Switching " + bt.getDescription() + " to <" + switchTo + "> profile ... ");
        bt.setCardProfile(switchTo);
        System.out.println("done.");
        System.out.print("Setting volume ... ");
        bt.setMicVolume(65535);
        bt.setSinkVolume(32767);
        System.out.print("muting mic ... ");
        bt.setMicMute(true);
        System.out.println("done.");
        System.out.print("Setting sink [" + bt.getSinkId() + "] as default ... ");
        bt.setAsDefaultSink();
        System.out.println("done.");
    }
}
