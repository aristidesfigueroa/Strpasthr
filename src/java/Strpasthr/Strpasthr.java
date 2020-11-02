/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strpasthr;


import java.io.BufferedReader;
import java.io.BufferedWriter;


import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author E101TEC25
 */
@WebService(serviceName = "Strpasthr")
public class Strpasthr {

    /**
     * Web service operation
     * @param ip
     * @param port
     * @param data
     * @return 
     */
    @WebMethod(operationName = "goSystem")
    public String goSystem(@WebParam(name = "ip") String ip, @WebParam(name = "port") String port, @WebParam(name = "data") String data)  {
        
        String respuesta;        
        String Host = ip;  
        int Puerto = Integer.parseInt(port);
        
        String trx="";
        if (Puerto == 4021)
            trx = "Amdc";
           
        
        Socket socket;
         
        crearFileLog(trx + "Recv : "+ data);
        
         try{
            socket = new Socket(Host,Puerto);
            PrintStream tramaSend = new PrintStream(socket.getOutputStream()); 
            InputStreamReader IR = new InputStreamReader(socket.getInputStream());
            BufferedReader BR = new BufferedReader(IR);
            
            tramaSend.println(data); 
            respuesta = BR.readLine();
            
            //tramaSend.flush(); 
            tramaSend.close();            
            BR.close();
            socket.close();
            crearFileLog(trx + "Send : " +respuesta);
        } catch (UnknownHostException e){ 
           crearFileLog(trx+"Send :"+e);
            respuesta = ""+e;
        }catch (IOException e){
            crearFileLog(trx+"Send : "+e);
            respuesta = ""+e;              
        }
        
        return respuesta;
    }
    
    
    
    /**
     *
     * Recupera la fecha y hora del día. Luego verifica si Logtuca de la fecha
     * existe. 
     * Si existe llama método para llenar linea siguiente del logtuca.
     * Si no existe el logtuca, lo crea y llama método para llenar primera línea.
     *
     */   
    private static void  crearFileLog(String linea){
        
        Date date = new Date();
        DateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
        String fechaString = fecha.format(date);
        
        DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fechaHoraString = fechaHora.format(date);    
        
        String newLinea = fechaHoraString.substring(11, 19) + "| " + linea;
        
        String ruta = ("c:\\Strpasthr\\Log-"+ fechaString + ".txt");
                    
        //String ruta = ("/Users/Figueroa/desktop/Logtuca" + fechaString + ".txt");
         //String ruta = ("/root/Desktop/Log" + fechaString + ".txt");
        
        try{
            File file = new File(ruta);
            if(file.exists()){               
               llenarSiguienteLinea(fechaString, newLinea);               
            }else{
                if(file.createNewFile()){                
                llenarPrimeraLinea(fechaString, newLinea);                                
            }
                
            }             
            
        } catch(IOException e){
            //System.out.println("No se ha podido crear el Archivo Log" + e);            
            
        }     
        
    }   
    
    
    /**
     *
     * Llena primera línea en el Logtuca
     *
     */    
    private static void llenarPrimeraLinea(String fechaString, String linea) throws IOException{
        
        String ruta = ("c:\\Strpasthr\\Log-"+ fechaString + ".txt");
        //String ruta = ("/Users/Figueroa/desktop/Logtuca" + fechaString + ".txt");
        //String ruta = ("/root/Desktop/Log" + fechaString + ".txt"); 
        
        BufferedWriter out;
        out = null;
        try  
        { 
            out = new BufferedWriter(new FileWriter(ruta, true));            
            out.write(linea);
            
       
        } catch (IOException e) { 
            // error
       
        } 
        finally{
            if (out != null){                
                out.close();
            }                
                
        }        
        
    }
    
    /**
     *
     * Llena siguientes lineas en el Logtuca
     *
     */    
    private static void llenarSiguienteLinea(String fechaString, String linea) throws IOException{
        
        String ruta = ("c:\\Strpasthr\\Log-"+ fechaString + ".txt");
        //String ruta = ("/Users/Figueroa/desktop/Logtuca" + fechaString + ".txt");
        //String ruta = ("/root/Desktop/Log" + fechaString + ".txt"); 
        
        BufferedWriter rc;
        rc = null;
        try  
        { 
            rc = new BufferedWriter(new FileWriter(ruta, true));            
            rc.newLine();
            rc.write(linea);            
       
        } catch (IOException e) { 
            // error       
        } 
        finally{
            if (rc != null){                
                rc.close();
            }                
                
        }         
        
    }//Fin de método

    
}
