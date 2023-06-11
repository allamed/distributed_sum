import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.43.62", 1111); // Adresse IP et port du serveur

            System.out.println("Connected to server " + socket);

            // Envoyer un tableau d'entiers au serveur pour en calculer la somme
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            int[] arr = {4,66,77,98,555,777};

            StringBuilder sb = new StringBuilder();
            for (int row : arr) {
                sb.append(row).append(";");
            }
            String arrString = sb.toString();
            writer.println(arrString);

            // Reception du résultat de calcul du serveur
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response = reader.readLine();
            System.out.println("Le serveur a envoyé la réponse suivante :  " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
