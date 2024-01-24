package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import org.acme.schooltimetabling.TimetableApp;
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
//    for(var incrementalCalculator:JavaIncrementalCalculator.values())
//      incrementalCalculator.recalculateBeforeVariableChange(lesson,count>1);
  }

  @Override
  public void afterVariableChanged(ScoreDirector<Timetable> scoreDirector, Lesson lesson) {
//    for(var incrementalCalculator:JavaIncrementalCalculator.values())
//      incrementalCalculator.recalculateAfterVariableChange(lesson,count>1);
    //TimetableApp.printTimetable(scoreDirector.getWorkingSolution());
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

  int count =0;
  @Override
  public void resetWorkingSolution(ScoreDirector<Timetable> scoreDirector) {
    EvaluateChangesUniCostraintCollector.SINGLETON.timetable = scoreDirector.getWorkingSolution();
    System.out.println("reset working solution");
    TimetableApp.printTimetable(scoreDirector.getWorkingSolution());
    if(count>0)
      for(var value:JavaIncrementalCalculator.values())
        value.clear();
//    try {
//      Thread.sleep(3000);
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
    count ++;
//    for(var incrementalCalculator:JavaIncrementalCalculatorUsingGlobalState.values())
//      incrementalCalculator.clear();
//    for(var lesson:scoreDirector.getWorkingSolution().getLessons())
//      afterVariableChanged(scoreDirector,lesson);
  }
}
