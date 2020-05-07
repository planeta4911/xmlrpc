package org.apache.xmlrpc.demo.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeApp extends Application{

    XmlRpcClient client = new XmlRpcClient();

    private Text symbol = new Text();

    public String sym = new String();

    private boolean turn = false;
    private boolean playable = true;
    private List<Combo> comboList = new ArrayList<>();
    private Tile[][] field = new Tile[3][3];

    private Parent createParent(){
        Pane rootPane = new Pane();
        rootPane.setPrefSize(600,600);

        for(int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                Tile tile = new Tile();
                tile.setTranslateX(j*200);
                tile.setTranslateY(i*200);
                rootPane.getChildren().add(tile);
                field[j][i] = tile;
            }
        }

        for(int i = 0; i<3; i++){
            comboList.add(new Combo(field[0][i], field[1][i], field[2][i]));
            comboList.add(new Combo(field[i][0], field[i][1], field[i][2]));
        }
        comboList.add(new Combo(field[0][0], field[1][1], field[2][2]));
        comboList.add(new Combo(field[0][2], field[1][1], field[2][0]));

        return rootPane;
    }

    public void checkState() {
        for (Combo combo : comboList) {
            if(combo.isComplete()){
                playable = false;
                break;
            }
        }
    }

    private class Combo{
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



    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(createParent()));
        stage.show();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://127.0.0.1:8765/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);



        // use Commons HttpClient as transport
        client.setTransportFactory(
                new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);
        sym = (String) client.execute("Example.init",new Object[]{});
        System.out.println(sym);
        if(sym.equals("X")){
            turn = false;
        } else if(sym.equals("O")){

            while (turn){
                turn = (boolean) client.execute("Example.getTurn", new Object[]{});
            }
            for(int i=0; i<3; i++){
                for(int j=0; j<3; j++){
                    String tmpfield =
                            null;
                    try {
                        System.out.println(j + " " + i);
                        tmpfield = (String) client.execute("Example.returnField", new Object[]{j, i});
                    } catch (XmlRpcException e) {
                        e.printStackTrace();
                    }
                    field[j][i].symbol.setText(tmpfield);
                }
            }
        }

    }

    public static void main(String[] args){
        launch(args);
    }

    private class Tile extends StackPane{

        private Text symbol = new Text();

        public String getSymbolValue(){
            return symbol.getText();
        }


        public void drawSymbol(){
            symbol.setText(sym);
        }


        public Tile(){
            Rectangle border = new Rectangle(200,200);
            border.setFill(Color.GREENYELLOW);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().setAll(border, symbol);

            symbol.setFont(Font.font(72));

            setOnMouseClicked(mouseEvent -> {

                if(!playable){
                    return;
                }

                if(mouseEvent.getButton() == MouseButton.PRIMARY){
                    if(sym.equals("X")){
                        try {
                            turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                        } catch (XmlRpcException e) {
                            e.printStackTrace();
                        }
                        if(!turn){
                            drawSymbol();
                            String[] tmpfield = new String[9];
                            for(int i=0; i<3; i++){
                                for(int j=0; j<3; j++){
                                    tmpfield[3*j+i]=field[j][i].symbol.getText();
                                }
                            }
                            Object[] par = tmpfield;
                            try {
                                boolean e = (boolean) client.execute("Example.setField", par);
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            try {
                                boolean e = (boolean) client.execute("Example.changeTurn",new Object[]{true});
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            checkState();

                        } else {
                            while (turn){
                                try {
                                    turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                            }
                            for(int i=0; i<3; i++){
                                for(int j=0; j<3; j++){
                                    String tmpfield =
                                            null;
                                    try {
                                        tmpfield = (String) client.execute("Example.returnField", new Object[]{j, i});
                                    } catch (XmlRpcException e) {
                                        e.printStackTrace();
                                    }
                                    field[j][i].symbol.setText(tmpfield);
                                }
                            }
                        }
                    } else if (sym.equals("O")){
                        try {
                            turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                        } catch (XmlRpcException e) {
                            e.printStackTrace();
                        }
                        if(turn){
                            drawSymbol();
                            String[] tmpfield = new String[9];
                            for(int i=0; i<3; i++){
                                for(int j=0; j<3; j++){
                                    tmpfield[3*j+i]=field[j][i].symbol.getText();
                                }
                            }
                            Object[] par = tmpfield;
                            try {
                                boolean e = (boolean) client.execute("Example.setField", par);
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            try {
                                boolean e = (boolean) client.execute("Example.changeTurn",new Object[]{true});
                            } catch (XmlRpcException e) {
                                e.printStackTrace();
                            }
                            checkState();

                        } else {
                            while (!turn){
                                try {
                                    turn = (boolean) client.execute("Example.getTurn", new Object[]{true});
                                } catch (XmlRpcException e) {
                                    e.printStackTrace();
                                }
                            }
                            for(int i=0; i<3; i++){
                                for(int j=0; j<3; j++){
                                    String tmpfield =
                                            null;
                                    try {
                                        tmpfield = (String) client.execute("Example.returnField", new Object[]{j, i});
                                    } catch (XmlRpcException e) {
                                        e.printStackTrace();
                                    }
                                    field[j][i].symbol.setText(tmpfield);
                                }
                            }
                        }
                    }
                }
            });

        }

    }

}
