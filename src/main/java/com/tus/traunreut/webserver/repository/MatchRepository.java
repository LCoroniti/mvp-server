package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    /**
     * Find the next match that is in the future. Returns an empty Optional if no match is found.
     */
    Optional<Match> findTopByMatchDateAfterOrderByMatchDateAsc(LocalDateTime dateTime);

    @Query("SELECT m FROM Match m WHERE m.matchDate >= :startOfWeekend AND m.matchDate <= :endOfWeekend ORDER BY m.matchDate ASC")
    List<Match> findMatchesInTimeRange(
            @Param("startOfWeekend") LocalDateTime startOfWeekend,
            @Param("endOfWeekend") LocalDateTime endOfWeekend
    );

    Page<Match> findByMatchDateBeforeOrderByMatchDateDesc(LocalDateTime date, Pageable pageable);

    List<Match> findByMatchDateGreaterThanEqual(LocalDateTime timestamp);

    Page<Match> findByMatchDateBeforeAndHomeTeam_League_NameOrderByMatchDateDesc(
            LocalDateTime date, String leagueName, Pageable pageable);

    Optional<Match> findByHomeTeamAndGuestTeam(Team homeTeam, Team guestTeam);
}

