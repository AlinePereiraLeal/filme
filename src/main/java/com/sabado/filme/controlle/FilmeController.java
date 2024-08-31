package com.sabado.filme.controlle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sabado.filme.model.Filme;
import com.sabado.filme.repo.FilmeRepository;

@Controller
@RequestMapping("/filme")

public class FilmeController {

	@Autowired
	private FilmeRepository filmeRepo;
	// http://localhost:8080 /filme/
	private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/";

	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("filmes", filmeRepo.findAll());
		return "index";
	}

	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("filme", new Filme());
		return "form";
	}

	@GetMapping("/form/{id}")
	public String form(@PathVariable("id") Long id, Model model) {
		Optional<Filme> filme = filmeRepo.findById(id);
		if (filme.isPresent()) {
			model.addAttribute("filme", filme.get());
		} else {
			model.addAttribute("filme", new Filme());
		}
		return "form";
	}

	@PostMapping("/add")
	public String addFilme(@RequestParam("id") Optional<Long> id, @RequestParam("nome") String nome,
			@RequestParam("data") String data, @RequestParam("img") MultipartFile imagem) {
		Filme filme;
		if (id.isPresent()) {
			filme = filmeRepo.findById(id.get()).orElse(new Filme());
		} else {
			filme = new Filme();
		}
		filme.setNome(nome);
		filme.setData(Date.valueOf(data));

		filmeRepo.save(filme); // Salvar dentro do banco de dados

		// Salvar a imagem
		if (!imagem.isEmpty()) {
			try {
				// Logica para salvar a imagem
				String fileName = "filme_" + filme.getId() + "_" + imagem.getOriginalFilename();
				Path path = Paths.get(UPLOAD_DIR + fileName);
				Files.write(path, imagem.getBytes());
				filme.setImagem("/" + fileName);

				filmeRepo.save(filme);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return "redirect:/filme/";

	}

	@GetMapping("/delere/{id}")
	public String deletString(@PathVariable("Id") Long Id) {
		Optional<Filme> filme = filmeRepo.findById(Id);

		if (filme.isPresent()) {
			Filme filmeParaDeletar = filme.get();
			String imagePath = UPLOAD_DIR + filmeParaDeletar.getImagem();
			try {
				Files.deleteIfExists(Paths.get(imagePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
			filmeRepo.deleteById(Id);
		}
		return "redirect:/filme/";
	}

}
