import java.io.*;
import java.net.*;
import java.nio.file.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server {
    private static final int PORT = 8080;
    private static final String DB_FILE = "database.json";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                String line;
                StringBuilder request = new StringBuilder();
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    request.append(line).append("\n");
                }

                StringBuilder body = new StringBuilder();
                while (in.ready()) {
                    body.append((char) in.read());
                }

                if (body.length() > 0) {
                    JSONObject submission = new JSONObject(body.toString());
                    saveSubmission(submission);
                }

                String response = "{\"status\":\"success\",\"message\":\"Submission received!\"}";

                out.println("HTTP/1.1 200 OK");
                out.println("Access-Control-Allow-Origin: *");
                out.println("Content-Type: application/json");
                out.println();
                out.println(response);
                out.flush();

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void saveSubmission(JSONObject submission) {
            try {
                File file = new File(DB_FILE);
                JSONArray submissions = new JSONArray();

                if (file.exists()) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    if (!content.isEmpty()) {
                        submissions = new JSONArray(content);
                    }
                }

                submissions.put(submission);
                Files.write(file.toPath(), submissions.toString(4).getBytes());
                System.out.println("💾 Saved: " + submission.getString("username"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
