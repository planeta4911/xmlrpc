package org.apache.xmlrpc.demo.client;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeApp extends Application{

    static XmlRpcClient client = new XmlRpcClient();

    GameParameters gameParameters = new GameParameters();

    public void setClient(XmlRpcClient cl) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8765/xmlrpc"));

        config.setReplyTimeout(50000);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(50000);
        cl.setTransportFactory(
                new XmlRpcCommonsTransportFactory(cl));
        cl.setConfig(config);
    }

    private Parent createParent(){
        Pane rootPane = new Pane();
        rootPane.setPrefSize(600,600);

        for(int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                Tile tile = new Tile();
                tile.setTranslateX(j*200);
                tile.setTranslateY(i*200);
                rootPane.getChildren().add(tile);
                gameParameters.setTileInField(j,i,tile);
            }
        }



        return rootPane;
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(createParent()));
        stage.show();
        setClient(client);
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 3; j++) {
                gameParameters.setClickTile(j, i, client);
            }
        }
        gameParameters.setSym((String) client.execute("Example.init",new Object[]{}));
        if(gameParameters.getSym().equals("X")){
            gameParameters.setTurn(false);
        } else if(gameParameters.getSym().equals("O")){
            gameParameters.setTurn(false);
            while (!gameParameters.isTurn()){
                gameParameters.setTurn((boolean) client.execute("Example.getTurn", new Object[]{true}));
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
                    gameParameters.setSymbolInTile(j, i, tmpfield);
                }
            }
            gameParameters.checkState();
        }

    }

    public static void main(String[] args){
        launch(args);
    }



}
