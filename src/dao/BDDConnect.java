package dao;

/**
 *
 * @author 626
 */
import cookie.swipe.application.SystemSettings;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe static permetant à une base de donnée MySql
 * @author Stitch & Mary
 */
public class BDDConnect {

    private static Connection connexion;
    private static final String[] conf = new String[4];

    /**
     * Permet de se connecter à la base de donnée
     *
     * @return une instance de connexion
     */
    public static Connection getConnection() {
        if (connexion == null) {
            if (conf[1] == null || conf[2] == null) {
                conf[0] = "jdbc:mysql://";
                conf[1] = "";
                conf[2] = "";
                conf[3] = "";
                try {
                    BufferedReader fichier = new BufferedReader(new FileReader(
                            "src" + SystemSettings.SEPARATOR + 
                            "conf" + SystemSettings.SEPARATOR + 
                            "confDatabase"
                    ));
                    String ligne;
                    int i = 0;
                    while ((ligne = fichier.readLine()) != null) {
                        conf[i] += ligne;
                        i++;
                    }
                    fichier.close();
                } catch (IOException e) {
                    Logger.getLogger("BDDConnect").log(Level.SEVERE, "Erreur lors de la lecture du fichier de configuration", e);
                    return null;
                }
            }
            try {
                String pilote = "com.mysql.jdbc.Driver";
                Class.forName(pilote);
            } catch (ClassNotFoundException e) {
                Logger.getLogger("BDDConnect").log(Level.SEVERE, "Where is your MySQL JDBC Driver 1?", e);
                return null;
            }
            try {
                connexion = DriverManager.getConnection(conf[0] + "/" + conf[1], conf[2], conf[3]);
            } catch (SQLException e) {
                Logger.getLogger("BDDConnect").log(Level.SEVERE, "Where is your MySQL JDBC Driver 2?", e);
                return null;
            }
            return connexion;
        } else {
            return connexion;
        }
    }

}
