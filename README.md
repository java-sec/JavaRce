# JavaRceDemo

By. Whoopsunix

# 0x00 do what?

🚀 记录贴 对照实战场景梳理较通用的 Java Rce 相关漏洞的利用方式或知识点

🚩 对于实际环境遇到过的组件如有必要会针对可利用版本进行一个梳理 慢更 

🚧 长期项目 不定期学习后更新......

🛰️ 部分利用已经集成在二开 [ysoserial](https://github.com/Whoopsunix/ysoserial) 项目中

🪝 [PPPRASP](https://github.com/Whoopsunix/PPPRASP) 项目中对本项目给出的漏洞实现防护（仅实现关键函数的 HOOK，不作进一步处理）

## 目录

- [0x01 RceEcho](#0x01-rceecho)
    - [Tomcat](#tomcatecho)
    - [Spring](#springecho)
    - [Jetty](#jettyecho)
    - [Undertow](#undertowecho)
    - [Resin](#resinecho)
    - [OS](#osecho)
- [0x02 MemShell](#0x02-memshell)
    - [TomcatMemShell](#tomcatmemshell)
- [0x03 命令执行](#0x03-command)
  - [执行Demo，java jsp](#执行demo)
  - [执行结果输出（InputStream 处理Demo）](#执行结果输出inputstream-处理demo)
- [0x04 表达式注入](#0x04-expression-inject)
  - [OGNL](#ognl)
  - [EL](#el)
  - [SPEL](#spel)
- [0x05 JDBC Attack](#0x05-jdbc-attack)
  - [Mysql](#mysql)
  - [PostgreSQL](#postgresql)
  - [H2database](#h2database)
  - [IBM DB2](#ibmdb2)
  - [ModeShape](#modeshape)
  - [Apache Derby](#apache-derby)
  - [Sqlite](#sqlite)
- [0x06 Serialization](#0x06-serialization)
  - [类加载](#class-load)
  - [XMLSerialization](#xmlserialization)
    - [JavaBean](#jarbean)
    - [XStream](#xstream)
  - [构造方法利用](#constructorexp)
- [0x07 文件读写 Demo](#0x07-文件读写-demo)
- [鸣谢](#Thanks)

# 0x01 [RceEcho](RceEcho)

结合 [java-object-searcher](https://github.com/c0ny1/java-object-searcher) 工具挖掘命令回显 慢更版本适配

本项目主要给出反序列化 demo，jsp 的例子可以参考 [Java-Rce-Echo](https://github.com/feihong-cs/Java-Rce-Echo)

## [TomcatEcho](RceEcho/TomcatEcho)

Version Test

- 6.0.53
- 7.0.59、7.0.109
- 8.0.53、8.5.82
- 9.0.65

## [SpringEcho](RceEcho/SpringEcho)

Version Test

- spring-boot-starter-web
  - [2.2.x, 2.7.x]

## [JettyEcho](RceEcho/JettyEcho)

Version Test

- 7.x、8.x、9.x、10.x 全版本

## [UndertowEcho](RceEcho/UndertowEcho)

WildFly 默认容器用的 Undertow

Version Test

- spring-boot-starter-undertow
  - 2.7.15

## [ResinEcho](RceEcho/ResinEcho)

Version Test

- [4.0.52, 4.0.66]

## [OSEcho](RceEcho/OSEcho)

- windows
- linux

# 0x02 [MemShell](MemShell)

## [TomcatMemShell](MemShell/TomcatMemShell)

| Tomcat     |                    |          |
| ---------- | ------------------ | -------- |
| 内存马类型 | Loader             | 测试版本 |
| Filter     | ContextClassLoader | 8 9      |
|            | JMX                | 7 8 9    |
|            | Thread             | 6 7 8 9  |
| Servlet    | ContextClassLoader | 8 9      |
|            | JMX                | 7 8 9    |
|            | Thread             | 7 8 9    |
| Listener   | ContextClassLoader | 8 9      |
|            | JMX                | 7 8 9    |
|            | Thread             | 7 8 9    |

## [SpringMemShell](MemShell/SpringMemShell)

| Springboot2 |                       |                |
| ----------- | --------------------- | -------------- |
| 内存马类型  | Loader                | 测试版本       |
| Controller  | WebApplicationContext | [2.2.x, 2.7.x] |

## [UndertowMemShell](MemShell/UndertowMemShell)

| Undertow   |        |          |
| ---------- | ------ | -------- |
| 内存马类型 | Loader | 测试版本 |
| Listener   | Thread | 2.7.15   |

## [ResinMemShell](MemShell/ResinMemShell)

| 内存马类型 | Loader | 测试版本         |
| ---------- | ------ | ---------------- |
| Listener   | Thread | [4.0.52, 4.0.66] |
| Servlet    | Thread | [4.0.52, 4.0.66] |
| Filter     | Thread | [4.0.52, 4.0.66] |

# 0x03 [Command](Command)

## [执行Demo](Command)

参考 [javaweb-sec](https://github.com/javaweb-sec/javaweb-sec) 有很详细的例子

- Runtime
- ProcessBuilder
- ProcessImpl
- ProcessImpl & UnixProcess
- ProcessImpl & UnixProcess by unsafe - Native
- Thread
- ScriptEngine
- jni

## [执行结果输出（InputStream 处理Demo）](Command)

- java.lang.StringBuilder
- java.io.ByteArrayOutputStream
- java.util.Scanner
- java.io.BufferedReader
- java.io.InputStream.readNBytes > JDK 9
- org.springframework:spring-core
- org.apache.commons:commons-io

# 0x04 [Expression inject](Expression)

## [OGNL](Expression/OGNLAttack)

- 普通执行demo、jsEngine：get、set方式
- 有sout的回显 (Ps. 通过 Servlet 的回显移到 RceEcho 章节介绍)
  - 明文
  - 套一层base64加密
- 探测用Payload
  - DNSLOG、HTTPLOG
  - 延时

## [EL](Expression/ELAttack)

- runtime 回显
- jsEngine 回显
- Scriptlet 标记写法（放在这里对照）

## [SPEL](Expression/SPELAttack)

- runtime 回显
- 探测用Payload
  - DNSLOG、HTTPLOG
  - 延时

# 0x05 [JDBC Attack](JDBCAttack)

参考 [JDBC-Attack](https://github.com/su18/JDBC-Attack) 、[pyn3rd blog](https://pyn3rd.github.io/) 、[A New Attack Interface In Java Applications](https://i.blackhat.com/Asia-23/AS-23-Yuanzhen-A-new-attack-interface-in-Java.pdf) 、 [Deserial_Sink_With_JDBC](https://github.com/luelueking/Deserial_Sink_With_JDBC)
- Mysql
    - 文件读取
    - 反序列化
        - statementInterceptors、detectCustomCollations
- PostgreSQL
  - CVE-2022-21724 RCE
    - AbstractXmlApplicationContext 实现类
  - 文件写入
    - loggerLevel / loggerFile
      - 原始方式写入 EL
      - 截断方式写入 jsp
- H2database
  - RUNSCRIPT 远程sql加载
  - 代码执行
      - INIT转义分号
      - TriggerJS
      - Groovy
- IBMDB2
  - JNDI
- ModeShape
  - JNDI
- Apache Derby
  - Serialize
- Sqlite
  - RCE
- dameng 达梦
  - JDNI
- Oracle
  - JNDI
- teradata
  - JDBC RCE

# 0x06 [Serialization](Serialization)

## [Class load](Serialization/ClassLoad)

- AppClassLoader
- URLCLassLoader
- BCEL
- TransletClassLoader
- Unsafe
- ReflectUtils
- RhinoClassloader
- ScriptEngineDemo

## [XMLSerialization](Serialization/XMLSerialization)

### [JarBean](Serialization/XMLSerialization/JavaBean)

- 命令执行 Runtime、ProcessBuilder、js
- 探测用Payload
    - DNSLOG、SOCKETLOG
    - 延时
- JNDI
- BCEL
- RemoteJar

## [ConstructorEXP](Serialization/ConstructorEXP)

通过构造方法触发RCE

- xml

### XStream

主要为 CVE 不具体展开，<= 1.4.17 的生成集成在 yso 项目中

# 0x07 [文件读写 Demo](FilesOperations)

可用的文件读写方法，即 Java 数据流的各种操作方法

# Stats

![Alt](https://repobeats.axiom.co/api/embed/818a4d2c0d1562eec751b2637b825b3b0d2cf0e3.svg "Repobeats analytics image")

# Thanks

感谢师傅们的研究 带来了很大的帮助 :)

> https://github.com/javaweb-sec/javaweb-sec
>
> https://github.com/yzddmr6/Java-Js-Engine-Payloads
>
> https://github.com/su18/JDBC-Attack
>
> https://pyn3rd.github.io/
>
> https://forum.butian.net/share/886
>
> https://github.com/woodpecker-appstore
>
> https://www.yulegeyu.com/archives/
>
> https://github.com/c0ny1/java-object-searcher
>
> https://github.com/feihong-cs/Java-Rce-Echo
>
> https://flowerwind.github.io/2021/10/11/tomcat6%E3%80%817%E3%80%818%E3%80%819%E5%86%85%E5%AD%98%E9%A9%AC/
>
> https://github.com/luelueking/Deserial_Sink_With_JDBC

[//]: # ([![Stargazers over time]&#40;https://starchart.cc/Whoopsunix/JavaRce.svg&#41;]&#40;https://starchart.cc/Whoopsunix/JavaRce&#41;)
