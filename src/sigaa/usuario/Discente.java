package sigaa.usuario;

import sigaa.entidades.Atividade;
import sigaa.entidades.Curso;
import sigaa.entidades.Materia;
import sigaa.sistema.Sistema;

import java.util.List;
import java.util.Scanner;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Discente extends Usuario implements Comparable<Discente>{
	public Discente(Sistema sistema, String login, String senha, String email, String nome, String matricula, Curso curso) {
		super(login, senha, email, nome);
		this.matricula = matricula;
		this.curso = curso;
		sistema.adicionaUsuario(this);
	}
	private @Getter String matricula;
	private @Getter Curso curso;
	private @Getter Integer indicePrioridade;
	private List<Materia> materias = new ArrayList<Materia>();
	
	public void matriculaMateria(Materia materia) {
		materia.joinListaDeEspera(this);
	}
	
	public List<Materia> getMaterias(Usuario usuario){
		if(usuario instanceof Administrador)
			return this.materias;
		return Collections.unmodifiableList(this.materias);
	}

	
	
	public void entregarTarefa() {
		
		if(this.materias.size() == 0) {
			System.out.println("Não há matérias para que uma entrega seja realizada sobre.");
			return;
		}
		
		int i = 1, res;
		Scanner sc = new Scanner(System.in);
		System.out.println("A entrega deve ser feita para qual matéria?");
		for(Materia materia : materias) {
			System.out.println(i++ + " - " + materia.getNome());
		}
		System.out.println(i + " - SAIR");
		do {
			System.out.println("Selecione o número correspondente à matéria para a qual deseja realizar uma entrega.");
			res = sc.nextInt();
		}while(res < 1 || res > i);
		
		if(res == i) {
			sc.close();
			return;
		}
		
		Materia materiaEntrega = materias.get(res - 1);
		List<Atividade> listaAtividades = materiaEntrega.getListaDeAtividades(this);
		
		if(listaAtividades.size() == 0) {
			System.out.println("Não há atividades cadastradas nessa matéria.");
			sc.close();
			return;
		}
		
		i = 1;
		for(Atividade atividade : listaAtividades) {
			System.out.println(i++ + " - " + atividade.getNome());
			System.out.println("Data de postagem: " + atividade.getDataPostagem());
			System.out.print("Data de entrega: " + atividade.getDataEntrega());
			if(atividade.getDataEntrega().isBefore(LocalDate.now()))
				System.out.print("--- DATA LIMITE ULTRAPASSADA ---");
			System.out.println("\n");
		}
		System.out.println(i + " - SAIR");
		do {
			System.out.println("Selecione o número correspondente à atividade que será entregue.");
			res = sc.nextInt();
		}while(res < 1 || res > i);
		
		if(res == i) {
			sc.close();
			return;
		}
		
		Atividade atividadeEntrega = listaAtividades.get(res - 1);
		
		if(atividadeEntrega.getDataEntrega().isBefore(LocalDate.now())) {
			System.out.println("O limite de data para entrega dessa atividade já passou.");
			sc.close();
			return;
		}
		
		String entrega;
		
		System.out.println("Insira o material para a entrega.");
		entrega = sc.nextLine();
		
		atividadeEntrega.getEntregas().put(this, entrega);
		
		System.out.println("Sua entrega da atividade " + atividadeEntrega.getNome() + " foi registrada com sucesso.");
		sc.close();
	}
	
	@Override
	public int compareTo(Discente discente) {
		return this.getIndicePrioridade().compareTo(discente.getIndicePrioridade());
	}
}
