package utils;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;

public class AgenciaFixture {

    public static AgenciaHttp criarAgenciaHttp(String status) {
        return new AgenciaHttp("Agencia Teste", "Razao social da Agencia Teste", "123", status);
    }

    public static Agencia criarAgencia() {
        Endereco endereco = new Endereco(1L, "Rua de teste", "Logradouro de teste", "Complemento de teste", 1);
        return new Agencia(1L, "Agencia Teste", "Razao social da Agencia Teste", "123", endereco);
    }
}
