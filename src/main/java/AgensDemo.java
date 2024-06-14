import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AgensDemo {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/my_graph_db"; // Change to your database URL
        String user = ""; // Change to your database username
        String password = ""; // Change to your database password

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement()) {

            // Create the AGE extension if not exists
//            st.execute("CREATE EXTENSION IF NOT EXISTS age");

            // Set the graph path
            st.execute("SET graph_path = my_graph");


            // Be careful. If you are doing work from terminal, below lines should be commented
            st.execute("DROP VLABEL individual CASCADE");
            st.execute("DROP VLABEL orders CASCADE");
            st.execute("DROP VLABEL product CASCADE");
            st.execute("DROP ELABEL transactions");
            st.execute("DROP ELABEL viewed");
            st.execute("DROP ELABEL purchased");

            // Create vertex and edge labels
            st.execute("CREATE VLABEL IF NOT EXISTS individual");
            st.execute("CREATE VLABEL IF NOT EXISTS orders");
            st.execute("CREATE VLABEL IF NOT EXISTS product");
            st.execute("CREATE ELABEL IF NOT EXISTS transactions");
            st.execute("CREATE ELABEL IF NOT EXISTS viewed");
            st.execute("CREATE ELABEL IF NOT EXISTS purchased");

            // Insert an individual
            st.execute("CREATE (n:individual {name: 'Rishabh', id: '12'})");
            st.execute("CREATE (n:individual {name: 'Sonal', id: '14'})");
            st.execute("CREATE (n:individual {name: 'Joe', id: '16'})");

            // Create orders
            st.execute("CREATE (n:orders {name: 'Order5', id: 'o5'})");
            st.execute("CREATE (n:orders {name: 'Order7', id: 'o7'})");
            st.execute("CREATE (n:orders {name: 'Order20', id: 'o20'})");

            // Create products
            st.execute("CREATE (n:product {name: 'Product1', id: 'p1'})");
            st.execute("CREATE (n:product {name: 'Product6', id: 'p6'})");
            st.execute("CREATE (n:product {name: 'Product3', id: 'p3'})");


            // Create a relationship between nodes - Individuals doing transactions
            st.execute("MATCH (a:individual {id: '12'}), (b:orders {id: 'o5'}) CREATE (a)-[:transactions]->(b)");
            st.execute("MATCH (a:individual {id: '12'}), (b:orders {id: 'o7'}) CREATE (a)-[:transactions]->(b)");
            st.execute("MATCH (a:individual {id: '14'}), (b:orders {id: 'o20'}) CREATE (a)-[:transactions]->(b)");

            // Create a relationship between nodes - Individuals viewing products

            st.execute("MATCH (a:individual {id: '12'}), (b:product {id: 'p1'}) CREATE (a)-[:viewed]->(b)");
            st.execute("MATCH (a:individual {id: '16'}), (b:product {id: 'p1'}) CREATE (a)-[:viewed]->(b)");
            st.execute("MATCH (a:individual {id: '12'}), (b:product {id: 'p6'}) CREATE (a)-[:viewed]->(b)");
            st.execute("MATCH (a:individual {id: '14'}), (b:product {id: 'p6'}) CREATE (a)-[:viewed]->(b)");


            // Create a relationship between nodes - Orders purchasing products
            st.execute("MATCH (a:orders {id: 'o20'}), (b:product {id: 'p6'}) CREATE (a)-[:purchased]->(b)");
            st.execute("MATCH (a:orders {id: 'o7'}), (b:product {id: 'p6'}) CREATE (a)-[:purchased]->(b)");
            st.execute("MATCH (a:orders {id: 'o7'}), (b:product {id: 'p3'}) CREATE (a)-[:purchased]->(b)");

            //
            String query = " MATCH (i:individual {id: '12'})-[:transactions]->(o:orders)-[:purchased]->(p:product) RETURN p.name AS productName";
            System.out.println(query);

            // Query the node and its relationships
            ResultSet rs = st.executeQuery(query);


            while (rs.next()) {
                String output = rs.getString("productName");
                System.out.println(output);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
