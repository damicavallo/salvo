package com.accenture.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(PlayerRepository repository,GameRepository game,GamePlayerRepository gamePlayer
			,ShipRepository ship,SalvoRepository salvo,ScoreRepository score) {
		return (args) -> {
			Player player1= new Player("j.bauer@ctu.gov","24");
			Player player2= new Player("c.obrian@ctu.gov","42");
			Player player3= new Player("kim_bauer@gmail.com","kb");
			Player player4= new Player("t.almeida@ctu.gov","mole");

			repository.save(player1);
			repository.save(player2);
			repository.save(player3);
			repository.save(player4);

			Game game1= new Game();
			Game game2= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600)));
			Game game3= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*2)));
			Game game4= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*3)));
			Game game5= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*4)));
			Game game6= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*5)));
			Game game7= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*6)));
			Game game8= new Game(Date.from(game1.getFechaCreacion().toInstant().plusSeconds(3600*7)));

			game.save(game1);
			game.save(game2);
			game.save(game3);
			game.save(game4);
			game.save(game5);
			game.save(game6);
			game.save(game7);
			game.save(game8);


			GamePlayer gameplayer1 = new GamePlayer(game1,player1);
			GamePlayer gameplayer2 = new GamePlayer(game1,player2);
			GamePlayer gameplayer3 = new GamePlayer(game2,player1);
			GamePlayer gameplayer4 = new GamePlayer(game2,player2);
			GamePlayer gameplayer5 = new GamePlayer(game3,player2);
			GamePlayer gameplayer6 = new GamePlayer(game3,player4);
			GamePlayer gameplayer7 = new GamePlayer(game4,player2);
			GamePlayer gameplayer8 = new GamePlayer(game4,player1);
			GamePlayer gameplayer9 = new GamePlayer(game5,player4);
			GamePlayer gameplayer10 = new GamePlayer(game5,player1);
			GamePlayer gameplayer11 = new GamePlayer(game6,player3);
			GamePlayer gameplayer12 = new GamePlayer(game7,player4);
			GamePlayer gameplayer13 = new GamePlayer(game8,player3);
			GamePlayer gameplayer14 = new GamePlayer(game8,player4);


			gamePlayer.save(gameplayer1);
			gamePlayer.save(gameplayer2);
			gamePlayer.save(gameplayer3);
			gamePlayer.save(gameplayer4);
			gamePlayer.save(gameplayer5);
			gamePlayer.save(gameplayer6);
			gamePlayer.save(gameplayer7);
			gamePlayer.save(gameplayer8);
			gamePlayer.save(gameplayer9);
			gamePlayer.save(gameplayer10);
			gamePlayer.save(gameplayer11);
			gamePlayer.save(gameplayer12);
			gamePlayer.save(gameplayer13);
			gamePlayer.save(gameplayer14);


			Ship ship1= new Ship("Destroyer",gameplayer1,Arrays.asList("H2","H3","H4"));
			Ship ship2= new Ship("Submarine",gameplayer1,Arrays.asList("E1","F1","G1"));
			Ship ship3= new Ship("Patrolboat",gameplayer1,Arrays.asList("B4","B5"));
			Ship ship4= new Ship("Destroyer",gameplayer2,Arrays.asList("B5","C5","D5"));
			Ship ship5= new Ship("Patrolboat",gameplayer2,Arrays.asList("F1","F2"));
			Ship ship6= new Ship("Destroyer",gameplayer3,Arrays.asList("B5","C5","D5"));
			Ship ship7= new Ship("Patrolboat",gameplayer3,Arrays.asList("C6","C7"));
			Ship ship8= new Ship("Submarine",gameplayer4,Arrays.asList("A2","A3","A4"));
			Ship ship9= new Ship("Patrol",gameplayer4,Arrays.asList("G6","H6"));
			Ship ship10= new Ship("Destroyer",gameplayer5,Arrays.asList("B5","C5","D5"));
			Ship ship11= new Ship("Patrol",gameplayer5,Arrays.asList("C6","C7"));
			Ship ship12= new Ship("Submarine",gameplayer6,Arrays.asList("A2","A3","A4"));
			Ship ship13= new Ship("Patrol",gameplayer6,Arrays.asList("G6","H6"));



			gameplayer1.ships.add(ship1);
			gameplayer1.ships.add(ship2);
			gameplayer1.ships.add(ship3);
			gameplayer2.ships.add(ship4);
			gameplayer2.ships.add(ship5);
			gameplayer3.ships.add(ship6);
			gameplayer3.ships.add(ship7);
			gameplayer4.ships.add(ship8);
			gameplayer4.ships.add(ship9);
			gameplayer5.ships.add(ship10);
			gameplayer5.ships.add(ship11);
			gameplayer6.ships.add(ship12);
			gameplayer6.ships.add(ship13);


			ship.save(ship1);
			ship.save(ship2);
			ship.save(ship3);
			ship.save(ship4);
			ship.save(ship5);
			ship.save(ship6);
			ship.save(ship7);
			ship.save(ship8);
			ship.save(ship9);
			ship.save(ship10);
			ship.save(ship11);
			ship.save(ship12);
			ship.save(ship13);

			Salvo salvo1=new Salvo(1,gameplayer1,Arrays.asList("B5", "C5", "F1"));
			Salvo salvo2=new Salvo(1,gameplayer2,Arrays.asList("B4", "B5", "B6"));
			Salvo salvo3=new Salvo(2,gameplayer1,Arrays.asList("F2","D5"));
			Salvo salvo4=new Salvo(2,gameplayer2,Arrays.asList("E1","H3","A2"));
			Salvo salvo5=new Salvo(1,gameplayer3,Arrays.asList("A2","A4","G6"));
			Salvo salvo6=new Salvo(1,gameplayer4,Arrays.asList("B5","D5","C7"));
			Salvo salvo7=new Salvo(2,gameplayer3,Arrays.asList("A3","H6"));
			Salvo salvo8=new Salvo(2,gameplayer4,Arrays.asList("C5","C6"));
			Salvo salvo9=new Salvo(1,gameplayer5,Arrays.asList("G6","H6","A4"));
			Salvo salvo10=new Salvo(1,gameplayer6,Arrays.asList("H1","H2","H3"));
			Salvo salvo11=new Salvo(2,gameplayer5,Arrays.asList("A2","A3","D8"));
			Salvo salvo12=new Salvo(2,gameplayer6,Arrays.asList("E1","F2","G3"));

			gameplayer1.salvoes.add(salvo1);
			gameplayer1.salvoes.add(salvo3);
			gameplayer2.salvoes.add(salvo2);
			gameplayer2.salvoes.add(salvo4);
			gameplayer3.salvoes.add(salvo5);
			gameplayer3.salvoes.add(salvo7);
			gameplayer4.salvoes.add(salvo6);
			gameplayer4.salvoes.add(salvo8);
			gameplayer5.salvoes.add(salvo9);
			gameplayer5.salvoes.add(salvo11);
			gameplayer6.salvoes.add(salvo10);
			gameplayer6.salvoes.add(salvo12);

			salvo.save(salvo1);
			salvo.save(salvo2);
			salvo.save(salvo3);
			salvo.save(salvo4);
			salvo.save(salvo5);
			salvo.save(salvo6);
			salvo.save(salvo7);
			salvo.save(salvo8);
			salvo.save(salvo9);
			salvo.save(salvo10);
			salvo.save(salvo11);
			salvo.save(salvo12);

			Score score1= new Score(game1,player1,1.0);
			Score score2= new Score(game1,player2,0.0);
			Score score3= new Score(game2,player1,0.5);
			Score score4= new Score(game2,player2,0.5);
			Score score5= new Score(game3,player2,1.0);
			Score score6= new Score(game3,player4,0.0);

			score.save(score1);
			score.save(score2);
			score.save(score3);
			score.save(score4);
			score.save(score5);
			score.save(score6);
		};
	}
}

@Configuration
class WebAuthSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/web/game.html").hasAuthority("USER")
				.antMatchers("/api/game_view").hasAuthority("USER")
				.antMatchers("/web/js/game.js").hasAuthority("USER")
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.anyRequest().fullyAuthenticated();

		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}



