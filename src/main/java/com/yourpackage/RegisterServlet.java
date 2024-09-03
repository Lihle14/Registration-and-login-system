import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraryManagementSystemDB", "postgres", "admin");
            
            PreparedStatement psCheck = con.prepareStatement("SELECT * FROM Registration WHERE username = ? OR email = ?");
            psCheck.setString(1, username);
            psCheck.setString(2, email);
            if (psCheck.executeQuery().next()) {
                response.getWriter().write("Username or Email already taken");
                return;
            }
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO Registration(username, name, surname, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, username);
            ps.setString(2, name);
            ps.setString(3, surname);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, password);
            ps.executeUpdate();
            
            String insertLoginQuery = "INSERT INTO Login(username, password) VALUES (?, ?)";
            ps = con.prepareStatement(insertLoginQuery);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Registration failed: " + e.getMessage());
        }
    }
}
