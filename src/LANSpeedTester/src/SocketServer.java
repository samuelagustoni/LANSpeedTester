
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Samuel Agustoni
 */
public class SocketServer {

    private static ServerSocket server;
    private static int port = 9876;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);
        System.out.println("Aspettando la richiesta del client:");
        String message;
        try ( Socket socket = server.accept()) {
            long tI = System.currentTimeMillis();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            message = (String) ois.readObject();
            System.out.println("Messaggio ricevuto: " + message);
            System.out.println("Tempo impiegato per la ricezione " + (System.currentTimeMillis() - tI) + " ms");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Ciao client " + message);
            ois.close();
            oos.close();
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }
}
