package org.hackaton.oracle.churninsight.web.controller;

import jakarta.validation.Valid;
import org.hackaton.oracle.churninsight.infra.service.UsuarioService;
import org.hackaton.oracle.churninsight.web.dto.usuario.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                     // Todas las anotaciones @  son de springframework excepto validaciones
@RequestMapping("/usuario")
public class UsuarioController {


    /*
     * Se elimina @Autowired debido a los siguientes problemas:
     *    - Dificulta testing
     *    - Rompe inmutabilidad
     *    - Oculta dependencias
     *
     *  Se recomienda un constructor injection
     */
    private final UsuarioService service;


    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaUsuario>> listar(
            @PageableDefault(size=2, sort={"apellidoPaterno"}) Pageable paginacion
    ){   //Pageable de springframework
        return ResponseEntity.ok(service.listar(paginacion));
    }

    @PutMapping
    public ResponseEntity<DatosDetalleUsuario>  actualizar(
            @RequestBody @Valid DatosActualizacionUsuario datos,@PathVariable Long id
    ) {
        return ResponseEntity.ok(service.actualizar(datos,id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleUsuario> detallar(@PathVariable Long id){
        return ResponseEntity.ok(service.detallar(id));
    }


}
