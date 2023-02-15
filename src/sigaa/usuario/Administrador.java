package sigaa.usuario;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import sigaa.entidades.Curso;
import sigaa.entidades.Departamento;
import sigaa.entidades.Formacao;
import sigaa.entidades.Materia;
import sigaa.sistema.Sistema;

public class Administrador extends Usuario{

	public Administrador(Sistema sistema, String login, String senha, String email, String nome) {
		super(login, senha, email, nome);
		sistema.adicionaUsuario(this);
	}
	
	public void matriculaMateria(Materia materia, Sistema sistema) {
		sistema.getMaterias(this).add(materia);
	}
	
	public Administrador cadastraAdministrador(Sistema sistema, String login, String senha, String email, String nome) {
		return new Administrador(sistema, login, senha, email, nome);
	}
	
	public Docente cadastraDocente(Sistema sistema, String login, String senha, String email, String nome, Formacao formacao, Departamento departamento) {
		return new Docente(sistema, login, senha, email, nome, formacao, departamento);
	}
	
	public Discente cadastraDiscente(Sistema sistema, String login, String senha, String email, String nome, String matricula, Curso curso) {
		return new Discente(sistema, login, senha, email, nome, matricula, curso);
	}
	
	public void aprovaMatricula(Sistema sistema) {
		Scanner sc = new Scanner(System.in);
		List<Materia> materias = sistema.getMaterias(this);
		Discente discenteHolder;
		int input, i = 0;
		for(Materia materia : materias) {
			System.out.println(i++ + " - " + materia);
		}
		System.out.println("1 - Escolher materia especifica\n2 - Iterar sobre todas as materias");
		input = sc.nextInt();
		if(input == 1) {
			System.out.println("Escolha o index da matéria:");
			input = sc.nextInt();
			Materia m = materias.get(input);
			if(m.getAlunos(this).size() < m.getVagas() && m.getListaDeEspera(this).size() > 0) {
				List<Discente> lde = m.getListaDeEspera(this);
				Collections.sort(lde);
				System.out.println("Os seguintes alunos serão matriculados na matéria " + m);
				i = m.getVagas() - m.getAlunos(this).size();
				for(int j = 0; j < i; j++)
					System.out.println(lde.get(lde.size() - 1 - j));
				System.out.println("1 - Confirma\n2- Sair");
				input = sc.nextInt();
				if(input == 1) {
					lde = m.getListaDeEspera(this);
					while(i > 0 || !lde.isEmpty()) {
						discenteHolder = lde.get(lde.size() - 1);
						m.getAlunos(this).add(discenteHolder);
						m.getListaDeFrequencia(this).put(discenteHolder, 0);
						discenteHolder.getMaterias(this).add(m);
						lde.remove(lde.size() -1);
						i--;
					}
				}
			}else {
				System.out.println("Não há matrículas para serem aprovadas na matéria " + m);
			}
		}else {
			materias.forEach((m) -> {
				if(m.getAlunos(this).size() < m.getVagas() && !m.getListaDeEspera(this).isEmpty()) {
					List<Discente> lde = m.getListaDeEspera(this);
					Collections.sort(lde);
					int j = m.getVagas() - m.getAlunos(this).size();
					while(j > 0 || !lde.isEmpty()) {
						m.getAlunos(this).add(lde.get(lde.size() - 1));
						m.getListaDeFrequencia(this).put(lde.get(lde.size() - 1), 0);
						lde.get(lde.size() - 1).getMaterias(this).add(m);
						lde.remove(lde.size() - 1);
						j--;
					}
				}
			});
		}
		sc.close();
	}
	
	public void associaDocente(Docente docente, Materia materia) {
		materia.setDocente(docente);
		docente.getMateriasDocente(this).add(materia);
	}
	
}
