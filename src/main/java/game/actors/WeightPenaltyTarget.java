package game.actors;

public interface WeightPenaltyTarget {
    int applyWeightPenalty(int reduced);
    void removeWeightPenalty(int original);
}