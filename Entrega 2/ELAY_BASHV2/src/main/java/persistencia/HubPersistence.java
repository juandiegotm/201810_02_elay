/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import entidad.Hub;

/**
 *
 * @author jd.trujillom
 */
public class HubPersistence extends Persistencer<Hub, String> {

    public HubPersistence() {
    this.entityClass = Hub.class;
    }
    
    
}
