package Server;

import Client.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Rappresenta il ruolo Server dell'applicazione. Si occupa di gestire le porte
 * d'ascolto disponibili e il proprio stato.
 *
 * @version 15.10.2020
 * @author Samuel
 */
public class Server {
    
    /**
     * Il socket del server per poter mettersi in comunicazione con il client.
     */
    private ServerSocket serverSocket;
    
    /**
     * Contiene le porte libere utilizzabili dal server per mettersi in ascolto
     * e ricevere pacchetti di byte.
     */
    private int[] ports;

    /**
     * È lo stato del server. Se true il server è pronto e in ascolto, se false
     * non è pronto.
     */
    private boolean state;

    /**
     * La porta sulla quale mettere il server in ascolto.
     */
    private int port;
    
    public Server(){
        
    }
    
    /**
     * Istanzia un oggetto server con i valori definiti dai parametri.
     *
     * @param ports le porte libere per mettere in ascolto il server
     * @param state lo stato del server
     */
    public Server(int[] ports, boolean state) {
        this.ports = ports;
        this.state = state;
    }
    
    public void setPorts(int[] ports){
        this.ports = ports;
    }
    
    /**
     * Cerca le porte disponibili e le inserisce nell'attributo "ports".
     */
    public void getPorts() {

    }
    
    /**
     * Getter della porta settata.
     * 
     * @return la porta settata. 
     */
    public int getPort(){
        return port;
    }
    
    /**
     * Permette di impostare la porta sulla quale il server starà in ascolto.
     * 
     * @param port la porta sulla quale mettere il server in ascolto
     */
    public void setPort(int port) {

            for (int i = 0; i < ports.length; i++) {
                if (port == ports[i]) {
                    this.port = port;
                    i = ports.length;
                }
            }
            if(this.port == port){
                System.out.println("Porta settata correttamente: " + this.port);
            }else{
                System.out.println("Porta non disponibile: " + port);
                System.out.println("Ecco alcune porte disponibili: ");
                System.out.print("[");
                for(int i = 0; i < 10; i++){
                    if(i < 9){
                        System.out.print(ports[i] + ", ");
                    }else{
                        System.out.print(ports[i]);
                    }
                }
                System.out.println("]");
            }
          
    }
    
    /**
     * Crea il socket del Server, si mette in ascolto e attende le 
     * richieste Client.
     * 
     * @throws IOException 
     */
    public void start() throws IOException{
        serverSocket = new ServerSocket(port);
        while(true){
            new Client(10, serverSocket.accept()).start();
        }
    }
    
    /**
     * Chiude il socket del Server.
     * 
     * @throws IOException 
     */
    public void stop() throws IOException{
        serverSocket.close();
    }
    
    
    /**
     * Si occupa di riferire all'utente se il server è pronto e in ascolto.
     */
    public void getState(){
        if(state){
            System.out.println("Il server è pronto e in ascolto sulla porta " + port);
        }else{
            System.out.println("ERRORE: il server non è stato configurato correttamente, riprovare!");
        }
    }
    
    /**
     * Imposta lo stato sel server.
     * 
     * @param state è lo stato del server
     */
    private void setState(boolean state){
        this.state = state;
    }
}
