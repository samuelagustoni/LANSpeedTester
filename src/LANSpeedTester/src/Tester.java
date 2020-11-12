
import Server.Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Samuel
 */
public class Tester {

    private static Server sv;

    public static final String ANSI_RED = "\u001B[31m";

    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_BG_RED = "\u001B[41m";

    public static void main(String[] args) throws IOException, InterruptedException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(ANSI_BLUE + "LANSpeedTester ti da il benvenuto!" + ANSI_RESET);
        boolean flag = true;            //-----------caricamento-----------
        /*for(int i = 0; i< 11; i++){
            System.out.print(ANSI_RED+"-"+ANSI_RESET);
            Thread.sleep(250);
        }
        System.out.print(ANSI_GREEN+"c"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"a"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"r"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"i"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"c"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"a"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"m"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"e"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"n"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"t"+ANSI_RESET);
        Thread.sleep(250);
        System.out.print(ANSI_GREEN+"o"+ANSI_RESET);
        Thread.sleep(250);
        for(int i = 0; i< 11; i++){
            System.out.print(ANSI_RED+"-"+ANSI_RESET);
            Thread.sleep(250);
        }
        System.out.println(ANSI_RED+"-"+ANSI_RESET);*/

        while (flag) {
            System.out.println("Ti trovi sulla macchina server o sulla macchina client? (s/c):");
            String s1 = br.readLine();
            if (s1.equals("s")) {
                System.out.println(ANSI_GREEN + "Hai selezionato il server!" + ANSI_RESET);
                sv = new Server();
                int[] ports = new int[65535 - 1024];
                int zeroCounter = 0;
                System.out.print("Cercando le porte disponibili attendere...");
                for (int i = 1024; i < 65535; i++) {
                    try {
                        ServerSocket connect = new ServerSocket(i);
                        ports[i - 1024] = i;
                        connect.close();
                    } catch (BindException ce) {
                        ports[i - 1024] = 0;
                        zeroCounter++;
                    }

                }
                int[] dispoPorts = new int[ports.length - zeroCounter];
                int counter = 0;
                for (int i = 0; i < ports.length; i++) {
                    if (ports[i] != 0) {
                        dispoPorts[counter] = ports[i];
                        counter++;
                    }
                }
                sv.setPorts(dispoPorts);
                System.out.println("Fatto!");
                boolean flag2 = true;
                while (flag2) {
                    System.out.print("Vuoi inserire manualmente la porta su cui mettere il server in ascolto?(y/n):");
                    String s2 = br.readLine();
                    if (s2.equals("y")) {
                        boolean flag3 = true;
                        while (flag3) {
                            System.out.print("Inserire la porta desiderata:");
                            String s3 = br.readLine();
                            try {
                                int num = Integer.parseInt(s3);
                                sv.setPort(num);
                                if(sv.getPort() == num){
                                    flag3 = false;
                                }
                                
                            } catch (NumberFormatException nfe) {
                                System.out.println("ERRORE: inserire un valore valido!");
                            }
                        }
                        flag2 = false;
                    } else if (s2.equals("n")) {
                        sv.setPort(ports[0]);
                        flag2 = false;
                    } else {
                        System.out.println("ERRORE: non hai selezionato una tra le possibilità disponibili (y/n)! Riprovare!");
                    }
                }
                ServerSocket svSocket = new ServerSocket(sv.getPort());
                InetAddress host = InetAddress.getLocalHost();
                System.out.println("Il server è in ascolto sulla porta: " + sv.getPort() + ".\nPer procedere con la configurazione sul client: \n"
                        + "1) Avvia l'applicazione su un'altra macchina\n2) Imposta il nome host seguente: " + host.getHostName()
                        + "\n3) Imposta la porta seguente: " + sv.getPort() + "\n4) Imposta dimensione e numero dei pacchetti di byte da mandare\n5) Attendi la risposta del server\n"
                        + "Aspettando...");
                String message;
                try ( Socket socket = svSocket.accept()) {
                    long tI = System.currentTimeMillis();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    message = (String) ois.readObject();
                    System.out.println("Messaggio ricevuto: " + message);
                    System.out.println("Tempo impiegato per la ricezione " + (System.currentTimeMillis() - tI) + " ms");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject("Ciao client " + message);
                    ois.close();
                    oos.close();
                } catch (ClassNotFoundException cnfe) {

                }
                System.out.println("Shutting down Socket server!!");
                svSocket.close();

                flag = false;
            } else if (s1.equals("c")) {
                System.out.println(ANSI_GREEN + "Hai selezionato il client! \n" + ANSI_RESET + ANSI_BG_RED + ANSI_WHITE
                        + "Assicurati di aver già avviato e configurato il software su una macchina server in precedenza, " + ANSI_RESET + ANSI_BG_RED + ANSI_WHITE
                        + "\naltrimenti la comunicazione non avrà successo!" + ANSI_RESET);
                boolean flag2 = true;
                while (flag2) {
                    System.out.print("Inserisci il nome host del server:");
                    String host = br.readLine();
                    System.out.print("Inserisci la porta sulla quale il server è in ascolto:");
                    String s3 = br.readLine();
                    int port = 0;
                    try {
                        port = Integer.parseInt(s3);
                        Socket socket = null;
                        ObjectOutputStream oos = null;
                        ObjectInputStream ois = null;
                        long tI = System.currentTimeMillis();
                        socket = new Socket(host, port);
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("Inviando la richiesta al server");
                        oos.writeObject("Samuel");
                        ois = new ObjectInputStream(socket.getInputStream());
                        String message = (String) ois.readObject();
                        System.out.println("Messaggio SocketServer: " + message);
                        ois.close();
                        oos.close();
                        System.out.println("Tempo impiegato per l'invio e la ricezione del messaggio: " + (System.currentTimeMillis() - tI) + " ms");
                    } catch (NumberFormatException nfe) {

                    } catch (ClassNotFoundException cnfe) {

                    }
                    flag2 = false;
                }
                flag = false;
            } else {
                System.out.println("ERRORE: non hai selezionato uno tra i ruoli dispponibili (s/c)! Riprovare!");
            }
        }

    }
}
