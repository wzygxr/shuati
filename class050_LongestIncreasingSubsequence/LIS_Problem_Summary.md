# 最长递增子序列（LIS）问题全解析

## 问题概述

最长递增子序列（Longest Increasing Subsequence, LIS）问题是动态规划中的经典问题，要求在给定数组中找到最长的严格递增子序列。

### 核心概念

- **子序列**：从原数组中删除若干元素（也可以不删除）得到的序列，保持原有顺序
- **严格递增**：相邻元素满足 `a[i] < a[i+1]`
- **非递减（不下降）**：相邻元素满足 `a[i] <= a[i+1]`

## 基本算法

### 1. 动态规划解法

```java
// dp[i] 表示以 nums[i] 结尾的最长递增子序列长度
public static int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n];
    int ans = 0;
    
    for (int i = 0; i < n; i++) {
        dp[i] = 1;
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
        ans = Math.max(ans, dp[i]);
    }
    
    return ans;
}
```

- 时间复杂度：O(n²)
- 空间复杂度：O(n)

### 2. 贪心+二分查找优化解法

```java
// 维护 ends 数组，ends[i] 表示长度为 i+1 的递增子序列的最小结尾元素
public static int lengthOfLIS(int[] nums) {
    int n = nums.length;
    int[] ends = new int[n];
    int len = 0;
    
    for (int i = 0; i < n; i++) {
        int find = binarySearch(ends, len, nums[i]);
        if (find == -1) {
            ends[len++] = nums[i];
        } else {
            ends[find] = nums[i];
        }
    }
    
    return len;
}
```

- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

## 经典变种问题

### 1. 最长递增子序列的个数（LeetCode 673）

在基本LIS问题基础上，要求返回最长递增子序列的个数。

**关键点**：
- 维护两个数组：dp[i]（长度）、cnt[i]（个数）
- 状态转移时更新长度和计数

### 2. 俄罗斯套娃信封问题（LeetCode 354）

二维LIS问题，需要先排序再转化为一维LIS。

**关键点**：
- 按宽度升序排序，宽度相同时按高度降序排序
- 对高度数组求LIS

### 3. 最长数对链（LeetCode 646）

可以使用LIS方法，也可以使用贪心算法。

**关键点**：
- 贪心算法更优：按结束位置排序

### 4. 使数组K递增的最少操作次数（LeetCode 2100）

将数组分组后分别求最长不下降子序列。

**关键点**：
- 按间隔k分组
- 每组需要修改的元素数 = 组长度 - LIS长度

### 5. 有一次修改机会的最长不下降子序列（洛谷P8776）

枚举修改区间，预处理前后缀信息。

**关键点**：
- 预处理right数组
- 枚举修改区间计算最优解

### 6. 最长字符串链（LeetCode 1048）

字符串版本的LIS问题。

**关键点**：
- 按长度排序
- 判断字符串前身关系

## 二分查找技巧总结

### 1. >= num 的最左位置

```java
public static int bs1(int[] ends, int len, int num) {
    int l = 0, r = len - 1, m, ans = -1;
    while (l <= r) {
        m = (l + r) / 2;
        if (ends[m] >= num) {
            ans = m;
            r = m - 1;
        } else {
            l = m + 1;
        }
    }
    return ans;
}
```

### 2. > num 的最左位置

```java
public static int bs2(int[] ends, int len, int num) {
    int l = 0, r = len - 1, m, ans = -1;
    while (l <= r) {
        m = (l + r) / 2;
        if (ends[m] > num) {
            ans = m;
            r = m - 1;
        } else {
            l = m + 1;
        }
    }
    return ans;
}
```

### 3. < num 的最左位置

```java
public static int bs3(int[] ends, int len, int num) {
    int l = 0, r = len - 1, m, ans = -1;
    while (l <= r) {
        m = (l + r) / 2;
        if (num < ends[m]) {
            ans = m;
            r = m - 1;
        } else {
            l = m + 1;
        }
    }
    return ans;
}
```

## 复杂度分析对比

| 问题 | 时间复杂度 | 空间复杂度 | 最优解 |
|------|------------|------------|--------|
| 基本LIS（DP） | O(n²) | O(n) | 否 |
| 基本LIS（贪心+二分） | O(n log n) | O(n) | 是 |
| LIS个数 | O(n²) | O(n) | 是 |
| 俄罗斯套娃信封 | O(n log n) | O(n) | 是 |
| 最长数对链（DP） | O(n²) | O(n) | 否 |
| 最长数对链（贪心） | O(n log n) | O(1) | 是 |
| 使数组K递增 | O(n log(n/k)) | O(n) | 是 |
| 字符串链 | O(N * L²) | O(N * L) | 是 |

## 工程化考量

### 1. 异常处理

- 空数组处理
- 单元素数组处理
- 重复元素处理

### 2. 边界场景

- 有序数组（递增、递减）
- 所有元素相同
- 极端值输入

### 3. 性能优化

- 使用二分查找优化时间复杂度
- 原地修改减少空间使用
- 预处理优化多次查询

### 4. 跨语言特性

- Java：使用Arrays.sort进行排序
- C++：使用std::sort进行排序
- Python：使用内置sort方法进行排序

## 面试要点

### 1. 理解本质

- LIS问题的核心是找到满足递增关系的最长子序列
- 贪心思想：维护结尾元素最小的序列

### 2. 算法对比

- 动态规划：思路直观，但时间复杂度较高
- 贪心+二分：时间复杂度更优，但理解难度较大

### 3. 变种问题

- 二维扩展：俄罗斯套娃信封
- 区间问题：最长数对链
- 数组变换：使数组K递增

### 4. 调试技巧

- 打印中间状态验证算法正确性
- 使用小规模测试用例验证边界情况
- 性能测试对比不同算法的效率

## 常见误区

### 1. 严格递增 vs 非递减

- 严格递增：`a[i] < a[i+1]`
- 非递减：`a[i] <= a[i+1]`
- 二分查找条件不同

### 2. 排序策略

- 俄罗斯套娃：宽度升序，高度降序
- 错误排序会导致重复选择

### 3. 状态转移

- LIS个数问题需要同时维护长度和计数
- 不能只考虑长度最大值

## 扩展应用

### 1. 机器学习

- 序列模式识别
- 时间序列分析

### 2. 图像处理

- 特征点匹配
- 轮廓检测

### 3. 自然语言处理

- 词序列分析
- 句法解析

### 4. 数据分析

- 趋势分析
- 异常检测

## 总结

LIS问题是一类重要的动态规划问题，掌握其基本解法和各种变种对于算法面试和实际开发都有重要意义。关键在于理解问题本质，选择合适的算法，并注意各种边界情况和优化技巧。