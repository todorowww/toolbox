# Toolbox

### Script toolbox

This repo will contain various scripts I use on a daily basis, that make my life on Linux a little bit easier. I suspect the list of scripts here will grow.


#### Bluetooth

If you own a pair of SONY WH-1000XM2 (maybe goes for XM3 as well), and use them on Linux, you're most likely aware of the pain and suffering they tend to cause. No matter what I tried, they just don't seem to want to cooperate. Scripts in this folder are used for the sole purpose of making my life easier when it comes to connectinng my headphones to my computer.

##### Prerequisites
Installed bluetoothctl, expect, successfully paired your headphones with your computer, set them to trusted device.
You need to get your headphones HW address, in order to use these scripts. Naturally, you need to edit them as well, and input your numbers in appropriate places. 

To obtain headphones HW address, run `bluetoothctl` as root, and run `devices` command. This gives you the list of paired BT devices. You're looking for something like:
```
[bluetooth]# devices
Device A0:B1:C2:D3:EE:44 WH-1000XM2
```
You need the `A0:B1:C2:D3:EE:44` part. For `hs` script, replace colon with underscore, making it `A0_B1_C2_D3_EE_44`

#### btc
Expect script, uses `bluetoothctl`. **Must be run as root.** Performs disconnect/connect routine, when your headphones automatically connect to the computer. Disconnect/connect is needed in order to enable A2DP profile to be used.

#### hs
Bash script. **Run as regular user, NOT root.** Used to quickly switch between A2DP and headset profiles. Because, why would PulseAudio do that automagically (actually it does sometimes, but it's unreliable, and sometimes completely breaks profiles, and requires a tangled web of steps to repair without restarting the machine.)

- Run `hs` to switch to A2DP profile
- Run `hs on` to switch to headset profile. Automatically mutes the microphone upon switch.
- Run `hs mute` or `hs unmute` to mute/unmute your headset microphone
- Run `hs vol x` to set  microphone volume for your headset. X is in percent, and default is 50%.

#### hs.jar

Java program to perform the same action as `hs` BASH script, only better and more reliably. Sources for the compiled version are in `bluetooth/headsetchanger`directory. I made this as an exercise while learning Java.

- Run with no parameters to alternate between A2DP and Headset profiles
- Run with `off` parameter to force A2DP profile
- Run with `on` parameter to force Headset profile
- Run with `mute` or `unmute` parameter to mute/unmute your headset microphone

Unlike the `hs` BASH script, `hs.jar` sets headphone volume to max, and microphone volume to 50%, by default. For now, it's impossible to change the volume through this program. I plan to add it in the future.