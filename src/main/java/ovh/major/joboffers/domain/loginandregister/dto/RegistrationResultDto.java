package ovh.major.joboffers.domain.loginandregister.dto;

public record RegistrationResultDto(
        String id,
        String name,
        boolean registered
) {
}
