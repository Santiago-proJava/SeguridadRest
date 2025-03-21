/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$ ServicioSeguridadMock.java
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 3.0
 *
 * Ejercicio: Muebles de los Alpes
 * Autor: Juan Sebastián Urrego
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.OperacionInvalidaException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import java.util.List;


/**
 * Clase que se encarga de la autenticación de un usuario en el sistema
 * @author Juan Sebastián Urrego
 */
public class ServicioSeguridadMock implements IServicioSeguridadMockLocal
{

    //-----------------------------------------------------------
    // Atributos
    //-----------------------------------------------------------

    /**
     * Interface con referencia al servicio de persistencia en el sistema
     */
    private IServicioPersistenciaMockLocal persistencia;

    //-----------------------------------------------------------
    // Métodos
    //-----------------------------------------------------------

    /**
     * Constructor sin argumentos de la clase
     */
    public ServicioSeguridadMock()
    {
        persistencia=new ServicioPersistenciaMock();
    }

    //-----------------------------------------------------------
    // Métodos
    //-----------------------------------------------------------

    /**
     * Registra el ingreso de un usuario al sistema.
     * @param nombre Login del usuario que quiere ingresar al sistema.
     * @param contraseña Contraseña del usuario que quiere ingresar al sistema.
     * @return usuario Retorna el objeto que contiene la información del usuario que ingreso al sistema.
     */
    @Override
    public Usuario ingresar(String nombre, String contraseña) throws AutenticacionException
    {
   
        Usuario u = (Usuario) persistencia.findById(Usuario.class, nombre);

        if (u != null)
        {
            if (u.getLogin().equals(nombre) && u.getContraseña().equals(contraseña))
            {
                return u;
            } 
            else
            {
                throw new AutenticacionException("La contraseña no es válida. Por favor, asegúrate de que el bloqueo de mayúsculas no está activado e inténtalo de nuevo.");
            }
        } 
        else
        {
            throw new AutenticacionException("El nombre de usuario proporcionado no pertenece a ninguna cuenta.");
        }
    }

    // Métodos CRUD extendidos para usuarios

    /**
     * Crea un nuevo usuario en el sistema.
     */
    @Override
    public Usuario createUsuario(Usuario u) throws OperacionInvalidaException {
        persistencia.create(u);
        return u;
    }

    /**
     * Retorna la lista de todos los usuarios registrados.
     */
    @Override
    public List<Usuario> getAllUsuarios() {
        return (List<Usuario>) persistencia.findAll(Usuario.class);
    }

    /**
     * Retorna un usuario en específico, dado su login.
     */
    @Override
    public Usuario getUsuario(String login) {
        return (Usuario) persistencia.findById(Usuario.class, login);
    }

    /**
     * Actualiza la información de un usuario.
     */
    @Override
    public Usuario updateUsuario(Usuario u) {
        persistencia.update(u);
        return u;
    }

    /**
     * Elimina un usuario del sistema (por su login). Se validan restricciones, por ejemplo, si tiene compras asociadas.
     */
    @Override
    public void deleteUsuario(String login) throws OperacionInvalidaException {
        Usuario u = getUsuario(login);
        if (u != null) {
            persistencia.delete(u);
        }
    }
}
