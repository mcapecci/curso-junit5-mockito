package org.mcapecci.junit5.mockito.app.repositories;


import org.mcapecci.junit5.mockito.app.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
