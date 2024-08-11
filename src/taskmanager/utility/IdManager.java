package taskmanager.utility;

class IdManager {
    private static int id = 1;

    public static int generateId() {
        return id++;
    }
}
