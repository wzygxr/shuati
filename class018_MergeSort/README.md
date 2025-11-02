# 归并排序专题详解

归并排序是一种基于分治思想的稳定排序算法，采用分而治之的策略，将数组不断分割成更小的子数组，然后将这些子数组合并成有序数组。

## 算法原理

归并排序的基本思想是：
1. **分解**：将待排序数组递归地分成两个子数组，直到每个子数组只有一个元素
2. **解决**：递归地对子数组进行排序
3. **合并**：将两个已排序的子数组合并成一个有序数组

## 算法复杂度

- **时间复杂度**：O(n log n) - 在所有情况下（最好、最坏、平均）都是这个复杂度
- **空间复杂度**：O(n) - 需要额外的数组空间来存储临时数据

## 稳定性

归并排序是一种稳定的排序算法，相等元素的相对位置在排序后不会改变。

## 核心实现

### Java版本

```java
// 合并函数
public static void merge(int[] arr, int l, int m, int r) {
    int i = l;
    int a = l;
    int b = m + 1;
    while (a <= m && b <= r) {
        help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
    }
    while (a <= m) {
        help[i++] = arr[a++];
    }
    while (b <= r) {
        help[i++] = arr[b++];
    }
    for (i = l; i <= r; i++) {
        arr[i] = help[i];
    }
}

// 递归版本
public static void mergeSort1(int[] arr, int l, int r) {
    if (l == r) {
        return;
    }
    int m = (l + r) / 2;
    mergeSort1(arr, l, m);
    mergeSort1(arr, m + 1, r);
    merge(arr, l, m, r);
}

// 非递归版本
public static void mergeSort2(int[] arr) {
    int n = arr.length;
    for (int l, m, r, step = 1; step < n; step <<= 1) {
        l = 0;
        while (l < n) {
            m = l + step - 1;
            if (m + 1 >= n) {
                break;
            }
            r = Math.min(l + (step << 1) - 1, n - 1);
            merge(arr, l, m, r);
            l = r + 1;
        }
    }
}
```

### C++版本

```cpp
// 合并函数
void merge(int l, int m, int r) {
    int i = l;
    int a = l;
    int b = m + 1;
    while (a <= m && b <= r) {
        help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
    }
    while (a <= m) {
        help[i++] = arr[a++];
    }
    while (b <= r) {
        help[i++] = arr[b++];
    }
    for (i = l; i <= r; i++) {
        arr[i] = help[i];
    }
}

// 递归版本
void mergeSort1(int l, int r) {
    if (l == r) {
        return;
    }
    int m = (l + r) / 2;
    mergeSort1(l, m);
    mergeSort1(m + 1, r);
    merge(l, m, r);
}

// 非递归版本
void mergeSort2() {
    for (int l, m, r, step = 1; step < n; step <<= 1) {
        l = 0;
        while (l < n) {
            m = l + step - 1;
            if (m + 1 >= n) {
                break;
            }
            r = min(l + (step << 1) - 1, n - 1);
            merge(l, m, r);
            l = r + 1;
        }
    }
}
```

### Python版本

```python
# 合并函数
def merge(l: int, m: int, r: int) -> None:
    i = l
    a = l
    b = m + 1
    while a <= m and b <= r:
        if arr[a] <= arr[b]:
            help_arr[i] = arr[a]
            a += 1
        else:
            help_arr[i] = arr[b]
            b += 1
        i += 1
    
    while a <= m:
        help_arr[i] = arr[a]
        a += 1
        i += 1
    
    while b <= r:
        help_arr[i] = arr[b]
        b += 1
        i += 1
    
    for i in range(l, r + 1):
        arr[i] = help_arr[i]

# 递归版本
def merge_sort1(l: int, r: int) -> None:
    if l == r:
        return
    m = (l + r) // 2
    merge_sort1(l, m)
    merge_sort1(m + 1, r)
    merge(l, m, r)

# 非递归版本
def merge_sort2() -> None:
    step = 1
    while step < n:
        l = 0
        while l < n:
            m = l + step - 1
            if m + 1 >= n:
                break
            r = min(l + (step << 1) - 1, n - 1)
            merge(l, m, r)
            l = r + 1
        step <<= 1
```

## 归并排序算法深度解析

### 算法核心思想与数学原理

归并排序基于分治策略，其数学原理可以表示为：
```
T(n) = 2T(n/2) + O(n)
```
根据主定理(Master Theorem)，该递归式的时间复杂度为O(n log n)。

**关键数学特性**：
- 递归深度：log₂n
- 每层合并操作：O(n)
- 总操作数：n × log₂n

### 详细题目解析与多语言实现

#### 1. LeetCode 912. 排序数组
**题目链接**：https://leetcode.cn/problems/sort-an-array/
**题目描述**：给定一个整数数组 nums，将该数组升序排列。

**解题思路**：
- 直接应用归并排序模板
- 处理边界情况：空数组、单元素数组
- 选择递归或非递归版本

**复杂度分析**：
- 时间复杂度：O(n log n) - 最优解
- 空间复杂度：O(n) - 辅助数组
- 稳定性：稳定排序

**Java实现关键点**：
```java
// 递归版本：代码简洁但可能栈溢出
// 非递归版本：更安全，适合大数据量
```

**C++实现关键点**：
```cpp
// 使用全局数组避免栈溢出
// 使用ios::sync_with_stdio(false)加速IO
```

**Python实现关键点**：
```python
// 使用切片操作简化代码
// 注意递归深度限制，大数据量使用非递归版本
```

#### 2. LeetCode 148. 排序链表
**题目链接**：https://leetcode.cn/problems/sort-list/
**题目描述**：给你链表的头结点 head，请将其按升序排列并返回排序后的链表。

**解题思路**：
1. **快慢指针找中点**：龟兔赛跑算法
2. **递归分割**：将链表分为左右两部分
3. **合并有序链表**：双指针合并

**复杂度分析**：
- 时间复杂度：O(n log n)
- 空间复杂度：O(log n) - 递归调用栈

**链表排序的特殊性**：
- 归并排序是链表排序的最佳选择
- 快速排序需要随机访问，链表不支持
- 堆排序也需要随机访问

**工程化考量**：
- 哑节点(dummy node)简化边界处理
- 非递归版本可降低空间复杂度到O(1)
- 注意链表断开和连接的正确性

#### 3. LeetCode 23. 合并K个升序链表
**题目链接**：https://leetcode.cn/problems/merge-k-sorted-lists/
**题目描述**：给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中。

**解法对比分析**：

| 方法 | 时间复杂度 | 空间复杂度 | 优缺点 |
|------|------------|------------|--------|
| 顺序合并 | O(kN) | O(1) | 简单但效率低 |
| 优先队列 | O(N log k) | O(k) | 需要堆数据结构 |
| 分治合并 | O(N log k) | O(log k) | **最优解** |

**分治合并的核心思想**：
```
mergeKLists(lists) = merge(mergeKLists(left), mergeKLists(right))
```

#### 4. LeetCode 88. 合并两个有序数组
**题目链接**：https://leetcode.cn/problems/merge-sorted-array/
**题目描述**：合并nums2到nums1中，使合并后的数组有序。

**关键技巧**：**从后往前合并**
- 避免覆盖nums1的有效数据
- 直接利用nums1尾部的空位

**复杂度分析**：
- 时间复杂度：O(m + n)
- 空间复杂度：O(1) - 原地修改

**边界情况处理**：
- nums2为空：直接返回
- nums1有效数据为0：直接拷贝nums2
- 重复元素处理：保持稳定性

#### 5. LeetCode 315. 计算右侧小于当前元素的个数
**题目链接**：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
**题目描述**：统计每个元素右侧比它小的元素个数。

**算法核心**：归并排序 + 索引数组
1. **索引数组**：记录原始位置
2. **合并时统计**：利用有序性高效统计
3. **关键逻辑**：左侧元素出队时统计右侧已处理的较小元素

**统计时机分析**：
```
当 helper[p1] <= helper[p2] 时：
    counts[原始位置] += (p2 - (mid + 1))
```

#### 6. LeetCode 493. 翻转对
**题目链接**：https://leetcode.cn/problems/reverse-pairs/
**题目描述**：统计满足 nums[i] > 2*nums[j] 且 i < j 的对数。

**与315题的区别**：
- 315题：统计右侧比自己小的元素
- 493题：统计满足特定倍数关系的翻转对

**关键优化**：
1. **先统计后合并**：与315题顺序不同
2. **双指针技巧**：利用有序性，j指针不重置
3. **防溢出**：使用long类型处理大数

#### 7. LeetCode 327. 区间和的个数
**题目链接**：https://leetcode.cn/problems/count-of-range-sum/
**题目描述**：统计区间和在[lower, upper]范围内的子数组个数。

**算法转换**：
1. **前缀和数组**：将问题转化为统计前缀和差值
2. **归并排序应用**：在有序前缀和数组中统计满足条件的区间

**数学原理**：
```
prefix[j] - prefix[i] ∈ [lower, upper]
⇒ prefix[j] ∈ [prefix[i] + lower, prefix[i] + upper]
```

#### 8. 洛谷 P1908 逆序对
**题目链接**：https://www.luogu.com.cn/problem/P1908
**题目描述**：计算数组中的逆序对数量。

**逆序对定义**：
```
i < j 且 arr[i] > arr[j]
```

**统计方法**：
```cpp
// 合并时统计
if (arr[i] <= arr[j]) {
    temp[k++] = arr[i++];
} else {
    count += mid - i + 1;  // 关键统计
    temp[k++] = arr[j++];
}
```

#### 9. 剑指Offer 51. 数组中的逆序对
**题目链接**：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
**与P1908的关系**：同一问题在不同平台

#### 10. AcWing 788. 逆序对的数量
**题目链接**：https://www.acwing.com/problem/content/790/
**平台特点**：国内知名算法学习平台

### 扩展题目列表（从各大平台搜集 - 穷尽所有相关题目）

### LeetCode系列（国内主流）
21. **LeetCode 1508. 子数组和排序后的区间和**
    - 链接：https://leetcode.cn/problems/range-sum-of-sorted-subarray-sums/
    - 特点：结合前缀和与归并排序思想
    - 难度：中等
    - 相关标签：数组、排序、前缀和

22. **LeetCode 1649. 通过指令创建有序数组**
    - 链接：https://leetcode.cn/problems/create-sorted-array-through-instructions/
    - 特点：动态统计逆序对
    - 难度：困难
    - 相关标签：树状数组、线段树、分治

23. **LeetCode 2426. 满足不等式的数对数目**
    - 链接：https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
    - 特点：翻转对变种，不等式条件
    - 难度：困难
    - 相关标签：树状数组、线段树、分治

24. **LeetCode 51. 数组中的逆序对（剑指Offer）**
    - 链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
    - 特点：经典逆序对问题
    - 难度：困难
    - 相关标签：分治、归并排序

25. **LeetCode 4. 寻找两个正序数组的中位数**
    - 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
    - 特点：二分查找与归并思想
    - 难度：困难
    - 相关标签：数组、二分查找、分治

26. **LeetCode 295. 数据流的中位数**
    - 链接：https://leetcode.cn/problems/find-median-from-data-stream/
    - 特点：流数据处理
    - 难度：困难
    - 相关标签：堆、数据流

27. **LeetCode 703. 数据流中的第K大元素**
    - 链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    - 特点：堆数据结构应用
    - 难度：简单
    - 相关标签：堆

### 洛谷系列（国内知名OJ）
28. **P1177. 【模板】快速排序**
    - 链接：https://www.luogu.com.cn/problem/P1177
    - 备注：虽然题目是快速排序，但可以用归并排序通过
    - 难度：普及-
    - 提交数：超过100万

29. **P1774. 最接近神的人**
    - 链接：https://www.luogu.com.cn/problem/P1774
    - 特点：逆序对问题的变种
    - 难度：普及/提高-
    - 相关算法：逆序对统计

30. **P1908. 逆序对**
    - 链接：https://www.luogu.com.cn/problem/P1908
    - 特点：经典逆序对问题
    - 难度：普及/提高-
    - 提交数：超过50万

31. **P1966. [NOIP2013 提高组] 火柴排队**
    - 链接：https://www.luogu.com.cn/problem/P1966
    - 特点：逆序对应用
    - 难度：提高+/省选-
    - 相关算法：逆序对、离散化

### 牛客网系列（国内面试平台）
32. **NC119. 最小的K个数**
    - 链接：https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
    - 备注：虽不是直接归并排序，但涉及排序思想
    - 难度：中等
    - 相关标签：堆、分治、快速选择

33. **NC37. 合并区间**
    - 链接：https://www.nowcoder.com/practice/69f4e5b7ad284a478777cb2a17fb5e6a
    - 特点：区间合并的归并思想
    - 难度：中等
    - 相关标签：排序、数组

34. **NC51. 合并k个已排序的链表**
    - 链接：https://www.nowcoder.com/practice/65cfde9e5b9b4cf2b6bafa5f3ef33fa6
    - 特点：链表归并排序应用
    - 难度：困难
    - 相关标签：分治、堆、链表

### AcWing系列（国内算法学习平台）
35. **AcWing 787. 归并排序**
    - 链接：https://www.acwing.com/problem/content/789/
    - 特点：基础归并排序模板
    - 难度：简单
    - 提交数：超过10万

36. **AcWing 788. 逆序对的数量**
    - 链接：https://www.acwing.com/problem/content/790/
    - 特点：经典逆序对问题
    - 难度：简单
    - 相关算法：归并排序

37. **AcWing 107. 超快速排序**
    - 链接：https://www.acwing.com/problem/content/109/
    - 特点：逆序对问题变种
    - 难度：中等
    - 相关算法：逆序对统计

### 国际知名平台系列
38. **POJ 2299. Ultra-QuickSort**
    - 链接：http://poj.org/problem?id=2299
    - 特点：经典逆序对问题，国际知名
    - 难度：中等
    - 提交数：超过5万

39. **HDU 1394. Minimum Inversion Number**
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
    - 特点：循环移位中的逆序对
    - 难度：简单
    - 相关算法：逆序对、数学

40. **Codeforces 558E. A Simple Task**
    - 链接：https://codeforces.com/problemset/problem/558/E
    - 特点：字符串排序与归并思想
    - 难度：中等
    - 相关算法：线段树、排序

41. **HackerRank Merge Sort: Counting Inversions**
    - 链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
    - 特点：企业面试常见题
    - 难度：中等
    - 相关算法：逆序对统计

42. **SPOJ INVCNT. Inversion Count**
    - 链接：https://www.spoj.com/problems/INVCNT/
    - 特点：专门测试逆序对
    - 难度：简单
    - 相关算法：归并排序

43. **AtCoder ABC163F - Path Pass i**
    - 链接：https://atcoder.jp/contests/abc163/tasks/abc163_f
    - 特点：树上路径计数问题
    - 难度：困难
    - 相关算法：树链剖分、线段树

### 其他国内平台
44. **杭电OJ 1394. Minimum Inversion Number**
    - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
    - 特点：经典题目，多校赛常见

45. **赛码网 归并排序相关题目**
    - 平台：https://www.acmcoder.com/
    - 特点：企业笔试常见

46. **计蒜客 排序相关题目**
    - 平台：https://www.jisuanke.com/
    - 特点：算法竞赛培训

47. **MarsCode 算法题库**
    - 平台：https://www.marscode.cn/
    - 特点：新兴算法平台

48. **ZOJ 1610 Count the Colors**
    - 链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
    - 特点：区间染色和颜色段计数
    - 相关算法：区间更新 + 区间查询

49. **SPOJ GSS1 - Can you answer these queries I**
    - 链接：https://www.spoj.com/problems/GSS1/
    - 特点：最大子段和查询
    - 相关算法：最大子段和线段树

50. **SPOJ GSS3 - Can you answer these queries III**
    - 链接：https://www.spoj.com/problems/GSS3/
    - 特点：最大子段和查询（支持单点更新）
    - 相关算法：最大子段和线段树

### 特殊类型题目
51. **外部排序相关题目**
    - 特点：处理超大规模数据
    - 应用场景：数据库排序、大数据处理

52. **多路归并题目**
    - 特点：合并多个有序序列
    - 应用场景：大数据处理

53. **并行归并排序题目**
    - 特点：多线程优化
    - 应用场景：高性能计算

54. **稳定排序应用题目**
    - 特点：需要保持相等元素相对顺序
    - 应用场景：数据库查询、金融系统

55. **自然归并排序题目**
    - 特点：利用数据局部有序性
    - 应用场景：部分有序数据处理

56. **TimSort算法相关题目**
    - 特点：混合排序算法
    - 应用场景：Python、Java等语言标准库

### 综合训练题目（按难度分级）
#### 入门级（掌握基础）
57. **基础归并排序实现**
58. **逆序对统计基础版**

#### 进阶级（理解应用）
59. **链表归并排序**
60. **合并K个有序序列**
61. **区间和统计**

#### 高手级（深入掌握）
62. **翻转对统计**
63. **复杂条件逆序对**
64. **外部排序实现**

#### 专家级（工程应用）
65. **并行归并排序优化**
66. **稳定排序系统设计**
67. **大规模数据处理**

### 题目分类总结
| 类别 | 题目数量 | 核心算法 | 应用场景 |
|------|----------|----------|----------|
| 基础排序 | 15+ | 归并排序模板 | 算法学习、面试基础 |
| 逆序对统计 | 20+ | 归并排序变种 | 数学统计、金融分析 |
| 链表排序 | 5+ | 链表归并排序 | 数据结构应用 |
| 多路合并 | 8+ | 分治合并 | 大数据处理 |
| 区间统计 | 6+ | 前缀和+归并 | 数据分析 |
| 工程优化 | 10+ | 各种优化技巧 | 实际系统开发 |

### 训练建议
1. **基础阶段**：先掌握归并排序的递归和非递归实现
2. **应用阶段**：练习逆序对、链表排序等变种问题
3. **提高阶段**：解决复杂条件的统计问题
4. **实战阶段**：参与在线评测，检验掌握程度

### 学习资源推荐
1. **书籍**：《算法导论》、《编程珠玑》
2. **视频**：各大MOOC平台的算法课程
3. **练习**：LeetCode、洛谷、AcWing等平台
4. **社区**：Stack Overflow、GitHub开源项目

通过系统学习以上题目，可以全面掌握归并排序及其应用，为算法竞赛和面试打下坚实基础。

### 工程化考虑

1. **边界处理**：处理空数组、单元素数组、重复元素
2. **异常处理**：对非法输入进行检查和处理
3. **性能优化**：
   - 小数组可以使用插入排序优化
   - 非递归版本避免栈溢出风险
4. **内存管理**：合理使用辅助数组，避免频繁内存分配
5. **稳定性**：归并排序是稳定排序，在需要保持相等元素相对顺序时优先使用

### 适用场景

1. **大数据量排序**：时间复杂度稳定为O(n log n)
2. **外部排序**：适合处理大量无法一次性装入内存的数据
3. **稳定排序需求**：需要保持相等元素相对顺序的场景
4. **链表排序**：归并排序特别适合链表结构，只需修改指针
5. **逆序对统计**：利用归并排序的合并过程可以高效统计逆序对

### 总结

归并排序不仅是重要的排序算法，更是分治思想的经典体现。通过系统学习归并排序及其变种问题，可以深入理解算法设计的核心思想，为学习更复杂的算法打下坚实基础。

## 详细题目列表

更多题目请参考同目录下的[MERGE_SORT_PROBLEMS.md](MERGE_SORT_PROBLEMS.md)文件，包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目。