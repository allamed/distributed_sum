import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Node2 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6666); // Port d'écoute du nœud
            System.out.println("\nJe suis Le noeud 2");
            System.out.println("\nJe suis entrain d'écouter sur le port 6666...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\nConnexion acceptée du client : " + clientSocket);

                // Créer un thread pour gérer la connexion client
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Recevoir la sous-tache du client
                InputStream inputStream = clientSocket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                int[] subTask = (int[]) objectInputStream.readObject();

                System.out.println("\nJ'ai recu la tache de calculer la somme des elements : " + (Arrays.toString(subTask) ));


                int result = 0;

                for (int i = 0; i < subTask.length; i++) {
                    result += subTask[i] ;
                }

                // Envoyer le résultat au serveur
                OutputStream outputStream = clientSocket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(result);
                System.out.println("\nj'ai envoyé au serveur le résultat : " + result + " .");

                // Fermer les connexions
                objectInputStream.close();
                objectOutputStream.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}