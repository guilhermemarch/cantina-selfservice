INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 1, NOW(), 'atendente@aluno.santoangelo.uri.br', 'senha123', 'ADMIN', NOW(), 'atendente1'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 1);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 2, NOW(), 'joao@aluno.santoangelo.uri.br', 'senha123', 'USERS', NOW(), 'joao123'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 2);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 3, NOW(), 'maria@aluno.santoangelo.uri.br', 'senha123', 'USERS', NOW(), 'mariazinha'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 3);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 4, NOW(), 'lucas@uni.edu', 'senha123', 'USERS', NOW(), 'lucasdev'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 4);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 5, NOW(), 'ana@aluno.santoangelo.uri.br', 'senha123', 'ADMIN', NOW(), 'anaat'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 5);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 6, NOW(), 'admin@admin', 'admin', 'ADMIN', NOW(), 'admin'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 6);
INSERT INTO usuarios (id, created_at, email, password, role, updated_at, username)
SELECT 0, NOW(), 'desconhecido', 'desconhecido', 'USERS', NOW(), 'desconhecido'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE id = 0);

INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 1, 'SALGADO', NOW(), 'Coxinha de frango com catupiry', 10, 'Coxinha', 6.00, 100, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751908653/dpalgcudpsvtvv3dtngw.png'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 1);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 2, 'BEBIDA', NOW(), 'Refrigerante lata 350ml', 20, 'Coca-Cola', 5.00, 80, NOW(), '2025-09-15', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751910103/okaq1cvakawktimsi9ha.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 2);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 3, 'DOCE', NOW(), 'Barra de chocolate 100g', 5, 'Chocolate Ao Leite', 4.50, 50, NOW(), '2026-01-01', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909958/buzzgosq2rrzoakc5p6q.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 3);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 4, 'SALGADO', NOW(), 'Pão de queijo grande', 10, 'Pão de Queijo', 4.00, 60, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751908979/o8q0sjbdzebokphyjvgz.png'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 4);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 5, 'LANCHE', NOW(), 'X-Burger com queijo e salada', 8, 'X-Burger', 12.00, 40, NOW(), '2025-08-30', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909226/w9k3iq0yk0kmp3vni7ik.png'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 5);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 6, 'BEBIDA', NOW(), 'Suco natural de laranja 300ml', 15, 'Suco de Laranja', 6.50, 70, NOW(), '2025-09-10', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909368/brl4skidzezhwpyldhkr.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 6);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 7, 'DOCE', NOW(), 'Bolo de chocolate fatia', 10, 'Fatia de Bolo', 5.50, 30, NOW(), '2025-08-20', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751909886/ws3lo3inobwa38nyesxs.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 7);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 8, 'DOCE', NOW(), 'Kibe recheado com queijo', 15, 'Kibe', 11.34, 129, NOW(), '2025-12-25', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911631/laimvdg4m6xixefgtmga.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 8);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 9, 'LANCHE', NOW(), 'Refrigerante garrafa 600ml', 10, 'Refrigerante 600ml', 14.07, 40, NOW(), '2025-09-01', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911566/xxgwe3cbquv8eeoqxlbv.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 9);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 10, 'BEBIDA', NOW(), 'Pacote de bala de goma sortida 100g', 12, 'Bala de goma', 9.51, 124, NOW(), '2025-10-25', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911060/gwk44rgu1uekkctzvo86.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 10);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 11, 'LANCHE', NOW(), 'X-Tudo completo com bacon e ovo', 5, 'X-Tudo', 10.28, 81, NOW(), '2025-10-12', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751910949/uywvfuergat9n74foutu.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 11);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 12, 'LANCHE', NOW(), 'Empada recheada com frango e catupiry', 10, 'Empada de frango', 12.27, 81, NOW(), '2025-12-26', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911898/rxuzgohk24tnob1rkppx.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 12);
INSERT INTO produtos (id, categoria, created_at, descricao, estoque_minimo, nome, preco, quantidade_estoque, updated_at, validade, imagem_url)
SELECT 13, 'LANCHE', NOW(), 'Suco natural de uva 300ml', 6, 'Suco de Uva', 7.49, 104, NOW(), '2025-12-14', 'https://res.cloudinary.com/dfqu4q6zo/image/upload/v1751911696/fdj3sav7fzlmjn8ivq2u.jpg'
WHERE NOT EXISTS (SELECT 1 FROM produtos WHERE id = 13);

INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id)
SELECT 1, NOW(), '2025-07-01T09:15:00', 'ENTREGUE', NOW(), 15.00, 2
WHERE NOT EXISTS (SELECT 1 FROM pedidos WHERE id = 1);
INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id)
SELECT 2, NOW(), '2025-07-01T10:30:00', 'ENTREGUE', NOW(), 13.50, 3
WHERE NOT EXISTS (SELECT 1 FROM pedidos WHERE id = 2);
INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id)
SELECT 3, NOW(), '2025-07-01T12:00:00', 'ENTREGUE', NOW(), 23.50, 4
WHERE NOT EXISTS (SELECT 1 FROM pedidos WHERE id = 3);
INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id)
SELECT 4, NOW(), '2025-07-01T14:45:00', 'PENDENTE', NOW(), 11.00, 2
WHERE NOT EXISTS (SELECT 1 FROM pedidos WHERE id = 4);
INSERT INTO pedidos (id, created_at, data_pedido, status, updated_at, valor_total, usuario_id)
SELECT 5, NOW(), '2025-07-01T16:20:00', 'ENTREGUE', NOW(), 18.50, 3
WHERE NOT EXISTS (SELECT 1 FROM pedidos WHERE id = 5);

INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id)
SELECT 1, 'pix001cantina', NOW(), '2025-07-01T09:16:00', 'DINHEIRO', 'APROVADO', 0.00, NOW(), 15.00, 1
WHERE NOT EXISTS (SELECT 1 FROM pagamentos WHERE id = 1);
INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id)
SELECT 2, 'pix002cantina', NOW(), '2025-07-01T10:31:00', 'PIX', 'APROVADO', 0.00, NOW(), 13.50, 2
WHERE NOT EXISTS (SELECT 1 FROM pagamentos WHERE id = 2);
INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id)
SELECT 3, 'pix003cantina', NOW(), '2025-07-01T12:01:00', 'PIX', 'APROVADO', 0.00, NOW(), 23.50, 3
WHERE NOT EXISTS (SELECT 1 FROM pagamentos WHERE id = 3);
INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id)
SELECT 4, 'pix004cantina', NOW(), '2025-07-01T14:46:00', 'DINHEIRO', 'PENDENTE', 0.00, NOW(), 11.00, 4
WHERE NOT EXISTS (SELECT 1 FROM pagamentos WHERE id = 4);
INSERT INTO pagamentos (id, codigo_pix, created_at, data_pagamento, metodo, status, troco, updated_at, valor, pedido_id)
SELECT 5, 'pix005cantina', NOW(), '2025-07-01T16:21:00', 'DINHEIRO', 'APROVADO', 0.50, NOW(), 18.50, 5
WHERE NOT EXISTS (SELECT 1 FROM pagamentos WHERE id = 5);

INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 1, NOW(), 6.00, 1, 6.00, NOW(), 1, 1
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 1);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 2, NOW(), 5.00, 1, 5.00, NOW(), 1, 2
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 2);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 3, NOW(), 4.00, 1, 4.00, NOW(), 1, 4
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 3);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 4, NOW(), 4.50, 1, 4.50, NOW(), 2, 3
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 4);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 5, NOW(), 5.00, 1, 5.00, NOW(), 2, 2
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 5);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 6, NOW(), 4.00, 1, 4.00, NOW(), 2, 4
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 6);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 7, NOW(), 12.00, 1, 12.00, NOW(), 3, 5
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 7);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 8, NOW(), 6.50, 1, 6.50, NOW(), 3, 6
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 8);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 9, NOW(), 4.50, 1, 4.50, NOW(), 3, 3
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 9);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 10, NOW(), 5.50, 1, 5.50, NOW(), 4, 7
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 10);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 11, NOW(), 5.00, 1, 5.00, NOW(), 4, 2
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 11);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 12, NOW(), 12.00, 1, 12.00, NOW(), 5, 5
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 12);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 13, NOW(), 5.50, 1, 5.50, NOW(), 5, 7
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 13);
INSERT INTO itens_pedido (id, created_at, preco_unitario, quantidade, subtotal, updated_at, pedido_id, produto_id)
SELECT 14, NOW(), 1.00, 1, 1.00, NOW(), 5, 2
WHERE NOT EXISTS (SELECT 1 FROM itens_pedido WHERE id = 14);