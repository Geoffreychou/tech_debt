Zookeeper



ZAB协议

消息广播    崩溃恢复

发现

同步

广播







# 客户端：

## 组件：

Zookeeper实例：客户端入口

ClientWatchManager: 客户端 Watcher 管理器

HostProvider: 客户端地址列表管理器

ClientCnxn: 客户端核心线程，内部包含2个线程，即 SendThread 和 EventThread

SendThread: 负责 Zookeeper 客户端和服务端之间的网络 I/O 通信

EventThread: 负责对服务端事件进行处理



## 会话创建过程

### 初始化阶段

1. 初始化 Zookeeper 对象

   通过构造方法实例化 Zookeeper 对象，创建 ClientWatchManager

2. 设置会话默认的 Watcher

   若构造方法中传入一个 Watcher 对象，将其作为默认的 Watcher 保存在 ClientWatchManager 中

3. 构造 Zookeeper 服务器地址列表管理器: HostProvider

4. 创建并初始化客户端网络连接器：ClientCnxn

   创建 ClientCnxn 时，还会初始化客户端的2个核心队列 

   outgoingQueue: 客户端请求发送队列

   pendingQueue: 服务端响应等待队列

5. 初始化 SendThread 和 EventThread

   客户端还会将 ClientCnxnSocket 分配给 SendThread 作为底层网络 I/O处理器，并初始化 EventThread 的待处理事件队列 waitingEvents，用于存放所有等待被客户端处理的事件

### 会话创建阶段

1. 启动 SendThread 和 EventThread

   SendThread 首先会判断当前客户端状态，进行清理工作，为客户端发送 “会话创建” 请求做准备

2. 获取一个服务器地址

   SendThread 从 HostProvider 中随机获取出一个地址，委托给 ClientCnxnSocket 去创建与 Zookeeper 服务器之间的 TCP 连接

3. 创建TCP连接

   ClientCnxnSocket 负责和服务器创建一个 TCP 长连接

4. 构造 ConnectRequest 请求

   SendThread 根据当前客户端的实际设置，构造出一个 ConnectRequest 请求，该请求代表了客户端试图与服务器创建一个会话。同时，Zookeeper  客户端还会进一步将该请求包装成网络 I/O 层的 Packet 对象，放入请求发送队列 outgoingQueue 中去。

5. 发送请求

   当客户端请求准备完毕了，就可以开始向服务端发送请求了。ClientCnxnSocket 负责从 outgoingQueue 中取出一个待发送的 Packet 对象，将其序列化成 ByteBuffer 后，向服务端进行发送。



### 响应处理阶段

1. 接收服务端响应

   ClientCnxnSocket 接收到服务端的响应后，会首先判断当前的客户端状态是否已初始化，若尚未完成初始化，那么就认为该响应一定是会话创建请求的响应，直接交由 readConnectResult 方法来处理该响应

2. 处理 Response

   ClientCnxnSocket 会对接收到的服务端响应反序列化，得到 ConnectResponse 对象，并从中获取到 Zookeeper 服务端分配的会话 SeesionId

3. 连接成功

   通知 SendThread 线程，对客户端进行会话参数设置，包括 readTimeout 和 connectTimeout等，更新客户端状态

   通知地址管理器 HostProvider 当前成功连接的服务器地址

4. 生成事件

   SendThread 生成 SyncConnected-None 事件，表示客户端与服务端会话创建成功，并将该事件传递给 EventThread 线程

5. 查询 Watcher

   EventThread 线程接收到事件后，从 ClientWatchManager 管理器中查询出对应的 Watcher，针对 SyncConnected-None 事件，直接找出默认的 Watcher，将其放到 EventThread 的 waitingEvents 队列中去

6. 处理事件

   EventThread 不断的从 waitingEvents 队列中取出待处理的 Watcher 对象，然后直接调用该对象的 process 接口方法，以达到触发 Watcher 的目的





### ClientCnxn



#### Packet

requestHeader

replyHeader

request

response

clientPath/serverPath

watcher



#### outgoingQueue



#### pendingQueue



#### ClientCnxnSocket

-Dzookeeper.clientCnxnSocket=org.apache.zookeeper.ClientCnxnSocketNIO



#### 请求发送

outgoingQueue 队列提取一个可发送的 Packet对象，同时生成一个客户端请求序号XID并将其设置到 Packet 请求头中，将其序列化并发送。

请求发送完毕会立即将该 Packet 保存到 pendingQueue 队列中，以便等待服务端响应返回后进行响应的处理



#### 响应接收

客户端未初始化，说明当前客户端与服务端之间正在进行会话创建，那么就直接将接收到的 ByteBuffer(incomingBuffer) 序列化成 ConnectResponse 对象

客户端处于正常的会话周期，且接收到的服务端响应是一个事件，那么 Zookeeper 客户端会将接收到的 ByteBuffer(incomingBuffer) 序列化成 WatcherEvent 对象，并将该事件放入待处理队列中

如果是一个常规请求（Create/GetData/Exist等），那么会从 pendingQueue 队列中取出一个 Packet 来进行相应的处理。Zookeeper 客户端先检验服务端响应中包含的 XID 值来确保请求处理的顺序性，然后将接收到的 ByteBuffer(incomingBuffer)序列化成响应的 Response 对象。

最后会在 finishPacket 方法中处理 Watcher 注册等逻辑。



#### SendThread

1. 维护客户端与服务端之间的会话生命周期，通过在一定的周期频率内向服务端发送一个 PING 包来实现心跳检测。同时，如果客户端与服务端出现 TCP 连接断开的情况，自动且透明化的完成重连操作。
2. 管理客户端所有的请求发送和响应接收操作，将其上层客户端 API 操作转换成响应的请求协议并发送到服务端，并完成对同步调用的返回和异步回调。同时，SendThread 还负责将来自服务端的事件传递给 EventThread 去处理



#### EventThread

EventThread 内有一个 waitingEvents 队列，用于临时存放需要被触发的对象，包括客户端注册的 Watcher 和 异步接口中注册的回调器 AsyncCallback。

EventThread 不断地从 waitingEvents 队列中取出对象，判断类型（Watcher/AsyncCallback），分别调用 process 和 processResult 接口方法实现对事件的触发和回调。





# 会话

## 会话创建

### Session

sessionID: 每次客户端创建新会话时，Zookeeper 都会为其分配一个全局唯一的 sessionID

```
(System.currentTimeMillis() << 24) >>>8 | (id << 56)
```

timeout: 会话超时时间

tickTime: 下次会话超时时间点，用于 “分桶策略”。 13 位long 类型数据

isClosing: 标记一个会话是否已经被关闭

### SessionTracker

 Zookeeper 服务端的会话管理器，负责会话创建、管理和清理等工作。

1. sessionsById: HashMap<Long,SessionImpl>类型数据结构，根据sessionID管理 session
2. sessionWithTimeout:ConcurrentHashMap<Long, Integer>类型数据结构，用于根据 sessionID 来管理会话的超时时间，会定期持久化到快照文件中
3. sessionSets: HashMap<Long, SessionSet>类型的数据结构，用于根据下次会话超时时间来归档会话，便于进行会话管理和超时检查

### 创建连接





## 会话管理

分桶策略：将类似的会话放在同一个区块中进行管理，以便于 Zookeeper 对会话进行不同区块的隔离处理以及同一区块的统一处理。

```java
expirationTime = currentTime + sessionTimeout
expiratinTime = (expirationTime / expirationInterval + 1) * expirationInterval
```



会话激活

TouchSession: Zookeeper d 的运行过程中，客户端在会话超时时间过期范围内向服务端发送 PING 请求来保持会话的有效性，服务端需要不断的接收来自客户端的这个心跳检测，重新激活对应的客户端会话。



1. 检查该会话是否已经关闭，若关闭则不继续激活该会话
2. 计算该会话的新的超时时间
3. 定位该会话当前的区块
4. 迁移会话





1. 客户端向服务端发送请求，包括读或写请求，那么就会触发一次会话激活

2. 如果客户端发现在 sessionTimeout / 3 时间内尚未和服务器进行过任何通信，即没有想服务端发送任何请求，会主动发起一个 PING 请求，服务端收到该请求，触发一次会话激活



会话超时检查



## 会话清理

1. 标记会话状态为  “已关闭”

   由于整个会话清理过程需要一段时间，因此，为了保证此期间不再处理来自该客户端的新请求，SessionTracker 会首先将该会话的 isClosing 属性标记为 true。这样，即使在会话清理期间接收到该客户端的新请求，也无法继续处理。

2. 发起“会话关闭” 请求

   交付给 PrepRequestProcessor 处理器进行处理。

3. 收集需要清理的临时节点

4. 添加“节点删除”事务变更

   将需要删除的临时节点转换成 “节点删除”请求，并放入事务变更队列 outstandingChanges 中去。

5. 删除临时节点

   FinalRequestProcessor 处理器会触发内存数据库，删除该会话对应的临时节点

6. 移除会话

   将会话从 SessionTracker 中移除。（sessionsById/sessionWithTimeout/sessionSets）

7. 关闭 NIOServerCnxn

   从 NIOServerCnxnFactory 找到该会话对应的 NIOServerCnxn，将其关闭



## 重连







# 服务启动

## 单机

### 预启动

1. 统一由 QuorumPeerMain 作为启动类
2. 解析配置文件 zoo.cfg
3. 创建并启动历史文件清理器 DatadirCleanupManager
4. 判断当前是集群模式还是单机模式启动
5. 再次进行配置文件 zoo.cfg 的解析
6. 创建服务器实例 ZookeeperServer



### 初始化

1. 创建服务器统计器 ServerStats
2. 创建 Zookeeper 数据管理器 FileTxnSnapLog
3. 设置服务器 tickTime 和 会话超时时间限制
4. 创建 ServerCnxnFactory
5. 初始化 ServerCnxnFactory
6. 启动 ServerCnxnFacotry 主线程
7. 恢复本地数据
8. 创建并启动会话管理器
9. 初始化 Zookeeper 的请求处理链
10. 注册 JMX 服务
11. 注册 Zookeeper 服务器实例





## 集群

### 预启动

1. 统一由 QuorumPeerMain 作为启动类
2. 解析 zoo.cfg 配置文件
3. 创建并启动历史文件清理器 DatadirCleanupManager
4. 判断当前线程是集群模式还是单机启动模式



### 初始化

1. 创建 ServerCnxnFactory
2. 初始化 ServerCnxnFactory
3. 创建 Zookeeper 数据管理器 FileTxnSnapLog
4. 创建 QuorumPeer 实例
5. 创建内存数据库 ZKDataBase
6. 初始化 QuorumPeer
7. 恢复本地数据
8. 启动 ServerCnxnFactory 主线程



### Leader 选举

1. 初始化 Leader 选举

   根据 SID, lastLoggerZxid, epoch 生成初始化投票

   根据配置创建相应的 Leader 算法实现，3.4.0+ 版本，只支持 FastLeaderElection 选举算法

   创建 Leader 选举所需的网络I/O层 QuorumCnxManager，同时启动对 Leader 选举端口的监听，等待集群中其他服务器创建连接

2. 注册 JMX 服务

3. 检测当前服务器状态

   Looking/ Leading/ Following/ Observing

4. Leader 选举





# Leader 选举

## 服务器启动时的Leader选取

1. 每个 Server 会发出一个投票
2. 接收来自各服务器的投票
3. 处理投票
4. 统计投票
5. 改变服务器状态



## 服务器运行期间的Leader选举

1. 变更状态
2. 每个 server 会发出一个投票
3. 接收来自各服务器的投票
4. 处理投票
5. 统计投票
6. 改变服务器状态



## Leader选取算法分析

### 进入 Leader 选举









# 各服务角色

## Leader

Leader 服务器是整个 Zookeeper 集群工作机制中的核心，主要工作有以下2个:

1. 事务请求的唯一调度和处理者，保证集群事务处理的顺序性
2. 集群内部各服务器的调度者



### 请求处理链



![1554370903763](E:\tech_stack_parent\tech_stack\zookeeper\imgs\1554370903763.png)

#### PrepRequestProcessor

请求预处理器，识别当前客户端请求是否是事务请求。

对于事务请求，对其进行预处理，包括创建请求事务头，事务体，会话检查，ACL检查和版本检查等。



#### ProposalRequestProcessor

非事务请求，会直接将请求流转到 CommitProcessor 处理器，不再做其他处理；

事务请求，除了将请求交给 CommitProcessor 处理器外，还会根据请求类型创建对应的 Proposal 提议，并发给所有的 Follower 服务器来发起一次集群内的事务投票。同时，将事务请求交给 SyncRequestProcessor 进行事务日志的记录。



#### SyncRequestProcessor

事务日志记录处理器，将事务请求记录到事务日志文件中去，并触发 Zookeeper 进行数据快照。



#### AckRequestProcessor

负责在 SyncRequestProcessor 处理器完成事务日志记录后，向 Proposal 的投票收集器发送 ACK 响应，以通知投票收集器当前服务器已经完成了对该 Proposal 的事务日志记录。



#### CommitProcessor

事务提交处理器。

非事务请求，直接交给下一级处理器进行处理

事务请求，CommitProcessor 处理器会等待集群内针对 Proposal 的投票直到该 Proposal 可被提交。利用 CommitProcessor 处理器，每个服务器都可以很好的控制对事务请求的顺序处理。



#### ToBeCommitProcessor

其中包含 toBeApplied 队列，专门用来存储那些已经被 CommitProcessor 处理过的可被提交的 Proposal。

ToBeCommitProcessor 处理器将这些请求逐个交付给 FinalRequestProcessor 处理器进行处理，等到 FinalRequestProcessor 处理完后，将其从 toBeApplied 队列移除。



#### FinalRequestProcessor

创建客户端请求的响应。

针对事务请求，该处理器还会负责将事务应用到内存数据库中去。





### LearnerHandler

Leader 服务器会为每个 Follower/Observer 服务器简历一个 TCP 长连接，同时会为每个 Follower/Observer 服务器创建一个名为 LearnerHandler 的实体。

负责 Leader 服务器于 Learner 服务器之间一系列的网络通信，包括数据同步、请求转发和 Proposal 提议的投票等。





## Follower

1. 处理客户端非事务请求，转发事务请求给 Leader 服务器
2. 参与事务请求的 Proposal 的投票
3. 参与 Leader 选举投票



![1554372692406](E:\tech_stack_parent\tech_stack\zookeeper\imgs\1554372692406.png)





## Observer





## 集群间消息通信

### 数据同步型

#### DIFF

#### TRUNC

#### SNAP

#### UPTODATE





服务器初始化型

### 请求处理型

### 会话管理型

















# 请求处理



## 会话创建

### 请求接收

### 会话创建

### 预处理

### 事务处理

### 事务应用

### 会话响应

