package br.com.literalura.literalura.service;

import br.com.literalura.literalura.dto.LivrosAutoresDTO;
import br.com.literalura.literalura.model.Autor;
import br.com.literalura.literalura.model.Livro;
import br.com.literalura.literalura.repository.AutorRepository;
import br.com.literalura.literalura.repository.LivroRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {
    @Autowired
    private final AutorRepository autorRepository;

    @Autowired
    private final LivroRepository livroRepository;

    @Autowired
    private final EntityManager entityManager;


    @Autowired
    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository, EntityManager entityManager) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.entityManager = entityManager;
    }

    /**
     * Get all authors with their respective books.
     * Obtém todos os autores com seus respectivos livros.
     */
    public List<LivrosAutoresDTO> todosAutoresLivros() {
        List<Autor> autores = autorRepository.findAll();
        return autores.stream().map(autor -> {
            List<Livro> livros = livroRepository.findByAuthorsContaining(autor);
            return new LivrosAutoresDTO(autor, livros);
        }).collect(Collectors.toList());
    }

    /**
     * Display authors with their respective books.
     * Exibe os autores com seus respectivos livros.
     */
    public void telaAutoresLivros() {
        List<LivrosAutoresDTO> autoresComLivros = todosAutoresLivros();
        List<Autor> autores = autorRepository.findAll();

        for (LivrosAutoresDTO autorComLivros : autoresComLivros) {
            System.out.println("\n------------AUTOR------------");
            System.out.println("Autor: " + autorComLivros.getAutor().getName());
            Autor autor = autorComLivros.getAutor();
            System.out.println("Ano de Nascimento: " + (autor.getBirthYear() != null ? autor.getBirthYear() : "N/A"));
            System.out.println("Ano de Falecimento: " + (autor.getDeathYear() != null ? autor.getDeathYear() : "N/A"));

            int posicaoLivro = 1;
            for (Livro livro : autorComLivros.getLivros()) {
                System.out.println("Livro - " + posicaoLivro + " : " + livro.getTitle());
                posicaoLivro++;
            }
            System.out.println("-----------------------------\n");
        }
    }

    /**
     * Display authors alive in a given year along with their books.
     * Exibe os autores vivos em um determinado ano juntamente com seus livros.
     */
    @Transactional
    public void autoresVivosAno(Integer year) {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado no banco de dados.");
            return;
        }

        boolean encontrouAutorVivo = false;

        for (Autor autor : autores) {
            if ((autor.getBirthYear() != null && autor.getBirthYear() <= year) &&
                    (autor.getDeathYear() == null || autor.getDeathYear() >= year)) {
                encontrouAutorVivo = true;
                System.out.println("\n------------ AUTOR ------------");
                System.out.println("Nome: " + autor.getName());
                System.out.println("Ano de Nascimento: " + autor.getBirthYear());
                System.out.println("Ano de Falecimento: " + (autor.getDeathYear() != null ? autor.getDeathYear() : "Vivo"));

                List<Livro> autoresLivros = autor.getLivrosEAutores();
                if (autoresLivros.isEmpty()) {
                    System.out.println("   - Nenhum livro encontrado.");
                } else {
                    int posicaoLivro = 1;
                    for (Livro livro : autoresLivros) {
                        System.out.println("Livro " + posicaoLivro + ": " + livro.getTitle());
                        posicaoLivro++;
                    }
                }
                System.out.println("------------------------------\n");
            }
        }

        if (!encontrouAutorVivo) {
            System.out.println("Nenhum autor vivo encontrado no ano " + year + ".");
        }
    }

    /**
     * Find authors by name and display their information along with their books.
     * Encontra autores pelo nome e exibe suas informações juntamente com seus livros.
     */


    private String reverseName(String name) {
        String[] parts = name.split(" ");
        if (parts.length > 1) {
            return parts[parts.length - 1] + ", " + String.join(" ", Arrays.copyOf(parts, parts.length - 1));
        }
        return name;
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]+");
    }


}
