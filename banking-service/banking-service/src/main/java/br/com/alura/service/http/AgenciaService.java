package br.com.alura.service.http;

import br.com.alura.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.repository.AgenciaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;
    AgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    public void cadastrar(Agencia agencia){
        AgenciaHttp agenciaHttp =
                situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if(agenciaHttp != null &&
                agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)){
            agenciaRepository.persist(agencia);
        } else {
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
    }

    @Transactional
    public void alterar(Agencia agencia) {
        Agencia entidadeExiste = agenciaRepository.findById(agencia.getId());

        if(entidadeExiste != null) {
            entidadeExiste.setNome(agencia.getNome());
            entidadeExiste.setRazaoSocial(agencia.getRazaoSocial());
            entidadeExiste.setCnpj(agencia.getCnpj());
        } else {
            throw new IllegalStateException("Agencia com ID" +agencia.getId() + "não encontrada");
        }
    }
}
