package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.Usuario;
import br.com.fiap.pedro.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Usuario criar(String nome, String email, String telefone) {
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Já existe usuário com o e-mail " + email);
        }
        return repository.save(new Usuario(nome, email.toLowerCase(), telefone));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    @Transactional
    public Usuario atualizar(Long id, String nome, String email, String telefone) {
        Usuario usuario = buscar(id);
        if (!usuario.getEmail().equalsIgnoreCase(email) && repository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Já existe usuário com o e-mail " + email);
        }
        usuario.atualizar(nome, email.toLowerCase(), telefone);
        return usuario;
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }
}
