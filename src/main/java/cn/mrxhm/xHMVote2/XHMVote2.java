package cn.mrxhm.xHMVote2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class XHMVote2 extends JavaPlugin {
    public static XHMVote2 instance;
    public static HashMap<String, Class<?>> voteTypes = new HashMap<>();
    public static List<Vote> votes = new ArrayList<>();

    @Override
    public void onEnable() {
        welcome();
        getCommand("vote").setExecutor(new MainCommand());
        getCommand("freeze").setExecutor(new Freeze());
        getServer().getPluginManager().registerEvents(new Freeze(), this);
        instance = this;
        // Plugin startup logic
        registerVoteType(KickVote.class);
        registerVoteType(KillVote.class);
        registerVoteType(FreezeVote.class);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerVoteType(Class<?> voteClass) {
        if (voteClass.isAnnotationPresent(XHMVoteType.class)) {
            for (Method m : voteClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(VoteExecutor.class)) {
                    voteTypes.put(voteClass.getAnnotation(XHMVoteType.class).type(), voteClass);
                }
            }
        }
    }

    /**
     * 发起一轮投票
     *
     * @param vote 一个投票
     */
    public static void voting(Vote vote) {

    }

    public String readStringConfig(String path) {
        return getConfig().getString(path);
    }

    public int readIntConfig(String path) {
        return getConfig().getInt(path);
    }

    public double readDoubleConfig(String path) {
        return getConfig().getDouble(path);
    }

    public List<String> readListConfig(String path) {
        return getConfig().getStringList(path);
    }

    public void print(String msg) {
        Bukkit.broadcastMessage(msg.replaceAll("&", "§"));
    }

    public void print(List<String> stringList) {
        for (String s : stringList) {
            print(s);
        }
    }

    public void welcome() {
        System.out.println("""
                 
                 __   __      __  __                  __  __     _____       ______    ____         ___    \s
                /\\ \\ /\\ \\    /\\ \\/\\ \\     /'\\_/`\\    /\\ \\/\\ \\   /\\  __`\\    /\\__  _\\  /\\  _`\\     /'___`\\  \s
                \\ `\\`\\/'/'   \\ \\ \\_\\ \\   /\\      \\   \\ \\ \\ \\ \\  \\ \\ \\/\\ \\   \\/_/\\ \\/  \\ \\ \\L\\_\\  /\\_\\ /\\ \\ \s
                 `\\/ > <      \\ \\  _  \\  \\ \\ \\__\\ \\   \\ \\ \\ \\ \\  \\ \\ \\ \\ \\     \\ \\ \\   \\ \\  _\\L  \\/_/// /__\s
                    \\/'/\\`\\    \\ \\ \\ \\ \\  \\ \\ \\_/\\ \\   \\ \\ \\_/ \\  \\ \\ \\_\\ \\     \\ \\ \\   \\ \\ \\L\\ \\   // /_\\ \\
                    /\\_\\\\ \\_\\   \\ \\_\\ \\_\\  \\ \\_\\\\ \\_\\   \\ `\\___/   \\ \\_____\\     \\ \\_\\   \\ \\____/  /\\______/
                    \\/_/ \\/_/    \\/_/\\/_/   \\/_/ \\/_/    `\\/__/     \\/_____/      \\/_/    \\/___/   \\/_____/\s
                                                                                                           \s
                                                                                                           \s""");
    }
}
