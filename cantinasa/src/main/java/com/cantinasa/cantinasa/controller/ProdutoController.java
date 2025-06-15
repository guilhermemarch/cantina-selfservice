package com.cantinasa.cantinasa.controller;


import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/cantina/produtos")
public class ProdutoController {



    //    precisa ser admin aq
    @PostMapping("/produtos")
    public  ResponseEntity<ProdutoDTO> cadastrarProduto(RequestBody ProdutoDTO) {

        // Lógica para cadastrar um novo produto

        return ResponseEntity.ok(new ProdutoDTO());
    }

    @GetMapping("/produtos")
    public ResponseEntity<?> listarProdutos() {

        // Lógica para listar todos os produtos

        return ResponseEntity.ok("Lista de produtos");
    }


    @GetMapping("/produtos/{id}")
    public ResponseEntity<?> listarProdutobyId() {

        // Lógica para listar todos os produtos

        return ResponseEntity.ok("Lista de produtos");
    }



//    GET	/produtos	Lista todos os produtos disponíveis
//    GET	/produtos/{id}	Detalhes de um produto
//    POST	/produtos	Cadastra novo produto (admin)
//    PUT	/produtos/{id}	Atualiza produto existente
//    DELETE	/produtos/{id}	Remove produto
//    PATCH	/produtos/{id}/estoque	Atualiza quantidade no estoque (abastecimento ou venda)
//

}
