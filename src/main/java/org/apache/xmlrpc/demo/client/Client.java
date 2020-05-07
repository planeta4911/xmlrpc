package org.apache.xmlrpc.demo.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;

public class Client {
    public static void main(String[] args) throws Exception {
        // create configuration
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://127.0.0.1:8765/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();

        // use Commons HttpClient as transport
        client.setTransportFactory(
                new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);

        // make the a regular call
        Object[] params = new Object[]
                { new Integer(2), new Integer(3) };
        Integer result = (Integer) client.execute("Calculator.add", params);
        System.out.println("2 + 3 = " + result);

        params = new Object[] {new Integer(5), new Integer(3)};
        result = (Integer) client.execute("Calculator.subtract", params);
        System.out.println("5 - 3 = " + result);


        String string = "kek";
        params = new String[]{string};
        System.out.println((boolean) client.execute("Calculator.check", params));



    }
}
