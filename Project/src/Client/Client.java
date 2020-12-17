package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rappresenta il ruolo Client dell'applicazione. Si occupa di "creare" i
 * pacchetti di byte e di spedirli al Server. Genera una statistica
 * significativa sulle velocità di trasmissione.
 *
 * @version 15.10.2020
 * @author Samuel
 */
public class Client extends Thread implements Runnable{

    /**
     * Il socket del client per poter mettersi in comunicazione con il server.
     */
    private Socket clientSocket;

    /**
     * Il canale output per mandare messaggi al server.
     */
    private PrintWriter out;

    /**
     * Il canale input per ricevere messaggi dal server.
     */
    private BufferedReader in;
    
    /**
     * La porta sulla quale il server è in ascolto.
     */
    private int svPort;
    
    /**
     * Il nome host del server.
     */
    private String svHost;

    /**
     * L'array di byte da spedire al Server. Le sue dimensioni possono essere
     * definite dall'utente.
     */
    private byte[] pack;

    /**
     * È la dimensione dell'array pack.
     */
    private int dim;
    
    /**
     * Il numero di pacchetti di byte da mandare al server.
     */
    private int connection;
    
    /**
     * Istanzia un oggetto client.
     */
    public Client(){
        connection = 1;
    }
    
    /**
     * Istanzia un oggetto client.
     * 
     * @param client il socket da impostare per la comunicazione con il server
     */
    public Client(Socket client){
        this.clientSocket = client;
        connection = 1;
    }

    /**
     * Istanzia un oggetto client.
     *
     * @param dim la dimensione dell'array pack
     * @param socket il socket del client per poter mettersi in comunicazione
     * con il server
     */
    public Client(int dim, Socket socket) {
        if (dim >= 1) {
            this.dim = dim;
        } else {
            this.dim = 10;
        }
        pack = new byte[this.dim];
        this.clientSocket = socket;
    }
    
    /**
     * Imposta il numero di pacchetti da mandare al server.
     * 
     * @param num il numero di pacchetti da mandare al server
     */
    public void setConnection(int num){
        if(num > 0){
            connection = num;
        }
    }
    
    /**
     * Ritorna il numero di pacchetti da mandare al server.
     * 
     * @return il numero di pacchetti di byte da mandare al server
     */
    public int getConnection(){
        return connection;
    }
    
    /**
     * Imposta la porta su cui il server è in ascolto.
     * 
     * @param port la porta sulla quale il server è in ascolto
     */
    public void setSvPort(int port){
        svPort = port;
    }
    
    /**
     * Ritorna la porta sulla quale il server è in ascolto.
     */
    public int getSvPort(){
        return svPort;
    }
    
    /**
     * Imposta il nome host del server.
     * 
     * @param host il nome host del server
     */
    public void setSvHost(String host){
        svHost = host;
    }
    
    /**
     * Ritorna il nome host del server.
     */
    public String getSvHost(){
        return svHost;
    }
    
    /**
     * Ritorna il pacchetto di byte da mandare al server.
     */
    public byte[] getPack(){
        return pack;
    }
    
    /**
     * Getter della dimensione dell'array pack.
     *
     * @return la dimensione dell'array pack
     */
    public int getDim() {
        return dim;
    }
    
    /**
     * Imposta il pacchetto di byte da mandare al server.
     */
    public void setPack(){
        pack = new byte[this.dim];
        for(int i = 0; i < pack.length; i++){
            pack[i] = (byte)1;
        }
    }

    /**
     * Setta la dimensione della array pack.
     *
     * @param dim la dimensione dell'array pack
     */
    public void setDim(int dim) {
        if (dim >= 1) {
            this.dim = dim;
        } else {
            this.dim = 10;
        }
    }

    /**
     * Permette la comunicazione tra client e server.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                out.println(inputLine);
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
