# Sistema de Controle de Delivery

Este é um projeto de sistema simplificado para o controle de delivery de um restaurante, desenvolvido em Java com o framework Spring Boot.

- Acesse a documentação da API na Nuvem: [https://delivery-production-1264.up.railway.app/swagger-ui/index.html#/](https://delivery-production-1264.up.railway.app/swagger-ui/index.html#/).

## Requisitos

Antes de começar, certifique-se de ter os seguintes requisitos instalados em sua máquina:

- **Java 8 ou superior:** [Download](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven:** [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL:** [Download](https://www.postgresql.org/download/)
- IDE de sua escolha (recomendado: IntelliJ IDEA, Eclipse)

## Passo a Passo de Instalação

1. **Clone o Repositório:**
   ```bash
   git clone https://github.com/Mateus-msj3/delivery.git
    ```

## Configure o Banco de Dados:

1. **Crie um banco de dados PostgreSQL:**
    - Crie um banco de dados PostgreSQL com o nome desejado (por exemplo, `db_delivery`).

2. **Ajuste as Configurações do Banco de Dados:**
    - Abra o arquivo `src/main/resources/application.properties`.
    - Ajuste as configurações do banco de dados, como URL, nome do usuário e senha, de acordo com a sua instalação.

   
## Executar a Aplicação:

- Execute a classe principal `DeliveryApplication` para iniciar a aplicação.
- Este projeto contém testes unitários para a camada Service.

**Importante:**

Um arquivo com a collection está disponível na raiz do projeto com o nome: `Delivery.postman_collection.json`

Caso haja dúvida na montagem das requests, apenas importe este arquivo no Postaman.

Crie um usuário, faça login, obtenha o token JWT e faça as requests necessárias.

## Acessar a API:

- A API estará disponível em [http://localhost:8080](http://localhost:8080).

## Documentação da API (Swagger):

- Acesse a documentação da API em [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

**Importante**

Alternar os endereços no swagger entre [http://localhost:8080](http://localhost:8080) e [https://delivery-production-1264.up.railway.app/swagger-ui/index.html#/](https://delivery-production-1264.up.railway.app/swagger-ui/index.html#/)

## Autenticação JWT:

- A autenticação na API é feita usando JSON Web Token (JWT). Certifique-se de incluir o token JWT nas solicitações autenticadas.

## Dependências Principais

- Spring Boot: Framework para criação de aplicativos Java baseados em microserviços.
- Spring Data JPA: Facilita o acesso e a manipulação de dados em bancos de dados relacionais.
- Spring Web: Fornece suporte para criar aplicativos da web.
- Spring Security: Oferece recursos de segurança para proteger a aplicação.
- Spring Validation: Biblioteca para validação de dados.
- PostgreSQL Driver: Driver JDBC para PostgreSQL.
- Lombok: Biblioteca para simplificar a criação de classes Java.
- ModelMapper: Facilita a conversão entre objetos Java.
- JWT (JSON Web Token): Implementação para trabalhar com tokens JWT.
- Springdoc OpenAPI UI: Gera documentação da API usando o OpenAPI e fornece uma interface gráfica.

Para informações detalhadas sobre outras dependências, consulte o arquivo `pom.xml`.