package trap.model;

import org.junit.jupiter.api.Test;
import trap.common.Classifications;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndividualTotalTest {

    @Test
    void teamClassificationMapsSupportedLabelsAndPreservesUnknownLabels() {
        assertEquals(Classifications.VARSITY, individual("Senior/Varsity").teamClassification());
        assertEquals(Classifications.JUNIOR_VARSITY, individual("Senior/Jr. Varsity").teamClassification());
        assertEquals(Classifications.JUNIOR_VARSITY, individual(Classifications.JUNIOR_VARSITY).teamClassification());
        assertEquals(Classifications.INTERMEDIATE_ADVANCED, individual("Intermediate/Advanced").teamClassification());
        assertEquals(Classifications.INTERMEDIATE_ENTRY, individual("Intermediate/Entry Level").teamClassification());
        assertEquals("Custom", individual("Custom").teamClassification());
    }

    @Test
    void teamClassificationForTotalGroupsSupportedClassifications() {
        assertEquals(Classifications.VARSITY, individual("Senior/Varsity").teamClassificationForTotal());
        assertEquals(Classifications.VARSITY, individual("Senior/Jr. Varsity").teamClassificationForTotal());
        assertEquals(Classifications.INTERMEDIATE_ENTRY, individual("Intermediate/Advanced").teamClassificationForTotal());
        assertEquals(Classifications.INTERMEDIATE_ENTRY, individual("Intermediate/Entry Level").teamClassificationForTotal());
        assertEquals(Classifications.ROOKIE, individual(Classifications.ROOKIE).teamClassificationForTotal());
    }

    private IndividualTotal individual(String classification) {
        return new IndividualTotal(1, "Team", "Athlete", classification, "M", 25, "singles");
    }
}
