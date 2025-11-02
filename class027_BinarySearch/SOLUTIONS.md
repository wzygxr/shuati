# 二分答案专题题解与总结

## 题目概览

本专题主要涵盖二分答案相关的经典题目，包括基础的二分搜索、二分答案求解最值问题等。

## 题目列表

### 1. LeetCode 875. 爱吃香蕉的珂珂 (Code01_KokoEatingBananas.java)
- **问题描述**: 珂珂吃香蕉，需要在h小时内吃完所有堆，求最小速度
- **解法**: 二分答案 + 贪心验证
- **时间复杂度**: O(n * log(max))
- **空间复杂度**: O(1)

### 2. LeetCode 410. 分割数组的最大值 (Code02_SplitArrayLargestSum.java)
- **问题描述**: 将数组分成m段，使各段和的最大值最小
- **解法**: 二分答案 + 贪心验证
- **时间复杂度**: O(n * log(sum))
- **空间复杂度**: O(1)

### 3. 牛客网 机器人跳跃问题 (Code03_RobotPassThroughBuilding.java)
- **问题描述**: 机器人跳跃建筑，求能通关的最小初始能量
- **解法**: 二分答案 + 模拟验证
- **时间复杂度**: O(n * log(max))
- **空间复杂度**: O(1)

### 4. LeetCode 719. 找出第K小的数对距离 (Code04_FindKthSmallestPairDistance.java)
- **问题描述**: 求数组中所有数对距离的第K小值
- **解法**: 二分答案 + 双指针计数
- **时间复杂度**: O(n * log(n) + n * log(max-min))
- **空间复杂度**: O(log(n))

### 5. LeetCode 2141. 同时运行N台电脑的最长时间 (Code05_MaximumRunningTimeOfNComputers.java)
- **问题描述**: 用给定电池让n台电脑同时运行的最长时间
- **解法**: 二分答案 + 贪心验证
- **时间复杂度**: O(n * log(sum))
- **空间复杂度**: O(1)

### 6. 等位时间问题 (Code06_WaitingTime.java, Code06_WaitingTime2.java)
- **问题描述**: 计算第w+1个客人需要等待的时间
- **解法**: 二分答案 + 数学计算
- **时间复杂度**: O(n * log(min * w))
- **空间复杂度**: O(1)

### 7. 刀砍毒杀怪兽问题 (Code07_CutOrPoison.java)
- **问题描述**: 杀死怪兽的最少回合数
- **解法**: 二分答案 + 贪心策略
- **时间复杂度**: O(n * log(hp))
- **空间复杂度**: O(1)

### 8. LeetCode 1011. 在D天内送达包裹的能力 (Code08_CapacityToShipPackages.java)
- **问题描述**: 找到能在D天内运输完所有包裹的最低运载能力
- **解法**: 二分答案 + 贪心验证
- **时间复杂度**: O(n * log(sum))
- **空间复杂度**: O(1)

### 9. LeetCode 1482. 制作m束花所需的时间 (Code09_MinimumNumberOfDaysToMakeBouquets.java)
- **问题描述**: 制作m束花需要等待的最少天数
- **解法**: 二分答案 + 贪心验证
- **时间复杂度**: O(n * log(max))
- **空间复杂度**: O(1)

### 10. LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 (Code10_FindFirstAndLastPosition.java)
- **问题描述**: 查找目标值在排序数组中的起始和结束位置
- **解法**: 两次二分搜索（左边界和右边界）
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 11. SPOJ Aggressive Cows (Code11_AggressiveCows.java)
- **问题描述**: 在牛棚中放置奶牛，使得任意两头奶牛之间的最小距离最大
- **解法**: 二分答案 + 贪心验证（最大化最小值）
- **时间复杂度**: O(n * log(max-min))
- **空间复杂度**: O(log(n))

### 12. Book Allocation Problem (Code12_BookAllocation.java)
- **问题描述**: 将书籍分配给学生，使得分配给任意一个学生的最大页数最小
- **解法**: 二分答案 + 贪心验证（最小化最大值）
- **时间复杂度**: O(n * log(sum))
- **空间复杂度**: O(1)

### 13. SPOJ EKO (Code13_EKO.java)
- **问题描述**: 设置锯片高度切木材，使得切下的木材总量至少为M米且高度最高
- **解法**: 二分答案 + 贪心验证（最大化满足条件的值）
- **时间复杂度**: O(n * log(max))
- **空间复杂度**: O(1)

## 二分答案问题解题套路

### 1. 适用场景
- 求满足某种条件的最值（最大值最小、最小值最大）
- 判断某个值是否存在
- 在有序数据中查找特定元素

### 2. 解题步骤
1. **确定搜索范围**: 找到答案可能的最小值和最大值
2. **设计判断函数**: 编写函数验证某个值是否满足条件
3. **二分搜索**: 在范围内进行二分搜索，根据判断函数结果调整搜索区间
4. **返回结果**: 根据题目要求返回合适的结果

### 3. 常见变形
- **最大化最小值**: 二分答案，判断函数验证是否能达到该最小值
- **最小化最大值**: 二分答案，判断函数验证是否能控制在该最大值内
- **查找边界**: 使用左边界/右边界二分搜索模板

### 4. 注意事项
- **整数溢出**: 处理大数运算时使用long类型
- **边界条件**: 注意搜索区间的开闭性，避免死循环
- **精度问题**: 浮点数二分需要注意精度控制
- **贪心验证**: 判断函数常使用贪心策略验证答案可行性

## 完整题目列表（已实现代码）

### 基础题目（原仓库）
1. **Code01_KokoEatingBananas.java** - LeetCode 875. 爱吃香蕉的珂珂
2. **Code02_SplitArrayLargestSum.java** - LeetCode 410. 分割数组的最大值  
3. **Code03_RobotPassThroughBuilding.java** - 牛客网 机器人跳跃问题
4. **Code04_FindKthSmallestPairDistance.java** - LeetCode 719. 找出第K小的数对距离
5. **Code05_MaximumRunningTimeOfNComputers.java** - LeetCode 2141. 同时运行N台电脑的最长时间
6. **Code06_WaitingTime.java** - 等位时间问题
7. **Code07_CutOrPoison.java** - 刀砍毒杀怪兽问题
8. **Code08_CapacityToShipPackages.java** - LeetCode 1011. 在D天内送达包裹的能力
9. **Code09_MinimumNumberOfDaysToMakeBouquets.java** - LeetCode 1482. 制作m束花所需的时间
10. **Code10_FindFirstAndLastPosition.java** - LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置
11. **Code11_AggressiveCows.java** - SPOJ Aggressive Cows
12. **Code12_BookAllocation.java** - Book Allocation Problem
13. **Code13_EKO.java** - SPOJ EKO
14. **Code14_FindSmallestDivisor.java** - LeetCode 1283. 使结果不超过阈值的最小除数

### 新增题目（本次补充）
15. **Code15_DivideChocolate.java** - LeetCode 1231. 分享巧克力
16. **Code16_MagneticForceBetweenTwoBalls.java** - LeetCode 1552. 两球之间的磁力
17. **Code17_FindSmallestLetterGreaterThanTarget.java** - LeetCode 744. 寻找比目标字母大的最小字母
18. **Code18_FirstBadVersion.java** - LeetCode 278. 第一个错误的版本
19. **Code19_SqrtX.java** - LeetCode 69. Sqrt(x)
20. **Code20_SearchInsertPosition.java** - LeetCode 35. 搜索插入位置
21. **Code21_WoodCutting.java** - LintCode 183. 木材加工
22. **Code22_CopyBooks.java** - LintCode 437. 书籍复印
23. **Code23_MinimumTimeRequired.java** - HackerRank Minimum Time Required
24. **Code24_Present.java** - Codeforces 460C - Present
25. **Code25_BuyAnInteger.java** - AtCoder ABC146 - C - Buy an Integer
26. **Code26_MonthlyExpense.java** - POJ 3273 - Monthly Expense
27. **Code27_JumpStones.java** - 洛谷 P2678 - 跳石头
28. **Code28_FindTheDuplicateNumber.java** - LeetCode 287. 寻找重复数
29. **Code29_MedianOfTwoSortedArrays.java** - LeetCode 4. 寻找两个正序数组的中位数
30. **Code30_SolveEquation.java** - 杭电OJ 2199 - Can you solve this equation?
31. **Code31_BinarySearch.java** - AizuOJ ALDS1_4_B - Binary Search
32. **Code32_FindMissingLetter.java** - CodeWars - Find the missing letter

### 其他平台题目（参考实现）
- **UVa 10484 - Divisibility of Factors** - 求最小的n使得n!能被k整除
- **ZOJ 3537 - Cake** - 将圆形蛋糕分成k块，求最大的最小块面积
- **CodeChef - EOEO** - 求满足条件的数对个数
- **TimusOJ 1018 - Binary Apple Tree** - 在树上保留q条边，求最大苹果数
- **计蒜客 T1155 - 跳石头** - 同洛谷P2678
- **HackerEarth - The Easiest Way** - 求最小的k使得k!能被n整除
- **Project Euler 57 - Square root convergents** - 研究平方根展开中的分数项

### LintCode
- **LintCode 183. 木材加工**
  - 问题描述：将木材切成长度相同的小段，使小段总数量至少为k，求小段的最大可能长度
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(1)
  - 链接：https://www.lintcode.com/problem/183/

- **LintCode 437. 书籍复印**
  - 问题描述：k个抄写员抄写n本书，求最短完成时间
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(sum))
  - 空间复杂度：O(1)
  - 链接：https://www.lintcode.com/problem/437/

- **LintCode 1843. 圆形煎饼**
  - 问题描述：将煎饼分成k块，求最大的最小块面积
  - 解法：二分答案 + 数学验证
  - 时间复杂度：O(k * log(max_area))
  - 空间复杂度：O(1)
  - 链接：https://www.lintcode.com/problem/1843/

### HackerRank
- **HackerRank - Minimum Time Required**
  - 问题描述：计算制造m个产品所需的最少时间
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max_time))
  - 空间复杂度：O(1)
  - 链接：https://www.hackerrank.com/challenges/minimum-time-required/problem

- **HackerRank - Maximum Subarray Sum**
  - 问题描述：找出最大的子数组和不超过k
  - 解法：二分答案 + 前缀和 + 二分查找
  - 时间复杂度：O(n * log(sum))
  - 空间复杂度：O(n)
  - 链接：https://www.hackerrank.com/challenges/maximum-subarray-sum/problem

### Codeforces
- **Codeforces 460C - Present**
  - 问题描述：给植物浇水，求最后植物的最小可能最大高度
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(n)
  - 链接：https://codeforces.com/problemset/problem/460/C

- **Codeforces 1355B - Young Explorers**
  - 问题描述：将探险者分组，求最多能分成多少组
  - 解法：排序 + 贪心 + 计数
  - 时间复杂度：O(n * log n)
  - 空间复杂度：O(n)
  - 链接：https://codeforces.com/problemset/problem/1355/B

### AtCoder
- **AtCoder ABC146 - C - Buy an Integer**
  - 问题描述：购买数字，求最大可能的数字
  - 解法：二分答案 + 数学计算
  - 时间复杂度：O(log max_num)
  - 空间复杂度：O(1)
  - 链接：https://atcoder.jp/contests/abc146/tasks/abc146_c

### SPOJ
- **SPOJ - EKO**
  - 问题描述：设置锯片高度切木材，使得切下的木材总量至少为M米且高度最高
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(1)
  - 链接：https://www.spoj.com/problems/EKO/

### 牛客网
- **牛客网 NC163 机器人跳跃问题**
  - 问题描述：机器人跳跃建筑，求能通关的最小初始能量
  - 解法：二分答案 + 模拟验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(1)
  - 链接：https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71

### UVa
- **UVa 10484 - Divisibility of Factors**
  - 问题描述：求最小的n使得n!能被k整除
  - 解法：二分答案 + 质因数分解
  - 时间复杂度：O(sqrt(k) + log(n) * log(k))
  - 空间复杂度：O(log k)
  - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1425

### POJ
- **POJ 3273 - Monthly Expense**
  - 问题描述：将数组分成m段，使各段和的最大值最小
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(sum))
  - 空间复杂度：O(1)
  - 链接：http://poj.org/problem?id=3273

### ZOJ
- **ZOJ 3537 - Cake**
  - 问题描述：将圆形蛋糕分成k块，求最大的最小块面积
  - 解法：二分答案 + 计算几何
  - 时间复杂度：O(k * log(max_area))
  - 空间复杂度：O(1)
  - 链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366119

### CodeChef
- **CodeChef - EOEO**
  - 问题描述：求满足条件的数对个数
  - 解法：数学分析 + 二分答案
  - 时间复杂度：O(log N)
  - 空间复杂度：O(1)
  - 链接：https://www.codechef.com/problems/EOEO

### TimusOJ
- **TimusOJ 1018 - Binary Apple Tree**
  - 问题描述：在树上保留q条边，求最大苹果数
  - 解法：树形DP
  - 时间复杂度：O(n^2)
  - 空间复杂度：O(n^2)
  - 链接：https://acm.timus.ru/problem.aspx?space=1&num=1018

### AizuOJ
- **AizuOJ ALDS1_4_B - Binary Search**
  - 问题描述：二分查找的基本实现
  - 解法：二分搜索
  - 时间复杂度：O(log n)
  - 空间复杂度：O(1)
  - 链接：https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_4_B

### 杭电OJ
- **杭电OJ 2199 - Can you solve this equation?**
  - 问题描述：求解方程f(x)=0在区间内的解
  - 解法：二分答案（二分查找根）
  - 时间复杂度：O(log(max-min)/epsilon)
  - 空间复杂度：O(1)
  - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=2199

### 洛谷
- **洛谷 P2678 - 跳石头**
  - 问题描述：移除部分石头，使得剩下的石头之间的最小距离最大
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(1)
  - 链接：https://www.luogu.com.cn/problem/P2678

### 计蒜客
- **计蒜客 T1155 - 跳石头**
  - 问题描述：同洛谷P2678
  - 解法：二分答案 + 贪心验证
  - 时间复杂度：O(n * log(max))
  - 空间复杂度：O(1)
  - 链接：https://www.jisuanke.com/problem/T1155

### CodeWars
- **CodeWars - Find the missing letter**
  - 问题描述：找出字母序列中缺失的字母
  - 解法：二分搜索
  - 时间复杂度：O(log n)
  - 空间复杂度：O(1)
  - 链接：https://www.codewars.com/kata/5839edaa6754d6fec10000a2

### Project Euler
- **Project Euler 57 - Square root convergents**
  - 问题描述：研究平方根展开中的分数项
  - 解法：数学分析 + 二分答案
  - 时间复杂度：O(n)
  - 空间复杂度：O(1)
  - 链接：https://projecteuler.net/problem=57

### HackerEarth
- **HackerEarth - The Easiest Way**
  - 问题描述：求最小的k使得k!能被n整除
  - 解法：二分答案 + 质因数分解
  - 时间复杂度：O(sqrt(n) + log(max_k) * log(n))
  - 空间复杂度：O(log n)
  - 链接：https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/the-easiest-way-1/

## 总结

二分答案是解决最优化问题的重要技巧，通过将最值问题转化为判定问题，可以大大降低时间复杂度。掌握二分答案的关键在于：
1. 准确识别问题类型
2. 合理确定搜索范围
3. 正确设计判断函数
4. 熟练掌握二分搜索模板

### 新增题目类型总结

1. **最大化最小值问题**：如Aggressive Cows问题，目标是使最小值尽可能大
2. **最小化最大值问题**：如Book Allocation问题，目标是使最大值尽可能小
3. **最大化满足条件的值**：如EKO问题，目标是找到满足条件的最大值

这些问题都可以通过二分答案的方法来解决，关键在于确定搜索范围和设计正确的判断函数。