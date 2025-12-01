package trap.model;

import lombok.With;

public record TeamScore(String name, @With int total) {
}
