package org.mcapecci.junit5.mockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntasPorExamenId(Long id);
}
