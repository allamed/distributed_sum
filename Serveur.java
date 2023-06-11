import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Serveur {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1111); // Port d'écoute du serveur
            System.out.println("Serveur demarré");
            System.out.println("\nJe suis Le serveur qui coordonne les noeuds");
            System.out.println("\nJe suis à l'écoute sur le port 1111...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nJe viens d'accepter la connection du du client : " + clientSocket);

                // Créer un thread pour gérer la connexion client
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Recevoir la tache du client
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String arrString = reader.readLine();
            System.out.println("\nJ'ai recu la requete du client  : [ " + arrString + " ]");


            int[] arr = convertStringToArray(arrString);



            // Diviser le tableau  en deux sous tableaux


            int [] tache1 = new int [arr.length/2];
            int [] tache2 = new int [arr.length/2];

            for (int i = 0; i < arr.length/2; i++) {
                tache1[i] = arr[i];
            }
            int j=0;
            for (int i = arr.length/2; i < arr.length; i++) {
                tache2[j] = arr[i];
                j++;
            }

            // Répartir les tâches aux deux nœuds
            int result1 = assignerTache(tache1, "192.168.43.62", 5555);
            int result2 = assignerTache(tache2, "192.168.43.62", 6666);

            System.out.println("\nJ'ai envoyé la tache au nœud 1 : " + Arrays.toString(tache1) + " .");
            System.out.println("\nJ'ai envoyé la tache au nœud 2 : " + Arrays.toString(tache2) + " .");


            // Merger les résultats des 2 nœuds
            int mergedResult = mergeResults(result1, result2);

            // Envoyer le résultat au client
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println((mergedResult));
            System.out.println("\nJ'ai envoyé le résultat au client : " + mergedResult + " .");

            // Fermer les connexions
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] convertStringToArray(String matrixString) {
        String[] rows = matrixString.split(";");
        int rowCount = rows.length;


        int[] matrix = new int[rowCount];

        for (int i = 0; i < rowCount; i++) {
            matrix[i] = Integer.parseInt(rows[i]);

        }

        return matrix;
    }

    private int assignerTache(int[] subMatrix, String nodeAddress, int nodePort) {
        int resultMatrix = 0;

        try {
            Socket nodeSocket = new Socket(nodeAddress, nodePort); // Adresse IP et port du nœud
            OutputStream outputStream = nodeSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(subMatrix);

            InputStream inputStream = nodeSocket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            resultMatrix = (int) objectInputStream.readObject();

            objectInputStream.close();
            objectOutputStream.close();
            nodeSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultMatrix;
    }


    private int mergeResults(int result1, int result2) {

        return result1+ result2;
    }
}