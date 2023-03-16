package ovh.major.joboffers.domain.loginandregister;

import lombok.Builder;

@Builder
record User(
        String id,
        String name,
        String password
) {
}