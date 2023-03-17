package org.mcapecci.junit5.mockito.ejemplos.services;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class MockUtil {
    public final static List<Examen> EXAMEN_LIST =  Arrays.asList(
            new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia")
    );

    public final static List<Examen> EXAMEN_LIST_ID_NULL =  Arrays.asList(
            new Examen(null, "Matemáticas"), new Examen(null, "Lenguaje"),
            new Examen(null, "Historia")
    );

    public final static List<String> PREGUNTA_LIST = Arrays.asList("aritmética",
            "integrales", "derivadas", "trigonometría", "geometría");

    public final  static Examen EXAMEN = new Examen(null, "Física");
}
