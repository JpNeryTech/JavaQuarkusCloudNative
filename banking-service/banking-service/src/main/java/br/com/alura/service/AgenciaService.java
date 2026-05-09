package br.com.alura.service;

import br.com.alura.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;
    private final MeterRegistry meterRegistry;
    AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    public void cadastrar(Agencia agencia){
        AgenciaHttp agenciaHttp =
                situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if(agenciaHttp != null &&
                agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)){
            Log.info("A agencia com o CNPJ" +agencia.getCnpj()+ " foi cadastrada");
            meterRegistry.counter("agencia_adicionada_counter").increment();
            agenciaRepository.persist(agencia);
        } else {
            Log.info("A agencia com o CNPJ" +agencia.getCnpj()+ " não foi cadastrada");
            meterRegistry.counter("agencia_nao_adicionada_counter").increment();
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        Log.info("A agencia " + id + " foi deletada");
        agenciaRepository.deleteById(id);
    }

    @Transactional
    public void alterar(Agencia agencia) {
     Log.info("A agencia com o CNPJ" + agencia.getCnpj() + " foi alterada" );
     agenciaRepository.update("nome = ?1, razãoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());
    }
}
