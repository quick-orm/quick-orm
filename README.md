# quick-orm
    快速开发框架Quick之数据库操作模块，支持简单高效的数据库操作，脱离繁琐的XML配置，30秒快速上手，帮助开发者专注于业务。支持完全面
    向对象操作、无PO操作、异步执行SQL、SQL执行耗时监控、自动建表、分表操作、spring事务管理等功能。

文档地址：http://quick.zkp.kim<br/>

简单示例:
```java
  //面向对象操作
  User u = new User();
  //获取所有年龄大于18的用户的用户名，且按照id升序排序
  List<User> userList = u.select("username").gt("age",18).orderByAsc("id").list();
  //获取用户名等于zhangsan的用户
  u.get("username","zhangsan");
  //删除用户名等于zhangsan的用户
  u.delete("username","zhangsan");
  ...
```
```java
//无PO操作
Schema schema = Schema.open("t_user");
//获取id等于1的用户的用户名
schema.get("id", "1").getStr("username");
//获取所有username等于zhangsan且年龄大于18的用户
List<Schema> schemaList = schema.eq("username", "zhangsan").gt("age", 18).list();
//分页获取所有username等于zhangsan且年龄大于18的用户,按照id升序排序
Page<Schema> schemaPage = schema.eq("username", "zhangsan").gt("age", 18).orderByAsc("id").page(1,10);
//删除id等于1的用户
schema.delete("id", "1");
...
```
