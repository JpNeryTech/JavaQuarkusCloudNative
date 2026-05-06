package br.com.alura.service;

import br.com.alura.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class AgeciaServiceTest {

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject
    private AgenciaService agenciaService;

    // Método executado antes de cada teste para preparar o ambiente,
    // mockando o persist para evitar acesso ao banco de dados
    @BeforeEach
    public void SetUp() {
        Mockito.doNothing().when(agenciaRepository).persist(Mockito.any(Agencia.class));
    }

    @Test
    public void deveNaoCadastrarQuandoClientRetornarNull() {
        Agencia agencia = criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtiva() {
        Agencia agencia = criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(criarAgenciaHttp());

        agenciaService.cadastrar(agencia);

        Mockito.verify(agenciaRepository).persist(agencia);
    }

    @Test
    public void deveNaoCadastrarQuandoClientRetornarSituacaoCadastralInativa() {
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(agenciaHttpInativa);
    }

    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1L, "Quadra", "Teste", "Teste", 1);
        return new Agencia(1L, "Agencia Teste", "Razão Agencia Teste", "123", endereco);
    }

    private AgenciaHttp criarAgenciaHttp() {
        return new AgenciaHttp("Agencia Teste", "Razão Agencia Teste", "123", "ATIVO");
    }

    private AgenciaHttp criarAgenciaHttpInativa() {
        return new AgenciaHttp("Agencia Teste", "Razao social da Agencia Teste", "123", "INATIVO");
    }

    private AgenciaHttp agenciaHttpInativa = criarAgenciaHttpInativa();
}
