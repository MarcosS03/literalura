package br.com.literalura.literalura;

import br.com.literalura.literalura.principal.Principal;
import br.com.literalura.literalura.repository.LivroRepository;
import br.com.literalura.literalura.service.AutorService;
import br.com.literalura.literalura.service.LivroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final LivroService livroService;

	private final AutorService autorService;


	public LiteraluraApplication(LivroService livroService, AutorService autorService, LivroRepository livroRepository) {
        this.livroService = livroService;
        this.autorService = autorService;
    }

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livroService, autorService);

		principal.exibeMenu();
	}
}
