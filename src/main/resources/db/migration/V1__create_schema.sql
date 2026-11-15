-- RF07: um barbeiro não pode ter dois agendamentos ativos no mesmo horário.
-- Índice parcial (não uma constraint única comum) para que um agendamento
-- cancelado libere o horário para uma nova reserva.
CREATE UNIQUE INDEX IF NOT EXISTS uk_agendamento_barbeiro_data_hora_ativo
    ON agendamento (barbeiro_id, data_hora)
    WHERE status NOT IN ('CANCELADO', 'CANCELADO_TARDIO');
