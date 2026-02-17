package victor.encurtadourl.app.dtos;


import jakarta.validation.constraints.NotBlank;

public record EncurtadorURLDTOs(@NotBlank String url, @NotBlank String encutrador) {
}
