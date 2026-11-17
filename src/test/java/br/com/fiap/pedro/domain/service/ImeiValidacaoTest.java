package br.com.fiap.pedro.domain.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ImeiValidacaoTest {
    @ParameterizedTest
    @ValueSource(strings = {"490154203237518", "356938035643809", "352099001761481"})
    void imeisValidos(String imei) {
        assertThat(DispositivoService.imeiValido(imei)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"490154203237519", "123", "abcdefghijklmno", "4901542032375180"})
    void imeisInvalidos(String imei) {
        assertThat(DispositivoService.imeiValido(imei)).isFalse();
    }
}
