### 监控东航余票 随心飞

#### 介绍

随心飞总是抢不到，用这个脚本就可以监控航班余票，第一时间占座。

可配置监控多个航段

#### 原理

爬取东航官网搜索航班接口，并以邮件通知用户。爬取的是东航web端的接口，东航手机端有5-10分钟的延迟，所以web端放票后，在手机端不停刷票即可。注意频率，频率过快会被认定为爬虫被ban。

#### 使用

1. 运行环境

   1. Java运行环境
   2. 本地安装 [ChromeDriver](https://chromedriver.chromium.org/) 和 [Chrome](https://www.google.com/intl/zh-CN/chrome/)浏览器，两者版本需要保持一致
   3. 把 [donghang.jar](https://github.com/TonyPhoneix/donghang/blob/master/donghang-1.0-SNAPSHOT.jar)下载到本地

2. 在donghang.jar的目录新建文件config.json

   支持配置多个航段，一个航段可配置搜索多个航班号

   ```json
   {
     //航段config
     "config": [
       {
         "email": "13093687239@163.com",//接受者邮箱
         "name": "xxx", //接受者姓名
         "segmentList": [ //航段
           {
             "arrCd": "KWE", //到达地代码
             "arrCdTxt": "贵阳", //到达地
             "arrCityCode": "KWE", //到达地城市代码
             "deptCd": "SHA", //出发地代码
             "deptCdTxt": "上海", //出发地
             "deptCityCode": "SHA",//出发地代码
             "deptDt": "2020-12-31", //出发日期
             "flightNos": [
               "FM9459" //航班号
             ]
           }
         ]
       }
     ],
     "sender": "13093687239@163.com", //发送者邮箱
     "code": "xxx" //发送者邮箱授权码code
   }
   ```

   **出发地和到达地代码**

   ![image-20201225145653374](https://tva1.sinaimg.cn/large/0081Kckwgy1gm043nfqs6j316u0mxn1r.jpg)

**邮箱授权码获取**

![image-20201225145810684](https://tva1.sinaimg.cn/large/0081Kckwgy1gm044ta6kcj30ur0cgq4z.jpg)

3. run命令

   ```shell
   java -jar donghang-1.0-SNAPSHOT.jar
   ```

4. 如果有服务器的话，可以部署到服务器上，写一个启动脚本配合crontab使用，我自己配置了每三分钟run一次

   ```java
   */3 * * * * . ~/.profile;~/donghang/config.json;/root/donghang/start.sh > /root/donghang/info.log 2>&1 &g
   ```

   


