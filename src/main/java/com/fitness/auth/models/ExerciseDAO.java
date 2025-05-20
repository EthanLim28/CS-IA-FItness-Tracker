package com.fitness.auth.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.fitness.auth.models.Exercise;

public class ExerciseDAO {
    /**
     * Returns a list of all unique muscle groups in the exercises table.
     */
    public List<String> getAllMuscleGroups() throws SQLException {
        List<String> muscleGroups = new ArrayList<>();
        String sql = "SELECT DISTINCT muscle_group FROM exercises ORDER BY muscle_group ASC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                muscleGroups.add(rs.getString("muscle_group"));
            }
        }
        return muscleGroups;
    }
    private final Connection connection;

    public ExerciseDAO(Connection connection) {
        this.connection = connection;
        try {
            clearAndReseedAllExercises();
        } catch (Exception e) {
            System.err.println("Error reseeding default exercises: " + e.getMessage());
        }
    }

    /**
     * Deletes all exercises and re-inserts all default exercises.
     */
    public void clearAndReseedAllExercises() {
        try {
            String sql = "DELETE FROM exercises";
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
            }
            seedDefaultExercises();
            System.out.println("[DEBUG] All exercises cleared and default exercises re-seeded.");
        } catch (Exception e) {
            System.err.println("[DEBUG] Error reseeding exercises: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Seeds all default exercises into the database.
     */
    private void seedDefaultExercises() throws SQLException {
        try {
        // Chest
        addExercise(new Exercise(0, "Bench Press", "Classic barbell movement for chest strength.", "Chest", "Barbell"));
        addExercise(new Exercise(0, "Incline Dumbbell Press", "Targets upper chest with dumbbells.", "Chest", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Decline Push-Up", "Push-up with feet elevated to hit lower chest.", "Chest", "Bodyweight"));
        addExercise(new Exercise(0, "Cable Crossover", "Cable movement for chest contraction.", "Chest", "Cable machine"));
        addExercise(new Exercise(0, "Dumbbell Fly", "Chest isolation with dumbbells.", "Chest", "Dumbbells"));
        addExercise(new Exercise(0, "Chest Dip", "Bodyweight dip focusing on chest.", "Chest", "Parallel bars"));
        addExercise(new Exercise(0, "Pec Deck Fly", "Machine fly for chest isolation.", "Chest", "Pec deck machine"));
        addExercise(new Exercise(0, "Push-Up", "Bodyweight chest exercise.", "Chest", "Bodyweight"));
        addExercise(new Exercise(0, "Incline Push-Up", "Push-up variation for upper chest.", "Chest", "Bodyweight"));
        addExercise(new Exercise(0, "Svend Press", "Plate press for chest activation.", "Chest", "Weight plate"));

        // Front Delts
        addExercise(new Exercise(0, "Front Dumbbell Raise", "Lifts dumbbells to shoulder height.", "Front Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Barbell Overhead Press", "Standing press for shoulders.", "Front Delts", "Barbell"));
        addExercise(new Exercise(0, "Seated Arnold Press", "Rotational dumbbell press.", "Front Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Landmine Press", "Angled barbell press.", "Front Delts", "Barbell, landmine attachment"));
        addExercise(new Exercise(0, "Dumbbell Shoulder Press", "Classic dumbbell overhead press.", "Front Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Smith Machine Press", "Guided overhead press.", "Front Delts", "Smith machine"));
        addExercise(new Exercise(0, "Plate Front Raise", "Front raise using a plate.", "Front Delts", "Weight plate"));
        addExercise(new Exercise(0, "Z-Press", "Seated barbell press on the floor.", "Front Delts", "Barbell"));
        addExercise(new Exercise(0, "Cable Front Raise", "Cable machine front raise.", "Front Delts", "Cable machine"));
        addExercise(new Exercise(0, "Single-Arm Landmine Press", "One-arm landmine press.", "Front Delts", "Barbell, landmine attachment"));

        // Side Delts
        addExercise(new Exercise(0, "Dumbbell Lateral Raise", "Raises arms to sides.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Cable Lateral Raise", "Side raise using cable.", "Side Delts", "Cable machine"));
        addExercise(new Exercise(0, "Machine Lateral Raise", "Lateral raise on machine.", "Side Delts", "Lateral raise machine"));
        addExercise(new Exercise(0, "Seated Lateral Raise", "Lateral raise while seated.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Leaning Lateral Raise", "Lean away for more tension.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Upright Row", "Barbell row to chest.", "Side Delts", "Barbell"));
        addExercise(new Exercise(0, "Cable Upright Row", "Cable version of upright row.", "Side Delts", "Cable machine"));
        addExercise(new Exercise(0, "Incline Lateral Raise", "Performed on incline bench.", "Side Delts", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Standing Lateral Raise", "Classic standing side raise.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Resistance Band Lateral Raise", "Side raise with band.", "Side Delts", "Resistance band"));

        // Rear Delts
        addExercise(new Exercise(0, "Rear Delt Fly", "Reverse fly for rear delts.", "Rear Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Face Pull", "Cable pull to face.", "Rear Delts", "Cable machine, rope"));
        addExercise(new Exercise(0, "Reverse Pec Deck", "Machine reverse fly.", "Rear Delts", "Pec deck machine"));
        addExercise(new Exercise(0, "Bent-Over Rear Delt Raise", "Bent-over dumbbell raise.", "Rear Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Rear Delt Row", "Row with elbows out.", "Rear Delts", "Barbell"));
        addExercise(new Exercise(0, "Cable Rear Delt Pull", "Cable pull for rear delts.", "Rear Delts", "Cable machine"));
        addExercise(new Exercise(0, "Band Pull-Apart", "Band stretch for rear delts.", "Rear Delts", "Resistance band"));
        addExercise(new Exercise(0, "Prone Rear Delt Raise", "Lying dumbbell fly.", "Rear Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Reverse Cable Crossover", "Crossover for rear delts.", "Rear Delts", "Cable machine"));
        addExercise(new Exercise(0, "Chest Supported Rear Delt Row", "Row on incline bench.", "Rear Delts", "Dumbbells, incline bench"));

        // Upper Back
        addExercise(new Exercise(0, "Barbell Bent-Over Row", "Classic barbell row.", "Upper Back", "Barbell"));
        addExercise(new Exercise(0, "T-Bar Row", "Row with T-bar.", "Upper Back", "T-bar"));
        addExercise(new Exercise(0, "Seated Cable Row", "Cable machine row.", "Upper Back", "Cable row machine"));
        addExercise(new Exercise(0, "Inverted Row", "Bodyweight row under bar.", "Upper Back", "Bar"));
        addExercise(new Exercise(0, "Dumbbell Shrug", "Shoulder shrug for traps.", "Upper Back", "Dumbbells"));
        addExercise(new Exercise(0, "Barbell Shrug", "Barbell shrug for traps.", "Upper Back", "Barbell"));
        addExercise(new Exercise(0, "Face Pull (Upper Back)", "Cable pull to face.", "Upper Back", "Cable machine, rope"));
        addExercise(new Exercise(0, "Reverse Cable Row", "Row with wide grip.", "Upper Back", "Cable machine"));
        addExercise(new Exercise(0, "Machine High Row", "High row machine exercise.", "Upper Back", "High row machine"));
        addExercise(new Exercise(0, "Incline Prone Y Raise", "Y raise on incline bench.", "Upper Back", "Dumbbells, incline bench"));

        // Lats
        addExercise(new Exercise(0, "Pull-Up", "Classic vertical pull.", "Lats", "Pull-up bar"));
        addExercise(new Exercise(0, "Lat Pulldown", "Machine pulldown for lats.", "Lats", "Lat pulldown machine"));
        addExercise(new Exercise(0, "Chin-Up", "Supinated grip pull-up.", "Lats", "Pull-up bar"));
        addExercise(new Exercise(0, "Straight-Arm Pulldown", "Cable straight arm pull.", "Lats", "Cable machine"));
        addExercise(new Exercise(0, "Single-Arm Dumbbell Row", "Row with one arm.", "Lats", "Dumbbell, bench"));
        addExercise(new Exercise(0, "Seal Row", "Chest supported barbell row.", "Lats", "Barbell, bench"));
        addExercise(new Exercise(0, "Meadows Row", "Landmine row variation.", "Lats", "Barbell, landmine attachment"));
        addExercise(new Exercise(0, "Machine Row", "Seated machine row.", "Lats", "Row machine"));
        addExercise(new Exercise(0, "Kroc Row", "Heavy dumbbell row.", "Lats", "Dumbbell, bench"));
        addExercise(new Exercise(0, "Wide-Grip Lat Pulldown", "Wide grip for upper lats.", "Lats", "Lat pulldown machine"));

        // Triceps
        addExercise(new Exercise(0, "Close-Grip Bench Press", "Bench press with narrow grip.", "Triceps", "Barbell"));
        addExercise(new Exercise(0, "Triceps Rope Pushdown", "Cable pushdown with rope.", "Triceps", "Cable machine, rope"));
        addExercise(new Exercise(0, "Overhead Dumbbell Extension", "Two-hand overhead extension.", "Triceps", "Dumbbell"));
        addExercise(new Exercise(0, "Skullcrusher", "Lying triceps extension.", "Triceps", "EZ bar"));
        addExercise(new Exercise(0, "Bench Dip", "Dip using a bench.", "Triceps", "Bench, bodyweight"));
        addExercise(new Exercise(0, "Cable Overhead Extension", "Overhead cable extension.", "Triceps", "Cable machine, rope"));
        addExercise(new Exercise(0, "Triceps Kickback", "Dumbbell kickback.", "Triceps", "Dumbbells"));
        addExercise(new Exercise(0, "EZ Bar Overhead Extension", "Overhead EZ bar extension.", "Triceps", "EZ bar"));
        addExercise(new Exercise(0, "Diamond Push-Up", "Push-up with hands together.", "Triceps", "Bodyweight"));
        addExercise(new Exercise(0, "Machine Triceps Press", "Seated machine press.", "Triceps", "Triceps press machine"));

        // Biceps
        addExercise(new Exercise(0, "Barbell Curl", "Classic biceps curl.", "Biceps", "Barbell"));
        addExercise(new Exercise(0, "Dumbbell Curl", "Alternating curls with dumbbells.", "Biceps", "Dumbbells"));
        addExercise(new Exercise(0, "EZ Bar Curl", "EZ bar for wrist comfort.", "Biceps", "EZ curl bar"));
        addExercise(new Exercise(0, "Incline Dumbbell Curl", "Curl on incline bench.", "Biceps", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Concentration Curl", "Seated one-arm curl.", "Biceps", "Dumbbell"));
        addExercise(new Exercise(0, "Preacher Curl", "Curl on preacher bench.", "Biceps", "Barbell, preacher bench"));
        addExercise(new Exercise(0, "Cable Curl", "Standing cable curl.", "Biceps", "Cable machine"));
        addExercise(new Exercise(0, "Spider Curl", "Chest on bench curl.", "Biceps", "Barbell, incline bench"));
        addExercise(new Exercise(0, "Hammer Curl", "Neutral grip dumbbell curl.", "Biceps", "Dumbbells"));
        addExercise(new Exercise(0, "Bayesian Curl", "Cable curl with back lean.", "Biceps", "Cable machine"));

        // Forearms
        addExercise(new Exercise(0, "Wrist Curl", "Curl with palms up.", "Forearms", "Barbell"));
        addExercise(new Exercise(0, "Reverse Wrist Curl", "Curl with palms down.", "Forearms", "Barbell"));
        addExercise(new Exercise(0, "Hammer Curl (Forearms)", "Hammer grip curl.", "Forearms", "Dumbbells"));
        addExercise(new Exercise(0, "Zottman Curl", "Curl up, rotate down.", "Forearms", "Dumbbells"));
        addExercise(new Exercise(0, "Farmer's Walk", "Carry heavy weights.", "Forearms", "Dumbbells"));
        addExercise(new Exercise(0, "Reverse Curl", "Curl with overhand grip.", "Forearms", "Barbell"));
        addExercise(new Exercise(0, "Wrist Roller", "Roll weight up and down.", "Forearms", "Wrist roller"));
        addExercise(new Exercise(0, "Plate Pinch", "Pinch two plates together.", "Forearms", "Weight plates"));
        addExercise(new Exercise(0, "Dead Hang", "Hang from pull-up bar.", "Forearms", "Pull-up bar"));
        addExercise(new Exercise(0, "Towel Pull-Up", "Pull-up with towel grip.", "Forearms", "Towel, pull-up bar"));

        // Abs
        addExercise(new Exercise(0, "Plank", "Isometric core hold.", "Abs", "Bodyweight"));
        addExercise(new Exercise(0, "Hanging Leg Raise", "Raise legs while hanging.", "Abs", "Pull-up bar"));
        addExercise(new Exercise(0, "Cable Crunch", "Crunch with cable resistance.", "Abs", "Cable machine, rope"));
        addExercise(new Exercise(0, "Weighted Sit-Up", "Sit-up with added weight.", "Abs", "Weight plate"));
        addExercise(new Exercise(0, "Russian Twist", "Twist with weight.", "Abs", "Medicine ball"));
        addExercise(new Exercise(0, "Ab Wheel Rollout", "Roll ab wheel forward.", "Abs", "Ab wheel"));
        addExercise(new Exercise(0, "Decline Sit-Up", "Sit-up on decline bench.", "Abs", "Decline bench"));
        addExercise(new Exercise(0, "Toe Touch", "Reach for toes while lying down.", "Abs", "Bodyweight"));
        addExercise(new Exercise(0, "V-Up", "Simultaneous leg and torso raise.", "Abs", "Bodyweight"));
        addExercise(new Exercise(0, "Mountain Climber", "Alternating knee drive.", "Abs", "Bodyweight"));

        // Quads
        addExercise(new Exercise(0, "Back Squat", "Barbell squat for quads.", "Quads", "Barbell"));
        addExercise(new Exercise(0, "Front Squat", "Squat with barbell in front.", "Quads", "Barbell"));
        addExercise(new Exercise(0, "Leg Press", "Machine press for legs.", "Quads", "Leg press machine"));
        addExercise(new Exercise(0, "Walking Lunge", "Alternating forward lunge.", "Quads", "Dumbbells"));
        addExercise(new Exercise(0, "Split Squat", "Stationary lunge.", "Quads", "Dumbbells"));
        addExercise(new Exercise(0, "Step-Up", "Step onto bench or box.", "Quads", "Dumbbells, bench"));
        addExercise(new Exercise(0, "Hack Squat", "Machine squat variation.", "Quads", "Hack squat machine"));
        addExercise(new Exercise(0, "Sissy Squat", "Deep knee bend.", "Quads", "Bodyweight"));
        addExercise(new Exercise(0, "Cyclist Squat", "Heels elevated squat.", "Quads", "Barbell, wedge"));
        addExercise(new Exercise(0, "Wall Sit", "Static hold against wall.", "Quads", "Bodyweight"));

        // Hamstrings
        addExercise(new Exercise(0, "Romanian Deadlift", "Hip hinge for hamstrings.", "Hamstrings", "Barbell"));
        addExercise(new Exercise(0, "Lying Leg Curl", "Machine curl for hamstrings.", "Hamstrings", "Leg curl machine"));
        addExercise(new Exercise(0, "Seated Leg Curl", "Seated machine curl.", "Hamstrings", "Leg curl machine"));
        addExercise(new Exercise(0, "Good Morning", "Barbell hip hinge.", "Hamstrings", "Barbell"));
        addExercise(new Exercise(0, "Nordic Curl", "Bodyweight hamstring curl.", "Hamstrings", "Bodyweight, anchor"));
        addExercise(new Exercise(0, "Glute Ham Raise", "Raise on GHD machine.", "Hamstrings", "GHD machine"));
        addExercise(new Exercise(0, "Kettlebell Swing", "Explosive hip hinge.", "Hamstrings", "Kettlebell"));
        addExercise(new Exercise(0, "Cable Pull-Through", "Cable hip hinge.", "Hamstrings", "Cable machine, rope"));
        addExercise(new Exercise(0, "Single-Leg Deadlift", "Balance and hinge on one leg.", "Hamstrings", "Dumbbell"));
        addExercise(new Exercise(0, "Reverse Hyperextension", "Hip extension machine.", "Hamstrings", "Reverse hyper machine"));

        // Glutes
        addExercise(new Exercise(0, "Hip Thrust", "Barbell hip thrust for glutes.", "Glutes", "Barbell, bench"));
        addExercise(new Exercise(0, "Glute Bridge", "Bodyweight hip bridge.", "Glutes", "Bodyweight"));
        addExercise(new Exercise(0, "Bulgarian Split Squat", "Rear foot elevated squat.", "Glutes", "Dumbbells, bench"));
        addExercise(new Exercise(0, "Cable Kickback", "Cable glute extension.", "Glutes", "Cable machine, ankle strap"));
        addExercise(new Exercise(0, "Step-Up (Glutes)", "Step onto high box.", "Glutes", "Dumbbells, box"));
        addExercise(new Exercise(0, "Frog Pump", "Feet together glute bridge.", "Glutes", "Bodyweight"));
        addExercise(new Exercise(0, "Reverse Lunge", "Step back lunge.", "Glutes", "Dumbbells"));
        addExercise(new Exercise(0, "Glute-Ham Raise (Glutes)", "GHD machine raise.", "Glutes", "GHD machine"));
        addExercise(new Exercise(0, "Donkey Kick", "Quadruped leg kick.", "Glutes", "Bodyweight"));
        addExercise(new Exercise(0, "Curtsy Lunge", "Lunge behind and across.", "Glutes", "Dumbbells"));

        // Calves
        addExercise(new Exercise(0, "Standing Calf Raise", "Classic standing raise for calves.", "Calves", "Bodyweight"));
        addExercise(new Exercise(0, "Seated Calf Raise", "Calf raise while seated.", "Calves", "Seated calf raise machine"));
        addExercise(new Exercise(0, "Donkey Calf Raise", "Bent-over calf raise.", "Calves", "Bodyweight"));
        addExercise(new Exercise(0, "Smith Machine Calf Raise", "Calf raise on Smith machine.", "Calves", "Smith machine"));
        addExercise(new Exercise(0, "Leg Press Calf Raise", "Calf raise on leg press.", "Calves", "Leg press machine"));
        addExercise(new Exercise(0, "Single-Leg Calf Raise", "One leg at a time.", "Calves", "Bodyweight"));
        addExercise(new Exercise(0, "Tibialis Raise", "Lift toes for tibialis.", "Calves", "Bodyweight"));
        addExercise(new Exercise(0, "Jump Rope", "Jump rope for calves.", "Calves", "Jump rope"));
        addExercise(new Exercise(0, "Barbell Calf Raise", "Barbell on back calf raise.", "Calves", "Barbell"));
        addExercise(new Exercise(0, "Weighted Calf Walk", "Walk on toes with weights.", "Calves", "Dumbbells"));



        // Front Delts
        addExercise(new Exercise(0, "Front Raise", "Raise weights to shoulder level; keep arms straight.", "Front Delts", "Dumbbells or barbell"));
        addExercise(new Exercise(0, "Overhead Press (Front Delts)", "Press weight overhead; avoid arching back.", "Front Delts", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Arnold Press (Front Delts)", "Rotate palms during press; full range of motion.", "Front Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Incline Bench Press (Front Delts)", "Bench at 30–45° angle; press upwards.", "Front Delts", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Push Press (Front Delts)", "Use slight leg drive; press overhead.", "Front Delts", "Barbell"));
        addExercise(new Exercise(0, "Military Press (Front Delts)", "Stand upright; press barbell overhead.", "Front Delts", "Barbell"));
        addExercise(new Exercise(0, "Landmine Press (Front Delts)", "Press barbell at an angle; keep core engaged.", "Front Delts", "Barbell in landmine attachment"));
        addExercise(new Exercise(0, "Cable Front Raise", "Use low pulley; raise handle to shoulder height.", "Front Delts", "Cable machine"));
        addExercise(new Exercise(0, "Z-Press (Front Delts)", "Sit on floor with legs extended; press overhead.", "Front Delts", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Incline Front Raise", "Lie face down; raise weights in front.", "Front Delts", "Dumbbells, incline bench"));

        // Side Delts
        addExercise(new Exercise(0, "Lateral Raise", "Raise arms to sides until shoulder level; slight bend in elbows.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Upright Row (Side Delts)", "Pull weight to chest level; elbows higher than wrists.", "Side Delts", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Cable Lateral Raise", "Use low pulley; raise arm to side.", "Side Delts", "Cable machine"));
        addExercise(new Exercise(0, "Arnold Press (Side Delts)", "Rotate palms during press; full range of motion.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Dumbbell Shoulder Press (Side Delts)", "Press weights overhead; keep back straight.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Machine Lateral Raise", "Adjust seat height; align shoulders with machine arms.", "Side Delts", "Lateral raise machine"));
        addExercise(new Exercise(0, "Incline Lateral Raise", "Lie on side; raise top arm to shoulder level.", "Side Delts", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Leaning Cable Lateral Raise", "Lean away from pulley; raise arm to side.", "Side Delts", "Cable machine"));
        addExercise(new Exercise(0, "Seated Dumbbell Lateral Raise", "Sit upright; raise arms to sides.", "Side Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Barbell Overhead Press (Side Delts)", "Press barbell overhead; control movement.", "Side Delts", "Barbell"));

        // Rear Delts
        addExercise(new Exercise(0, "Reverse Fly", "Bend at hips; lift arms to side.", "Rear Delts", "Dumbbells"));
        addExercise(new Exercise(0, "Face Pull", "Pull towards face; elbows high and out.", "Rear Delts", "Cable machine with rope"));
        addExercise(new Exercise(0, "Reverse Pec Deck", "Sit facing the pad; adjust arms for reverse fly motion.", "Rear Delts", "Pec deck machine"));
        addExercise(new Exercise(0, "Bent-Over Reverse Cable Fly", "Cross cables; pull arms out wide while bent over.", "Rear Delts", "Cable machine"));
        addExercise(new Exercise(0, "Band Pull Apart", "Stretch band apart at chest level; control throughout.", "Rear Delts", "Resistance band"));
        addExercise(new Exercise(0, "Seated Rear Delt Row", "Pull elbows back and out; focus on rear delts.", "Rear Delts", "Cable machine or resistance band"));
        addExercise(new Exercise(0, "Rear Delt Row (Chest-Supported)", "Chest on bench; row with elbows flared.", "Rear Delts", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Reverse Cable Crossover", "Start with cables crossed; pull outward and back.", "Rear Delts", "Cable machine"));
        addExercise(new Exercise(0, "Prone Rear Delt Raise", "Lie chest down; lift arms to the sides.", "Rear Delts", "Dumbbells, flat or incline bench"));
        addExercise(new Exercise(0, "High Row (Rear Delt Focused)", "Use wide grip; pull to upper chest with elbows high.", "Rear Delts", "Cable machine or barbell"));

        // Upper Back
        addExercise(new Exercise(0, "Barbell Row", "Hinge at hips; pull bar to lower chest/upper abs.", "Upper Back", "Barbell"));
        addExercise(new Exercise(0, "Chest-Supported Row", "Keep chest on bench; row with control.", "Upper Back", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "T-Bar Row", "Neutral grip; drive elbows back.", "Upper Back", "T-bar or landmine setup"));
        addExercise(new Exercise(0, "Cable Face Pull", "Pull towards face; flare elbows out.", "Upper Back", "Cable machine with rope"));
        addExercise(new Exercise(0, "Reverse Cable Crossover", "Use light weight; focus on contraction.", "Upper Back", "Cable machine"));
        addExercise(new Exercise(0, "Inverted Row", "Body in straight line; pull chest to bar.", "Upper Back", "Bar or TRX"));
        addExercise(new Exercise(0, "Seated Cable Row (Wide Grip)", "Elbows high and wide; squeeze shoulder blades.", "Upper Back", "Cable row machine with wide bar"));
        addExercise(new Exercise(0, "Dumbbell Shrug", "Lift shoulders up; avoid rolling.", "Upper Back", "Dumbbells"));
        addExercise(new Exercise(0, "Barbell Shrug", "Use heavy weight; keep reps controlled.", "Upper Back", "Barbell"));
        addExercise(new Exercise(0, "Face-Down Y Raise", "Lie prone; raise arms in a Y shape.", "Upper Back", "Dumbbells or bodyweight"));

        // Lats
        addExercise(new Exercise(0, "Pull-Up", "Use full range; control descent.", "Lats", "Pull-up bar"));
        addExercise(new Exercise(0, "Lat Pulldown", "Lean slightly back; pull to upper chest.", "Lats", "Lat pulldown machine"));
        addExercise(new Exercise(0, "Straight-Arm Pulldown", "Keep arms straight; hinge at shoulders.", "Lats", "Cable machine"));
        addExercise(new Exercise(0, "Single-Arm Dumbbell Row", "Pull with elbow; avoid twisting torso.", "Lats", "Dumbbell and bench"));
        addExercise(new Exercise(0, "Meadow’s Row", "Staggered stance; pull to hip.", "Lats", "Landmine barbell"));
        addExercise(new Exercise(0, "Seal Row", "Chest supported; strict rowing motion.", "Lats", "Barbell, flat bench"));
        addExercise(new Exercise(0, "Chin-Up", "Supinated grip; engage lats from bottom.", "Lats", "Pull-up bar"));
        addExercise(new Exercise(0, "Machine Row (Close Grip)", "Pull handle to torso; control return.", "Lats", "Seated row machine"));
        addExercise(new Exercise(0, "Bent-Over Dumbbell Row (Neutral Grip)", "Keep back flat; row towards hips.", "Lats", "Dumbbells"));
        addExercise(new Exercise(0, "Dead Stop Row", "Rest bar on ground each rep; reset form.", "Lats", "Barbell"));

        // Triceps
        addExercise(new Exercise(0, "Close-Grip Bench Press", "Hands just inside shoulder width; elbows close to body.", "Triceps", "Barbell"));
        addExercise(new Exercise(0, "Triceps Pushdown", "Keep elbows tucked; extend fully at the bottom.", "Triceps", "Cable machine (rope or straight bar)"));
        addExercise(new Exercise(0, "Overhead Triceps Extension (Dumbbell)", "Hold with both hands; lower behind head, extend up.", "Triceps", "Dumbbell"));
        addExercise(new Exercise(0, "Skullcrusher (Lying Triceps Extension)", "Lower to forehead or behind head; control descent.", "Triceps", "EZ bar or dumbbells"));
        addExercise(new Exercise(0, "Dips (Triceps Variation)", "Keep torso upright; focus on elbow extension.", "Triceps", "Parallel bars or bench"));
        addExercise(new Exercise(0, "Cable Overhead Triceps Extension", "Face away from pulley; extend arms overhead.", "Triceps", "Cable machine with rope"));
        addExercise(new Exercise(0, "Kickback", "Elbows fixed; extend arms fully back.", "Triceps", "Dumbbells or cables"));
        addExercise(new Exercise(0, "EZ Bar Triceps Extension (Overhead)", "Use both hands; lower bar behind head.", "Triceps", "EZ bar"));
        addExercise(new Exercise(0, "Bodyweight Triceps Extension", "Lean forward and extend elbows to press body up.", "Triceps", "Bar, bench or Smith machine"));
        addExercise(new Exercise(0, "Machine Triceps Press (Seated Dip Machine)", "Push handles down; keep elbows close.", "Triceps", "Dip press machine"));

        // Biceps
        addExercise(new Exercise(0, "Barbell Curl", "Keep elbows tucked; don’t swing the weight.", "Biceps", "Barbell"));
        addExercise(new Exercise(0, "Dumbbell Curl", "Rotate wrist (supinate) as you lift for peak contraction.", "Biceps", "Dumbbells"));
        addExercise(new Exercise(0, "EZ Bar Curl", "Easier on wrists; keep elbows by sides.", "Biceps", "EZ curl bar"));
        addExercise(new Exercise(0, "Incline Dumbbell Curl", "Keep elbows behind torso; full stretch at bottom.", "Biceps", "Dumbbells, incline bench"));
        addExercise(new Exercise(0, "Concentration Curl", "Sit down, elbow against inner thigh; curl slowly.", "Biceps", "Dumbbell"));
        addExercise(new Exercise(0, "Preacher Curl", "Elbows fixed; great isolation.", "Biceps", "EZ bar or dumbbells, preacher bench"));
        addExercise(new Exercise(0, "Cable Curl", "Constant tension; curl with straight bar or rope.", "Biceps", "Cable machine"));
        addExercise(new Exercise(0, "Spider Curl", "Chest on incline bench; curl vertically.", "Biceps", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Hammer Curl", "Neutral grip; lift with control.", "Biceps", "Dumbbells"));
        addExercise(new Exercise(0, "Zottman Curl", "Curl with supinated grip; rotate at top and lower slowly with pronated grip.", "Biceps", "Dumbbells"));

        // Forearms
        addExercise(new Exercise(0, "Wrist Curl", "Sit on bench, rest forearms on thighs, curl wrists up.", "Forearms", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Reverse Wrist Curl", "Same position as wrist curl; palms facing down.", "Forearms", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Hammer Curl", "Neutral grip; lift with control.", "Forearms", "Dumbbells"));
        addExercise(new Exercise(0, "Zottman Curl", "Supinated up, pronated down for full forearm engagement.", "Forearms", "Dumbbells"));
        addExercise(new Exercise(0, "Farmer’s Carry", "Walk slowly; maintain upright posture.", "Forearms", "Dumbbells or kettlebells"));
        addExercise(new Exercise(0, "Reverse Curl", "Overhand grip; elbows fixed at sides.", "Forearms", "EZ bar or barbell"));
        addExercise(new Exercise(0, "Wrist Roller", "Roll weight up and down using wrist flexion and extension.", "Forearms", "Wrist roller device"));
        addExercise(new Exercise(0, "Plate Pinch Hold", "Hold two plates together with fingers and thumb.", "Forearms", "Weight plates"));
        } catch (Exception e) {
            System.err.println("[DEBUG] Error in seedDefaultExercises: " + e.getMessage());
            e.printStackTrace();
        }
        addExercise(new Exercise(0, "Dead Hang", "Passive hang from bar; aim for time under tension.", "Forearms", "Pull-up bar"));
        addExercise(new Exercise(0, "Towel Pull-Up Hold", "Wrap towel over bar; hold or pull up for intensity.", "Forearms", "Towels, pull-up bar"));

        // Abs
        addExercise(new Exercise(0, "Plank", "Keep body straight; avoid sagging hips.", "Abs", "Bodyweight"));
        addExercise(new Exercise(0, "Hanging Leg Raise", "Raise legs straight; avoid swinging.", "Abs", "Pull-up bar"));
        addExercise(new Exercise(0, "Cable Crunch", "Kneel and crunch down using core, not arms.", "Abs", "Cable machine with rope"));
        addExercise(new Exercise(0, "Weighted Sit-Up", "Hold weight on chest; avoid neck strain.", "Abs", "Dumbbell or plate"));
        addExercise(new Exercise(0, "Russian Twist", "Twist torso side to side; keep back upright.", "Abs", "Bodyweight, plate or medicine ball"));
        addExercise(new Exercise(0, "Ab Wheel Rollout", "Control rollout and return; keep core tight.", "Abs", "Ab wheel"));
        addExercise(new Exercise(0, "Decline Sit-Up", "Slow descent; hands across chest or behind head.", "Abs", "Decline bench"));
        addExercise(new Exercise(0, "Toe Touch", "Lie on back; reach for toes with legs raised.", "Abs", "Bodyweight or small weight"));
        addExercise(new Exercise(0, "V-Up", "Simultaneously lift legs and torso to form a 'V'.", "Abs", "Bodyweight"));
        addExercise(new Exercise(0, "Mountain Climber", "Fast knee drives while in plank position.", "Abs", "Bodyweight"));

        // Quads
        addExercise(new Exercise(0, "Back Squat", "Bar on upper traps; squat to parallel or below.", "Quads", "Barbell"));
        addExercise(new Exercise(0, "Front Squat", "Keep elbows high; upright torso for quad emphasis.", "Quads", "Barbell"));
        addExercise(new Exercise(0, "Leg Press", "Feet shoulder-width; push through heels.", "Quads", "Leg press machine"));
        addExercise(new Exercise(0, "Walking Lunge", "Step forward and lower until back knee nearly touches ground.", "Quads", "Dumbbells or barbell"));
        addExercise(new Exercise(0, "Split Squat", "Rear foot elevated (optional); drop straight down.", "Quads", "Dumbbells or bodyweight"));
        addExercise(new Exercise(0, "Step-Up", "Push through front foot; full extension at the top.", "Quads", "Dumbbells or barbell, bench or box"));
        addExercise(new Exercise(0, "Hack Squat", "Keep feet lower on platform to bias quads.", "Quads", "Hack squat machine"));
        addExercise(new Exercise(0, "Sissy Squat", "Lean back while lowering; keep hips extended.", "Quads", "Sissy squat machine or bodyweight"));
        addExercise(new Exercise(0, "Cyclist Squat", "Heels elevated; deep knee bend for quad tension.", "Quads", "Barbell, wedge or plates"));
        addExercise(new Exercise(0, "Wall Sit", "Hold 90° seated position against wall; static contraction.", "Quads", "Bodyweight"));

        // Hamstrings
        addExercise(new Exercise(0, "Romanian Deadlift", "Hinge at hips; slight knee bend; stretch hamstrings.", "Hamstrings", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Lying Leg Curl", "Flex knees; slow eccentric for best activation.", "Hamstrings", "Leg curl machine"));
        addExercise(new Exercise(0, "Seated Leg Curl", "Adjust pad above ankles; flex through full range.", "Hamstrings", "Seated leg curl machine"));
        addExercise(new Exercise(0, "Good Morning", "Bar on traps; hinge at hips with flat back.", "Hamstrings", "Barbell"));
        addExercise(new Exercise(0, "Nordic Hamstring Curl", "Lower slowly; use arms to assist if needed.", "Hamstrings", "Bodyweight, anchored feet"));
        addExercise(new Exercise(0, "Glute Ham Raise", "Start from parallel; curl yourself upright using hamstrings.", "Hamstrings", "GHD machine"));
        addExercise(new Exercise(0, "Kettlebell Swing", "Hinge not squat; drive hips forward forcefully.", "Hamstrings", "Kettlebell"));
        addExercise(new Exercise(0, "Cable Pull-Through", "Face away from stack; hinge and pull rope through.", "Hamstrings", "Cable machine with rope"));
        addExercise(new Exercise(0, "Single-Leg Romanian Deadlift", "Hinge on one leg; maintain balance and control.", "Hamstrings", "Dumbbell or kettlebell"));
        addExercise(new Exercise(0, "Barbell Hip Thrust (Hamstring Bias)", "Feet further forward to shift load to hamstrings.", "Hamstrings", "Barbell, bench"));

        // Glutes
        addExercise(new Exercise(0, "Barbell Hip Thrust", "Drive through heels; squeeze glutes at the top.", "Glutes", "Barbell, bench"));
        addExercise(new Exercise(0, "Glute Bridge", "Keep feet flat and close to hips; lift hips to full extension.", "Glutes", "Bodyweight or barbell"));
        addExercise(new Exercise(0, "Bulgarian Split Squat", "Rear foot elevated; sink hips straight down.", "Glutes", "Dumbbells or barbell, bench"));
        addExercise(new Exercise(0, "Romanian Deadlift", "Hinge with flat back; feel the stretch in glutes.", "Glutes", "Barbell or dumbbells"));
        addExercise(new Exercise(0, "Cable Kickback", "Keep knee slightly bent; kick straight back.", "Glutes", "Cable machine with ankle strap"));
        addExercise(new Exercise(0, "Step-Up (Glute Focused)", "Step high enough to force hip extension.", "Glutes", "Dumbbells or barbell, box"));
        addExercise(new Exercise(0, "Frog Pump", "Soles of feet together; thrust hips up explosively.", "Glutes", "Bodyweight or dumbbell"));
        addExercise(new Exercise(0, "Reverse Lunge", "Step back into a lunge; push up through front heel.", "Glutes", "Dumbbells or barbell"));
        addExercise(new Exercise(0, "Glute-Ham Raise", "Initiate movement with glutes; keep core tight.", "Glutes", "GHD machine"));
        addExercise(new Exercise(0, "Donkey Kick", "Keep foot flexed; drive heel up to ceiling.", "Glutes", "Bodyweight or ankle weights"));

        // Calves
        addExercise(new Exercise(0, "Standing Calf Raise", "Straight legs; full contraction and stretch.", "Calves", "Bodyweight or machine"));
        addExercise(new Exercise(0, "Seated Calf Raise", "Knees bent to isolate soleus; controlled reps.", "Calves", "Seated calf raise machine"));
        addExercise(new Exercise(0, "Donkey Calf Raise", "Bent at hips; emphasises full stretch and peak contraction.", "Calves", "Machine or bodyweight"));
        addExercise(new Exercise(0, "Smith Machine Calf Raise", "Use block under feet for increased range.", "Calves", "Smith machine"));
        addExercise(new Exercise(0, "Leg Press Calf Raise", "Push only with forefoot; keep knees locked.", "Calves", "Leg press machine"));
        addExercise(new Exercise(0, "Single-Leg Calf Raise", "Perform slowly; use support for balance.", "Calves", "Bodyweight or dumbbell"));
        addExercise(new Exercise(0, "Tibialis Raise", "Flex toes upward; works front of lower leg.", "Calves", "Bodyweight or tib bar"));
        addExercise(new Exercise(0, "Jump Rope", "High reps for endurance; use soft bounce.", "Calves", "Skipping rope"));
        addExercise(new Exercise(0, "Barbell Calf Raise", "Bar on back; elevate heels on platform for full range.", "Calves", "Barbell"));
        addExercise(new Exercise(0, "Weighted Calf Walk", "Walk on tiptoes holding weight; slow and steady steps.", "Calves", "Dumbbells or barbell"));
    }

    public void addExercise(Exercise exercise) throws SQLException {
        String sql = "INSERT INTO exercises (name, description, muscle_group, equipment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getDescription());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setString(4, exercise.getEquipment());
            stmt.executeUpdate();
        }
    }

    public void updateExercise(Exercise exercise) throws SQLException {
        String sql = "UPDATE exercises SET name = ?, description = ?, muscle_group = ?, equipment = ? WHERE exercise_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getDescription());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setString(4, exercise.getEquipment());
            stmt.setInt(5, exercise.getExerciseId());
            
            stmt.executeUpdate();
        }
    }

    public void deleteExercise(int exerciseId) throws SQLException {
        String sql = "DELETE FROM exercises WHERE exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exerciseId);
            stmt.executeUpdate();
        }
    }

    public Exercise getExercise(int exerciseId) throws SQLException {
        String sql = "SELECT * FROM exercises WHERE exercise_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, exerciseId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Exercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("muscle_group"),
                        rs.getString("equipment")
                    );
                }
            }
        }
        return null;
    }

    public List<Exercise> getAllExercises() throws SQLException {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises ORDER BY name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Exercise exercise = new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("muscle_group"),
                    rs.getString("equipment")
                );
                exercises.add(exercise);
            }
        }
        return exercises;
    }
}
