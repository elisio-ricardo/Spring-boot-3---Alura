package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DadosDetalhamentoconsulta
        (Long id, Long idMedico, Long idPaciente, LocalDateTime data) {
    public DadosDetalhamentoconsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(),
                consulta.getPaciente().getId(), consulta.getData());
    }
}
