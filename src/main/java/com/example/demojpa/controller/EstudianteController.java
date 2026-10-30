package com.example.demojpa.controller;

import com.example.demojpa.dao.Estudiante;
import com.example.demojpa.repository.EstudianteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository repository;

    public EstudianteController(EstudianteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Estudiante estudiante) {
        if (estudiante.getId() == null) {
            return ResponseEntity.badRequest().body("El id es obligatorio");
        }
        if (repository.existsById(estudiante.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un estudiante con ese ID");
        }
        Estudiante saved = repository.save(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Estudiante incoming) {
        return repository.findById(id)
                .<ResponseEntity<?>>map(e -> {
                    e.setNombre(incoming.getNombre());
                    Estudiante saved = repository.save(e);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
