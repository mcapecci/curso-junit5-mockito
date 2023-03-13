package org.mcapecci.junit5.mockito.ejemplos.services;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;

import java.util.Optional;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
    Examen findExamenPorNombreConPreguntas(String nombre);

    Examen guardar(Examen examen);
}
