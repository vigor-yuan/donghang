### Monitor Eastern Airlines Ticket Availability - Fly at Your Will

#### Introduction

Always having trouble grabbing tickets with "随心飞" (Suixinfei)? With this script, you can monitor the remaining seats of a flight and secure your reservation as soon as they become available.

Configurable to monitor multiple flight segments.

#### Principle

It crawls the Eastern Airlines official website's flight search interface and notifies users via email. It scrapes data from the web version, as the mobile version has a 5-10 minute delay. So, after tickets are released on the web version, continuously refreshing the mobile version will do the trick. Be cautious about the frequency; if it's too fast, it may be flagged as a web scraper and get banned.

#### Usage

1. Runtime Environment

   1. Java runtime environment.
   2. Locally install [ChromeDriver](https://chromedriver.chromium.org/) and [Chrome](https://www.google.com/intl/zh-CN/chrome/) browser, ensuring both versions match.
   3. Download [donghang.jar](https://github.com/TonyPhoneix/donghang/blob/master/donghang-1.0-SNAPSHOT.jar) to your local machine.

2. Create a config.json file in the same directory as donghang.jar.

   Supports configuring multiple flight segments, and each segment can have multiple flight numbers to monitor.

   ```json
   {
     // Segment config
     "config": [
       {
         "email": "13093687239@163.com", // Receiver's email
         "name": "xxx", // Receiver's name
         "segmentList": [ // Flight segments
           {
             "arrCd": "KWE", // Arrival city code
             "arrCdTxt": "Guiyang", // Arrival city
             "arrCityCode": "KWE", // Arrival city code
             "deptCd": "SHA", // Departure city code
             "deptCdTxt": "Shanghai", // Departure city
             "deptCityCode": "SHA", // Departure city code
             "deptDt": "2020-12-31", // Departure date
             "flightNos": [
               "FM9459" // Flight number
             ]
           }
         ]
       }
     ],
     "sender": "13093687239@163.com", // Sender's email
     "code": "xxx" // Sender's email authorization code
   }
   ```

   **Departure and Arrival City Codes**

   ![image-20201225145653374](https://tva1.sinaimg.cn/large/0081Kckwgy1gm043nfqs6j316u0mxn1r.jpg)

   **How to Get Email Authorization Code**

   ![image-20201225145810684](https://tva1.sinaimg.cn/large/0081Kckwgy1gm044ta6kcj30ur0cgq4z.jpg)

3. Run the command

   ```shell
   java -jar donghang-1.0-SNAPSHOT.jar
   ```

4. If you have a server, you can deploy it there and write a startup script to use with crontab. I personally configured it to run every three minutes.

   ```java
   */3 * * * * . ~/.profile;~/donghang/config.json;/root/donghang/start.sh > /root/donghang/info.log 2>&1 &
   ```

   
