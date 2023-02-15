package sigaa.usuario;

import lombok.AllArgsConstructor;
import sigaa.sistema.Sistema;

@AllArgsConstructor
public abstract class Usuario {
	private String login;
	private String senha;
	private String email;
	private String nome;
	
	public boolean logar(String senha, Sistema sistema) {
		return senha.equals(this.senha) && sistema.getUsuarios(this).contains(this);
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public boolean setSenha(String novaSenha, String senha) {
		if(this.senha == senha) {
			this.senha = novaSenha;
			return true;
		}
		return false;
	}
}
