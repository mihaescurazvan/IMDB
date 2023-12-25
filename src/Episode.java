public class Episode {
    public String episodeName;
    public String duration;
    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }
    public void displayInfo() {
        System.out.println("Episode: " + this.episodeName);
        System.out.println("Duration: " + this.duration);
    }
    public String getDisplayInfo() {
        String info = "";
        info += "Episode: " + this.episodeName + "\n";
        info += "Duration: " + this.duration + "\n";
        return info;
    }
}