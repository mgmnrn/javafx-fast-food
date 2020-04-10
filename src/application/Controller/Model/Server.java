package application.Controller.Model;

public class Server {
    private static boolean status;
    private static String address;
    private static int port;

    public static boolean getStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        Server.status = status;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Server.address = address;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Server.port = port;
    }
}
