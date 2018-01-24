package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class ClientTest {

	private Usuario esperado1;
	private Usuario esperado2;
	private Usuario david;
	private Leilao leilaoPcGammer;

	@Before
	public void setup() {
		esperado1 = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		esperado2 = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");

		david = new Usuario("David", "x@x.com");
		
		leilaoPcGammer = new Leilao(10L, "PcGammer", 5000.0, david, false);

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	public void deveRetornarListaDeUsuarios() {
		XmlPath response = given().header("Accept", "application/xml").get("/usuarios").andReturn().xmlPath();

		List<Usuario> usuarios = response.getList("list.usuario", Usuario.class);

		assertEquals(esperado1, usuarios.get(0));
		assertEquals(esperado2, usuarios.get(1));
	}

	@Test
	public void deveRetornarUsuarioPeloId() {
		JsonPath path = given().header("Accept", "application/json").queryParam("usuario.id", 1).get("/usuarios/show")
				.andReturn().jsonPath();

		Usuario usuario = path.getObject("usuario", Usuario.class);

		assertEquals(esperado1, usuario);
	}

	@Test
	public void deveRetornarLeilaoPeloId() {
		JsonPath path = given().header("Accept", "application/json").queryParam("leilao.id", 1).get("/leiloes/show")
				.andReturn().jsonPath();

		String produto = path.getString("leilao.nome");

		assertEquals("Geladeira", produto);
	}

	 @Test
	 public void deveRetornarOTotalDeLeiloes() {
	 JsonPath path = given().header("Accept",
	 "application/json").get("/leiloes/total").andReturn().jsonPath();
	
	 assertEquals(2, path.getInt("int"));
	 }

	@Test
	public void deveAdicionarUmUsuario() {
		XmlPath path = given().header("Accept", "application/xml").contentType("application/xml").body(david).expect()
				.statusCode(200).when().post("/usuarios").andReturn().xmlPath();

		Usuario usuario = path.getObject("usuario", Usuario.class);

		assertEquals(david.getNome(), usuario.getNome());
		assertEquals(david.getEmail(), usuario.getEmail());

		given().contentType("application/xml").body(usuario).expect().statusCode(200).when().delete("/usuarios/deleta");
	}

	@Test
	public void deveInserirUmLeilao() {
		XmlPath path = given().header("Accept", "application/xml").contentType("application/xml").body(leilaoPcGammer).expect()
				.statusCode(200).when().post("leiloes").andReturn().xmlPath();

		Leilao leilao = path.getObject("leilao", Leilao.class);

		assertEquals(leilao.getNome(), leilaoPcGammer.getNome());

		given().contentType("application/xml").body(leilaoPcGammer).expect().statusCode(200).when().delete("leiloes/deletar");
	}

}
