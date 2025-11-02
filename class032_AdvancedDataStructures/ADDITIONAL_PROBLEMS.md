# 补充题目列表

本文件记录了为[class035](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035)文件夹中高级数据结构设计题目找到的补充题目，包括题目名称、来源、内容描述以及网址链接。

## 1. 设计有setAll功能的哈希表

### 题目描述
哈希表常见的三个操作是put、get和containsKey，而且这三个操作的时间复杂度为O(1)。现在想加一个setAll功能，就是把所有记录value都设成统一的值。请设计并实现这种有setAll功能的哈希表，并且put、get、containsKey和setAll四个操作的时间复杂度都为O(1)。

### 来源
- 牛客网: [设计有setAll功能的哈希表](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967)

### 相关实现
- Java: [Code01_SetAllHashMap.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code01_SetAllHashMap.java)
- Python: [Code01_SetAllHashMap.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code01_SetAllHashMap.py)
- C++: [Code01_SetAllHashMap.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code01_SetAllHashMap.cpp)

## 2. LRU缓存机制

### 题目描述
运用你所掌握的数据结构，设计和实现一个LRU (最近最少使用) 缓存机制。它应该支持以下操作：获取数据 get 和写入数据 put 。

### 来源
- LeetCode 146: [LRU Cache](https://leetcode.com/problems/lru-cache/)
- 剑指Offer II 031: [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
- 牛客网: [设计LRU缓存结构](https://www.nowcoder.com/practice/5dfded165916435d9defb053c63f1e84)

### 相关实现
- Java: [Code02_LRU.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code02_LRU.java)
- Python: [Code02_LRU.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code02_LRU.py)
- C++: [Code02_LRU.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code02_LRU.cpp)

## 3. O(1)时间插入、删除和获取随机元素

### 题目描述
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构：
1. insert(val): 当元素val不存在时返回true，并向集合中插入该项，否则返回false
2. remove(val): 元素val存在时，从集合中移除该项，返回true，否则返回false
3. getRandom: 随机返回现有集合中的一项，每个元素应该有相同的概率被返回

### 来源
- LeetCode 380: [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/)
- 牛客网: [O(1)时间插入、删除和获取随机元素](https://www.nowcoder.com/discuss/353149939293298688)

### 相关实现
- Java: [Code03_InsertDeleteRandom.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code03_InsertDeleteRandom.java)
- Python: [Code03_InsertDeleteRandom.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code03_InsertDeleteRandom.py)
- C++: [Code03_InsertDeleteRandom.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code03_InsertDeleteRandom.cpp)

## 4. 允许重复元素的O(1)数据结构

### 题目描述
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构（允许重复元素）：
1. insert(val): 将一个元素val插入到集合中，返回true
2. remove(val): 如果元素val存在，则从中删除一个实例，返回true，否则返回false
3. getRandom: 随机返回集合中的一个元素，每个元素被返回的概率与其在集合中的数量成线性关系

### 来源
- LeetCode 381: [常数时间插入、删除和获取随机元素-允许重复](https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/)
- LeetCode 380: [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/)

### 相关实现
- Java: [Code04_InsertDeleteRandomDuplicatesAllowed.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code04_InsertDeleteRandomDuplicatesAllowed.java)
- Python: [Code04_InsertDeleteRandomDuplicatesAllowed.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code04_InsertDeleteRandomDuplicatesAllowed.py)
- C++: [Code04_InsertDeleteRandomDuplicatesAllowed.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code04_InsertDeleteRandomDuplicatesAllowed.cpp)

## 5. 数据流的中位数

### 题目描述
设计一个支持以下两种操作的数据结构：
1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
2. double findMedian() - 返回目前所有元素的中位数

中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。

### 来源
- LeetCode 295: [数据流的中位数](https://leetcode.com/problems/find-median-from-data-stream/)
- 剑指Offer 41: [数据流中的中位数](https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/)
- LeetCode 480: [滑动窗口中位数](https://leetcode.com/problems/sliding-window-median/)

### 相关实现
- Java: [Code05_MedianFinder.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code05_MedianFinder.java)
- Python: [Code05_MedianFinder.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code05_MedianFinder.py)
- C++: [Code05_MedianFinder.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code05_MedianFinder.cpp)

## 6. 最大频率栈

### 题目描述
实现一个类似栈的数据结构，支持以下操作：
1. push(val): 将一个整数val压入栈顶
2. pop(): 删除并返回栈中出现频率最高的元素
   如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素

### 来源
- LeetCode 895: [最大频率栈](https://leetcode.com/problems/maximum-frequency-stack/)
- 牛客网: [最大频率栈](https://www.nowcoder.com/discuss/791601453080055808)

### 相关实现
- Java: [Code06_MaximumFrequencyStack.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code06_MaximumFrequencyStack.java)
- Python: [Code06_MaximumFrequencyStack.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code06_MaximumFrequencyStack.py)
- C++: [Code06_MaximumFrequencyStack.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code06_MaximumFrequencyStack.cpp)

## 7. 全O(1)的数据结构

### 题目描述
设计一个数据结构支持以下操作，所有操作的时间复杂度都为O(1)：
1. inc(key): 将key的计数增加1，如果key不存在则插入计数为1的key
2. dec(key): 将key的计数减少1，如果计数变为0则删除key
3. getMaxKey(): 返回计数最大的任意一个key，如果不存在返回空字符串
4. getMinKey(): 返回计数最小的任意一个key，如果不存在返回空字符串

### 来源
- LeetCode 432: [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/)
- LeetCode 146: [LRU缓存](https://leetcode.com/problems/lru-cache/)

### 相关实现
- Java: [Code07_AllO1.java](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code07_AllO1.java)
- Python: [Code07_AllO1.py](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code07_AllO1.py)
- C++: [Code07_AllO1.cpp](file:///D:/Upan/src/algorithm-journey/src/algorithm-journey/src/class035/Code07_AllO1.cpp)

## 其他相关题目

### 8. LFU缓存
- LeetCode 460: [LFU Cache](https://leetcode.com/problems/lfu-cache/)

### 9. 设计哈希映射
- LeetCode 706: [设计哈希映射](https://leetcode.com/problems/design-hashmap/)

### 10. 设计哈希集合
- LeetCode 705: [设计哈希集合](https://leetcode.com/problems/design-hashset/)

### 11. 设计停车系统
- 力扣 1603: [设计停车系统](https://leetcode.cn/problems/design-parking-system/)

### 12. 每隔n个顾客打折
- 力扣 1357: [每隔n个顾客打折](https://leetcode.cn/problems/apply-discount-every-n-orders/)

### 13. 设计自助结算系统
- LCR 184: [设计自助结算系统](https://leetcode.cn/problems/dui-lie-de-zhui-da-zhi-lcof/solutions/)

### 14. 滑动窗口中位数
- LeetCode 480: [滑动窗口中位数](https://leetcode.com/problems/sliding-window-median/)

### 15. 设计链表
- LeetCode 707: [设计链表](https://leetcode.com/problems/design-linked-list/)

### 16. 设计循环队列
- LeetCode 622: [设计循环队列](https://leetcode.com/problems/design-circular-queue/)

### 17. 设计循环双端队列
- LeetCode 641: [设计循环双端队列](https://leetcode.com/problems/design-circular-deque/)

### 18. 设计栈
- LeetCode 155: [最小栈](https://leetcode.com/problems/min-stack/)

### 19. 设计扁平迭代器
- LeetCode 251: [展开二维向量](https://leetcode.com/problems/flatten-2d-vector/)

### 20. 设计压缩字符串迭代器
- LeetCode 604: [设计压缩字符串迭代器](https://leetcode.com/problems/design-compressed-string-iterator/)

## 高级数据结构题目

### 21. 线段树相关题目
- 洛谷 P3372: [【模板】线段树 1](https://www.luogu.com.cn/problem/P3372)
- 洛谷 P3373: [【模板】线段树 2](https://www.luogu.com.cn/problem/P3373)

### 22. 平衡树相关题目
- 洛谷 P3369: [【模板】普通平衡树](https://www.luogu.com.cn/problem/P3369)
- 洛谷 P3391: [【模板】文艺平衡树](https://www.luogu.com.cn/problem/P3391)

### 23. 树状数组相关题目
- 洛谷 P3374: [【模板】树状数组 1](https://www.luogu.com.cn/problem/P3374)
- 洛谷 P3368: [【模板】树状数组 2](https://www.luogu.com.cn/problem/P3368)

### 24. 并查集相关题目
- 洛谷 P3367: [【模板】并查集](https://www.luogu.com.cn/problem/P3367)
- 洛谷 P1551: [亲戚](https://www.luogu.com.cn/problem/P1551)

### 25. 字典树相关题目
- 洛谷 P8306: [【模板】字典树](https://www.luogu.com.cn/problem/P8306)
- 洛谷 P2580: [于是他错误的点名开始了](https://www.luogu.com.cn/problem/P2580)

### 26. 堆相关题目
- 洛谷 P3378: [【模板】堆](https://www.luogu.com.cn/problem/P3378)
- 洛谷 P2251: [质数统计](https://www.luogu.com.cn/problem/P2251)

### 27. 主席树相关题目
- 洛谷 P3834: [【模板】可持久化线段树 1](https://www.luogu.com.cn/problem/P3834)
- 洛谷 P2617: [Dynamic Rankings](https://www.luogu.com.cn/problem/P2617)

### 28. 左偏树相关题目
- 洛谷 P3377: [【模板】左偏树（可并堆）](https://www.luogu.com.cn/problem/P3377)
- 洛谷 P1456: [Monkey King](https://www.luogu.com.cn/problem/P1456)

### 29. Link-Cut Tree相关题目
- 洛谷 P3690: [【模板】Link Cut Tree](https://www.luogu.com.cn/problem/P3690)
- 洛谷 P2147: [【SDOI2008】洞穴勘测](https://www.luogu.com.cn/problem/P2147)

### 30. 线性基相关题目
- 洛谷 P3812: [【模板】线性基](https://www.luogu.com.cn/problem/P3812)
- 洛谷 P4151: [【WC2011】最大XOR和路径](https://www.luogu.com.cn/problem/P4151)

## 面试高频题目

### 31. 设计Twitter
- LeetCode 355: [设计推特](https://leetcode.com/problems/design-twitter/)

### 32. 设计内存文件系统
- LeetCode 588: [设计内存文件系统](https://leetcode.com/problems/design-in-memory-file-system/)

### 33. 设计搜索自动补全系统
- LeetCode 642: [设计搜索自动补全系统](https://leetcode.com/problems/design-search-autocomplete-system/)

### 34. 设计日志存储系统
- LeetCode 359: [日志速率限制器](https://leetcode.com/problems/logger-rate-limiter/)

### 35. 设计敲击计数器
- LeetCode 362: [设计敲击计数器](https://leetcode.com/problems/design-hit-counter/)

### 36. 设计电话目录管理系统
- LeetCode 379: [设计电话目录](https://leetcode.com/problems/design-phone-directory/)

### 37. 设计栈排序
- LeetCode 394: [字符串解码](https://leetcode.com/problems/decode-string/)

### 38. 设计扁平化嵌套列表迭代器
- LeetCode 341: [扁平化嵌套列表迭代器](https://leetcode.com/problems/flatten-nested-list-iterator/)

### 39. 设计顶端迭代器
- LeetCode 284: [顶端迭代器](https://leetcode.com/problems/peeking-iterator/)

### 40. 设计最不经常使用（LFU）缓存算法
- LeetCode 460: [LFU缓存](https://leetcode.com/problems/lfu-cache/)