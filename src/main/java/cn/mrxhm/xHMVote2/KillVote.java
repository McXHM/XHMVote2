package cn.mrxhm.xHMVote2;


import org.bukkit.entity.Player;
/**
 * @author Mr_XHM
 */
@XHMVoteType(type = "kill")
public class KillVote extends Vote {

    public KillVote(Player voter, Player target) {
        super(voter, target);
    }

    @Override
    @VoteExecutor
    public void execute(String... args) {
        setMaxRatio(XHMVote2.instance.readDoubleConfig("vote-setting.kill.ratio"));
        setJudgeInfo(XHMVote2.instance.readListConfig("vote-setting.kill.msg"));
        setVoteInfo(XHMVote2.instance.readListConfig("vote-msg.kill"));
        setReason(String.join("，", args));
        parsePlaceHolders();
        XHMVote2.instance.print(getVoteInfo());

    }

    @Override
    public void judge() {
        XHMVote2.instance.print(getJudgeInfo());
        getTarget().setHealth(0);
        stop();
    }
}
