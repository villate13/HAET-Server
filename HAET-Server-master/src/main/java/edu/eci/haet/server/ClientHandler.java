/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.haet.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.System.in;
import static java.lang.System.out;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author estudiante
 */
// ClientHandler class 
class ClientHandler extends Thread {

    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream in;
    final DataOutputStream out;
    DataInputStream input;
    String response;
    boolean connect;
    final Socket s;
    final int client;

    // Constructor 
    public ClientHandler(Socket s, DataInputStream in, DataOutputStream out, int client) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.connect = false;
        this.client = client;
    }

    @Override
    public void run() {
        String ipClient ="";
        input = new DataInputStream(System.in);//comandos del teermnal
        //Boolean actual = Server.getHiloActual();
        //while (!actual) {
        while (true) {
            try {
                

                // creating Date object
                if (!connect) {

                    //out.writeUTF("SERVIDOR CONECTADO");
                    System.out.println("---------------------------------");
                    Date date = new Date();
                    System.out.println("Dia: " + fordate.format(date) + " Hora: " + fortime.format(date));
                    out.writeUTF("hostname -I");
                    ipClient = in.readUTF();
                    System.out.println(">> Cliente#: " + client + " - IP: " + ipClient);
                    System.out.println("---------------------------------");

                    connect = true;
                }

                System.out.print("~$ ");
                String command = input.readLine();//escribir comando
                out.writeUTF(command);
                response = in.readUTF();
                System.out.println(response);

                if (response.equals("exit")) {
                    System.out.println(">> Client " + this.client + " with IP: "+ipClient);
                    System.out.println(">> Closing this connection.");
                    this.s.close();
                    System.out.println(">> Connection closed");
                    break;
                }


            } catch (IOException e) {
                try {
                    System.out.println(">> Client " + this.client + " with IP: " + ipClient);
                    System.out.println(">> Dont responsed.");
                    this.s.close();
                    System.out.println(">> Connection closed");
                    break;
                    //e.printStackTrace();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        try {
            // closing resources
            this.in.close();
            this.out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
