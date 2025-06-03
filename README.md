# 💈 App Cliente Cabeleireiro

Aplicativo Android desenvolvido em Kotlin com Room DB local, permitindo que o cliente agende serviços como corte de cabelo, barba ou ambos. Ele também possui um painel administrativo para o proprietário do salão gerenciar preços e horários disponíveis.

---

## 📱 Funcionalidades

### Funcionalidades do Cliente:

* 📅 **Agendamento de Serviços**: Permite ao cliente agendar serviços como corte de cabelo, barba ou ambos, com verificação de horário disponível. O sistema não permite agendamentos às segundas-feiras, e todas as datas devem ser futuras.
* 🧑‍💼 **Cadastro e Login de Usuário**: Os usuários podem se registrar com e-mail, nome de usuário e nome completo. O login é protegido por senha criptografada (SHA-256).
* ✏️ **Edição de Perfil**: Usuários podem editar seu nome completo e alterar a senha. O nome de usuário e e-mail são somente para leitura.
* 📋 **Consulta de Agendamentos**: Clientes podem visualizar seus agendamentos, com opções para editar o serviço ou cancelar o agendamento.
* 💵 **Visualização de Preços**: Uma tela simples exibe os preços dos serviços.
* 🔐 **Proteção de Acesso com Sessão Persistente**: A sessão do usuário é mantida para facilitar o acesso.

### Funcionalidades do Administrador:

* 🗓️ **Ver Agendamentos**: Visualiza todos os agendamentos realizados pelos clientes.
* ⚙️ **Gerenciar Datas e Horários**: Permite ao administrador bloquear horários específicos para agendamento.
* 💰 **Alterar Preços**: Possibilita a atualização dos preços dos serviços oferecidos. Há também a opção de restaurar os preços padrão.

---

## 🧑‍💻 Tecnologias Utilizadas

* [**Kotlin**](https://kotlinlang.org/): Linguagem de programação primária para o desenvolvimento Android.
* [**Android Studio**](https://developer.android.com/studio): Ambiente de Desenvolvimento Integrado (IDE) oficial para Android.
* [**Room Database (SQLite)**](https://developer.android.com/training/data-storage/room): Biblioteca de persistência para armazenamento local de dados no aplicativo.
* [**Material Design 3**](https://m3.material.io/): Conjunto de diretrizes e componentes para criar interfaces de usuário modernas e responsivas.
* [**Coroutines + LifecycleScope**](https://developer.android.com/kotlin/coroutines): Utilizadas para gerenciar operações assíncronas e de banco de dados de forma eficiente, vinculadas ao ciclo de vida dos componentes Android.

---

## 📂 Estrutura de Telas (Activities)

O aplicativo é composto pelas seguintes telas principais:

* `MainActivity`: Tela inicial com opções para navegar para as telas de Login ou Registro.
* `RegisterActivity`: Permite que novos usuários criem uma conta com validação de e-mail e senha.
* `LoginActivity`: Autentica usuários (clientes e administrador) e gerencia a sessão.
* `HomeClientActivity`: A tela principal para usuários clientes, oferecendo navegação para agendamento, perfil, preços e a opção de sair.
* `AgendarServicoActivity`: Onde os clientes selecionam o serviço, data e hora para agendar um compromisso.
* `ListaAgendamentosActivity`: Exibe os agendamentos do usuário atual em uma `RecyclerView`, com opções para editar ou cancelar.
* `ProfileActivity`: Mostra os detalhes do perfil do cliente (usuário, e-mail, nome completo).
* `EditProfileActivity`: Permite que os clientes editem seu nome completo e senha.
* `PrecosActivity`: Exibe a lista de preços dos serviços.
* `AdminHomeActivity`: O painel principal para o administrador, com botões para as funcionalidades administrativas.
* `VerTodosAgendamentosActivity`: Permite ao administrador visualizar todos os agendamentos de todos os usuários.
* `AlterarPrecosActivity`: Permite ao administrador modificar os preços dos serviços e restaurar os valores padrão.
* `GerenciarDatasHorariosActivity`: Permite ao administrador bloquear ou liberar horários específicos em determinadas datas.

---

## 🗃️ Banco de Dados (Room Database)

O aplicativo utiliza o Room Database para persistência local de dados. O banco de dados é inicializado com preços padrão para os serviços.

### Tabelas:

* `users`: Armazena informações dos usuários, incluindo `username`, `email`, `fullName` e `passwordHash` (senha criptografada com SHA-256).
* `agendamento`: Armazena os detalhes dos agendamentos, incluindo `id`, `username` (associando o agendamento ao usuário), `servico` e `dataHora` (timestamp do agendamento).
* `precos`: Armazena os preços dos serviços, com `servico` (nome do serviço) como chave primária e `preco`.

---

## 📸 Capturas de Tela

### Telas Iniciais:
* **Tela Inicial**
![Tela Inicial](caminho/para/Tela_Inicial.jpg)
* **Tela de Registro**
![Tela de Registro](caminho/para/Tela_de_Registro.jpg)
* **Tela de Login**
![Tela de Login](caminho/para/Tela_de_Login.jpg)

### Painel Cliente:
* **Tela Principal Cliente**
![Tela Principal Cliente](caminho/para/Tela_Principal_Cliente.jpg)
* **Tela Agendar Serviço**
![Tela Agendar Serviço](caminho/para/Tela_Agendar_Servico.jpg)
* **Tela Meus Agendamentos**
![Tela Meus Agendamentos](caminho/para/Tela_Meus_Agendamentos.jpg)
* **Tela Lista de Preços Cliente**
![Tela Lista de Preços Cliente](caminho/para/Tela_Lista_de_Precos_Cliente.jpg)
* **Tela Meu Perfil Cliente**
![Tela Meu Perfil Cliente](caminho/para/Tela_Meu_Perfil_Cliente.jpg)
* **Tela Editar Meu Perfil**
![Tela Editar Meu Perfil](caminho/para/Tela_Editar_Meu_Perfil.jpg)

### Painel Administrativo:
* **Tela Painel do Administrador**
![Tela Painel do Administrador](caminho/para/Tela_Painel_do_Administrador.jpg)
* **Tela Todos os Agendamentos (Admin)**
![Tela Todos os Agendamentos (Admin)](caminho/para/Tela_Todos_os_Agendamentos_(Admin).jpg)
* **Tela Gerenciar datas e horas (Admin)**
![Tela Gerenciar datas e horas (Admin)](caminho/para/Tela_Gerenciar_datas_e_horas_(Admin).jpg)
* **Tela Alterar Preços (Admin)**
![Tela Alterar Preços (Admin)](caminho/para/Tela_Alterar_Precos_(Admin).jpg)

---

## ⚙️ Como Rodar o Projeto

Para configurar e executar o projeto em seu ambiente local:

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
    ```
2.  **Abra o projeto no Android Studio:**
    * No Android Studio, selecione `File` > `Open` e navegue até a pasta raiz do projeto clonado.
3.  **Sincronize o Gradle:**
    * O Android Studio deve automaticamente sincronizar o projeto com o Gradle. Se não, clique em `Sync Project with Gradle Files` na barra de ferramentas.
4.  **Execute o aplicativo:**
    * Conecte um dispositivo Android físico ou inicie um emulador.
    * Clique no botão `Run 'app'` (o ícone de reprodução verde) na barra de ferramentas do Android Studio.

---

## 🔐 Observações de Segurança e Regras de Negócio

* **Criptografia de Senha**: As senhas dos usuários são armazenadas utilizando hash SHA-256 para maior segurança.
* **Dias de Funcionamento**: O sistema impede agendamentos para segundas-feiras, pois o cabeleireiro é considerado fechado neste dia.
* **Agendamentos Futuros**: Apenas agendamentos para datas futuras são permitidos.
* **Gerenciamento de Horários pelo Admin**: O administrador pode marcar horários como "BLOQUEIO" para indicar indisponibilidade, e esses agendamentos "bloqueados" não são exibidos na lista de agendamentos do cliente.
* **Preços Padrão**: Os preços dos serviços são inicializados com valores padrão (Cabelo: R$ 30.0, Barba: R$ 30.0, Cabelo e Barba: R$ 45.0) quando o banco de dados é criado.

---
