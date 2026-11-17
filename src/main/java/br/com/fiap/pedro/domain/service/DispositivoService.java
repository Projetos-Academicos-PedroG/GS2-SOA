package br.com.fiap.pedro.domain.service;

import br.com.fiap.pedro.domain.model.Dispositivo;
import br.com.fiap.pedro.domain.model.Usuario;
import br.com.fiap.pedro.domain.repository.DispositivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DispositivoService {
    private final DispositivoRepository repository;
    private final UsuarioService usuarioService;

    public DispositivoService(DispositivoRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Dispositivo cadastrar(Long usuarioId, String marca, String modelo, String imei,
                                 String operadora, String numeroLinha) {
        if (!imeiValido(imei)) {
            throw new RegraNegocioException("IMEI inválido (dígito verificador não confere): " + imei);
        }
        if (repository.existsByImei(imei)) {
            throw new RegraNegocioException("IMEI já cadastrado: " + imei);
        }
        Usuario usuario = usuarioService.buscar(usuarioId);
        return repository.save(new Dispositivo(usuario, marca, modelo, imei, operadora, numeroLinha));
    }

    @Transactional(readOnly = true)
    public List<Dispositivo> listarDoUsuario(Long usuarioId) {
        usuarioService.buscar(usuarioId);
        return repository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Dispositivo buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Dispositivo", id));
    }

    @Transactional(readOnly = true)
    public Dispositivo consultarPorImei(String imei) {
        return repository.findByImei(imei).orElseThrow(() -> new RecursoNaoEncontradoException("Dispositivo com IMEI", imei));
    }

    @Transactional
    public Dispositivo marcarRecuperado(Long id) {
        Dispositivo d = buscar(id);
        d.marcarComoRecuperado();
        return d;
    }

    public static boolean imeiValido(String imei) {
        if (imei == null || !imei.matches("\\d{15}")) return false;
        int soma = 0;
        for (int i = 0; i < 15; i++) {
            int digito = imei.charAt(i) - '0';
            if (i % 2 == 1) {
                digito *= 2;
                if (digito > 9) digito -= 9;
            }
            soma += digito;
        }
        return soma % 10 == 0;
    }
}
