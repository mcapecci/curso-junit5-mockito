package org.mcapecci.junit5.mockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mcapecci.junit5.mockito.ejemplos.models.Examen;
import org.mcapecci.junit5.mockito.ejemplos.repositories.ExamenRepository;
import org.mcapecci.junit5.mockito.ejemplos.repositories.PreguntaRepository;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {
    ExamenRepository repository;

    PreguntaRepository preguntaRepository;
    ExamenService service;
    @BeforeEach
    void setUp() {
        repository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        when(repository.findaAll()).thenReturn(MockUtil.examenList);
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.get().getNombre());
    }
    @Test
    void findExamenPorNombreEmptyList() {
        List<Examen> examenList =  Collections.emptyList();

        when(repository.findaAll()).thenReturn(examenList);
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findaAll()).thenReturn(MockUtil.examenList);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.preguntaList);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findaAll()).thenReturn(MockUtil.examenList);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.preguntaList);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findaAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.preguntaList);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas2");
        assertNull(examen);
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }


}