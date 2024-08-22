

# 简介

XHMVote2（以下简称xv2）为服务器提供了一套简单的投票系统

玩家可以通过投票来进行很多事

# 命令
/vote <join|stop|投票类型> [参数1] [参数2] [参数3]

/vote freeze [玩家] 冻结自己/玩家

# 权限
|        权限名        |        作用        | 默认给予 |
| :------------------: | :----------------: | :------: |
|   xhmvote2.vote.*    | 投票插件的所有权限 |  管理员  |
| xhmvote2.vote.create |      创建投票      |  管理员  |
|  xhmvote2.vote.join  |      参与投票      |  所有人  |
|  xhmvote2.vote.stop  |      停止投票      |  管理员  |
| xhmvote2.vote.bypass |      投票豁免      |  管理员  |
|   xhmvote2.freeze    |      冻结玩家      |  管理员  |

# 配置文件

```yaml
# 插件前缀（但是没有任何用）
prefix: '&8[&aXHMVote2&8]'
# 边框
side: '&9+-----------------------------+'
# 参与投票提示
help: '&a输入/vote join %uuid%参与投票'
# 投票设置
# ratio：当服务器内参与玩家到达多少比例后执行该投票
# msg：投票执行时的信息
# 可用占位符（暂不支持PlaceHolderAPI）
# %voter%：投票发起者
# %target%：目标玩家名
# %max_ratio%：当前比率
# %side%：上方配置边框
# %help%：上方配置帮助信息
# %uuid%：投票id
# %reason%：投票理由
vote-setting:
  kick:
    ratio: 0.6
    msg:
      - '&4%target% &c被群众的怒火淹没！'
  kill:
    ratio: 0.5
    msg:
      - '&4%target% &4遭受天谴！'
  freeze:
    ratio: 0.4
    msg:
      - '&4%target% &b被冰封！'
  # 其他
# 投票发起时的信息，占位符通用
vote-msg:
  kick:
    - '%side%'
    - '&a%voter% &d发起了一轮对 &c%target% &d的投票'
    - '&b投票类型：%type%'
    - '&d投票理由：%reason%'
    - '&6裁决比例：%max_ratio%'
    - '%help%'
    - '%side%'
  kill:
    - '%side%'
    - '&a%voter% &d发起了一轮对 &c%target% &d的投票'
    - '&b投票类型：%type%'
    - '&d投票理由：%reason%'
    - '&6裁决比例：%max_ratio%'
    - '%help%'
    - '%side%'
  freeze:
    - '%side%'
    - '&a%voter% &d发起了一轮对 &c%target% &d的投票'
    - '&b投票类型：%type%'
    - '&d投票理由：%reason%'
    - '&6裁决比例：%max_ratio%'
    - '%help%'
    - '%side%'
  # 其他
```

# 如何自定义一个投票

* 首先，在IDEA创建一个spigot插件

* 其次，打开plugin.yml，加上` softdepend: [XHMVote2]`

* 将本插件下载并添加到您项目的库

* 新建一个类，并继承Vote，以下为示例：

* ```Java
  //别忘了这个注解，type是投票类型，随便填，会出现在/vote后面的tab补全中
  @XHMVoteType(type = "tnt")
  public class TNTVote extends Vote {
      //默认构造器，非特殊情况不要动
      public TNTVote(Player voter, Player target) {
          super(voter, target);
      }
  
      //投票发起时执行
      @Override
      //别忘了这个注解
      @VoteExecutor
      //args：玩家输入的参数
      public void execute(String... args) {
          //设置投票最大比率
          setMaxRatio(0.5);
          //投票满足后发送的消息，字符串列表
          setJudgeInfo(Arrays.asList("1", "2", "3"));
          //投票发起时发送的消息，字符串列表
          setVoteInfo(Arrays.asList("4", "5", "6"));
          //设置理由，可选
          setReason(String.join("，", args));
          //替换所有占位符，别忘了这个
          parsePlaceHolders();
          //打印投票发起时的信息
          XHMVote2.instance.print(getVoteInfo());
  
      }
  
      //当投票满足条件后执行
      @Override
      public void judge() {
          XHMVote2.instance.print(getJudgeInfo());
          getTarget().getWorld().spawnEntity(getTarget().getLocation(), EntityType.PRIMED_TNT);
          stop();
      }
  }
  ```

* 接着，打开插件主类，在onEnable方法中添加如下代码：

* ```java
  @Override
  public void onEnable() {
      //获取插件示实例
      XHMVote2 xhmVote2 = (XHMVote2) (getServer().getPluginManager().getPlugin("XHMVote2"));
      //判断插件是否加载
      if (xhmVote2 != null) {
          //加载您的VoteType
          getLogger().info("§bXHMVote2 found!");
          xhmVote2.registerVoteType(TNTVote.class);
          getLogger().info("§aLoading TNTVote successfully!");
      } else {
          //可选，输出插件未找到的信息
          getLogger().info("§cXHMVote2 not found");
      }
      // Plugin startup logic
  }
  ```

* 接着，将插件打包（注意不要把XHMVote2一起打包进去），将插件放到服务端，测试效果
