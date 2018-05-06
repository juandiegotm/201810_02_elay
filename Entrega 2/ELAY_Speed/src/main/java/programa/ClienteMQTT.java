/*
 * The MIT License
 *
 * Copyright 2018 hs.hernandez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package programa;

import ch.qos.logback.classic.util.ContextInitializer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import servicios.Contrasenias;

/**
 *
 * @author hs.hernandez
 */
public class ClienteMQTT 
{
    
    public enum Topicos {
        PUBLICAR("entradasCerradura"), 
        SUSCRIBIR("alarmasCerraduras");
        private final String topic;
        Topicos(String topic) { this.topic = topic; }
        public String getTopic() { return topic; }
    }
    
    public static ClienteMQTT CLIENTE = new ClienteMQTT();
    static long time = System.currentTimeMillis();
    public final Logger LOG =  Logger.getLogger(ClienteMQTT.class.getName());
    
    private MqttClient cliente;
    public ClienteMQTT() {
        if(cliente == null) {
            try {
                cliente = new MqttClient("tcp://172.24.42.23:8083","0");
                MqttConnectOptions op = new MqttConnectOptions();
                op.setCleanSession(true);
                op.setConnectionTimeout(100000);
                cliente.setCallback(new MqttCallback() {
                    @Override public void connectionLost(Throwable thrwbl) {Logger.getLogger(ClienteMQTT.class.getName()).log(Level.INFO, "==== Gabrial no joda x2 {0}", time = System.currentTimeMillis()-time);}
                    @Override public void messageArrived(String topic, MqttMessage mm) throws Exception {
                        System.out.println(topic+" >> "+mm.toString());
                        if(mm.toString().startsWith(Contrasenias.Protocolo.ERROR.getCmd())) {
                            switch (mm.toString().split(":")[1]) {
                                case "01":
                                    reportar("Error al momento de agregar la contraseña");
                                    break;
                                case "02":
                                    reportar("Error al momento de modificar la contraseña");
                                    break;
                                case "03":
                                    reportar("Error al momento de eliminar la contraseña");
                                    break;
                                case "04":
                                    reportar("Error al momento de eliminar todas las contraseña");
                                    break;
                                case "05":
                                    reportar("Error al momento de sincronizar las contraseñas");
                                    break;
                            }
                        }
                        if(mm.toString().startsWith(Contrasenias.Protocolo.OK.getCmd())) {
                            switch (mm.toString().split(":")[1]) {
                                case "01":
                                    LOG.log(Level.INFO, "Adición exitosa");
                                    break;
                                case "02":
                                    LOG.log(Level.INFO, "Modificación exitosa");
                                    break;
                                case "03":
                                    LOG.log(Level.INFO, "Eliminación exitosa");
                                    break;
                                case "04":
                                    LOG.log(Level.INFO, "Eliminación exitosa");
                                    break;
                                case "05":
                                    LOG.log(Level.INFO, "Sincronización exitosa");
                                    break;
                            }
                        }
                        
                        if(p(t -> t.equals("estado") ,topic)) {
                            System.out.println("estado: "+mm);
                            if(mm.toString().startsWith("STARDHUB")) healdcheck.HealdCheck.empezarVerificador(mm.toString().split(":")[1]);
                            if(mm.toString().startsWith("ESTADO")) healdcheck.HealdCheck.notificar(mm.toString().split(":")[2]);
                        }
                    }
                    @Override public void deliveryComplete(IMqttDeliveryToken imdt) {}
                });
                cliente.connect(op);
                cliente.subscribe(new String[]{Topicos.SUSCRIBIR.topic,"estado"}, new int[]{1,1});
            } catch (MqttException ex) { Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex); }
        }
    }
    
    private boolean p(Predicate<String> l, String s) { return l.test(s); }
    
    private void reportar(String msg) {
        LOG.log(Level.SEVERE, msg);
    }
    
    public static void publicar(String msg) {
        try { CLIENTE.cliente.publish(Topicos.PUBLICAR.topic, msg.getBytes(), 2, true); } 
        catch (MqttException ex) { Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    public static void publicar(String msg, int QoS, boolean retain) {
        try { CLIENTE.cliente.publish(Topicos.PUBLICAR.topic, msg.getBytes(), QoS, retain); } 
        catch (MqttException ex) { Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    public static void publicar(String topic, String msg, int QoS, boolean retain) {
        try { CLIENTE.cliente.publish(topic, msg.getBytes(), QoS, retain); } 
        catch (MqttException ex) { Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex); }
    }
    
    public static ClienteMQTT getCLIENTE() {
        return CLIENTE;
    }
}