package org.acme.schooltimetabling.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Lesson {

    @PlanningId
    private Long id;

    private String subject;
    private String teacher;
    private String studentGroup;

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

    // We have to annotate planning variables on the getter otherwise
    // timefold will not use our changed setter.
    @PlanningVariable
    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        IncrementalCountingConstraint.TEACHER_CONFLICT.beforeVariableChange(this);
        IncrementalCountingConstraint.ROOM_CONFLICT.beforeVariableChange(this);
        IncrementalCountingConstraint.STUDENT_GROUP_CONFLICT.beforeVariableChange(this);
        this.timeslot = timeslot;
        IncrementalCountingConstraint.TEACHER_CONFLICT.afterVariableChange(this);
        IncrementalCountingConstraint.ROOM_CONFLICT.afterVariableChange(this);
        IncrementalCountingConstraint.STUDENT_GROUP_CONFLICT.afterVariableChange(this);
    }

    // We have to annotate planning variables on the getter otherwise
    // timefold will not use our changed setter.
    @PlanningVariable
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        IncrementalCountingConstraint.ROOM_CONFLICT.beforeVariableChange(this);
        this.room = room;
        IncrementalCountingConstraint.ROOM_CONFLICT.afterVariableChange(this);
    }

}
