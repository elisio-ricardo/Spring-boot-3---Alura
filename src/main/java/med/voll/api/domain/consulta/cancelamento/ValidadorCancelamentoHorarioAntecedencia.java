package med.voll.api.domain.consulta.cancelamento;


import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorCancelamentoHorarioAntecedencia implements ValidadorCancelamentoDeConsulta{

   @Autowired
   private ConsultaRepository repository;

    @Override
    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = repository.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();
        var diferencaDeHoras = Duration.between(agora, consulta.getData()).toHours();

        if(diferencaDeHoras < 24){
            throw new ValidacaoException("Consulta pode ser cancelada com antecedencia de 24h! ");
        }


    }
}
