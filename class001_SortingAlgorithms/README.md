# 排序算法与数据结构专题 - Class001

## 📚 目录
- [算法概述](#算法概述)
- [核心排序算法](#核心排序算法)
- [扩展题目](#扩展题目)
- [工程化考量](#工程化考量)
- [复杂度分析](#复杂度分析)
- [面试技巧](#面试技巧)
- [实战训练](#实战训练)

## 🎯 算法概述

本专题深入探讨排序算法及其相关应用，涵盖从基础排序到高级应用的完整知识体系。

### 核心算法
- **归并排序** - 分治思想的经典应用
- **快速排序** - 实际应用最广泛的排序算法
- **堆排序** - 原地排序的O(n log n)算法
- **其他基础排序** - 插入、冒泡、选择排序

## 🔧 核心排序算法

### 1. 归并排序 (Merge Sort)
**时间复杂度**: O(n log n)  
**空间复杂度**: O(n)  
**稳定性**: 稳定

**适用场景**:
- 需要稳定排序的场合
- 链表排序
- 外部排序（大数据量无法全部加载到内存）

**关键特性**:
- 分治思想的典型应用
- 递归实现简单直观
- 迭代实现节省栈空间

### 2. 快速排序 (Quick Sort)
**时间复杂度**: O(n log n) 平均, O(n²) 最坏  
**空间复杂度**: O(log n) 平均, O(n) 最坏  
**稳定性**: 不稳定

**适用场景**:
- 通用排序（实际应用最广泛）
- 内存受限环境
- 需要原地排序的场合

**优化策略**:
- 随机化基准选择
- 三路划分处理重复元素
- 小数组使用插入排序

### 3. 堆排序 (Heap Sort)
**时间复杂度**: O(n log n)  
**空间复杂度**: O(1)  
**稳定性**: 不稳定

**适用场景**:
- 需要原地排序且要求O(n log n)时间复杂度
- 优先级队列实现
- 大数据量的部分排序

## 📈 扩展题目

### 题目1: 215. 数组中的第K个最大元素
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/kth-largest-element-in-an-array/

**多种解法**:
1. **快速选择算法** (最优解)
   - 时间复杂度: O(n) 平均
   - 空间复杂度: O(1)
2. **最小堆实现**
   - 时间复杂度: O(n log k)
   - 空间复杂度: O(k)
3. **排序后直接取**
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(1)

**相关题目**:
- **ALDS1_6_B: Partition** - https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B

### 题目2: 75. 颜色分类 (荷兰国旗问题)
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/sort-colors/

**解法**:
- **三指针法**: O(n)时间, O(1)空间
- **计数排序**: O(n)时间, O(1)空间（只有3种颜色）

**相关题目**:
- **ALDS1_2_C: Stable Sort** - https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C

### 题目3: 56. 合并区间
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/merge-intervals/

**解法**: 排序+合并，时间复杂度O(n log n)

**相关题目**:
- **ALDS1_6_D: Minimum Cost Sort** - https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D

### 题目4: 347. 前K个高频元素
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/top-k-frequent-elements/

**解法**:
- **最小堆法**: O(n log k)时间
- **桶排序**: O(n)时间

### 题目5: 164. 最大间距
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/maximum-gap/

**解法**:
- **基数排序**: O(n)时间（线性时间）
- **普通排序**: O(n log n)时间

**相关题目**:
- **ALDS1_5_D: The Number of Inversions** - https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D

## 🏗️ 工程化考量

### 1. 异常处理
```python
# 输入验证
if not isinstance(nums, list):
    raise TypeError("输入必须是列表")
if not all(isinstance(x, int) for x in nums):
    raise TypeError("列表必须只包含整数")
```

### 2. 边界条件处理
- 空数组处理
- 单元素数组
- 已排序/逆序数组
- 大量重复元素

### 3. 性能优化
- 小数组优化（使用插入排序）
- 避免不必要的内存分配
- 缓存友好性考虑

### 4. 可测试性
- 单元测试覆盖各种边界情况
- 性能基准测试
- 内存使用监控

## 📊 复杂度分析

### 时间复杂度对比
| 算法 | 最好情况 | 平均情况 | 最坏情况 | 空间复杂度 |
|------|----------|----------|----------|------------|
| 归并排序 | O(n log n) | O(n log n) | O(n log n) | O(n) |
| 快速排序 | O(n log n) | O(n log n) | O(n²) | O(log n) |
| 堆排序 | O(n log n) | O(n log n) | O(n log n) | O(1) |
| 插入排序 | O(n) | O(n²) | O(n²) | O(1) |

### 空间复杂度分析
- **归并排序**: 需要额外O(n)空间存储临时数组
- **快速排序**: 递归调用栈空间O(log n)平均
- **堆排序**: 原地排序，O(1)空间

## 💡 面试技巧

### 1. 算法选择依据
- **数据规模**: 小数据用简单排序，大数据用高效排序
- **内存限制**: 内存紧张时选择原地排序
- **稳定性要求**: 需要稳定排序时选择归并排序
- **数据特性**: 大量重复元素时使用三路快排

### 2. 代码实现要点
- 清晰的变量命名
- 关键步骤注释
- 边界条件处理
- 异常情况考虑

### 3. 性能分析能力
- 能够分析时间/空间复杂度
- 理解常数项对实际性能的影响
- 知道如何优化常数项

## 🎯 实战训练

### 推荐练习题目

#### LeetCode
1. **88. 合并两个有序数组** - 归并排序应用
2. **148. 排序链表** - 链表上的归并排序
3. **912. 排序数组** - 各种排序算法的实现
4. **973. 最接近原点的K个点** - 快速选择应用
5. **1054. 距离相等的条形码** - 堆排序应用

#### 牛客网
1. **NC140 排序** - 基础排序实现
2. **NC119 最小的K个数** - 堆的应用
3. **NC88 寻找第K大** - 快速选择

#### 剑指Offer
1. **面试题40. 最小的k个数** - 堆/快速选择
2. **面试题51. 数组中的逆序对** - 归并排序应用

### 进阶题目
1. **外部排序** - 处理超大数据集
2. **并行排序** - 多线程/分布式排序
3. **稳定快速排序** - 保持稳定性的快速排序变种

## 🔍 调试技巧

### 1. 打印中间过程
```python
def quick_sort_debug(nums, low, high, depth=0):
    indent = "  " * depth
    print(f"{indent}quick_sort({low}, {high}): {nums[low:high+1]}")
    # ... 排序逻辑
```

### 2. 断言验证
```python
def test_sort_algorithm():
    test_cases = [
        ([], []),
        ([1], [1]),
        ([3, 1, 2], [1, 2, 3])
    ]
    
    for input_nums, expected in test_cases:
        result = sort_algorithm(input_nums)
        assert result == expected, f"Failed for {input_nums}"
```

### 3. 性能分析
```python
import time
import random

def benchmark_sort(algorithm, size=10000):
    test_data = [random.randint(0, size) for _ in range(size)]
    
    start_time = time.time()
    result = algorithm(test_data)
    end_time = time.time()
    
    return end_time - start_time
```

## 📚 学习资源

### 推荐书籍
1. 《算法导论》 - 排序算法理论基础
2. 《编程珠玑》 - 排序算法的实际应用
3. 《算法》 - 各种排序算法的实现和比较

### 在线资源
1. **VisualGo** - 排序算法可视化
2. **USFCA Sorting Animations** - 动画演示
3. **Khan Academy** - 算法课程

### 实践平台
1. **LeetCode** - 算法题目练习
2. **HackerRank** - 编程挑战
3. **牛客网** - 国内算法题库

---

**持续更新中... 更多题目和解析将陆续添加**

*最后更新: 2025年10月17日*