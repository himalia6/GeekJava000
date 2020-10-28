极客时间Java训练营第二周作业
=========================

#### 1. 使用 [GCLogAnalysis.java](./src/main/java/GCLogAnalysis.java) 自己演练一遍串行/并行/CMS/G1的案例。
代码编译：
```shell script
javac -encoding GBK  GCLogAnalysis.java
# output: 
# GCLogAnalysis.class
# GCLogAnalysis.java
```
默认参数运行：
```shell script
java -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
```
```text
正在执行...
2020-10-28T21:22:55.996-0800: [GC (Allocation Failure) [PSYoungGen: 131584K->21494K(153088K)] 131584K->40907K(502784K), 0.0170183 secs] [Times: user=0.03 sys=0.14, real=0.02 secs]
2020-10-28T21:22:56.031-0800: [GC (Allocation Failure) [PSYoungGen: 153078K->21503K(284672K)] 172491K->87284K(634368K), 0.0278149 secs] [Times: user=0.03 sys=0.23, real=0.02 secs]
2020-10-28T21:22:56.133-0800: [GC (Allocation Failure) [PSYoungGen: 284671K->21488K(284672K)] 350452K->166274K(634368K), 0.0408590 secs] [Times: user=0.05 sys=0.34, real=0.04 secs]
2020-10-28T21:22:56.209-0800: [GC (Allocation Failure) [PSYoungGen: 284656K->21503K(547840K)] 429442K->235912K(897536K), 0.0377766 secs] [Times: user=0.05 sys=0.31, real=0.04 secs]
2020-10-28T21:22:56.389-0800: [GC (Allocation Failure) [PSYoungGen: 547839K->21502K(547840K)] 762248K->371207K(898048K), 0.0661914 secs] [Times: user=0.07 sys=0.53, real=0.07 secs]
2020-10-28T21:22:56.455-0800: [Full GC (Ergonomics) [PSYoungGen: 21502K->0K(547840K)] [ParOldGen: 349705K->260752K(553472K)] 371207K->260752K(1101312K), [Metaspace: 2875K->2875K(1056768K)], 0.0311157 secs] [Times: user=0.31 sys=0.02, real=0.03 secs]
2020-10-28T21:22:56.572-0800: [GC (Allocation Failure) [PSYoungGen: 526336K->143672K(1135616K)] 787088K->404425K(1689088K), 0.0615436 secs] [Times: user=0.07 sys=0.52, real=0.06 secs]
2020-10-28T21:22:56.905-0800: [GC (Allocation Failure) [PSYoungGen: 1135416K->184319K(1179136K)] 1396169K->536189K(1732608K), 0.0858705 secs] [Times: user=0.17 sys=0.66, real=0.09 secs]
执行结束!共生成对象次数:10779
Heap
 PSYoungGen      total 1179136K, used 224167K [0x0000000715580000, 0x00000007a6980000, 0x00000007c0000000)
  eden space 994816K, 4% used [0x0000000715580000,0x0000000717c69f58,0x0000000752100000)
  from space 184320K, 99% used [0x0000000752100000,0x000000075d4fff58,0x000000075d500000)
  to   space 260608K, 0% used [0x0000000796b00000,0x0000000796b00000,0x00000007a6980000)
 ParOldGen       total 553472K, used 351870K [0x00000005c0000000, 0x00000005e1c80000, 0x0000000715580000)
  object space 553472K, 63% used [0x00000005c0000000,0x00000005d579f850,0x00000005e1c80000)
 Metaspace       used 2882K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 307K, capacity 386K, committed 512K, reserved 1048576K
```

* 串行GC
    * heap 128m
```shell script
java -Xmx128m -Xms128m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
```
```text
正在执行...
2020-10-28T21:29:00.251-0800: [GC (Allocation Failure) [PSYoungGen: 32888K->5116K(38400K)] 32888K->11447K(125952K), 0.0035700 secs] [Times: user=0.01 sys=0.02, real=0.00 secs]
2020-10-28T21:29:00.261-0800: [GC (Allocation Failure) [PSYoungGen: 38396K->5119K(38400K)] 44727K->21849K(125952K), 0.0052502 secs] [Times: user=0.01 sys=0.04, real=0.00 secs]
2020-10-28T21:29:00.272-0800: [GC (Allocation Failure) [PSYoungGen: 37746K->5112K(38400K)] 54477K->31968K(125952K), 0.0047477 secs] [Times: user=0.01 sys=0.03, real=0.00 secs]
2020-10-28T21:29:00.285-0800: [GC (Allocation Failure) [PSYoungGen: 37909K->5092K(38400K)] 64765K->44402K(125952K), 0.0060599 secs] [Times: user=0.01 sys=0.04, real=0.01 secs]
2020-10-28T21:29:00.296-0800: [GC (Allocation Failure) [PSYoungGen: 38358K->5115K(38400K)] 77669K->56674K(125952K), 0.0066297 secs] [Times: user=0.02 sys=0.04, real=0.01 secs]
2020-10-28T21:29:00.307-0800: [GC (Allocation Failure) [PSYoungGen: 38378K->5116K(19968K)] 89938K->70721K(107520K), 0.0068638 secs] [Times: user=0.02 sys=0.05, real=0.01 secs]
2020-10-28T21:29:00.317-0800: [GC (Allocation Failure) [PSYoungGen: 19813K->12734K(29184K)] 85418K->78750K(116736K), 0.0022272 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.321-0800: [GC (Allocation Failure) [PSYoungGen: 27582K->14319K(29184K)] 93598K->83608K(116736K), 0.0029294 secs] [Times: user=0.02 sys=0.01, real=0.00 secs]
2020-10-28T21:29:00.326-0800: [GC (Allocation Failure) [PSYoungGen: 29124K->14335K(29184K)] 98413K->90276K(116736K), 0.0052949 secs] [Times: user=0.02 sys=0.02, real=0.01 secs]
2020-10-28T21:29:00.331-0800: [Full GC (Ergonomics) [PSYoungGen: 14335K->0K(29184K)] [ParOldGen: 75941K->80451K(87552K)] 90276K->80451K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0098767 secs] [Times: user=0.08 sys=0.02, real=0.01 secs]
2020-10-28T21:29:00.343-0800: [Full GC (Ergonomics) [PSYoungGen: 14544K->0K(29184K)] [ParOldGen: 80451K->84346K(87552K)] 94996K->84346K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0131732 secs] [Times: user=0.12 sys=0.01, real=0.01 secs]
2020-10-28T21:29:00.358-0800: [Full GC (Ergonomics) [PSYoungGen: 14700K->2269K(29184K)] [ParOldGen: 84346K->87343K(87552K)] 99046K->89612K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0169170 secs] [Times: user=0.16 sys=0.00, real=0.02 secs]
2020-10-28T21:29:00.377-0800: [Full GC (Ergonomics) [PSYoungGen: 14116K->6075K(29184K)] [ParOldGen: 87343K->86677K(87552K)] 101460K->92752K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0113403 secs] [Times: user=0.10 sys=0.01, real=0.01 secs]
2020-10-28T21:29:00.390-0800: [Full GC (Ergonomics) [PSYoungGen: 14848K->6642K(29184K)] [ParOldGen: 86677K->87252K(87552K)] 101525K->93895K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0109222 secs] [Times: user=0.11 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.402-0800: [Full GC (Ergonomics) [PSYoungGen: 14736K->8696K(29184K)] [ParOldGen: 87252K->87362K(87552K)] 101988K->96059K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0100580 secs] [Times: user=0.10 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.414-0800: [Full GC (Ergonomics) [PSYoungGen: 14684K->11762K(29184K)] [ParOldGen: 87362K->87179K(87552K)] 102047K->98941K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0051196 secs] [Times: user=0.05 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.419-0800: [Full GC (Ergonomics) [PSYoungGen: 14802K->13192K(29184K)] [ParOldGen: 87179K->87179K(87552K)] 101981K->100372K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0018788 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.421-0800: [Full GC (Ergonomics) [PSYoungGen: 14641K->13233K(29184K)] [ParOldGen: 87179K->87179K(87552K)] 101820K->100413K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0016465 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.423-0800: [Full GC (Ergonomics) [PSYoungGen: 14835K->13336K(29184K)] [ParOldGen: 87179K->87179K(87552K)] 102015K->100515K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0016182 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.425-0800: [Full GC (Ergonomics) [PSYoungGen: 14295K->14050K(29184K)] [ParOldGen: 87179K->86999K(87552K)] 101475K->101049K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0115295 secs] [Times: user=0.12 sys=0.01, real=0.01 secs]
2020-10-28T21:29:00.437-0800: [Full GC (Ergonomics) [PSYoungGen: 14822K->14134K(29184K)] [ParOldGen: 86999K->86999K(87552K)] 101821K->101134K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0041395 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.441-0800: [Full GC (Ergonomics) [PSYoungGen: 14780K->13247K(29184K)] [ParOldGen: 86999K->87440K(87552K)] 101780K->100687K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0078480 secs] [Times: user=0.07 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.449-0800: [Full GC (Ergonomics) [PSYoungGen: 14765K->14060K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102205K->101500K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0023919 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.452-0800: [Full GC (Ergonomics) [PSYoungGen: 14744K->14060K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102184K->101500K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0018154 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.454-0800: [Full GC (Ergonomics) [PSYoungGen: 14781K->14228K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102221K->101668K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0015833 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.455-0800: [Full GC (Ergonomics) [PSYoungGen: 14766K->14335K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102206K->101775K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0014789 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.457-0800: [Full GC (Ergonomics) [PSYoungGen: 14787K->14551K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102227K->101991K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0014684 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2020-10-28T21:29:00.459-0800: [Full GC (Ergonomics) [PSYoungGen: 14836K->14531K(29184K)] [ParOldGen: 87440K->87440K(87552K)] 102276K->101971K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0023927 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.461-0800: [Full GC (Ergonomics) [PSYoungGen: 14583K->14583K(29184K)] [ParOldGen: 87440K->87356K(87552K)] 102023K->101940K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0084124 secs] [Times: user=0.08 sys=0.00, real=0.01 secs]
2020-10-28T21:29:00.470-0800: [Full GC (Allocation Failure) [PSYoungGen: 14583K->14583K(29184K)] [ParOldGen: 87356K->87337K(87552K)] 101940K->101921K(116736K), [Metaspace: 2875K->2875K(1056768K)], 0.0114663 secs] [Times: user=0.12 sys=0.01, real=0.01 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:48)
	at GCLogAnalysis.main(GCLogAnalysis.java:25)
Heap
 PSYoungGen      total 29184K, used 14848K [0x00000007bd580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 14848K, 100% used [0x00000007bd580000,0x00000007be400000,0x00000007be400000)
  from space 14336K, 0% used [0x00000007be400000,0x00000007be400000,0x00000007bf200000)
  to   space 14336K, 0% used [0x00000007bf200000,0x00000007bf200000,0x00000007c0000000)
 ParOldGen       total 87552K, used 87337K [0x00000007b8000000, 0x00000007bd580000, 0x00000007bd580000)
  object space 87552K, 99% used [0x00000007b8000000,0x00000007bd54a708,0x00000007bd580000)
 Metaspace       used 2906K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 310K, capacity 386K, committed 512K, reserved 1048576K
```
* 并行GC

* CMS-GC

* G1-GC

#### 2. 使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例。



#### 3. (选做)如果自己本地有可以运行的项目，可以按照2的方式进行演练。



#### 4. (可选)运行课上的例子，以及Netty的例子，分析相关现象。
* Single Thread

server端：`java -Xmx512m -Xms512m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps netty.server.HttpServer01`
```text
$ wrk -c 40 -d 60 -t 32 http://localhost:8801
Running 1m test @ http://localhost:8801
  32 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   723.48ms   67.07ms 792.47ms   97.22%
    Req/Sec     0.46      0.68    10.00     98.36%
  791 requests in 1.00m, 106.57KB read
  Socket errors: connect 0, read 2616, write 3, timeout 0
Requests/sec:     13.16
Transfer/sec:      1.77KB
```
这种方式显然效率很低下，既不能使用现代处理器的多核心优势，加上sleep（IO时间）阻塞的影响，吞吐量会很差。

* One thread per request

server端：`java -Xmx512m -Xms512m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps netty.server.HttpServer02`
```text
$ wrk -c 40 -d 60 -t 32 http://localhost:8802
Running 1m test @ http://localhost:8802
  32 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.71ms    6.18ms  76.81ms   98.55%
    Req/Sec     2.89      4.08    30.00     83.75%
  2201 requests in 1.00m, 1.54MB read
  Socket errors: connect 0, read 80504, write 27, timeout 0
Requests/sec:     36.62
Transfer/sec:     26.18KB
```
这种方式较单线程有显著提升，但实际创建和销毁线程的损耗将很大，受限于所运行环境的性能，而且性能折损比较大。

* with threadpool

server端：`java -Xmx512m -Xms512m -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps netty.server.HttpServer03`
```text
$ wrk -c 40 -d 60 -t 32 http://localhost:8803
Running 1m test @ http://localhost:8803
  32 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    23.43ms    5.66ms  70.45ms   98.17%
    Req/Sec     2.21      3.52    20.00     86.79%
  1635 requests in 1.00m, 1.49MB read
  Socket errors: connect 0, read 82260, write 16, timeout 0
Requests/sec:     27.20
Transfer/sec:     25.40KB
```
这种方式较每个请求创建一线程的模型而言，创建和销毁线程的损耗得到较大幅度减缓，受限于线程池的配置状况，难以有显著提升。


#### 5. 写一段代码，使用HttpClient或OkHttp访问 http://localhost:8801，代码提交到 github。
主要有一个借助HttpClient处理请求，包括HTTP头部、请求参数、Token认证、消息题的，并同样处理远端响应的[HttpProxyClient](./src/main/java/http/HttpProxyClient.java):
```java
package http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpProxyClient {
    private final CloseableHttpClient httpClient;

    public HttpProxyClient() {
        this.httpClient = HttpClients.createDefault();
    }

    public String request(HttpUriRequest request) {
        final ResponseHandler<String> handler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                System.out.println("response status => " + httpResponse.getStatusLine());
                final int status = httpResponse.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    final HttpEntity entity = httpResponse.getEntity();
                    final String result = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    System.out.println("body => " + result);
                    return result;
                } else {
                    throw new ClientProtocolException("Unexpected response status => " + status);
                }
            }
        };

        // final HttpGet get = new HttpGet("http://localhost:8801");

        // request headers & remote auth
        request.addHeader("Proxy-Type", "Gateway");
        request.addHeader("Proxy-Auth", "AuthorizedKeys");
        String result = "";
        try {
            result = httpClient.execute(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

```
和一个由SocketServer改写来的用于接收请求的HTTP入口[GatewayHTTPClient](./src/main/java/http/GatewayHTTPClient.java)：
```java
package http;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GatewayHTTPClient {
    static HttpProxyClient client = new HttpProxyClient();

    public static void main(String[] args) {
        final ExecutorService pool = Executors.newFixedThreadPool(40);
        try (final ServerSocket serverSocket = new ServerSocket(8805)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                pool.execute(() -> {
                        service(socket);
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type:text/html;charset=utf-8");
            final long start = System.currentTimeMillis();
            String body = client.request(new HttpGet("http://localhost:8801"));
            final long duration = System.currentTimeMillis() - start;
            writer.println("Content-Length:" + body.getBytes().length);
            // request remote time
            writer.println("Request-Time:" + duration);
            writer.println();
            writer.write(body);
            writer.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
```