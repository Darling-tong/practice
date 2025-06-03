import java.sql.*;
import java.util.*;

public class Merchandisemanagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/management";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n商品管理系统:");
            System.out.println("1. 新增商品");
            System.out.println("2. 根据商品类型查询商品列表");
            System.out.println("3. 查询销量最高的商品");
            System.out.println("4. 根据商品ID删除商品");
            System.out.println("5. 根据商品ID修改商品库存");
            System.out.println("6. 购买商品");
            System.out.println("7. 退货商品");
            System.out.println("8. 退出系统");
            System.out.print("请输入操作编号: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over

            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    listProductsByType(scanner);
                    break;
                case 3:
                    getTopSalesProduct(scanner);
                    break;
                case 4:
                    deleteProduct(scanner);
                    break;
                case 5:
                    updateStock(scanner);
                    break;
                case 6:
                    buyProduct(scanner);
                    break;
                case 7:
                    returnProduct(scanner);
                    break;
                case 8:
                    System.out.println("退出系统...");
                    scanner.close();
                    return;
                default:
                    System.out.println("无效的选择，请重新输入.");
            }
        }
    }

    // 新增商品
    private static void addProduct(Scanner scanner) {
        System.out.print("请输入商品名称: ");
        String goods_name = scanner.nextLine();
        System.out.print("请输入商品类型: ");
        String goods_type = scanner.nextLine();
        System.out.print("请输入商品库存: ");
        int stock = scanner.nextInt();
        System.out.print("请输入商品销量: ");
        int sales_volume = scanner.nextInt();

        String sql = "INSERT INTO jc_goods (goods_name, goods_type, stock, sales_volume) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, goods_name);
            statement.setString(2, goods_type);
            statement.setInt(3, stock);
            statement.setInt(4, sales_volume);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("商品添加成功.");
            } else {
                System.out.println("商品添加失败.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据商品类型查询商品列表
    private static void listProductsByType(Scanner scanner) {
        System.out.print("请输入商品类型: ");
        String goods_type = scanner.nextLine();

        String sql = "SELECT * FROM products WHERE type = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, goods_type);
            ResultSet resultSet = statement.executeQuery();
            boolean found = false;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String goods_name = resultSet.getString("goods_name");
                int stock = resultSet.getInt("stock");
                int sales_volume= resultSet.getInt("sales_volume");
                System.out.println("id: " + id + ", goods_name: " + goods_name + ", Stock: " + stock + ", Sales_volume: " + sales_volume);
                found = true;
            }
            if (!found) {
                System.out.println("没有找到该类型的商品.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询销量最高的商品
    private static void getTopSalesProduct(Scanner scanner) {
        System.out.print("请输入商品类型（不输入查询所有类型，直接回车即可）：");
        String goods_type = scanner.nextLine();

        String sql = goods_type.isEmpty() ? "SELECT * FROM products ORDER BY sales DESC LIMIT 1"
                : "SELECT * FROM products WHERE type = ? ORDER BY sales DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (!goods_type.isEmpty()) {
                statement.setString(1, goods_type);
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String goods_name = resultSet.getString("goods_name");
                String type = resultSet.getString("type");
                int stock = resultSet.getInt("stock");
                int sales_volume = resultSet.getInt("sales_volume");
                System.out.println("销量最高的商品: id: " + id + ", goods_name: " + goods_name + ", type: " + type + ", stock: " + stock + ", sales: " + sales_volume);
            } else {
                System.out.println("没有商品.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据商品ID删除商品
    private static void deleteProduct(Scanner scanner) {
        System.out.print("请输入商品ID: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("商品删除成功.");
            } else {
                System.out.println("商品ID不存在.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据商品ID修改商品库存
    private static void updateStock(Scanner scanner) {
        System.out.print("请输入商品ID: ");
        int id = scanner.nextInt();
        System.out.print("请输入新的库存数量: ");
        int stock = scanner.nextInt();

        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, stock);
            statement.setInt(2, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("商品库存更新成功.");
            } else {
                System.out.println("商品ID不存在.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 购买商品
    private static void buyProduct(Scanner scanner) {
        System.out.print("请输入商品ID: ");
        int id = scanner.nextInt();
        System.out.print("请输入购买数量: ");
        int quantity = scanner.nextInt();

        String sql = "SELECT stock, sales FROM products WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int stock = resultSet.getInt("stock");
                int sales_volume = resultSet.getInt("sales_volume");

                if (stock >= quantity) {
                    // 更新库存和销量
                    String updateSql = "UPDATE products SET stock = ?, sales = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setInt(1, stock - quantity);
                        updateStatement.setInt(2, sales_volume + quantity);
                        updateStatement.setInt(3, id);
                        updateStatement.executeUpdate();
                        System.out.println("购买成功.");
                    }
                } else {
                    System.out.println("库存不足.");
                }
            } else {
                System.out.println("商品ID不存在.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 退货商品
    private static void returnProduct(Scanner scanner) {
        System.out.print("请输入商品ID: ");
        int id = scanner.nextInt();
        System.out.print("请输入退货数量: ");
        int quantity = scanner.nextInt();

        String sql = "SELECT stock, sales FROM products WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int stock = resultSet.getInt("stock");
                int sales_volume = resultSet.getInt("sales_volume");

                // 更新库存和销量
                String updateSql = "UPDATE products SET stock = ?, sales = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setInt(1, stock + quantity);
                    updateStatement.setInt(2, sales_volume - quantity);
                    updateStatement.setInt(3, id);
                    updateStatement.executeUpdate();
                    System.out.println("退货成功.");
                }
            } else {
                System.out.println("商品ID不存在.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
