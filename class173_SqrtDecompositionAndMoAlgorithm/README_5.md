# 三分枚举算法专题

## 算法原理

三分枚举（Ternary Search）是一种用于在单峰函数（unimodal function）上寻找极值点的搜索算法。单峰函数是指在定义域内先单调递增后单调递减（或先单调递减后单调递增）的函数，即函数只有一个极值点（最大值或最小值）。

三分枚举的基本思想是：

1. 在搜索区间 [left, right] 中选择两个中间点 m1 和 m2（通常取 m1 = left + (right - left)/3 和 m2 = right - (right - left)/3）
2. 比较函数在 m1 和 m2 处的值：f(m1) 和 f(m2)
3. 根据单峰函数的性质，通过比较这两个值来缩小搜索区间：
   - 如果 f(m1) < f(m2)，则极值点在 [m1, right] 区间内
   - 如果 f(m1) > f(m2)，则极值点在 [left, m2] 区间内
4. 重复上述过程，直到区间足够小，满足精度要求

## 时间复杂度分析

- 每次迭代将搜索区间缩小为原来的 2/3
- 经过 n 次迭代后，区间长度为初始长度的 (2/3)^n
- 通常需要 log( (right-left)/ε ) 次迭代，其中 ε 是精度要求
- 时间复杂度为 O(log n)，其中 n 是搜索区间的大小

## 算法实现

### 1. 基本三分枚举实现（求单峰函数的最大值）

**题目来源**：算法基础题

**题目描述**：实现三分枚举算法，在单峰函数上寻找最大值点

**解题思路**：
- 实现三分枚举函数，给定搜索区间和精度要求
- 迭代缩小搜索区间，直到满足精度要求
- 返回极值点的近似位置

**代码实现**：

**Java代码**：
```java
public class TernarySearch {
    // 定义精度常量
    private static final double EPS = 1e-10;
    
    // 单峰函数示例：f(x) = -(x-2)^2 + 5，在x=2处取得最大值5
    private static double func(double x) {
        return -(x - 2) * (x - 2) + 5;
    }
    
    // 三分枚举寻找单峰函数的最大值点
    public static double ternarySearch(double left, double right) {
        // 当区间足够小时停止迭代
        while (right - left > EPS) {
            // 计算两个中间点
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            // 比较两个中间点的函数值
            double f1 = func(m1);
            double f2 = func(m2);
            
            // 根据单峰函数的性质缩小搜索区间
            if (f1 < f2) {
                // 最大值在[m1, right]区间
                left = m1;
            } else {
                // 最大值在[left, m2]区间
                right = m2;
            }
        }
        
        // 返回极值点的近似位置
        return (left + right) / 2;
    }
    
    public static void main(String[] args) {
        double left = -10.0;
        double right = 10.0;
        double maxPoint = ternarySearch(left, right);
        double maxValue = func(maxPoint);
        
        System.out.println("最大值点: " + maxPoint);
        System.out.println("最大值: " + maxValue);
        System.out.println("实际最大值点: 2.0");
        System.out.println("实际最大值: 5.0");
    }
}
```

**Python代码**：
```python
import math

# 定义精度常量
EPS = 1e-10

# 单峰函数示例：f(x) = -(x-2)^2 + 5，在x=2处取得最大值5
def func(x):
    return -(x - 2) ** 2 + 5

# 三分枚举寻找单峰函数的最大值点
def ternary_search(left, right):
    # 当区间足够小时停止迭代
    while right - left > EPS:
        # 计算两个中间点
        m1 = left + (right - left) / 3
        m2 = right - (right - left) / 3
        
        # 比较两个中间点的函数值
        f1 = func(m1)
        f2 = func(m2)
        
        # 根据单峰函数的性质缩小搜索区间
        if f1 < f2:
            # 最大值在[m1, right]区间
            left = m1
        else:
            # 最大值在[left, m2]区间
            right = m2
    
    # 返回极值点的近似位置
    return (left + right) / 2

# 测试代码
if __name__ == "__main__":
    left = -10.0
    right = 10.0
    max_point = ternary_search(left, right)
    max_value = func(max_point)
    
    print(f"最大值点: {max_point}")
    print(f"最大值: {max_value}")
    print("实际最大值点: 2.0")
    print("实际最大值: 5.0")
```

**C++代码**：
```cpp
#include <iostream>
#include <cmath>
using namespace std;

// 定义精度常量
const double EPS = 1e-10;

// 单峰函数示例：f(x) = -(x-2)^2 + 5，在x=2处取得最大值5
double func(double x) {
    return -(x - 2) * (x - 2) + 5;
}

// 三分枚举寻找单峰函数的最大值点
double ternarySearch(double left, double right) {
    // 当区间足够小时停止迭代
    while (right - left > EPS) {
        // 计算两个中间点
        double m1 = left + (right - left) / 3;
        double m2 = right - (right - left) / 3;
        
        // 比较两个中间点的函数值
        double f1 = func(m1);
        double f2 = func(m2);
        
        // 根据单峰函数的性质缩小搜索区间
        if (f1 < f2) {
            // 最大值在[m1, right]区间
            left = m1;
        } else {
            // 最大值在[left, m2]区间
            right = m2;
        }
    }
    
    // 返回极值点的近似位置
    return (left + right) / 2;
}

int main() {
    double left = -10.0;
    double right = 10.0;
    double maxPoint = ternarySearch(left, right);
    double maxValue = func(maxPoint);
    
    cout.precision(15);
    cout << "最大值点: " << maxPoint << endl;
    cout << "最大值: " << maxValue << endl;
    cout << "实际最大值点: 2.0" << endl;
    cout << "实际最大值: 5.0" << endl;
    
    return 0;
}
```

### 2. 三分枚举求单谷函数的最小值

**题目来源**：算法基础题

**题目描述**：实现三分枚举算法，在单谷函数（先递减后递增）上寻找最小值点

**解题思路**：
- 对于单谷函数，三分枚举的逻辑略有不同
- 比较函数在m1和m2处的值，根据单谷函数的性质缩小搜索区间

**代码实现**：

**Java代码**：
```java
public class TernarySearchMin {
    // 定义精度常量
    private static final double EPS = 1e-10;
    
    // 单谷函数示例：f(x) = (x-3)^2 + 2，在x=3处取得最小值2
    private static double func(double x) {
        return (x - 3) * (x - 3) + 2;
    }
    
    // 三分枚举寻找单谷函数的最小值点
    public static double ternarySearchMin(double left, double right) {
        // 当区间足够小时停止迭代
        while (right - left > EPS) {
            // 计算两个中间点
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            // 比较两个中间点的函数值
            double f1 = func(m1);
            double f2 = func(m2);
            
            // 根据单谷函数的性质缩小搜索区间
            if (f1 < f2) {
                // 最小值在[left, m2]区间
                right = m2;
            } else {
                // 最小值在[m1, right]区间
                left = m1;
            }
        }
        
        // 返回极值点的近似位置
        return (left + right) / 2;
    }
    
    public static void main(String[] args) {
        double left = -10.0;
        double right = 10.0;
        double minPoint = ternarySearchMin(left, right);
        double minValue = func(minPoint);
        
        System.out.println("最小值点: " + minPoint);
        System.out.println("最小值: " + minValue);
        System.out.println("实际最小值点: 3.0");
        System.out.println("实际最小值: 2.0");
    }
}
```

**Python代码**：
```python
import math

# 定义精度常量
EPS = 1e-10

# 单谷函数示例：f(x) = (x-3)^2 + 2，在x=3处取得最小值2
def func(x):
    return (x - 3) ** 2 + 2

# 三分枚举寻找单谷函数的最小值点
def ternary_search_min(left, right):
    # 当区间足够小时停止迭代
    while right - left > EPS:
        # 计算两个中间点
        m1 = left + (right - left) / 3
        m2 = right - (right - left) / 3
        
        # 比较两个中间点的函数值
        f1 = func(m1)
        f2 = func(m2)
        
        # 根据单谷函数的性质缩小搜索区间
        if f1 < f2:
            # 最小值在[left, m2]区间
            right = m2
        else:
            # 最小值在[m1, right]区间
            left = m1
    
    # 返回极值点的近似位置
    return (left + right) / 2

# 测试代码
if __name__ == "__main__":
    left = -10.0
    right = 10.0
    min_point = ternary_search_min(left, right)
    min_value = func(min_point)
    
    print(f"最小值点: {min_point}")
    print(f"最小值: {min_value}")
    print("实际最小值点: 3.0")
    print("实际最小值: 2.0")
```

**C++代码**：
```cpp
#include <iostream>
#include <cmath>
using namespace std;

// 定义精度常量
const double EPS = 1e-10;

// 单谷函数示例：f(x) = (x-3)^2 + 2，在x=3处取得最小值2
double func(double x) {
    return (x - 3) * (x - 3) + 2;
}

// 三分枚举寻找单谷函数的最小值点
double ternarySearchMin(double left, double right) {
    // 当区间足够小时停止迭代
    while (right - left > EPS) {
        // 计算两个中间点
        double m1 = left + (right - left) / 3;
        double m2 = right - (right - left) / 3;
        
        // 比较两个中间点的函数值
        double f1 = func(m1);
        double f2 = func(m2);
        
        // 根据单谷函数的性质缩小搜索区间
        if (f1 < f2) {
            // 最小值在[left, m2]区间
            right = m2;
        } else {
            // 最小值在[m1, right]区间
            left = m1;
        }
    }
    
    // 返回极值点的近似位置
    return (left + right) / 2;
}

int main() {
    double left = -10.0;
    double right = 10.0;
    double minPoint = ternarySearchMin(left, right);
    double minValue = func(minPoint);
    
    cout.precision(15);
    cout << "最小值点: " << minPoint << endl;
    cout << "最小值: " << minValue << endl;
    cout << "实际最小值点: 3.0" << endl;
    cout << "实际最小值: 2.0" << endl;
    
    return 0;
}
```

## 经典算法题目

### 3. LeetCode 1011. 在 D 天内送达包裹的能力

**题目链接**：https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/

**题目描述**：传送带上的包裹必须在 D 天内从一个港口运送到另一个港口。传送带上的第 i 个包裹的重量为 weights[i]。每一天，我们都会按给出重量的顺序往传送带上装载包裹，我们装载的重量不会超过船的最大运载重量。返回能在 D 天内将所有包裹送达的船的最小运载能力。

**解题思路**：
- 这是一个典型的二分查找问题，但也可以用三分查找解决
- 定义函数：给定运载能力 x，计算需要多少天才能运完所有包裹
- 这个函数是单调递增的，可以用三分查找找到最小的 x 使得所需天数 <= D

**代码实现**：

**Java代码**：
```java
class Solution {
    public int shipWithinDays(int[] weights, int days) {
        // 确定二分查找的上下界
        int left = 0;  // 下界：单个包裹的最大重量
        int right = 0; // 上界：所有包裹的总重量
        for (int weight : weights) {
            left = Math.max(left, weight);
            right += weight;
        }
        
        // 二分查找
        while (left < right) {
            int mid = left + (right - left) / 2;
            int need = calculateDays(weights, mid);
            if (need <= days) {
                // 可以在days天内运完，尝试更小的运载能力
                right = mid;
            } else {
                // 需要更多天，必须增大运载能力
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 计算给定运载能力下需要多少天运完所有包裹
    private int calculateDays(int[] weights, int capacity) {
        int days = 1;
        int currentLoad = 0;
        
        for (int weight : weights) {
            if (currentLoad + weight > capacity) {
                // 需要新的一天
                days++;
                currentLoad = weight;
            } else {
                currentLoad += weight;
            }
        }
        
        return days;
    }
}
```

**Python代码**：
```python
class Solution:
    def shipWithinDays(self, weights: List[int], days: int) -> int:
        # 确定二分查找的上下界
        left = max(weights)  # 下界：单个包裹的最大重量
        right = sum(weights)  # 上界：所有包裹的总重量
        
        # 二分查找
        while left < right:
            mid = left + (right - left) // 2
            need = self.calculate_days(weights, mid)
            if need <= days:
                # 可以在days天内运完，尝试更小的运载能力
                right = mid
            else:
                # 需要更多天，必须增大运载能力
                left = mid + 1
        
        return left
    
    def calculate_days(self, weights: List[int], capacity: int) -> int:
        """计算给定运载能力下需要多少天运完所有包裹"""
        days = 1
        current_load = 0
        
        for weight in weights:
            if current_load + weight > capacity:
                # 需要新的一天
                days += 1
                current_load = weight
            else:
                current_load += weight
        
        return days
```

**C++代码**：
```cpp
class Solution {
private:
    // 计算给定运载能力下需要多少天运完所有包裹
    int calculateDays(const vector<int>& weights, int capacity) {
        int days = 1;
        int currentLoad = 0;
        
        for (int weight : weights) {
            if (currentLoad + weight > capacity) {
                // 需要新的一天
                days++;
                currentLoad = weight;
            } else {
                currentLoad += weight;
            }
        }
        
        return days;
    }
    
public:
    int shipWithinDays(vector<int>& weights, int days) {
        // 确定二分查找的上下界
        int left = 0;  // 下界：单个包裹的最大重量
        int right = 0; // 上界：所有包裹的总重量
        for (int weight : weights) {
            left = max(left, weight);
            right += weight;
        }
        
        // 二分查找
        while (left < right) {
            int mid = left + (right - left) / 2;
            int need = calculateDays(weights, mid);
            if (need <= days) {
                // 可以在days天内运完，尝试更小的运载能力
                right = mid;
            } else {
                // 需要更多天，必须增大运载能力
                left = mid + 1;
            }
        }
        
        return left;
    }
};
```

### 4. LeetCode 410. 分割数组的最大值

**题目链接**：https://leetcode.com/problems/split-array-largest-sum/

**题目描述**：给定一个非负整数数组 nums 和一个整数 k，你需要将这个数组分成 k 个非空的连续子数组。设计一个算法使得这 k 个子数组各自和的最大值最小。

**解题思路**：
- 这是一个典型的可以用二分查找或三分查找解决的优化问题
- 定义函数：给定一个最大和的上限 x，判断是否可以将数组分成至多 k 个部分
- 这个函数具有单调性，可以用三分查找找到最小的 x

**代码实现**：

**Java代码**：
```java
class Solution {
    public int splitArray(int[] nums, int k) {
        // 确定二分查找的上下界
        int left = 0;  // 下界：数组中的最大值
        int right = 0; // 上界：数组的总和
        for (int num : nums) {
            left = Math.max(left, num);
            right += num;
        }
        
        // 二分查找
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canSplit(nums, k, mid)) {
                // 可以分成k个部分，尝试更小的最大值
                right = mid;
            } else {
                // 不能分成k个部分，需要更大的最大值
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    // 判断是否可以将数组分成至多k个部分，使得每个部分的和不超过maxSum
    private boolean canSplit(int[] nums, int k, int maxSum) {
        int count = 1;
        int currentSum = 0;
        
        for (int num : nums) {
            if (currentSum + num > maxSum) {
                // 需要新的一部分
                count++;
                currentSum = num;
                if (count > k) {
                    return false;
                }
            } else {
                currentSum += num;
            }
        }
        
        return true;
    }
}
```

**Python代码**：
```python
class Solution:
    def splitArray(self, nums: List[int], k: int) -> int:
        # 确定二分查找的上下界
        left = max(nums)  # 下界：数组中的最大值
        right = sum(nums)  # 上界：数组的总和
        
        # 二分查找
        while left < right:
            mid = left + (right - left) // 2
            if self.can_split(nums, k, mid):
                # 可以分成k个部分，尝试更小的最大值
                right = mid
            else:
                # 不能分成k个部分，需要更大的最大值
                left = mid + 1
        
        return left
    
    def can_split(self, nums: List[int], k: int, max_sum: int) -> bool:
        """判断是否可以将数组分成至多k个部分，使得每个部分的和不超过max_sum"""
        count = 1
        current_sum = 0
        
        for num in nums:
            if current_sum + num > max_sum:
                # 需要新的一部分
                count += 1
                current_sum = num
                if count > k:
                    return False
            else:
                current_sum += num
        
        return True
```

**C++代码**：
```cpp
class Solution {
private:
    // 判断是否可以将数组分成至多k个部分，使得每个部分的和不超过maxSum
    bool canSplit(const vector<int>& nums, int k, int maxSum) {
        int count = 1;
        int currentSum = 0;
        
        for (int num : nums) {
            if (currentSum + num > maxSum) {
                // 需要新的一部分
                count++;
                currentSum = num;
                if (count > k) {
                    return false;
                }
            } else {
                currentSum += num;
            }
        }
        
        return true;
    }
    
public:
    int splitArray(vector<int>& nums, int k) {
        // 确定二分查找的上下界
        int left = 0;  // 下界：数组中的最大值
        int right = 0; // 上界：数组的总和
        for (int num : nums) {
            left = max(left, num);
            right += num;
        }
        
        // 二分查找
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canSplit(nums, k, mid)) {
                // 可以分成k个部分，尝试更小的最大值
                right = mid;
            } else {
                // 不能分成k个部分，需要更大的最大值
                left = mid + 1;
            }
        }
        
        return left;
    }
};
```

### 5. 寻找函数的峰值

**题目来源**：算法进阶题

**题目描述**：给定一个单峰函数，找到其峰值点（最大值点）

**解题思路**：
- 使用三分枚举算法寻找单峰函数的峰值
- 迭代缩小搜索区间，直到满足精度要求

**代码实现**：

**Java代码**：
```java
public class FindPeak {
    // 定义精度常量
    private static final double EPS = 1e-10;
    
    // 示例单峰函数
    private static double func(double x) {
        // 示例：f(x) = sin(x) + 0.5*x 在某个区间内是单峰函数
        return Math.sin(x) + 0.5 * x;
    }
    
    // 三分枚举寻找单峰函数的峰值
    public static double findPeak(double left, double right) {
        // 当区间足够小时停止迭代
        while (right - left > EPS) {
            // 计算两个中间点
            double m1 = left + (right - left) / 3;
            double m2 = right - (right - left) / 3;
            
            // 比较两个中间点的函数值
            double f1 = func(m1);
            double f2 = func(m2);
            
            // 根据单峰函数的性质缩小搜索区间
            if (f1 < f2) {
                // 峰值在[m1, right]区间
                left = m1;
            } else {
                // 峰值在[left, m2]区间
                right = m2;
            }
        }
        
        // 返回峰值点的近似位置
        return (left + right) / 2;
    }
    
    public static void main(String[] args) {
        double left = 0.0;
        double right = 10.0;
        double peak = findPeak(left, right);
        double peakValue = func(peak);
        
        System.out.println("峰值点: " + peak);
        System.out.println("峰值: " + peakValue);
    }
}
```

**Python代码**：
```python
import math

# 定义精度常量
EPS = 1e-10

# 示例单峰函数
def func(x):
    # 示例：f(x) = sin(x) + 0.5*x 在某个区间内是单峰函数
    return math.sin(x) + 0.5 * x

# 三分枚举寻找单峰函数的峰值
def find_peak(left, right):
    # 当区间足够小时停止迭代
    while right - left > EPS:
        # 计算两个中间点
        m1 = left + (right - left) / 3
        m2 = right - (right - left) / 3
        
        # 比较两个中间点的函数值
        f1 = func(m1)
        f2 = func(m2)
        
        # 根据单峰函数的性质缩小搜索区间
        if f1 < f2:
            # 峰值在[m1, right]区间
            left = m1
        else:
            # 峰值在[left, m2]区间
            right = m2
    
    # 返回峰值点的近似位置
    return (left + right) / 2

# 测试代码
if __name__ == "__main__":
    left = 0.0
    right = 10.0
    peak = find_peak(left, right)
    peak_value = func(peak)
    
    print(f"峰值点: {peak}")
    print(f"峰值: {peak_value}")
```

**C++代码**：
```cpp
#include <iostream>
#include <cmath>
using namespace std;

// 定义精度常量
const double EPS = 1e-10;

// 示例单峰函数
double func(double x) {
    // 示例：f(x) = sin(x) + 0.5*x 在某个区间内是单峰函数
    return sin(x) + 0.5 * x;
}

// 三分枚举寻找单峰函数的峰值
double findPeak(double left, double right) {
    // 当区间足够小时停止迭代
    while (right - left > EPS) {
        // 计算两个中间点
        double m1 = left + (right - left) / 3;
        double m2 = right - (right - left) / 3;
        
        // 比较两个中间点的函数值
        double f1 = func(m1);
        double f2 = func(m2);
        
        // 根据单峰函数的性质缩小搜索区间
        if (f1 < f2) {
            // 峰值在[m1, right]区间
            left = m1;
        } else {
            // 峰值在[left, m2]区间
            right = m2;
        }
    }
    
    // 返回峰值点的近似位置
    return (left + right) / 2;
}

int main() {
    double left = 0.0;
    double right = 10.0;
    double peak = findPeak(left, right);
    double peakValue = func(peak);
    
    cout.precision(15);
    cout << "峰值点: " << peak << endl;
    cout << "峰值: " << peakValue << endl;
    
    return 0;
}
```

### 6. 二维三分枚举

**题目来源**：算法进阶题

**题目描述**：在二维平面上寻找一个单峰函数的极值点

**解题思路**：
- 对于二维单峰函数，可以使用嵌套的三分枚举
- 先固定一个变量，对另一个变量进行三分枚举
- 然后再对固定的变量进行三分枚举
- 交替进行直到收敛

**代码实现**：

**Java代码**：
```java
public class TwoDTernarySearch {
    // 定义精度常量
    private static final double EPS = 1e-10;
    
    // 二维单峰函数示例：f(x,y) = -(x-1)^2 - (y-2)^2 + 5，在(1,2)处取得最大值5
    private static double func(double x, double y) {
        return -(x - 1) * (x - 1) - (y - 2) * (y - 2) + 5;
    }
    
    // 固定x，对y进行三分枚举
    private static double ternarySearchY(double x, double yLeft, double yRight) {
        while (yRight - yLeft > EPS) {
            double m1 = yLeft + (yRight - yLeft) / 3;
            double m2 = yRight - (yRight - yLeft) / 3;
            double f1 = func(x, m1);
            double f2 = func(x, m2);
            
            if (f1 < f2) {
                yLeft = m1;
            } else {
                yRight = m2;
            }
        }
        return (yLeft + yRight) / 2;
    }
    
    // 二维三分枚举寻找最大值
    public static double[] twoDTernarySearch(double xLeft, double xRight, double yLeft, double yRight) {
        while (xRight - xLeft > EPS) {
            double m1 = xLeft + (xRight - xLeft) / 3;
            double m2 = xRight - (xRight - xLeft) / 3;
            
            // 对每个x值，找到最优的y值
            double bestY1 = ternarySearchY(m1, yLeft, yRight);
            double bestY2 = ternarySearchY(m2, yLeft, yRight);
            
            // 比较两个点的函数值
            double f1 = func(m1, bestY1);
            double f2 = func(m2, bestY2);
            
            if (f1 < f2) {
                xLeft = m1;
            } else {
                xRight = m2;
            }
        }
        
        // 找到最终的x对应的最优y
        double bestX = (xLeft + xRight) / 2;
        double bestY = ternarySearchY(bestX, yLeft, yRight);
        
        return new double[] {bestX, bestY, func(bestX, bestY)};
    }
    
    public static void main(String[] args) {
        double xLeft = -10.0, xRight = 10.0;
        double yLeft = -10.0, yRight = 10.0;
        
        double[] result = twoDTernarySearch(xLeft, xRight, yLeft, yRight);
        
        System.out.println("最大值点: (" + result[0] + ", " + result[1] + ")");
        System.out.println("最大值: " + result[2]);
        System.out.println("实际最大值点: (1.0, 2.0)");
        System.out.println("实际最大值: 5.0");
    }
}
```

**Python代码**：
```python
import math

# 定义精度常量
EPS = 1e-10

# 二维单峰函数示例：f(x,y) = -(x-1)^2 - (y-2)^2 + 5，在(1,2)处取得最大值5
def func(x, y):
    return -(x - 1) ** 2 - (y - 2) ** 2 + 5

# 固定x，对y进行三分枚举
def ternary_search_y(x, y_left, y_right):
    while y_right - y_left > EPS:
        m1 = y_left + (y_right - y_left) / 3
        m2 = y_right - (y_right - y_left) / 3
        f1 = func(x, m1)
        f2 = func(x, m2)
        
        if f1 < f2:
            y_left = m1
        else:
            y_right = m2
    return (y_left + y_right) / 2

# 二维三分枚举寻找最大值
def two_d_ternary_search(x_left, x_right, y_left, y_right):
    while x_right - x_left > EPS:
        m1 = x_left + (x_right - x_left) / 3
        m2 = x_right - (x_right - x_left) / 3
        
        # 对每个x值，找到最优的y值
        best_y1 = ternary_search_y(m1, y_left, y_right)
        best_y2 = ternary_search_y(m2, y_left, y_right)
        
        # 比较两个点的函数值
        f1 = func(m1, best_y1)
        f2 = func(m2, best_y2)
        
        if f1 < f2:
            x_left = m1
        else:
            x_right = m2
    
    # 找到最终的x对应的最优y
    best_x = (x_left + x_right) / 2
    best_y = ternary_search_y(best_x, y_left, y_right)
    
    return best_x, best_y, func(best_x, best_y)

# 测试代码
if __name__ == "__main__":
    x_left, x_right = -10.0, 10.0
    y_left, y_right = -10.0, 10.0
    
    best_x, best_y, max_value = two_d_ternary_search(x_left, x_right, y_left, y_right)
    
    print(f"最大值点: ({best_x}, {best_y})")
    print(f"最大值: {max_value}")
    print("实际最大值点: (1.0, 2.0)")
    print("实际最大值: 5.0")
```

**C++代码**：
```cpp
#include <iostream>
#include <cmath>
#include <vector>
using namespace std;

// 定义精度常量
const double EPS = 1e-10;

// 二维单峰函数示例：f(x,y) = -(x-1)^2 - (y-2)^2 + 5，在(1,2)处取得最大值5
double func(double x, double y) {
    return -(x - 1) * (x - 1) - (y - 2) * (y - 2) + 5;
}

// 固定x，对y进行三分枚举
double ternarySearchY(double x, double yLeft, double yRight) {
    while (yRight - yLeft > EPS) {
        double m1 = yLeft + (yRight - yLeft) / 3;
        double m2 = yRight - (yRight - yLeft) / 3;
        double f1 = func(x, m1);
        double f2 = func(x, m2);
        
        if (f1 < f2) {
            yLeft = m1;
        } else {
            yRight = m2;
        }
    }
    return (yLeft + yRight) / 2;
}

// 二维三分枚举寻找最大值
vector<double> twoDTernarySearch(double xLeft, double xRight, double yLeft, double yRight) {
    while (xRight - xLeft > EPS) {
        double m1 = xLeft + (xRight - xLeft) / 3;
        double m2 = xRight - (xRight - xLeft) / 3;
        
        // 对每个x值，找到最优的y值
        double bestY1 = ternarySearchY(m1, yLeft, yRight);
        double bestY2 = ternarySearchY(m2, yLeft, yRight);
        
        // 比较两个点的函数值
        double f1 = func(m1, bestY1);
        double f2 = func(m2, bestY2);
        
        if (f1 < f2) {
            xLeft = m1;
        } else {
            xRight = m2;
        }
    }
    
    // 找到最终的x对应的最优y
    double bestX = (xLeft + xRight) / 2;
    double bestY = ternarySearchY(bestX, yLeft, yRight);
    
    return {bestX, bestY, func(bestX, bestY)};
}

int main() {
    double xLeft = -10.0, xRight = 10.0;
    double yLeft = -10.0, yRight = 10.0;
    
    vector<double> result = twoDTernarySearch(xLeft, xRight, yLeft, yRight);
    
    cout.precision(15);
    cout << "最大值点: (" << result[0] << ", " << result[1] << ")" << endl;
    cout << "最大值: " << result[2] << endl;
    cout << "实际最大值点: (1.0, 2.0)" << endl;
    cout << "实际最大值: 5.0" << endl;
    
    return 0;
}
```

## 三分枚举与二分查找的比较

| 特性 | 三分枚举 | 二分查找 |
|------|---------|----------|
| 适用函数类型 | 单峰/单谷函数 | 单调函数 |
| 搜索目的 | 寻找极值点 | 寻找特定值或阈值 |
| 时间复杂度 | O(log n) | O(log n) |
| 每次迭代操作 | 比较两个中间点的函数值 | 比较中间点的函数值与目标值 |
| 区间缩小方式 | 每次缩小为原来的2/3 | 每次缩小为原来的1/2 |

## 三分枚举的注意事项

1. **函数必须是单峰/单谷的**：三分枚举只能应用于单峰或单谷函数，如果函数有多个极值点，结果可能不准确
2. **精度设置**：需要根据问题的要求设置合适的精度阈值
3. **初始搜索区间**：需要确定合适的初始搜索区间，确保极值点在区间内
4. **浮点误差**：由于使用浮点数，需要注意精度问题
5. **收敛速度**：三分枚举的收敛速度略慢于二分查找，但对于非单调函数的极值查找，是一种有效的方法

## 总结

三分枚举是一种用于在单峰或单谷函数上寻找极值的有效算法。它通过不断缩小搜索区间，逐步逼近极值点。虽然在某些情况下可以用二分查找替代，但三分枚举在处理非单调函数的极值问题时具有独特的优势。

在实际应用中，我们需要注意函数的单峰/单谷性质、精度设置、初始搜索区间等因素，以确保算法的正确性和效率。","}}}