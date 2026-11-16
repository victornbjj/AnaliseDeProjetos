# Barber Pro — primeiro incremento

Aplicação web MVC Spring Boot para iniciar o painel administrativo de uma barbearia. Esta etapa implementa as telas administrativas e o cadastro persistente de profissionais, serviços associados, expediente semanal e intervalo de almoço. O fluxo de agendamento do cliente continua sem conta ou login.

## Requisitos

- Java 21 ou superior
- O Maven Wrapper incluído (baixa o Maven 3.9.9 na primeira execução)

## Executar localmente

```powershell
.\mvnw.cmd spring-boot:run
```

Acesse `http://localhost:8080/admin/login` para a tela de acesso administrativo ou `http://localhost:8080/admin/barbeiros/novo` para cadastrar um profissional. A listagem fica em `http://localhost:8080/admin/barbeiros`.

O login administrativo é apenas a tela visual nesta etapa. A autenticação ainda não foi implementada e o formulário fica desabilitado; não há credenciais padrão nem sucesso simulado. As rotas administrativas ainda não devem ser publicadas em ambiente acessível pela internet.


## Testes

```powershell
.\mvnw.cmd test
```

Os testes usam um banco H2 em memória.

## Escopo desta etapa

Implementados: página de login indicativa sem autenticação falsa; formulário e listagem de barbeiros; validação de nome, serviços, dias/horários e almoço; upload opcional de foto; persistência via JPA/H2; layouts responsivos em português. Ainda não implementados: autenticação administrativa, gerenciamento de serviços, agendamento de clientes, agenda, notificações e relatórios.
