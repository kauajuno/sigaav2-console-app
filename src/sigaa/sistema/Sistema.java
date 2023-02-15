package sigaa.sistema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sigaa.entidades.Materia;
import sigaa.usuario.Administrador;
import sigaa.usuario.Usuario;

public class Sistema {
	private static Sistema instance;
	private List<Usuario> usuarios = new ArrayList<>();
	private List<Materia> materias = new ArrayList<>();
	
	private Sistema() {
		
	}
	
	public static Sistema getInstance() {
		if(instance == null) {
			instance = new Sistema();
		}
		return instance;
	}
	
	public void adicionaUsuario(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	public List<Materia> getMaterias(Usuario usuario){
		if(usuario instanceof Administrador)
			return this.materias;
		return Collections.unmodifiableList(this.materias);
	}
	
	public List<Usuario> getUsuarios(Usuario usuario){
		if(usuario instanceof Administrador)
			return this.usuarios;
		return Collections.unmodifiableList(usuarios);
	}
}
