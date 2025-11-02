# FHQ-Treap 相关题目清单

## 1. 普通平衡树

### 题目描述
实现一种结构，支持如下操作：
1. 增加x，重复加入算多个词频
2. 删除x，如果有多个，只删掉一个
3. 查询x的排名，x的排名为，比x小的数的个数+1
4. 查询数据中排名为x的数
5. 查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
6. 查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值

### 相关题目

#### 洛谷 P3369：普通平衡树
- **题目链接**：[https://www.luogu.com.cn/problem/P3369](https://www.luogu.com.cn/problem/P3369)
- **难度**：普及+/提高
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **核心思路**：使用FHQ-Treap维护有序集合，实现插入、删除、查询排名等操作
- **代码实现**：
```java
// Java实现（带词频压缩）
public class FHQTreapWithCount {
    // 核心操作：split和merge
    // 通过随机优先级保证树的平衡
    // 详细实现见目录中的Code01_FHQTreapWithCount1.java
}
```

#### 洛谷 P2596：书架
- **题目链接**：[https://www.luogu.com.cn/problem/P2596](https://www.luogu.com.cn/problem/P2596)
- **难度**：提高
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个书架，支持将某本书置于顶部、底部、指定位置，查询某本书的位置等操作
- **核心思路**：使用FHQ-Treap维护书籍的位置关系，通过split和merge操作实现书籍的移动

#### SPOJ ORDERSET：Order statistic set
- **题目链接**：[https://www.spoj.com/problems/ORDERSET/](https://www.spoj.com/problems/ORDERSET/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个有序集合，支持插入、删除、查询第k小数、查询某数的排名等操作
- **核心思路**：使用FHQ-Treap维护集合，实现高效的统计操作

#### LeetCode 456. 132模式
- **题目链接**：[https://leetcode-cn.com/problems/132-pattern/](https://leetcode-cn.com/problems/132-pattern/)
- **难度**：中等
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n)
- **题目描述**：给定一个整数序列，判断是否存在一个132模式的子序列
- **核心思路**：使用FHQ-Treap维护可能的3值，高效查询前驱

#### LeetCode 2336. 无限集中的最小数字
- **题目链接**：[https://leetcode-cn.com/problems/smallest-number-in-infinite-set/](https://leetcode-cn.com/problems/smallest-number-in-infinite-set/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个包含所有正整数的无限集，支持弹出最小元素和添加元素
- **核心思路**：使用FHQ-Treap维护已删除的元素，高效查询最小可用元素

## 2. 文艺平衡树

### 题目描述
长度为n的序列，下标从1开始，一开始序列为1, 2, ..., n。接下来会有k个操作，每个操作给定l，r，表示从l到r范围上的所有数字翻转。做完k次操作后，从左到右打印所有数字。

### 相关题目

#### 洛谷 P3391：文艺平衡树
- **题目链接**：[https://www.luogu.com.cn/problem/P3391](https://www.luogu.com.cn/problem/P3391)
- **难度**：提高
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **核心思路**：使用FHQ-Treap维护序列，通过split和merge操作结合懒惰标记实现区间翻转
- **代码实现**：
```java
// Java实现
public class LiteraryTree {
    // 使用懒惰标记表示区间翻转
    // 详细实现见目录中的Code04_LiteraryTree1.java
}
```

#### 洛谷 P4146：序列终结者
- **题目链接**：[https://www.luogu.com.cn/problem/P4146](https://www.luogu.com.cn/problem/P4146)
- **难度**：省选/NOI-
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个序列，支持区间翻转、区间加、查询区间最大值等操作
- **核心思路**：使用FHQ-Treap维护序列，结合多个懒惰标记实现复杂的区间操作

#### SPOJ CTRICK：Card Trick
- **题目链接**：[https://www.spoj.com/problems/CTRICK/](https://www.spoj.com/problems/CTRICK/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：实现一个卡牌魔术，支持特殊的卡牌序列操作
- **核心思路**：使用FHQ-Treap模拟卡牌的插入过程

#### LeetCode 1845. 座位预约管理系统
- **题目链接**：[https://leetcode-cn.com/problems/seat-reservation-manager/](https://leetcode-cn.com/problems/seat-reservation-manager/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个座位预约系统，支持预约和释放座位
- **核心思路**：使用FHQ-Treap维护可用座位，高效查询最小可用座位

## 3. 可持久化平衡树

### 题目描述
认为一开始是0版本的树，为空树，实现如下操作，操作一共发生n次：
- v 1 x : 基于v版本的树，增加一个x，生成新版本的树
- v 2 x : 基于v版本的树，删除一个x，生成新版本的树
- v 3 x : 基于v版本的树，查询x的排名，生成新版本的树状况=v版本状况
- v 4 x : 基于v版本的树，查询数据中排名为x的数，生成新版本的树状况=v版本状况
- v 5 x : 基于v版本的树，查询x的前驱，生成新版本的树状况=v版本状况
- v 6 x : 基于v版本的树，查询x的后继，生成新版本的树状况=v版本状况

### 相关题目

#### 洛谷 P3835：可持久化平衡树
- **题目链接**：[https://www.luogu.com.cn/problem/P3835](https://www.luogu.com.cn/problem/P3835)
- **难度**：省选/NOI-
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n log n)
- **核心思路**：在FHQ-Treap的基础上，通过复制路径上的节点实现可持久化
- **代码实现**：
```java
// Java实现
public class PersistentFHQTreap {
    // 每次修改时复制节点，保留历史版本
    // 详细实现见目录中的Code05_PersistentFHQTreap1.java
}
```

#### SPOJ TTM：To the moon
- **题目链接**：[https://www.spoj.com/problems/TTM/](https://www.spoj.com/problems/TTM/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n log n)
- **题目描述**：维护一个可持久化数组，支持历史版本查询和修改
- **核心思路**：使用可持久化FHQ-Treap维护数组，支持区间修改和查询

#### LeetCode 1146. 快照数组
- **题目链接**：[https://leetcode-cn.com/problems/snapshot-array/](https://leetcode-cn.com/problems/snapshot-array/)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n log n)
- **题目描述**：实现一个支持快照的数组，能够在某个时间点创建快照并查询历史版本
- **核心思路**：虽然有更简单的解法，但可持久化FHQ-Treap是一种通用高效的解法

## 4. 文本编辑器

### 题目描述
一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作：
- Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
- Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
- Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
- Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
- Prev       : 光标前移一个字符，操作保证光标不会到非法位置
- Next       : 光标后移一个字符，操作保证光标不会到非法位置

### 相关题目

#### 洛谷 P4008：文本编辑器
- **题目链接**：[https://www.luogu.com.cn/problem/P4008](https://www.luogu.com.cn/problem/P4008)
- **难度**：省选/NOI-
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **核心思路**：使用FHQ-Treap维护文本，通过split和merge操作实现文本的插入、删除和查询
- **代码实现**：
```java
// Java实现
public class TextEditor {
    // 使用FHQ-Treap维护文本内容
    // 详细实现见目录中的Code03_TextEditor1.java
}
```

#### 洛谷 P2042：维护数列
- **题目链接**：[https://www.luogu.com.cn/problem/P2042](https://www.luogu.com.cn/problem/P2042)
- **难度**：省选/NOI
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个数列，支持插入、删除、翻转、求和、最大子段和等复杂操作
- **核心思路**：使用FHQ-Treap维护数列，结合多个懒惰标记和区间信息维护

## 5. 可持久化文艺平衡树

### 题目描述
一开始序列为空，实现如下操作，操作一共发生n次：
- v 1 x y : 基于v版本的序列，在第x个数后插入y，生成新版本的序列
- v 2 x   : 基于v版本的序列，删除第x个数，生成新版本的序列
- v 3 x y : 基于v版本的序列，范围[x,y]所有数字翻转，生成新版本的序列
- v 4 x y : 基于v版本的序列，查询范围[x,y]所有数字的和，生成新版本的序列状况=v版本状况

### 相关题目

#### 洛谷 P5055：可持久化文艺平衡树
- **题目链接**：[https://www.luogu.com.cn/problem/P5055](https://www.luogu.com.cn/problem/P5055)
- **难度**：省选/NOI-
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n log n)
- **核心思路**：结合可持久化技术和文艺平衡树的特性，支持历史版本的区间操作
- **代码实现**：
```java
// Java实现
public class PersistentLiteraryTree {
    // 结合可持久化和区间操作
    // 详细实现见目录中的Code06_PersistentLiteraryTree1.java
}
```

## 6. 普通平衡树（数据加强版）

### 题目描述
与普通平衡树类似，但数据规模更大，需要更高的效率。

### 相关题目

#### 洛谷 P6136：普通平衡树（数据加强版）
- **题目链接**：[https://www.luogu.com.cn/problem/P6136](https://www.luogu.com.cn/problem/P6136)
- **难度**：提高+/省选-
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **核心思路**：优化FHQ-Treap的实现，处理大规模数据

## 7. 第k小查询

### 题目描述
维护一个序列，支持查询区间第k小元素。

### 相关题目

#### POJ 2761：Feed the dogs
- **题目链接**：[http://poj.org/problem?id=2761](http://poj.org/problem?id=2761)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：在一个序列中，查询指定区间内第k小的元素
- **核心思路**：使用FHQ-Treap维护区间元素，高效查询第k小

#### LeetCode 373. 查找和最小的K对数字
- **题目链接**：[https://leetcode-cn.com/problems/find-k-pairs-with-smallest-sums/](https://leetcode-cn.com/problems/find-k-pairs-with-smallest-sums/)
- **难度**：中等
- **时间复杂度**：O(k log k)
- **空间复杂度**：O(k)
- **题目描述**：找出两个数组中，和最小的k对数字
- **核心思路**：使用FHQ-Treap维护候选对，高效查询最小和

#### LeetCode 658. 找到K个最接近的元素
- **题目链接**：[https://leetcode-cn.com/problems/find-k-closest-elements/](https://leetcode-cn.com/problems/find-k-closest-elements/)
- **难度**：中等
- **时间复杂度**：O(log n + k)
- **空间复杂度**：O(n)
- **题目描述**：在排序数组中找到k个最接近目标值的元素
- **核心思路**：FHQ-Treap可以高效支持范围查询和排序

## 8. 图查询

### 题目描述
维护一个图结构，支持图相关的查询操作。

### 相关题目

#### Codeforces F. Graph and Queries
- **题目链接**：[https://codeforces.com/contest/1416/problem/F](https://codeforces.com/contest/1416/problem/F)
- **难度**：省选/NOI
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：维护一个图，支持删除边、查询连通分量最大值等操作
- **核心思路**：使用FHQ-Treap维护连通分量的信息，支持高效查询

## 9. 区间查询

### 题目描述
维护一个序列，支持查询区间内不重复元素的个数。

### 相关题目

#### AtCoder F. Range Set Query
- **题目链接**：[https://atcoder.jp/contests/abc174/tasks/abc174_f](https://atcoder.jp/contests/abc174/tasks/abc174_f)
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **题目描述**：查询区间内不同颜色的宝石数量
- **核心思路**：使用FHQ-Treap维护区间的唯一元素集合

## 10. 员工工资管理问题

### 题目描述
维护员工工资信息，支持以下操作：
- 插入新员工（指定工资）
- 整体涨薪
- 整体降薪（自动移除工资低于最低工资标准的员工）
- 查询第k高的工资

### 相关题目

#### 员工工资管理系统
- **难度**：中等
- **时间复杂度**：O(log n)（单次操作）
- **空间复杂度**：O(n)
- **核心思路**：使用FHQ-Treap维护员工工资，通过维护delta避免频繁更新所有节点
- **代码实现**：
```cpp
// C++实现示例
struct Node {
    int l, r; // 左右子节点
    int w;    // 存储的工资基础值（实际工资 = w + delta）
    int size; // 子树大小
    int prio; // 随机优先级
} tr[MAXN];

int delta; // 全局工资增量
int m;     // 最低工资标准

// 关键操作：通过分裂和合并维护工资信息
```

## 解题技巧总结

### 1. 普通平衡树操作
- **插入操作**：通过split分割，然后merge合并
- **删除操作**：找到要删除的节点，通过多次split和merge重建树
- **排名查询**：统计小于目标值的节点数+1
- **第k小查询**：根据子树大小递归查找
- **前驱后继**：通过split分割，然后在相应子树中查找最值

### 2. 区间操作技巧
- **懒惰标记**：延迟更新，在split和merge时进行下传
- **区间翻转**：交换左右子树，递归处理
- **区间修改**：维护区间和、区间最大值等信息，结合懒惰标记

### 3. 可持久化技巧
- **路径复制**：修改时复制路径上的节点，不影响原版本
- **版本管理**：维护每个版本的根节点
- **内存优化**：预分配内存池，避免频繁申请内存

### 4. 性能优化技巧
- **词频压缩**：相同值的节点只存储一次，维护计数
- **非递归实现**：减少函数调用开销
- **内存池**：预先分配节点空间，提高内存分配效率
- **批处理**：对于批量操作，可以合并处理减少开销

### 5. 常见错误点
- **split和merge的顺序**：操作顺序错误会导致树结构错误
- **懒惰标记的下传**：忘记下传标记会导致计算错误
- **边界条件处理**：空树、单节点树等特殊情况
- **随机数生成**：确保随机数的质量，避免树退化
- **内存管理**：避免内存泄漏和越界访问

### 6. 调试技巧
- **中序遍历**：验证树的BST性质
- **打印树结构**：可视化树的形状，帮助理解
- **分步调试**：跟踪split和merge的过程
- **边界测试**：测试空树、极值等特殊情况

FHQ-Treap作为一种强大而灵活的数据结构，通过分裂和合并两个基本操作，能够高效解决各种复杂的动态集合和序列维护问题。掌握其核心原理和实现技巧，对于提升算法能力和解决实际问题都有很大帮助。