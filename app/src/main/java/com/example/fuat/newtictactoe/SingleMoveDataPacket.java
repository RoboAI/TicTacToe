package com.example.fuat.newtictactoe;

public class SingleMoveDataPacket {
    public int x;
    public int y;
    public String src_ip;//stores where the packet came from
    public String dst_ip;//used to send the packets to..

    public SingleMoveDataPacket(int x, int y, String dst_ip){
        this.x = x;
        this.y = y;
        this.dst_ip = dst_ip;
    }
}
