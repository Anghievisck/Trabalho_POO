# Space Invaders - Trabalho de POO

Este projeto é uma recriação expandida do clássico jogo arcade **Space Invaders**, desenvolvido em **Java** utilizando o framework **LibGDX**. O projeto foi construído sob o padrão arquitetural **MVC (Model-View-Controller)**, com forte ênfase na aplicação dos pilares da Programação Orientada a Objetos (POO).

## 🚀 Funcionalidades e Mecânicas de Jogo

* **Single-player e Multiplayer Co-op:** Suporte para 1 ou 2 jogadores simultâneos com controles independentes.
* **Sistema de Munição e Drops:** Jogadores começam com munição limitada. Pacotes de munição (`AmmoDrop`) surgem na tela aleatoriamente e desaparecem caso não sejam coletados em 8 segundos.
* **Invulnerabilidade (*I-frames*):** Ao perder uma vida, o jogador ganha 2.0 segundos de invulnerabilidade temporal, acompanhada de um efeito visual de piscar (*blinking*).
* **Barricadas Destrutíveis:** 4 barricadas distribuídas no cenário protegem os jogadores. Elas são formadas por uma malha de 6x8 blocos que desenham o formato clássico do arcade. Cada bloco individual possui 3 estados de degradação: Intacto, Danificado e Destruído.
* **Enxame Dinâmico (Swarm):** Matriz de inimigos 5x11 composta por 3 tipos distintos de alienígenas (Squid, Octopus e Crab), cada um com pontuações e atributos diferentes. A velocidade do enxame e a cadência dos tiros aumentam de acordo com o nível de dificuldade.
* **Save/Load System:** Pausa do jogo (tecla `ESC`) permitindo salvar o estado exato da partida (vidas, posição pixel a pixel dos inimigos, estados individuais dos blocos das barricadas e munição) utilizando o `Preferences` do LibGDX.

## 🏗️ Arquitetura Orientada a Objetos (MVC)

### 1. Model (`com.group9.spaceinvaders.model`)
O coração das regras de negócio e aplicação de herança e composição. Todo cálculo de hitbox, posicionamento espacial (`x`, `y`) e lógica de sobreposição é feito utilizando a classe `Rectangle` do LibGDX.

* **`Entity` (Classe Abstrata):** Define a base de todas as entidades visíveis no jogo. Encapsula as posições e amarra automaticamente o `hitbox` à coordenada espacial atualizada. Força a implementação do método `update(delta)` via polimorfismo.
* **`Player`, `Enemy`, `Bullet`, `AmmoDrop` (Herança):** Extensões concretas de `Entity`.
  * `Bullet` possui o atributo `origin` para distinguir quem disparou o projétil, evitando que os alienígenas (ou os próprios jogadores) sofram dano de fogo amigo.
* **`Swarm` (Composição):** Agrupa os inimigos em uma matriz bi-dimensional, gerenciando a movimentação lateral unificada, a descida em bloco (quando tocam as bordas) e controlando o total de alienígenas vivos.
* **Sistema de Barricadas (Composição Escalonada):** 
  * `BarricadeBlock`: Usa um `Enum` (`State`) para transitar entre texturas (Intacto, Danificado, Destruído).
  * `Barricade`: Organiza os blocos em uma lista bidimensional respeitando o design clássico de "arco" (removendo blocos específicos na matriz).
  * `BarricadeManager`: Controla o espaçamento dinâmico e o roteamento de checagem de colisão com os tiros para todas as barricadas ativas simultaneamente.

### 2. View (`com.group9.spaceinvaders.view`)
Responsável pela interface gráfica e o gameloop principal, estendendo o `ScreenAdapter`.
* Renderiza as camadas do jogo de forma limpa.
* O HUD desenha pontuações, munição e vidas via `Stage` e `Label`.
* **Telas:** `MainMenuScreen`, `GameScreen` (Renderização da batalha), `LevelCompleteScreen` e `GameOverScreen`.

### 3. Controller (`com.group9.spaceinvaders.controller`)
Atua como o elo entre os inputs do usuário/sistema e o Model.
* `PlayerController`: Monitora inputs de teclado e resolve as colisões dos projéteis inimigos contra o `Player`, disparando sons via `AssetManager`.
* `SwarmController`: Define qual alienígena específico fará o disparo em cada intervalo de tempo de forma pseudoaleatória. Altera as animações/sprites do enxame a cada passo dado.
* `BarricadeTextureGenerator`: Gera texturas em *runtime* (proceduralmente usando `Pixmap`) para os blocos da barricada, garantindo diferentes tonalidades de cinza conforme sofrem dano.

## 💻 Tecnologias e Padrões Utilizados

* **Linguagem:** Java 11+
* **Engine/Framework:** [LibGDX](https://libgdx.com/)
* **Padrões de Projeto Notáveis:**
  * **MVC:** Separação clara de responsabilidades.
  * **Game Loop:** Utilização padrão de `render(float delta)` para atualizações atreladas ao tempo (*frame-rate independent*).
  * **State Machine (simples):** Usado via `Enum` no bloqueio das barricadas.

## 🎮 Controles

### Jogador 1
* **Mover Esquerda / Direita:** `Setas Direcionais` ⬅️ ➡️
* **Atirar:** `Seta para Cima` ⬆️

### Jogador 2 (Multiplayer)
* **Mover Esquerda / Direita:** `A` / `D`
* **Atirar:** `W`

### Gerais
* **Pausar / Menu:** `ESC`

## 🛠️ Como Executar o Projeto

Certifique-se de ter o **Java JDK 11 ou superior** instalado na sua máquina.

1. Clone o repositório:
```bash
   git clone [https://github.com/Anghievisck/Trabalho_POO.git](https://github.com/Anghievisck/Trabalho_POO.git)
