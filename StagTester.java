import StagEngine.StagTokenizer;
import StagExceptions.StagException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * StagTester is based on the StagCheck class that was provided. I created this class to prevent any potential overwrite
 * by the automated testing scripts.
 *
 * I used this class to check that my StagServer was performing as required.
 *
 * I compiled my code in IntelliJ, and the running of this class assumes the file structure used by IntelliJ.
 *
 * */
public class StagTester {

    static String playerOne = "Alpha";
    static String playerTwo = "Beta";
    static String playerThree = "Gamma";
    static String playerWithSpaces = "Eta and Epsilon";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";


    public static void main(String args[])
    {
        Process serverOne = startNewServer();
        System.out.println("Built-in Commands Tests Summary");
        System.out.println("_______________________________");
        builtInCommandTests();
        System.out.println("\nAll built in commands tests completed.\n");
        killOldServer(serverOne);

        Process serverTwo = startNewServer();
        System.out.println("Dynamic Commands Tests Summary");
        System.out.println("_______________________________");
        dynamicCommandTests();
        killOldServer(serverTwo);
        System.out.println("\nAll dynamic commands tests completed.\n");

        Process serverThree = startNewServer();
        System.out.println("Multiplayer Tests Summary");
        System.out.println("_________________________");
        multiplayerTests();
        killOldServer(serverThree);
        System.out.println("\nAll multiplayer tests completed.\n");

        System.out.println("Tokenizer Tests Summary");
        System.out.println("_______________________");
        tokenizerTests();

        System.out.println(ANSI_GREEN + "\nTESTING COMPLETE\n" + ANSI_RESET);
    }

    public static String executeCommand(String command) {
        try {
            String response = "";
            String incoming;
            Socket socket = new Socket("127.0.0.1", 8888);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write(command + "\n");
            out.flush();
            while((incoming = in.readLine()) != null) response = response + incoming + "\n";
            in.close();
            out.close();
            socket.close();
            return response;
        } catch(IOException ioe) {
            System.out.println(ioe);
            return "";
        }
    }

    private static Process startNewServer() {
        String homeDirectory = System.getProperty("user.dir");
        String libsPath = homeDirectory + File.separator + "libs";

        String serverPath = homeDirectory + File.separator + "out" + File.separator + "production" + File.separator + "STAG";
        String dotParserPath = libsPath + File.separator + "dot-parser.jar";
        String jsonParserPath = libsPath + File.separator + "json-parser.jar";

        String classPath = serverPath + ":" + dotParserPath + ":" + jsonParserPath + ":" + ".";

        try {
            String[] command = {"java", "-classpath", classPath, "StagServer", "data/entities.dot", "data/actions.json"};
            Process process = Runtime.getRuntime().exec(command);
            // Sleep for a bit to give the server some time to warm up
            Thread.sleep(2000);
            return process;
        } catch(InterruptedException ie) {
            return null;
        } catch(IOException ioe) {
            return null;
        }
    }

    private static void killOldServer(Process server) {
        try {
            server.destroy();
            server.waitFor();
        } catch(InterruptedException ie) {
        }
    }

    public static void tokenizerTests() {
        String command = "player: The player \t said   ;,    \"show me my inventory.\"";

        StagTokenizer tokenizer = new StagTokenizer(command);
        ArrayList<String> tokens = new ArrayList<>();

        try{
            System.out.println(command);
            tokenizer.splitIntoTokens(tokens);
        }
        catch (StagException se) {
            se.toString();
        }

        if(tokens.size() == 12) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);

        if(tokens.get(tokens.size()-1).equals("\"")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);

        if(tokens.get(tokens.size()-2).equals(".")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);

        System.out.println("\nAll tokenizer tests completed.\n");
    }

    private static void builtInCommandTests() {
        ArrayList<String> commands = new ArrayList<>();
        String command;
        String response;

        // =============================================================================================================
        //  look command tests
        // =============================================================================================================

        commands.add(playerOne + ": look" );
        commands.add(playerOne + ": look around" );
        commands.add(playerOne + ": i want to look here" );
        commands.add(playerOne + ": look     " );
        commands.add(playerOne + ": i want to stop and look  around  " );
        commands.add(playerOne + ":     look    there" );
        commands.add(playerOne + ":    look    " );

        System.out.println("\nLook tests");
        System.out.println("___________\n");
        for (String s : commands){
            response = executeCommand(s);
            System.out.println(s);

            if(response.contains("cabin")) {
                System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);
            }
        }

        System.out.println("\nLook tests completed.\n");

        // =============================================================================================================
        //  get, drop and inv command tests
        // =============================================================================================================

        System.out.println("\nGet, drop and inventory tests");
        System.out.println("_____________________________\n");

        // Get an artefact
        command = playerOne + ": get axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Check inventory
        command = playerOne + ": inv";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);


        // Check inventory variation
        command = playerOne + ": inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);


        // Check inventory variation
        command = playerOne + ":  inventory        ";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);


        // Check inventory variation
        command = playerOne + ":  inv    entory  ";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Check inventory variation
        command = playerOne + ": show me my inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Drop the artefact
        command = playerOne + ": drop axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Dropping the same artefact should throw an error
        command = playerOne + ": drop axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Get artefact variation
        command = playerOne + ": i would like to get the axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Check that the artefact is in the inventory
        command = playerOne + ": inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Simply stating an artefact without a trigger should throw an error
        command = playerOne + ": axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // Check that the axe is in the inventory
        command = playerOne + ": inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        System.out.println("\nGet, drop and inventory tests completed.\n");

        // =============================================================================================================
        //  goto command tests
        // =============================================================================================================

        System.out.println("\nGoto tests");
        System.out.println("__________\n");

        // Basic goto command
        command = playerOne + ": goto forest";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("key") && response.contains("forest") && response.contains("tree")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // More formalised goto command
        command = playerOne + ": I would like to goto the cabin";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin") && response.contains("trapdoor")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        // goto command variation
        command = playerOne + ":    goto     forest      ; ?";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("key") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        System.out.println("\nGoto tests completed.\n");

        // =============================================================================================================
        //  health command tests
        // =============================================================================================================

        System.out.println("\nHealth tests");
        System.out.println("____________\n");

        command = playerOne + ": I want to goto the cabin";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get me that potion";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("potion") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": check that potion in inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("potion") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": health (should be 3)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("3") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": computer, i want to drink that potion";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("potion") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": show me my health after that potion (should be 4)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("4") && !response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": inventory should not contain a potion";
        response = executeCommand(command);
        System.out.println(command);
        if(!response.contains("potion")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

    }

    private static void dynamicCommandTests() {
        String command;
        String response;

        // Ensure we are starting fom the beginning
        command = playerOne + ": look";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get potion";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("potion")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get axe";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get coin";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("coin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look";
        response = executeCommand(command);
        System.out.println(command);
        if(!response.contains("axe") || !response.contains("potion") || !response.contains("coin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": inventory";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe") && response.contains("potion") && response.contains("coin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto forest";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("forest")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get the key";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("key")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto the cabin";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": please unlock the trapdoor";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("trapdoor")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look (should show a path to a cellar)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cellar")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto cellar";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("dusty")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": pay (just pay should produce an error)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": pay the elf";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("shovel")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get the shovel";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("shovel")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": inv";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("shovel")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto cabin";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto forest";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("forest")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": i want to chop (should produce error)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": I want to chop the tree";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("axe")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look (check for the log)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("log")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get that log";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("log") && response.contains("picked")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look (check for the log again, should not be there)";
        response = executeCommand(command);
        System.out.println(command);
        if(!response.contains("log")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto riverbank";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("riverbank")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get horn";
        response = executeCommand(command);
        System.out.println(command);
        if(!response.contains("log")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": blow my horn loudly";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("magic")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": drop horn";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("horn")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": blow my horn loudly again";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("magic")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look for the burly woodcutter";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("burly")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": i want to create a bridge (too vague)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("Error")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": i want to create a bridge across the river (atleast one subject)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("other side")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look (check for clearing)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("clearing")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto clearing";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("clearing")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": dig into the ground with my shovel";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("gold")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": get that gold ASAP";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("gold")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("hole")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        System.out.println("\nJust going to teleport the player to the cellar and make him fight with the elf.\n");

        command = playerOne + ": goto riverbank";
        executeCommand(command);
        command = playerOne + ": goto forest";
        executeCommand(command);
        command = playerOne + ": goto cabin";
        executeCommand(command);
        command = playerOne + ": goto cellar";
        executeCommand(command);

        command = playerOne + ": look (should be in the cellar here)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("angry")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": fight the elf";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("health") && response.contains("lose")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": health";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("2")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": hit the elf";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("health") && response.contains("lose")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": health";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("1")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": slap the elf";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("health")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("cabin")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto cellar";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("angry")) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": look (expect to find gold, potion, shovel and axe)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains("gold") && response.contains("potion") &&
                response.contains("shovel") && response.contains("axe"))
            System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

    }

    private static void multiplayerTests() {
        String command;
        String response;

        command = playerOne + ": look ";
        executeCommand(command);
        System.out.println(command);

        command = playerTwo + ": look ";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains(playerOne) && !response.contains(playerTwo)) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerThree + ": look ";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains(playerTwo) && response.contains(playerOne)) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerWithSpaces + ": look ";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains(playerOne) && response.contains(playerTwo) && response.contains(playerThree) && !response.contains(playerWithSpaces)) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

        command = playerOne + ": goto forest";
        executeCommand(command);
        System.out.println(command);

        command = playerOne + ": goto cabin (basically going back and checking if other players are there)";
        response = executeCommand(command);
        System.out.println(command);
        if(response.contains(playerTwo) && response.contains(playerThree) && response.contains(playerWithSpaces) && !response.contains(playerOne)) System.out.println(ANSI_GREEN + "SUCCESS" + ANSI_RESET);
        else System.out.println(ANSI_RED + "FAIL" + ANSI_RESET);

    }

}
