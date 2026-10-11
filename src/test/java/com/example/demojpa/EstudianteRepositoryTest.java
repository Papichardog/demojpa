package com.example.demojpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.example.demojpa.dao.Estudiante;
import com.example.demojpa.repository.EstudianteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2") // fuerza el uso de H2 en memoria
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // datasource embebido en los tests
public class EstudianteRepositoryTest {

    @Autowired
    EstudianteRepository repository;

    @Test
    void guardar_y_encontrar_por_id() {
        Estudiante e = new Estudiante(100L, "Pablo");
        Estudiante saved = repository.save(e);

        Optional<Estudiante> found = repository.findById(100L);
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Pablo");
    }

    @Test
    void actualizar_nombre() {
        repository.save(new Estudiante(101L, "Ana"));
        Estudiante e = repository.findById(101L).orElseThrow();
        e.setNombre("Ana María");
        repository.save(e);

        Estudiante updated = repository.findById(101L).orElseThrow();
        assertThat(updated.getNombre()).isEqualTo("Ana María");
    }

    @Test
    void eliminar_por_id() {
        repository.save(new Estudiante(102L, "Luis"));
        repository.deleteById(102L);
        assertThat(repository.findById(102L)).isEmpty();
    }
}
