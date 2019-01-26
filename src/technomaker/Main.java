package technomaker;

public class Main {

	public static void main(String[] args) {
		new TechnoMaker().save("test1.mid");
		try {
			Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\" D:\\spigotworkspace\\TenchnoMaker\\test1.mid");
			Thread.sleep(41*1000);
			p = Runtime.getRuntime().exec("taskkill /IM \"vlc.exe\" /F");
		} catch (Exception e) {
			
		}
	}

}
