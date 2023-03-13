package org.mcapecci.junit5.mockito.ejemplos.services;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class MockUtil {
    public final static List<Examen> examenList =  Arrays.asList(
            new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia")
    );

    public final static List<String> preguntaList = Arrays.asList("aritmética",
            "integrales", "derivadas", "trigonometría", "geometría");
}
