/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Id;

/**
 *
 * @author ma.forero11
 */
public class User {
    
    private String correo;
    
    private String nombre;
    
    private String usuario;
    
    private String contraseña;
    
    private String fechaNacimiento;
    
    private List<String> roles;

    public User(String correo, String fechaNacimiento, String usuario, String contraseña, String nombre, List<String> roles) {
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.roles = roles;
    }

    
    public User(){
        
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String id) {
        this.correo = correo;
    }

     public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
  //  @Override
   // public String toString() {
   //     return "entidad.Hub[ id=" + id + " ]";
    //}
    
}
