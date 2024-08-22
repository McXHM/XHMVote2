package cn.mrxhm.xHMVote2;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainCommand implements CommandExecutor, TabCompleter {
    CommandSender cs;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        cs = sender;
        if (sender instanceof Player) {
            if (args.length >= 2) {
                if (!args[0].equalsIgnoreCase("join") && hasPerm("xhmvote2.vote.create")) {
                    Player voter = (Player) sender;
                    String type = args[0];
                    Player target = Bukkit.getPlayer(args[1]);
                    if (!isVoteExist(voter)) {
                        if (!target.hasPermission("xhmvote2.vote.bypass")) {
                            if (voter != null && target != null && XHMVote2.voteTypes.containsKey(type)) {
                                try {
                                    Class<?> voteClass = XHMVote2.voteTypes.get(type);
                                    Vote v = (Vote) voteClass.getConstructor(Player.class, Player.class).newInstance(voter, target);
                                    v.execute(Arrays.copyOfRange(args, 2, args.length));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sender.sendMessage("§c您无法对该玩家投票！");
                        }
                    } else {
                        sender.sendMessage("§c请等待您的投票结束！");
                    }
                } else if (hasPerm("xhmvote2.vote.join")) {
                    if (args[0].equalsIgnoreCase("join")) {
                        UUID voteId = UUID.fromString(args[1]);
                        if (isVoteExist(voteId)) {
                            Vote v = getVote(voteId);
                            if (v.addVoter((Player) sender)) {
                                sender.sendMessage("§7投票成功");
                                sender.sendMessage("§7当前比率：§f" + v.countRatio()*100.0+"%");
                            } else {
                                sender.sendMessage("§c投票失败，您已参与此投票");
                            }
                        }
                    }
                } else if (hasPerm("xhmvote2.vote.stop")) {
                    if (args[0].equalsIgnoreCase("stop")) {
                        UUID voteId = UUID.fromString(args[1]);
                        if (isVoteExist(voteId)) {
                            Vote v = getVote(voteId);
                            v.stop();
                        }
                    }
                } else {
                    sender.sendMessage("§c权限不足");
                }
            } else {
                sender.sendMessage("§c语法错误");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> sl = new ArrayList<>();
            sl.addAll(XHMVote2.voteTypes.keySet());
            sl.add("join");
            sl.add("stop");
            return sl;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                return uuidList();
            } else if (args[0].equalsIgnoreCase("stop")) {
                return uuidList();
            }
        }
        return playerList();
    }

    public List<String> uuidList() {
        List<String> sl = new ArrayList<>();
        for (Vote v : XHMVote2.votes) {
            sl.add(v.getVoteId().toString());
        }
        return sl;
    }

    public List<String> playerList() {
        List<String> sl = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            sl.add(p.getName());
        }
        return sl;
    }

    public boolean isVoteExist(UUID voteId) {
        for (Vote v : XHMVote2.votes) {
            if (v.getVoteId().equals(voteId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVoteExist(Player voter) {
        for (Vote v : XHMVote2.votes) {
            if (v.getVoter().equals(voter)) {
                return true;
            }
        }
        return false;
    }

    public Vote getVote(UUID voteId) {
        for (Vote v : XHMVote2.votes) {
            if (v.getVoteId().equals(voteId)) {
                return v;
            }
        }
        return null;
    }

    public boolean hasPerm(String perm) {
        return cs.hasPermission(perm);
    }
}
