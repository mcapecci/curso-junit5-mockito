package org.mcapecci.junit5.mockito.ejemplos.repositories;

import org.mcapecci.junit5.mockito.ejemplos.services.MockUtil;

import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository{

    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
        return MockUtil.PREGUNTA_LIST;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVarias");
    }
}
