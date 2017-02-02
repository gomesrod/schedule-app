:: Scheduler App ::

Resposta ao Desafio para desenvolvedor Java.

Aplica��o web que implementa:
- Interface REST para inclus�o, consulta e exclus�o de jobs
- Motor de agendamento baseado em Quartz para execu��o dos jobs

_________________________________________________________
:: Defini��o da API
curl -H "Content-Type: application/json" -X POST \
  -d '{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}' \
  http://localhost:8080/api/jobs

curl http://localhost:8080/api/jobs
# retorna list dos jobs, exemplo:
#   [{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}]

curl -X DELETE http://localhost:8080/api/jobs/job-name

-> A execu��o dos jobs consiste em imprimir no log do servidor a mensagem de cada job, dentro dos respectivos dias/hor�rios agendados.

_________________________________________________________
:: Deploy e Teste
1) mvn install
2) Fazer deploy do arquivo schedule-app.war no Web Container de sua prefer�ncia.
3) Utilizar as chamadas da API e acompanhar a execu��o no log do servidor.

OBS: Os exemplos na Defini��o da API consideram a aplica��o instalada na raiz do container (context root = "/").
Se o WAR for instalado sem nenhum procedimento especial as chamadas dever�o ser feitas como no exemplo:
$ curl http://localhost:8080/schedule-app/api/jobs
(observar o contexto /schedule-app).

Para manter as chamadas exatamente como na Defini��o da API, instalar a aplica��o com context root = /
(procedimento varia conforme o Web Container ou Application Server utilizado.
Ex: No tomcat renomear o pacote para ROOT.war, no Jetty renomear para root.war)
