

package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Seguridad")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadService {

    @EJB
    private IServicioSeguridadMockLocal seguridadEjb;

    /**
     * Autenticación de usuario.
     * URL: POST /Seguridad/login
     * Cuerpo JSON: { "login": "admin", "contraseña": "adminadmin" }
     */
    @POST
    @Path("/login")
    public Response login(Usuario credenciales) {
        try {
            Usuario usuario = seguridadEjb.ingresar(credenciales.getLogin(), credenciales.getContraseña());
            return Response.ok(usuario).build();
        } catch (AutenticacionException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity(e.getMessage())
                           .build();
        }
    }

    /**
     * Crear un nuevo usuario.
     * URL: POST /Seguridad/usuarios
     */
    @POST
    @Path("/usuarios")
    public Response createUsuario(Usuario nuevoUsuario) {
        try {
            seguridadEjb.createUsuario(nuevoUsuario);
            return Response.status(Response.Status.CREATED).entity(nuevoUsuario).build();
        } catch (OperacionInvalidaException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    /**
     * Obtener la lista de todos los usuarios.
     * URL: GET /Seguridad/usuarios
     */
    @GET
    @Path("/usuarios")
    public Response getAllUsuarios() {
        List<Usuario> lista = seguridadEjb.getAllUsuarios();
        return Response.ok(lista).build();
    }

    /**
     * Obtener un usuario específico dado su login.
     * URL: GET /Seguridad/usuarios/{login}
     */
    @GET
    @Path("/usuarios/{login}")
    public Response getUsuario(@PathParam("login") String login) {
        Usuario u = seguridadEjb.getUsuario(login);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No se encontró el usuario con login: " + login)
                           .build();
        }
        return Response.ok(u).build();
    }

    /**
     * Actualizar la información de un usuario.
     * URL: PUT /Seguridad/usuarios/{login}
     * Cuerpo JSON con los nuevos datos.
     */
    @PUT
    @Path("/usuarios/{login}")
    public Response updateUsuario(@PathParam("login") String login, Usuario usuarioActualizado) {
        Usuario u = seguridadEjb.getUsuario(login);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No se encontró el usuario con login: " + login)
                           .build();
        }
        // Forzamos que el login se mantenga igual
        usuarioActualizado.setLogin(login);
        seguridadEjb.updateUsuario(usuarioActualizado);
        return Response.ok(usuarioActualizado).build();
    }

    /**
     * Eliminar un usuario.
     * URL: DELETE /Seguridad/usuarios/{login}
     */
    @DELETE
    @Path("/usuarios/{login}")
    public Response deleteUsuario(@PathParam("login") String login) {
        try {
            seguridadEjb.deleteUsuario(login);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (OperacionInvalidaException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }
}