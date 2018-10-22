/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybitcoinclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Stefan Toltesi
 */
public class MyBitcoinClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Scanner reader = new Scanner(System.in);
        String line = reader.nextLine();
        try {
            while (!line.trim().equals("exit")) {

                String[] words = line.split("\\s");
                switch (words[0]) {
                    case "balance":
                        client.checkBalance();
                        break;
                    case "newaddress":
                        client.createNewAddress();
                        break;
                    case "listunspent":
                        client.listUnspentTransactions();
                        break;
                    case "sendto":
                        if(words.length != 3){
                            System.err.println("You need to specify a recipients address and ammont of bitcoin to be send");
                        }else{
                            List<String> params = new ArrayList<>();
                            params.add(words[1]);
                            params.add(words[2]);
                            client.sendToAdress(params);
                        }
                        break;
                    default:
                        System.err.println("Unknown command");
                }

                line = reader.nextLine();

            }
        } catch (ParseException | IOException ex) {
            Logger.getLogger(MyBitcoinClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            client.closeConnection();
        }
    }
}
