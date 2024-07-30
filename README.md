# JavaDB
JavaDB 是一个 Java 实现的简单的数据库，原理参照自 MySQL以及 @qw4990 大佬的数据库项目：NYADB2。实现了以下功能：

- 数据的可靠性和数据恢复
- 两段锁协议（2PL）实现可串行化调度
- MVCC
- 两种事务隔离级别（读提交和可重复读）
- 死锁处理
- 简单的表和字段管理
- 简陋的 SQL 解析（因为懒得写词法分析和自动机，就弄得比较简陋）
- 基于 socket 的 server 和 client



## 运行方式

注意首先需要在 pom.xml 中调整编译版本，如果导入 IDE，请更改项目的编译版本以适应你的 JDK

首先执行以下命令编译源码：

```
mvn compile
```

接着执行以下命令以 /tmp/javadb （这里为示例路径，具体路径以个人实际路径为准）作为路径创建数据库：

```
mvn exec:java -Dexec.mainClass="top.lzxu.javadb.backend.Launcher" -Dexec.args="-create /tmp/javadb"
```

随后通过以下命令以默认参数启动数据库服务：

```
mvn exec:java -Dexec.mainClass="top.lzxu.javadb.backend.Launcher" -Dexec.args="-open /tmp/javadb"
```

这时数据库服务就已经启动在本机的 9999 端口。重新启动一个终端，执行以下命令启动客户端连接数据库：

```
mvn exec:java -Dexec.mainClass="top.lzxu.javadb.client.Launcher"
```

会启动一个交互式命令行，就可以在这里输入类 SQL 语法，回车会发送语句到服务，并输出执行的结果。
![image](https://github.com/swdyqw/JavaDB/assets/155432770/80ef7c0f-4c6d-4d0c-a2cd-c5ceea607776)

