package org.apache.xmlrpc.demo;

import org.apache.xmlrpc.demo.webserver.Server;

import java.util.ArrayList;
import java.util.List;

public class Example {

    public String init(){
        if (!Server.first){
            Server.first = true;
            Server.turn = false;
            return "X";
        } else if (!Server.second){
            Server.second = true;
            return "O";
        } else {
            return "false";
        }
    }

    public String returnField( int x, int y){
        return Server.field[x][y];
    }

    public boolean setField(String tmpField1, String tmpField2, String tmpField3,String tmpField4,
                            String tmpField5, String tmpField6, String tmpField7,String tmpField8,
                            String tmpField9){
        Server.field[0][0] = tmpField1;
        Server.field[1][0] = tmpField4;
        Server.field[2][0] = tmpField7;
        Server.field[0][1] = tmpField2;
        Server.field[1][1] = tmpField5;
        Server.field[2][1] = tmpField8;
        Server.field[0][2] = tmpField3;
        Server.field[1][2] = tmpField6;
        Server.field[2][2] = tmpField9;
        return true;
    }

    public boolean changeTurn(boolean b){
        Server.turn=!Server.turn;
        return true;
    }

    public boolean getTurn( boolean b){
        return Server.turn;
    }



}
