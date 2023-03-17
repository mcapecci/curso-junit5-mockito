package org.mcapecci.junit5.mockito.app.repositories;


import org.mcapecci.junit5.mockito.app.models.Banco;

import java.util.List;

public interface BancoRepository {
    List<Banco> findAll();

    Banco findById(Long id);

    void update(Banco banco);

}
