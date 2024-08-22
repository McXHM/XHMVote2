package cn.mrxhm.xHMVote2;

import org.bukkit.Bukkit;

public class Debug {
    public static int d = 0;

    public static void print(String str) {
        System.out.println(str);
        Bukkit.broadcastMessage(str);
    }

    public static void print(int i) {
        print(d++ + ". " + i);
    }

    public static void print() {
        print(d++);
    }
}
