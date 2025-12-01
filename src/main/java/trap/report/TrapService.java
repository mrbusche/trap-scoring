package trap.report;

import trap.common.EventTypes;
import trap.model.IndividualTotal;
import trap.model.RoundTotal;
import trap.model.TrapRoundScore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;

import static java.util.Map.entry;

public class TrapService {

    private static final Map<String, Integer> EVENT_COUNTS = Map.ofEntries(
            entry(EventTypes.SINGLES, 5),
            entry(EventTypes.DOUBLES, 5),
            entry(EventTypes.HANDICAP, 5),
            entry(EventTypes.SKEET, 5),
            entry(EventTypes.CLAYS, 4),
            entry(EventTypes.FIVESTAND, 5),
            entry(EventTypes.DOUBLESKEET, 5)
    );

    private static final Map<String, Integer> ROUND_COUNTS = Map.ofEntries(
            entry(EventTypes.SINGLES, 5),
            entry(EventTypes.DOUBLES, 5),
            entry(EventTypes.HANDICAP, 5),
            entry(EventTypes.SKEET, 3),
            entry(EventTypes.CLAYS, 3),
            entry(EventTypes.FIVESTAND, 3),
            entry(EventTypes.DOUBLESKEET, 3)
    );

    private static final Set<String> SINGLE_ROUND_TYPES = Set.of(
            EventTypes.CLAYS, EventTypes.DOUBLES, EventTypes.DOUBLESKEET, EventTypes.FIVESTAND
    );

    private static final int MIN_UNIQUE_LOCATIONS = 3;

    public static int getEventsToCount(String type) {
        return EVENT_COUNTS.getOrDefault(type, 0);
    }

    public static int getRoundsToCount(String type) {
        return ROUND_COUNTS.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static String trimString(String s) {
        return s == null ? "" : s.trim();
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

    public Map<String, List<RoundTotal>> calculatePlayerRoundTotals(List<TrapRoundScore> roundScores) {
        return roundScores.stream().gather(expandRounds()).collect(Collectors.groupingBy(RoundTotal::uniqueName));
    }

    public static Gatherer<TrapRoundScore, ?, RoundTotal> expandRounds() {
        return Gatherer.ofSequential((_, element, downstream) -> {
            boolean isSingle = isSingleRound(element.type());

            if (isSingle) {
                downstream.push(createRoundTotal(element, element.round1()));
                if (element.round2() > 0) {
                    downstream.push(createRoundTotal(element, element.round2()));
                }
            } else {
                // Combined Round 1+2
                downstream.push(createRoundTotal(element, element.round1() + element.round2()));

                // Pairs
                int[] pairs = {
                        element.round3() + element.round4(),
                        element.round5() + element.round6(),
                        element.round7() + element.round8()
                };
                for (int score : pairs) {
                    if (score <= 0) {
                        break;
                    }
                    downstream.push(createRoundTotal(element, score));
                }
            }
            return true;
        });
    }

    private static RoundTotal createRoundTotal(TrapRoundScore r, int total) {
        return new RoundTotal(r.eventId(), r.locationId(), r.team(), r.athlete(), r.classification(), r.gender(), total, r.type());
    }

    public Map<String, List<IndividualTotal>> calculatePlayerIndividualTotal(List<TrapRoundScore> roundScores, Map<String, List<RoundTotal>> playerRoundTotals) {
        Map<String, List<IndividualTotal>> result = roundScores.stream().map(TrapRoundScore::uniqueName).distinct().collect(Collectors.toMap(name -> name, _ -> new ArrayList<>()));

        playerRoundTotals.forEach((player, totals) -> {
            if (!totals.isEmpty()) {
                result.put(player, selectBestScoresForPlayer(totals));
            }
        });

        return result;
    }

    private List<IndividualTotal> selectBestScoresForPlayer(List<RoundTotal> totals) {
        var first = totals.getFirst();
        int roundsToCount = getEventsToCount(first.type());

        // Sort all available rounds by score descending
        var sortedRounds = new ArrayList<>(totals);
        sortedRounds.sort(Comparator.comparingInt(RoundTotal::total).reversed());

        List<IndividualTotal> finalList = new ArrayList<>();
        List<IndividualTotal> remainderList = new ArrayList<>();
        Set<Integer> usedLocations = new HashSet<>();

        // Pass 1: Identify high scores from unique locations
        for (var round : sortedRounds) {
            // If we haven't hit the location cap and this is a new location
            if (usedLocations.size() < MIN_UNIQUE_LOCATIONS && !usedLocations.contains(round.locationId())) {
                finalList.add(toIndividualTotal(round));
                usedLocations.add(round.locationId());
            } else {
                remainderList.add(toIndividualTotal(round));
            }
        }

        int uniqueFound = usedLocations.size();
        int targetSize = (uniqueFound >= MIN_UNIQUE_LOCATIONS) ? roundsToCount : roundsToCount - (MIN_UNIQUE_LOCATIONS - uniqueFound);

        // Pass 2: Fill remaining slots from the highest scoring remainders
        int needed = targetSize - finalList.size();
        if (needed > 0) {
            finalList.addAll(remainderList.stream().limit(needed).toList());
        }

        // Final sort of the result list
        finalList.sort(Comparator.comparingInt(IndividualTotal::total).reversed());
        return finalList;
    }

    public Map<String, IndividualTotal> calculatePlayerFinalTotal(Map<String, List<IndividualTotal>> playerIndividualTotals) {
        return playerIndividualTotals.entrySet().stream().filter(e -> !e.getValue().isEmpty()).collect(Collectors.toMap(Map.Entry::getKey, e -> {
            var list = e.getValue();
            var first = list.getFirst(); // Java 21
            int sum = list.stream().mapToInt(IndividualTotal::total).sum();

            return new IndividualTotal(0, first.team(), first.athlete(), first.classification(), first.gender(), sum, first.type());
        }));
    }

    private IndividualTotal toIndividualTotal(RoundTotal t) {
        return new IndividualTotal(t.locationId(), t.team(), t.athlete(), t.classification(), t.gender(), t.total(), t.type());
    }

    public static boolean isSingleRound(String roundType) {
        return SINGLE_ROUND_TYPES.contains(roundType);
    }
}
