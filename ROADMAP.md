# rentACar — Junior Spring Backend Yol Haritası

Bu projeyi olgunlaştırarak junior Spring backend developer seviyesine çıkma yol haritası. Her adım sırayla uygulanacak — bir adım bitmeden bir sonrakine geçilmeyecek.

**Mevcut durum**: Temel CRUD, layered architecture, JPA, DTO, ModelMapper, global exception handling. (~%20-25)

**Hedef**: Junior Spring rolüne güvenle başvurabilecek seviye. (~%70+)

---

## Adım 1 — REST Refactor + Validation

**Süre**: 3-5 gün

### Niye

Mevcut endpoint'ler RESTful değil (`/getAll`, `/add`). Validation starter ekli ama hiç kullanılmıyor. Mülakatta ilk göze çarpan iki şey.

### Bu projede yapılacaklar

- [ ] `BrandsController` endpoint'lerini RESTful yap:
  - `GET /api/brands` (getAll)
  - `GET /api/brands/{id}` (getById)
  - `POST /api/brands` (create)
  - `PUT /api/brands/{id}` (update)
  - `DELETE /api/brands/{id}` (delete)
- [ ] `ModelsController` için aynı refactor
- [ ] `UpdateBrandRequest`'ten `id` field'ını çıkar, path'ten al (`@PathVariable`)
- [ ] `DeleteBrandRequest`'i sil — `@PathVariable int id` yeterli
- [ ] `@Valid` ekle her `@RequestBody` parametresine
- [ ] Request DTO'larına validation annotation'ları:
  - `CreateBrandRequest.name` → `@NotBlank`, `@Size(min=2, max=50)`
  - `CreateModelRequest.name` → `@NotBlank`, `@Size(min=1, max=50)`
  - `CreateModelRequest.brandId` → `@Positive`
- [ ] `GlobalExceptionHandler`'a `MethodArgumentNotValidException` handler ekle — validation hatalarını temiz JSON döndürsün
- [ ] HTTP status code'ları doğru kullan:
  - POST create → `201 Created`
  - DELETE → `204 No Content`
  - GET found → `200 OK`
  - GET not found → `404 Not Found`

### Öğrenilecek kavramlar

- REST verb'lerinin doğru kullanımı
- HTTP status code semantiği
- `@Valid` + `BindingResult`
- `MethodArgumentNotValidException` yapısı
- `ResponseEntity<T>` ile explicit status dönüşü

### Bitti sayma kriteri

Swagger üzerinden geçersiz body gönderildiğinde temiz validation error JSON dönüyor. CRUD operasyonları RESTful endpoint'lerden çalışıyor.

---

## Adım 2 — Transaction Yönetimi

**Süre**: 2-3 gün

### Niye

Çoklu repository çağırısı yapan service metodları transactional olmalı. Olmazsa partial failure durumunda data tutarsızlığı oluşur.

### Bu projede yapılacaklar

- [ ] Tüm service metodlarına `@Transactional` ekle (`org.springframework.transaction.annotation.Transactional`)
- [ ] Read-only metodlarda `@Transactional(readOnly = true)` — performans optimizasyonu
- [ ] Write metodlarında default `@Transactional` (REQUIRED propagation)
- [ ] `ModelManager.add` örneği: `brandRepository.findById` + `modelRepository.save` aynı transaction'da olmalı
- [ ] Bilinçli test: deliberately bir exception fırlat ortada, rollback olduğunu DB'den doğrula

### Öğrenilecek kavramlar

- ACID
- Transaction propagation (REQUIRED, REQUIRES_NEW, SUPPORTS, MANDATORY, NESTED)
- Isolation level (READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE)
- `readOnly = true` ne yapar (Hibernate flush mode, dirty check skip)
- `rollbackFor` ve `noRollbackFor`
- Self-invocation problem (aynı sınıf içinden çağırınca proxy bypass)

### Bitti sayma kriteri

Tüm `Manager` sınıflarında uygun `@Transactional` annotation'ı var. Propagation'ı sözlü açıklayabiliyorsun.

---

## Adım 3 — Pagination & Sorting

**Süre**: 3-5 gün

### Niye

Production'da liste endpoint'leri ham `List<T>` döndürmez. 1 milyon kayıt varsa felaket. Junior'dan beklenir.

### Bu projede yapılacaklar

- [ ] `BrandService.getAll` imzasını değiştir: `Page<GetAllBrandsResponse> getAll(Pageable pageable)`
- [ ] `ModelService.getAll` için aynı
- [ ] Controller'larda `Pageable` parametre olarak al — Spring otomatik bind eder query string'ten
- [ ] Test: `GET /api/brands?page=0&size=10&sort=name,asc`
- [ ] `Page<T>` response'unu DTO'ya map'le — `page.map(brand -> mapper.map(...))`
- [ ] Filter eklemek için: `BrandRepository`'ye `findByNameContaining(String name, Pageable pageable)` ekle

### Öğrenilecek kavramlar

- `Pageable`, `PageRequest`, `Sort`
- `Page<T>` vs `Slice<T>` vs `List<T>` farkı
- Spring Data query derivation (method name'den SQL üretme)
- Frontend ile pagination contract

### Bitti sayma kriteri

Swagger'dan sayfalı liste çekiliyor, total count, totalPages, current page bilgisi dönüyor.

---

## Adım 4 — Logging Refactor

**Süre**: 1 gün

### Niye

`System.out.println` production'da yok. Log level'ları, structured logging, log dosyaları için SLF4J standart.

### Bu projede yapılacaklar

- [ ] Tüm `System.out.println` çağrılarını sil
- [ ] Service ve controller sınıflarına `@Slf4j` (Lombok) ekle
- [ ] Log level'larını uygun yerlerde kullan:
  - `log.debug(...)` — detaylı bilgi (request body, mapper sonuçları)
  - `log.info(...)` — önemli olaylar (entity oluşturuldu, login)
  - `log.warn(...)` — beklenmedik ama recoverable durumlar
  - `log.error(...)` — exception yakaladığında
- [ ] `GlobalExceptionHandler` içinde `log.error("Business exception: {}", ex.getMessage(), ex)` ekle
- [ ] `application.properties`'te log level configure et:
  ```
  logging.level.root=INFO
  logging.level.com.baran.rentacar=DEBUG
  logging.level.org.hibernate.SQL=DEBUG
  ```

### Öğrenilecek kavramlar

- SLF4J facade pattern
- Parametreli logging (`log.info("User {} created", id)` vs `log.info("User " + id + " created")` — performans farkı)
- Log level hiyerarşisi
- MDC (Mapped Diagnostic Context) — request tracing için

### Bitti sayma kriteri

Hiç `System.out` kalmamış. Log dosyası (`logs/spring.log` veya console) düzenli formatta akıyor.

---

## Adım 5 — Testing

**Süre**: 1-2 hafta

### Niye

**En büyük açık.** Test yazmadan kod merge edilen şirket yok. Junior'dan en çok beklenen beceri.

### Bu projede yapılacaklar

#### Unit test (Mockito)

- [ ] `BrandManagerTest` — `@ExtendWith(MockitoExtension.class)`, `BrandRepository` mock
- [ ] Test case'leri:
  - `getAll_shouldReturnListOfBrands()`
  - `getById_whenBrandExists_shouldReturnBrand()`
  - `getById_whenBrandNotExists_shouldThrowException()`
  - `add_whenNameAlreadyExists_shouldThrowBusinessException()`
  - `add_whenValid_shouldSaveBrand()`
- [ ] `ModelManager` için aynı pattern
- [ ] `BrandBusinessRules` için unit test

#### Repository test (`@DataJpaTest`)

- [ ] `BrandRepositoryTest` — H2 in-memory DB
- [ ] `existsByName` metodunun çalıştığını doğrula
- [ ] Custom query metodları için test

#### Integration test (`@SpringBootTest` + `MockMvc`)

- [ ] `BrandsControllerIntegrationTest`
- [ ] Full request → response flow test et
- [ ] Validation hata case'lerini test et (geçersiz body → 400)
- [ ] Business exception case'leri (mükerrer isim → 400 + ProblemDetails)

### Öğrenilecek kavramlar

- JUnit 5 (`@Test`, `@BeforeEach`, `@DisplayName`, `assertThrows`)
- Mockito (`@Mock`, `@InjectMocks`, `when().thenReturn()`, `verify()`)
- AAA pattern (Arrange-Act-Assert) / Given-When-Then
- Test slicing — `@DataJpaTest`, `@WebMvcTest`, `@SpringBootTest`
- MockMvc API
- Test coverage hedefleri
- Mocking vs Stubbing vs Faking
- Test naming conventions

### Bitti sayma kriteri

`mvn test` çalıştırınca en az 20+ test geçiyor. Coverage %60+. Service'in her public metodunun en az bir test'i var.

---

## Adım 6 — N+1 Problem & Fetch Tuning

**Süre**: 3-5 gün

### Niye

JPA'nın gerçek hayatta canını yakan kısmı. Junior+ pozisyonların ayırt edici sorusu.

### Bu projede yapılacaklar

- [ ] `application.properties`'te SQL log'u açık tut: `spring.jpa.show-sql=true`, `spring.jpa.properties.hibernate.format_sql=true`
- [ ] `Model` entity'sinde `@ManyToOne` fetch type'ını LAZY yap:
  ```java
  @ManyToOne(fetch = FetchType.LAZY)
  ```
- [ ] `Brand.models` zaten LAZY (OneToMany default) — ama açıkça yaz
- [ ] `ModelManager.getAll` çağrısı sırasında SQL log'u izle — N+1 görmen lazım (1 SELECT models + N SELECT brand)
- [ ] Fix için 3 yol dene, farkı gör:
  1. `@EntityGraph(attributePaths = {"brand"})` — repository metoduna ekle
  2. JPQL `JOIN FETCH`: `@Query("SELECT m FROM Model m JOIN FETCH m.brand")`
  3. **DTO projection** (en iyi): JPQL constructor expression — entity hiç yüklenmesin
- [ ] `MultipleBagFetchException` ile tanış (iki `@OneToMany` aynı anda JOIN FETCH yapamazsın)
- [ ] **`@Data` entity'de tehlikeli**: hashCode/equals tüm field'lara bakar, lazy proxy tetikler. Entity'lerde `@Getter` `@Setter` `@NoArgsConstructor` `@AllArgsConstructor` kullan, `@Data` kaldır.

### Öğrenilecek kavramlar

- LAZY vs EAGER fetch
- Persistence context, first-level cache
- N+1 problem mekaniği
- `JOIN FETCH` vs `@EntityGraph` vs DTO projection
- Cartesian product problem (çoklu JOIN FETCH)
- Open-session-in-view pattern (anti-pattern olarak da bilinir)
- `LazyInitializationException`
- Hibernate proxy mekaniği

### Bitti sayma kriteri

`GET /api/models` çağrısında SQL log'da tek query (veya 2 query) görüyorsun, N+1 yok. Niye öyle olduğunu açıklayabiliyorsun.

---

## Adım 7 — DB Migration (Flyway)

**Süre**: 2-3 gün

### Niye

`ddl-auto=update` öğrenme için. Production'da kullanan şirket yok. Migration tool sektör standardı.

### Bu projede yapılacaklar

- [ ] `pom.xml`'e Flyway dependency ekle (`flyway-core` + `flyway-database-postgresql`)
- [ ] `application.properties`'te:
  ```
  spring.jpa.hibernate.ddl-auto=validate
  spring.flyway.enabled=true
  spring.flyway.locations=classpath:db/migration
  ```
- [ ] DB'yi sıfırla (manuel veya `flyway:clean`)
- [ ] Migration dosyaları oluştur (`src/main/resources/db/migration/`):
  - `V1__create_brands_table.sql`
  - `V2__create_models_table.sql`
  - `V3__create_cars_table.sql`
- [ ] Versioning convention'ı öğren: `V<version>__<description>.sql`
- [ ] Repeatable migration (`R__view_definitions.sql`) ile tanış
- [ ] Production'da migration nasıl çalışır anla — startup sırasında otomatik

### Öğrenilecek kavramlar

- Schema migration vs ORM auto-generation
- Migration versioning
- Rollback stratejisi (Flyway/Liquibase rollback yapmaz — forward-only)
- `ddl-auto` değerleri: `none`, `validate`, `update`, `create`, `create-drop`
- Production deploy'da migration sırası

### Bitti sayma kriteri

Uygulamayı boş bir DB üzerinde başlattığında Flyway tabloları oluşturuyor. `flyway_schema_history` tablosunda version kayıtları var.

---

## Adım 8 — Spring Security + JWT

**Süre**: 1-2 hafta

### Niye

Her ürün API'sinde auth var. Junior pozisyon mülakatlarında %80 sorulur.

### Bu projede yapılacaklar

- [ ] `pom.xml`'e Spring Security + JWT (jjwt) dependency ekle
- [ ] `User` entity oluştur: id, email, password (hashed), role
- [ ] `UserRepository extends JpaRepository<User, Long>` + `findByEmail`
- [ ] `UserDetailsService` implementasyonu
- [ ] `PasswordEncoder` (BCrypt) bean
- [ ] `SecurityFilterChain` konfigürasyonu:
  - `/api/auth/**` public
  - `/api/brands/**` POST/PUT/DELETE → `ROLE_ADMIN`
  - `/api/brands/**` GET → authenticated
- [ ] `AuthController`: `POST /api/auth/register`, `POST /api/auth/login`
- [ ] JWT token üretimi (login response'unda dön)
- [ ] JWT filter (her request'te token'ı doğrula, SecurityContext'e user koy)
- [ ] `@PreAuthorize("hasRole('ADMIN')")` ile metod seviyesi yetkilendirme
- [ ] Test: Login ol → token al → token ile brand create et → token olmadan dene 401 al

### Öğrenilecek kavramlar

- Authentication vs Authorization
- Session-based vs Token-based auth
- JWT yapısı (header.payload.signature)
- BCrypt password hashing
- `SecurityFilterChain`, filter order
- `UsernamePasswordAuthenticationFilter`
- `@PreAuthorize`, `@PostAuthorize`, `@Secured`
- CSRF (REST API'de niye disable edilir)
- CORS configuration

### Bitti sayma kriteri

Postman/Swagger'dan auth akışı çalışıyor: register → login → JWT alıyor → korunan endpoint'i token ile çağırıyor. Token olmadan 401, yetki yoksa 403 dönüyor.

---

## Sonraki adımlar (yol haritası dışı, ekstra)

Bunlar bittikten sonra ele alınacak — junior pozisyonu için zorunlu değil, ama CV'de fark yaratır:

- **Docker**: Postgres + uygulamayı container'da çalıştırma, `docker-compose.yml`
- **Profiles**: `application-dev.properties`, `application-prod.properties`
- **Actuator + Health checks**: `/actuator/health`, custom health indicator
- **Caching**: `@Cacheable` ile Brand listesi cache'le
- **Async**: `@Async` ile background job (örn. email gönderme)
- **OpenAPI annotations**: `@Operation`, `@ApiResponse`, `@Schema` ile zengin Swagger
- **CI/CD**: GitHub Actions ile build + test pipeline
- **Code quality**: SpotBugs, Checkstyle, SonarLint

---

## İlerleme Tablosu

| Adım | Konu | Tahmini Süre | Durum |
|---|---|---|---|
| 1 | REST + Validation | 3-5 gün | ⏳ |
| 2 | `@Transactional` | 2-3 gün | ⏳ |
| 3 | Pagination | 3-5 gün | ⏳ |
| 4 | Logging | 1 gün | ⏳ |
| 5 | Testing | 1-2 hafta | ⏳ |
| 6 | N+1 & Fetch | 3-5 gün | ⏳ |
| 7 | Flyway | 2-3 gün | ⏳ |
| 8 | Security + JWT | 1-2 hafta | ⏳ |

**Toplam tahmini süre**: 6-8 hafta düzenli çalışma.

**Bittiğinde**: ~%70+ junior Spring backend hazırlığı. Mülakatlara güvenle başvurabilirsin.
