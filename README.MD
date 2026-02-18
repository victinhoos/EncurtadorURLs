<div align="center">
    <img width="250" height="150" alt="Image" src="https://github.com/user-attachments/assets/3fe82311-adcb-4a16-a950-13870dc4413f" />
</div>

# 🌍  EncurtadorURL - Spring Boot, MongoDB e Redis

![Badge Concluído](https://img.shields.io/badge/STATUS-CONCLUÍDO-brightgreen?style=for-the-badge)

![Badge Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Badge MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Badge Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Badge Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Badge Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

![Badge License](http://img.shields.io/static/v1?label=LICENSE&message=MIT&color=BLUE&style=for-the-badge)

## 📝 Descrição do Projeto

Uma API RESTful desenvolvida em Java com Spring Boot que tem como objetivo encurtar URLs longas, tornando-as mais amigáveis e fáceis de compartilhar (semelhante a serviços como Bitly). O sistema recebe um link original, gera um código curto ofuscado e único, e realiza o redirecionamento automático quando o link curto é acessado.

## 🎟 Funcionalidades:

* **Geração Segura de Hashes:** Utiliza a biblioteca genérica Hashids (com Base 62) para transformar IDs numéricos em strings curtas, únicas e não sequenciais, evitando previsibilidade.

* **Validação de dados:** Para garantir a integridade do sistema e evitar o processamento de requisições malformadas, ele impedir que valores nulos, vazio até em branco seja processados.

* **Redirecionamento Automático (GET /{codigoCurto}):** Ao acessar o código gerado, a API busca a URL original no banco de dados e redireciona o usuário perfeitamente (HTTP Status 302 Found).

* **Encurtamento de URL (POST /encurtar):** Recebe uma URL longa via JSON e retorna um objeto contendo a nova URL encurtada.

* **Ranking em Tempo Real (Top 100):** Utiliza a estrutura *Sorted Sets* (ZSet) do banco de dados em memória **Redis** para computar cada clique nos links. A rota `GET /ranking` devolve as 100 URLs mais acessadas instantaneamente, garantindo altíssima performance sem onerar o banco de dados principal (MongoDB).

* **Métricas de Visitantes Únicos (Analytics):** Implementa o algoritmo avançado **HyperLogLog** no Redis para rastrear endereços de IP únicos que acessam os links curtos. Permite consultar o engajamento real de uma URL na rota `GET /{codigoCurto}/estatisticas` com um consumo de memória RAM minúsculo e tempo de execução constante $O(1)$, suportando milhões de acessos.

## 🛠️ Tecnologias e Arquitetura

* **Back-End: Java 21 e Spring Boot 3:** Camada de aplicação baseada em arquitetura RESTful. Responsável pela exposição dos endpoints HTTP, tratamento e validação de requisições (Bean Validation), aplicação das regras de negócio e geração de identificadores ofuscados utilizando Hashids.

* **Banco de Dados Principal: MongoDB:** Banco de dados orientado a documentos utilizado como storage primário. Armazena de forma persistente e indexada a entidade da URL, contendo: URL original, código encurtado, metadados e timestamp de criação. Otimizado para operações de leitura e escrita com baixa latência, garantindo escalabilidade horizontal.

* **Banco de Dados em Memória: Redis:** Camada de Cache e Analytics. Utilizado para processamento de métricas complexas em tempo real, absorvendo o impacto de operações pesadas (como contagem de cliques e visitantes) e reduzindo drasticamente a carga de leitura/escrita no MongoDB.

* **Infraestrutura: Docker Compose:** Ambiente conteinerizado para padronização e isolamento dos serviços (API, MongoDB e Redis).
## 💬 Observação sobre o repositório

Ah, bem observado! Eu tinha colocado um link genérico no passo a passo anterior, mas o correto é exatamente esse do seu repositório:

https://github.com/victinhoos/EncurtadorURLs.git

Sobre o “faltou o quê?”: para o seu README ficar **100% à prova de recrutadores e desenvolvedores Sêniores**, faltou apenas mostrar **como usar a API**.

Quem entra no seu GitHub precisa saber quais são as rotas e como mandar os dados sem ter que abrir o código Java para ler. Nós chamamos isso de **Documentação dos Endpoints**.

---


```bash
git clone https://github.com/victinhoos/EncurtadorURLs.git
```

---

## 📡 Documentação da API (Endpoints)

### 1. Encurtar uma URL

- **Rota:** `POST /encurtar`
- **Descrição:** Recebe uma URL longa e devolve a entidade com o código curto gerado.

**Corpo da Requisição (JSON):**

```json
{
  "url": "https://www.google.com"
}
```

**Resposta de Sucesso (201 Created):**

```json
{
  "idURL": "65b2a1f...",
  "url": "https://www.google.com",
  "encurtador": "x7K9p",
  "data": "2026-02-18"
}
```

---

### 2. Acessar URL Encurtada (Redirecionamento)

- **Rota:** `GET /{codigoCurto}`
- **Descrição:** Busca a URL original no banco, registra as métricas no Redis e redireciona o usuário.
- **Exemplo:** `GET /x7K9p`
- **Resposta:** HTTP Status `302 (Found)` redirecionando para o site original.

---

### 3. Ver Ranking Top 100 URLs Mais Acessadas

- **Rota:** `GET /ranking`
- **Descrição:** Retorna a lista das URLs curtas mais acessadas em tempo real, ordenada por quantidade de cliques.

**Resposta de Sucesso (200 OK):**

```json
[
  {
    "value": "x7K9p",
    "score": 150.0
  },
  {
    "value": "aB3dE",
    "score": 85.0
  }
]
```


---

### 4. Ver Estatísticas de Visitantes Únicos

- **Rota:** `GET /{codigoCurto}/estatisticas`
- **Descrição:** Retorna a quantidade aproximada de endereços IP únicos que acessaram o link, utilizando o HyperLogLog.

**Resposta de Sucesso (200 OK):**

```json
{
  "encurtador": "x7K9p",
  "visitantes_unicos": 42
}
```
