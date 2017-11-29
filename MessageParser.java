package com.iflytek.msp.lfasr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.util.ArrayList;

public class MessageParser {
    public ArrayList<MessageXunFei>  Parse(String message)
    {
        ArrayList<MessageXunFei> list1 = JSON.parseObject(message,
                new TypeReference<ArrayList<MessageXunFei>>() {
                });
        return list1;

    }

    /*
[{"name":"0","id":"390","nc":"1.0","onebest":"我","si":"0","speaker":"0","wordsResultList":
[{"alternativeList":[{"wc":"0.2027","wordsName":"嗯","wp":"n"},{"wc":"0.1317","wordsName":"你","wp":"n"}],
"wc":"0.3640","wordBg":"3","wordEd":"13","wordsName":"我","wp":"n"}]},{"name":"1380","id":"3840","nc":"1.0",
"onebest":"猪喂鸡食","si":"1","speaker":"0","wordsResultList":[{"alternativeList":[{"wc":"0.0126","wordsName":"-","wp":"n"},
{"wc":"0.0000","wordsName":"朱","wp":"n"}],"wc":"0.9874","wordBg":"24","wordEd":"62","wordsName":"猪","wp":"n"},
{"alternativeList":[{"wc":"0.0144","wordsName":"-","wp":"n"}],"wc":"0.9856","wordBg":"62","wordEd":"162","wordsName":"喂",
"wp":"n"},{"alternativeList":[{"wc":"0.0126","wordsName":"诸位","wp":"n"},{"wc":"0.0018","wordsName":"为","wp":"n"}],
"wc":"0.9856","wordBg":"162","wordEd":"186","wordsName":"鸡","wp":"n"},{"alternativeList":[{"wc":"0.2764","wordsName":"时","wp":"n"},
{"wc":"0.0107","wordsName":"基石","wp":"n"}],"wc":"0.7077","wordBg":"186","wordEd":"214","wordsName":"食","wp":"n"}]},
{"name":"5390","id":"8910","nc":"1.0","onebest":"今天我们会员国协会","si":"2","speaker":"0","wordsResultList":[{"alternativeList":[],
"wc":"1.0000","wordBg":"24","wordEd":"136","wordsName":"今天","wp":"n"},{"alternativeList":[],"wc":"1.0000","wordBg":"136",
"wordEd":"201","wordsName":"我们","wp":"n"},{"alternativeList":[],"wc":"1.0000","wordBg":"201","wordEd":"247",
"wordsName":"会员","wp":"n"},{"alternativeList":[{"wc":"0.0466","wordsName":"服务","wp":"n"},{"wc":"0.0026","wordsName":"佛",
"wp":"n"}],"wc":"0.9508","wordBg":"247","wordEd":"270","wordsName":"国","wp":"n"},{"alternativeList":[],"wc":"1.0000",
"wordBg":"270","wordEd":"326","wordsName":"协会","wp":"n"}]},{"name":"9710","id":"13130","nc":"1.0","onebest":"畸形依然等法规",
"si":"3","speaker":"0","wordsResultList":[{"alternativeList":[{"wc":"0.4211","wordsName":"机型","wp":"n"},{"wc":"0.0001",
"wordsName":"即","wp":"n"}],"wc":"0.5787","wordBg":"28","wordEd":"187","wordsName":"畸形","wp":"n"},{"alternativeList":
[{"wc":"0.0768","wordsName":"伊朗","wp":"n"},{"wc":"0.0003","wordsName":"一","wp":"n"}],"wc":"0.9228","wordBg":"187","wordEd":
"236","wordsName":"依然","wp":"n"},{"alternativeList":[],"wc":"1.0000","wordBg":"236","wordEd":"266","wordsName":"等","wp":"n"},
{"alternativeList":[{"wc":"0.0196","wordsName":"法","wp":"n"}],"wc":"0.9804","wordBg":"266","wordEd":"314","wordsName":"法规",
"wp":"n"}]},{"name":"15170","id":"17620","nc":"1.0","onebest":"首先祝各位","si":"4","speaker":"0","wordsResultList":
[{"alternativeList":[],"wc":"1.0000","wordBg":"10","wordEd":"132","wordsName":"首先","wp":"n"},{"alternativeList":[],
"wc":"1.0000","wordBg":"132","wordEd":"156","wordsName":"祝","wp":"n"},{"alternativeList":[],"wc":"1.0000","wordBg":"156",
"wordEd":"214","wordsName":"各位","wp":"n"}]},{"name":"18370","id":"20590","nc":"1.0","onebest":"身心健康","si":"5","speaker":"0",
"wordsResultList":[{"alternativeList":[],"wc":"1.0000","wordBg":"9","wordEd":"196","wordsName":"身心健康","wp":"n"}]},{"name":"20740",
"id":"22660","nc":"1.0","onebest":"啊法喜充满","si":"6","speaker":"0","wordsResultList":[{"alternativeList":[{"wc":"0.0002",
"wordsName":"R","wp":"n"},{"wc":"0.0002","wordsName":"阿","wp":"n"}],"wc":"0.9995","wordBg":"18","wordEd":"80","wordsName":
"啊","wp":"n"},{"alternativeList":[],"wc":"1.0000","wordBg":"80","wordEd":"172","wordsName":"法喜充满","wp":"n"}]}
* */

}
