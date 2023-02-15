package sigaa.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lombok.Getter;
import lombok.Setter;
import sigaa.entidades.Atividade;
import sigaa.entidades.Departamento;
import sigaa.entidades.Formacao;
import sigaa.entidades.Materia;
import sigaa.entidades.Situacao;
import sigaa.sistema.Sistema;

public class Docente extends Usuario{
	
	private @Getter @Setter Formacao formacao;
	private @Getter @Setter Departamento departamento;
	private List<Materia> materias = new ArrayList<>();
	
	public Docente(Sistema sistema, String login, String senha, String email, String nome, Formacao formacao, Departamento departamento) {
		super(login, senha, email, nome);
		this.formacao = formacao;
		this.departamento = departamento;
		sistema.adicionaUsuario(this);
	}
	
	public void cadastraAtividade(Materia materia, LocalDate dataEntrega, String descricao, int peso) {
		List<Atividade> m = materia.getListaDeAtividades(this);
		m.add(new Atividade(LocalDate.now(), dataEntrega, descricao, peso));
	}
	
	public List<Materia> getMateriasDocente(Usuario usuario){
		if(usuario instanceof Administrador)
			return this.materias;
		return Collections.unmodifiableList(this.materias);
	}
	
	public void editaFrequencia(Materia materia) {
		
		Scanner sc = new Scanner(System.in);
		int res, presencas;
		int ch = materia.getCh();
		int aulasMinistradas = materia.getAulasMinistradas();
		Map<Discente,Integer> ldf = materia.getListaDeFrequencia(this);
		
		System.out.println("Carga horária da matéria: " + ch);
		System.out.println("Aulas ministradas até o momento: " + aulasMinistradas);
		
		if(aulasMinistradas == ch) {
			System.out.println("Todas as aulas dessa matéria já foram ministradas.");
			System.out.println("Todas as frequências já estão registradas.");
			do {
				System.out.println("Deseja realizar o fechamento da matéria? 1 - Sim, 2 - Não");
				res = sc.nextInt();
			}while(res != 1 || res != 2);
			if(res == 1) concluiMateria(materia);
			sc.close();
			return;
		}
		
		System.out.println("Quantas presenças serão contabilizadas para cada aluno presente?");
		presencas = sc.nextInt();
		
		if((presencas + aulasMinistradas) > ch) {
			System.out.println("Não é possível registrar essa quantidade de frequências");
			sc.close();
			return;
		}
		
		for(Discente d : materia.getAlunos(this)) {
			System.out.println(d.getNome() + " está presente? 1 - Sim, 2 - Não");
			res = sc.nextInt();
			while(res != 1 || res != 2) {
				System.out.println("Resposta inválida.");
				System.out.println(d.getNome() + " está presente? 1 - Sim, 2 - Não");
			}
			if(res == 1)
				ldf.put(d, ldf.get(d) + presencas);
		}
		
		materia.setAulasMinistradas(materia.getAulasMinistradas() + presencas);
		aulasMinistradas = materia.getAulasMinistradas();
		
		if(aulasMinistradas == ch) {
			System.out.println("Todas as aulas dessa matéria já foram ministradas.");
			System.out.println("Todas as frequências já estão registradas.");
			System.out.println("Deseja realizar o fechamento da matéria? 1 - Sim, 2 - Não");
			res = sc.nextInt();
			while(res != 1 || res != 2) {
				System.out.println("Resposta inválida.");
				System.out.println("Deseja realizar o fechamento da matéria? 1 - Sim, 2 - Não");
			}
			if(res == 1) concluiMateria(materia);
			sc.close();
			return;
		}
		
		sc.close();
	}
	
	public void avaliaAtividade(Materia materia) {
		
		Scanner sc = new Scanner(System.in);
		int i = 0, res;
		List<Atividade> listaAtividades = materia.getListaDeAtividades(this);
		
		for(Atividade atividade : listaAtividades) {
			System.out.println(i++ + " - " + atividade.getNome());
		}
		System.out.println(i + " - SAIR");
		do {
			System.out.println("Selecione o número correspondente à atividade que deseja avaliar.");
			res = sc.nextInt();
		}while(res < 1 ||res > i);
		
		if(res == i) {
			sc.close();
			return;
		}
		
		Atividade atividade = listaAtividades.get(res - 1);
		List<Discente> alunos = materia.getAlunos(this);
		Map<Discente,String> mapAtividades = atividade.getEntregas();
		Map<Discente,Float> notas = atividade.getNotas(this);
		String entrega;
		Float nota;
		
		for(Discente aluno : alunos) {
			entrega = mapAtividades.get(aluno);
			if(entrega == null)
				notas.put(aluno, (float) 0.0);
			else {
				System.out.println(entrega);
				System.out.println("Insira a nota para a entrega de " + aluno.getNome() + ":");
				nota = sc.nextFloat();
				notas.put(aluno, nota);
			}
		}
		
		sc.close();
	}
	
	public void concluiMateria(Materia materia) {
		
		int ch = materia.getCh();
		int aulasMinistradas = materia.getAulasMinistradas();
		
		if(ch != aulasMinistradas) {
			System.out.println("A matéria não pode ser concluída ainda.");
			System.out.println("Carga Horária: " + ch);
			System.out.println("Aulas ministradas: " + aulasMinistradas);
			return;
		}
		
		List<Atividade> listaAtividades = materia.getListaDeAtividades(this);
		List<Discente> listaAlunos = materia.getAlunos(this);
		Map<Discente, Integer> listaDeFrequencia = materia.getListaDeFrequencia(this);
		Map<Discente, Situacao> resultadoFinal = materia.getListaDeSituacao(this);
		
		int pesoTotalAtividades = 0;
		
		for(Atividade atividade : listaAtividades) {
			pesoTotalAtividades += atividade.getPeso();
		}
		
		float soma, nota;
		
		for(Discente aluno : listaAlunos) {
			if(listaDeFrequencia.get(aluno) < 0.75) {
				resultadoFinal.put(aluno, Situacao.REPROVADOPORFALTA);
			}else {
				soma = 0;
				nota = 0;
				for(Atividade atividade : listaAtividades) {
					soma += atividade.getNotas(this).get(aluno) * atividade.getPeso();
				}
				nota = soma / pesoTotalAtividades;
				if(nota < 6.0)
					resultadoFinal.put(aluno, Situacao.REPROVADOPORMEDIA);
				else
					resultadoFinal.put(aluno, Situacao.APROVADO);
			}
		}
		
	}
}
