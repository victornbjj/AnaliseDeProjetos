# Requisitos Funcionais

## Fluxo de Agendamento (cliente — sem login)

| RF01 | Sequência fixa de etapas |
|---|---|
| Descrição | (1) barbeiro → (2) serviço → (3) horário → (4) formulário de contato → (5) confirmação. Nenhuma tela de login/cadastro em nenhum momento; cliente pode voltar a uma etapa anterior sem perder as seleções já feitas. |

| RF02 | Escolha de barbeiro |
|---|---|
| Descrição | Lista os barbeiros ativos (foto, nome); opção "qualquer disponível" também disponível. |

| RF03 | Escolha de serviço |
|---|---|
| Descrição | Exibe apenas os serviços que o barbeiro escolhido executa (nome, duração, preço); se for "qualquer disponível", exibe todos os serviços ativos. |

| RF04 | Escolha de horário |
|---|---|
| Descrição | Calcula disponibilidade cruzando expediente do barbeiro + duração do serviço + agendamentos existentes, com buffer de 5 min entre atendimentos; se "qualquer disponível" foi escolhido, cruza a agenda de todos os barbeiros aptos àquele serviço e atribui automaticamente o profissional livre no horário escolhido. |

| RF05 | Formulário de contato (substitui o cadastro) |
|---|---|
| Descrição | Nome completo (obrigatório, 3–100 caracteres), telefone (obrigatório, formato (DD) 9XXXX-XXXX), e-mail (obrigatório, formato válido). Nenhuma senha ou conta é criada — os dados ficam vinculados apenas àquele agendamento específico. |

| RF06 | Tela de confirmação |
|---|---|
| Descrição | Revisão de barbeiro + serviço + horário + dados antes de gravar; gravação atômica (transação) para não permitir 2 clientes reservando o mesmo slot; limite de 3 agendamentos futuros pendentes por e-mail informado (evita spam de reservas). |

| RF07 | Impedir conflito |
|---|---|
| Descrição | Constraint única no banco (barbeiro_id + data + horário); se o horário for tomado entre a seleção e a confirmação, exibir erro claro e recarregar horários livres. |

| RF08 | Cancelamento sem login |
|---|---|
| Descrição | E-mail de confirmação contém link único; permite cancelar sem restrição até 2h antes do horário; depois disso, fica registrado como "cancelamento tardio". |

| RF09 | Remarcação sem login |
|---|---|
| Descrição | Mesmo link permite escolher novo horário, respeitando a mesma regra de 2h e as validações de RF04/RF07. |

| RF10 | Consulta de status |
|---|---|
| Descrição | O link enviado por e-mail funciona também como página de consulta (confirmado/cancelado/concluído); após o horário do atendimento passar, o link vira somente leitura (não cancela/remarca mais) e passa a exibir a opção de avaliação (RF19). |

## Serviços e Profissionais (admin)

| RF11 | Cadastro de serviços |
|---|---|
| Descrição | Nome (até 60 caracteres), duração em múltiplos de 15 min, preço em R$ (> 0), opção de inativar sem apagar histórico. |

| RF12 | Cadastro de barbeiros |
|---|---|
| Descrição | Nome, foto (opcional), serviços que executa, horário de trabalho por dia da semana com intervalo de almoço configurável, slot mínimo de 15 min. |

## Notificações

| RF13 | E-mail de confirmação |
|---|---|
| Descrição | Enviado em até 5 minutos, contendo barbeiro, serviço, data, horário, endereço e o link de cancelamento/remarcação/status. |

| RF14 | E-mail de lembrete automático |
|---|---|
| Descrição | Enviado 24h antes e 2h antes do horário. |

## Administração

| RF15 | Login administrativo |
|---|---|
| Descrição | Barbeiro/dono acessa o painel via usuário/senha próprios, totalmente separado do fluxo do cliente; bloqueio de 15 min após 5 tentativas incorretas; sessão expira após 30 min de inatividade. |

| RF16 | Painel de agenda |
|---|---|
| Descrição | Grade com granularidade de 15 min, filtrável por barbeiro e por dia/semana, mostrando nome/telefone do cliente (vindos do formulário), serviço e horário. |

| RF17 | Bloqueio de horários |
|---|---|
| Descrição | Pontual ou recorrente; os horários bloqueados somem automaticamente do fluxo de agendamento do cliente (RF04). |

| RF18 | Relatórios |
|---|---|
| Descrição | Faturamento por período, ranking dos 5 serviços mais vendidos, horários de pico, taxa de falta em %, exportável em CSV. |

## Opcionais

| RF19 | Avaliação |
|---|---|
| Descrição | 1–5 estrelas + comentário (até 500 caracteres), acessível pela página de status (RF10) só depois do atendimento concluído. |

| RF20 | Programa de fidelidade |
|---|---|
| Descrição | Regra configurável (ex: a cada 10 cortes, o 11º grátis), rastreado pelo e-mail informado — como não há login, o cliente só acumula pontos se usar sempre o mesmo e-mail. |

# Requisitos Não Funcionais

## Usabilidade

| RNF01 | Conclusão do agendamento |
|---|---|
| Descrição | Cliente conclui o agendamento completo em no máximo 5 passos (RF01), sem tutorial; em teste de usabilidade, 90% dos usuários completam sozinhos em até 3 minutos. |
| Tipo | Usabilidade |

## Responsividade

| RNF02 | Layout responsivo |
|---|---|
| Descrição | Layout funcional de 320px até 1920px de largura; breakpoints: mobile ≤767px, tablet 768–1024px, desktop ≥1025px. |
| Tipo | Responsividade |

## Desempenho

| RNF03 | Carregamento das páginas do fluxo |
|---|---|
| Descrição | Páginas do fluxo (barbeiros, serviços, calendário, formulário) carregam em até 2s em banda larga e 4s em 4G, no percentil 95; pontuação Lighthouse ≥ 90. |
| Tipo | Desempenho |

| RNF04 | Suporte a usuários simultâneos |
|---|---|
| Descrição | Suportar pelo menos 50 usuários simultâneos sem tempo de resposta acima de 3s. |
| Tipo | Desempenho |

## Segurança

| RNF05 | Armazenamento de senhas administrativas |
|---|---|
| Descrição | Senhas (apenas de contas administrativas — o cliente nunca cria senha) com hash bcrypt (fator ≥ 12) ou Argon2id. |
| Tipo | Segurança |

| RNF06 | Comunicação segura |
|---|---|
| Descrição | Tráfego 100% via HTTPS/TLS 1.2+; certificado válido; redirecionamento automático HTTP→HTTPS. |
| Tipo | Segurança |

| RNF07 | Segurança dos tokens de agendamento |
|---|---|
| Descrição | Token do link de cancelamento/remarcação/status: aleatório e não sequencial (ex: UUID v4, mínimo 32 caracteres), não previsível a partir do ID do agendamento, sem exposição de dados sensíveis na URL em texto claro. |
| Tipo | Segurança |

## Privacidade / LGPD

| RNF08 | Consentimento e tratamento de dados |
|---|---|
| Descrição | Checkbox de consentimento no próprio formulário de agendamento (RF05, já que não existe mais tela de cadastro separada); solicitação de exclusão/exportação de dados via canal definido (e-mail dedicado ou botão na página de status), respondida em até 15 dias (Art. 19, II da LGPD); dados de agendamentos concluídos há mais de 3 anos anonimizados automaticamente. |
| Tipo | Privacidade / LGPD |

## Confiabilidade

| RNF09 | Disponibilidade do sistema |
|---|---|
| Descrição | Uptime mínimo de 99% mensal, sem manutenções programadas em horário comercial. |
| Tipo | Confiabilidade |

| RNF10 | Backup e recuperação |
|---|---|
| Descrição | Backup diário, retido por 30 dias; RPO de 24h e RTO de 4h. |
| Tipo | Confiabilidade |

## Compatibilidade

| RNF11 | Compatibilidade com navegadores |
|---|---|
| Descrição | Funcionar nas últimas 2 versões estáveis de Chrome, Firefox, Safari e Edge. |
| Tipo | Compatibilidade |

## Manutenibilidade

| RNF12 | Arquitetura e cobertura de testes |
|---|---|
| Descrição | Arquitetura em camadas (ou MVC); cobertura mínima de 70% de testes automatizados; API documentada (Swagger/OpenAPI). |
| Tipo | Manutenibilidade |

| RNF13 | Extensibilidade de módulos |
|---|---|
| Descrição | Novos módulos (pagamento, fidelidade, cupons) adicionáveis sem alterar o núcleo de agendamento. |
| Tipo | Manutenibilidade |

# Prioridade

| Requisito | Prioridade |
|---|---:|
| RF01 | 1º |
| RF04 | 2º |
| RF06 | 3º |
| RF07 | 4º |
| RF05 | 5º |
| RF02 | 6º |
| RF03 | 7º |
| RF08 | 8º |
| RF09 | 9º |
| RF10 | 10º |
| RF11 | 11º |
| RF12 | 12º |
| RF15 | 13º |
| RF16 | 14º |
| RF17 | 15º |
| RF13 | 16º |
| RF14 | 17º |
| RF18 | 18º |
| RNF05 | 19º |
| RNF06 | 20º |
| RNF07 | 21º |
| RNF01 | 22º |
| RNF02 | 23º |
| RNF03 | 24º |
| RNF04 | 25º |
| RNF08 | 26º |
| RNF09 | 27º |
| RNF10 | 28º |
| RNF11 | 29º |
| RNF12 | 30º |
| RNF13 | 31º |
| RF19 | 32º |
| RF20 | 33º |
