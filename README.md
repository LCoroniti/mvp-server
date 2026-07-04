# MVP-Voting – Backend (mvp-server)

Spring-Boot-Backend für das „Player of the Match"-Voting des TuS Traunreut.
Es scrapt Spielpläne und Spielerkader vom nuLiga-Portal (Bayerischer Handball-Verband),
plant sich daraus automatisch Aktualisierungs-Tasks rund um jeden Anpfiff und stellt die
REST-API sowie das fertig gebaute React-Frontend (Repository **mvp-web**) bereit.

## Architektur

Multi-Modul-Gradle-Projekt (Spring Boot 3.3, Java 22):

| Modul | Inhalt |
|---|---|
| `model` | JPA-Entities: Club, Gender, League, Team, Player, Match, MatchPlayer, Vote, ScheduledTask |
| `scraper` | nuLiga-Scraper (jsoup für HTML-Spielpläne, HttpClient für das REST-Backend `hbde-live.liga.nu`) |
| `server` | Spring-Boot-Anwendung: Controller, Services, Scheduler, statisches Frontend |
| `log` | SLF4J-Logging-Fassade |
| `util` | Datums-/Zeitzonen-Hilfen (Europe/Berlin) |

### Ablauf im Betrieb

1. Beim Start (nur Profil `prod`) legt der `ScheduledTasksBootstrapper` einen
   `UpdateAllMatchesTask` an, der alle in der DB gepflegten Ligen scrapt.
2. Für jedes neue oder verschobene Spiel werden automatisch drei Tasks geplant:
   Meeting-ID holen (Anpfiff −5 min), Spielerkader laden (Anpfiff),
   Ergebnis aktualisieren (Anpfiff +2 h).
3. Der `TaskSchedulingService` synchronisiert die geplanten Tasks alle 30 s mit der Datenbank.

Welche Ligen/Teams zum Verein gehören, ist **reiner Dateninhalt** (über die Admin-Seite
`/admin/leagues` bzw. die API gepflegt) – im Code ist nichts vereinsspezifisch verdrahtet
außer dem Paketnamen und dem Frontend-Branding.

### Wichtigste Endpoints

- `GET /api/match/week`, `GET /api/match/past`, `GET /api/match/{id}` – Spiele
- `POST/DELETE /api/vote`, `GET /api/vote/check` – Abstimmen (Voter-Token aus Cookie)
- `GET/POST/PUT /api/league`, `GET/POST/PUT/DELETE /api/team` – Stammdaten (Admin)
- `GET /api/scheduler` – geplante Tasks (Admin)
- Alle übrigen Pfade werden auf die React-SPA (`index.html`) weitergeleitet.

Der Admin-Bereich (`/admin/*`), `/api/scheduler` und alle schreibenden Endpoints
sind per HTTP Basic Auth geschützt (`SecurityConfig`): Beim Aufruf einer Admin-Seite
fragt der Browser nach Benutzername/Passwort (`ADMIN_USERNAME`/`ADMIN_PASSWORD`).
Voting und alle Lese-Endpoints sind öffentlich.

## Konfiguration

`server/src/main/resources/application.properties` liest die Datenbank-Zugangsdaten aus
Umgebungsvariablen:

| Variable | Bedeutung |
|---|---|
| `TUS_HEROKU_URL` | JDBC-URL der PostgreSQL-Datenbank (`jdbc:postgresql://…`) |
| `TUS_HEROKU_USERNAME` | DB-Benutzer |
| `TUS_HEROKU_PASSWORD` | DB-Passwort |
| `PORT` | HTTP-Port (Default 8080, wird von Heroku gesetzt) |
| `APP_CORS_ALLOWEDORIGINS` | Erlaubte CORS-Origins (Default `http://localhost:3000`, nur für lokale Frontend-Entwicklung nötig) |
| `ADMIN_USERNAME` / `ADMIN_PASSWORD` | Login für den Admin-Bereich (`/admin/*`, `/api/scheduler`, alle schreibenden Endpoints). Lokaler Default: admin/admin — **in Produktion zwingend setzen!** |

Das Profil `prod` (in den Properties aktiv) startet den Scraping-Scheduler. Für lokale
Läufe ohne Scraping `SPRING_PROFILES_ACTIVE=default` setzen.

## Bauen & lokal starten

Java 22 wird über die Gradle-Toolchain automatisch heruntergeladen, falls nicht
installiert. Die lokale Datenbank kommt aus `docker-compose.yml`; die Defaults in
`application.properties` passen dazu, und `ddl-auto=update` legt das Schema beim
ersten Start automatisch an (es gibt keine Flyway/Liquibase-Migrationen).

```bash
docker compose up -d          # PostgreSQL 16 auf localhost:5432 (mvp/mvp/mvp)
./gradlew build -x test
java -jar server/build/libs/server.jar
```

Danach läuft alles unter http://localhost:8080 (inklusive Frontend). Mit
`testdata.sql` lässt sich ein gerade laufendes Testspiel einspielen, um das
Voting sofort auszuprobieren:

```bash
docker exec -i mvp-postgres psql -U mvp -d mvp < testdata.sql
```

Falls `TUS_HEROKU_*`-Umgebungsvariablen gesetzt sind, überschreiben sie die
lokalen Defaults — für lokale Läufe ggf. in der Shell auf die Docker-Werte setzen.

## Deployment (Heroku)

Das Projekt ist für Heroku eingerichtet:

- `Procfile`: `web: java -jar server/build/libs/server.jar`
- `system.properties`: Java-Version 22
- Config Vars (`TUS_HEROKU_*`) im Heroku-Dashboard setzen
- Datenbank: Heroku Postgres (die JDBC-URL ergibt sich aus der `DATABASE_URL` des Add-ons)

Deploy per `git push heroku <branch>:main` – Heroku baut mit dem Gradle-Buildpack
(`./gradlew build -x test`) und startet den Prozess aus dem Procfile.

## Frontend aktualisieren

Das React-Frontend wird im Repository **mvp-web** entwickelt. Nach `npm run build` den
Inhalt von `build/` nach `server/src/main/resources/static/` kopieren und das Backend
neu deployen.
