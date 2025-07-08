-- USUARIOS
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username) VALUES
(1, NOW(), 'atendente@aluno.santoangelo.uri.br', 'senha123', 'ADMIN', NOW(), 'atendente1'),
(2, NOW(), 'joao@aluno.santoangelo.uri.br', 'senha123', 'USERS', NOW(), 'joao123'),
(3, NOW(), 'maria@aluno.santoangelo.uri.br', 'senha123', 'USERS', NOW(), 'mariazinha'),
(4, NOW(), 'lucas@uni.edu', 'senha123', 'USERS', NOW(), 'lucasdev'),
(5, NOW(), 'ana@aluno.santoangelo.uri.br', 'senha123', 'ADMIN', NOW(), 'anaat');
(6, NOW(), 'admin@admin', 'admin', 'ADMIN', NOW(), 'admin');
(0, NOW(), 'desconhecido', 'desconhecido', 'USERS', NOW(), 'desconhecido');



INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade) VALUES
(1, 'SALGADO', NOW(), 'Coxinha de frango com catupiry', 10, 'Coxinha', 6.00, 100, NOW(), '2025-08-30'),
(2, 'BEBIDA', NOW(), 'Refrigerante lata 350ml', 20, 'Coca-Cola', 5.00, 80, NOW(), '2025-09-15'),
(3, 'DOCE', NOW(), 'Barra de chocolate 100g', 5, 'Chocolate Ao Leite', 4.50, 50, NOW(), '2026-01-01'),
(4, 'SALGADO', NOW(), 'Pão de queijo grande', 10, 'Pão de Queijo', 4.00, 60, NOW(), '2025-08-30'),
(5, 'LANCHE', NOW(), 'X-Burger com queijo e salada', 8, 'X-Burger', 12.00, 40, NOW(), '2025-08-30'),
(6, 'BEBIDA', NOW(), 'Suco natural de laranja 300ml', 15, 'Suco de Laranja', 6.50, 70, NOW(), '2025-09-10'),
(7, 'DOCE', NOW(), 'Bolo de chocolate fatia', 10, 'Fatia de Bolo', 5.50, 30, NOW(), '2025-08-20');
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade) VALUES
(8, 'DOCE', NOW(), 'Kibe recheado com queijo', 15, 'Kibe', 11.34, 129, NOW(), '2025-12-25'),
(9, 'LANCHE', NOW(), 'Refrigerante garrafa 600ml', 10, 'Refrigerante 600ml', 14.07, 40, NOW(), '2025-09-01'),
(10, 'BEBIDA', NOW(), 'Pacote de bala de goma sortida 100g', 12, 'Bala de goma', 9.51, 124, NOW(), '2025-10-25'),
(11, 'LANCHE', NOW(), 'X-Tudo completo com bacon e ovo', 5, 'X-Tudo', 10.28, 81, NOW(), '2025-10-12'),
(12, 'LANCHE', NOW(), 'Empada recheada com frango e catupiry', 10, 'Empada de frango', 12.27, 81, NOW(), '2025-12-26'),
(13, 'LANCHE', NOW(), 'Suco natural de uva 300ml', 6, 'Suco de Uva', 7.49, 104, NOW(), '2025-12-14'),


INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url) VALUES
    (1, 'SALGADO', NOW(), 'Coxinha de frango com catupiry', 10, 'Coxinha', 6.00, 100, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751908653/dpalgcudpsvtvv3dtngw.png'),
    (2, 'BEBIDA', NOW(), 'Refrigerante lata 350ml', 20, 'Coca-Cola', 5.00, 80, NOW(), '2025-09-15', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751910103/okaq1cvakawktimsi9ha.jpg'),
    (3, 'DOCE', NOW(), 'Barra de chocolate 100g', 5, 'Chocolate Ao Leite', 4.50, 50, NOW(), '2026-01-01', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909958/buzzgosq2rrzoakc5p6q.jpg'),
    (4, 'SALGADO', NOW(), 'Pão de queijo grande', 10, 'Pão de Queijo', 4.00, 60, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751908979/o8q0sjbdzebokphyjvgz.png'),
    (5, 'LANCHE', NOW(), 'X-Burger com queijo e salada', 8, 'X-Burger', 12.00, 40, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909226/w9k3iq0yk0kmp3vni7ik.png'),
    (6, 'BEBIDA', NOW(), 'Suco natural de laranja 300ml', 15, 'Suco de Laranja', 6.50, 70, NOW(), '2025-09-10', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909368/brl4skidzezhwpyldhkr.jpg'),
    (7, 'DOCE', NOW(), 'Bolo de chocolate fatia', 10, 'Fatia de Bolo', 5.50, 30, NOW(), '2025-08-20', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909886/ws3lo3inobwa38nyesxs.jpg'),
    (8, 'DOCE', NOW(), 'Kibe recheado com queijo', 15, 'Kibe', 11.34, 129, NOW(), '2025-12-25', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911631/laimvdg4m6xixefgtmga.jpg'),
    (9, 'LANCHE', NOW(), 'Refrigerante garrafa 600ml', 10, 'Refrigerante 600ml', 14.07, 40, NOW(), '2025-09-01', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911566/xxgwe3cbquv8eeoqxlbv.jpg'),
    (10, 'BEBIDA', NOW(), 'Pacote de bala de goma sortida 100g', 12, 'Bala de goma', 9.51, 124, NOW(), '2025-10-25', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911060/gwk44rgu1uekkctzvo86.jpg'),
    (11, 'LANCHE', NOW(), 'X-Tudo completo com bacon e ovo', 5, 'X-Tudo', 10.28, 81, NOW(), '2025-10-12', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751910949/uywvfuergat9n74foutu.jpg'),
    (12, 'LANCHE', NOW(), 'Empada recheada com frango e catupiry', 10, 'Empada de frango', 12.27, 81, NOW(), '2025-12-26', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911898/rxuzgohk24tnob1rkppx.jpg'),
    (13, 'LANCHE', NOW(), 'Suco natural de uva 300ml', 6, 'Suco de Uva', 7.49, 104, NOW(), '2025-12-14', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911696/fdj3sav7fzlmjn8ivq2u.jpg');



INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id) VALUES
(1, NOW(), '2025-07-01T09:15:00', 'ENTREGUE', NOW(), 15.00, 2),
(2, NOW(), '2025-07-01T10:30:00', 'ENTREGUE', NOW(), 13.50, 3),
(3, NOW(), '2025-07-01T12:00:00', 'ENTREGUE', NOW(), 23.50, 4),
(4, NOW(), '2025-07-01T14:45:00', 'PENDENTE', NOW(), 11.00, 2),
(5, NOW(), '2025-07-01T16:20:00', 'ENTREGUE', NOW(), 18.50, 3);


INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id) VALUES
(1, 'pix001cantina', NOW(), '2025-07-01T09:16:00', 'DINHEIRO', 'APROVADO', 0.00, NOW(), 15.00, 1),
(2, 'pix002cantina', NOW(), '2025-07-01T10:31:00', 'PIX', 'APROVADO', 0.00, NOW(), 13.50, 2),
(3, 'pix003cantina', NOW(), '2025-07-01T12:01:00', 'PIX', 'APROVADO', 0.00, NOW(), 23.50, 3),
(4, 'pix004cantina', NOW(), '2025-07-01T14:46:00', 'DINHEIRO', 'PENDENTE', 0.00, NOW(), 11.00, 4),
(5, 'pix005cantina', NOW(), '2025-07-01T16:21:00', 'DINHEIRO', 'APROVADO', 0.50, NOW(), 18.50, 5);

INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id) VALUES
(1, NOW(), 6.00, 1, 6.00, NOW(), 1, 1),
(2, NOW(), 5.00, 1, 5.00, NOW(), 1, 2),
(3, NOW(), 4.00, 1, 4.00, NOW(), 1, 4),

(4, NOW(), 4.50, 1, 4.50, NOW(), 2, 3),
(5, NOW(), 5.00, 1, 5.00, NOW(), 2, 2),
(6, NOW(), 4.00, 1, 4.00, NOW(), 2, 4),

(7, NOW(), 12.00, 1, 12.00, NOW(), 3, 5),
(8, NOW(), 6.50, 1, 6.50, NOW(), 3, 6),
(9, NOW(), 4.50, 1, 4.50, NOW(), 3, 3),

(10, NOW(), 5.50, 1, 5.50, NOW(), 4, 7),
(11, NOW(), 5.00, 1, 5.00, NOW(), 4, 2),
(12, NOW(), 12.00, 1, 12.00, NOW(), 5, 5),
(13, NOW(), 5.50, 1, 5.50, NOW(), 5, 7),
(14, NOW(), 1.00, 1, 1.00, NOW(), 5, 2);






----
SELECT 
        DATE_TRUNC('hour', created_at) AS hora,
        SUM(subtotal) AS total_vendido
    FROM 
        itens_pedido
    GROUP BY 
        hora
    ORDER BY 
        total_vendido DESC
    LIMIT 1;



INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade) VALUES
(8, 'DOCE', NOW(), 'Bolo de chocolate fatia', 10, 'Fatia de Bolo', 5.50, 30, NOW(), '2025-07-05');
