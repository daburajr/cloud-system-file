# Cloud System File #
### Desenvolver um sistema backend que permita o upload, download e gerenciamento de arquivos na nuvem utilizando AWS S3. O sistema deve ser implementado em Java e a infraestrutura deve ser provisionada usando Terraform.

### 1. Aplicação atendendo os seguintes requisitos funcionais: 

- Usuario
* [x] Auth JWT

- Arquivo 
* [x] Upload de Arquivos: Permitir que usuários façam upload de arquivos para um bucket S3.
* [x] Download de Arquivos: Permitir que usuários façam download de arquivos armazenados no S3.
* [x] Listagem de Arquivos: Listar todos os arquivos disponíveis em um bucket S3.
* [x] Exclusão de Arquivos: Permitir que usuários excluam arquivos do bucket S3.


### 2. Aplicação atendendo os seguintes requisitos não funcionais: 

* [x] Clareza do código;
* [x] Estrutura de código organizada;
* [x] Conceitos do REST;
* [x] Tratamentos de Erros (Controller Advice);
* [X] Testes de Unidade na camada de servico;
* [ ] Adicionar a Camada de Pesistência com Mybatis
* [ ] Dockerzirar
* [ ] Testes de Integração
* [ ] Implementar logs e monitoramento usando AWS CloudWatch;
* [ ] Configurar um pipeline de CI/CD para deploy automático usando AWS CodePipeline ou GitHub Actions;
* [ ] Usar VPC 
* [ ] Criar um Usuário IAM p/ acessar recursos espeficios da AWS (Nao Utilizar o Root)
* [ ] Criar uma policy para de Acesso do Usuario zup p/ acesso do S3 e EC2
* [ ] Ajustar scripts terraform para contemplar as melhorias de segurança na AWS


### 3. Tecnologias 

- Java 21
- Spring Boot 3.4.1

### 4. Dependencias Externas no Sistema Operacional (SO)
1. [Instalar AWS Command Line Interface no SO](https://docs.aws.amazon.com/cli/v1/userguide/install-linux.html) 
2. [Configurar AWS Command Line Interface no SO](https://docs.aws.amazon.com/cli/v1/userguide/cli-configure-files.html)
3. [Instalar o Terraform](https://developer.hashicorp.com/terraform/install?product_intent=terraform)

### 5. Provisionando Recursos com o Terraform
1. Provisionando BucketS3
   1. Altere os valores do arquivo variables.tf em **/src/main/resources/terraform/buckets3/variables
   2. Execute o script localizado em **/src/main/resources/terraform/buckets3/main
      ```shell
        terraform init && terraform apply --auto-approve
      ```
2. Provisionando EC2
   1. Altere os valores do arquivo variables.tf em **/src/main/resources/terraform/ec2/variables
   2. Execute o script localizado em **/src/main/resources/terraform/ec2/main
     ```shell
       terraform init && terraform apply --auto-approve
     ```

### 6. Fazendo o Deploy do Jar
1. Gere o Artefato Jar 
   1. Execute o comando mvn clean package
   2. Salve o arquivo cloudsystemfile-1.0.0.jar gerado em target/
2. Copiando o Arquivo para a maquina provisionada
   1. Faça a copia com scp, Ex: scp /home/jock/Desktop/cloudsystemfile-1.0.0.jar ec2-user@ec2-3-83-248-88.compute-1.amazonaws.com:/home/ec2-user
   2. Acessando a maquina provisionada
      1. Faça uma conexão ssh, Ex: ssh ec2-user@ec2-54-158-121-126.compute-1.amazonaws.com
      2. Execute o Jar colocando suas credenciais: 
       ```shell
        java -Dspring.profiles.active=prod -jar cloudsystemfile-1.0.0.jar --AWS_ACCESS_KEY_ID=SUA_AWS_ACCESS_KEY_ID --AWS_SECRET_KEY=SUA_AWS_ACCESS_KEY_ID --AWS_REGION=us-east-1 --AWS_BUCKET_NAME=cloud-system-file
       ```
### 7. Importando a Collection Postman
1. Importe a Collection no Postman
2. Click na collection cloudsystenfile
3. Click na aba Variables
4. Altere o DNS de PROD

### 8. Validando a aplicação

1. Auth com um Usuario Default (admin)
   ```shell
   curl --location --request POST 'http://{{SEU_DNS}}:8081/api/v1/auth/login' \
      --header 'Content-Type: application/json' \
      --data-raw '{
      "email": "admin@admin",
      "password": "123456"
      }'
   ```
2. Listar os arquivos no Bucket S3
   ```shell
    curl --location --request GET 'http://{{SEU_DNS}}:8081/api/v1/files' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjbG91ZHN5c3RlbWZpbGUiLCJzdWIiOiJhZG1pbkBhZG1pbiIsInJvbGVzIjoiUk9MRV9BRE1JTixST0xFX1VTRVIiLCJleHAiOjE3MzY0OTcwMDB9.zR-qhUF52fMPSdqeFN8MGgdav3hDjgIIlkV35OwKrSc'
   ```
3. Fique livre para testar os demais endpoints

**Lembre-se que uma aplicação boa é uma aplicação bem testada**

**By Jock**







