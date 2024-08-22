package cn.mrxhm.xHMVote2;

import org.bukkit.entity.Player;

@XHMVoteType(type = "kick")
public class KickVote extends Vote {
    public KickVote(Player voter, Player target) {
        super(voter, target);
    }

    @Override
    @VoteExecutor
    public void execute(String... args) {
        setMaxRatio(XHMVote2.instance.readDoubleConfig("vote-setting.kick.ratio"));
        setJudgeInfo(XHMVote2.instance.readListConfig("vote-setting.kick.msg"));
        setVoteInfo(XHMVote2.instance.readListConfig("vote-msg.kick"));
        setReason(String.join("，", args));
        parsePlaceHolders();
        XHMVote2.instance.print(getVoteInfo());

    }

    @Override
    public void judge() {
        XHMVote2.instance.print(getJudgeInfo());
        getTarget().kickPlayer("§c服务器玩家一致决定将您踢出游戏！");
        stop();
    }
}
