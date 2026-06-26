classDiagram
    %% Relações de Herança (Generalização)
    Entity <|-- Player
    Entity <|-- Enemy
    Entity <|-- Bullet
    Entity <|-- AmmoDrop

    %% Relações de Composição e Agregação
    Swarm *-- Enemy : contém matriz 2D
    Barricade *-- BarricadeBlock : contém malha 2D
    BarricadeManager *-- Barricade : gerencia lista
    
    %% Relações de Associação e Dependência
    Bullet --> Entity : conhece a origem (origin)
    PlayerController --> Player : controla
    SwarmController --> Swarm : controla

    GameScreen *-- Player : instancia 1 ou 2
    GameScreen *-- Swarm : gerencia
    GameScreen *-- BarricadeManager : gerencia
    GameScreen *-- Bullet : lista ativa
    GameScreen *-- AmmoDrop : lista ativa
    GameScreen --> PlayerController : delega input
    GameScreen --> SwarmController : delega IA

    %% Definição da Classe Abstrata Base
    class Entity {
        <<abstract>>
        -float x
        -float y
        -float width
        -float height
        -Rectangle hitbox
        #TextureRegion sprite
        +update(float delta)*
        +draw(SpriteBatch batch)
        #updateHitbox()
        +getHitbox() Rectangle
    }

    %% Subclasses do Modelo
    class Player {
        +int points
        +int lives
        +int ammo
        -float invulnerableTimer
        +update(float delta)
        +moveLeft(float delta)
        +moveRight(float delta)
        +checkCollision(Bullet bullet) boolean
        +triggerInvulnerability()
    }

    class Enemy {
        +int health
        +TextureRegion bulletSprite
        +float bulletSpeed
        +update(float delta)
        +checkCollision(Bullet bullet) boolean
        +updateSprite(TextureRegion newSprite)
    }

    class Bullet {
        -float speed
        +int damage
        +boolean isValid
        +Entity origin
        +update(float delta)
    }

    class AmmoDrop {
        -float timeAlive
        +boolean isCollected
        +update(float delta)
        +draw(SpriteBatch batch)
    }

    class Swarm {
        +Enemy[][] enemies
        +int rows
        +int cols
        +int aliveCount
        +boolean movingRight
        +move(float deltaX, float deltaY)
    }

    %% Estrutura das Barricadas
    class BarricadeBlock {
        -State state
        -Rectangle hitbox
        +damage()
        +destroy()
        +isDestroyed() boolean
    }

    class Barricade {
        -List~List~BarricadeBlock~~ grid
        +checkCollision(Rectangle bulletHitbox) boolean
        +isFullyDestroyed() boolean
        +move(float deltaX, float deltaY)
    }

    class BarricadeManager {
        -List~Barricade~ barricades
        +createBarricades(int count, float screenWidth, float baseY)
        +checkBulletCollision(Rectangle bulletHitbox) boolean
    }

    %% Controladores
    class PlayerController {
        -Player player
        -SpaceInvadersGame game
        +update(float delta, List~Bullet~ bullets)
    }

    class SwarmController {
        -Swarm swarm
        -float shootTimer
        +update(float delta, List~Bullet~ bullets, List~AmmoDrop~ drops)
    }
