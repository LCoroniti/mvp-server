-- Testdaten für die lokale Entwicklung: ein Spiel, das "jetzt" läuft,
-- damit das Voting unter http://localhost:8080 sofort ausprobierbar ist.
--
-- Einspielen (bei laufender docker-compose-Datenbank):
--   docker exec -i mvp-postgres psql -U mvp -d mvp < testdata.sql
--
-- Alles zurücksetzen:
--   docker compose down -v && docker compose up -d
--   (danach den Server neu starten, damit Hibernate das Schema neu anlegt)

INSERT INTO leagues (league_id, name, season, group_id, match_plan_url)
VALUES (1, 'Bezirksoberliga Frauen', '25/26', 'test', 'http://localhost/unused')
ON CONFLICT DO NOTHING;

INSERT INTO genders (gender_id, name) VALUES (1, 'Frauen') ON CONFLICT DO NOTHING;

INSERT INTO clubs (club_id, name) VALUES (1, 'TuS Traunreut'), (2, 'SV Testheim')
ON CONFLICT DO NOTHING;

INSERT INTO teams (team_id, name, club_id, gender_id, league_id) VALUES
  (1, 'TuS Traunreut', 1, 1, 1),
  (2, 'SV Testheim', 2, 1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO players (player_id, first_name, surname, team_id) VALUES
  (1, 'Anna', 'Beispiel', 1), (2, 'Lena', 'Muster', 1), (3, 'Marie', 'Test', 1),
  (4, 'Julia', 'Gast', 2), (5, 'Sarah', 'Fremd', 2)
ON CONFLICT DO NOTHING;

-- Anpfiff vor 10 Minuten => Voting ist für ~50 weitere Minuten offen
-- (Zeiten werden ohne Zeitzone in deutscher Lokalzeit gespeichert)
INSERT INTO matches (match_id, start_timestamp, home_team_id, guest_team_id, has_report)
VALUES (1, (now() at time zone 'Europe/Berlin') - interval '10 minutes', 1, 2, false)
ON CONFLICT DO NOTHING;

INSERT INTO matchplayers (match_id, player_id, jersey_number) VALUES
  (1, 1, 7), (1, 2, 10), (1, 3, 13), (1, 4, 4), (1, 5, 9)
ON CONFLICT DO NOTHING;
