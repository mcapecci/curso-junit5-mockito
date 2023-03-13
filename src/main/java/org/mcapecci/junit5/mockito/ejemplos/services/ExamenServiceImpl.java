package org.mcapecci.junit5.mockito.ejemplos.services;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;
import org.mcapecci.junit5.mockito.ejemplos.repositories.ExamenRepository;
import org.mcapecci.junit5.mockito.ejemplos.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{
    private ExamenRepository repository;

    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository repository, PreguntaRepository preguntaRepository) {
        this.repository = repository;
        this.preguntaRepository = preguntaRepository;
    }


    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return repository.findaAll().stream().filter(e -> e.getNombre().equals(nombre))
                .findFirst();

    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOpt = findExamenPorNombre(nombre);
        Examen examen = null;
        if(examenOpt.isPresent()){
            examen = examenOpt.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return repository.guardar(examen);
    }
}
