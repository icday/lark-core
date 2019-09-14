[TOC]

# lark
一个可扩展的应用内嵌Telnet控制台。

使用**lark**，你可以*attach*一个运行中的Java应用，执行一些预定义的命令或者一个任意的代码片段（*Groovy*代码）。

# Getting Started

## Embed it

支持提供多种应用类型的接入。

### spring-boot

如果是一个spring boot应用，可以直接使用[lark-spring-boot-starter](https://github.com/icday/lark-extensions/tree/master/lark-spring-boot-starter)：
```xml
<dependency>
  <groupId>com.daiyc.lark</groupId>
  <artifactId>lark-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

引入**lark-spring-boot-starter**后，默认是开启lark服务的，可以通过`lark.enable=false`配置关闭。
可用的配置项包括：
- `lark.server.host` 默认`127.0.0.1`
- `lark.server.port` 默认`8989`
- `lark.console.prompt` 默认为：`lark > `

### spring应用

对于一个spring应用，可以使用**com.daiyc.lark:lark-spring**：

```xml
<dependency>
  <groupId>com.daiyc.lark</groupId>
  <artifactId>lark-spring</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

然后将`LarkServerBean`注册到容器中，完成。

### 其他
其他情况可以直接使用`com.daiyc.lark:lark-core`：
```xml
<dependency>
  <groupId>com.daiyc.lark</groupId>
  <artifactId>lark-core</artifactId>
  <version>0.1.3-RELEASE</version>
</dependency>
```
使用`TelnetServer`启动控制台的开启关闭。

## Use it

现在可以使用Telnet连接控制台`telnet 127.0.0.1 8989`。在控制台执行命令。

# 命令（Command）

`lark-core`中默认提供了一些内置的命令。
借助Java的SPI机制，你也可以定制自己的命令（实现`com.daiyc.lark.core.command.Command`接口）。

例如[**lark-dubbo**](https://github.com/icday/lark-extensions/tree/master/lark-dubbo)重新实现了**Dubbo**的`invoke`、`ls`等命令。

## `help`命令

`help Command`

## `groovy`命令

`groovy`命令打开一个groovy的**REPL**解释器。
REPL中可以引用应用中的静态方法和静态数据，也可以通过Java SPI的方式扩展，向执行上下文中添加变量。

[**lark-spring-boot-starter**](https://github.com/icday/lark-extensions/tree/master/lark-spring-boot-starter)中向执行上下文注入了一个`ctx`变量指向Spring容器的`ApplicationContext`。

### 添加上下文变量

1. 实现`GroovyContextVariable`接口。
2. 把实现类的全限定名填到到`META-INF/services/com.daiyc.lark.core.groovy.GroovyContextVariable`文件中。

# 运行效果

# 扩展