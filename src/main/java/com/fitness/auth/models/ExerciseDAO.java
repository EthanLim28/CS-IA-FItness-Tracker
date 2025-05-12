package com.fitness.auth.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {
    private final Connection connection;

    public ExerciseDAO(Connection connection) {
        this.connection = connection;
        try {
            if (getAllExercises().isEmpty()) {
                // Chest
                addExercise(new Exercise(0, "Bench Press", "Use a shoulder-width grip; ensure controlled movement.", "Chest", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Incline Dumbbell Press", "Set bench at a 30–45° angle; press upwards and slightly inwards.", "Chest", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Decline Barbell Press", "Secure feet; lower bar to lower chest.", "Chest", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Push-Up", "Maintain a straight line from head to heels; hands shoulder-width apart.", "Chest", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Chest Fly", "Slight bend in elbows; bring hands together in a wide arc.", "Chest", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Dips (Chest Variation)", "Lean forward to emphasise chest involvement.", "Chest", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Pec Deck Machine", "Adjust seat height so handles are at chest level.", "Chest", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Cable Crossover", "Start with arms extended; bring hands together in front of chest.", "Chest", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Incline Push-Up", "Hands on elevated surface; body in straight line.", "Chest", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Decline Push-Up", "Feet elevated; hands on floor; maintain straight body alignment.", "Chest", ExerciseCategory.STRENGTH, "None"));

                // Front Delts
                addExercise(new Exercise(0, "Front Raise", "Raise weights to shoulder level; keep arms straight.", "Front Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Overhead Press", "Press weight overhead; avoid arching back.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Arnold Press", "Rotate palms during press; full range of motion.", "Front Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Incline Bench Press", "Bench at 30–45° angle; press upwards.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Push Press", "Use slight leg drive; press overhead.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Military Press", "Stand upright; press barbell overhead.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Landmine Press", "Press barbell at an angle; keep core engaged.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Cable Front Raise", "Use low pulley; raise handle to shoulder height.", "Front Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Z-Press", "Sit on floor with legs extended; press overhead.", "Front Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Incline Front Raise", "Lie face down; raise weights in front.", "Front Delts", ExerciseCategory.STRENGTH, "Dumbbells"));

                // Side Delts
                addExercise(new Exercise(0, "Lateral Raise", "Raise arms to sides until shoulder level; slight bend in elbows.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Cable Lateral Raise", "Use cable machine; raise arm to side.", "Side Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Machine Lateral Raise", "Sit upright; raise arms to side using machine.", "Side Delts", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Upright Row", "Pull barbell to upper chest; elbows high.", "Side Delts", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Dumbbell Upright Row", "Pull dumbbells to upper chest; elbows high.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Lean-Away Lateral Raise", "Lean away from support; raise arm to side.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Seated Lateral Raise", "Sit upright; raise dumbbells to side.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Cable Y Raise", "Use cable; raise arms in Y shape.", "Side Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Incline Lateral Raise", "Lie on side; raise dumbbell to shoulder height.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Standing Lateral Raise", "Stand upright; raise dumbbells to side.", "Side Delts", ExerciseCategory.STRENGTH, "Dumbbells"));

                // Rear Delts
                addExercise(new Exercise(0, "Reverse Fly", "Bend at hips.", "Rear Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Face Pull", "Pull towards face; elbows high and out.", "Rear Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Reverse Pec Deck", "Sit facing the pad; adjust arms for reverse fly motion.", "Rear Delts", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Bent-Over Reverse Cable Fly", "Cross cables; pull arms out wide while bent over.", "Rear Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Band Pull Apart", "Stretch band apart at chest level; control throughout.", "Rear Delts", ExerciseCategory.STRENGTH, "Resistance Band"));
                addExercise(new Exercise(0, "Seated Rear Delt Row", "Pull elbows back and out; focus on rear delts.", "Rear Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Rear Delt Row (Chest-Supported)", "Chest on bench; row with elbows flared.", "Rear Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Reverse Cable Crossover", "Start with cables crossed; pull outward and back.", "Rear Delts", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Prone Rear Delt Raise", "Lie chest down; lift arms to the sides.", "Rear Delts", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "High Row (Rear Delt Focused)", "Use wide grip; pull to upper chest with elbows high.", "Rear Delts", ExerciseCategory.STRENGTH, "Cables"));

                // Upper Back
                addExercise(new Exercise(0, "Barbell Row", "Hinge at hips, pull bar to lower chest.", "Upper Back", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Chest Supported Row", "Bench at 45°, pull dumbbells to hips.", "Upper Back", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Seated Cable Row", "Sit upright, pull handle to torso.", "Upper Back", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "T-Bar Row", "Neutral grip, drive elbows back.", "Upper Back", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Cable Face Pull", "Pull towards face, flare elbows out.", "Upper Back", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Reverse Cable Crossover", "Use light weight, focus on contraction.", "Upper Back", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Inverted Row", "Body in straight line, pull chest to bar.", "Upper Back", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Seated Cable Row (Wide Grip)", "Elbows high and wide, squeeze shoulder blades.", "Upper Back", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Dumbbell Shrug", "Lift shoulders up, avoid rolling.", "Upper Back", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Barbell Shrug", "Use heavy weight, keep reps controlled.", "Upper Back", ExerciseCategory.STRENGTH, "Barbell"));


                // Lats
                addExercise(new Exercise(0, "Pull-Up", "Use full range, control descent.", "Lats", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Lat Pulldown", "Lean slightly back, pull to upper chest.", "Lats", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Straight-Arm Pulldown", "Keep arms straight, hinge at shoulders.", "Lats", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Single-Arm Dumbbell Row", "Pull with elbow, avoid twisting torso.", "Lats", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Meadow’s Row", "Staggered stance, pull to hip.", "Lats", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Seal Row", "Chest supported, strict rowing motion.", "Lats", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Chin-Up", "Supinated grip, engage lats from bottom.", "Lats", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Machine Row (Close Grip)", "Pull handle to torso, control return.", "Lats", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Bent-Over Dumbbell Row (Neutral Grip)", "Keep back flat, row towards hips.", "Lats", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Dead Stop Row", "Rest bar on ground each rep, reset form.", "Lats", ExerciseCategory.STRENGTH, "Barbell"));

                // Triceps
                addExercise(new Exercise(0, "Close-Grip Bench Press", "Hands just inside shoulder width, elbows close to body.", "Triceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Triceps Pushdown", "Keep elbows tucked, extend fully at the bottom.", "Triceps", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Overhead Triceps Extension (Dumbbell)", "Hold with both hands, lower behind head, extend up.", "Triceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Skullcrusher (Lying Triceps Extension)", "Lower to forehead or behind head, control descent.", "Triceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Dips (Triceps Variation)", "Keep torso upright, focus on elbow extension.", "Triceps", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Cable Overhead Triceps Extension", "Face away from pulley, extend arms overhead.", "Triceps", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Kickback", "Elbows fixed, extend arms fully back.", "Triceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "EZ Bar Triceps Extension (Overhead)", "Use both hands, lower bar behind head.", "Triceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Bodyweight Triceps Extension", "Lean forward and extend elbows to press body up.", "Triceps", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Machine Triceps Press (Seated Dip Machine)", "Push handles down, keep elbows close.", "Triceps", ExerciseCategory.STRENGTH, "Machine"));

                // Biceps
                addExercise(new Exercise(0, "Barbell Curl", "Keep elbows tucked, don’t swing the weight.", "Biceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Dumbbell Curl", "Rotate wrist (supinate) as you lift for peak contraction.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "EZ Bar Curl", "Easier on wrists, keep elbows by sides.", "Biceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Incline Dumbbell Curl", "Keep elbows behind torso, full stretch at bottom.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Concentration Curl", "Sit down, elbow against inner thigh, curl slowly.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Preacher Curl", "Elbows fixed, great isolation.", "Biceps", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Cable Curl", "Constant tension, curl with straight bar or rope.", "Biceps", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Spider Curl", "Chest on incline bench, curl vertically.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Hammer Curl", "Neutral grip, lift with control.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Zottman Curl", "Curl with supinated grip, rotate at top and lower slowly with pronated grip.", "Biceps", ExerciseCategory.STRENGTH, "Dumbbells"));

                // Forearms
                addExercise(new Exercise(0, "Wrist Curl", "Sit on bench, rest forearms on thighs, curl wrists up.", "Forearms", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Reverse Wrist Curl", "Same position as wrist curl, palms facing down.", "Forearms", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Hammer Curl", "Neutral grip, lift with control.", "Forearms", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Zottman Curl", "Supinated up, pronated down for full forearm engagement.", "Forearms", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Farmer's Walk", "Hold heavy dumbbells, walk with upright posture.", "Forearms", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Plate Pinch", "Pinch weight plates together, hold as long as possible.", "Forearms", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Reverse Curl", "Curl with overhand grip, targets brachioradialis.", "Forearms", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Towel Pull-Up", "Drape towels over bar, pull up for grip strength.", "Forearms", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Wrist Roller", "Roll weight up and down using wrists.", "Forearms", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Dead Hang", "Hang from bar as long as possible, keep shoulders engaged.", "Forearms", ExerciseCategory.STRENGTH, "None"));

                // Abs
                addExercise(new Exercise(0, "Plank", "Keep body straight, avoid sagging hips.", "Abs", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Hanging Leg Raise", "Raise legs straight, avoid swinging.", "Abs", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Cable Crunch", "Kneel and crunch down using core, not arms.", "Abs", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Weighted Sit-Up", "Hold weight on chest, avoid neck strain.", "Abs", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Russian Twist", "Twist torso side to side, keep back upright.", "Abs", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Ab Wheel Rollout", "Control rollout and return, keep core tight.", "Abs", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Decline Sit-Up", "Slow descent, hands across chest or behind head.", "Abs", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Toe Touch", "Lie on back, reach for toes with legs raised.", "Abs", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "V-Up", "Simultaneously lift legs and torso to form a 'V'.", "Abs", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Mountain Climber", "Fast knee drives while in plank position.", "Abs", ExerciseCategory.STRENGTH, "None"));

                // Quads
                addExercise(new Exercise(0, "Back Squat", "Bar on upper traps, squat to parallel or below.", "Quads", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Front Squat", "Keep elbows high, upright torso for quad emphasis.", "Quads", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Leg Press", "Feet shoulder-width, push through heels.", "Quads", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Walking Lunge", "Step forward and lower until back knee nearly touches ground.", "Quads", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Split Squat", "Rear foot elevated (optional), drop straight down.", "Quads", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Step-Up", "Push through front foot, full extension at the top.", "Quads", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Hack Squat", "Keep feet lower on platform to bias quads.", "Quads", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Sissy Squat", "Lean back while lowering, keep hips extended.", "Quads", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Cyclist Squat", "Heels elevated, deep knee bend for quad tension.", "Quads", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Wall Sit", "Hold 90° seated position against wall, static contraction.", "Quads", ExerciseCategory.STRENGTH, "None"));

                // Hamstrings
                addExercise(new Exercise(0, "Romanian Deadlift", "Hinge at hips, slight knee bend, stretch hamstrings.", "Hamstrings", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Lying Leg Curl", "Flex knees, slow eccentric for best activation.", "Hamstrings", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Seated Leg Curl", "Adjust pad above ankles, flex through full range.", "Hamstrings", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Good Morning", "Bar on traps, hinge at hips with flat back.", "Hamstrings", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Nordic Hamstring Curl", "Lower slowly, use arms to assist if needed.", "Hamstrings", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Glute Ham Raise", "Start from parallel, curl yourself upright using hamstrings.", "Hamstrings", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Kettlebell Swing", "Hinge not squat, drive hips forward forcefully.", "Hamstrings", ExerciseCategory.STRENGTH, "Kettlebell"));
                addExercise(new Exercise(0, "Cable Pull-Through", "Face away from stack, hinge and pull rope through.", "Hamstrings", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Single-Leg Romanian Deadlift", "Hinge on one leg, maintain balance and control.", "Hamstrings", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Barbell Hip Thrust (Hamstring Bias)", "Feet further forward to shift load to hamstrings.", "Hamstrings", ExerciseCategory.STRENGTH, "Barbell"));

                // Glutes
                addExercise(new Exercise(0, "Barbell Hip Thrust", "Drive through heels, squeeze glutes at the top.", "Glutes", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Glute Bridge", "Feet on floor, lift hips until fully extended.", "Glutes", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Cable Kickback", "Extend leg back and up, squeeze glutes at top.", "Glutes", ExerciseCategory.STRENGTH, "Cables"));
                addExercise(new Exercise(0, "Kettlebell Swing (Glute Focus)", "Drive hips forward, focus on glute contraction.", "Glutes", ExerciseCategory.STRENGTH, "Kettlebell"));
                addExercise(new Exercise(0, "Bulgarian Split Squat", "Rear foot elevated, drop straight down.", "Glutes", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Step-Up (Glute Focus)", "Step onto bench, push through heel.", "Glutes", ExerciseCategory.STRENGTH, "Dumbbells"));
                addExercise(new Exercise(0, "Sumo Deadlift", "Wide stance, toes out, lift with hips and glutes.", "Glutes", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Frog Pump", "Feet together, knees out, thrust hips up.", "Glutes", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Hip Abduction Machine", "Push knees apart, squeeze at top.", "Glutes", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Reverse Hyperextension", "Lie face down, lift legs behind using glutes.", "Glutes", ExerciseCategory.STRENGTH, "Machine"));

                // Glutes (continued)
                // (Already seeded: Barbell Hip Thrust, Glute Bridge, Cable Kickback, Kettlebell Swing (Glute Focus), Bulgarian Split Squat, Step-Up (Glute Focus), Sumo Deadlift, Frog Pump, Hip Abduction Machine, Reverse Hyperextension)

                // Calves
                addExercise(new Exercise(0, "Standing Calf Raise", "Straight legs, full contraction and stretch.", "Calves", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Seated Calf Raise", "Knees bent to isolate soleus, controlled reps.", "Calves", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Donkey Calf Raise", "Bent at hips, emphasises full stretch and peak contraction.", "Calves", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Smith Machine Calf Raise", "Use block under feet for increased range.", "Calves", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Leg Press Calf Raise", "Push only with forefoot, keep knees locked.", "Calves", ExerciseCategory.STRENGTH, "Machine"));
                addExercise(new Exercise(0, "Single-Leg Calf Raise", "Perform slowly, use support for balance.", "Calves", ExerciseCategory.STRENGTH, "None"));
                addExercise(new Exercise(0, "Tibialis Raise", "Flex toes upward, works front of lower leg.", "Calves", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Jump Rope", "High reps for endurance, use soft bounce.", "Calves", ExerciseCategory.STRENGTH, "Other"));
                addExercise(new Exercise(0, "Barbell Calf Raise", "Bar on back, elevate heels on platform for full range.", "Calves", ExerciseCategory.STRENGTH, "Barbell"));
                addExercise(new Exercise(0, "Weighted Calf Walk", "Walk on tiptoes holding weight, slow and steady steps.", "Calves", ExerciseCategory.STRENGTH, "Dumbbells"));
            }
        } catch (Exception e) {
            System.err.println("Error seeding default exercises: " + e.getMessage());
        }
    }

    public void addExercise(Exercise exercise) throws SQLException {
        String sql = "INSERT INTO exercises (name, description, muscle_group, category, equipment) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getDescription());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setString(4, exercise.getCategory().name());
            stmt.setString(5, exercise.getEquipment());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exercise.setExerciseId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateExercise(Exercise exercise) throws SQLException {
        String sql = "UPDATE exercises SET name = ?, description = ?, muscle_group = ?, category = ?, equipment = ? WHERE exercise_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getDescription());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setString(4, exercise.getCategory().name());
            stmt.setString(5, exercise.getEquipment());
            stmt.setInt(6, exercise.getExerciseId());
            
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
                        ExerciseCategory.valueOf(rs.getString("category")),
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
                    ExerciseCategory.valueOf(rs.getString("category")),
                    rs.getString("equipment")
                );
                exercises.add(exercise);
            }
        }
        return exercises;
    }

    public List<Exercise> searchExercises(String searchTerm) throws SQLException {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE LOWER(name) LIKE ? OR LOWER(muscle_group) LIKE ? OR LOWER(description) LIKE ?";
        String searchPattern = "%" + searchTerm.toLowerCase() + "%";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = new Exercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("muscle_group"),
                        ExerciseCategory.valueOf(rs.getString("category")),
                        rs.getString("equipment")
                    );
                    exercises.add(exercise);
                }
            }
        }
        return exercises;
    }

    public List<Exercise> getExercisesByCategory(ExerciseCategory category) throws SQLException {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE category = ? ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = new Exercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("muscle_group"),
                        ExerciseCategory.valueOf(rs.getString("category")),
                        rs.getString("equipment")
                    );
                    exercises.add(exercise);
                }
            }
        }
        return exercises;
    }
}
