package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timetable;

@PlanningEntity
public class ResetWorkingSolutionListener implements VariableListener<Timetable, Lesson> {

  @Override
  public boolean requiresUniqueEntityEvents() {
    return true;
  }

  @Override
  public void beforeVariableChanged(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void afterVariableChanged(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void beforeEntityAdded(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void afterEntityAdded(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void beforeEntityRemoved(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void afterEntityRemoved(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
  }

  @Override
  public void resetWorkingSolution(ScoreDirector<Timetable> scoreDirector) {
    //evil part which maybe requires singleton
    for (var value : JavaIncrementalCalculator.values())
      value.clear();
  }
}
