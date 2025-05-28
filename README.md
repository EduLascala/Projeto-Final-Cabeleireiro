# 💈 App Cliente Cabeleireiro

Aplicativo Android desenvolvido em Kotlin com Room DB local, permitindo que o cliente agende serviços como corte de cabelo, barba ou ambos.

---

## 📱 Funcionalidades

* 📅 **Agendamento de Serviços** com verificação de horário disponível
* 🧑‍💼 **Cadastro e Login de Usuário** com senha criptografada
* ✏️ **Edição de Perfil** (nome e senha)
* 📋 **Consulta de Agendamentos** com opção de editar ou cancelar
* 💵 **Visualização de Preços** (tela de design simples)
* 🔐 **Proteção de acesso com sessão persistente**

---

## 🧑‍💻 Tecnologias Utilizadas

* [Kotlin](https://kotlinlang.org/)
* [Android Studio](https://developer.android.com/studio)
* [Room Database (SQLite)](https://developer.android.com/training/data-storage/room)
* [Material Design 3](https://m3.material.io/)
* [Coroutines + LifecycleScope](https://developer.android.com/kotlin/coroutines)

---

## 📂 Estrutura de Telas

* `MainActivity`: Tela inicial com opções de login e registro
* `LoginActivity`: Autenticação do usuário
* `RegisterActivity`: Cadastro com validação de e-mail e senha
* `HomeClientActivity`: Tela principal com navegação para agendamento, perfil, preços e sair
* `AgendarServicoActivity`: Escolha de data/hora com verificação e confirmação de agendamento
* `ListaAgendamentosActivity`: Lista de agendamentos com RecyclerView
* `ProfileActivity` + `EditProfileActivity`: Visualização e edição de perfil
* `PrecosActivity`: Tela simples com imagem e botão de voltar

---

## 🗃️ Banco de Dados

### Tabelas

* `users`: username, email, fullName, passwordHash
* `agendamento`: id, servico, dataHora

---

## 📸 Capturas de Tela

> *(inserir imagens das principais telas aqui)*

---

## ⚙️ Como Rodar o Projeto

1. Clone o repositório

   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```
2. Abra no Android Studio
3. Execute no emulador ou dispositivo físico

---

## 🔐 Observações

* A senha é armazenada com hash SHA-256
* O sistema não permite agendamentos às segundas-feiras
* Todas as datas devem ser futuras

---
