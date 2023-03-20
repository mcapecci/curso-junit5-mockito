package com.mcapecci.test.springboot.app.controllers;

import static org.springframework.http.HttpStatus.*;

import com.mcapecci.test.springboot.app.models.Cuenta;
import com.mcapecci.test.springboot.app.models.TransaccionDto;
import com.mcapecci.test.springboot.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Para acceder a la UI: http://localhost:8080/swagger-ui/
 * Desde localhost:8080/v2/api-docs accedemos a la documentación de nuestra aplicación
 */
@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(OK)
    public List<Cuenta> listar() {
        return cuentaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<?>  detalle(@PathVariable Long id) {
        Cuenta cuenta = null;
        try {
            cuenta = cuentaService.findById(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto) {
        cuentaService.transferir(dto.getCuentaOrigenId(),
                dto.getCuentaDestinoId(),
                dto.getMonto(), dto.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito!");
        response.put("transaccion", dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cuentaService.deleteById(id);
    }

}
