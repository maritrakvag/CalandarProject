package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionMySQL {
	public static void main(String[] args) {
		Connection conn = null;
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://studsql.idi.ntnu.no:3306/tdt4145_bokbase", "stud_tdt4145_r", "qwertyuiop");
			Statement stat = conn.createStatement();
			
			//Lese fra databasen
			/*String query = "SELECT * FROM bok";
			ResultSet rs = stat.executeQuery(query);
			while(rs.next()) {
				System.out.println("Bok " + rs.getString("tittel"));
			}
*/			
			
			//Legge inn i databasen
			String sql = "insert into bok"
					+ " (bokid, tittel, utgittår, forlagid)"
					+ " values (26,'Marits bok', 2000, 3)";
			
			stat.executeUpdate(sql);
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
