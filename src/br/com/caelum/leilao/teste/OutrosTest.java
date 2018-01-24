package br.com.caelum.leilao.teste;

import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;

public class OutrosTest {

	@Test
	public void deveGerarUmCookie() {

		expect()
			.cookie("rest-assured", "funciona")
		.get("/cookie/teste");
		
	}
	
	@Test
	public void deveGerarUmHeader(){
		
		expect()
			.header("novo-header", "abc")
		.get("/cookie/teste");
		
	}
	
	
//	given().
//    	multiPart(new File("/path/para/arquivo")).
//    when().
//    	post("/upload");

}
