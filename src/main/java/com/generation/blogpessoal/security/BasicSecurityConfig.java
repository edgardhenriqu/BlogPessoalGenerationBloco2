package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		
		auth.inMemoryAuthentication()
			.withUser("root")
			.password(passwordEncoder().encode("root"))
			.authorities("ROLE_USER");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/**
		 * antMatchers().permitAll -> Endpoint liberado de autenticação
		 * 
		 * HttpMethod.OPTIONS -> O parâmetro HttpMethod.OPTIONS permite que 
		 * o cliente (frontend), possa descobrir quais são as opções de 
		 * requisição permitidas para um determinado recurso em um servidor. 
		 * Nesta implementação, está sendo liberada todas as opções das 
		 * requisições através do método permitAll().
		 * 
		 * anyRequest().authenticated() -> Todos os demais endpoints 
		 * serão autenticados
		 * 
		 * httpBasic -> Tipo de autenticação http (Basic Security)
		 * 
		 * Http Sessions: As sessões HTTP são um recurso que permite 
		 * que os servidores da Web mantenham a identidade do usuário 
		 * e armazenem dados específicos do usuário durante várias 
		 * interações (request/response) entre um aplicativo 
		 * cliente e um aplicativo da Web.
		 * 
		 * sessionManagement() -> Cria um gerenciador de Sessões
		 * 
		 * sessionCreationPolicy(SessionCreationPolicy.STATELESS) -> Define
		 * como o Spring Secuiryt irá criar (ou não) as sessões
		 * 
		 * STATELESS -> Nunca será criada uma sessão, ou seja, basta enviar
		 * o token através do cabeçalho da requisição que a mesma será processada.
		 * 
		 * cors -> O compartilhamento de recursos de origem cruzada (CORS) surgiu 
		 * porquê os navegadores não permitem solicitações feitas por um domínio
		 * (endereço) diferente daquele de onde o site foi carregado. Desta forma o 
		 * Frontend da aplicação, por exemplo, obrigatoriamente teria que estar 
		 * no mesmo domínio que o Backend. Habilitando o CORS, o Spring desabilita 
		 * esta regra e permite conexões de outros domínios.
		 * 
		 * CSRF: O cross-site request forgery (falsificação de 
		 * solicitação entre sites), é um tipo de ataque no qual comandos não 
		 * autorizados são transmitidos a partir de um usuário em quem a 
		 * aplicação web confia. 
		 * 
		 * csrf().disabled() -> Esta opção de proteção é habilitada por padrão no 
		 * Spring Security, entretanto precisamos desabilitar, caso contrário, todos 
		 * os endpoints que respondem ao verbo POST não serão executados.
		 * 
		 */
		
		http.authorizeRequests()
		.antMatchers("/usuario/logar").permitAll()
		.antMatchers("/usuario/cadastrar").permitAll()
		.antMatchers(HttpMethod.OPTIONS).permitAll()
		.anyRequest().authenticated()
		.and().httpBasic()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().cors()
		.and().csrf().disable();
		
	}

}
