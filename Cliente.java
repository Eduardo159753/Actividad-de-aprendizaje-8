package Cliente;

import Seguridad.AES;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "Cliente", urlPatterns = {"/Cliente"})
public class Cliente extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            final String Host = "127.0.0.1";
            final int Puerto = 6500;
            
            AES aesito = new AES();
            
            List<String> Llevar = new ArrayList<>();
            
            
            
            String UrName = request.getParameter("Nombre");
            String UrNameC = aesito.Encriptar(UrName);
            Llevar.add(UrNameC);
            
            String UrFName = request.getParameter("Apellidos");
            String UrFNameC = aesito.Encriptar(UrFName);
            Llevar.add(UrFNameC);
            
            String Age = request.getParameter("Edad");
            String AgeC = aesito.Encriptar(Age);
            Llevar.add(AgeC);
            
            String OldSchool = request.getParameter("LvlEstudios");
            String OldSchoolC = aesito.Encriptar(OldSchool);
            Llevar.add(OldSchoolC);
            
            String Email = request.getParameter("Correo");
            String EmailC = aesito.Encriptar(Email);
            Llevar.add(EmailC);
            
            String Pass = request.getParameter("Contrasena");
            String PassC = aesito.Encriptar(Pass);
            Llevar.add(PassC);
            try {
                Socket socket = new Socket(Host, Puerto);

                DataInputStream Entrada = new DataInputStream(socket.getInputStream());  // Sirve para leer/recibir los mensajes del cliente
                DataOutputStream Salida = new DataOutputStream(socket.getOutputStream());
                
                String EnviaM = String.join(",", Llevar);
		System.out.println(EnviaM);
                
                Salida.writeUTF(EnviaM + ",Registro");                // Mensaje a enviar
                
                String Mensaje = Entrada.readUTF();			// Lee el mensaje que envía el servidor
                
                List<String> Respuesta = new ArrayList<>(Arrays.asList(Mensaje.split(",")));
                
                System.out.println(Mensaje);
                
                
                for (int i = 0; i < Respuesta.size(); i++) {
                    
                    if(Respuesta.get(i).equals("Usuario Registrado con éxito")){
                        HttpSession sesion = request.getSession();
                        
                        sesion.setAttribute("Id_Usuario", Respuesta.get(0));
                        sesion.setAttribute("Nombre", UrName);
                        sesion.setAttribute("Apellidos", UrFName);
                        sesion.setAttribute("Correo", Email);
                        response.sendRedirect("index.jsp");

                    }else if(Respuesta.get(i).equals("Usuario ya registrado")){
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Redirigiendo...</title>");            
                        out.println("<link rel=\"stylesheet\" href=\"CSS/Inicio.css\">");            
                        out.println("<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">");            
                        out.println("<link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;700&display=swap\" rel=\"stylesheet\">");            
                        out.println("<link rel=\"stylesheet\" href=\"CSS/Formato.css\">");            
                        out.println("<link rel=\"stylesheet\" href=\"CSS/normalize.css\">");            
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<header class=\"header\" id=\"inicio\">");
                        out.println("<div class=\"contenedor head\">");
                        out.println("<h2>Ese correo ya esta registrado, por favor ingrese otro</h2>");
                        out.println("<h3 id='Cuenta'></h3>");
                        out.println("</div>");
                        out.println("</header>");
                        out.println("<script src='Scripts/RedirigirR.js'></script>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                }
                socket.close();

            } catch (Exception e) {
            }
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}