package com.pci.beacon;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.pci.service.util.PCILog;

import java.util.Arrays;

public class PCIAdvertise {
    private final String TVCode = "7584";
    private static PCIAdvertise PCIInstance = new PCIAdvertise();
    BeaconParser beaconParser;
    BeaconTransmitter beaconTransmitter;
    BluetoothAdapter mBlutoothAdapter;

    private PCIAdvertise() { }
    public static synchronized PCIAdvertise getInstance(){
        if(PCIInstance == null){
            PCIInstance = new PCIAdvertise(); }
        return PCIInstance; }


    public void start(Context context, String YN, String adid, String partercode) {
        beaconParser = new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(context, beaconParser);
        mBlutoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String minorcode = partercode;
        if (mBlutoothAdapter.isEnabled()) {

            Beacon beacon = new Beacon.Builder()
                    .setId1(adid)
                    .setId2(TVCode)
                    .setId3(minorcode)
                    .setManufacturer(0x0118)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[]{0l}))
                    .setBluetoothName("TV")
                    .build();

            if (YN == "start" && !beaconTransmitter.isStarted()) {
                beaconTransmitter.startAdvertising(beacon);

            } else {
                PCILog.d("Already Beacon is started !!!");
                //beaconTransmitter.stopAdvertising();
            }
        } else {
            PCILog.d("BLE State is disable !!!");
        }
    }
    public boolean isStarted(){

        try {
            boolean startResult = beaconTransmitter.isStarted();
            if (startResult == true) return true;
            else if (startResult == false) return false;
        }catch (Exception e) {
            PCILog.d(" Currently Beacon Advertising is not working !!" );
        }
        return false;
    }

    public void finish(){
        try {
            beaconTransmitter.stopAdvertising();
        }catch (Exception e){
            PCILog.d(" Already Beacon Advertising is finished !!" );
        }
    }


}
