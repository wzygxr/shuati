# 补充算法题目与训练

## 📋 更多排序相关题目

### LeetCode 题目补充

#### 基础排序题目
1. **88. 合并两个有序数组**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/merge-sorted-array/
   - 难度: 简单
   - 解法: 双指针从后向前合并

2. **148. 排序链表**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/sort-list/
   - 难度: 中等
   - 解法: 归并排序（链表版本）

3. **912. 排序数组**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/sort-an-array/
   - 难度: 中等
   - 解法: 各种排序算法的实现和比较

#### 快速选择相关
4. **973. 最接近原点的K个点**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
   - 难度: 中等
   - 解法: 快速选择/堆排序

5. **1054. 距离相等的条形码**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/distant-barcodes/
   - 难度: 中等
   - 解法: 堆排序（频率统计）

#### 特殊排序
6. **164. 最大间距**（已包含）
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/maximum-gap/
   - 难度: 困难
   - 解法: 基数排序/桶排序

7. **324. 摆动排序 II**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/wiggle-sort-ii/
   - 难度: 中等
   - 解法: 排序+双指针

### 牛客网题目

1. **NC140 排序**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
   - 难度: 简单
   - 解法: 各种排序算法实现

2. **NC119 最小的K个数**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
   - 难度: 中等
   - 解法: 堆/快速选择

3. **NC88 寻找第K大**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
   - 难度: 中等
   - 解法: 快速选择算法

### 剑指Offer题目

1. **面试题40. 最小的k个数**
   - 来源: 剑指Offer
   - 链接: 剑指Offer第二版第40题
   - 难度: 简单
   - 解法: 堆/快速选择

2. **面试题51. 数组中的逆序对**
   - 来源: 剑指Offer
   - 链接: 剑指Offer第二版第51题
   - 难度: 困难
   - 解法: 归并排序

### HackerRank题目

1. **Fraudulent Activity Notifications**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/fraudulent-activity-notifications
   - 难度: 中等
   - 解法: 滑动窗口+计数排序

2. **Counting Inversions**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/ctci-merge-sort
   - 难度: 困难
   - 解法: 归并排序统计逆序对

## 🎯 题目分类训练

### 按算法分类训练

#### 归并排序训练
1. **逆序对计数** - 归并排序的经典应用
2. **链表排序** - 归并排序在链表上的实现
3. **外部排序** - 处理超大数据集的排序

#### 快速排序训练
1. **三路快排** - 处理大量重复元素
2. **快速选择** - 寻找第K大/小元素
3. **荷兰国旗问题** - 三色排序

#### 堆排序训练
1. **Top K问题** - 前K大/小元素
2. **中位数查找** - 动态数据流的中位数
3. **优先级队列** - 堆的实际应用

### 按难度分级训练

#### 初级（掌握基础）
- 实现各种基础排序算法
- 理解时间/空间复杂度
- 处理简单边界条件

#### 中级（应用扩展）
- 解决LeetCode中等难度题目
- 掌握算法优化技巧
- 处理复杂边界情况

#### 高级（深入理解）
- 解决困难题目
- 理解算法底层原理
- 进行性能优化和工程化

## 💡 解题思路总结

### 见到排序题目的思考流程

1. **分析题目要求**
   - 是否需要稳定排序？
   - 是否有空间限制？
   - 数据规模有多大？
   - 数据分布特征？

2. **选择合适算法**
   - 小数据：插入/选择排序
   - 大数据：快速/归并/堆排序
   - 需要稳定：归并排序
   - 空间紧张：堆排序/原地快排

3. **考虑优化策略**
   - 小数组优化
   - 随机化避免最坏情况
   - 处理重复元素

4. **处理边界条件**
   - 空数组
   - 单元素
   - 已排序/逆序
   - 大量重复

### 常见题型模式

#### 模式1: Top K问题
- 特征：寻找前K大/小元素
- 解法：快速选择(O(n))或堆(O(n log k))

#### 模式2: 区间合并
- 特征：重叠区间合并
- 解法：按起点排序后合并

#### 模式3: 颜色分类
- 特征：有限种类的排序
- 解法：计数排序/多指针

#### 模式4: 逆序对统计
- 特征：统计逆序对数量
- 解法：归并排序

## 🛠️ 代码实现要点

### Java实现要点
```java
// 1. 使用泛型支持多种数据类型
public class SortAlgorithms<T extends Comparable<T>> {
    
    // 2. 异常处理
    public void sort(T[] array) {
        if (array == null) throw new IllegalArgumentException();
        if (array.length <= 1) return;
        
        // 排序逻辑
    }
    
    // 3. 性能监控
    public void sortWithTiming(T[] array) {
        long start = System.nanoTime();
        sort(array);
        long end = System.nanoTime();
        System.out.println("耗时: " + (end - start) + "纳秒");
    }
}
```

### C++实现要点
```cpp
// 1. 模板支持泛型
template<typename T>
class SortAlgorithms {
public:
    // 2. 异常安全
    void sort(std::vector<T>& nums) {
        if (nums.empty()) return;
        
        // 使用RAII确保资源安全
        // 排序逻辑
    }
    
    // 3. 移动语义优化
    std::vector<T> sorted(std::vector<T> nums) {
        sort(nums);
        return nums; // 移动语义优化
    }
};
```

### Python实现要点
```python
class SortAlgorithms:
    # 1. 类型注解
    @staticmethod
    def sort(nums: List[int]) -> List[int]:
        # 2. 输入验证
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表")
        
        # 3. 边界处理
        if len(nums) <= 1:
            return nums.copy()
        
        # 排序逻辑
        return sorted_nums
    
    # 4. 性能测试装饰器
    @staticmethod
    def timed_sort(nums):
        import time
        start = time.time()
        result = SortAlgorithms.sort(nums)
        end = time.time()
        print(f"排序耗时: {end - start:.6f}秒")
        return result
```

## 📊 复杂度分析深度

### 归并排序复杂度推导
```
T(n) = 2T(n/2) + O(n)
     = 2[2T(n/4) + O(n/2)] + O(n)
     = 4T(n/4) + 2O(n/2) + O(n)
     = 4T(n/4) + 2O(n)
     = ...
     = 2^k T(n/2^k) + kO(n)
     
当 n/2^k = 1 => k = log₂n
T(n) = nT(1) + O(n log n) = O(n log n)
```

### 快速排序复杂度分析
**最好情况**: 每次划分均衡
```
T(n) = 2T(n/2) + O(n) = O(n log n)
```

**最坏情况**: 每次划分极端不平衡
```
T(n) = T(n-1) + O(n) = O(n²)
```

**平均情况**: 通过随机化达到O(n log n)

## 🔧 工程化实践

### 1. 单元测试设计
```python
import unittest

class TestSortAlgorithms(unittest.TestCase):
    def test_empty_array(self):
        self.assertEqual(SortAlgorithms.sort([]), [])
    
    def test_single_element(self):
        self.assertEqual(SortAlgorithms.sort([1]), [1])
    
    def test_already_sorted(self):
        self.assertEqual(SortAlgorithms.sort([1, 2, 3]), [1, 2, 3])
    
    def test_reverse_sorted(self):
        self.assertEqual(SortAlgorithms.sort([3, 2, 1]), [1, 2, 3])
    
    def test_duplicate_elements(self):
        self.assertEqual(SortAlgorithms.sort([2, 2, 1, 1]), [1, 1, 2, 2])
```

### 2. 性能基准测试
```python
def benchmark_different_sizes():
    sizes = [100, 1000, 10000, 100000]
    algorithms = {
        '归并排序': merge_sort,
        '快速排序': quick_sort,
        '堆排序': heap_sort,
        '内置排序': sorted
    }
    
    for size in sizes:
        test_data = generate_test_data(size)
        print(f"\n数据规模: {size}")
        
        for name, algorithm in algorithms.items():
            time_taken = time_algorithm(algorithm, test_data)
            print(f"{name}: {time_taken:.6f}秒")
```

### 3. 内存使用监控
```python
import tracemalloc

def monitor_memory_usage(algorithm, data):
    tracemalloc.start()
    
    # 执行算法
    result = algorithm(data)
    
    current, peak = tracemalloc.get_traced_memory()
    tracemalloc.stop()
    
    print(f"当前内存使用: {current / 10**6:.2f} MB")
    print(f"峰值内存使用: {peak / 10**6:.2f} MB")
    
    return result
```

## 🎓 面试准备指南

### 1. 算法原理理解
- 能够白板写出各种排序算法
- 理解时间/空间复杂度推导
- 知道各种算法的优缺点

### 2. 代码实现能力
- 写出清晰、健壮的代码
- 处理各种边界条件
- 进行适当的优化

### 3. 问题分析能力
- 快速识别问题类型
- 选择合适的算法
- 分析算法适用性

### 4. 沟通表达能力
- 清晰解释算法思路
- 分析时间/空间复杂度
- 讨论优化可能性

---

**持续补充更多题目和解析...**