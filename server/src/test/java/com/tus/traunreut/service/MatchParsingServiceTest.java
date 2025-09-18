package com.tus.traunreut.service;

import com.tus.traunreut.AbstractIntegrationTest;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MatchParsingServiceTest extends AbstractIntegrationTest {
//    @Autowired
//    private MatchParsingService matchParsingService;
//
//    @Autowired
//    private LeagueRepository leagueRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    private League league;
//
//    @BeforeEach
//    void setUp() {
//        // Clean DB to be safe
//        teamRepository.deleteAll();
//        leagueRepository.deleteAll();
//
//        // Insert league
//        league = new League();
//        league.setName("Test League");
//        league.setSeason("25/26");
//        league.setLeaguePlanUrl("dummy url");
//        league = leagueRepository.save(league);
//
//        for (int i = 1; i <= 5; i++) {
//            Team team = new Team();
//            team.setName("Team" + i);
//            team.setLeague(league);
//            teamRepository.save(team);
//        }
//    }
//
//    @Test
//    void testParseTableData_allCombinations() throws IOException {
//        // Read test table from resources
//        String html = Files.readString(Paths.get("src/test/resources/html/dummy_match_table.html"));
//        Document doc = Jsoup.parse(html);
//        Elements rows = doc.select("table tr");
//        List<Element> listOfRows = rows.subList(0, rows.size());
//
//        // Parse test table to List of matches
//        List<Match> parsedMatches = matchParsingService.parseTableData(listOfRows, league);
//
//        // Check values. See dummy_match_table_docu.md for description of test data and expected behaviour
//        assertEquals(2, parsedMatches.size());
//
//        // Check parsed values from first match
//        Match firstMatch = parsedMatches.getFirst();
//        assertEquals("Team1", firstMatch.getHomeTeam().getName());
//        assertEquals("Team2", firstMatch.getGuestTeam().getName());
//        assertEquals(1, firstMatch.getHomeGoals());
//        assertEquals(2, firstMatch.getGuestGoals());
//        assertEquals(LocalDateTime.of(2025, 4, 12, 19, 0), firstMatch.getMatchDate());
//
//        // Check parsed values from second match
//        Match lastMatch = parsedMatches.getLast();
//        assertEquals("Team1", lastMatch.getHomeTeam().getName());
//        assertEquals("Team5", lastMatch.getGuestTeam().getName());
//        assertNull(lastMatch.getHomeGoals());
//        assertNull(lastMatch.getGuestGoals());
//        assertEquals(LocalDateTime.of(2026, 2, 14, 18, 0), lastMatch.getMatchDate());
//    }
}