# HANDOFF — Claude Code devam notu

> Yeni bilgisayarda Claude Code'a: **"HANDOFF.md oku, kaldığımız yerden devam edelim"** de.
> Bu dosya, Spring öğrenme yolculuğumun durumunu ve çalışma tarzımı Claude'a aktarır.

## Ben kimim / nasıl çalışırım
- Türk developer, Spring Boot öğreniyorum. ~2026-06-12'de **banka backend** işine başlıyorum.
- **Kodu ben yazarım.** Claude senior mentor gibi davransın: kavram + ne değişecek + neden + nasıl çalışır. Bana kod yazıp vermesin, yönlendirsin.
- Açıklamalar **Türkçe**, banka-domain örnekleriyle.
- Caveman mode aktif (terse yanıt) — kod/güvenlik/commit hariç.

## Proje: rentACar
- Spring Boot 4.0.6, Java 21, **Oracle Free** (Docker, FREEPDB1), Spring Data JPA, Lombok, ModelMapper, Flyway, Spring Security + JWT.
- Katmanlı mimari (TR isimlendirme): webApi/controllers, business/{abstracts,concretes,requests,responses,rules}, dataAccess/abstracts, entities/{concretes,abstracts}, core/{config,security,utilities}.
- Remote: https://github.com/BaranDatlar/Car-Rental-Spring-Boot-Project.git
- Git kimliği: **barandatlarr@gmail.com** (Baran Datlar).

## Tamamlanan: Banka Mini-Projesi (concurrency) ✅
Account + TransferTransaction üzerinden 5 adım yazıldı + **canlı test edildi**:

1. **JPA Auditing** — `BaseEntity` (@MappedSuperclass), `@CreatedBy/@CreatedAt/@LastModifiedBy/@LastModifiedAt`, `AuditorAware` JWT user'dan.
2. **`@Transactional`** — atomic debit/credit, RuntimeException → rollback (canlı görüldü: yarım transfer geri alındı). readOnly, rollbackFor, propagation (REQUIRES_NEW audit log için), self-invocation proxy tuzağı.
3. **Optimistic lock** — Account `@Version` + `spring-retry` `@Retryable(retryFor=ObjectOptimisticLockingFailureException)` + `@Recover`. Lost update canlı kanıtlandı, retry çalışıyor.
4. **Pessimistic lock** — `AccountRepository.findByAccountNumberForUpdate` + `@Lock(PESSIMISTIC_WRITE)`, **sıralı kilit** (accountNumber compareTo) ile deadlock önleme. Seri infaz canlı görüldü (A 8s / B 16s).
5. **Idempotency** — `Idempotency-Key` HTTP header → `reference` (unique) olarak kaydedilir. Pre-check + unique constraint + `retryFor=DataIntegrityViolationException`. Eşzamanlı çift-gönderim → tek transfer (canlı kanıtlandı).

Para her yerde **BigDecimal** (precision=19, scale=4). Enum'lar `@Enumerated(STRING)`.

### pom.xml notu (önemli tuzak)
Boot 4.0.6 BOM `spring-boot-starter-aop`'u **yönetmiyor** ve Central'da 4.0.6 yok (sadece 4.0.0-M2). Çözüm: starter-aop yerine explicit `org.aspectj:aspectjweaver:1.9.25.1` + `org.springframework.retry:spring-retry:2.0.12`. spring-aop zaten transitive geliyor.

## Kalan işler (öncelik sırası)
1. **Güvenlik — secret externalize:** `application.properties`'te JWT secret + DB password **plain-text**. Banka'da red flag. Profile/env değişkenine taşı. (İlk yapılacak.)
2. **Testing** — bilinçli en sona bırakıldı. Sadece `BrandManagerTest.getAll` var. Banka mini-projenin (Transfer concurrency) testleri YOK. Banka için kritik açık.
3. Polish: `GetAccountResponse` `id` leak ediyor; idempotent replay 201 yerine 200 dönmeli; `transferPessimistic`'e idempotency eklenmedi.
4. Sonra: **Redis + Kafka** öğrenmek (asıl bir sonraki hedef).

## Çalışma ortamı kurulumu (yeni PC)
- Oracle Free Docker + `rentacar`/`rentacar` user (FREEPDB1). Kurulum komutları için Claude'a sor.
- `./mvnw spring-boot:run` ile başlat. Swagger: http://localhost:8080/swagger-ui.html
- Endpoint'ler JWT korumalı — Swagger'dan login → Bearer token al.

## Claude hafızası (opsiyonel, tam süreklilik için)
Eski PC'de Claude'un kalıcı hafızası şurada:
`~/.claude/projects/-Users-burhanmac-IdeaProjects-rentACar/memory/`
İçindeki `MEMORY.md` + `*.md` dosyalarını yeni PC'de aynı proje memory klasörüne kopyalarsan Claude tüm bağlamı hatırlar. Kopyalamazsan bu HANDOFF.md yeter.
