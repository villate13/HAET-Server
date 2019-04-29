/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.haet.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
// Java implementation for a client 
// Save file as Client.java 
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class 
public class Client {

    public static void main(String[] args) throws IOException {
        try {

            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost");

            // establish the connection with server port 5056 
            Socket s = new Socket(ip, 8500);

            // obtaining input and out streams 
            DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));//cleinte
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            DataInputStream input = new DataInputStream(System.in);//terminal

            // the following loop performs the exchange of 
            // information between client and client handler 
            String command = "";

            while (true) {
                try {
                    command = in.readUTF();

                    if (command.equals("exit")) {
                        out.writeUTF("exit");
                        s.close();                        
                        break;
                    }
                    
                    try {
                        Process process = Runtime.getRuntime().exec(command);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                        String line, x = "";
                        while ((line = reader.readLine()) != null) {
                            x += line + "\n";
                            //System.out.println("line " + line);
                        }
                        x = x.substring(0, x.length() - 1);

                        out.writeUTF(x);

                        process.waitFor();
                    } catch (Exception ex) {
                        out.writeUTF("Error, comand error");
                        //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (IOException i) {
                    out.writeUTF("Error, comand error");
                    //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, i);
                }
            }

            // closing resources 
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
