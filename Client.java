import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.43.62", 1111); // Adresse IP et port du serveur
            System.out.println("\nJe suis Le client");
            System.out.println("\nConnecté au serveur " + socket);

            // Envoyer un tableau d'entiers au serveur pour en calculer la somme
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            Scanner scanner = new Scanner(System.in);

            System.out.print("\nEnter Le nombre d'élements à calculer ");
            int size = scanner.nextInt();

            int[] arr = new int[size];

            System.out.println("\nEntrer " + size + " nombres:");

            for (int i = 0; i < size; i++) {
                arr[i] = scanner.nextInt();
            }

            StringBuilder sb = new StringBuilder();
            for (int row : arr) {
                sb.append(row).append(";");
            }
            String arrString = sb.toString();
            writer.println(arrString);
            System.out.println("\nJ'ai envoyé au serveur le tableau suivant : " + Arrays.toString(arr) + " .");

            // Reception du résultat de calcul du serveur
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response = reader.readLine();
            System.out.println("\nJ'ai recu du serveur la réponse suivante :  " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
