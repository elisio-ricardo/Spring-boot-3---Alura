package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//Notação para não auto substituir o banco de dados configurado
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void findAllByAtivoTrue() {
    }

    @Test
    @DisplayName("Deveria devolver null quando o unico medico cadastrado nao esta disponivel na data")
    void escolherMedicoAleatorioOcupadoNaData() {
        //Parar não deixar a data chumbada e dar erro no futuro
        var proximaSegundaAsDez = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = cadastrarMedico("Medico", "medico@321.com", "123212", Especialidade.CARDIOLOGIA);
        var paciente = cadastroPaciente("Paciente", "paciente.com", "123532");
        cadastrarConsulta(medico, paciente, proximaSegundaAsDez);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAsDez);
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver medico cadastrado disponivel na data")
    void escolherMedicoAleatorioLivreNaData() {
        //Parar não deixar a data chumbada e dar erro no futuro
        var proximaSegundaAsDez = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);

        var medico = cadastrarMedico("Medico", "medico@321.com", "123212", Especialidade.CARDIOLOGIA);


        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAsDez);
        assertThat(medicoLivre).isEqualTo(medico);
    }

    @Test
    void findAtivoById() {
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime date) {
        em.persist(new Consulta(null, medico, paciente, date, null));
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }


    private Paciente cadastroPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }


    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome, email, "121546321", crm, especialidade, dadosEndereco());
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome, email, "8274895032", cpf, dadosEndereco());
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xxx", "bairro", "01010101",
                "Sao Paulo", "SP", null, null);
    }
}