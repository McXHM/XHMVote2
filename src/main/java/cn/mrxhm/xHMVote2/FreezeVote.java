package cn.mrxhm.xHMVote2;


import org.bukkit.entity.Player;

/**
 * @author Mr_XHM
 */
@XHMVoteType(type = "freeze")
public class FreezeVote extends Vote {
    public FreezeVote(Player voter, Player target) {
        super(voter, target);
    }

    @Override
    @VoteExecutor
    public void execute(String... args) {
        setMaxRatio(XHMVote2.instance.readDoubleConfig("vote-setting.freeze.ratio"));
        setJudgeInfo(XHMVote2.instance.readListConfig("vote-setting.freeze.msg"));
        setVoteInfo(XHMVote2.instance.readListConfig("vote-msg.freeze"));
        setReason(String.join("，", args));
        parsePlaceHolders();
        XHMVote2.instance.print(getVoteInfo());

    }

    @Override
    public void judge() {
        XHMVote2.instance.print(getJudgeInfo());
        Freeze.freeze(getTarget());
        getTarget().sendMessage("§b你被冻结了！");
        stop();
    }
}
