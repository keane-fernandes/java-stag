import StagEngine.StagController;
import StagEngine.StagModel;
import StagEngine.StagParser;
import StagExceptions.StagException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class StagServer
{
    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        StagModel model = new StagModel();
        StagParser parser = new StagParser(model, entityFilename, actionFilename);
        parser.importActions();
        parser.importEntities();

        StagController controller = new StagController(model);

        try {
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss, controller);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss, StagController controller)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out, controller);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out, StagController controller) throws IOException
    {
        String command = in.readLine();

        try {
            out.write(controller.handleIncomingCommand(command));
        } catch (StagException se) {
            out.write(se.toString());
        }
    }
}
