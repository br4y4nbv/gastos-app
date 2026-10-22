package com.br4y4nbv.gastos_app.service;

import com.br4y4nbv.gastos_app.dto.GastoForm;
import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Gasto;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import com.br4y4nbv.gastos_app.repository.GastoSpecs;
import com.br4y4nbv.gastos_app.dto.GastoFiltro;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// Lombok: crea un constructor con los campos "final".
@RequiredArgsConstructor  // Spring lo usa para inyectar los repositorios (inyección por constructor)
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    // TEMPORAL: Despues saldrá del usuario autenticado
    private Usuario usuarioActual() {
        return usuarioRepository.findByEmail("demo@gastos.com").orElseThrow();
    }

    // readOnly = true: avisa a la BD que solo se lee (pequeña optimización)
    // y mantiene la sesión abierta mientras se cargan datos LAZY.
    @Transactional(readOnly = true)
    public Page<Gasto> listar(GastoFiltro filtro, Pageable pageable) {
        var spec = GastoSpecs.filtrar(
                usuarioActual().getId(),
                filtro.getDesde(),
                filtro.getHasta(),
                filtro.getCategoriaId());
        return gastoRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByUsuarioId(usuarioActual().getId());
    }

    // Convierte un Gasto (entidad) en GastoForm para rellenar el formulario de edición.
    @Transactional(readOnly = true)
    public GastoForm obtenerForm(Long id) {
        // Busca por id Y usuario: si el gasto no es del usuario, lanza error
        Gasto gasto = gastoRepository.findByIdAndUsuarioId(id, usuarioActual().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));

        GastoForm form = new GastoForm();
        form.setId(gasto.getId());
        form.setDescripcion(gasto.getDescripcion());
        form.setMonto(gasto.getMonto());
        form.setFecha(gasto.getFecha());
        // La categoría puede ser null, por eso se comprueba antes de pedir su id
        form.setCategoriaId(gasto.getCategoria() != null ? gasto.getCategoria().getId() : null);
        return form;
    }

    // Sirve para CREAR y para EDITAR: depende de si el form trae id o no.
    // @Transactional: si algo falla a mitad, se deshacen todos los cambios.
    @Transactional
    public void guardar(GastoForm form) {
        Usuario usuario = usuarioActual();

        // Operador ternario: 
        // - Sin id: se crea un Gasto nuevo
        // - Con id: se busca el existente (y se verifica que sea del usuario)
        Gasto gasto = (form.getId() == null)
                ? new Gasto()
                : gastoRepository.findByIdAndUsuarioId(form.getId(), usuario.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));

        // Busca la categoría y verifica que pertenezca al usuario.
        // Sin este filtro, alguien podría manipular el formulario y usar
        // una categoría de otra persona.
        Categoria categoria = categoriaRepository.findById(form.getCategoriaId())
                .filter(c -> c.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Categoría inválida"));

        // Copia los datos del formulario a la entidad
        gasto.setDescripcion(form.getDescripcion());
        gasto.setMonto(form.getMonto());
        gasto.setFecha(form.getFecha());
        gasto.setCategoria(categoria);
        gasto.setUsuario(usuario);

        // save() hace INSERT si el gasto es nuevo y UPDATE si ya existe
        gastoRepository.save(gasto);
    }

    @Transactional
    public void eliminar(Long id) {
        // ifPresent: solo borra si existe Y es del usuario; si no, no hace nada
        // "gastoRepository::delete" es una referencia a método (forma corta de g -> delete(g))
        gastoRepository.findByIdAndUsuarioId(id, usuarioActual().getId())
                .ifPresent(gastoRepository::delete);
    }
}