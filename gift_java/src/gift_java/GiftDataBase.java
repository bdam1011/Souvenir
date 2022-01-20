package gift_java;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

public class GiftDataBase {

	public static void main(String[] args) {
		String jsonData = fetchOpendata();
		//System.out.println(jsonData);
		if (jsonData != null) {
			parseOpendata(jsonData);	
		}
	}
	//將所有資料讀成一個大String
	protected static String fetchOpendata() {
		try {
			URL url = new URL("https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx");
			URLConnection connection =  url.openConnection();
			connection.connect();
			
			BufferedInputStream bin = new BufferedInputStream(connection.getInputStream());
			
			byte[] buf = new byte[1024*1024]; int len;
			StringBuffer sb = new StringBuffer();
			
			while ( (len = bin.read(buf)) != -1) {
				sb.append(new String(buf,0,len));
			}
			
			bin.close();
//			sb.insert(0,"[");
//			sb.append(",");
			return sb.toString();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}

	}
	//解析JSON，用官方提供的JAR
	protected static void parseOpendata(String json) {		
		Properties prop = new Properties();
		prop.put("user", "root");
		//prop.put("password", "root");
	    String sqlDelAllString = "TRUNCATE TABLE giftinfo";
		String sql = "INSERT INTO giftinfo (name,feature,salePlace,produceOrg,specAndPrice,contactTel,picurl,orderUrl)" + 
				" VALUES (?,?,?,?,?,?,?,?)";

		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/gift", prop)) {
			connection.createStatement().executeUpdate(sqlDelAllString);
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			//迴圈寫法來自資料格式
			JSONArray root = new JSONArray(json);
			for (int i=0; i<root.length(); i++) {
				JSONObject row = root.getJSONObject(i);
				pstmt.setString(1, row.getString("Name"));
				pstmt.setString(2, row.getString("Feature"));
				pstmt.setString(3, row.getString("SalePlace"));
				pstmt.setString(4, row.getString("ProduceOrg"));
				pstmt.setString(5, row.getString("SpecAndPrice"));
				pstmt.setString(6, row.getString("ContactTel"));
				pstmt.setString(7, row.getString("Column1"));
				pstmt.setString(8, row.getString("OrderUrl"));
				pstmt.executeUpdate();
			}
			System.out.println("Success");
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	

}


