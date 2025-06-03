import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MySQLConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/management"; // 替换为您的数据库URL
        String user = "root"; // 替换为您的数据库用户名
        String password = "123456"; // 替换为您的数据库密码

        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立连接
            Connection connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("成功连接到MySQL数据库！");
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("未找到MySQL JDBC驱动！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("连接MySQL数据库失败！");
            e.printStackTrace();
        }
    }
}
