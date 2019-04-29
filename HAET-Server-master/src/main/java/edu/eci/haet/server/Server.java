/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.haet.server;

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
// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.ArrayList;

// Server class 
public class Server {
    static ArrayList<Thread> hilos = new ArrayList<>();
    
    /*static public Boolean getHiloActual(){
        if(hilos.isEmpty()){
            return false;
        }else{
            System.out.println("Actual2> "+Thread.currentThread());
            System.out.println("hilo: "+hilos.get(hilos.size()-1));
            return Thread.currentThread().isInterrupted();
        }
        
    }*/

    public static void main(String[] args) throws IOException {
        // server is listening on port 1313 
        ServerSocket server = new ServerSocket(1313);
        boolean running = false;
        
        

        // running infinite loop for getting 
        // client request 
        while (true) {
            if (!running) {
                System.out.println(">> SERVIDOR EJECUTADO...");
                running = true;
            }
            int client=hilos.size();

            Socket s = null;

            try {
                // socket object to receive incoming client requests
                
                
                s = server.accept();
                //if(s.isConnected() && !hilos.isEmpty()){
                    //System.out.println("Actual> "+Thread.currentThread());
                    //Thread.currentThread().interrupt();
                    //System.out.println("estado: "+Thread.currentThread().isInterrupted());
                //}
                System.out.println(">> CLIENTE "+ client+" CONECTADO... " );

                // obtaining input and out streams 
                DataInputStream in ;
                DataOutputStream out ;
                in = new DataInputStream(new BufferedInputStream(s.getInputStream())); //leer mensajes Clinete
                out = new DataOutputStream(s.getOutputStream());
                //System.out.println("NUEVA SESION CREADA PARA " + s);

                // create a new thread object 
                Thread t = new ClientHandler(s, in, out,client);
                hilos.add(t);
                // Invoking the start() method 
                t.start();
                t.join();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }
        }
    }
}
