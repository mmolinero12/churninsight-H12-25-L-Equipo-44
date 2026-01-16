package org.hackaton.oracle.churninsight.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroCredenciales(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {}
