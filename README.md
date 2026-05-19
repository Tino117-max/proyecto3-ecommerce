# Proyecto3 - E-Commerce UTS
### Sistema de E-Commerce con Spring Boot + Spring Security + Thymeleaf

**Autor:** Jaider Javier Jaimes | ID: 1097095697  
**Programa:** Ingeniería de Sistemas - UTS Bucaramanga  
**Año:** 2025

---

## 📋 Descripción

Sistema de e-commerce completo con autenticación y autorización por roles implementado con:
- **Spring Boot 3.2** - Framework backend
- **Spring Security 6** - Seguridad (autenticación + autorización por roles)
- **Thymeleaf 3** - Motor de plantillas frontend
- **Spring Data JPA / Hibernate** - ORM
- **MySQL 8.0** - Base de datos
- **Bootstrap 5** - Diseño responsivo

---

## 🔐 Cómo funciona Spring Security en este proyecto

### 1. Configuración (`SecurityConfig.java`)
Define qué rutas son públicas y cuáles requieren rol:
```
/login, /registro, /catalogo  →  público (sin autenticación)
/admin/**                     →  solo ROLE_ADMIN
/cliente/**                   →  ROLE_CLIENTE o ROLE_ADMIN
```

### 2. UserDetailsServiceImpl
Spring Security llama a `loadUserByUsername(email)` al hacer login.
Busca el usuario en MySQL, carga sus roles y retorna un `UserDetails`.

### 3. BCryptPasswordEncoder
Las contraseñas nunca se guardan en texto plano. Se cifran con BCrypt strength=10.

### 4. Redirección por rol
Después del login exitoso, el `AuthenticationSuccessHandler` redirige:
- `ROLE_ADMIN` → `/admin/dashboard`
- `ROLE_CLIENTE` → `/cliente/home`

### 5. Thymeleaf + Spring Security
Las plantillas usan `sec:authorize` para mostrar/ocultar elementos:
```html
<div sec:authorize="hasRole('ADMIN')">Solo el admin ve esto</div>
<div sec:authorize="isAuthenticated()">Solo usuarios logueados</div>
<span sec:authentication="name">Email del usuario actual</span>
```

---

## 🚀 Instrucciones de ejecución

### Prerrequisitos
- Java 17+
- Maven 3.9+
- MySQL 8.0

### Pasos

1. **Clonar el repositorio**
```bash
git clone https://github.com/TU_USUARIO/proyecto3-ecommerce.git
cd proyecto3-ecommerce
```

2. **Crear la base de datos**
```bash
mysql -u root -p < schema.sql
```

3. **Configurar credenciales MySQL** en `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

4. **Ejecutar el proyecto**
```bash
mvn spring-boot:run
```

5. **Abrir en el navegador**
```
http://localhost:8080
```

---

## 👤 Credenciales de prueba

| Rol | Email | Contraseña |
|-----|-------|-----------|
| Admin | admin@uts.edu.co | admin123 |
| Cliente | jaider@uts.edu.co | cliente123 |

---

## 📁 Estructura del Proyecto

```
src/main/java/com/proyecto3/ecommerce/
├── EcommerceApplication.java          # Punto de entrada
├── config/
│   └── SecurityConfig.java            # ⭐ Configuración Spring Security
├── controller/
│   ├── AuthController.java            # Login, Registro
│   ├── AdminController.java           # Panel de administración
│   ├── ClienteController.java         # Área del cliente
│   └── PublicController.java          # Catálogo público
├── model/
│   ├── Usuario.java                   # Entidad usuario
│   ├── Rol.java                       # Entidad rol
│   ├── Producto.java                  # Entidad producto
│   ├── Pedido.java                    # Entidad pedido
│   └── DetallePedido.java             # Entidad detalle
├── repository/                        # Interfaces JpaRepository
├── service/
│   ├── UserDetailsServiceImpl.java    # ⭐ Puente con Spring Security
│   ├── UsuarioService.java
│   └── ProductoService.java
src/main/resources/
├── templates/
│   ├── auth/login.html                # ⭐ Formulario de login
│   ├── auth/registro.html
│   ├── admin/dashboard.html
│   ├── admin/productos.html
│   ├── admin/producto-form.html
│   ├── admin/usuarios.html
│   ├── cliente/home.html
│   ├── public/catalogo.html
│   └── fragments/layout.html         # Navbar con sec:authorize
├── application.properties
schema.sql                             # Script BD completo
```

---

## 🗄️ Base de Datos

Tablas: `usuarios`, `roles`, `usuario_roles`, `productos`, `pedidos`, `detalle_pedido`, `carritos`, `items_carrito`

Ver archivo `schema.sql` para el script completo con datos de prueba.

---

## 📦 Dependencias principales (pom.xml)

```xml
spring-boot-starter-web
spring-boot-starter-security
spring-boot-starter-thymeleaf
thymeleaf-extras-springsecurity6
spring-boot-starter-data-jpa
mysql-connector-j
spring-boot-starter-validation
lombok
```

