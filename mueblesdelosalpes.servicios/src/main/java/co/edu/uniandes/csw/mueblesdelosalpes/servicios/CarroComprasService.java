package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockRemote;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/CarritoCompras")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarroComprasService {

    /**
     * Referencia al Ejb de carritos encargada de realizar las operaciones del
     * mismo.
     */
    @EJB
    private IServicioCarritoMockRemote carroEjb;

    @POST
    @Path("/agregar")
    public List<Mueble> agregarMuebles(List<Mueble> mb) {
        for (Mueble mueble : mb) {
            carroEjb.agregarItem(mueble);
        }

        return mb;
    }

    @GET
    @Path("/muebles")
    public List<Mueble> getTodosLosMuebles() {
        return carroEjb.getInventario();

    }

    @DELETE
    @Path("/eliminar/{referencia}")
    public Response eliminarMueblePorReferencia(@PathParam("referencia") String referenciaStr) {
        long referencia = Long.parseLong(referenciaStr);
        List<Mueble> inventario = carroEjb.getInventario();
        boolean encontrado = false;
        for (Mueble mueble : inventario) {
            if (mueble.getReferencia() == referencia) {
                carroEjb.removerItem(mueble, true);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            return Response.status(Response.Status.OK)
                    .entity("Mueble con referencia " + referencia + " eliminado correctamente")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró mueble con referencia " + referencia)
                    .build();
        }
    }

   @PUT
    @Path("/actualizar/{referencia}/{cantidad}")
    public Response actualizar(
    @PathParam("referencia") String referenciaStr,
    @PathParam("cantidad") String cantidad) { 
        long referencia = Long.parseLong(referenciaStr);
        int cant = Integer.parseInt(cantidad);
        List<Mueble> inventario = carroEjb.getInventario();
        boolean encontrado = false;
        for (Mueble mueble : inventario) {
            if (mueble.getReferencia() == referencia) {
                carroEjb.actualizarItem(mueble, cant);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            return Response.status(Response.Status.OK)
                    .entity("Mueble con referencia " + referencia + " actualizado correctamente")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No se encontró mueble con referencia " + referencia)
                    .build();
        }
    }

}
