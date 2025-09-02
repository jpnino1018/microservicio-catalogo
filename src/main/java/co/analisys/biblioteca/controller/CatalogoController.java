package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class CatalogoController {
    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @Operation(summary = "Obtener libro por ID", description = "Devuelve un libro específico según su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public Libro obtenerLibro(@PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro disponible"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @Operation(summary = "Verificar disponibilidad de libro", description = "Verifica si un libro está disponible")
    @GetMapping("/{id}/disponible")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public boolean isLibroDisponible(@PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @Operation(summary = "Actualizar disponibilidad de libro", description = "Actualiza la disponibilidad de un libro")
    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void actualizarDisponibilidad(@PathVariable String id, @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libros encontrados"),
        @ApiResponse(responseCode = "400", description = "Criterio de búsqueda inválido")
    })
    @Operation(summary = "Buscar libros", description = "Busca libros según un criterio específico")
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
