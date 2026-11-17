INSERT INTO usuario (nome, email, telefone_contato, criado_em)
VALUES ('Cidadão Demo', 'demo@pedro.com', '11988887777', CURRENT_TIMESTAMP);

INSERT INTO dispositivo (usuario_id, marca, modelo, imei, operadora, numero_linha, status, criado_em)
VALUES (1, 'Samsung', 'Galaxy A55', '490154203237518', 'Vivo', '11999990000', 'ATIVO', CURRENT_TIMESTAMP);

INSERT INTO ocorrencia (tipo, latitude, longitude, data_hora, descricao) VALUES
('FURTO',     -23.5577, -46.6609, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '2' DAY  + INTERVAL '19' HOUR, 'Celular levado da mão na saída do metrô Consolação'),
('ROUBO',     -23.5590, -46.6595, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '5' DAY  + INTERVAL '20' HOUR, 'Ciclista arrancou o celular na Av. Paulista'),
('FURTO',     -23.5614, -46.6559, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '7' DAY  + INTERVAL '18' HOUR, 'Furto em ponto de ônibus próximo ao MASP'),
('ROUBO',     -23.5620, -46.6550, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '9' DAY  + INTERVAL '21' HOUR, 'Abordagem por motociclista'),
('TENTATIVA', -23.5605, -46.6570, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '12' DAY + INTERVAL '19' HOUR, 'Tentativa de furto em faixa de pedestre'),
('FURTO',     -23.5599, -46.6581, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '15' DAY + INTERVAL '18' HOUR, 'Furto dentro de ônibus'),
('ROUBO',     -23.5632, -46.6542, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '20' DAY + INTERVAL '22' HOUR, 'Roubo na saída de bar'),
('FURTO',     -23.5611, -46.6563, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '25' DAY + INTERVAL '19' HOUR, 'Furto enquanto usava GPS');

INSERT INTO ocorrencia (tipo, latitude, longitude, data_hora, descricao) VALUES
('FURTO',     -23.5503, -46.6340, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '1' DAY  + INTERVAL '8' HOUR,  'Furto na saída do metrô Sé'),
('ROUBO',     -23.5510, -46.6335, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '4' DAY  + INTERVAL '7' HOUR,  'Roubo a pedestre na praça'),
('FURTO',     -23.5498, -46.6350, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '10' DAY + INTERVAL '17' HOUR, 'Furto em meio à multidão'),
('TENTATIVA', -23.5507, -46.6328, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '30' DAY + INTERVAL '18' HOUR, 'Tentativa de furto em fila');

INSERT INTO ocorrencia (tipo, latitude, longitude, data_hora, descricao) VALUES
('FURTO', -23.5440, -46.6160, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '3' DAY  + INTERVAL '10' HOUR, 'Furto em comércio popular'),
('ROUBO', -23.5452, -46.6148, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '6' DAY  + INTERVAL '19' HOUR, 'Roubo na estação Brás'),
('FURTO', -23.5447, -46.6155, CAST(CURRENT_DATE AS TIMESTAMP) - INTERVAL '40' DAY + INTERVAL '11' HOUR, 'Furto de bolso na feira');
