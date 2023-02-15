package sigaa.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sigaa.entidades.Curso;
import sigaa.entidades.Departamento;
import sigaa.entidades.Formacao;
import sigaa.entidades.Materia;
import sigaa.sistema.Sistema;
import sigaa.usuario.Administrador;
import sigaa.usuario.Discente;
import sigaa.usuario.Docente;

public class testing_001 {
	
	private Sistema sigaav2;
	
	@Before
	public void setup() {
		sigaav2 = Sistema.getInstance();
	}
	
	@Test
	public void TS001() {
		Administrador adm = new Administrador(sigaav2,
				"adm001",
				"12345678",
				"guilherme.santos@discente.ufg.br",
				"Guilherme Santos");
		assertEquals(adm.logar("12345678", sigaav2), true);
	}
	
	@Test
	public void TS002() {
		Discente dis = new Discente(sigaav2,
				"julio002",
				"87654321",
				"julio.cesar@discente.ufg.br",
				"Julio Cesar",
				"202105041",
				Curso.ES);
		assertEquals(dis.logar("87654321", sigaav2), true);
	}

	@Test
	public void TS003() {
		Docente doc = new Docente(sigaav2,
				"bruno003",
				"45685219",
				"bruno.fernandes@docente.ufg.br",
				"Bruno Fernandes",
				Formacao.MESTRADO,
				Departamento.INF);
		assertEquals(doc.logar("45685219", sigaav2), true);
	}

	@Test
	public void TS004() {
		Administrador adm = new Administrador(sigaav2,
				"adm001",
				"12345678",
				"guilherme.santos@discente.ufg.br",
				"Guilherme Santos");

		Docente docente = adm.cadastraDocente(sigaav2,
				"jorge004",
				"456321987",
				"jorge.guimaraes@docente.ufg.br",
				"Jorge Guimaraes",
				Formacao.LICENCIATURA,
				Departamento.ICB);

		assertTrue(sigaav2.getUsuarios(adm).contains(docente));
	}
	
	@Test
	public void TS005() {
		Materia materia = new Materia(
				"INF0042",
				"Testes de Software",
				64,
				"4N2345",
				"CAB107, CAB104",
				45
				);
		
		assertTrue(materia instanceof Materia);
	}
	
	@Test
	public void TS006() {
		Sistema ponteiro1 = Sistema.getInstance();
		Sistema ponteiro2 = Sistema.getInstance();
		
		assertTrue(sigaav2 == ponteiro1);
		assertTrue(ponteiro1 == ponteiro2);
	}

}