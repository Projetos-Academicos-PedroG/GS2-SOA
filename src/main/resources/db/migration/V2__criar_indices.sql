CREATE INDEX idx_ocorrencia_lat_lng   ON ocorrencia (latitude, longitude);
CREATE INDEX idx_ocorrencia_data_hora ON ocorrencia (data_hora);
CREATE INDEX idx_alerta_usuario_data  ON alerta (usuario_id, criado_em);
CREATE INDEX idx_dispositivo_usuario  ON dispositivo (usuario_id);
CREATE INDEX idx_incidente_status     ON incidente (dispositivo_id, status);
