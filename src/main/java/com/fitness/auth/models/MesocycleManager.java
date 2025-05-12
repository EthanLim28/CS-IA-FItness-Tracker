package com.fitness.auth.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MesocycleManager {
    private final int userId;
    private final DailyWorkoutDAO workoutDAO;
    private static final int MESOCYCLE_LENGTH_WEEKS = 4;
    private static final double DELOAD_THRESHOLD = 0.8; // 80% of max weight
    private int durationWeeks;
    private LocalDate startDate;
    private boolean autoDeload;
    private List<MesocycleProgress> progressList;

    public MesocycleManager(Connection connection, int userId) {
        this.userId = userId;
        this.workoutDAO = new DailyWorkoutDAO(connection);
        this.durationWeeks = MESOCYCLE_LENGTH_WEEKS;
        this.startDate = LocalDate.now();
        this.autoDeload = true;
        this.progressList = new ArrayList<>();
        initializeProgress();
    }

    public MesocycleManager(int durationWeeks, LocalDate startDate, boolean autoDeload) {
        this.userId = -1; // Temporary user ID for DTO purposes
        this.workoutDAO = null; // No DAO needed for DTO
        this.durationWeeks = durationWeeks;
        this.startDate = startDate;
        this.autoDeload = autoDeload;
        this.progressList = new ArrayList<>();
        initializeProgress();
    }

    public void updateSettings(Integer durationWeeks, LocalDate startDate, boolean autoDeload) {
        this.durationWeeks = durationWeeks;
        this.startDate = startDate;
        this.autoDeload = autoDeload;
        initializeProgress();
    }

    private void initializeProgress() {
        progressList.clear();
        for (int week = 1; week <= durationWeeks; week++) {
            progressList.add(new MesocycleProgress(userId, startDate.plusWeeks(week-1), startDate.plusWeeks(week), new ArrayList<>(), week, "Planned", false, ""));
        }
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isAutoDeload() {
        return autoDeload;
    }

    public List<MesocycleProgress> getProgressList() {
        return new ArrayList<>(progressList);
    }

    public void updateWeekProgress(int week, String status, boolean deload, String notes) {
        if (week > 0 && week <= progressList.size()) {
            MesocycleProgress progress = progressList.get(week - 1);
            progress.setStatus(status);
            progress.setDeload(deload);
            progress.setNotes(notes);
        }
    }

    public MesocycleProgress startNewMesocycle() throws SQLException {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);
        List<DailyWorkout> workouts = workoutDAO.getWorkoutsForDateRange(userId, startDate, endDate);
        return new MesocycleProgress(userId, startDate, endDate, workouts, 1, "In Progress", false, "");
    }

    public void checkAndAdjustProgress(LocalDate date, MesocycleProgress mesocycle) throws SQLException {
        if (mesocycle != null) {
            adjustIntensity(mesocycle);
        }
    }

    public void adjustIntensity(MesocycleProgress progress) throws SQLException {
        if (progress.isDeloadWeekRecommended()) {
            deloadExercises(progress.getExercisesNeedingDeload());
        } else {
            progressExercises(progress.getExercisesReadyForProgression());
        }
    }

    private void deloadExercises(List<Exercise> exercises) throws SQLException {
        List<DailyWorkout> recentWorkouts = workoutDAO.getWorkoutHistory(userId);
        
        for (Exercise exercise : exercises) {
            DailyWorkout latestWorkout = findLatestWorkoutWithExercise(recentWorkouts, exercise);
            if (latestWorkout != null) {
                // Reduce weight by 20% for all sets of this exercise
                for (WorkoutSet set : latestWorkout.getSets()) {
                    if (set.getExercise().equals(exercise)) {
                        set.setWeight(set.getWeight() * DELOAD_THRESHOLD);
                    }
                }
                workoutDAO.saveDailyWorkout(latestWorkout);
            }
        }
    }

    private void progressExercises(List<Exercise> exercises) throws SQLException {
        List<DailyWorkout> recentWorkouts = workoutDAO.getWorkoutHistory(userId);
        
        for (Exercise exercise : exercises) {
            DailyWorkout latestWorkout = findLatestWorkoutWithExercise(recentWorkouts, exercise);
            if (latestWorkout != null) {
                // Increase weight by 2.5-5kg based on current weight
                for (WorkoutSet set : latestWorkout.getSets()) {
                    if (set.getExercise().equals(exercise)) {
                        double increment = set.getWeight() < 20.0 ? 2.5 : 5.0;
                        set.setWeight(set.getWeight() + increment);
                    }
                }
                workoutDAO.saveDailyWorkout(latestWorkout);
            }
        }
    }

    private DailyWorkout findLatestWorkoutWithExercise(List<DailyWorkout> workouts, Exercise exercise) {
        return workouts.stream()
            .filter(w -> w.getSets().stream()
                .anyMatch(s -> s.getExercise().equals(exercise)))
            .findFirst()
            .orElse(null);
    }

    public List<WorkoutSet> suggestNextWorkout(Exercise exercise) throws SQLException {
        List<DailyWorkout> workouts = workoutDAO.getWorkoutHistory(userId);
        DailyWorkout latestWorkout = findLatestWorkoutWithExercise(workouts, exercise);
        
        if (latestWorkout != null) {
            return latestWorkout.getSets().stream()
                .filter(s -> s.getExercise().equals(exercise))
                .map(s -> new WorkoutSet(
                    exercise,
                    s.getSetNumber(),
                    s.getReps(),
                    s.getWeight(),
                    "Suggested weight based on previous workout"
                ))
                .toList();
        } else {
            // Default workout for new exercise
            return List.of(
                new WorkoutSet(exercise, 1, 12, 20.0, "Warmup set"),
                new WorkoutSet(exercise, 2, 10, 25.0, "Working set 1"),
                new WorkoutSet(exercise, 3, 8, 30.0, "Working set 2")
            );
        }
    }
}
