# 分块算法题目列表

## LOJ分块入门系列

### 1. LOJ #6277. 数列分块入门1
- **题目**：区间加法，单点查询
- **链接**：https://loj.ac/p/6277
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间加法，单点查值。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都加c
  - 操作 1 l r c : 询问ar的值（l和c忽略）
- **实现**：
  - Java: Code01_BlockProblem1_1.java
  - C++: Code01_BlockProblem1_2.cpp
  - Python: Code01_BlockProblem1_3.py

### 2. LOJ #6278. 数列分块入门2
- **题目**：区间加法，查询区间内小于某个值x的元素个数
- **链接**：https://loj.ac/p/6278
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的元素个数。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都加c
  - 操作 1 l r c : 询问[l,r]区间内小于c*c的数字的个数
- **实现**：
  - Java: Code02_BlockProblem2_1.java
  - C++: Code02_BlockProblem2_2.cpp
  - Python: Code02_BlockProblem2_3.py

### 3. LOJ #6279. 数列分块入门3
- **题目**：区间加法，查询区间内小于某个值x的前驱（比其小的最大元素）
- **链接**：https://loj.ac/p/6279
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的前驱（比其小的最大元素）。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都加c
  - 操作 1 l r c : 询问[l,r]区间内小于c的前驱（比其小的最大元素）
- **实现**：
  - Java: Code03_BlockProblem3_1.java
  - C++: Code03_BlockProblem3_2.cpp
  - Python: Code03_BlockProblem3_3.py

### 4. LOJ #6280. 数列分块入门4
- **题目**：区间加法，区间求和
- **链接**：https://loj.ac/p/6280
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都加c
  - 操作 1 l r c : 询问[l,r]区间的和 mod (c+1)
- **实现**：
  - Java: Code04_BlockProblem4_1.java
  - C++: Code04_BlockProblem4_2.cpp
  - Python: Code04_BlockProblem4_3.py

### 5. LOJ #6281. 数列分块入门5
- **题目**：区间开方，区间求和
- **链接**：https://loj.ac/p/6281
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间开方（下取整），区间求和。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都开方（下取整）
  - 操作 1 l r c : 询问[l,r]区间的和
- **实现**：
  - Java: Code05_BlockProblem5_1.java
  - C++: Code05_BlockProblem5_2.cpp
  - Python: Code05_BlockProblem5_3.py

### 6. LOJ #6282. 数列分块入门6
- **题目**：单点插入，单点询问
- **链接**：https://loj.ac/p/6282
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点询问。
- **操作**：
  - 操作 0 l r c : 在位置l后面插入数字c（r忽略）
  - 操作 1 l r c : 询问位置l的数字（r和c忽略）
- **实现**：
  - Java: Code06_BlockProblem1_1.java
  - C++: Code06_BlockProblem1_2.cpp
  - Python: Code06_BlockProblem1_3.py

### 7. LOJ #6283. 数列分块入门7
- **题目**：区间乘法，区间加法，单点查询
- **链接**：https://loj.ac/p/6283
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间乘法，区间加法，单点查询。
- **操作**：
  - 操作 0 l r c : 将位于[l,r]的之间的数字都加c
  - 操作 1 l r c : 将位于[l,r]的之间的数字都乘c
  - 操作 2 l r c : 询问ar的值 mod 10007（l和c忽略）
- **实现**：待补充

### 8. LOJ #6284. 数列分块入门8
- **题目**：区间询问等于一个数c的元素，并将这个区间的所有元素改为c
- **链接**：https://loj.ac/p/6284
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及区间询问等于一个数c的元素，并将这个区间的所有元素改为c。
- **操作**：
  - 操作 0 l r c : 先查询[l,r]的数字有多少个是c，再把位于[l,r]的数字都改为c
- **实现**：待补充

### 9. LOJ #6285. 数列分块入门9
- **题目**：询问区间的最小众数
- **链接**：https://loj.ac/p/6285
- **描述**：给出一个长为n的数列，以及n个操作，操作涉及询问区间的最小众数。
- **操作**：
  - 操作 0 l r c : 询问[l,r]区间的最小众数（r和c忽略）
- **实现**：待补充

## 其他经典分块题目

### 10. 洛谷 P4168 [Violet]蒲公英
- **题目**：区间众数查询
- **链接**：https://www.luogu.com.cn/problem/P4168
- **描述**：给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间里出现次数最多的是哪种数，如果有若干种数出现次数相同，则输出种类编号最小的那个。
- **实现**：待补充

### 11. 洛谷 P2801 教主的魔法
- **题目**：区间加法，区间大于等于查询
- **链接**：https://www.luogu.com.cn/problem/P2801
- **描述**：给定一个长度为n的序列，支持区间加法操作和区间大于等于查询。
- **操作**：
  - M l r w : 对闭区间[l,r]内的英雄的身高全部加上w
  - A l r c : 询问闭区间[l,r]内有多少英雄的身高大于等于c
- **实现**：待补充

### 12. 洛谷 P4145 上帝造题的七分钟2 / 花神游历各国
- **题目**：区间开方，区间求和
- **链接**：https://www.luogu.com.cn/problem/P4145
- **描述**：给定一个长度为n的序列，支持区间开方（下取整）操作和区间求和查询。
- **操作**：
  - 0 l r : 给[l,r]中每个数开平方（下取整）
  - 1 l r : 询问[l,r]中各个数的和
- **实现**：
  - Java: Code12_BlockProblem1_1.java
  - C++: Code12_BlockProblem1_2.cpp
  - Python: Code12_BlockProblem1_3.py

### 13. SPOJ DQUERY - D-query
- **题目**：区间不同数的个数
- **链接**：https://www.spoj.com/problems/DQUERY/
- **描述**：给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间里有多少个不同的数。
- **实现**：
  - Java: Code13_BlockProblem1_1.java
  - C++: Code13_BlockProblem1_2.cpp
  - Python: Code13_BlockProblem1_3.py

### 14. Codeforces 86D - Powerful array
- **题目**：区间权值和查询
- **链接**：https://codeforces.com/problemset/problem/86/D
- **描述**：给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间内每个数出现次数的平方乘以这个数的和。
- **实现**：待补充

### 15. Codeforces 1485C - Floor and Mod
- **题目**：数学分块
- **链接**：https://codeforces.com/problemset/problem/1485/C
- **描述**：给定x,y，询问当1≤a≤x, 1≤b≤y时，⌊a/b⌋=a mod b的对数。
- **实现**：待补充

### 16. HDU 4358 - Boring counting
- **题目**：树上分块
- **链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4358
- **描述**：给定一棵树，每个节点有一个权值，每次询问一个节点的子树中，有多少个节点的权值恰好出现k次。
- **实现**：待补充

## 总结

以上是分块算法的经典题目列表，涵盖了分块算法的各种应用场景。通过这些题目的练习，可以深入理解分块算法的核心思想和实现技巧。