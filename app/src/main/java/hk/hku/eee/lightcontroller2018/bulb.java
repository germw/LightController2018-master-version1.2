package hk.hku.eee.lightcontroller2018;

import java.util.ArrayList;

public class bulb {

    private ArrayList<String> MAClist;
    private String displayName;
    private String SystemName;
    private boolean status;
    private String intensity;
    private String color;


    public bulb(){
        MAClist = new ArrayList<String>();
    }

    public void addMAC(String mac) {
        //insert to list
        MAClist.add(mac);
    }

    public void removeMAC(String mac){

        if(!MAClist.isEmpty()){
            if(mac.equals("All")){
                MAClist.clear();
            }else{
                MAClist.remove(mac);
            }
        }
    }


    public void setMAClist(ArrayList<String> MAClist) {
        this.MAClist = MAClist;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSystemName() {
        return SystemName;
    }

    public void setSystemName(String systemName) {
        SystemName = systemName;
    }
}
