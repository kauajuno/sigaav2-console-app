package sigaa.entidades;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import sigaa.usuario.Discente;
import sigaa.usuario.Usuario;

public class Atividade {
	private @Getter String nome;
	private @Getter LocalDate dataPostagem;
	private @Getter LocalDate dataEntrega;
	private @Getter String descricao;
	private @Getter int peso;
	private @Getter Map<Discente,String> entregas = new TreeMap<>();
	private Map<Discente,Float> notas = new TreeMap<>();
	
	public Atividade(LocalDate dataPostagem, LocalDate dataEntrega, String descricao, int peso) {
		this.dataPostagem = dataPostagem;
		this.dataEntrega = dataEntrega;
		this.descricao = descricao;
		this.peso = peso;
	}
	
	public void setDataEntrega(LocalDate novaData){
		this.dataEntrega = novaData;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Map<Discente,Float> getNotas(Usuario usuario){
		if(usuario instanceof Discente)
			return Collections.unmodifiableMap(notas);
		return notas;
	}
	

}
