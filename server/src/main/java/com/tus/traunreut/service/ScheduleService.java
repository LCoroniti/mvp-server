package com.tus.traunreut.service;

import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.service.schedule.TaskScheduler;

import java.util.List;

public class ScheduleService {


    public List<ScheduledTask> getAllTasks(){
        // get all tasks from repo
        // read task id and cast to correct object. Switch over ETaskIds
    }

    public void scheduleTask(ScheduledTask taskToSchedule){
        TaskScheduler.getInstance().scheduleTask(...);
    }

    //    @EventListener(ApplicationReadyEvent.class)
//    public void onStartup() {
//        LOGGER.info("Updating match information for all leagues...");
//        updateAllMatches();
//        LOGGER.info("Finished match updates!");
//        LOGGER.info("Scheduling tasks for each match...");
//        scheduleAllMatchTasks();
//        LOGGER.info("On Startup routine finished!");
//    }

    /**
     * For each match that is in the future, the following tasks will be scheduled for execution:
     * - At match start: Retrieve match id and afterward all MatchPlayers for the match
     * - 2 hours after match start: Retrieve the match result
     */
//    public void scheduleAllMatchTasks() {
//        List<Match> upcomingMatches = matchRepository.findByMatchDateGreaterThanEqual(DateTimeUtil.nowGerman());
//
//        for (Match upcomingMatch : upcomingMatches) {
//            // Scrapes match id and afterward the players for the match
//            TaskScheduler.getInstance().scheduleTask(new MatchTask(upcomingMatch, EMatchTasks.GET_PLAYERS, () -> {
//                scraperService.scrapeMatchId(upcomingMatch.getHomeTeam().getLeague(), upcomingMatch);
//                List<MatchPlayer> matchPlayers = scraperService.getMatchPlayers(upcomingMatch);
//                matchPlayerRepository.saveAll(matchPlayers);
//                matchRepository.save(upcomingMatch);
//            }), upcomingMatch.getMatchDate());
//            // Scrape the match result
//            TaskScheduler.getInstance().scheduleTask(new MatchTask(upcomingMatch, EMatchTasks.GET_RESULT, () -> {
//                try {
//                    Match updated = scraperService.scrapeMatch(upcomingMatch);
//                    matchRepository.save(updated);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }), upcomingMatch.getMatchDate().plusHours(2));
//        }
//    }
}
