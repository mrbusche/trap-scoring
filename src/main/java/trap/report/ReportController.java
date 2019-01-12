package trap.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import trap.enums.ClassificationsEnum;
import trap.enums.GendersEnum;
import trap.model.DoublesAggregate;
import trap.model.DoublesTeamAggregate;
import trap.model.HandicapAggregate;
import trap.model.HandicapTeamAggregate;
import trap.model.SinglesAggregate;
import trap.model.SinglesTeamAggregate;
import trap.repository.DoublesDataRepository;
import trap.repository.DoublesDataTeamRepository;
import trap.repository.HandicapDataRepository;
import trap.repository.HandicapDataTeamRepository;
import trap.repository.SinglesDataRepository;
import trap.repository.SinglesDataTeamRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final SinglesDataRepository singlesRepository;
    private final SinglesDataTeamRepository singlesDataTeamRepository;
    private final DoublesDataRepository doublesDataRepository;
    private final DoublesDataTeamRepository doublesDataTeamRepository;
    private final HandicapDataRepository handicapDataRepository;
    private final HandicapDataTeamRepository handicapDataTeamRepository;
    private final List<String> classificationOptions = Stream.of(ClassificationsEnum.values()).map(ClassificationsEnum::name).collect(Collectors.toList());

    @Autowired
    public ReportController(SinglesDataRepository singlesRepository, SinglesDataTeamRepository singlesDataTeamRepository, DoublesDataRepository doublesDataRepository, DoublesDataTeamRepository doublesDataTeamRepository, HandicapDataRepository handicapDataRepository, HandicapDataTeamRepository handicapDataTeamRepository) {
        this.singlesRepository = singlesRepository;
        this.singlesDataTeamRepository = singlesDataTeamRepository;
        this.doublesDataRepository = doublesDataRepository;
        this.doublesDataTeamRepository = doublesDataTeamRepository;
        this.handicapDataRepository = handicapDataRepository;
        this.handicapDataTeamRepository = handicapDataTeamRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/individual/{gender}")
    public String individualReports(@PathVariable(name = "gender") String gender) {
        StringBuilder results = new StringBuilder();
        results.append("<h1>Singles</h1>");
        for (String classification : classificationOptions) {
            List<SinglesAggregate> data = singlesRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinSinglesData(data, results);
        }

        results.append("<h1>Doubles</h1>");
        for (String classification : classificationOptions) {
            List<DoublesAggregate> data = doublesDataRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinDoublesData(data, results);
        }

        results.append("<h1>Handicap</h1>");
        for (String classification : classificationOptions) {
            List<HandicapAggregate> data = handicapDataRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinHandicapData(data, results);
        }

        return results.toString();
    }

    private static void joinSinglesData(List<SinglesAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SinglesAggregate::toString).collect(Collectors.joining("<br>")));
    }

    private static void joinDoublesData(List<DoublesAggregate> data, StringBuilder results) {
        results.append(data.stream().map(DoublesAggregate::toString).collect(Collectors.joining("<br>")));
    }

    private static void joinHandicapData(List<HandicapAggregate> data, StringBuilder results) {
        results.append(data.stream().map(HandicapAggregate::toString).collect(Collectors.joining("<br>")));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/team/{gender}")
    public String teamReports(@PathVariable(name = "gender") String gender) {
        StringBuilder results = new StringBuilder();
        results.append("<h1>Singles</h1>");
        for (String classification : classificationOptions) {
            List<SinglesTeamAggregate> data = singlesDataTeamRepository.getAllByClassificationAndGenderOrderByTotalDesc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinTeamData(data, results);
        }

        results.append("<h1>Doubles</h1>");
        for (String classification : classificationOptions) {
            List<DoublesTeamAggregate> data = doublesDataTeamRepository.getAllByClassificationAndGenderOrderByTotalDesc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinDoublesTeamData(data, results);
        }

        results.append("<h1>Handicap</h1>");
        for (String classification : classificationOptions) {
            List<HandicapTeamAggregate> data = handicapDataTeamRepository.getAllByClassificationAndGenderOrderByTotalDesc(ClassificationsEnum.valueOf(classification).value, GendersEnum.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(ClassificationsEnum.valueOf(classification)).append("</h2>");
            joinHandicapTeamData(data, results);
        }

        return results.toString();
    }

    private static void joinTeamData(List<SinglesTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SinglesTeamAggregate::toString).collect(Collectors.joining("<br>")));
    }

    private static void joinDoublesTeamData(List<DoublesTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(DoublesTeamAggregate::toString).collect(Collectors.joining("<br>")));
    }

    private static void joinHandicapTeamData(List<HandicapTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(HandicapTeamAggregate::toString).collect(Collectors.joining("<br>")));
    }

}
