package org.apache.xmlrpc.demo.client;

import javafx.scene.input.MouseButton;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.util.ArrayList;
import java.util.List;

public class GameParameters {

    private boolean turn = false;
    private boolean playable = true;
    private static Tile[][] field = new Tile[3][3];
    private boolean clicked = false;
    private String sym;

    public GameParameters(){

    }

    public void checkState() {
        List<Combo> comboList = new ArrayList<>();
        for(int i = 0; i<3; i++){
            comboList.add(new Combo(field[0][i], field[1][i], field[2][i]));
            comboList.add(new Combo(field[i][0], field[i][1], field[i][2]));
        }
        comboList.add(new Combo(field[0][0], field[1][1], field[2][2]));
        comboList.add(new Combo(field[0][2], field[1][1], field[2][0]));
        for (Combo combo : comboList) {
            if(combo.isComplete()){
                this.setPlayable(false);
                break;
            }
        }
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }


    public static Tile[][] getField() {
        return field;
    }

    public boolean isPlayable() {
        return playable;
    }

    public boolean isTurn() {
        return turn;
    }


    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setTileInField(int x, int y, Tile t){
        field[x][y] = t;
    }

    public void setClickTile(int x, int y, XmlRpcClient client){
        field[x][y].setOnMouseClicked(mouseEvent -> {

                    if(!this.isPlayable()){
                        System.out.println("GAME OVER!!!");
                        return;
                    }

                    if(mouseEvent.getButton() == MouseButton.PRIMARY){
                        if(this.getSym().equals("X")){
                            try {
                                turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            if(!turn){
                                if(!clicked){
                                    field[x][y].drawSymbol(this.getSym());
                                    clicked=true;
                                    return;
                                }

                                String[] tmpfield = new String[9];
                                for(int i=0; i<3; i++){
                                    for(int j=0; j<3; j++){
                                        tmpfield[3*j+i]=this.getTileInField(j, i).getSymbolValue();
                                    }
                                }
                                Object[] par = tmpfield;
                                try {
                                    boolean e = (boolean) client.execute("Example.setField", par);
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    turn = (boolean) client.execute("Example.changeTurn",new Object[]{true});
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                                checkState();
                                if(!this.isPlayable()){
                                    System.out.println("GAME OVER!!!");
                                    return;
                                }


                                while (turn){
                                    try {
                                        turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                                    } catch (XmlRpcException e) {
                                        e.printStackTrace();
                                    }
                                }
                                for(int i=0; i<3; i++){
                                    for(int j=0; j<3; j++){
                                        String tmp =
                                                null;
                                        try {
                                            tmp = (String) client.execute("Example.returnField", new Object[]{j, i});
                                        } catch (XmlRpcException e) {
                                            e.printStackTrace();
                                        }
                                        this.setSymbolInTile(j, i, tmp);
                                    }
                                }
                                checkState();
                                clicked=false;
                            }
                        } else if (this.getSym().equals("O")){
                            try {
                                turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            if(turn){
                                if(!clicked){
                                    field[x][y].drawSymbol(this.getSym());
                                    clicked=true;
                                    return;
                                }


                                String[] tmpfield = new String[9];
                                for(int i=0; i<3; i++){
                                    for(int j=0; j<3; j++){
                                        tmpfield[3*j+i]=this.getTileInField(j, i).getSymbolValue();
                                    }
                                }
                                Object[] par = tmpfield;
                                try {
                                    boolean e = (boolean) client.execute("Example.setField", par);
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    turn = (boolean) client.execute("Example.changeTurn",new Object[]{true});
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                                checkState();
                                if(!this.isPlayable()){
                                    System.out.println("GAME OVER!!!");
                                    return;
                                }


                                while (!turn){
                                    try {
                                        turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                                    } catch (XmlRpcException e) {
                                        e.printStackTrace();
                                    }
                                }
                                for(int i=0; i<3; i++){
                                    for(int j=0; j<3; j++){
                                        String tmp =
                                                null;
                                        try {
                                            tmp = (String) client.execute("Example.returnField", new Object[]{j, i});
                                        } catch (XmlRpcException e) {
                                            e.printStackTrace();
                                        }
                                        this.setSymbolInTile(j, i, tmp);
                                    }
                                }
                                checkState();
                                clicked = false;

                            }
                        }
                    }
                });
    }

    public void setSymbolInTile(int x, int y, String sym){
        field[x][y].drawSymbol(sym);
    }

    public Tile getTileInField(int x, int y){
        return field[x][y];
    }


}
