package org.mcapecci.junit5.mockito.ejemplos.repositories;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findaAll();
}
