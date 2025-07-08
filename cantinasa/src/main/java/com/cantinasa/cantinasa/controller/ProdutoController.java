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

/**
 * Controlador responsável por operações com produtos da cantina.
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private UsuarioRepository userRepository;

    /**
     * Valida se o usuário informado é um administrador autorizado.
     *
     * @param userId   ID do usuário.
     * @param password Senha do usuário.
     * @return ResponseEntity de autorização.
     */
    private ResponseEntity<?> validateAdmin(Long userId, String password) {
        Usuario usuario = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if (usuario.getRole() != role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores têm acesso");
        }

        return ResponseEntity.ok("Acesso autorizado");
    }

    /**
     * Cadastra um novo produto. Requer autenticação de administrador.
     *
     * @param request Objeto com dados do produto e credenciais do admin.
     * @return Produto cadastrado.
     */
    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@Valid @RequestBody ProdutoRequest request) {
        System.out.println("0");
        validateAdmin(request.getUserId(), request.getPassword());
        System.out.println("1");
        ProdutoDTO produtoCadastrado = produtoService.cadastrarProduto(request.getProduto());
        System.out.println("2");
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    /**
     * Lista todos os produtos disponíveis.
     *
     * @return Lista de produtos.
     */
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        List<ProdutoDTO> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id ID do produto.
     * @return Produto encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable(name = "id") Long id) {
        ProdutoDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }

    /**
     * Atualiza os dados de um produto existente. Requer autenticação de administrador.
     *
     * @param id      ID do produto.
     * @param request Objeto com novos dados e credenciais do admin.
     * @return Produto atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {

        validateAdmin(request.getUserId(), request.getPassword());
        ProdutoDTO produtoAtualizado = produtoService.atualizarProduto(id, request.getProduto());
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * Remove um produto pelo ID. Requer autenticação de administrador.
     *
     * @param id      ID do produto a ser removido.
     * @param request Credenciais do admin.
     * @return Resposta sem conteúdo em caso de sucesso.
     */
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

    /**
     * Atualiza a quantidade em estoque de um produto. Requer autenticação de administrador.
     *
     * @param id        ID do produto.
     * @param quantidade Nova quantidade de estoque.
     * @param admin     Credenciais do admin.
     * @return Produto com estoque atualizado.
     */
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
