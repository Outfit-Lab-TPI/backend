# Arquitectura del Proyecto - Outfit Lab Backend

## Tabla de Contenidos

1. [Visión General](#visión-general)
2. [Principios Arquitectónicos](#principios-arquitectónicos)
3. [Estructura de Capas](#estructura-de-capas)
4. [Reglas de Dependencia](#reglas-de-dependencia)
5. [Patrones de Configuración](#patrones-de-configuración)
6. [Guía de Implementación](#guía-de-implementación)
7. [Ejemplos Prácticos](#ejemplos-prácticos)
8. [Anti-Patrones (Qué NO Hacer)](#anti-patrones-qué-no-hacer)

---

## Visión General

Este proyecto sigue una **arquitectura hexagonal (puertos y adaptadores)** con un enfoque **pragmático** que prioriza la simplicidad y mantenibilidad sobre la pureza teórica.

### Características Clave

- ✅ **Bean-based configuration**: Usamos `@Bean` en clases `*Config.java` en lugar de anotaciones en el dominio
- ✅ **Separación de capas**: Domain, Infrastructure, Presentation
- ✅ **Inyección de dependencias manual**: Sin `@Service`, `@Repository`, `@Component` en dominio
- ✅ **POJOs en dominio**: Clases simples sin dependencias de frameworks

---

## Principios Arquitectónicos

### 1. Independencia del Dominio

**El dominio NO debe conocer la infraestructura**

```java
// ❌ INCORRECTO - Dominio importando infraestructura
package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.infrastructure.model.UserEntity; // ❌
import org.springframework.stereotype.Service; // ❌

@Service // ❌
public class RegisterUser {
    // ...
}
```

```java
// ✅ CORRECTO - Dominio independiente
package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository; // ✅
import com.outfitlab.project.domain.model.UserModel; // ✅

public class RegisterUser { // ✅ POJO sin anotaciones
    private final UserRepository userRepository;
    
    public RegisterUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // ...
}
```

### 2. Configuración con Beans

**Toda la configuración se hace en clases `*Config.java` en la capa de infraestructura**

```java
// ✅ CORRECTO
@Configuration
public class UserConfig {
    
    @Bean
    public UserRepository userRepository(UserJpaRepository jpaRepo) {
        return new UserRepositoryImpl(jpaRepo);
    }
    
    @Bean
    public RegisterUser registerUser(UserRepository userRepository) {
        return new RegisterUser(userRepository);
    }
}
```

### 3. Pragmatismo sobre Pureza

**Está permitido inyectar infraestructura directamente en use cases cuando simplifica el código**

```java
// ✅ ACEPTABLE - Inyección directa de infraestructura
@Bean
public RegisterUser registerUser(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,        // ← Spring Security
    AuthenticationManager authManager,      // ← Spring Security
    TokenRepository tokenRepository,        // ← JPA Repository
    JwtService jwtService,                  // ← Servicio de infraestructura
    UserJpaRepository userJpaRepository,    // ← JPA Repository
    GmailGateway gmailGateway
) {
    return new RegisterUser(userRepository, passwordEncoder, authManager, 
        tokenRepository, jwtService, userJpaRepository, gmailGateway);
}
```

**Motivación**: Evitar crear abstracciones innecesarias que solo agregan complejidad sin valor real.

---

## Estructura de Capas

```
src/main/java/com/outfitlab/project/
│
├── domain/                          # Capa de Dominio (Lógica de Negocio)
│   ├── model/                       # Modelos de dominio (POJOs)
│   │   ├── UserModel.java
│   │   ├── Role.java               # ← Conceptos de negocio van aquí
│   │   └── ...
│   ├── interfaces/                  # Contratos (interfaces)
│   │   ├── repositories/
│   │   │   ├── UserRepository.java
│   │   │   └── ...
│   │   └── gateways/
│   │       └── GmailGateway.java
│   ├── useCases/                    # Casos de uso (lógica de negocio)
│   │   ├── user/
│   │   │   ├── RegisterUser.java   # ← POJO sin anotaciones
│   │   │   ├── LoginUser.java
│   │   │   └── ...
│   │   └── garment/
│   │       └── ...
│   └── exceptions/                  # Excepciones de dominio
│       └── UserNotFoundException.java
│
├── infrastructure/                  # Capa de Infraestructura (Detalles técnicos)
│   ├── config/                      # Configuración de Spring
│   │   ├── UserConfig.java         # ← Beans de User
│   │   ├── GarmentConfig.java      # ← Beans de Garment
│   │   └── security/
│   │       ├── SecurityConfig.java
│   │       ├── AuthResponse.java   # ← DTOs de infraestructura
│   │       └── jwt/
│   │           ├── JwtService.java
│   │           └── Token.java      # ← Entidad JPA
│   ├── model/                       # Entidades JPA
│   │   ├── UserEntity.java
│   │   ├── PrendaEntity.java
│   │   └── ...
│   ├── repositories/                # Implementaciones de repositorios
│   │   ├── UserRepositoryImpl.java # ← Implementa UserRepository
│   │   ├── interfaces/              # JPA Repositories
│   │   │   ├── UserJpaRepository.java
│   │   │   └── ...
│   │   └── ...
│   ├── services/                    # Servicios de infraestructura
│   │   └── ...
│   └── gateways/                    # Implementaciones de gateways
│       └── GmailGatewayImpl.java
│
└── presentation/                    # Capa de Presentación (API REST)
    ├── controllers/
    │   ├── UserController.java
    │   └── ...
    └── dto/
        ├── UserDTO.java
        └── ...
```

---

## Reglas de Dependencia

### ✅ Dependencias Permitidas

```
Presentation → Domain
Presentation → Infrastructure
Infrastructure → Domain
Domain → (NADA, solo Java estándar)
```

### ❌ Dependencias Prohibidas

```
Domain → Infrastructure  ❌
Domain → Presentation    ❌
Domain → Spring          ❌
Domain → JPA             ❌
```

### Verificación Rápida

Para verificar que no hay violaciones:

```bash
# Buscar imports de infraestructura en dominio (NO debe haber resultados)
grep -r "import com.outfitlab.project.infrastructure" src/main/java/com/outfitlab/project/domain/

# Buscar anotaciones Spring en dominio (NO debe haber resultados)
grep -r "@Service\|@Repository\|@Component" src/main/java/com/outfitlab/project/domain/
```

---

## Patrones de Configuración

### Organización de Configs

Cada módulo de dominio tiene su propia clase de configuración:

- `UserConfig.java` → Beans relacionados con usuarios
- `GarmentConfig.java` → Beans relacionados con prendas
- `CombinationConfig.java` → Beans relacionados con combinaciones
- `SubscriptionConfig.java` → Beans relacionados con suscripciones

### Estructura de un Config

```java
@Configuration
public class UserConfig {

    // 1. Repositorios del dominio
    @Bean
    public UserRepository userRepository(UserJpaRepository jpaRepo, BrandJpaRepository brandRepo) {
        return new UserRepositoryImpl(jpaRepo, brandRepo);
    }

    // 2. Use Cases
    @Bean
    public RegisterUser registerUser(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        // ... otras dependencias
    ) {
        return new RegisterUser(userRepository, passwordEncoder, ...);
    }

    @Bean
    public GetAllUsers getAllUsers(UserRepository userRepository) {
        return new GetAllUsers(userRepository);
    }

    // 3. Servicios de infraestructura (si es necesario)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

## Guía de Implementación

### Crear un Nuevo Use Case

#### 1. Definir el Use Case en Domain

```java
// domain/useCases/user/UpdateUserProfile.java
package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.domain.interfaces.repositories.UserRepository;
import com.outfitlab.project.domain.model.UserModel;

public class UpdateUserProfile {  // ← POJO sin anotaciones
    
    private final UserRepository userRepository;
    
    // Constructor para inyección de dependencias
    public UpdateUserProfile(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Método de ejecución
    public UserModel execute(String email, String newName) {
        UserModel user = userRepository.findUserByEmail(email);
        user.setName(newName);
        return userRepository.saveUser(user);
    }
}
```

#### 2. Registrar el Bean en Config

```java
// infrastructure/config/UserConfig.java
@Configuration
public class UserConfig {
    
    // ... otros beans
    
    @Bean
    public UpdateUserProfile updateUserProfile(UserRepository userRepository) {
        return new UpdateUserProfile(userRepository);
    }
}
```

#### 3. Usar en el Controller

```java
// presentation/controllers/UserController.java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UpdateUserProfile updateUserProfile;
    
    public UserController(UpdateUserProfile updateUserProfile) {
        this.updateUserProfile = updateUserProfile;
    }
    
    @PutMapping("/{email}/profile")
    public ResponseEntity<UserDTO> updateProfile(
        @PathVariable String email,
        @RequestBody UpdateProfileRequest request
    ) {
        UserModel updated = updateUserProfile.execute(email, request.getName());
        return ResponseEntity.ok(UserDTO.convertToDTO(updated));
    }
}
```

### Crear un Nuevo Repositorio

#### 1. Definir la Interfaz en Domain

```java
// domain/interfaces/repositories/ProductRepository.java
package com.outfitlab.project.domain.interfaces.repositories;

import com.outfitlab.project.domain.model.ProductModel;
import java.util.List;

public interface ProductRepository {
    ProductModel findById(Long id);
    List<ProductModel> findAll();
    ProductModel save(ProductModel product);
    void delete(Long id);
}
```

#### 2. Crear la Entidad JPA en Infrastructure

```java
// infrastructure/model/ProductEntity.java
package com.outfitlab.project.infrastructure.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    
    // Getters, setters, constructores
    
    // Método de conversión
    public static ProductModel convertToModel(ProductEntity entity) {
        return new ProductModel(entity.getId(), entity.getName(), entity.getPrice());
    }
}
```

#### 3. Crear JPA Repository

```java
// infrastructure/repositories/interfaces/ProductJpaRepository.java
package com.outfitlab.project.infrastructure.repositories.interfaces;

import com.outfitlab.project.infrastructure.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    // Métodos de consulta personalizados si es necesario
}
```

#### 4. Implementar el Repositorio de Dominio

```java
// infrastructure/repositories/ProductRepositoryImpl.java
package com.outfitlab.project.infrastructure.repositories;

import com.outfitlab.project.domain.interfaces.repositories.ProductRepository;
import com.outfitlab.project.domain.model.ProductModel;
import com.outfitlab.project.infrastructure.model.ProductEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.ProductJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRepositoryImpl implements ProductRepository {  // ← Sin anotaciones
    
    private final ProductJpaRepository jpaRepository;
    
    public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public ProductModel findById(Long id) {
        ProductEntity entity = jpaRepository.findById(id).orElse(null);
        return entity != null ? ProductEntity.convertToModel(entity) : null;
    }
    
    @Override
    public List<ProductModel> findAll() {
        return jpaRepository.findAll().stream()
            .map(ProductEntity::convertToModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public ProductModel save(ProductModel product) {
        ProductEntity entity = new ProductEntity(product.getName(), product.getPrice());
        ProductEntity saved = jpaRepository.save(entity);
        return ProductEntity.convertToModel(saved);
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}
```

#### 5. Registrar el Bean

```java
// infrastructure/config/ProductConfig.java
@Configuration
public class ProductConfig {
    
    @Bean
    public ProductRepository productRepository(ProductJpaRepository jpaRepo) {
        return new ProductRepositoryImpl(jpaRepo);
    }
}
```

---

## Ejemplos Prácticos

### Ejemplo 1: Use Case Simple

```java
// domain/useCases/user/GetAllUsers.java
public class GetAllUsers {
    private final UserRepository userRepository;
    
    public GetAllUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public List<UserDTO> execute() {
        return userRepository.findAll()
            .stream()
            .map(UserDTO::convertToDTO)
            .toList();
    }
}
```

### Ejemplo 2: Use Case con Múltiples Dependencias

```java
// domain/useCases/user/RegisterUser.java
public class RegisterUser {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final GmailGateway gmailGateway;
    
    public RegisterUser(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        TokenRepository tokenRepository,
        GmailGateway gmailGateway
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.gmailGateway = gmailGateway;
    }
    
    public UserModel execute(RegisterDTO request) {
        // Lógica de negocio
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserModel user = new UserModel(request.getEmail(), hashedPassword);
        UserModel saved = userRepository.saveUser(user);
        
        // Enviar email de verificación
        gmailGateway.sendEmail(user.getEmail(), "Bienvenido", "...");
        
        return saved;
    }
}
```

### Ejemplo 3: Repositorio con Conversión Entity ↔ Model

```java
// infrastructure/repositories/UserRepositoryImpl.java
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;
    
    @Override
    public UserModel findUserByEmail(String email) {
        UserEntity entity = jpaRepository.findByEmail(email);
        return entity != null ? UserEntity.convertToModel(entity) : null;
    }
    
    @Override
    public UserModel saveUser(UserModel model) {
        UserEntity entity = UserEntity.convertFromModel(model);
        UserEntity saved = jpaRepository.save(entity);
        return UserEntity.convertToModel(saved);
    }
}
```

---

## Anti-Patrones (Qué NO Hacer)

### ❌ 1. Anotaciones Spring en Dominio

```java
// ❌ INCORRECTO
package com.outfitlab.project.domain.useCases.user;

import org.springframework.stereotype.Service;

@Service  // ❌ NO usar anotaciones en dominio
public class RegisterUser {
    // ...
}
```

### ❌ 2. Imports de Infrastructure en Domain

```java
// ❌ INCORRECTO
package com.outfitlab.project.domain.useCases.user;

import com.outfitlab.project.infrastructure.model.UserEntity;  // ❌
import com.outfitlab.project.infrastructure.config.security.jwt.JwtService;  // ❌

public class RegisterUser {
    private UserEntity user;  // ❌ Usar UserModel en su lugar
}
```

### ❌ 3. Lógica de Negocio en Controllers

```java
// ❌ INCORRECTO
@RestController
public class UserController {
    
    @Autowired
    private UserJpaRepository userRepository;  // ❌
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        // ❌ Lógica de negocio en el controller
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User exists");
        }
        
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        // ... más lógica
        
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
```

```java
// ✅ CORRECTO
@RestController
public class UserController {
    
    private final RegisterUser registerUser;  // ✅ Use case
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        try {
            UserModel user = registerUser.execute(dto);  // ✅ Delegar al use case
            return ResponseEntity.ok(UserDTO.convertToDTO(user));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

### ❌ 4. Retornar Entities desde Repositorios de Dominio

```java
// ❌ INCORRECTO
public interface UserRepository {
    UserEntity findByEmail(String email);  // ❌ Retornar Entity
}
```

```java
// ✅ CORRECTO
public interface UserRepository {
    UserModel findUserByEmail(String email);  // ✅ Retornar Model
}
```

**Excepción**: Está permitido retornar Entities en casos específicos donde simplifica el código y no viola la separación de responsabilidades (ej: `addToFavorite()` en repositorios de favoritos).

### ❌ 5. Crear Abstracciones Innecesarias

```java
// ❌ SOBRE-INGENIERÍA - No crear interfaces para todo
public interface AuthenticationService {
    void authenticate(String email, String password);
}

public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authManager;
    
    public void authenticate(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }
}
```

```java
// ✅ PRAGMÁTICO - Inyectar directamente cuando tiene sentido
@Bean
public LoginUser loginUser(
    UserRepository userRepository,
    AuthenticationManager authManager  // ✅ Directo, sin wrapper innecesario
) {
    return new LoginUser(userRepository, authManager);
}
```

### ❌ 6. Un Use Case Llamando a Otro Use Case

**Regla**: Los use cases NO deben llamar a otros use cases. La orquestación de múltiples casos de uso debe hacerse en el **controller**.

```java
// ❌ INCORRECTO - Use case orquestando otros use cases
public class RegisterUser {
    private final UserRepository userRepository;
    private final AssignFreePlanToUser assignFreePlanToUser;  // ❌
    
    public UserModel execute(RegisterDTO request) {
        // Lógica de registro
        UserModel user = userRepository.saveUser(newUser);
        
        // ❌ Llamando a otro use case
        assignFreePlanToUser.execute(user.getEmail());
        
        return user;
    }
}
```

```java
// ✅ CORRECTO - Controller orquestando use cases
@RestController
public class UserController {
    private final RegisterUser registerUser;
    private final AssignFreePlanToUser assignFreePlanToUser;
    
    @PostMapping("/register")
    @Transactional  // ✅ Control explícito de transacción
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        try {
            // 1. Registrar usuario
            UserModel user = registerUser.execute(dto);
            
            // 2. Asignar plan gratuito
            assignFreePlanToUser.execute(user.getEmail());
            
            return ResponseEntity.ok(UserDTO.convertToDTO(user));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

**Razones**:
- ✅ **Responsabilidad Única**: Cada use case hace UNA cosa
- ✅ **Reusabilidad**: `AssignFreePlanToUser` puede usarse en otros contextos
- ✅ **Testabilidad**: Más fácil testear use cases independientes
- ✅ **Claridad**: El controller muestra explícitamente el flujo completo
- ✅ **Transaccionalidad**: Mejor control sobre transacciones

---

## Checklist para Code Review

Usa este checklist al revisar Pull Requests:

### Dominio

- [ ] ¿Los use cases son POJOs sin anotaciones?
- [ ] ¿No hay imports de `com.outfitlab.project.infrastructure` en domain?
- [ ] ¿No hay imports de `org.springframework` en domain?
- [ ] ¿Los modelos de dominio son POJOs simples?
- [ ] ¿Las interfaces de repositorio retornan Models (no Entities)?
- [ ] ¿Los use cases NO llaman a otros use cases?

### Infraestructura

- [ ] ¿Los repositorios implementan las interfaces de dominio?
- [ ] ¿Las implementaciones convierten entre Entity y Model?
- [ ] ¿Los beans están registrados en las clases `*Config.java`?
- [ ] ¿Las entidades JPA están en `infrastructure.model`?

### Presentación

- [ ] ¿Los controllers solo orquestan (no tienen lógica de negocio)?
- [ ] ¿Los controllers usan DTOs para entrada/salida?
- [ ] ¿Los controllers delegan a use cases?

---

## Recursos Adicionales

### Comandos Útiles

```bash
# Verificar violaciones de arquitectura
./scripts/check-architecture.sh

# Ejecutar tests
./mvnw test

# Build con Docker
docker compose up -d --build
```

## Preguntas Frecuentes

### ¿Por qué no usamos `@Service` en los use cases?

Para mantener el dominio independiente de Spring. Los use cases deben ser POJOs que puedan ejecutarse sin un contenedor de Spring.

### ¿Cuándo está bien inyectar infraestructura directamente?

Cuando crear una abstracción adicional no aporta valor y solo agrega complejidad. Ejemplos: `PasswordEncoder`, `AuthenticationManager`, `JwtService`.

### ¿Dónde van los conceptos de negocio como `Role`?

En el dominio (`domain.model.Role`), no en infraestructura. Si es un concepto de negocio, va en domain.

### ¿Puedo usar Entities en los use cases?

No directamente. Los use cases deben trabajar con Models. Los repositorios se encargan de la conversión Entity ↔ Model.

### ¿Qué pasa si necesito usar una librería externa en un use case?

Crea una interfaz (gateway) en domain y la implementación en infrastructure. Ejemplo: `GmailGateway` (interface) → `GmailGatewayImpl` (implementación).

### ¿Puede un use case llamar a otro use case?

**No**. La orquestación de múltiples use cases debe hacerse en el controller. Esto mantiene cada use case con una responsabilidad única, facilita el testing, y da mejor control sobre transacciones.

---

**Última actualización**: 2025-11-21  
**Mantenido por**: Lucas - (FELI lo va a revisar.)
