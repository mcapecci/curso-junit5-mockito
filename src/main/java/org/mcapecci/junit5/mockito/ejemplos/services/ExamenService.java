package org.mcapecci.junit5.mockito.ejemplos.services;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;

public interface ExamenService {
    Examen findExamenPorNombre(String nombre);
}
