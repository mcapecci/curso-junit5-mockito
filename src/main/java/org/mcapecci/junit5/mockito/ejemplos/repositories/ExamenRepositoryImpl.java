package org.mcapecci.junit5.mockito.ejemplos.repositories;

import org.mcapecci.junit5.mockito.ejemplos.models.Examen;
import org.mcapecci.junit5.mockito.ejemplos.services.MockUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl.guardar");
        return MockUtil.EXAMEN;
    }

    @Override
    public List<Examen> findaAll() {
        System.out.println("ExamenRepositoryImpl.findaAll");
        try {
            System.out.println("ExamenRepositoryImpl");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return MockUtil.EXAMEN_LIST;
    }
}
