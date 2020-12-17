package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * È un'estensione della classe Server e permette l'esecuzione di più processi
 * Client paralleli.
 *
 * @version 15.10.2020
 * @author Samuel
 */
public class Multi implements Runnable {
    
    /**
     * Il socket utile per il multithreading tra client e server.
     */
    private Socket multiSocket;

    /**
     * È il costruttore della classe. Istanzia un oggetto MultiClient.
     */
    public Multi(Socket clientSocket) {
        this.multiSocket = clientSocket;
    }

    /**
     * È utile per eseguire più processi Client alla volta.
     */
    public void run(){
        int connect;
        ObjectInputStream ois;
        try{
            ois = new ObjectInputStream(multiSocket.getInputStream());
            connect = (int) ois.readObject();

            for (int i = 0; i < connect; i++) {
                ObjectOutputStream oos = new ObjectOutputStream(multiSocket.getOutputStream());;
                long tI = System.nanoTime();
                Object message = ois.readObject();
                long tF = System.nanoTime();
                System.out.println("Messaggio ricevuto dal client "+ multiSocket.getRemoteSocketAddress()+": " + message);
                oos.writeObject("Grazie del pacchetto " + (i + 1) + ": " + message);
                if (i == connect) {
                    oos.close();
                }

            }
            ois.close();
        } catch (IOException ex) {
            System.out.println("Disconnessione da parte del client " + multiSocket.getRemoteSocketAddress());
        } catch (ClassNotFoundException ex) {
            System.out.println("ERRORE");
        }
    }
}
