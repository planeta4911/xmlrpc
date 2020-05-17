package org.apache.xmlrpc.demo.client;

import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.xmlrpc.XmlRpcException;


public class Tile extends StackPane {

    private Text symbol = new Text();

    public String getSymbolValue(){
        return symbol.getText();
    }




    public void drawSymbol(String sym){
        symbol.setText(sym);
    }

    public int click = 0;


    public Tile(){
        Rectangle border = new Rectangle(200,200);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);

        setAlignment(Pos.CENTER);
        getChildren().setAll(border, symbol);

        symbol.setFont(Font.font(72));





    }

}
