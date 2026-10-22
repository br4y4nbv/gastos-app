package com.br4y4nbv.gastos_app.service;

import com.br4y4nbv.gastos_app.dto.GastoForm;
import com.br4y4nbv.gastos_app.model.Categoria;
import com.br4y4nbv.gastos_app.model.Gasto;
import com.br4y4nbv.gastos_app.model.Usuario;
import com.br4y4nbv.gastos_app.repository.CategoriaRepository;
import com.br4y4nbv.gastos_app.repository.GastoRepository;
import com.br4y4nbv.gastos_app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    // TEMPORAL: Despues saldrá del usuario autenticado
    private Usuario usuarioActual() {
        return usuarioRepository.findByEmail("demo@gastos.com").orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<Gasto> listar(Pageable pageable) {
        return gastoRepository.findByUsuarioId(usuarioActual().getId(), pageable);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findByUsuarioId(usuarioActual().getId());
    }

    @Transactional(readOnly = true)
    public GastoForm obtenerForm(Long id) {
        Gasto gasto = gastoRepository.findByIdAndUsuarioId(id, usuarioActual().getId())
                .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));

        GastoForm form = new GastoForm();
        form.setId(gasto.getId());
        form.setDescripcion(gasto.getDescripcion());
        form.setMonto(gasto.getMonto());
        form.setFecha(gasto.getFecha());
        form.setCategoriaId(gasto.getCategoria() != null ? gasto.getCategoria().getId() : null);
        return form;
    }

    @Transactional
    public void guardar(GastoForm form) {
        Usuario usuario = usuarioActual();

        Gasto gasto = (form.getId() == null)
                ? new Gasto()
                : gastoRepository.findByIdAndUsuarioId(form.getId(), usuario.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Gasto no encontrado"));

        Categoria categoria = categoriaRepository.findById(form.getCategoriaId())
                .filter(c -> c.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Categoría inválida"));

        gasto.setDescripcion(form.getDescripcion());
        gasto.setMonto(form.getMonto());
        gasto.setFecha(form.getFecha());
        gasto.setCategoria(categoria);
        gasto.setUsuario(usuario);
        gastoRepository.save(gasto);
    }

    @Transactional
    public void eliminar(Long id) {
        gastoRepository.findByIdAndUsuarioId(id, usuarioActual().getId())
                .ifPresent(gastoRepository::delete);
    }
}