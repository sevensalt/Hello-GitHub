import java.io.*;
import java.util.*;

// ============================================================================
// ENUMS
// ============================================================================

// OOP: Enum to represent game status
enum Status {
    WANT_TO_PLAY, PLAYING, COMPLETED, ABANDONED
}

// ============================================================================
// INTERFACES
// ============================================================================

// OOP: Interface - defines a contract for recommendation strategies
interface RecommendationStrategy {
    List<Game> recommend(List<Game> allGames, Game baseGame);
}

// OOP: Interface implementation - recommends games by genre
class GenreBasedStrategy implements RecommendationStrategy {
    @Override
    public List<Game> recommend(List<Game> allGames, Game baseGame) {
        List<Game> recommendations = new ArrayList<>();
        for (Game game : allGames) {
            if (!game.getId().equals(baseGame.getId()) && 
                game.getGenre().equalsIgnoreCase(baseGame.getGenre())) {
                recommendations.add(game);
            }
        }
        return recommendations;
    }
}

// OOP: Interface implementation - recommends games by estimated time
class TimeBasedStrategy implements RecommendationStrategy {
    @Override
    public List<Game> recommend(List<Game> allGames, Game baseGame) {
        List<Game> recommendations = new ArrayList<>();
        int baseHours = baseGame.getEstimatedHours();
        for (Game game : allGames) {
            if (!game.getId().equals(baseGame.getId()) && 
                Math.abs(game.getEstimatedHours() - baseHours) <= 10) {
                recommendations.add(game);
            }
        }
        return recommendations;
    }
}

// ============================================================================
// REVIEW CLASS
// ============================================================================

// OOP: Composition - Review belongs to a Game
class Review {
    // OOP: Encapsulation - private fields
    private String reviewer;
    private int rating;
    private String comment;

    // OOP: Constructor
    public Review(String reviewer, int rating, String comment) {
        this.reviewer = reviewer;
        this.rating = rating;
        this.comment = comment;
    }

    // OOP: Encapsulation - public getters
    public String getReviewer() { return reviewer; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }

    @Override
    public String toString() {
        return reviewer + " (" + rating + "/10): " + comment;
    }
}

// ============================================================================
// ABSTRACT GAME CLASS
// ============================================================================

// OOP: Abstraction - abstract class defines common behavior
abstract class Game {
    // OOP: Static member - counts total games created
    private static int gameCounter = 0;
    
    // OOP: Encapsulation - private fields
    private String id;
    private String title;
    private String genre;
    private String platform;
    private int estimatedHours;
    private Status status;
    
    // OOP: Composition - Game has Reviews
    private List<Review> reviews;

    // OOP: Constructor overloading - constructor with fewer parameters
    public Game(String title, String genre, String platform, int estimatedHours) {
        this.id = "GAME-" + (++gameCounter);
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.estimatedHours = estimatedHours;
        this.status = Status.WANT_TO_PLAY;
        this.reviews = new ArrayList<>();
    }

    // OOP: Constructor overloading - constructor with all parameters
    public Game(String id, String title, String genre, String platform, 
                int estimatedHours, Status status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.estimatedHours = estimatedHours;
        this.status = status;
        this.reviews = new ArrayList<>();
        gameCounter++;
    }

    // OOP: Abstraction - abstract method must be implemented by subclasses
    public abstract String playMode();

    // OOP: Method that can be overridden
    public String getDescription() {
        return title + " (" + genre + ") on " + platform;
    }

    // OOP: Encapsulation - public getters and setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public int getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(int hours) { this.estimatedHours = hours; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public List<Review> getReviews() { return reviews; }

    // OOP: Static method to access static member
    public static int getTotalGames() {
        return gameCounter;
    }

    // Method to add reviews
    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %dh | Status: %s | Mode: %s",
            id, title, genre, platform, estimatedHours, status, playMode());
    }
}

// ============================================================================
// CONCRETE GAME CLASSES
// ============================================================================

// OOP: Inheritance - SinglePlayerGame extends Game
class SinglePlayerGame extends Game {
    // OOP: Encapsulation - subclass specific field
    private boolean hasStoryMode;

    // OOP: Constructor
    public SinglePlayerGame(String title, String genre, String platform, 
                           int estimatedHours, boolean hasStoryMode) {
        super(title, genre, platform, estimatedHours);
        this.hasStoryMode = hasStoryMode;
    }

    // OOP: Constructor for loading from file
    public SinglePlayerGame(String id, String title, String genre, String platform,
                           int estimatedHours, Status status, boolean hasStoryMode) {
        super(id, title, genre, platform, estimatedHours, status);
        this.hasStoryMode = hasStoryMode;
    }

    // OOP: Method overriding - implements abstract method
    @Override
    public String playMode() {
        return "Single-Player";
    }

    // OOP: Method overriding - overrides parent method
    @Override
    public String getDescription() {
        return super.getDescription() + (hasStoryMode ? " [Story Mode Available]" : "");
    }

    public boolean hasStoryMode() { return hasStoryMode; }
}

// OOP: Inheritance - MultiplayerGame extends Game
class MultiplayerGame extends Game {
    // OOP: Encapsulation - subclass specific field
    private int maxPlayers;

    // OOP: Constructor
    public MultiplayerGame(String title, String genre, String platform,
                          int estimatedHours, int maxPlayers) {
        super(title, genre, platform, estimatedHours);
        this.maxPlayers = maxPlayers;
    }

    // OOP: Constructor for loading from file
    public MultiplayerGame(String id, String title, String genre, String platform,
                          int estimatedHours, Status status, int maxPlayers) {
        super(id, title, genre, platform, estimatedHours, status);
        this.maxPlayers = maxPlayers;
    }

    // OOP: Method overriding - implements abstract method
    @Override
    public String playMode() {
        return "Multiplayer (up to " + maxPlayers + " players)";
    }

    // OOP: Method overriding - overrides parent method
    @Override
    public String getDescription() {
        return super.getDescription() + " [" + maxPlayers + " players max]";
    }

    public int getMaxPlayers() { return maxPlayers; }
}

// ============================================================================
// BACKLOG CLASS
// ============================================================================

// OOP: Aggregation - Backlog contains Games
class Backlog {
    // OOP: Encapsulation - private collection
    private List<Game> games;
    private String filename;

    // OOP: Constructor
    public Backlog(String filename) {
        this.games = new ArrayList<>();
        this.filename = filename;
    }

    // OOP: Method overloading - add game with just the game object
    public void addGame(Game game) {
        games.add(game);
        System.out.println("Added: " + game.getTitle());
    }

    // OOP: Method overloading - add game with parameters
    public void addGame(String title, String genre, String platform, 
                       int hours, String type) {
        Game game;
        if (type.equalsIgnoreCase("single")) {
            game = new SinglePlayerGame(title, genre, platform, hours, true);
        } else {
            game = new MultiplayerGame(title, genre, platform, hours, 4);
        }
        addGame(game);
    }

    public void listAllGames() {
        if (games.isEmpty()) {
            System.out.println("No games in backlog.");
            return;
        }
        System.out.println("\n=== GAME BACKLOG ===");
        for (Game game : games) {
            System.out.println(game);
        }
        System.out.println("Total games: " + Game.getTotalGames());
    }

    // OOP: Method overloading - find by ID
    public Game findGame(String id) {
        for (Game game : games) {
            if (game.getId().equalsIgnoreCase(id)) {
                return game;
            }
        }
        return null;
    }

    // OOP: Method overloading - find by title
    public List<Game> findGamesByTitle(String title) {
        List<Game> found = new ArrayList<>();
        for (Game game : games) {
            if (game.getTitle().toLowerCase().contains(title.toLowerCase())) {
                found.add(game);
            }
        }
        return found;
    }

    public void changeStatus(String id, Status newStatus) {
        Game game = findGame(id);
        if (game != null) {
            game.setStatus(newStatus);
            System.out.println("Updated " + game.getTitle() + " to " + newStatus);
        } else {
            System.out.println("Game not found.");
        }
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Game game : games) {
                String type = (game instanceof SinglePlayerGame) ? "SINGLE" : "MULTI";
                writer.println(game.getId() + "|" + game.getTitle() + "|" + 
                             game.getGenre() + "|" + game.getPlatform() + "|" +
                             game.getEstimatedHours() + "|" + game.getStatus() + "|" + type);
            }
            System.out.println("Backlog saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No existing backlog file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int loaded = 0;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|");
                    if (parts.length < 7) {
                        System.out.println("Skipping malformed line: " + line);
                        continue;
                    }
                    
                    String id = parts[0];
                    String title = parts[1];
                    String genre = parts[2];
                    String platform = parts[3];
                    int hours = Integer.parseInt(parts[4]);
                    Status status = Status.valueOf(parts[5]);
                    String type = parts[6];

                    Game game;
                    if (type.equals("SINGLE")) {
                        game = new SinglePlayerGame(id, title, genre, platform, hours, status, true);
                    } else {
                        game = new MultiplayerGame(id, title, genre, platform, hours, status, 4);
                    }
                    games.add(game);
                    loaded++;
                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
            System.out.println("Loaded " + loaded + " games from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }
}

// ============================================================================
// MAIN APPLICATION
// ============================================================================

public class GameBacklogApp {

    private static Backlog backlog = new Backlog("backlog.txt");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   GAMING BACKLOG TRACKER");
        System.out.println("====================================\n");

        // Load existing backlog
        backlog.loadFromFile();

        // Run OOP demonstration
        runOOPDemo();

        // Main menu loop
        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Add game");
            System.out.println("2. List all games");
            System.out.println("3. Change status");
            System.out.println("4. Save backlog to file");
            System.out.println("5. Load backlog from file");
            System.out.println("6. Run demo again");
            System.out.println("7. Exit (auto-save)");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addGameMenu();
                    break;
                case "2":
                    backlog.listAllGames();
                    break;
                case "3":
                    changeStatusMenu();
                    break;
                case "4":
                    backlog.saveToFile();
                    break;
                case "5":
                    backlog.loadFromFile();
                    break;
                case "6":
                    runOOPDemo();
                    break;
                case "7":
                    backlog.saveToFile();
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void runOOPDemo() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     OOP CONCEPTS DEMONSTRATION         ║");
        System.out.println("╔════════════════════════════════════════╗\n");

        // === ENCAPSULATION ===
        System.out.println("--- OOP DEMO: Encapsulation ---");
        System.out.println("Creating a game with private fields accessed via getters/setters:");
        SinglePlayerGame demoGame = new SinglePlayerGame("The Witcher 3", "RPG", "PC", 100, true);
        System.out.println("  Title (via getter): " + demoGame.getTitle());
        System.out.println("  Genre (via getter): " + demoGame.getGenre());
        demoGame.setStatus(Status.PLAYING);
        System.out.println("  Status changed (via setter): " + demoGame.getStatus());

        // === ABSTRACTION ===
        System.out.println("\n--- OOP DEMO: Abstraction ---");
        System.out.println("Game is an abstract class with abstract method playMode():");
        System.out.println("  Cannot instantiate Game directly, must use subclasses.");
        System.out.println("  Abstract method playMode() is implemented by each subclass.");

        // === INHERITANCE ===
        System.out.println("\n--- OOP DEMO: Inheritance ---");
        System.out.println("SinglePlayerGame and MultiplayerGame inherit from Game:");
        SinglePlayerGame sp = new SinglePlayerGame("Dark Souls", "Action", "PS5", 60, true);
        MultiplayerGame mp = new MultiplayerGame("Overwatch 2", "FPS", "PC", 200, 12);
        System.out.println("  SinglePlayer: " + sp.getDescription());
        System.out.println("  Multiplayer: " + mp.getDescription());

        // === POLYMORPHISM ===
        System.out.println("\n--- OOP DEMO: Polymorphism ---");
        System.out.println("Game references can point to subclass objects:");
        Game g1 = new SinglePlayerGame("Elden Ring", "RPG", "PC", 80, true);
        Game g2 = new MultiplayerGame("Apex Legends", "Battle Royale", "PC", 150, 60);
        System.out.println("  g1.playMode(): " + g1.playMode());  // calls SinglePlayerGame version
        System.out.println("  g2.playMode(): " + g2.playMode());  // calls MultiplayerGame version

        // === INTERFACE ===
        System.out.println("\n--- OOP DEMO: Interface ---");
        System.out.println("RecommendationStrategy interface with multiple implementations:");
        
        // Create demo games for recommendations
        List<Game> demoGames = new ArrayList<>();
        demoGames.add(new SinglePlayerGame("Skyrim", "RPG", "PC", 100, true));
        demoGames.add(new SinglePlayerGame("Fallout 4", "RPG", "PS4", 95, true));
        demoGames.add(new SinglePlayerGame("Portal 2", "Puzzle", "PC", 10, true));
        demoGames.add(new MultiplayerGame("League of Legends", "MOBA", "PC", 500, 10));
        
        Game baseGame = new SinglePlayerGame("Cyberpunk 2077", "RPG", "PC", 90, true);
        
        // OOP: Strategy pattern - using different strategies
        RecommendationStrategy genreStrategy = new GenreBasedStrategy();
        RecommendationStrategy timeStrategy = new TimeBasedStrategy();
        
        System.out.println("  Using GenreBasedStrategy for 'Cyberpunk 2077' (RPG):");
        List<Game> genreRecs = genreStrategy.recommend(demoGames, baseGame);
        for (Game rec : genreRecs) {
            System.out.println("    - " + rec.getTitle());
        }
        
        System.out.println("  Using TimeBasedStrategy for 'Cyberpunk 2077' (90h):");
        List<Game> timeRecs = timeStrategy.recommend(demoGames, baseGame);
        for (Game rec : timeRecs) {
            System.out.println("    - " + rec.getTitle() + " (" + rec.getEstimatedHours() + "h)");
        }

        // === COMPOSITION ===
        System.out.println("\n--- OOP DEMO: Composition/Aggregation ---");
        System.out.println("Backlog HAS-A list of Games (Aggregation):");
        System.out.println("  Backlog contains " + Game.getTotalGames() + " games in total");
        System.out.println("Game HAS-A list of Reviews (Composition):");
        Review review1 = new Review("IGN", 9, "Amazing open world!");
        Review review2 = new Review("GameSpot", 8, "Great story but buggy at launch");
        demoGame.addReview(review1);
        demoGame.addReview(review2);
        System.out.println("  " + demoGame.getTitle() + " has " + demoGame.getReviews().size() + " reviews:");
        for (Review r : demoGame.getReviews()) {
            System.out.println("    " + r);
        }

        // === METHOD OVERLOADING ===
        System.out.println("\n--- OOP DEMO: Method Overloading ---");
        System.out.println("Backlog.addGame() has multiple signatures:");
        System.out.println("  addGame(Game game) - adds a Game object");
        System.out.println("  addGame(String, String, String, int, String) - creates and adds");
        System.out.println("Game constructor has overloaded versions:");
        System.out.println("  Game(title, genre, platform, hours) - basic constructor");
        System.out.println("  Game(id, title, genre, platform, hours, status) - full constructor");

        // === METHOD OVERRIDING ===
        System.out.println("\n--- OOP DEMO: Method Overriding ---");
        System.out.println("getDescription() is overridden in subclasses:");
        System.out.println("  Base: " + new SinglePlayerGame("Base", "RPG", "PC", 50, false).getDescription());
        System.out.println("  Override: " + sp.getDescription());

        // === STATIC MEMBERS ===
        System.out.println("\n--- OOP DEMO: Static Members ---");
        System.out.println("Static counter tracks total games created:");
        System.out.println("  Total games created: " + Game.getTotalGames());
        new SinglePlayerGame("Test Game", "Test", "PC", 1, false);
        System.out.println("  After creating one more: " + Game.getTotalGames());

        System.out.println("\n╚════════════════════════════════════════╝");
        System.out.println("OOP Demo Complete! Proceeding to main menu...\n");
    }

    private static void addGameMenu() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Enter platform: ");
        String platform = scanner.nextLine().trim();
        System.out.print("Enter estimated hours: ");
        int hours = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Type (single/multi): ");
        String type = scanner.nextLine().trim();

        backlog.addGame(title, genre, platform, hours, type);
    }

    private static void changeStatusMenu() {
        System.out.print("Enter game ID: ");
        String id = scanner.nextLine().trim();
        System.out.println("Status options: WANT_TO_PLAY, PLAYING, COMPLETED, ABANDONED");
        System.out.print("Enter new status: ");
        String statusStr = scanner.nextLine().trim().toUpperCase();
        
        try {
            Status status = Status.valueOf(statusStr);
            backlog.changeStatus(id, status);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status.");
        }
    }
}
