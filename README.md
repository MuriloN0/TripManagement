# API Rest Gerenciador de Viagem

- Nesta API foi realizada a construção na linguagem JAVA de um algoritimo para o gerenciamento de viagens e funcionando como um itinerario possibilitanto com que o usuário possa marcar os compromissos que irá fazer como um tour durante a mesma em certo dia, hora e local.
- Na construção usamos o Spring Initializer para começar a preparação do algoritimo, também utilizamos o banco de dados H2 para podermos guardar as informações na memória, assim como também a JPA para delclararmos a assinatura e o LOMBOK.
- Para esta API ultilizamos o padrão MVC, então nós criamos as classes como a controller para que possamos deixar o tratamento de dados e não permitir a classe e a interface ter o acesso direto uma na outra. também fizemos tratamentos como a classe Repository para fazer a ligação com o banco de dados.
- Foi dividido a API em quatro packages sendo a TRIP, Activies, Links e Participant. Assim deixamos cada um com seus respectivos dados e seus tratamento onde no final damos um acesso interligando elas através  do TripController.
- Uma das ferramentas que usamos foi o insominia para testarmos o nosso algoritimo e garantir o bom funcionamento dela para a hora da integração com as demais partes.
