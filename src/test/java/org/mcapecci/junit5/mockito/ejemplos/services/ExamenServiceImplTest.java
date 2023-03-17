package org.mcapecci.junit5.mockito.ejemplos.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mcapecci.junit5.mockito.ejemplos.models.Examen;
import org.mcapecci.junit5.mockito.ejemplos.repositories.ExamenRepository;
import org.mcapecci.junit5.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.mcapecci.junit5.mockito.ejemplos.repositories.PreguntaRepository;
import org.mcapecci.junit5.mockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Habilitar el uso de anotaciones, 2 maneras posibles:
 * 1. MockitoAnnotations.openMocks(this); en el setUp @BeforeEach
 * 2. @ExtendWith(MockitoExtension.class) clase anotada (importante tener mockito-junit-jupiter en el pom)
 */
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    ExamenRepositoryImpl repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;
    @Captor
    ArgumentCaptor<Long> captor;
    @InjectMocks
    ExamenServiceImpl service;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void findExamenPorNombre() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
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
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        // Given
        when(repository.findaAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);

        // When
        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas2");

        // Then
        assertNull(examen);
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testGuardarExamen() {
        // Given
        Examen newExamen = MockUtil.EXAMEN;
        newExamen.setPreguntas(MockUtil.PREGUNTA_LIST);
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        // When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matemáticas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testManejoExceptionExamenIdNull() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matemáticas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);

        service.findExamenPorNombreConPreguntas("Matemáticas");

        verify(repository).findaAll();
//        verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg.equals(5L)));
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));


    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);

        service.findExamenPorNombreConPreguntas("Matemáticas");

        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat((argument -> argument != null && argument > 0)));

    }

    @Test
    void testArgumentMatchers3() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);

        service.findExamenPorNombreConPreguntas("Matemáticas");

        verify(repository).findaAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));

    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long>{
        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "mensaje personalizado de error que imprime mockito en caso de que falle el test" +
                    argument + " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);
        service.findExamenPorNombreConPreguntas("Matemáticas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = MockUtil.EXAMEN;
        examen.setPreguntas(MockUtil.PREGUNTA_LIST);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());
        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? MockUtil.PREGUNTA_LIST : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometría"));
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());


    }

    @Test
    void testDoAnswerGuardarExamen() {
        // Given
        Examen newExamen = MockUtil.EXAMEN;
        newExamen.setPreguntas(MockUtil.PREGUNTA_LIST);
        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        // When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    /**
     * doCallRealMethod
     *
     * when you want to call the real implementation of a method.
     *
     * se cambia @Mock ExamenRepository por ExamenRepositoryImpl
     * se cambia @Mock PreguntaRepository por PreguntaRepositoryImpl
     * ya que doCallRealMethod trabaja con la implementación no con la Interfaz
     */
    @Test
    void testDoCallRealMehthod() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);

        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    void testOrdenDeInvocaciones() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);

        service.findExamenPorNombreConPreguntas("Matemáticas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(preguntaRepository);
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);

    }

    @Test
    void testOrdenDeInvocaciones2() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);

        service.findExamenPorNombreConPreguntas("Matemáticas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findaAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);

        inOrder.verify(repository).findaAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);

    }

    @Test
    void testNumeroDeInvocaciones() {
        when(repository.findaAll()).thenReturn(MockUtil.EXAMEN_LIST);
        service.findExamenPorNombreConPreguntas("Matemáticas");

        verify(preguntaRepository).findPreguntasPorExamenId(5L);
        //wanted number of invocations
        verify(preguntaRepository, times(1)).findPreguntasPorExamenId(5L);
        //min number of invocations
        verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(5L);
        //al menos una vez
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        //max number of invocations
         verify(preguntaRepository, atMost(10)).findPreguntasPorExamenId(5L);
         //como max una vez
         verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }

    @Test
    void testNumeroDeInvocaciones2() {
        when(repository.findaAll()).thenReturn(Collections.emptyList());
        service.findExamenPorNombreConPreguntas("Matemáticas");

        //never indica que no hubo interacción con ese método
        verify(preguntaRepository, never()).findPreguntasPorExamenId(5L);
        //no interactua con el mock
        verifyNoInteractions(preguntaRepository);

        //tiene interaccion
        verify(repository).findaAll();
        verify(repository, times(1)).findaAll();
        verify(repository, atLeast(1)).findaAll();
        verify(repository, atLeastOnce()).findaAll();
        verify(repository, atMost(10)).findaAll();
        verify(repository, atMostOnce()).findaAll();

    }
}