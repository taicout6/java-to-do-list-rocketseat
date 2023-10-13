package br.com.taicout6.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.taicout6.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) {
          // Pegar auth (usuario e senha)
          var authorizathion = request.getHeader("Authorization");

          var userAuthEncoded = authorizathion.substring("Basic".length()).trim();
          System.out.println("Authorization");
          System.out.println(userAuthEncoded);

          byte[] userAuthDecoded = Base64.getDecoder().decode(userAuthEncoded);
          System.out.println(userAuthDecoded);

          var userAuthString = new String(userAuthDecoded);
          System.out.println(userAuthString);

          // ["username", "password"]
          String[] credentials = userAuthString.split(":");
          String username = credentials[0];
          String password = credentials[1];
          System.out.println(username);
          System.out.println(password);

          // Validar usuario
          var user = this.userRepository.findByUsername(username);
          if (user == null) {
            response.sendError(401);
          } else {
            // Validar senha
            var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            if (passwordVerified.verified) {
              request.setAttribute("idUser", user.getId());
              filterChain.doFilter(request, response);
            } else {
              response.sendError(401);
            }
            // Segue viagem
          }
        } else {
          filterChain.doFilter(request, response);
        }
  }
}
