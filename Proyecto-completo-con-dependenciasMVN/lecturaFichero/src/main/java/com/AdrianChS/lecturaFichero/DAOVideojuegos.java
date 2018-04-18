package com.AdrianChS.lecturaFichero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DAOVideojuegos {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<EscrituraTablaVideojuego> verTabla() {
    List<EscrituraTablaVideojuego> lista = new ArrayList<EscrituraTablaVideojuego>();
    lista = jdbcTemplate.query("select * from videojuegos",
        new BeanPropertyRowMapper<>(EscrituraTablaVideojuego.class));

    return lista;
  }

  private Integer codVideojuego() {

    Integer codMayor =
        jdbcTemplate.queryForObject("Select MAX(CodVideojuegos) from videojuegos", Integer.class)
            + 1;
    return codMayor;
  }

  public void insertarDatos(EscrituraTablaVideojuego videojuego) {
    jdbcTemplate.update("insert into videojuegos values " + "(?, ?, ?, ?, ?, ?, ?)",
        codVideojuego(), videojuego.getTitulo(), videojuego.getPlataforma(),
        videojuego.getDesarrolladora(), videojuego.getPublisher(), videojuego.getPrecio(),
        videojuego.getStock());
  }

  public void actualizarDatos(EscrituraTablaVideojuego vjAct) {
    jdbcTemplate.update(
        "update videojuegos set desarrolladora = ?, precio = ?, stock = ? where titulo = ? AND"
            + " plataforma = ? AND publisher = ?",
        vjAct.getDesarrolladora(), vjAct.getPrecio(), vjAct.getStock(), vjAct.getTitulo(),
        vjAct.getPlataforma(), vjAct.getPublisher());
  }

  public boolean comprobarDatos(EscrituraTablaVideojuego juego) {
    String sql = "SELECT * FROM videojuegos WHERE titulo= ? AND publisher= ? AND plataforma = ?";
    List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql, juego.getTitulo(),
        juego.getPublisher(), juego.getPlataforma());
    if (lista != null && lista.size() >= 1) {
      return true;
    } else {
      return false;
    }
  }
}
