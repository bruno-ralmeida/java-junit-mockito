package br.com.alura.leilao.service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class GeradorDePagamentoTest {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Mock
    private Clock clock;

    @Captor
    private ArgumentCaptor<Pagamento> captor;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);

        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
    }

    @Test
    public void deveriaCriarUmPagamentoParaOVecedorDoLeilaoNaSexta(){
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        LocalDate date = LocalDate.of(2022,1,6);
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
        assertEquals(new BigDecimal("600"), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    public void deveriaCriarUmPagamentoParaOVecedorDoLeilaoNaSegundaQuandoOLeilaoForSexta(){
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        LocalDate date = LocalDate.of(2022,1,7);
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(3), pagamento.getVencimento());
        assertEquals(new BigDecimal("600"), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    public void deveriaCriarUmPagamentoParaOVecedorDoLeilaoNaSegundaQuandoOLeilaoForSabado(){
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        LocalDate date = LocalDate.of(2022,1,8);
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(2), pagamento.getVencimento());
        assertEquals(new BigDecimal("600"), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    @Test
    public void deveriaCriarUmPagamentoParaOVecedorDoLeilaoNaSegundaQuandoOLeilaoForDomingo(){
        Leilao leilao = leilao();
        Lance vencedor = leilao.getLanceVencedor();
        LocalDate date = LocalDate.of(2022,1,9);
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Mockito.when(clock.instant()).thenReturn(instant);
        Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        geradorDePagamento.gerarPagamento(vencedor);

        Mockito.verify(pagamentoDao).salvar(captor.capture());

        Pagamento pagamento = captor.getValue();

        assertEquals(LocalDate.now(clock).plusDays(1), pagamento.getVencimento());
        assertEquals(new BigDecimal("600"), pagamento.getValor());
        assertFalse(pagamento.getPago());
        assertEquals(vencedor.getUsuario(), pagamento.getUsuario());
        assertEquals(leilao, pagamento.getLeilao());
    }

    private Leilao leilao(){


        Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Fulano"));

        Lance primeiro = new Lance(new Usuario("Beltrano"), new BigDecimal("600"));

        leilao.propoe(primeiro);

        leilao.setLanceVencedor(primeiro);

        return leilao;

    }
}