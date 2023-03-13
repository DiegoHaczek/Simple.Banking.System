package banking;

import java.sql.*;

public class DB {

    public static void createDB(String filename){

        String url = "jdbc:sqlite:" + filename;

        try (Connection conn = DriverManager.getConnection(url)){
            if(conn != null){
                System.out.println("Database created");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean verifyLogin(int pin, long cardNumber){
        String url = "jdbc:sqlite:card.s3db";

        String query="select id from card where pin = ? and number = ?;";

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1,pin);
            stmt.setLong(2,cardNumber);

            ResultSet rs = stmt.executeQuery();

           if(rs.getInt("id")>0){

               return true;
        }

        }catch (SQLException e){
            //System.out.println(e.getMessage());
        }

    return false;
    }

    public static int checkBalance(int pin){
        String url = "jdbc:sqlite:card.s3db";

        String query="select balance from card where pin = ?;";

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1,pin);

            ResultSet rs = stmt.executeQuery();

                return rs.getInt("Balance");


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void createTable(){

        String url = "jdbc:sqlite:card.s3db";

        String query="Create table if not exists card (" +
                "id integer primary key," +
                "number varchar(16) not null," +
                "pin varchar(4) not null," +
                "balance int default 0)";

        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()){

            stmt.execute(query);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void insertCard(String number,String pin){

        String url = "jdbc:sqlite:card.s3db";

        String query="insert into card (number,pin)" +
                "values ("+number+","+pin+");";

        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()){

            stmt.execute(query);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addIncome(int pin, int amount){
        String url = "jdbc:sqlite:card.s3db";

        String query="update card set balance = balance+?" +
                "where pin = ? ;";

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1,amount);
            stmt.setInt(2,pin);
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void closeAccount(int pin){
        String url = "jdbc:sqlite:card.s3db";

        String query="delete from card where pin  = ? ;";

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1,pin);
            stmt.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean cardExists(long cardNumber){
        String url = "jdbc:sqlite:card.s3db";

        String query="select id from card where number = ?;";

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setLong(1,cardNumber);

            ResultSet rs = stmt.executeQuery();

            if(rs.getInt("id")>0){

                return true;
            }

        }catch (SQLException e){
            //System.out.println(e.getMessage());
        }

        return false;
    }

    public static void transfer(int amount, long origin, long destinatary){
        String url = "jdbc:sqlite:card.s3db";

        String discount="update card set balance = balance - ?" +
                "where number = ?;";
        String add="update card set balance = balance + ?" +
                "where number = ? ;";

        try(Connection conn = DriverManager.getConnection(url)){

            conn.setAutoCommit(false);

            try(PreparedStatement stmt1 = conn.prepareStatement(discount);
                PreparedStatement stmt2 = conn.prepareStatement(add)){

                stmt1.setInt(1,amount);
                stmt1.setLong(2,origin);
                stmt1.executeUpdate();

                stmt2.setInt(1,amount);
                stmt2.setLong(2,destinatary);
                stmt2.executeUpdate();

                conn.commit();
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    }

