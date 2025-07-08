package com.cantinasa.cantinasa.controller;

import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.AdminDTO;
import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.dto.ProdutoRequest;
import com.cantinasa.cantinasa.model.enums.role;
import com.cantinasa.cantinasa.repository.UsuarioRepository;
import com.cantinasa.cantinasa.service.ProdutoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);
    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioRepository userRepository;

    private ResponseEntity<?> validateAdmin(Long userId, String password) {
        Usuario usuario = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if (usuario.getRole() != role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores têm acesso");
        }

        return ResponseEntity.ok("Acesso autorizado");
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@Valid @RequestBody ProdutoRequest request) {
        System.out.println("0");

        validateAdmin(request.getUserId(), request.getPassword());


        System.out.println("1");
        ProdutoDTO produtoCadastrado = produtoService.cadastrarProduto(request.getProduto());
        System.out.println("2");
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);


    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<ProdutoDTO> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable(name = "id") Long id) {
        ProdutoDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        
         validateAdmin(request.getUserId(), request.getPassword());

        ProdutoDTO produtoAtualizado = produtoService.atualizarProduto(id, request.getProduto());
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        
        ResponseEntity<?> validationResponse = validateAdmin(request.getUserId(), request.getPassword());
        if (validationResponse != null) {
            return validationResponse;
        }

        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/estoque")
    public ResponseEntity<?> atualizarEstoque(
            @PathVariable("id") Long id,
            @RequestParam("quantidade") int quantidade,
            @Valid @RequestBody AdminDTO admin) {

        ResponseEntity<?> validationResponse = validateAdmin(admin.getUserId(), admin.getPassword());

        if (!validationResponse.getStatusCode().is2xxSuccessful()) {
            return validationResponse;
        }

        ProdutoDTO produtoAtualizado = produtoService.atualizarEstoque(id, quantidade);
        return ResponseEntity.ok(produtoAtualizado);
    }

}
