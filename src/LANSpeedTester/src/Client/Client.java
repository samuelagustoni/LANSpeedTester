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
public class Client extends Thread {

    /**
     * Il socket del client per poter mettersi in comunicazione con il server.
     */
    private Socket clientSocket;

    /**
     *
     */
    private PrintWriter out;

    /**
     *
     */
    private BufferedReader in;

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
     * Getter della dimensione dell'array pack.
     *
     * @return la dimensione dell'array pack
     */
    public int getDim() {
        return dim;
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

    /**
     * Mostra all'utente una statistica significativa delle velocità di
     * comunicazione tra client e server.
     */
    public void getReport() {

    }
}
