
import Client.Client;
import Client.Multi;
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
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe Tester è la classe principale che permette di lanciare il software. 
 * Legge gli input dell'utente e gestisce le classi utili per il client e le
 * clssi utili per il server.
 * 
 * @version 17.12.2020
 * @author Samuel
 */
public class Tester {
    
    /**
     * L'oggetto che farà da Server durante tutta l'esecuzione del programma.
     */
    private static Server sv;
    
    /**
     * Colore rosso per i caratteri sulla linea di comando. 
     */
    public static final String ANSI_RED = "\u001B[31m";
    
    /**
     * Colore verde per i caratteri sulla linea di comando. 
     */
    public static final String ANSI_GREEN = "\u001B[32m";
    
    /**
     * Colore blu per i caratteri sulla linea di comando. 
     */
    public static final String ANSI_BLUE = "\u001B[34m";
    
    /**
     * Colore bianco per i caratteri sulla linea di comando. 
     */
    public static final String ANSI_WHITE = "\u001B[37m";
    
    /**
     * Resetta il colore dei caratteri sulla linea di comando. 
     */
    public static final String ANSI_RESET = "\u001B[0m";
    
    /**
     * Colore rosso dello sfondo deii caratteri sulla linea di comando. 
     */
    public static final String ANSI_BG_RED = "\u001B[41m";
    
    /**
     * Oggetto utile a rendere il Server multithread.
     */
    private static Multi multiClient;
    
    /**
     * Oggetto utile per l'esecuzione di più processi contemporanei.
     */
    private static Thread thread;
    
    /**
     * Flag di un while.
     */
    private static boolean on = true;

    public static void main(String[] args) throws IOException, InterruptedException, BindException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(ANSI_BLUE + "LANSpeedTester ti da il benvenuto!" + ANSI_RESET);
            //System.out.println(ANSI_RED + "(Per uscire dall'applicazione digitare 'exit' in qualsiasi momento)" + ANSI_RESET);
            boolean flag = true;
            while (flag) {
                System.out.print("Ti trovi sulla macchina server o sulla macchina client? (s/c):");
                String s1 = br.readLine().replace(" ", "");
                if (s1.equals("s")) {
                    System.out.println(ANSI_GREEN + "Hai selezionato il server!" + ANSI_RESET);
                    sv = new Server();
                    System.out.print("Cercando le porte disponibili attendere...");
                    int[] ports = sv.findPorts();
                    sv.setPorts(ports);
                    System.out.println("Fatto!");
                    boolean flag2 = true;
                    while (flag2) {
                        System.out.print("Vuoi inserire manualmente la porta su cui mettere il server in ascolto?(y/n):");
                        String s2 = br.readLine().replace(" ", "");
                        if (s2.equals("y")) {
                            boolean flag3 = true;
                            while (flag3) {
                                System.out.print("Inserire la porta desiderata:");
                                String s3 = br.readLine().replace(" ", "");
                                try {
                                    int num = Integer.parseInt(s3);
                                    if (num < 1024 || num > 65535) {
                                        throw new NumberFormatException();
                                    }
                                    sv.setPort(num);
                                    if (sv.getPort() == num) {
                                        flag3 = false;
                                    }

                                } catch (NumberFormatException nfe) {
                                    System.out.println("\n" + ANSI_RED + "ERRORE: inserire un valore valido, compreso tra 1024 e 65535!"
                                            + ANSI_RESET + "\n");
                                }
                            }
                            flag2 = false;
                        } else if (s2.equals("n")) {
                            sv.setPort(ports[0]);
                            flag2 = false;
                        } else if (s2.toUpperCase().equals("EXIT")) {
                            System.out.println("\n" + ANSI_RED + "Shutting down LANSpeedTester!" + ANSI_RESET);
                            System.exit(0);
                        } else {
                            System.out.println("\n" + ANSI_RED + "ERRORE: non hai selezionato una tra le possibilità disponibili (y/n)! Riprovare!"
                                    + ANSI_RESET + "\n");
                        }
                    }
                    ServerSocket svSocket = new ServerSocket(sv.getPort());
                    InetAddress host = InetAddress.getLocalHost();
                    sv.getState();
                    boolean flagg = true;
                    while (flagg) {
                        System.out.println("\nPer procedere con la configurazione sul client: \n"
                                + "1) Avvia l'applicazione su un'altra macchina\n2) Imposta il nome host seguente: " + host.getHostName()
                                + "\n3) Imposta la porta seguente: " + sv.getPort()
                                + "\n4) Imposta dimensione e numero dei pacchetti di byte da mandare\n5) Attendi la risposta del server\n" + ANSI_BLUE
                                + "\nIL SERVER PUÒ ESSERE SPENTO IN QUALSIASI MOMENTO DIGITANDO 'EXIT'!" + ANSI_RESET
                                + "\nAspettando...\n");
                        Socket socket = null;
                        while (on) {
                            (new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        if (br.readLine().replace(" ", "").toUpperCase().equals("EXIT")) {
                                            System.out.println("\n" + ANSI_RED + "Shutting down Socket server!" + ANSI_RESET);
                                            svSocket.close();
                                            System.exit(0);
                                        } else {
                                            br.skip(br.readLine().length());
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            })).start();
                            socket = svSocket.accept();
                            multiClient = new Multi(socket);
                            thread = new Thread(multiClient);
                            thread.start();
                            System.out.println("Connessione da parte del client: " + socket.getRemoteSocketAddress());
                        }
                    }
                } else if (s1.equals("c")) {
                    System.out.println(ANSI_GREEN + "Hai selezionato il client! \n\n" + ANSI_RESET + ANSI_BG_RED + ANSI_WHITE
                            + "Assicurati di aver già avviato e configurato il software su una macchina server in precedenza, "
                            + ANSI_RESET + ANSI_BG_RED + ANSI_WHITE
                            + "\naltrimenti la comunicazione non avrà successo!" + ANSI_RESET + "\n");
                    boolean flag2 = true;
                    Client cl = new Client();
                    while (flag2) {
                        System.out.print("Inserisci il nome host del server:");
                        cl.setSvHost(br.readLine().replace(" ", ""));
                        System.out.print("Inserisci la porta sulla quale il server è in ascolto:");
                        String s3 = br.readLine().replace(" ", "");
                        int port = 0;
                        try {
                            cl.setSvPort(Integer.parseInt(s3));
                            if (cl.getSvPort() < 1024 || cl.getSvPort() > 65535) {
                                throw new NumberFormatException();
                            }
                            Socket socket = null;
                            ObjectOutputStream oos = null;
                            ObjectInputStream ois = null;
                            socket = new Socket(cl.getSvHost(), cl.getSvPort());
                            oos = new ObjectOutputStream(socket.getOutputStream());
                            System.out.println("\n" + ANSI_GREEN + "Connessione con il server riuscita!" + ANSI_RESET + "\n");
                            boolean flagConn = true;
                            while (flagConn) {
                                System.out.print("Inserisci il numero di pacchetti da mandare al server:");
                                String s = br.readLine().replace(" ", "");
                                try {
                                    cl.setConnection(Integer.parseInt(s));
                                    if (cl.getConnection() == Integer.parseInt(s)) {
                                        flagConn = false;
                                    } else {
                                        System.out.println(ANSI_RED + "ERRORE: minimo 1 pacchetto!" + ANSI_RESET);
                                    }
                                } catch (NumberFormatException nfe) {
                                    System.out.println(ANSI_RED + "ERRORE: inserire un valore valido!" + ANSI_RESET);
                                }

                            }
                            boolean flagBy = true;
                            while (flagBy) {
                                System.out.print("Inserisci la dimensione in bytes dei pacchetti da mandare al server:");
                                String dimB = br.readLine();
                                try {
                                    cl.setDim(Integer.parseInt(dimB));
                                    if (cl.getDim() == Integer.parseInt(dimB)) {
                                        flagBy = false;
                                    } else {
                                        System.out.println(ANSI_RED + "ERRORE: dimensione al minimo di 1 byte!" + ANSI_RESET);
                                    }
                                } catch (NumberFormatException nfe) {
                                    System.out.println(ANSI_RED + "ERRORE: inserire un valore valido!" + ANSI_RESET);
                                }
                            }

                            cl.setPack();
                            if (cl.getConnection() == 1) {
                                System.out.println("\nInviando il pacchetto di " + cl.getDim() + " bytes al server...");
                            } else {
                                System.out.println("\nInviando " + cl.getConnection() + " pacchetti di " + cl.getDim() + " bytes al server...");
                            }
                            oos.writeObject(cl.getConnection());
                            double[] times = new double[cl.getConnection()];
                            double avg = 0;
                            double sum = 0;
                            for (int i = 0; i < cl.getConnection(); i++) {
                                long tI = System.nanoTime();
                                oos.writeObject(cl.getPack());
                                ois = new ObjectInputStream(socket.getInputStream());
                                Object message = ois.readObject();
                                long tF = System.nanoTime();
                                times[i] = (double) ((tF - tI) / Math.pow(1000, 2));
                                avg += times[i];
                                System.out.println("Risposta SocketServer -> " + message + "\nTempo impiegato per l'invio e la ricezione: "
                                        + times[i] + " ms\n");
                                if (i == cl.getConnection()) {
                                    ois.close();
                                }
                            }
                            double max = times[0];
                            double min = times[0];
                            for (int i = 0; i < times.length; i++) {
                                if (times[i] > max) {
                                    max = times[i];
                                } else if (times[i] < min) {
                                    min = times[i];
                                }
                            }
                            sum = avg;
                            avg = avg / cl.getConnection();
                            oos.close();
                            System.out.println("Tempo totale: " + sum + " ms\nMedia: " + avg + " ms\nMassimo: " + max + " ms\nMinimo: " + min + " ms");
                            flag2 = false;
                        } catch (NumberFormatException nfe) {
                            System.out.println("\n" + ANSI_RED + "ERRORE: il valore inserito per la porta non è valido, "
                                    + "inserire un numero compreso tra 1024 e 65535!" + ANSI_RESET + "\n");
                        } catch (ClassNotFoundException cnfe) {
                            System.out.println("ERRORE: riprovare!");
                        } catch (ConnectException ce) {
                            System.out.println("\n" + ANSI_RED + "ERRORE: la connessione non è avvenuta correttamente! "
                                    + "\n" + "Assicurarsi di aver avviato l'applicazione sul server e riprovare!" + ANSI_RESET + "\n");
                        } catch (UnknownHostException uhe) {
                            System.out.println("\n" + ANSI_RED + "ERRORE: il valore inserito per l'host non è valido! Riprovare!" + ANSI_RESET + "\n");
                        }catch(IOException ioe){
                            System.out.println(ANSI_RED + "ERRORE: il server si è disconnesso...QUITTING!" + ANSI_RESET);
                            System.exit(0);
                        }

                    }
                    flag = false;
                } else if (s1.toUpperCase().equals("EXIT")) {
                    System.out.println("\n" + ANSI_RED + "Shutting down LANSpeedTester!" + ANSI_RESET);
                    System.exit(0);
                } else {
                    System.out.println(ANSI_RED + "ERRORE: non hai selezionato uno tra i ruoli disponibili (s/c)! Riprovare!" + ANSI_RESET);
                }
            }
        } catch (Exception e) {

        }

    }
}
