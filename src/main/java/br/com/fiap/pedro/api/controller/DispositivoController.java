package br.com.fiap.pedro.api.controller;

import br.com.fiap.pedro.api.dto.DispositivoDTO;
import br.com.fiap.pedro.domain.model.Dispositivo;
import br.com.fiap.pedro.domain.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
@Tag(name = "Dispositivos")
public class DispositivoController {
    private final DispositivoService service;

    public DispositivoController(DispositivoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra um celular do usuário (IMEI validado pelo algoritmo de Luhn)")
    public ResponseEntity<DispositivoDTO.Response> cadastrar(@RequestBody @Valid DispositivoDTO.Request req) {
        Dispositivo d = service.cadastrar(req.usuarioId(), req.marca(), req.modelo(), req.imei(),
                req.operadora(), req.numeroLinha());
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(d.getId()).toUri();
        return ResponseEntity.created(uri).body(DispositivoDTO.Response.de(d));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca dispositivo por id")
    public DispositivoDTO.Response buscar(@PathVariable Long id) {
        return DispositivoDTO.Response.de(service.buscar(id));
    }

    @GetMapping
    @Operation(summary = "Lista os dispositivos de um usuário")
    public List<DispositivoDTO.Response> listarDoUsuario(@RequestParam Long usuarioId) {
        return service.listarDoUsuario(usuarioId).stream().map(DispositivoDTO.Response::de).toList();
    }

    @GetMapping("/imei/{imei}")
    @Operation(summary = "Consulta pública: verifica se um IMEI tem registro de roubo (antes de comprar usado)")
    public DispositivoDTO.ConsultaImei consultarImei(@PathVariable String imei) {
        return DispositivoDTO.ConsultaImei.de(service.consultarPorImei(imei));
    }

    @PatchMapping("/{id}/recuperado")
    @Operation(summary = "Marca o aparelho como recuperado")
    public DispositivoDTO.Response recuperado(@PathVariable Long id) {
        return DispositivoDTO.Response.de(service.marcarRecuperado(id));
    }
}
