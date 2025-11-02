# 快速排序相关算法题目清单

## 一、LeetCode题目

### 基础排序类
1. **LeetCode 912. 排序数组**
   - 链接: https://leetcode.cn/problems/sort-an-array/
   - 难度: Medium
   - 题目描述: 给你一个整数数组 nums，请你将该数组升序排列。
   - 解题思路: 使用快速排序算法对数组进行排序。
   - 算法要点: 随机化基准选择避免最坏情况，三路快排处理重复元素

2. **LeetCode 75. 颜色分类**
   - 链接: https://leetcode.cn/problems/sort-colors/
   - 难度: Medium
   - 题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
   - 解题思路: 使用三路快速排序的思想，将数组分为三个区域：<1, =1, >1
   - 算法要点: 荷兰国旗问题，双指针技术

3. **LeetCode 283. 移动零**
   - 链接: https://leetcode.cn/problems/move-zeroes/
   - 难度: Easy
   - 题目描述: 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
   - 解题思路: 使用快速排序的分区思想，将非零元素移到数组前面，保持相对顺序
   - 算法要点: 双指针技术，稳定分区

### 查找第K个元素类
4. **LeetCode 215. 数组中的第K个最大元素**
   - 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
   - 难度: Medium
   - 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
   - 解题思路: 使用快速选择算法，在快速排序的基础上进行优化，只处理包含目标元素的区间。
   - 算法要点: 快速选择算法，平均时间复杂度O(n)

5. **LeetCode 347. 前 K 个高频元素**
   - 链接: https://leetcode.cn/problems/top-k-frequent-elements/
   - 难度: Medium
   - 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
   - 解题思路: 使用堆或者快速选择算法来找出前k个高频元素。
   - 算法要点: 哈希表统计频率，最小堆维护前k大元素

6. **LeetCode 973. 最接近原点的 K 个点**
   - 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
   - 难度: Medium
   - 题目描述: 给定一个points数组和整数K，返回最接近原点的K个点。
   - 解题思路: 计算每个点到原点的距离，然后使用快速选择算法找出最小的K个距离。
   - 算法要点: 距离计算，快速选择算法

7. **LeetCode 703. 数据流中的第K大元素**
   - 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
   - 难度: Easy
   - 题目描述: 设计一个找到数据流中第 k 大元素的类。
   - 解题思路: 使用最小堆维护前k大的元素。
   - 算法要点: 最小堆数据结构，动态维护

### 其他相关题目
8. **LeetCode 324. 摆动排序 II**
   - 链接: https://leetcode.cn/problems/wiggle-sort-ii/
   - 难度: Medium
   - 题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
   - 解题思路: 先排序，然后通过特定的索引映射来构造摆动序列。
   - 算法要点: 排序预处理，索引映射技巧

9. **LeetCode 414. 第三大的数**
   - 链接: https://leetcode.cn/problems/third-maximum-number/
   - 难度: Easy
   - 题目描述: 给你一个非空数组，返回此数组中第三大的数。
   - 解题思路: 使用一次遍历维护三个最大值，或者使用快速选择算法。
   - 算法要点: 维护三个变量，去重处理

10. **LeetCode 462. 最少移动次数使数组元素相等 II**
    - 链接: https://leetcode.cn/problems/minimum-moves-to-equal-array-elements-ii/
    - 难度: Medium
    - 题目描述: 给你一个长度为 n 的整数数组 nums ，返回使所有数组元素相等需要的最少移动数。
    - 解题思路: 找到中位数，所有元素向中位数移动的步数之和最小。
    - 算法要点: 中位数性质，快速选择算法

## 二、剑指Offer题目

11. **剑指 Offer 40. 最小的k个数**
    - 链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
    - 难度: Easy
    - 题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
    - 解题思路: 使用快速选择算法或者快速排序算法找出最小的k个数。
    - 算法要点: 快速选择算法，边界条件处理

## 三、牛客网题目

12. **牛客网 - 快速排序**
    - 链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
    - 难度: Easy
    - 题目描述: 实现快速排序算法
    - 解题思路: 标准快速排序实现，注意边界条件和递归终止条件
    - 算法要点: 分区操作，递归实现

13. **牛客网 - 最小的k个数**
    - 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
    - 难度: Easy
    - 题目描述: 输入n个整数，找出其中最小的K个数。
    - 解题思路: 使用快速选择算法或者堆来解决。
    - 算法要点: 快速选择算法，堆数据结构

## 四、PAT题目

14. **PAT 1101 Quick Sort**
    - 链接: https://pintia.cn/problem-sets/994805342720868352/problems/994805366343188480
    - 难度: Medium
    - 题目描述: 快速排序中的主元(pivot)是左面都比它小、右边都比它大的位置对应的数字。找出所有满足条件的主元。
    - 解题思路: 分析快速排序过程中每个元素是否可能作为主元，预处理左右边界最大值数组。
    - 算法要点: 预处理技术，边界分析

## 五、洛谷题目

15. **洛谷 P1177 【模板】快速排序**
    - 链接: https://www.luogu.com.cn/problem/P1177
    - 难度: 普及-
    - 题目描述: 利用快速排序算法将读入的N个数从小到大排序后输出。
    - 解题思路: 实现标准快速排序算法，注意输入输出格式。
    - 算法要点: 标准快速排序，输入输出处理

## 六、Codeforces题目

16. **Codeforces 401C. Team**
    - 链接: https://codeforces.com/problemset/problem/401/C
    - 难度: 1500
    - 题目描述: 构造一个01序列，满足特定的约束条件。
    - 解题思路: 在某些构造方法中可以使用排序来优化解的生成，贪心策略。
    - 算法要点: 构造算法，贪心思想

## 七、AtCoder题目

17. **AtCoder ABC121C. Energy Drink Collector**
    - 链接: https://atcoder.jp/contests/abc121/tasks/abc121_c
    - 难度: 300
    - 题目描述: 购买能量饮料以获得最少的总花费。
    - 解题思路: 按价格排序后贪心选择，优先选择价格低的饮料。
    - 算法要点: 贪心算法，排序预处理

## 八、其他平台题目

18. **ZOJ 2581 Random Walking**
    - 链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367080
    - 难度: Medium
    - 题目描述: 随机游走问题，需要排序预处理数据。
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 最优解: 排序预处理数据
    - 算法要点: 排序预处理，随机游走分析

19. **HackerRank - QuickSort 1 - Partition**
    - 链接: https://www.hackerrank.com/challenges/quicksort1/problem
    - 难度: Easy
    - 题目描述: 实现快速排序的分区操作。
    - 时间复杂度: O(n)
    - 空间复杂度: O(n)
    - 最优解: 快速排序分区操作实现
    - 算法要点: 分区算法实现

20. **HackerRank - QuickSort 2 - Sorting**
    - 链接: https://www.hackerrank.com/challenges/quicksort2/problem
    - 难度: Easy
    - 题目描述: 实现完整的快速排序算法。
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(log n)
    - 最优解: 完整快速排序算法实现
    - 算法要点: 完整快速排序实现

21. **杭电 OJ 1425. sort**
    - 链接: http://acm.hdu.edu.cn/showproblem.php?pid=1425
    - 难度: 简单
    - 题目描述: 对整数数组进行快速排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(log n)
    - 最优解: 快速排序或堆排序
    - 算法要点: 基础排序算法

22. **POJ 2388. Who's in the Middle**
    - 链接: http://poj.org/problem?id=2388
    - 难度: 简单
    - 题目描述: 找出一组数的中位数，快速选择的经典应用
    - 时间复杂度: O(n) 平均
    - 空间复杂度: O(log n)
    - 最优解: 快速选择算法找中位数
    - 算法要点: 中位数查找，快速选择

23. **AizuOJ ALDS1_6_C. Quick Sort**
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_6_C
    - 难度: 简单
    - 题目描述: 实现快速排序算法并输出每一步的分区结果
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(log n)
    - 最优解: 快速排序算法实现
    - 算法要点: 快速排序实现，调试输出

24. **Comet OJ Contest 11 E. 快速排序**
    - 链接: https://cometoj.com/contest/59/problem/E?problem_id=2830
    - 难度: 中等
    - 题目描述: 快速排序相关的概率问题
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(log n)
    - 最优解: 快速排序相关的概率问题
    - 算法要点: 概率分析，快速排序

25. **SPOJ - SORT1 - Sorting Test**
    - 链接: https://www.spoj.com/problems/SORT1/
    - 难度: 简单
    - 题目描述: 基本排序问题，测试排序算法效率
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(log n)
    - 最优解: 快速排序基准测试
    - 算法要点: 排序算法测试

26. **UVa 10152 - ShellSort**
    - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1093
    - 难度: 中等
    - 题目描述: 实现一种特殊的排序算法，与快速排序思想有关
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 最优解: 特殊排序算法
    - 算法要点: 特殊排序算法实现

27. **LeetCode 169. 多数元素**
    - 链接: https://leetcode.cn/problems/majority-element/
    - 难度: Easy
    - 题目描述: 给定一个大小为 n 的数组，找到其中的多数元素
    - 时间复杂度: O(n)
    - 空间复杂度: O(1)
    - 最优解: Boyer-Moore投票算法（与快速选择思想相关）
    - 算法要点: 投票算法，多数元素查找

28. **LeetCode 229. 求众数 II**
    - 链接: https://leetcode.cn/problems/majority-element-ii/
    - 难度: Medium
    - 题目描述: 找出数组中所有出现次数超过 ⌊ n/3 ⌋ 的元素
    - 时间复杂度: O(n)
    - 空间复杂度: O(1)
    - 最优解: 摩尔投票法扩展
    - 算法要点: 扩展投票算法，众数查找

29. **LeetCode 274. H 指数**
    - 链接: https://leetcode.cn/problems/h-index/
    - 难度: Medium
    - 题目描述: 计算研究人员的 h 指数
    - 时间复杂度: O(n) 平均
    - 空间复杂度: O(n)
    - 最优解: 计数排序或快速选择
    - 算法要点: H指数计算，排序应用

30. **LeetCode 378. 有序矩阵中第K小的元素**
    - 链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
    - 难度: Medium
    - 题目描述: 给定一个 n x n 矩阵，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素
    - 时间复杂度: O(n log(max-min))
    - 空间复杂度: O(1)
    - 最优解: 二分查找 + 计数
    - 算法要点: 二分查找，矩阵性质利用

## 总结

快速排序作为一种经典的排序算法，在各类算法竞赛和面试中都有广泛应用。通过系统练习以上题目，可以：

1. **掌握基础算法**：熟练实现标准快速排序
2. **理解算法变种**：掌握三路快排、快速选择等变种
3. **提升优化能力**：学会各种优化技巧，如随机化、小数组优化等
4. **扩展应用场景**：了解快速排序在不同问题中的应用，如查找第K大元素、中位数等
5. **增强调试能力**：通过大量练习提升算法调试能力，掌握边界条件处理

建议按照难度顺序逐步练习，并在练习过程中注重代码质量、时间复杂度分析和边界情况处理。同时，要注意不同题目可能需要不同的快速排序变种，如三路快排处理重复元素、快速选择查找第K大元素等。