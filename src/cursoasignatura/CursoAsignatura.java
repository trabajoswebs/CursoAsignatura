/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursoasignatura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class CursoAsignatura {
    
    public static final String DIRECTORY = "..\\Profesores\\";
    public static final String CURASIGFILENAME = "cursoAsignatura.txt";
    public static final String CURASIGFILEPATH = DIRECTORY + CURASIGFILENAME;
    
    static Scanner sc = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
     
    
    public static void main(String[] args) {
        // TODO code application logic here
        subMenuCursoAsignaturas();
    }
        
    /**
     * Elimina la asignatura en el fichero Profesores/CursosAsignaturas.txt
     */
    public static void bajaCursoAsignatura(){
        
        File ficheroActualizado = new File(DIRECTORY + "cursosActualizados.txt");
        String codCurso, cadena, continuar;
        int indice;
        RandomAccessFile fichero = null;
        RandomAccessFile ficheroNuevo = null;
        boolean repetir = false;
        
       
        do {
            boolean existeCodigo = false;
            
            try {
                System.out.println("\nListado de cursos: ");
                System.out.println(imprimeCursosAsignaturas());
                System.out.print("Indique el código del curso que desea eliminar: ");
                codCurso = sc.nextLine();
                
                if (codCurso.trim().isEmpty()) {
                    throw new Exception("Debe introducir un código de asignatura válido.");
                }
                                                
                fichero = new RandomAccessFile(CURASIGFILEPATH, "r");
                
                if(fichero.length() == 0) throw new Exception("El fichero de las asignaturas se encuentra vacio.");
                
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if (indice != -1) {
                        if(cadena.substring(0, indice).equalsIgnoreCase(codCurso)){
                            existeCodigo = true;
                            break; // dejamos de recorrer el fichero saliendo del while
                        } 
                            
                    }                    
                    cadena = fichero.readLine();
                }
                
                if (! existeCodigo) // Si el código no existe en el fichero lanzamos la exepción
                        throw new Exception("El código del curso (" + codCurso.toUpperCase() + ") que se desea eliminar no existe en el fichero.");
                
                crearFichero(DIRECTORY, "cursosActualizados.txt"); //creamos un nuevo fichero
                
                ficheroNuevo = new RandomAccessFile(ficheroActualizado, "rw");
                
                fichero.seek(0); //Llevamos el puntero al inicio
                
                while (cadena != null) {
                    indice = cadena.indexOf(",");
                    if(indice != -1){  
                        if (! cadena.substring(0, indice).equalsIgnoreCase(codCurso)) {

                            ficheroNuevo.seek(ficheroNuevo.getFilePointer());
                            ficheroNuevo.writeBytes(cadena + "\n");
                        }
                    }
                    cadena = fichero.readLine();
                }
             
                ficheroNuevo.close(); //Cerramos el fichero actualizado
                fichero.close(); //Debemos cerrar el fichero antes de eliminarlo
                
                File ficheroOriginal = new File(DIRECTORY + "cursosActualizados.txt");
                File destFichero = new File(CURASIGFILEPATH);
                
                 if (destFichero.delete()) { //Borramos el fichero anterior
                     
                        if (ficheroOriginal.renameTo(destFichero)) {//Renombramos el fichero
                            
                        System.out.println("Se ha eliminado correctamente la asignatura  " + codCurso + " del fichero.");
                        System.out.println("Si desea eliminar más asignaturas de la lista introduzca la letra: \"S\"");

                        continuar = sc.nextLine();
                        repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo cursos

                    } else {
                        throw new Exception("No se ha podido renombrar el archivo " + ficheroActualizado.getName() + " a " + CURASIGFILENAME);
                    }

                } else {
                    throw new Exception("No se ha podido eliminar el fichero " + destFichero.getName() + " necesario para reeditarlo.");
                }
                

            } catch (FileNotFoundException ex) {//Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }catch(IOException ieo){
                System.out.println("Se ha producido un error: " + ieo.getMessage());
                sc.nextLine();
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                sc.nextLine();
                System.out.println("Si desea eliminar alguna asignatura de la lista introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo asignaturas

            } finally {
                try {
                    if (fichero != null) {
                        fichero.close();
                    }
                    if (ficheroNuevo != null) {
                        ficheroNuevo.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

        } while (repetir);

    }
    
    
    /**
     * Se da de alta a una asignatura en el fichero cursosAsignaturas.txt
     */
    public static void altaCursoAsignatura() {        
        String codCurso, codCursoAsignatura, nombreAsignatura, cadena, continuar;        
        RandomAccessFile fichero = null; 
        long size = 0;
        int indice;
        boolean repetir = false;
        
        do {

            try {
                System.out.println("Introduzca el código de la asignatura:");
                codCursoAsignatura = sc.nextLine();
                
                 if (codCursoAsignatura.isEmpty()) {
                    throw new Exception("Debe introducir el código de la asignatura.");
                }
                codCurso = codCursoAsignatura.substring(0, 2).toUpperCase(); // Obtenemos el código del curso
                
                               
                System.out.println("Introduzca el nombre de la asignatura:");
                nombreAsignatura = sc.nextLine();                
               
                if (nombreAsignatura.isEmpty()) {
                    throw new Exception("Debe introducir el nombre de la asignatura.");
                }

                crearFichero(DIRECTORY, CURASIGFILENAME); //Se crea el fichero si no existe
                
                fichero = new RandomAccessFile(CURASIGFILEPATH, "rw");
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if(indice != -1){                        
                        if (cadena.substring(0, indice).equalsIgnoreCase(codCursoAsignatura)) //Obtenemos el código de la asignatura
                            throw new Exception("El código del curso ya se encuentra en la lista");
                    }
                    cadena = fichero.readLine();                    
                }                
                size = fichero.length(); 
                fichero.seek(size);// nos situamos al final del fichero
                cadena = codCursoAsignatura.toUpperCase() + "," + nombreAsignatura + "\n";
                fichero.writeBytes(cadena);
                
               
                System.out.println("Se ha añadido correctamente la asignatura en el fichero.");
                System.out.println("Si desea añadir más asignaturas al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo asignaturas
                
            } catch (FileNotFoundException ex) {     //Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                sc.nextLine();
            } catch (IOException ex) {
                repetir = true;
                Logger.getLogger("Ha ocurrido una excepción: " + CursoAsignatura.class.getName()).log(Level.SEVERE, null, ex);
                sc.nextLine();
            } catch (Exception ex) {                
                repetir = true;
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                sc.nextLine();
                System.out.println("Si desea añadir más asignaturas al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo asignaturas
            } finally{
                if (fichero != null) {
                    try {
                        fichero.close();
                    } catch (IOException ex) {
                        Logger.getLogger("Ha ocurrido una excepción: " +CursoAsignatura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } while (repetir);

    }
    
    
    /**
     * Imprime por pantalla las asignaturas que se encuentran en el fichero
     * @return String codigo y nombre de las asignaturas
     */
    
    public static String imprimeCursosAsignaturas() {
        String cadena;
        FileReader fr = null;
        BufferedReader entrada = null;
        StringBuilder cursos = new StringBuilder();
        
        try {           
            
            crearFichero(DIRECTORY, CURASIGFILENAME); //creamos un nuevo fichero si no existe
            
            fr = new FileReader(CURASIGFILEPATH);
            entrada = new BufferedReader(fr);
            cadena = entrada.readLine();
            
            if(cadena == null) throw new IOException("El fichero se encuentra vacio.");
            
            while(cadena != null){
                cursos.append(cadena);
                cursos.append("\n");
                cadena = entrada.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CursoAsignatura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
             System.out.println("\n\tExcepción: " + ex.getMessage());
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(CursoAsignatura.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return cursos.toString();
    }
    
    /**
     * SUBMENU
     */
    public static void subMenuCursoAsignaturas(){
        int opcion = 0;
        boolean continuar = true;
        
        do{             
            System.out.println("\n*************** MANTENIMIENTO DE ASIGNATURAS ***************\n");
            System.out.println("\t1. ALTA DE ASIGNATURAS");
            System.out.println("\t2. BAJA DE ASIGNATURAS");
            System.out.println("\t3. MOSTRAR ASIGNATURAS");            
            System.out.println("\t0. SALIR ASIGNATURAS");            
            System.out.print("\n\t   Opcion seleccionada: ");
            
            opcion = sc.nextInt();
            
            switch(opcion){
                case 0: default:
                    sc.nextLine(); //Limpiamos el Buffer
                    continuar = false;
                    break;
                case 1:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n1. ALTA DE ASIGNATURAS");
                    altaCursoAsignatura();
                    break;
                case 2:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n2. BAJA DE ASIGNATURAS");
                    bajaCursoAsignatura();
                    break;
                case 3:
                    System.out.println("\n3. MOSTRAR ASIGNATURAS");
                    System.out.println(imprimeCursosAsignaturas());                   
                    break;
            }
            
        }while(continuar);
        
    }
    
     /**
     * Crea el fichero, pasado por parametro, si no existe
     * @param ruta direccion de la ubicacion del fichero
     * @param fichero nombre del fichero
     * @throws IOException 
     */
    public static void crearFichero(String ruta, String fichero) throws IOException {
        File cursoRuta = new File(ruta);
        File cursoFichero = new File(cursoRuta, fichero);
        
        if (! cursoFichero.exists()) {

            System.out.println("El fichero " + cursoFichero.getAbsolutePath() + " no existe.");

            if (!cursoRuta.exists()) {
                System.out.println("El directorio " + cursoRuta.getAbsolutePath() + " no existe.");

                if (cursoRuta.mkdir()) {
                    System.out.println("Se ha creado el directorio " + cursoRuta.getAbsolutePath());

                    if (cursoFichero.createNewFile()) {
                        System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                    } else {
                        throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                    }

                } else {
                    throw new IOException("No se ha podido crear la ruta " + cursoRuta.getAbsolutePath());
                }
            } else {

                if (cursoFichero.createNewFile()) {
                    System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                } else {
                    throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                }

            }
        }
    }
}