# 归并排序经典题目汇总

## 一、LeetCode系列题目

### 基础排序题目
1. **LeetCode 912. 排序数组**
   - 题目链接: https://leetcode.cn/problems/sort-an-array/
   - 题目描述: 给定一个整数数组 nums，将该数组升序排列
   - 解法: 直接应用归并排序模板
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 是否最优: 是 - 基于比较的排序算法下界

2. **LeetCode 148. 排序链表**
   - 题目链接: https://leetcode.cn/problems/sort-list/
   - 题目描述: 给你链表的头结点 head，请将其按升序排列并返回排序后的链表
   - 解法: 链表归并排序，使用快慢指针找中点
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(log n) - 递归调用栈
   - 是否最优: 是 - 链表排序的最佳选择

3. **LeetCode 23. 合并K个升序链表**
   - 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
   - 题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中
   - 解法: 分治合并，类似归并排序
   - 时间复杂度: O(N log k) - N是所有节点总数，k是链表数量
   - 空间复杂度: O(log k) - 递归调用栈
   - 是否最优: 是 - 与优先队列方法时间复杂度相同

4. **LeetCode 88. 合并两个有序数组**
   - 题目链接: https://leetcode.cn/problems/merge-sorted-array/
   - 题目描述: 合并nums2到nums1中，使合并后的数组有序
   - 解法: 从后往前合并，避免覆盖nums1的有效数据
   - 时间复杂度: O(m + n)
   - 空间复杂度: O(1) - 原地修改
   - 是否最优: 是 - 时间、空间都最优

5. **LeetCode 21. 合并两个有序链表**
   - 题目链接: https://leetcode.cn/problems/merge-two-sorted-lists/
   - 题目描述: 将两个升序链表合并为一个新的升序链表并返回
   - 解法: 双指针合并
   - 时间复杂度: O(m + n)
   - 空间复杂度: O(1)
   - 是否最优: 是

### 逆序对相关题目
6. **LeetCode 315. 计算右侧小于当前元素的个数**
   - 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 题目描述: 统计每个元素右侧比它小的元素个数
   - 解法: 归并排序 + 索引数组，在合并过程中利用有序性高效统计
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 是否最优: 是

7. **LeetCode 493. 翻转对**
   - 题目链接: https://leetcode.cn/problems/reverse-pairs/
   - 题目描述: 统计满足 nums[i] > 2*nums[j] 且 i < j 的对数
   - 解法: 归并排序，在合并前先统计翻转对
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 是否最优: 是

8. **LeetCode 327. 区间和的个数**
   - 题目链接: https://leetcode.cn/problems/count-of-range-sum/
   - 题目描述: 统计区间和在[lower, upper]范围内的子数组个数
   - 解法: 前缀和数组 + 归并排序
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 是否最优: 是

9. **LeetCode LCR 170. 交易逆序对的总数** (剑指Offer 51)
   - 题目链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   - 题目描述: 计算数组中的逆序对总数
   - 解法: 归并排序过程中统计逆序对
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 是否最优: 是

10. **LeetCode 2426. 满足不等式的数对数目**
    - 题目链接: https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
    - 题目描述: 统计满足不等式的数对数目
    - 解法: 翻转对变种，处理不等式条件
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

11. **LeetCode 4. 寻找两个正序数组的中位数**
    - 题目链接: https://leetcode.cn/problems/median-of-two-sorted-arrays/
    - 题目描述: 寻找两个正序数组的中位数
    - 解法: 二分查找，不是归并排序但涉及有序数组合并思想
    - 时间复杂度: O(log(min(m, n)))
    - 空间复杂度: O(1)
    - 是否最优: 是

### 其他相关题目
12. **LeetCode 1508. 子数组和排序后的区间和**
    - 题目链接: https://leetcode.cn/problems/range-sum-of-sorted-subarray-sums/
    - 题目描述: 子数组和排序后的区间和
    - 解法: 结合前缀和与归并排序思想
    - 时间复杂度: O(n² log n)
    - 空间复杂度: O(n²)
    - 是否最优: 是

13. **LeetCode 1649. 通过指令创建有序数组**
    - 题目链接: https://leetcode.cn/problems/create-sorted-array-through-instructions/
    - 题目描述: 通过指令创建有序数组
    - 解法: 动态统计逆序对，可使用树状数组、线段树或分治
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

## 二、洛谷系列题目

14. **洛谷 P1177. 【模板】快速排序**
    - 题目链接: https://www.luogu.com.cn/problem/P1177
    - 题目描述: 快速排序模板题（虽然题目是快速排序，但可以用归并排序通过）
    - 解法: 归并排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

15. **洛谷 P1908. 逆序对**
    - 题目链接: https://www.luogu.com.cn/problem/P1908
    - 题目描述: 计算数组中的逆序对数量
    - 解法: 归并排序统计逆序对
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

16. **洛谷 P1774. 最接近神的人**
    - 题目链接: https://www.luogu.com.cn/problem/P1774
    - 题目描述: 逆序对问题的变种
    - 解法: 逆序对统计
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

17. **洛谷 P1966. [NOIP2013 提高组] 火柴排队**
    - 题目链接: https://www.luogu.com.cn/problem/P1966
    - 题目描述: 逆序对应用
    - 解法: 逆序对、离散化
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

## 三、牛客网系列题目

18. **牛客 NC119. 最小的K个数**
    - 题目链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
    - 题目描述: 寻找最小的K个数
    - 解法: 虽不是直接归并排序，但涉及排序思想，可使用堆或分治
    - 时间复杂度: O(n log k)
    - 空间复杂度: O(k)
    - 是否最优: 是

19. **牛客 NC37. 合并区间**
    - 题目链接: https://www.nowcoder.com/practice/65cfde9e5b9b4cf2b6bafa5f3ef33fa6
    - 题目描述: 合并区间
    - 解法: 区间合并的归并思想
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(1)
    - 是否最优: 是

20. **牛客 NC51. 合并k个已排序的链表**
    - 题目链接: https://www.nowcoder.com/practice/65cfde9e5b9b4cf2b6bafa5f3ef33fa6
    - 题目描述: 合并k个已排序的链表
    - 解法: 链表归并排序应用
    - 时间复杂度: O(N log k)
    - 空间复杂度: O(log k)
    - 是否最优: 是

## 四、AcWing系列题目

21. **AcWing 787. 归并排序**
    - 题目链接: https://www.acwing.com/problem/content/789/
    - 题目描述: 基础归并排序模板
    - 解法: 归并排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

22. **AcWing 788. 逆序对的数量**
    - 题目链接: https://www.acwing.com/problem/content/790/
    - 题目描述: 经典逆序对问题
    - 解法: 归并排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

23. **AcWing 107. 超快速排序**
    - 题目链接: https://www.acwing.com/problem/content/109/
    - 题目描述: 逆序对问题变种
    - 解法: 逆序对统计
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

## 五、国际知名平台系列

24. **POJ 2299. Ultra-QuickSort**
    - 题目链接: http://poj.org/problem?id=2299
    - 题目描述: 经典逆序对问题，国际知名
    - 解法: 归并排序统计逆序对
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

25. **HDU 1394. Minimum Inversion Number**
    - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
    - 题目描述: 循环移位中的逆序对
    - 解法: 逆序对应用
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

26. **Codeforces 558E. A Simple Task**
    - 题目链接: https://codeforces.com/problemset/problem/558/E
    - 题目描述: 字符串排序与归并思想
    - 解法: 线段树、排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

27. **HackerRank Merge Sort: Counting Inversions**
    - 题目链接: https://www.hackerrank.com/challenges/ctci-merge-sort/problem
    - 题目描述: 企业面试常见题
    - 解法: 逆序对统计
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

28. **SPOJ INVCNT. Inversion Count**
    - 题目链接: https://www.spoj.com/problems/INVCNT/
    - 题目描述: 专门测试逆序对
    - 解法: 归并排序
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

## 六、其他国内平台

29. **杭电OJ 1394. Minimum Inversion Number**
    - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
    - 题目描述: 经典题目，多校赛常见
    - 解法: 逆序对统计
    - 时间复杂度: O(n log n)
    - 空间复杂度: O(n)
    - 是否最优: 是

30. **赛码网 归并排序相关题目**
    - 平台: https://www.acmcoder.com/
    - 题目描述: 企业笔试常见
    - 解法: 归并排序及其变种
    - 是否最优: 是

31. **计蒜客 排序相关题目**
    - 平台: https://www.jisuanke.com/
    - 题目描述: 算法竞赛培训
    - 解法: 归并排序及其变种
    - 是否最优: 是

32. **MarsCode 算法题库**
    - 平台: https://www.marscode.cn/
    - 题目描述: 新兴算法平台
    - 解法: 归并排序及其变种
    - 是否最优: 是

## 七、特殊类型题目

33. **外部排序相关题目**
    - 题目描述: 处理超大规模数据
    - 解法: 多路归并
    - 应用场景: 数据库排序、大数据处理
    - 是否最优: 是

34. **多路归并题目**
    - 题目描述: 合并多个有序序列
    - 解法: 分治合并
    - 应用场景: 大数据处理
    - 是否最优: 是

35. **并行归并排序题目**
    - 题目描述: 多线程优化
    - 解法: 并行化处理
    - 应用场景: 高性能计算
    - 是否最优: 是

36. **稳定排序应用题目**
    - 题目描述: 需要保持相等元素相对顺序
    - 解法: 归并排序
    - 应用场景: 数据库查询、金融系统
    - 是否最优: 是

## 八、综合训练题目（按难度分级）

### 入门级（掌握基础）
37. **基础归并排序实现**
38. **逆序对统计基础版**

### 进阶级（理解应用）
39. **链表归并排序**
40. **合并K个有序序列**
41. **区间和统计**

### 高手级（深入掌握）
42. **翻转对统计**
43. **复杂条件逆序对**
44. **外部排序实现**

### 专家级（工程应用）
45. **并行归并排序优化**
46. **稳定排序系统设计**
47. **大规模数据处理**

## 九、题目分类总结

| 类别 | 题目数量 | 核心算法 | 应用场景 |
|------|----------|----------|----------|
| 基础排序 | 15+ | 归并排序模板 | 算法学习、面试基础 |
| 逆序对统计 | 20+ | 归并排序变种 | 数学统计、金融分析 |
| 链表排序 | 5+ | 链表归并排序 | 数据结构应用 |
| 多路合并 | 8+ | 分治合并 | 大数据处理 |
| 区间统计 | 6+ | 前缀和+归并 | 数据分析 |
| 工程优化 | 10+ | 各种优化技巧 | 实际系统开发 |

## 十、训练建议

1. **基础阶段**: 先掌握归并排序的递归和非递归实现
2. **应用阶段**: 练习逆序对、链表排序等变种问题
3. **提高阶段**: 解决复杂条件的统计问题
4. **实战阶段**: 参与在线评测，检验掌握程度

## 十一、学习资源推荐

1. **书籍**: 《算法导论》、《编程珠玑》
2. **视频**: 各大MOOC平台的算法课程
3. **练习**: LeetCode、洛谷、AcWing等平台
4. **社区**: Stack Overflow、GitHub开源项目

通过系统学习以上题目，可以全面掌握归并排序及其应用，为算法竞赛和面试打下坚实基础。