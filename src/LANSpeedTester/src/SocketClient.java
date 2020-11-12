
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Samuel Agustoni
 */
public class SocketClient {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        long tI = System.currentTimeMillis();
        socket = new Socket(host.getHostName(), 9876);
        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Inviando la richiesta al server");
        oos.writeObject("Samuel");
        ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();
        System.out.println("Messaggio SocketServer: " + message);
        ois.close();
        oos.close();
        System.out.println("Tempo impiegato per l'invio e la ricezione del messaggio: " + (System.currentTimeMillis() - tI) + " ms");
    }
}
