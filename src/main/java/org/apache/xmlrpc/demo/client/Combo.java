package org.apache.xmlrpc.demo.client;

public class Combo{
    private Tile[] tiles;
    public Combo(Tile... tiles){
        this.tiles = tiles;
    }

    public boolean isComplete(){
        if(tiles[0].getSymbolValue().isEmpty()){
            return false;
        }
        return tiles[0].getSymbolValue().equals(tiles[1].getSymbolValue()) &&
                tiles[0].getSymbolValue().equals(tiles[2].getSymbolValue());
    }

}
