### dummy_match_table Wiki

The dummy_match_table.html file can be used as test input if a league table is needed.
The file tries to simulate multiple scenarios that can happen to a match.
The following matches are in the dummy table:

| Date                 | Home Team | Guest Team |   Score | Extras                                           | Valid |
|----------------------|-----------|------------|--------:|--------------------------------------------------|:-----:|
| 12.04.2025 19:00 (v) | Team1     | Team2      |     1:2 | Postponed match. Has extra char 'v' in date cell |   ✅   |
| Termin offen         | Team1     | Team3      |   &nbsp | Postponed to unknown date                        |   ❌   |
| 05.04.2025 19:00 (x) | Team1     | Team4      | img tag | One team did not compete                         |   ❌   |
| 14.02.2026 18:00     | Team1     | Team5      |   &nbsp | Normal game in the future                        |   ✅   |
