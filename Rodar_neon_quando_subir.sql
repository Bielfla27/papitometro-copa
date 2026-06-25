ALTER TABLE tb_palpite
DROP CONSTRAINT uk4g43q08a5dxruexsgmbtqqiex;

ALTER TABLE tb_palpite
ADD CONSTRAINT uk_palpite_usuario_jogo_sala
UNIQUE (usuario_id, jogo_id, sala_id);

Se ainda não tiver sala_id na tabela:

ALTER TABLE tb_palpite
ADD COLUMN sala_id BIGINT;

ALTER TABLE tb_palpite
ADD CONSTRAINT fk_palpite_sala
FOREIGN KEY (sala_id) REFERENCES tb_sala(id);