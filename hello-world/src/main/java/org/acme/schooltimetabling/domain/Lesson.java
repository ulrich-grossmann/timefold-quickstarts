package org.acme.schooltimetabling.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import org.acme.schooltimetabling.solver.ResetWorkingSolutionListener;

@PlanningEntity
public class Lesson {

    @ShadowVariable(variableListenerClass = ResetWorkingSolutionListener.class,sourceVariableName= "timeslot")
     public Integer getFoo(){
        return 0;
    }

    @PlanningId
    private Long id;

    private String subject;
    private String teacher;
    private String studentGroup;

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    private Timeslot timeslot;

    private Room room;

    // No-arg constructor required for Timefold
    public Lesson() {
    }

    public Lesson(long id, String subject, String teacher, String studentGroup) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.studentGroup = studentGroup;
    }

    public Lesson(long id, String subject, String teacher, String studentGroup, Timeslot timeslot, Room room) {
        this(id, subject, teacher, studentGroup);
        this.timeslot = timeslot;
        this.room = room;
    }

    @Override
    public String toString() {
        return subject + "(" + id + ")";
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public Long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    @PlanningVariable
    public Timeslot getTimeslot() {
        return timeslot;
    }

    @PlanningVariable
    public Room getRoom() {
        return room;
    }
}
