# 排序算法模式与技巧总结

## 🎯 排序算法选择指南

### 根据数据特征选择算法

| 数据特征 | 推荐算法 | 时间复杂度 | 空间复杂度 | 稳定性 |
|---------|---------|-----------|-----------|--------|
| 小规模数据(n<50) | 插入排序 | O(n²) | O(1) | 稳定 |
| 中等规模数据 | 快速排序 | O(n log n)平均 | O(log n) | 不稳定 |
| 大规模数据 | 归并排序 | O(n log n) | O(n) | 稳定 |
| 需要原地排序 | 堆排序 | O(n log n) | O(1) | 不稳定 |
| 数据范围小 | 计数排序 | O(n+k) | O(k) | 稳定 |
| 数据均匀分布 | 桶排序 | O(n) | O(n) | 稳定 |
| 整数排序 | 基数排序 | O(d(n+k)) | O(n+k) | 稳定 |

### 根据需求选择算法

| 需求 | 推荐算法 | 理由 |
|------|---------|------|
| 需要稳定排序 | 归并排序 | 保证相等元素的相对顺序 |
| 内存有限 | 堆排序 | 原地排序，空间复杂度O(1) |
| 平均性能最好 | 快速排序 | 实际应用中常数因子小 |
| 最坏情况保证 | 归并排序 | 最坏情况也是O(n log n) |
| 处理链表 | 归并排序 | 适合链表结构 |
| 外部排序 | 多路归并 | 处理大数据集 |

## 💡 常见问题模式识别

### 模式1: Top K问题
**识别特征**: 寻找前K大/小元素
**最优解法**: 
- 快速选择算法: O(n)平均时间复杂度
- 堆排序: O(n log k)时间复杂度

**相关题目**:
- LeetCode 215: 数组中的第K个最大元素
- LeetCode 973: 最接近原点的K个点
- 牛客网 NC88: 寻找第K大

**解题模板**:
```python
def find_kth_largest(nums, k):
    # 快速选择实现
    def quick_select(left, right, k_smallest):
        # 分区逻辑
        pivot_index = partition(left, right)
        if pivot_index == k_smallest:
            return nums[pivot_index]
        elif pivot_index < k_smallest:
            return quick_select(pivot_index + 1, right, k_smallest)
        else:
            return quick_select(left, pivot_index - 1, k_smallest)
    
    return quick_select(0, len(nums)-1, len(nums)-k)
```

### 模式2: 区间合并问题
**识别特征**: 重叠区间需要合并
**最优解法**: 按起点排序后合并

**相关题目**:
- LeetCode 56: 合并区间
- LeetCode 57: 插入区间
- LeetCode 252: 会议室

**解题模板**:
```python
def merge_intervals(intervals):
    if not intervals:
        return []
    
    # 按起点排序
    intervals.sort(key=lambda x: x[0])
    
    merged = []
    for interval in intervals:
        # 如果结果为空或当前区间不重叠
        if not merged or merged[-1][1] < interval[0]:
            merged.append(interval)
        else:
            # 合并区间
            merged[-1][1] = max(merged[-1][1], interval[1])
    
    return merged
```

### 模式3: 颜色分类/荷兰国旗问题
**识别特征**: 有限种类的元素需要分类
**最优解法**: 三指针/三路快排

**相关题目**:
- LeetCode 75: 颜色分类
- LeetCode 280: 摆动排序
- 剑指Offer 21: 调整数组顺序

**解题模板**:
```python
def sort_colors(nums):
    # 三指针：left, right, current
    left, current, right = 0, 0, len(nums) - 1
    
    while current <= right:
        if nums[current] == 0:
            nums[left], nums[current] = nums[current], nums[left]
            left += 1
            current += 1
        elif nums[current] == 2:
            nums[current], nums[right] = nums[right], nums[current]
            right -= 1
        else:
            current += 1
```

### 模式4: 逆序对统计
**识别特征**: 统计满足某种条件的逆序对数量
**最优解法**: 归并排序过程中统计

**相关题目**:
- LeetCode 493: 翻转对
- 剑指Offer 51: 数组中的逆序对
- HackerRank: Counting Inversions

**解题模板**:
```python
def reverse_pairs(nums):
    def merge_sort_count(left, right):
        if left >= right:
            return 0
        
        mid = (left + right) // 2
        count = merge_sort_count(left, mid) + merge_sort_count(mid + 1, right)
        
        # 统计逆序对
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += (j - (mid + 1))
        
        # 合并
        merge(left, mid, right)
        return count
    
    return merge_sort_count(0, len(nums) - 1)
```

### 模式5: 自定义排序规则
**识别特征**: 需要特殊的比较规则
**最优解法**: 实现自定义比较器

**相关题目**:
- LeetCode 179: 最大数
- 剑指Offer 45: 把数组排成最小的数
- LeetCode 524: 通过删除字母匹配到字典里最长单词

**解题模板**:
```python
def largest_number(nums):
    # 将数字转换为字符串
    str_nums = [str(num) for num in nums]
    
    # 自定义排序：比较 s1+s2 和 s2+s1
    str_nums.sort(key=lambda x: x*10, reverse=True)
    
    # 处理前导零
    result = ''.join(str_nums)
    return '0' if result[0] == '0' else result
```

## 🔧 优化技巧与策略

### 1. 小数组优化
**技巧**: 对于小数组(n<50)，使用简单排序算法
**理由**: 简单算法常数因子小，实际运行更快

```python
def optimized_sort(nums):
    if len(nums) <= 10:
        return insertion_sort(nums)  # 小数组使用插入排序
    else:
        return quick_sort(nums)      # 大数组使用快速排序
```

### 2. 随机化避免最坏情况
**技巧**: 随机选择pivot元素
**理由**: 避免快速排序的最坏情况

```python
import random

def randomized_quick_sort(nums):
    if len(nums) <= 1:
        return nums
    
    # 随机选择pivot
    pivot_index = random.randint(0, len(nums)-1)
    pivot = nums[pivot_index]
    
    # 分区逻辑
    left = [x for x in nums if x < pivot]
    middle = [x for x in nums if x == pivot]
    right = [x for x in nums if x > pivot]
    
    return randomized_quick_sort(left) + middle + randomized_quick_sort(right)
```

### 3. 处理重复元素优化
**技巧**: 使用三路快排
**理由**: 高效处理大量重复元素

```python
def three_way_quick_sort(nums):
    if len(nums) <= 1:
        return nums
    
    # 选择pivot
    pivot = nums[len(nums)//2]
    
    # 三路分区
    left = [x for x in nums if x < pivot]
    middle = [x for x in nums if x == pivot]
    right = [x for x in nums if x > pivot]
    
    return three_way_quick_sort(left) + middle + three_way_quick_sort(right)
```

### 4. 利用数据特性
**技巧**: 根据数据分布选择特殊排序算法
**理由**: 特定算法在特定数据分布下更高效

```python
def adaptive_sort(nums):
    if not nums:
        return []
    
    # 如果数据范围小，使用计数排序
    min_val, max_val = min(nums), max(nums)
    if max_val - min_val < 1000:
        return counting_sort(nums, min_val, max_val)
    
    # 如果数据基本有序，使用插入排序
    if is_almost_sorted(nums):
        return insertion_sort(nums)
    
    # 默认使用快速排序
    return quick_sort(nums)
```

## 🎯 面试实战技巧

### 1. 问题分析步骤
**第一步**: 理解题目要求
- 明确输入输出格式
- 理解排序规则
- 确定时间/空间复杂度要求

**第二步**: 分析数据特征
- 数据规模大小
- 数据分布情况
- 是否需要稳定排序

**第三步**: 选择合适算法
- 根据特征选择基础算法
- 考虑优化策略
- 准备备选方案

### 2. 代码实现要点
**清晰的变量命名**:
```python
# 好的命名
def merge_sorted_arrays(nums1, m, nums2, n):
    pointer1 = m - 1  # nums1有效部分末尾
    pointer2 = n - 1  # nums2末尾
    merge_pointer = m + n - 1  # 合并位置
    
# 差的命名
def merge(a, x, b, y):
    i = x - 1
    j = y - 1
    k = x + y - 1
```

**适当的注释**:
```python
def quick_select(nums, k):
    """
    快速选择算法寻找第k小元素
    
    Args:
        nums: 输入数组
        k: 要寻找的第k小元素索引(0-based)
    
    Returns:
        第k小的元素值
    """
    # 随机选择pivot避免最坏情况
    pivot_index = random.randint(0, len(nums)-1)
    pivot = nums[pivot_index]
    
    # 分区操作
    left = [x for x in nums if x < pivot]
    middle = [x for x in nums if x == pivot]
    right = [x for x in nums if x > pivot]
    
    # 根据分区结果递归选择
    if k < len(left):
        return quick_select(left, k)
    elif k < len(left) + len(middle):
        return pivot
    else:
        return quick_select(right, k - len(left) - len(middle))
```

### 3. 测试用例设计
**全面覆盖各种情况**:
```python
def test_sort_algorithm():
    # 空数组
    assert sort([]) == []
    
    # 单元素数组
    assert sort([1]) == [1]
    
    # 已排序数组
    assert sort([1, 2, 3]) == [1, 2, 3]
    
    # 逆序数组
    assert sort([3, 2, 1]) == [1, 2, 3]
    
    # 重复元素数组
    assert sort([2, 2, 1, 1]) == [1, 1, 2, 2]
    
    # 包含负数的数组
    assert sort([-3, -1, -2]) == [-3, -2, -1]
    
    # 混合正负数数组
    assert sort([3, -1, 0, -2, 1]) == [-2, -1, 0, 1, 3]
    
    # 大规模随机数组
    import random
    large_array = [random.randint(1, 10000) for _ in range(1000)]
    assert sort(large_array.copy()) == sorted(large_array)
```

### 4. 性能分析能力
**时间复杂度分析**:
```python
def analyze_time_complexity(algorithm, data_sizes):
    """
    分析算法的时间复杂度
    
    Args:
        algorithm: 排序算法函数
        data_sizes: 不同数据规模列表
    
    Returns:
        时间复杂度趋势分析
    """
    results = []
    for size in data_sizes:
        test_data = generate_test_data(size)
        start_time = time.time()
        algorithm(test_data)
        end_time = time.time()
        results.append((size, end_time - start_time))
    
    return analyze_trend(results)
```

**空间复杂度分析**:
```python
def analyze_space_complexity(algorithm, data):
    """
    分析算法的空间复杂度
    
    Args:
        algorithm: 排序算法函数
        data: 测试数据
    
    Returns:
        空间使用情况分析
    """
    import tracemalloc
    
    tracemalloc.start()
    result = algorithm(data)
    current, peak = tracemalloc.get_traced_memory()
    tracemalloc.stop()
    
    return {
        'peak_memory': peak,
        'current_memory': current,
        'data_size': len(data)
    }
```

## 🔍 调试与问题定位

### 1. 调试技巧
**打印中间结果**:
```python
def debug_merge_sort(nums, depth=0):
    indent = "  " * depth
    print(f"{indent}排序数组: {nums}")
    
    if len(nums) <= 1:
        return nums
    
    mid = len(nums) // 2
    left = debug_merge_sort(nums[:mid], depth+1)
    right = debug_merge_sort(nums[mid:], depth+1)
    
    result = merge(left, right)
    print(f"{indent}合并结果: {result}")
    return result
```

**断言验证**:
```python
def partition(nums, low, high):
    pivot = nums[high]
    i = low - 1
    
    for j in range(low, high):
        # 断言验证分区正确性
        assert all(nums[k] <= pivot for k in range(low, j+1) if nums[k] <= pivot)
        
        if nums[j] <= pivot:
            i += 1
            nums[i], nums[j] = nums[j], nums[i]
    
    nums[i+1], nums[high] = nums[high], nums[i+1]
    return i + 1
```

### 2. 性能问题定位
**识别性能瓶颈**:
```python
def profile_sort_algorithm():
    import cProfile
    import pstats
    
    # 生成测试数据
    test_data = generate_large_test_data()
    
    # 性能分析
    profiler = cProfile.Profile()
    profiler.enable()
    
    # 执行排序算法
    sort_algorithm(test_data)
    
    profiler.disable()
    stats = pstats.Stats(profiler)
    stats.sort_stats('cumulative')
    stats.print_stats(10)  # 显示前10个最耗时的函数
```

## 📚 进阶学习方向

### 1. 分布式排序算法
- MapReduce排序模式
- 外部排序算法
- 并行排序算法

### 2. 特殊数据结构排序
- 链表排序