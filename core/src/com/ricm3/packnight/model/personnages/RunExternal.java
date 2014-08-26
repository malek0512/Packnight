package com.ricm3.packnight.model.personnages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lancer un exécutable ou une commande externe
 */
public class RunExternal {

    public static void launch(String command) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            //Attendre la fin de l'execution
            if (process.waitFor() != 0) {
                System.out.println("Une erreur est survenue ");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Exemples
     */
    public static void main(String args[]) {
        RunExternal.launch("make all");
        
    }
}