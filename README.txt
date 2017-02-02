:: Scheduler App ::

Resposta ao Desafio para desenvolvedor Java.

Aplicação web que implementa:
- Interface REST para inclusão, consulta e exclusão de jobs
- Motor de agendamento baseado em Quartz para execução dos jobs

_________________________________________________________
:: Definição da API
curl -H "Content-Type: application/json" -X POST \
  -d '{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}' \
  http://localhost:8080/api/jobs

curl http://localhost:8080/api/jobs
# retorna list dos jobs, exemplo:
#   [{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}]

curl -X DELETE http://localhost:8080/api/jobs/job-name

-> A execução dos jobs consiste em imprimir no log do servidor a mensagem de cada job, dentro dos respectivos dias/horários agendados.

_________________________________________________________
:: Deploy e Teste
1) mvn install
2) Fazer deploy do arquivo schedule-app.war no Web Container de sua preferência.
3) Utilizar as chamadas da API e acompanhar a execução no log do servidor.

OBS: Os exemplos na Definição da API consideram a aplicação instalada na raiz do container (context root = "/").
Se o WAR for instalado sem nenhum procedimento especial as chamadas deverão ser feitas como no exemplo:
$ curl http://localhost:8080/schedule-app/api/jobs
(observar o contexto /schedule-app).

Para manter as chamadas exatamente como na Definição da API, instalar a aplicação com context root = /
(procedimento varia conforme o Web Container ou Application Server utilizado.
Ex: No tomcat renomear o pacote para ROOT.war, no Jetty renomear para root.war)
