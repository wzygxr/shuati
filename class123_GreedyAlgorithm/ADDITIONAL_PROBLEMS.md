# Class092 贪心算法专题 - 补充题目清单

本文件整理了与class092中贪心算法问题相关的更多练习题目，来源于各大算法平台。

## 📚 按平台分类

### LeetCode (力扣)
1. **LeetCode 45. 跳跃游戏 II** - https://leetcode.cn/problems/jump-game-ii/
   - 类型：贪心算法
   - 难度：中等
   - 核心思想：维护当前能到达的最远位置和下一步能到达的最远位置

2. **LeetCode 135. 分发糖果** - https://leetcode.cn/problems/candy/
   - 类型：贪心算法
   - 难度：困难
   - 核心思想：两次扫描，分别处理左右两个方向的约束条件

3. **LeetCode 134. 加油站** - https://leetcode.cn/problems/gas-station/
   - 类型：贪心算法
   - 难度：中等
   - 核心思想：如果总油量减去总消耗量大于等于0，那么一定存在解

4. **LeetCode 781. 森林中的兔子** - https://leetcode.cn/problems/rabbits-in-forest/
   - 类型：贪心算法
   - 难度：中等
   - 核心思想：尽量让叫相同数值的兔子是同一颜色

5. **LeetCode 1675. 数组的最小偏移量** - https://leetcode.cn/problems/minimize-deviation-in-array/
   - 类型：贪心算法 + 有序集合
   - 难度：困难
   - 核心思想：通过有序集合维护最大值和最小值

6. **LeetCode 2449. 使数组相似的最少操作次数** - https://leetcode.cn/problems/minimum-number-of-operations-to-make-arrays-similar/
   - 类型：贪心算法
   - 难度：困难
   - 核心思想：奇偶数分离后排序匹配

7. **LeetCode 871. 最低加油次数** - https://leetcode.cn/problems/minimum-number-of-refueling-stops/
   - 类型：贪心算法 + 优先队列
   - 难度：困难
   - 核心思想：在油即将用光时，选择油量最多的加油站加油

8. **LeetCode 179. 最大数** - https://leetcode.cn/problems/largest-number/
   - 类型：贪心算法 + 自定义排序
   - 难度：中等
   - 核心思想：自定义排序规则，使拼接后的数字最大

9. **LeetCode 402. 移掉K位数字** - https://leetcode.cn/problems/remove-k-digits/
   - 类型：贪心算法 + 单调栈
   - 难度：中等
   - 核心思想：维护单调递增栈，使结果最小

10. **LeetCode 452. 用最少数量的箭引爆气球** - https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
    - 类型：贪心算法 + 区间调度
    - 难度：中等
    - 核心思想：按右端点排序，尽可能多地引爆重叠气球

### 牛客网 (NowCoder)
1. **牛客网 NC128 接雨水问题** - https://www.nowcoder.com/practice/31c1aed01b394f0b8b7734de0324e00f
   - 类型：贪心算法 + 双指针
   - 难度：困难
   - 核心思想：维护左右两边的最大高度

2. **牛客网 NC50 子数组最大累加和** - https://www.nowcoder.com/practice/559291275c16468cb7499375d2d4920d
   - 类型：贪心算法 (Kadane算法)
   - 难度：中等
   - 核心思想：维护当前子数组和与全局最大和

3. **牛客网 NC123 表达式求值** - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
   - 类型：贪心算法 + 栈
   - 难度：困难
   - 核心思想：使用栈处理表达式求值

### 洛谷 (Luogu)
1. **洛谷 P1090 合并果子** - https://www.luogu.com.cn/problem/P1090
   - 类型：贪心算法 + 优先队列
   - 难度：普及-
   - 核心思想：每次合并最小的两堆果子

2. **洛谷 P1223 排队接水** - https://www.luogu.com.cn/problem/P1223
   - 类型：贪心算法 + 排序
   - 难度：普及-
   - 核心思想：按接水时间升序排列

3. **洛谷 P1803 凌乱的yyy / 线段覆盖** - https://www.luogu.com.cn/problem/P1803
   - 类型：贪心算法 + 区间调度
   - 难度：普及-
   - 核心思想：按结束时间排序，尽可能多地选择不重叠区间

### Codeforces
1. **Codeforces 1360B - Honest Coach** - https://codeforces.com/problemset/problem/1360/B
   - 类型：贪心算法 + 排序
   - 难度：800
   - 核心思想：排序后找相邻元素的最小差值

2. **Codeforces 1367B - Even Array** - https://codeforces.com/problemset/problem/1367/B
   - 类型：贪心算法
   - 难度：800
   - 核心思想：统计奇偶位置不匹配的元素数量

### AtCoder
1. **AtCoder ABC143C - Slimes** - https://atcoder.jp/contests/abc143/tasks/abc143_c
   - 类型：贪心算法
   - 难度：灰
   - 核心思想：统计连续相同字符的段数
2. **AtCoder ABC153F - Silver Fox vs Monster** - https://atcoder.jp/contests/abc153/tasks/abc153_f
   - 类型：贪心算法 + 前缀和
   - 难度：蓝
   - 核心思想：按位置排序，使用贪心策略和前缀和计算最小攻击次数
3. **AtCoder ABC127D - Integer Cards** - https://atcoder.jp/contests/abc127/tasks/abc127_d
   - 类型：贪心算法 + 优先队列
   - 难度：黄
   - 核心思想：优先队列维护最小值，贪心替换

### HackerRank
1. **HackerRank - Greedy Florist** - https://www.hackerrank.com/challenges/greedy-florist/problem
   - 类型：贪心算法 + 排序
   - 难度：中等
   - 核心思想：价格高的花优先分配给购买次数少的人
2. **HackerRank - Mark and Toys** - https://www.hackerrank.com/challenges/mark-and-toys/problem
   - 类型：贪心算法 + 排序
   - 难度：简单
   - 核心思想：按价格排序，优先购买价格低的玩具
3. **HackerRank - Max Min** - https://www.hackerrank.com/challenges/angry-children/problem
   - 类型：贪心算法 + 排序
   - 难度：中等
   - 核心思想：排序后滑动窗口选择最小差异

### 补充题目
1. **LeetCode 455. 分发饼干** - https://leetcode.cn/problems/assign-cookies/
   - 类型：贪心算法
   - 难度：简单
   - 核心思想：优先满足胃口小的孩子，优先使用尺寸小的饼干

2. **LeetCode 179. 最大数** - https://leetcode.cn/problems/largest-number/
   - 类型：贪心算法 + 自定义排序
   - 难度：中等
   - 核心思想：自定义排序规则，使拼接后的数字最大

3. **洛谷 P1090 合并果子** - https://www.luogu.com.cn/problem/P1090
   - 类型：贪心算法 + 优先队列
   - 难度：普及-
   - 核心思想：每次合并最小的两堆果子

4. **LeetCode 435. 无重叠区间** - https://leetcode.cn/problems/non-overlapping-intervals/
   - 类型：贪心算法 + 区间调度
   - 难度：中等
   - 核心思想：按右端点排序，移除最少的重叠区间

5. **LeetCode 55. 跳跃游戏** - https://leetcode.cn/problems/jump-game/
   - 类型：贪心算法
   - 难度：中等
   - 核心思想：维护能到达的最远位置

6. **CodeChef - STICKS** - https://www.codechef.com/problems/STICKS
   - 类型：贪心算法 + 计数
   - 难度：简单
   - 核心思想：统计相同长度的木棍，优先选择较长的木棍组成正方形

7. **SPOJ - AGGRCOW** - https://www.spoj.com/problems/AGGRCOW/
   - 类型：贪心算法 + 二分查找
   - 难度：中等
   - 核心思想：二分查找最大最小距离，贪心检查可行性

8. **POJ 3253 - Fence Repair** - http://poj.org/problem?id=3253
   - 类型：贪心算法 + 优先队列
   - 难度：简单
   - 核心思想：每次合并最小的两段篱笆，类似哈夫曼编码

9. **UVa 11100 - The Trip, 2007** - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2041
   - 类型：贪心算法 + 排序
   - 难度：简单
   - 核心思想：排序后计算最小搬运距离

10. **牛客网 NC87 丢棋子问题** - https://www.nowcoder.com/practice/d1418aaa147a4cb394c3c3efc4302266
    - 类型：贪心算法 + 动态规划
    - 难度：困难
    - 核心思想：确定在最优策略下的最少尝试次数

11. **杭电 OJ 2037 - 今年暑假不AC** - http://acm.hdu.edu.cn/showproblem.php?pid=2037
    - 类型：贪心算法 + 区间调度
    - 难度：简单
    - 核心思想：按结束时间排序，选择最多不重叠的节目

12. **计蒜客 T1594 - 贪心的国王** - https://nanti.jisuanke.com/t/T1594
    - 类型：贪心算法 + 优先队列
    - 难度：中等
    - 核心思想：优先选择金币最多且风险最小的任务

13. **AcWing 125. 耍杂技的牛** - https://www.acwing.com/problem/content/127/
    - 类型：贪心算法 + 排序
    - 难度：中等
    - 核心思想：按重量和强壮度之和排序

14. **MarsCode - 会议室预约** - https://www.marscode.top/problem/1004
    - 类型：贪心算法 + 区间调度
    - 难度：中等
    - 核心思想：按结束时间排序，最大化会议数量

15. **AizuOJ ALDS1_10_B - Matrix Chain Multiplication** - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_10_B
    - 类型：贪心算法 + 动态规划
    - 难度：中等
    - 核心思想：矩阵链乘法最优计算顺序

### 堆与优先队列相关贪心问题

这类问题通常需要维护多个可能的候选解，并在每一步选择最优的候选。优先队列是解决这类问题的有效工具。

#### LeetCode 215. 数组中的第K个最大元素
**题目链接**：https://leetcode.cn/problems/kth-largest-element-in-an-array/
**难度**：中等
**核心算法**：堆排序、快速选择
**思路**：可以使用最大堆或最小堆，也可以使用快速选择算法。

#### LeetCode 239. 滑动窗口最大值
**题目链接**：https://leetcode.cn/problems/sliding-window-maximum/
**难度**：困难
**核心算法**：单调队列、优先队列
**思路**：维护一个滑动窗口内的最大值队列，可以使用单调队列优化时间复杂度。

#### LeetCode 621. 任务调度器
**题目链接**：https://leetcode.cn/problems/task-scheduler/
**难度**：中等
**核心算法**：贪心算法、优先队列、数学优化
**思路**：
1. 统计每个任务的频率
2. 使用最大堆维护任务频率，优先处理高频任务
3. 或者使用数学公式直接计算最短时间
**关键点**：
- 两种解法：优先队列模拟和数学公式优化
- 数学公式：(maxFreq-1)*(n+1) + maxFreqCount
- 时间复杂度优化：从O(m log k)到O(m)

#### LeetCode 355. 设计推特
**题目链接**：https://leetcode.cn/problems/design-twitter/
**难度**：中等
**核心算法**：贪心算法、优先队列
**思路**：
1. 使用哈希表存储用户发布的推文和关注关系
2. 使用优先队列合并多个有序推文列表，每次取出最新的推文
3. 维护全局时间戳记录推文发布顺序
**关键点**：
- 使用链表存储用户的推文，便于快速插入新推文
- 使用优先队列高效合并多个有序列表
- 优化空间使用，只保留必要的信息

## 🧠 贪心算法常见题型与解题思路

### 1. 区间调度问题
**适用场景**：
- 活动安排问题
- 会议室安排问题
- 气球引爆问题

**解题思路**：
- 按区间右端点排序
- 贪心选择最早结束的活动

### 2. 分配问题
**适用场景**：
- 分糖果问题
- 任务分配问题
- 资源分配问题

**解题思路**：
- 局部最优策略
- 两次扫描处理双向约束

### 3. 序列变换问题
**适用场景**：
- 数组变换问题
- 字符串变换问题
- 数字组合问题

**解题思路**：
- 自定义排序规则
- 贪心选择最优变换策略

### 4. 图论相关贪心
**适用场景**：
- 最小生成树(Kruskal, Prim)
- 单源最短路径(Dijkstra)
- 拓扑排序

**解题思路**：
- 选择当前最优边或点
- 维护最优解的性质

### 5. 资源分配问题
**适用场景**：
- 分发饼干问题
- 花店经营问题
- 任务调度问题

**解题思路**：
- 优先处理高价值资源
- 合理分配有限资源
- 根据优先级进行排序

### 6. 跳跃游戏类问题
**适用场景**：
- 跳跃游戏
- 加油站问题
- 路径可达性问题

**解题思路**：
- 维护当前能到达的最远位置
- 贪心选择最优跳点
- 累积检查可行性

## 🎯 贪心算法解题模板

### 1. 问题建模
```
1. 确定贪心策略
2. 验证贪心选择性质
3. 验证最优子结构性质
```

### 2. 算法实现
```
1. 排序预处理（如需要）
2. 贪心选择
3. 更新状态
4. 重复步骤2-3直到结束
```

### 3. 正确性证明
```
1. 贪心选择性质：每一步的贪心选择都能得到全局最优解
2. 最优子结构：问题的最优解包含子问题的最优解
```

## 📈 复杂度分析

### 时间复杂度
- 排序预处理：O(n log n)
- 单次遍历：O(n)
- 优先队列操作：O(log n)

### 空间复杂度
- 原地算法：O(1)
- 需要辅助数组：O(n)
- 优先队列：O(n)

## 🔧 工程化考虑

### 1. 边界条件处理
- 空数组/空集合
- 单元素情况
- 极端输入值

### 2. 异常处理
- 输入参数验证
- 非法操作检查
- 错误信息提示

### 3. 性能优化
- 避免重复计算
- 合理使用数据结构
- 减少不必要的操作

### 4. 可读性提升
- 变量命名清晰
- 注释详细完整
- 代码结构清晰

## 🧪 测试用例设计

### 基本测试用例
1. 空输入
2. 单元素输入
3. 已排序输入
4. 逆序输入
5. 重复元素输入

### 边界测试用例
1. 极大值/极小值
2. 相同元素
3. 特殊模式

### 性能测试用例
1. 大规模数据
2. 随机数据
3. 最坏情况数据

## 🆕 新增补充题目（Code20-24）

### Code20: 盛最多水的容器 (LeetCode 11)
**题目链接**：https://leetcode.cn/problems/container-with-most-water/
**难度**：中等
**核心算法**：贪心 + 双指针
**解题思路**：
- 使用双指针从两端向中间移动
- 每次移动高度较小的指针
- 计算当前容器的面积并更新最大值
**时间复杂度**：O(n)
**空间复杂度**：O(1)

### Code21: 接雨水 (LeetCode 42)
**题目链接**：https://leetcode.cn/problems/trapping-rain-water/
**难度**：困难
**核心算法**：贪心 + 双指针/单调栈
**解题思路**：
- 双指针法：维护左右最大高度
- 单调栈法：维护递减栈计算雨水
**时间复杂度**：O(n)
**空间复杂度**：O(1) 或 O(n)

### Code22: 柠檬水找零 (LeetCode 860)
**题目链接**：https://leetcode.cn/problems/lemonade-change/
**难度**：简单
**核心算法**：贪心
**解题思路**：
- 优先使用10美元找零20美元
- 次优使用5美元找零
- 维护5美元和10美元的数量
**时间复杂度**：O(n)
**空间复杂度**：O(1)

### Code23: 买卖股票的最佳时机 (LeetCode 121)
**题目链接**：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
**难度**：简单
**核心算法**：贪心
**解题思路**：
- 维护历史最低价格
- 计算当前价格与最低价格的差值
- 更新最大利润
**时间复杂度**：O(n)
**空间复杂度**：O(1)

### Code24: 优势洗牌 (LeetCode 870)
**题目链接**：https://leetcode.cn/problems/advantage-shuffle/
**难度**：中等
**核心算法**：贪心 + 田忌赛马策略
**解题思路**：
- 排序两个数组
- 使用双指针匹配最优对局
- 最大化优势数量
**时间复杂度**：O(n log n)
**空间复杂度**：O(n)

## 📚 参考资料

1. 《算法导论》第16章 贪心算法
2. 《编程珠玑》相关章节
3. LeetCode官方题解
4. 各大OJ平台题目解析