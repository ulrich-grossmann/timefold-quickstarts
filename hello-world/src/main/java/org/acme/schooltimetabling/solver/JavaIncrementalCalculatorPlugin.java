package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timetable;

@PlanningEntity
public class JavaIncrementalCalculatorPlugin implements VariableListener<Timetable, Lesson> {

  @Override
  public boolean requiresUniqueEntityEvents() {
    return true;
  }

  @Override
  public void beforeVariableChanged(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    for(var incrementalCalculator:JavaIncrementalCalculatorUsingGlobalState.values())
      incrementalCalculator.recalculateBeforeVariableChange(lesson);
   }

  @Override
  public void afterVariableChanged(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    for(var incrementalCalculator:JavaIncrementalCalculatorUsingGlobalState.values())
      incrementalCalculator.recalculateAfterVariableChange(lesson);
  }

  @Override
  public void beforeEntityAdded(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    System.out.println("before added");
  }

  @Override
  public void afterEntityAdded(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    System.out.println("after added");
  }

  @Override
  public void beforeEntityRemoved(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    System.out.println("before removed");
  }

  @Override
  public void afterEntityRemoved(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
    System.out.println("after removed");
  }

  @Override
  public void resetWorkingSolution(ScoreDirector<Timetable> scoreDirector) {
//    for(var incrementalCalculator:JavaIncrementalCalculatorUsingGlobalState.values())
//      incrementalCalculator.clear();
//    for(var lesson:scoreDirector.getWorkingSolution().getLessons())
//      afterVariableChanged(scoreDirector,lesson);
  }
}
