package com.AdrianChS.lecturaFichero;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class LecturaFicheroApplication implements CommandLineRunner {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private DAOVideojuegos daoVideojuegos;

  public static void main(String[] args) {
    SpringApplication.run(LecturaFicheroApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Scanner s = new Scanner(System.in);
    // Troceamos los datos de un fichero CSV y lo añadimos a una lista del tipo de nuestra clase bean////
    List<String> lineas = FileUtils.readLines(
        new File("C:\\Users\\adrian.chamorrosilva\\Documents\\DUAL\\lecturaficheros.txt"),
        Charset.defaultCharset());
    List<EscrituraTablaVideojuego> videojuegos = new ArrayList<>(); // List<tipo de lista (objeto,
                                                                    // string, etc)>

    for (String linea : lineas) {
      String[] word = linea.split(",");
      EscrituraTablaVideojuego videojuego = new EscrituraTablaVideojuego();
      videojuego.setTitulo(word[0]);
      videojuego.setPlataforma(word[1]);
      videojuego.setDesarrolladora(word[2]);
      videojuego.setPublisher(word[3]);
      videojuego.setPrecio(Float.parseFloat(word[4]));
      videojuego.setStock(Integer.parseInt(word[5]));
      videojuegos.add(videojuego); // Con el método .add Lo que hacemos es añadir cada atributo del
                                   // objeto a la lista
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Hacemos print de cada elemento de nuestra lista(que sigue siendo desde fichero)////////////////////
    System.out.println("Lectura de videojuegos desde fichero");
    for (EscrituraTablaVideojuego videojuego : videojuegos) {
      System.out.println(videojuego);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Insertamos o actualizamos en función de la existencia de los elementos de la lista y los de BD/////
    System.out.println("Insertamos desde fichero usando funcion de comprobacion de elementos");
    for (EscrituraTablaVideojuego videojuego : videojuegos) {
      if (daoVideojuegos.comprobarDatos(videojuego)) {
        System.out.println("Existen datos de " + videojuego.getTitulo());
        daoVideojuegos.actualizarDatos(videojuego);
      } else {
        System.out.println("NO Existen datos de " + videojuego.getTitulo());
        daoVideojuegos.insertarDatos(videojuego);
      }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Comprobamos que se han cargado correctamente los datos en BD(ya no es lectura desde fichero)///////
    System.out.println("Comprobaciones");
    List<EscrituraTablaVideojuego> tabla = daoVideojuegos.verTabla();
    System.out.println(tabla);
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Comrpobación de elementos desde un diccionario/////////////////////////////////////////////////////
    Map<Integer, EscrituraTablaVideojuego> m = new HashMap<>();
    System.out.println();
    for (EscrituraTablaVideojuego mapeo : tabla) {
      m.put(mapeo.getCodVideojuegos(), mapeo);
    }
    System.out.print("Por favor, introduzca un código: ");
    int codigoIntroducido = Integer.parseInt(s.nextLine());
    if (m.containsKey(codigoIntroducido)) {
      System.out.print("El código " + codigoIntroducido + " corresponde a ");
      System.out.println(m.get(codigoIntroducido));
    } else {
      System.out.println("El código introducido no existe.");
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
  }
}
