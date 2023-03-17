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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Habilitar el uso de anotaciones, 2 maneras posibles:
 * 1. MockitoAnnotations.openMocks(this); en el setUp @BeforeEach
 * 2. @ExtendWith(MockitoExtension.class) clase anotada (importante tener mockito-junit-jupiter en el pom)
 */
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {
    @Spy
    ExamenRepositoryImpl repository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;

    /**
     * Mock vs Spy
     *
     * Mock: podemos utilizar la interfaz, o una clase abstracta o la clase concreta
     * los métodos siempre se van a simular.
     *
     * Spy: vamos a simular algunos métodos pero no todos, algunos métodos le vamos a cambiar la implementación,
     * la vamos a simular por una falsa pero no todos. Entonces los métodos que se llamen de forma real tienen que ser
     * un objeto real, un objeto concreto a partir de una clase. Ej.: spy(RepositoryImpl.class) y no spy(Repository.class) ya que esta es la interfaz.
     */


    /**
     * Spy
     * <p>
     * es un híbrido entre un objeto real y un mock
     * <p>
     * permite invocar sin definir ningún when, ningún simulacro, no tenemos que mockear ningún método desde Spy
     * <p>
     * cuando invocamos un método, va a ir al método real
     * <p>
     * podemos convivir entre un mock y un objeto real, aunque no es el objeto real 100% sino es un clon del objeto real con
     * caracteristicas de un mock
     * <p>
     * no hay que abusar de los spy xq no tenemos el control 100% de lo que pueda ocurrir en ese contexto, en esa llamada real
     * <p>
     * no se deberian usar mucho
     * <p>
     * requiere que se cree a partir de una clase concreta, no desde una clase abstracta o desde una interfaz, porque
     * va a llamar a métodos reales y si llamamos a una interfaz ese metodo real no va a estar implementado, por lo tanto la prueba
     * va a fallar xq va a devolver null.
     */
    @Test
    void testSpy() {
        /*
        No son necesarios ya que los inyectamos con la anotación @Spy

        ExamenRepository repository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(repository, preguntaRepository);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matemáticas");
         */

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testSpyHibrido() {

        List<String> preguntas = Arrays.asList("aritmética");
        /*
          Aca tenemos el hibrido
          -findAll hace una llama real de ExamenRepository
          -preguntaRepository hace una invocación falsa de findPreguntasPorExamenId, es un MOCK
          por lo tanto no va a invocar al método real.
          Se recomienda usar el doReturn antes del when ya que dentro del when preguntaRepository.findPreguntasPorExamenId se realiza una llamada real y en el service se invoca con el mock
          when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(MockUtil.PREGUNTA_LIST);
         */
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        // examenRepository.findaAll() se llama de forma real
        verify(repository).findaAll();
        // preguntaRepository.findPreguntasPorExamenId(anyLong()) se llama de forma simulada
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }
}