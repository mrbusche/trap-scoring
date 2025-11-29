package trap.report;

import trap.common.EventTypes;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.RoundTotal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Gatherer;

public class TrapService {
    protected static final Map<String, Integer> ROUND_COUNTS = determineRoundsToCount();
    private static final Map<String, Integer> EVENT_COUNTS = determineEventsToCount();

    private static Map<String, Integer> determineEventsToCount() {
        var eventCounts = new HashMap<String, Integer>();
        eventCounts.put(EventTypes.SINGLES, 4);
        eventCounts.put(EventTypes.DOUBLES, 4);
        eventCounts.put(EventTypes.HANDICAP, 4);
        eventCounts.put(EventTypes.SKEET, 4);
        eventCounts.put(EventTypes.CLAYS, 3);
        eventCounts.put(EventTypes.FIVESTAND, 4);
        eventCounts.put(EventTypes.DOUBLESKEET, 4);
        return eventCounts;
    }

    public static int getEventsToCount(String type) {
        return EVENT_COUNTS.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static String trimString(String s) {
        return s.trim();
    }

    private static Map<String, Integer> determineRoundsToCount() {
        var roundCounts = new HashMap<String, Integer>();
        roundCounts.put(EventTypes.SINGLES, 5);
        roundCounts.put(EventTypes.DOUBLES, 5);
        roundCounts.put(EventTypes.HANDICAP, 5);

        roundCounts.put(EventTypes.SKEET, 3);
        roundCounts.put(EventTypes.CLAYS, 3);
        roundCounts.put(EventTypes.FIVESTAND, 3);
        roundCounts.put(EventTypes.DOUBLESKEET, 3);
        return roundCounts;
    }

    public static int getRoundsToCount(String type) {
        return ROUND_COUNTS.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static int parseInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException _) {
            return 0;
        }
    }

    public static int setStringToZero(String number) {
        return number.isEmpty() ? 0 : parseInteger(number);
    }

    public Map<String, List<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {
        Map<String, List<RoundTotal>> playerRoundTotals = new HashMap<>();

        // Initialize map with empty lists for each unique player
        roundScores.forEach(r -> playerRoundTotals.put(r.uniqueName(), new ArrayList<>()));

        // Process each round score
        for (RoundScore r : roundScores) {
            List<RoundTotal> currentPlayerRoundTotal = playerRoundTotals.get(r.uniqueName());

            // If it's a single round, process round 1 and optionally round 2
            if (singleRound(r.type())) {
                addRound(currentPlayerRoundTotal, r, r.round1());
                if (r.round2() > 0) {
                    addRound(currentPlayerRoundTotal, r, r.round2());
                }
            } else {
                addRound(currentPlayerRoundTotal, r, r.round1() + r.round2());
                addMultipleRounds(currentPlayerRoundTotal, r);
            }
        }

        return playerRoundTotals;
    }

    // Helper method to add individual rounds
    private void addRound(List<RoundTotal> totals, RoundScore r, int total) {
        totals.add(new RoundTotal(r.eventId(), r.locationId(), r.team(), r.athlete(),
                r.classification(), r.gender(), total, r.type()));
    }

    // Helper method to add rounds 3 to 8 if applicable
    private void addMultipleRounds(List<RoundTotal> totals, RoundScore r) {
        int[] additionalRounds = {r.round3() + r.round4(), r.round5() + r.round6(), r.round7() + r.round8()};

        for (int round : additionalRounds) {
            if (round > 0) {
                addRound(totals, r, round);
            } else {
                break; // Stop processing once a round with zero total is found
            }
        }
    }

    public Map<String, List<IndividualTotal>> calculatePlayerIndividualTotal(List<RoundScore> roundScores, Map<String, List<RoundTotal>> playerRoundTotals) {
        Map<String, List<IndividualTotal>> playerIndividualTotal = new HashMap<>();
        roundScores.forEach(r -> playerIndividualTotal.put(r.uniqueName(), new ArrayList<>()));

        for (var entry : playerRoundTotals.entrySet()) {
            List<RoundTotal> rounds = entry.getValue();
            if (rounds.isEmpty()) continue;

            int roundsToCount = getEventsToCount(rounds.getFirst().type());

            List<IndividualTotal> selectedRounds = rounds.stream()
                    .sorted(Comparator.comparingInt(RoundTotal::total).reversed())
                    .gather(selectTopRoundsWithLocationConstraint(roundsToCount))
                    .map(this::toIndividualTotal)
                    .toList();

            playerIndividualTotal.put(entry.getKey(), selectedRounds);
        }

        return playerIndividualTotal;
    }

    // State class to track progress inside the Gatherer
    private static class ScoringState {
        int count = 0;
        final Set<Integer> locations = new HashSet<>();
    }

    private static Gatherer<RoundTotal, ScoringState, RoundTotal> selectTopRoundsWithLocationConstraint(int limit) {
        return Gatherer.ofSequential(
                // Initializer
                ScoringState::new,
                // Integrator
                (state, element, downstream) -> {
                    // 1. If we haven't reached the "final round" check (N-1), just add it.
                    if (state.count < limit - 1) {
                        state.count++;
                        state.locations.add(element.locationId());
                        downstream.push(element);
                        return true;
                    }
                    // 2. If we are looking for the final round (the Nth round)
                    else if (state.count == limit - 1) {
                        boolean accept = false;
                        int locationId = element.locationId();

                        if (!state.locations.contains(locationId)) {
                            state.locations.add(locationId);
                            accept = true;
                        } else if (state.locations.size() == 2 || state.locations.size() == 3) {
                            accept = true;
                        }

                        if (accept) {
                            state.count++;
                            downstream.push(element);
                            return false; // Stop processing, we found our limit
                        }
                        return true; // Skip this round, keep looking for a valid final round
                    }

                    return false; // Stop if we are over the limit
                }
        );
    }

    private IndividualTotal toIndividualTotal(RoundTotal t) {
        return new IndividualTotal(t.locationId(), t.team(), t.athlete(), t.classification(), t.gender(), t.total(), t.type());
    }

    public Map<String, IndividualTotal> calculatePlayerFinalTotal(Map<String, List<IndividualTotal>> playerIndividualTotals) {
        var playerFinalTotals = new HashMap<String, IndividualTotal>();
        playerIndividualTotals.forEach((key, totals) -> {
            if (!totals.isEmpty()) {
                var firstTotal = totals.getFirst();
                int totalScore = totals.stream().mapToInt(IndividualTotal::total).sum();

                playerFinalTotals.put(key, new IndividualTotal(
                        0,
                        firstTotal.team(),
                        firstTotal.athlete(),
                        firstTotal.classification(),
                        firstTotal.gender(),
                        totalScore,
                        firstTotal.type()
                ));
            }
        });

        return playerFinalTotals;
    }

    public boolean singleRound(String roundType) {
        Set<String> validRounds = Set.of(EventTypes.CLAYS, EventTypes.DOUBLES, EventTypes.DOUBLESKEET, EventTypes.FIVESTAND);
        return validRounds.contains(roundType);
    }
}