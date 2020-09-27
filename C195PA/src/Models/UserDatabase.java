package Models;
import Main.LogFiles;
import Main.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDatabase {

    private static User currentUser;

    public static User getCurrentUser(){
        return currentUser;
    }

    public static Boolean login(String username, String password) {
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT * FROM user WHERE userName ='"+ username + "'AND password='"+ password + "'";
            ResultSet results = statement.executeQuery(query);
            if (results.next()) {
                currentUser = new User();
                currentUser.setUsername(results.getString("userName"));
                statement.close();
                LogFiles.logFile(username, true);
                return true;
                }
            else {
                LogFiles.logFile(username, false);
                return false;
                }
            }
            catch(SQLException e){
                System.out.println("SQLException " + e.getMessage());
                return false;
            }
        }

}
