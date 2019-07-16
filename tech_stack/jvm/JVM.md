# JVM

[java8 jvm](https://docs.oracle.com/javase/specs/jvms/se8/html/index.html)

## JVM体系结构概述

### JVM所属位置

Java Virtual Machine  -> OS -> Hardware

### JVM体系概览

![1553862604543](E:\tech_stack_parent\tech_stack\jvm\imgs\1553862604543.png)



## 类加载器

ClassLoader负责加载class文件，class文件在文件开头有特定的文件标识(CAFEBABE)，并且ClassLoader只负责class文件加载，至于class文件是否可以运行，则由Execution Engine决定。

![1553862847024](E:\tech_stack_parent\tech_stack\jvm\imgs\1553862847024.png)





ClassLoader双亲委派和沙箱安全机制

用户自定义类加载器（继承 `java.lang.ClassLoader`）-> 系统类加载器 -> 扩展类加载器 -> 启动类加载器

![1553863142705](E:\tech_stack_parent\tech_stack\jvm\imgs\1553863142705.png)

## Execution Engine

执行引擎负责解释命令，提交操作系统执行

PC寄存器(程序计数器)

程序计数器是一块较小的内存空间，是当前线程所执行的字节码的行号指示器

程序计数器属于线程独占区

如果线程执行的是Java方法，记录的是正在执行的虚拟机字节码指令的地址，如果是native方法，这个计数器的值是undefined

栈

Java栈也叫栈内存，主管java程序的运行，在线程创建时创建，它的生命周期是跟随线程的生命周期，线程结束时，栈内存就释放，为线程私有。

8种基本类型变量 + 对象的引用变量 + 实例方法都是在函数的栈内存分配的。

栈主要存储：

1. 局部变量表：输入输出参数以及方法内的变量类型；局部变量表在编译期间完成分配，当进入一个方法，这个方法在栈帧中分配多少内存是固定的
2. 栈操作：记录出栈和入栈的操作
3. 动态链接
4. 方法的出口

栈溢出的异常：StackOverflowError / OutOfMemory 

> 如下图所示，一个栈中有2个栈帧
>
> 1. Stack Freme2是最先被调用的方法，先入栈
> 2. 该方法中调用了方法1
> 3. 方法1入栈
> 4. 方法1执行完毕，出栈
> 5. 方法2执行完毕，出栈
> 6. 线程结束，栈释放
>
> 注：每执行一个方法都会产生一个栈帧，保存到栈顶，自行完后，自动出栈

![1553864094453](E:\tech_stack_parent\tech_stack\jvm\imgs\1553864094453.png)

栈中数据引用关系

![1553953213502](E:\tech_stack_parent\tech_stack\jvm\imgs\1553953213502.png)





Native Interface

本地接口，主要是融合不同的编程语言，为java所用。

Native Method Stack

本地方法栈：登记native方法，在Execution Engine执行时，加载本地方法库。

## 方法区

方法区被所有线程共享，用于存储虚拟机加载的：类信息 + 静态变量 + 普通常量 + 编译器编译后的代码等。 

> 1. 类信息
>    - 类的版本
>    - 字段
>    - 方法
>    - 接口
> 2. 静态变量
> 3. 常量
> 4. 运行时常量池：用于存放编译期生成的各种字面变量和符号引用，这部分内容将在类加载后存放到常量池

## 堆

一个JVM实例只存在一个堆内存，堆内存的大小可以调节的。类加载器读取类文件后，需要把类，方法，常量，变量放到堆内存中，保存所有类的真实信息，以方便执行器执行。

堆内存的结构

Young Generation Space

Tenure Generation Space

Permant Space/ Meta Space(JDK 1.8+)

![1553954992348](E:\tech_stack_parent\tech_stack\jvm\imgs\1553954992348.png)

### 创建对象

对象创建过程：

> 1. new 对象
> 2. 根据new 的参数在常量池中定位一个符号引用
> 3. 如果没有找到这个符号引用，则执行类加载（加载 -> 验证 -> 初始化）
> 4. 虚拟机为对象分配内存
> 5. 将对象初始化为零值
> 6. 调用对象的<init>方法

对象分配内存时采用指针碰撞和空间列表的方式，每个线程存在一个本地线程缓冲区（TLAB），优先将对象分配在TLAB，然后采用指针碰撞的方式，找到对象分配的内存地址，分配对象。

指针碰撞：保证分配的对象内存地址连续，减少内存碎片

空间列表：每个线程拥有TLAB，保证线程安全

### 对象的结构

- Header（对象头）

  - 自身运行时数据（Mark Word）

    - Hash值

    - GC分代年龄

    - 锁状态标志

    - 线程持有锁

    - 偏向线程ID

    - 偏向时间戳

      下图为32位OS下的Mark Word

      ![1553955897626](E:\tech_stack_parent\tech_stack\jvm\imgs\1553955897626.png)

  - 类型指针

  - 数组长度（只有数组对象才持有）

- InstanceData

  - 对象的实例数据就是在java代码中能看到的属性和他们的值。相同宽度的数据会被分配到一起（如 long 和 double）

- Padding 

  - 因为JVM要求java的对象占的内存大小应该是8bit的倍数，所以后面有几个字节用于把对象的大小补齐至8bit的倍数



## 垃圾回收

### 垃圾对象判断

#### 引用计数法

在对象中添加一个引用计数器，当有地方引用这个对象时，计数器+1，失效时，计数器-1。

当两个对象互相引用是，永远无法回收。

#### 可达性分析

GCRoot

可作为GCRoot的对象：

- 虚拟机栈（局部变量表中的对象）
- 方法区的类属性所引用的对象
- 方法区常量所引用的对象
- 本地方法栈所引用的对象

### 垃圾回收策略

- 标记清除算法，分为标记和清除2个阶段，缺点为效率低，且会产生内存碎片
- 复制算法
- 标记整理算法
- 分代算法

### 内存分配策略

- 优先分配在Eden区
- 大对象直接分配到老年代， -XX:PretenureSizeThreshold
- 长期存活的对象分配到老年代，-XX:MaxTenuringThreshold=15
- 空间分配担保，检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小， -XX:+HandlePromotionFailure
- 动态对象年龄，如果在survivor空间相同年龄所有对象大小总和大于survivor空间一半，年龄大于等于该年龄的对象可直接进入老年代， -XX:TargetSurvivorRatio

注：逃逸分析和栈上分配，若对象满足逃逸条件，可直接栈上分配

### 垃圾收集器

[参考](https://blog.csdn.net/coderlius/article/details/79272773)

![1553957817832](E:\tech_stack_parent\tech_stack\jvm\imgs\1553957817832.png)

#### Serial & Serial Old

串行收集器是client模式下默认的收集器配置。采用单线程stop-the-world的方式进行收集，内存不足时，串行GC设置停顿标识，待所有线程都进入安全点(Safepoint)时，应用线程暂停，串行GC开始工作，采用单线程方式回收空间并整理内存。单线程也意味着复杂度更低、占用内存更少，但同时也意味着不能有效利用多核优势。事实上，串行收集器特别适合堆内存不高、单核甚至双核CPU的场合。

![img](https://c1.staticflickr.com/5/4603/28345836579_8dff90eb76_z.jpg)

> 开启方式：-XX:+UseSerialGC

```
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJD
 * @date 2019/3/31
 */
public class SerialGCDemo {
    /**
     * -server -Xms10m -Xmx10m -XX:+UseSerialGC -XX:+PrintGCDetails
     * DefNew
     */
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        while (true){
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}
```

GC日志：

> [GC (Allocation Failure) [DefNew: 2201K->320K(3072K), 0.0033158 secs] 2201K->730K(9920K), 0.0033924 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [GC (Allocation Failure) [DefNew: 2418K->0K(3072K), 0.0037318 secs] 2829K->2778K(9920K), 0.0037645 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [GC (Allocation Failure) [DefNew: 2099K->0K(3072K), 0.0021989 secs] 4878K->4826K(9920K), 0.0022305 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [GC (Allocation Failure) [DefNew: 2091K->2091K(3072K), 0.0000158 secs][Tenured: 4826K->5850K(6848K), 0.0041248 secs] 6917K->6874K(9920K), [Metaspace: 3444K->3444K(1056768K)], 0.0042011 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
> [Full GC (Allocation Failure) [Tenured: 5850K->5850K(6848K), 0.0028962 secs] 7899K->7899K(9920K), [Metaspace: 3444K->3444K(1056768K)], 0.0029420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [Full GC (Allocation Failure) [Tenured: 5850K->6847K(6848K), 0.0048004 secs] 7899K->7871K(9920K), [Metaspace: 3444K->3444K(1056768K)], 0.0048474 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
> [Full GC (Allocation Failure) [Tenured: 6847K->6816K(6848K), 0.0045397 secs] 8925K->8864K(9920K), [Metaspace: 3445K->3445K(1056768K)], 0.0045867 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [Full GC (Allocation Failure) [Tenured: 6816K->6816K(6848K), 0.0027927 secs] 8864K->8864K(9920K), [Metaspace: 3445K->3445K(1056768K)], 0.0028350 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 



#### ParNew & CMS

[参考](https://blog.csdn.net/zqz_zqz/article/details/70568819)

ParNew 是 Serial的多线程版本，用于年轻代的垃圾收集。

CMS（Concurrent Mark Sweep）负责老年代和永久代/元空间的垃圾收集，它是一种预处理垃圾回收器，它不能等到old内存用尽时回收，需要在内存用尽前，完成回收操作，否则会导致并发回收失败；所以cms垃圾回收器开始执行回收操作，有一个触发阈值，默认是老年代或永久带达到92%；

> 开启方式：-XX:+UseConcMarkSweepGC

![img](https://c1.staticflickr.com/5/4740/40093687062_7383cd1b49_z.jpg)

CMS处理过程如下：

-  初始标记(CMS-initial-mark) ,会导致STW（stop-the-word）； 
- 并发标记(CMS-concurrent-mark)，与用户线程同时运行； 
- 预清理（CMS-concurrent-preclean），与用户线程同时运行； 
- 可被终止的预清理（CMS-concurrent-abortable-preclean） 与用户线程同时运行； 
- 重新标记(CMS-remark) ，会导致SWT； 
- 并发清除(CMS-concurrent-sweep)，与用户线程同时运行； 
- 并发重置状态等待下次CMS的触发(CMS-concurrent-reset)，与用户线程同时运行； 

> -XX:+PrintCommandLineFlags   #打印出启动参数行 
>
> -XX:+UseConcMarkSweepGC    #参数指定使用CMS垃圾回收器
>
> -XX:+UseCMSInitiatingOccupancyOnly     #只是用设定的回收阈值，如下面的80%，若不设置，JVM仅在第一次使用设定值,后续则自动调整。             
>
> -XX:CMSInitiatingOccupancyFraction=80     #参数指定CMS垃圾回收器在老年代达到80%的时候开始工作，如果不指定那么默认的值为92%
>
> -XX:+CMSClassUnloadingEnabled    #开启永久带（jdk1.8以下版本）或元数据区（jdk1.8及其以上版本）收集，如果没有设置这个标志，一旦永久代或元数据区耗尽空间也会尝试进行垃圾回收，但是收集不会是并行的，而再一次进行Full GC。             
>
> -XX:+UseParNewGC    # 年轻代使用ParNewGC，CMS下默认为ParNewGC                            
>
> -XX:+CMSParallelRemarkEnabled        #减少Remark阶段暂停的时间，启用并行Remark，如果Remark阶段暂停时间长，可以启用这个参数 
>
> -XX:+CMSScavengeBeforeRemark       #在Remark执行之前，先做一次ygc。因为这个阶段，年轻带也是cms的gcroot，cms会扫描年轻带指向老年代对象的引用，如果年轻带有大量引用需要被扫描，会让Remark阶段耗时增加。        
>
> -XX:+UseCMSCompactAtFullCollection     #针对cms垃圾回收器碎片做优化，CMS是不会移动内存的， 运行时间长了，会产生很多内存碎片， 导致没有一段连续区域可以存放大对象，出现”promotion failed”、”concurrent mode failure”, 导致fullgc，启用UseCMSCompactAtFullCollection 在FULL GC的时候， 对年老代的内存进行压缩。
>
> -XX:CMSFullGCsBeforeCompaction=0    #代表多少次FGC后对老年代做压缩操作，默认值为0，代表每次都压缩, 把对象移动到内存的最左边，可能会影响性能,但是可以消除碎片；。
>
> -XX:+CMSConcurrentMTEnabled     #默认开启，使用多线程进行收集
>
> -XX:ConcGCThreads=4                    #并发CMS过程运行时的线程数
>
> -XX:+ExplicitGCInvokesConcurrent         #JVM无论什么时候调用系统GC，都执行CMS GC，而不是Full GC。
>
> -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses    #当有系统GC调用时，永久代也被包括进CMS垃圾回收的范围内。
>
> -XX:+CMSParallelInitialMarkEnabled    #开启初始标记过程中的并行化，进一步提升初始化标记效率;。
>
> -X:+PrintGCDetails     # 打印GC日志
>
> -XX:+PrintGCCause                  # 打印GC日志
>
>  -XX:+PrintGCTimeStamps      # 打印GC日志      
>
> -XX:+PrintGCDateStamps        # 打印GC日志
>
> -Xloggc:../logs/gc.log              # 打印GC日志      
>
> -XX:+HeapDumpOnOutOfMemoryError     # 内存溢出时打印堆栈信息
>
> -XX:HeapDumpPath=../dump                  # 打印堆栈信息路径



```java
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJD
 * @date 2019/3/31
 */
public class CMSGCDemo {
    /**
     * -server -Xms10m -Xmx10m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails
     * ParNew  CMS
     */
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();

        while (true){
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}
```



> [GC (Allocation Failure) [ParNew: 2259K->318K(3072K), 0.0024308 secs] 2259K->756K(9920K), 0.0025114 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [GC (Allocation Failure) [ParNew: 2447K->75K(3072K), 0.0019773 secs] 2884K->2876K(9920K), 0.0020164 secs] [Times: user=0.09 sys=0.03, real=0.00 secs] 
> [GC (Allocation Failure) [ParNew: 2194K->37K(3072K), 0.0016415 secs] 4995K->4887K(9920K), 0.0016786 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>
> **#初始标记**
>
> [GC (CMS Initial Mark) [1 CMS-initial-mark: 4849K(6848K)] 5924K(9920K), 0.0001825 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>
> **#并发标记**
>
> [CMS-concurrent-mark-start]
> [GC (Allocation Failure) [ParNew: 2141K->2141K(3072K), 0.0000217 secs] [CMS[CMS-concurrent-mark: 0.001/0.003 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>  (concurrent mode failure): 4849K->5856K(6848K), 0.0069258 secs] 6990K->6880K(9920K), [Metaspace: 3455K->3455K(1056768K)], 0.0069989 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
> [Full GC (Allocation Failure) [CMS: 5856K->5856K(6848K), 0.0022123 secs] 7913K->7904K(9920K), [Metaspace: 3457K->3457K(1056768K)], 0.0022590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [Full GC (Allocation Failure) [CMS: 5856K->5838K(6848K), 0.0030254 secs] 7904K->7886K(9920K), [Metaspace: 3457K->3457K(1056768K)], 0.0030684 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
> [GC (CMS Initial Mark) [1 CMS-initial-mark: 5838K(6848K)] 7886K(9920K), 0.0001841 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [CMS-concurrent-mark-start]
> [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>
> **# 预清理**
>
> [CMS-concurrent-preclean-start]
> [CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>
> **# 可被终止的预清理**
>
> [CMS-concurrent-abortable-preclean-start]
> [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
>
> **# 重新标记**
>
> [GC (CMS Final Remark) [YG occupancy: 2154 K (3072 K)] [Rescan (parallel) , 0.0004002 secs] [weak refs processing, 0.0000115 secs] [class unloading, 0.0002607 secs] [scrub symbol table, 0.0005898 secs] [scrub string table, 0.0001778 secs] [1 CMS-remark: 5838K(6848K)] 7993K(9920K), 0.0015210 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> Heap
>
> **# 清理**
>
> [CMS-concurrent-sweep-start]



#### Parallel Scavenge & Parallel Old

[参考](https://blog.csdn.net/ffm83/article/details/42872661)

新生代收集器，采用复制算法，可多线程执行，其目标为达到可控的吞吐量。

所谓吞吐量就是CPU用于运行用户代码的时间与CPU总消耗时间的比值，即吞吐量 = 运行用户代码时间 /（运行用户代码时间 + 垃圾收集时间），虚拟机总共运行了100分钟，其中垃圾收集花掉1分钟，那吞吐量就是99%。

该垃圾收集器，是JAVA虚拟机在Server模式下的默认值，使用Server模式后，java虚拟机使用Parallel Scavenge收集器（新生代）+ Serial Old收集器（老年代）的收集器组合进行内存回收。

-XX:MaxGCPauseMillis:最大垃圾收集停顿时间

--XX:GCTimeRatio:吞吐量大小（0-100）

![img](https://c1.staticflickr.com/5/4662/28345836389_55e8402324_z.jpg)

```java
import java.util.ArrayList;
import java.util.List;
/**
 * @author ZJD
 * @date 2019/3/30
 */
public class ParallelScavengeDemo {
    /**
     * -server -Xms10m -Xmx10m -XX:+UseParallelGC -XX:+PrintGCDetails
     * PSYoungGen
     */
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();

        while (true){
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}
```

GC日志：

> [GC (Allocation Failure) [PSYoungGen: 2048K->504K(2560K)] 2048K->741K(9728K), 0.0293551 secs] [Times: user=0.03 sys=0.00, real=0.04 secs] 
> [GC (Allocation Failure) --[PSYoungGen: 1626K->1626K(2560K)] 8008K->8024K(9728K), 0.0161043 secs] [Times: user=0.05 sys=0.00, real=0.02 secs] 
> [Full GC (Ergonomics) [PSYoungGen: 1626K->1528K(2560K)] [ParOldGen: 6397K->6324K(7168K)] 8024K->7853K(9728K), [Metaspace: 3374K->3374K(1056768K)], 0.0087024 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
> [GC (Allocation Failure) --[PSYoungGen: 1528K->1528K(2560K)] 7853K->7869K(9728K), 0.0028148 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
> [Full GC (Allocation Failure) [PSYoungGen: 1528K->1508K(2560K)] [ParOldGen: 6340K->6327K(7168K)] 7869K->7836K(9728K), [Metaspace: 3374K->3374K(1056768K)], 0.0057699 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 





## Class文件

class 文件是一组以8位字节为基础的二进制文件，各个数据严格按照顺序紧凑的排列在class文件之中，中间没有添加任何分隔符，这使得整个Class文件存储的内容几乎全部都是程序运行的必要，没有空隙存在。

当遇到需要占用8字节以上空间的数据项时，会按照高位在前的方式分割成若干个8位字节进行存储。

class文件只有2个数据类型，无符号数和表

[class file format](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html)



## GC调优







## 总结