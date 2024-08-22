package cn.mrxhm.xHMVote2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XHMVoteType(type = "test")
public class Vote {
    //发起投票者
    final Player voter;
    //被投票者
    final Player target;
    //投票ID
    final UUID voteId;
    //投票比率
    double ratio = 0.0;
    //裁决比率
    double maxRatio = 0.5;
    //投票信息
    List<String> voteInfo = new ArrayList<String>();
    //裁决信息
    List<String> judgeInfo = new ArrayList<String>();
    //投票者列表
    List<Player> voters = new ArrayList<Player>();

    public Vote(Player voter, Player target) {
        this.voter = voter;
        this.target = target;
        this.voteId = UUID.randomUUID();
        XHMVote2.votes.add(this);
    }

    public boolean addVoter(Player voter) {
        if (voters.contains(voter)) {
            return false;
        } else {
            voters.add(voter);
            setRatio(countRatio());
            getVoter().sendMessage("§7玩家§f" + voter.getName() + "§7投票成功" + "，当前比率：§6" + countRatio() * 100.0 + " §d%");
            if (ratio >= maxRatio) {
                judge();
            }
            return true;
        }
    }

    public void judge() {
    }

    public boolean removeVoter(Player voter) {
        return voters.remove(voter);
    }

    @VoteExecutor
    public void execute(String... args) {
        Bukkit.broadcastMessage("投票执行了");
    }

    public Player getVoter() {
        return voter;
    }

    public Player getTarget() {
        return target;
    }

    public UUID getVoteId() {
        return voteId;
    }

    public List<Player> getVoters() {
        return voters;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double getMaxRatio() {
        return maxRatio;
    }

    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    public List<String> getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(List<String> voteInfo) {
        this.voteInfo = voteInfo;
    }

    public List<String> getJudgeInfo() {
        return judgeInfo;
    }

    public void setJudgeInfo(List<String> judgeInfo) {
        this.judgeInfo = judgeInfo;
    }

    public void parsePlaceHolders() {
        parsePlaceHolders(voteInfo);
        parsePlaceHolders(judgeInfo);
    }

    public void parsePlaceHolders(List<String> list) {
        for (String s : list) {
            list.set(list.indexOf(s), parsePlaceHolders(s));
        }
    }

    public String parsePlaceHolders(String s) {
        return s.replaceAll("%voter%", voter.getName())
                .replaceAll("%target%", target.getName())
                .replaceAll("%ratio%", String.valueOf(ratio))
                .replaceAll("%max_ratio%", String.valueOf(maxRatio))
                .replaceAll("%type%", this.getClass().getAnnotation(XHMVoteType.class).type())
                .replaceAll("%help%", XHMVote2.instance.readStringConfig("help").replaceAll("%uuid%", getVoteId().toString()))
                .replaceAll("%side%", XHMVote2.instance.readStringConfig("side"));
    }

    public void setReason(String reason) {
        setReason(reason, voteInfo);
        setReason(reason, judgeInfo);
    }

    public void setReason(String reason, List<String> list) {
        for (String s : list) {
            list.set(list.indexOf(s), s.replaceAll("%reason%", reason));
        }
    }

    public double countRatio() {
        return (double) voters.size() / (double) XHMVote2.instance.getServer().getOnlinePlayers().size();
    }

    public void stop() {
        XHMVote2.instance.print("§c投票已结束");
        XHMVote2.votes.remove(this);
    }
}
