# 扩展排序算法题目与训练

## 📋 更多排序相关题目（扩展版）

### LeetCode 题目补充（新增）

#### 基础排序题目
1. **88. 合并两个有序数组**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/merge-sorted-array/
   - 难度: 简单
   - 解法: 双指针从后向前合并
   - 时间复杂度: O(m+n)
   - 空间复杂度: O(1)
   - 最优解: 是

2. **148. 排序链表**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/sort-list/
   - 难度: 中等
   - 解法: 归并排序（链表版本）
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(log n) 递归栈
   - 最优解: 是

3. **912. 排序数组**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/sort-an-array/
   - 难度: 中等
   - 解法: 各种排序算法的实现和比较
   - 时间复杂度: 根据算法选择
   - 空间复杂度: 根据算法选择

#### 快速选择相关
4. **973. 最接近原点的K个点**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
   - 难度: 中等
   - 解法: 快速选择/堆排序
   - 时间复杂度: O(n) 平均
   - 空间复杂度: O(1)
   - 最优解: 是

5. **1054. 距离相等的条形码**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/distant-barcodes/
   - 难度: 中等
   - 解法: 堆排序（频率统计）
   - 时间复杂度: O(n log k)
   - 空间复杂度: O(n)
   - 最优解: 是

#### 特殊排序
6. **164. 最大间距**（已包含）
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/maximum-gap/
   - 难度: 困难
   - 解法: 基数排序/桶排序
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

7. **324. 摆动排序 II**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/wiggle-sort-ii/
   - 难度: 中等
   - 解法: 排序+双指针
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 最优解: 是

8. **280. 摆动排序**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/wiggle-sort/
   - 难度: 中等
   - 解法: 一次遍历交换
   - 时间复杂度: O(n)
   - 空间复杂度: O(1)
   - 最优解: 是

#### 困难题目
9. **493. 翻转对**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/reverse-pairs/
   - 难度: 困难
   - 解法: 归并排序统计
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)
   - 最优解: 是

### 牛客网题目（新增）

1. **NC140 排序**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
   - 难度: 简单
   - 解法: 各种排序算法实现
   - 时间复杂度: 根据算法选择
   - 空间复杂度: 根据算法选择

2. **NC119 最小的K个数**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
   - 难度: 中等
   - 解法: 堆/快速选择
   - 时间复杂度: O(n log k) / O(n)
   - 空间复杂度: O(k) / O(1)

3. **NC88 寻找第K大**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
   - 难度: 中等
   - 解法: 快速选择算法
   - 时间复杂度: O(n) 平均
   - 空间复杂度: O(1)

### 剑指Offer题目（新增）

1. **面试题40. 最小的k个数**
   - 来源: 剑指Offer
   - 链接: 剑指Offer第二版第40题
   - 难度: 简单
   - 解法: 堆/快速选择
   - 时间复杂度: O(n log k) / O(n)
   - 空间复杂度: O(k) / O(1)

2. **面试题51. 数组中的逆序对**
   - 来源: 剑指Offer
   - 链接: 剑指Offer第二版第51题
   - 难度: 困难
   - 解法: 归并排序
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)

3. **面试题45. 把数组排成最小的数**
   - 来源: 剑指Offer
   - 链接: 剑指Offer第二版第45题
   - 难度: 中等
   - 解法: 自定义排序
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)

### HackerRank题目（新增）

1. **Fraudulent Activity Notifications**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/fraudulent-activity-notifications
   - 难度: 中等
   - 解法: 滑动窗口+计数排序
   - 时间复杂度: O(n)
   - 空间复杂度: O(1)

2. **Counting Inversions**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/ctci-merge-sort
   - 难度: 困难
   - 解法: 归并排序统计逆序对
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)

### Codeforces题目（新增）

1. **Sort the Array**
   - 来源: Codeforces
   - 链接: https://codeforces.com/problemset/problem/451/B
   - 难度: 简单
   - 解法: 寻找逆序段
   - 时间复杂度: O(n)
   - 空间复杂度: O(1)

2. **Mike and Feet**
   - 来源: Codeforces
   - 链接: https://codeforces.com/problemset/problem/547/B
   - 难度: 中等
   - 解法: 单调栈+排序
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)

### AtCoder题目（新增）

1. **Sorting**
   - 来源: AtCoder
   - 链接: https://atcoder.jp/contests/abc163/tasks/abc163_c
   - 难度: 简单
   - 解法: 计数排序
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

2. **Sorting a Segment**
   - 来源: AtCoder
   - 链接: https://atcoder.jp/contests/abc242/tasks/abc242_d
   - 难度: 中等
   - 解法: 滑动窗口+排序
   - 时间复杂度: O(n log k)
   - 空间复杂度: O(k)

## 🎯 题目分类训练（扩展版）

### 按算法分类训练

#### 归并排序训练
1. **逆序对计数** - 归并排序的经典应用
2. **链表排序** - 归并排序在链表上的实现
3. **外部排序** - 处理超大数据集的排序
4. **翻转对统计** - 扩展的逆序对问题

#### 快速排序训练
1. **三路快排** - 处理大量重复元素
2. **快速选择** - 寻找第K大/小元素
3. **荷兰国旗问题** - 三色排序
4. **最接近点选择** - 距离计算+快速选择

#### 堆排序训练
1. **Top K问题** - 前K大/小元素
2. **中位数查找** - 动态数据流的中位数
3. **优先级队列** - 堆的实际应用
4. **频率统计排序** - 按频率排序

#### 特殊排序训练
1. **基数排序** - 处理大范围整数
2. **桶排序** - 均匀分布数据
3. **计数排序** - 小范围整数
4. **希尔排序** - 改进的插入排序

### 按难度分级训练

#### 初级（掌握基础）
- 实现各种基础排序算法
- 理解时间/空间复杂度
- 处理简单边界条件
- 编写单元测试

#### 中级（应用扩展）
- 解决LeetCode中等难度题目
- 掌握算法优化技巧
- 处理复杂边界情况
- 进行性能分析

#### 高级（深入理解）
- 解决困难题目
- 理解算法底层原理
- 进行性能优化和工程化
- 处理大数据量场景

## 💡 解题思路总结（扩展版）

### 见到排序题目的思考流程

1. **分析题目要求**
   - 是否需要稳定排序？
   - 是否有空间限制？
   - 数据规模有多大？
   - 数据分布特征？
   - 是否需要原地排序？

2. **选择合适算法**
   - 小数据（n < 50）：插入/选择排序
   - 大数据：快速/归并/堆排序
   - 需要稳定：归并排序
   - 空间紧张：堆排序/原地快排
   - 数据范围小：计数/基数排序

3. **考虑优化策略**
   - 小数组优化
   - 随机化避免最坏情况
   - 处理重复元素
   - 利用数据特性

4. **处理边界条件**
   - 空数组
   - 单元素
   - 已排序/逆序
   - 大量重复
   - 极端值

### 常见题型模式（扩展版）

#### 模式1: Top K问题
- 特征：寻找前K大/小元素
- 解法：快速选择(O(n))或堆(O(n log k))
- 变种：最接近点、频率最高元素

#### 模式2: 区间合并
- 特征：重叠区间合并
- 解法：按起点排序后合并
- 变种：会议室安排、区间插入

#### 模式3: 颜色分类
- 特征：有限种类的排序
- 解法：计数排序/多指针
- 变种：荷兰国旗、三路快排

#### 模式4: 逆序对统计
- 特征：统计逆序对数量
- 解法：归并排序
- 变种：翻转对、重要逆序对

#### 模式5: 自定义排序
- 特征：特殊的比较规则
- 解法：实现自定义比较器
- 变种：字符串拼接、特殊规则排序

## 🛠️ 代码实现要点（扩展版）

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
    
    // 4. 内存监控
    public void sortWithMemory(T[] array) {
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        sort(array);
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("内存使用: " + (memoryAfter - memoryBefore) + "字节");
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
    
    // 4. 性能分析
    void sortWithProfiling(std::vector<T>& nums) {
        auto start = std::chrono::high_resolution_clock::now();
        sort(nums);
        auto end = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);
        std::cout << "耗时: " << duration.count() << "微秒" << std::endl;
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
    
    # 5. 内存分析装饰器
    @staticmethod
    def memory_profiled_sort(nums):
        import tracemalloc
        tracemalloc.start()
        result = SortAlgorithms.sort(nums)
        current, peak = tracemalloc.get_traced_memory()
        tracemalloc.stop()
        print(f"峰值内存: {peak / 1024:.2f} KB")
        return result
```

## 📊 复杂度分析深度（扩展版）

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

### 堆排序复杂度分析
```
建堆: O(n)
每次调整: O(log n)
总复杂度: O(n log n)
```

## 🔧 工程化实践（扩展版）

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
    
    def test_large_random_array(self):
        import random
        nums = [random.randint(1, 1000) for _ in range(1000)]
        sorted_nums = SortAlgorithms.sort(nums.copy())
        self.assertEqual(sorted_nums, sorted(nums))
    
    def test_negative_numbers(self):
        self.assertEqual(SortAlgorithms.sort([-3, -1, -2]), [-3, -2, -1])
    
    def test_mixed_numbers(self):
        self.assertEqual(SortAlgorithms.sort([3, -1, 0, -2, 1]), [-2, -1, 0, 1, 3])
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

### 4. 压力测试
```python
def stress_test(algorithm, max_size=1000000):
    """压力测试：测试算法在大数据量下的表现"""
    print("开始压力测试...")
    
    # 测试随机数据
    random_data = [random.randint(1, 1000000) for _ in range(max_size)]
    start_time = time.time()
    result = algorithm(random_data)
    end_time = time.time()
    
    print(f"随机数据排序耗时: {end_time - start_time:.2f}秒")
    
    # 测试已排序数据
    sorted_data = list(range(max_size))
    start_time = time.time()
    result = algorithm(sorted_data)
    end_time = time.time()
    
    print(f"已排序数据排序耗时: {end_time - start_time:.2f}秒")
    
    # 测试逆序数据
    reverse_data = list(range(max_size, 0, -1))
    start_time = time.time()
    result = algorithm(reverse_data)
    end_time = time.time()
    
    print(f"逆序数据排序耗时: {end_time - start_time:.2f}秒")
```

## 🎓 面试准备指南（扩展版）

### 1. 算法原理理解
- 能够白板写出各种排序算法
- 理解时间/空间复杂度推导
- 知道各种算法的优缺点
- 理解稳定性的概念和重要性

### 2. 代码实现能力
- 写出清晰、健壮的代码
- 处理各种边界条件
- 进行适当的优化
- 编写完整的测试用例

### 3. 问题分析能力
- 快速识别问题类型
- 选择合适的算法
- 分析算法适用性
- 考虑优化空间

### 4. 沟通表达能力
- 清晰解释算法思路
- 分析时间/空间复杂度
- 讨论优化可能性
- 展示调试和优化过程

### 5. 系统设计能力
- 设计可扩展的排序系统
- 考虑大数据量处理
- 设计分布式排序方案
- 考虑容错和恢复机制

---

**持续补充更多题目和解析...**