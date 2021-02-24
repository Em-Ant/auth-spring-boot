package it.emant.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import it.emant.auth.model.Key;
import it.emant.auth.model.Role;
import it.emant.auth.model.Roles;
import it.emant.auth.model.User;
import it.emant.auth.repository.KeyRepository;
import it.emant.auth.repository.RoleRepository;
import it.emant.auth.repository.UserRepository;
import it.emant.auth.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	@Autowired
	@Profile("local")
	CommandLineRunner init(
		RoleRepository roles,
		UserRepository users,
		KeyRepository keys
	) {

		return args -> {

			log.info("initializing db...");
			Key key = null;
			Role admin = null;
			Role moderator = null;
			Role user = null;

			if (roles.findByName(Roles.ADMIN).isEmpty()) {
				admin = new Role();
				admin.setName(Roles.ADMIN);
				admin = roles.save(admin);
				log.info("{} role created", admin.getName());
			} ;
			

			if (roles.findByName(Roles.MODERATOR).isEmpty()) {
				moderator = new Role();
				moderator.setName(Roles.MODERATOR);
				moderator = roles.save(moderator);
				log.info("{} role created", moderator.getName());
			} ;


			if (roles.findByName(Roles.USER).isEmpty()) {
				user = new Role();
				user.setName(Roles.USER);
				user = roles.save(user);
				log.info("{} role created", user.getName());
			} ;

			if (keys.findAll().size() == 0) {
				key = new Key();
				String generated = KeyGenerator.generate();
				key.setKey(generated);

				Set<Role> set = Stream.of(admin, moderator, user).collect(Collectors.toSet());
				key.setRoles(set);

				key = keys.save(key);
				log.info("key {} created", key.getKey());
			}

			if (users.findAll().size() == 0) {
				User _user = new User();
				_user.setEmail("user@test.com");
				_user.setUsername("emant");
				_user.setKeys(new HashSet<Key>(Arrays.asList(key)));
				users.save(_user);
				log.info("user {} created and assigned {}", _user.getUsername(), key.getKey());
			}
		};
	}
}

