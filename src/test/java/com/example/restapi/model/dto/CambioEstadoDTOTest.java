package com.example.restapi.model.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CambioEstadoDTOTest {

    @Test
    void testGettersAndSetters() {
        CambioEstadoDTO dto = new CambioEstadoDTO();
        dto.setEstado("Enviado");

        assertThat(dto.getEstado()).isEqualTo("Enviado");

        CambioEstadoDTO paramDTO = new CambioEstadoDTO("Pendiente");
        assertThat(paramDTO.getEstado()).isEqualTo("Pendiente");
    }
}
