myNews
======

this is my news for rss reader 

知乎机器人源码JAVA重量级版正式放到Github上面了，Git地址：https://github.com/yuercl/myNews

整个知乎机器人构建于Springside4，详情参考：https://github.com/springside/springside4

玩玩须知：

第一步 下载源码：git下来
第二步 依赖：jdk版本大于6，本机安装maven，并配置好本地仓库，mysql数据库
第三部 config：
                       config.properties，中按要求设置用户名和密码，
                       application.properties，数据库用户名和密码
                       hibernate.cfg.xml，数据库信息，用户自动生成数据库，自己导入sql也行，那么这个文件就没用了
                       applicationContext.xml，设置crontab，发布频率
第四步 run：运行jetty脚本，等待吧，第一次运行会下载依赖包。                

eclipse_rebuild.bat 用于生成eclipse项目
jetty.bat 内嵌jetty容器，喜欢tomcat的走开
dependency.bat 依赖
package.bat 打包成war，可用户发布到tomcat、jboss等
refresh-db.bat 用户刷新数据库，一般不怎么用，也就是执行scheme下面的sql文件，在pom.xml里面有配置。
