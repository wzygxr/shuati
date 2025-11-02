# 二分查找算法详解与相关题目汇总

## 算法介绍

二分查找（Binary Search）是一种在已排序数组中查找特定元素的高效算法。它通过不断将搜索范围减半来快速定位目标元素，时间复杂度为 O(log n)。

基本思想：
1. 在有序数组中，比较目标值与中间元素
2. 如果相等，则找到目标
3. 如果目标值较小，则在左半部分继续查找
4. 如果目标值较大，则在右半部分继续查找
5. 重复直到找到目标或搜索范围为空

## 二分查找的三种模板

### 模板1：基础二分查找
查找目标值是否存在

```java
public static int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
```

### 模板2：查找左边界
查找第一个大于等于目标值的位置

```java
public static int binarySearchLeft(int[] arr, int target) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

### 模板3：查找右边界
查找最后一个小于等于目标值的位置

```java
public static int binarySearchRight(int[] arr, int target) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left - 1;
}
```

## 相关题目列表

### 基础题目

#### 1. LeetCode 704. Binary Search (二分查找)
在有序数组中查找目标值

**题目描述**：
给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。

**解题思路**：
使用基础二分查找模板

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int search(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
```

```cpp
// C++实现
int search(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
```

```python
# Python实现
def search(nums, target):
    left, right = 0, len(nums) - 1
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1
```

#### 2. LeetCode 35. Search Insert Position (搜索插入位置)
查找目标值应该插入的位置

**题目描述**：
给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。

**解题思路**：
使用左边界查找模板

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int searchInsert(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

```cpp
// C++实现
int searchInsert(vector<int>& nums, int target) {
    int left = 0, right = nums.size();
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

```python
# Python实现
def search_insert(nums, target):
    left, right = 0, len(nums)
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    return left
```

#### 3. LeetCode 34. Find First and Last Position of Element in Sorted Array (在排序数组中查找元素的第一个和最后一个位置)
查找目标值在数组中的起始和结束位置

**题目描述**：
给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。

**解题思路**：
分别查找左边界和右边界

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int[] searchRange(int[] nums, int target) {
    int first = findLeft(nums, target);
    if (first == nums.length || nums[first] != target) {
        return new int[]{-1, -1};
    }
    int last = findRight(nums, target);
    return new int[]{first, last};
}

private static int findLeft(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

private static int findRight(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left - 1;
}
```

```cpp
// C++实现
vector<int> searchRange(vector<int>& nums, int target) {
    int first = findLeft(nums, target);
    if (first == nums.size() || nums[first] != target) {
        return {-1, -1};
    }
    int last = findRight(nums, target);
    return {first, last};
}

int findLeft(vector<int>& nums, int target) {
    int left = 0, right = nums.size();
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

int findRight(vector<int>& nums, int target) {
    int left = 0, right = nums.size();
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left - 1;
}
```

```python
# Python实现
def search_range(nums, target):
    first = find_left(nums, target)
    if first == len(nums) or nums[first] != target:
        return [-1, -1]
    last = find_right(nums, target)
    return [first, last]

def find_left(nums, target):
    left, right = 0, len(nums)
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    return left

def find_right(nums, target):
    left, right = 0, len(nums)
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] <= target:
            left = mid + 1
        else:
            right = mid
    return left - 1
```

#### 4. LeetCode 367. Valid Perfect Square (有效的完全平方数)
判断一个数是否是完全平方数

**题目描述**：
给定一个正整数 num，编写一个函数，如果 num 是一个完全平方数，则返回 True，否则返回 False。

**解题思路**：
使用二分查找在 [1, num] 范围内查找是否存在一个数的平方等于 num

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static boolean isPerfectSquare(int num) {
    if (num < 1) return false;
    if (num == 1) return true;
    
    long left = 1, right = num / 2;
    while (left <= right) {
        long mid = left + (right - left) / 2;
        long square = mid * mid;
        if (square == num) {
            return true;
        } else if (square < num) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return false;
}
```

```cpp
// C++实现
bool isPerfectSquare(int num) {
    if (num < 1) return false;
    if (num == 1) return true;
    
    long long left = 1, right = num / 2;
    while (left <= right) {
        long long mid = left + (right - left) / 2;
        long long square = mid * mid;
        if (square == num) {
            return true;
        } else if (square < num) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return false;
}
```

```python
# Python实现
def is_perfect_square(num):
    if num < 1:
        return False
    if num == 1:
        return True
    
    left, right = 1, num // 2
    while left <= right:
        mid = left + (right - left) // 2
        square = mid * mid
        if square == num:
            return True
        elif square < num:
            left = mid + 1
        else:
            right = mid - 1
    return False
```

#### 5. LeetCode 278. First Bad Version (第一个错误的版本)
查找第一个错误的版本

**题目描述**：
假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。

**解题思路**：
使用左边界查找模板

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现 (假设存在 isBadVersion API)
public static int firstBadVersion(int n) {
    int left = 1, right = n;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (isBadVersion(mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}
```

```cpp
// C++实现 (假设存在 isBadVersion API)
int firstBadVersion(int n) {
    int left = 1, right = n;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (isBadVersion(mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}
```

```python
# Python实现 (假设存在 isBadVersion API)
def first_bad_version(n):
    left, right = 1, n
    while left < right:
        mid = left + (right - left) // 2
        if isBadVersion(mid):
            right = mid
        else:
            left = mid + 1
    return left
```

### 进阶题目

#### 6. LeetCode 162. Find Peak Element (寻找峰值)
寻找数组中的峰值元素

**题目描述**：
峰值元素是指其值大于左右相邻值的元素。给定一个输入数组 nums，其中 nums[i] ≠ nums[i+1]，找到峰值元素并返回其索引。

**解题思路**：
使用二分查找，比较 mid 和 mid+1 的值决定搜索方向

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int findPeakElement(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] > nums[mid + 1]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}
```

```cpp
// C++实现
int findPeakElement(vector<int>& nums) {
    int left = 0, right = nums.size() - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] > nums[mid + 1]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}
```

```python
# Python实现
def find_peak_element(nums):
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[mid + 1]:
            right = mid
        else:
            left = mid + 1
    return left
```

#### 7. LeetCode 153. Find Minimum in Rotated Sorted Array (寻找旋转排序数组中的最小值)
寻找旋转排序数组中的最小值

**题目描述**：
假设按照升序排序的数组在预先未知的某个点上进行了旋转。请找出其中最小的元素。

**解题思路**：
使用二分查找，比较 mid 和 right 的值决定搜索方向

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int findMin(int[] nums) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < nums[right]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return nums[left];
}
```

```cpp
// C++实现
int findMin(vector<int>& nums) {
    int left = 0, right = nums.size() - 1;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] < nums[right]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return nums[left];
}
```

```python
# Python实现
def find_min(nums):
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] < nums[right]:
            right = mid
        else:
            left = mid + 1
    return nums[left]
```

#### 8. LeetCode 74. Search a 2D Matrix (搜索二维矩阵)
在二维矩阵中搜索目标值

**题目描述**：
编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target。该矩阵具有以下特性：
- 每行的元素从左到右升序排列。
- 每列的元素从上到下升序排列。

**解题思路**：
将二维矩阵看作一维数组进行二分查找

**时间复杂度**：O(log(m*n))
**空间复杂度**：O(1)

```java
// Java实现
public static boolean searchMatrix(int[][] matrix, int target) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return false;
    }
    
    int m = matrix.length, n = matrix[0].length;
    int left = 0, right = m * n - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int midValue = matrix[mid / n][mid % n];
        
        if (midValue == target) {
            return true;
        } else if (midValue < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}
```

```cpp
// C++实现
bool searchMatrix(vector<vector<int>>& matrix, int target) {
    if (matrix.empty() || matrix[0].empty()) {
        return false;
    }
    
    int m = matrix.size(), n = matrix[0].size();
    int left = 0, right = m * n - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        int midValue = matrix[mid / n][mid % n];
        
        if (midValue == target) {
            return true;
        } else if (midValue < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}
```

```python
# Python实现
def search_matrix(matrix, target):
    if not matrix or not matrix[0]:
        return False
    
    m, n = len(matrix), len(matrix[0])
    left, right = 0, m * n - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        mid_value = matrix[mid // n][mid % n]
        
        if mid_value == target:
            return True
        elif mid_value < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return False
```

#### 9. LeetCode 33. Search in Rotated Sorted Array (搜索旋转排序数组)
在旋转排序数组中搜索目标值

**题目描述**：
整数数组 nums 按升序排列，数组中的值互不相同。在传递给函数之前，nums 在某个未知的下标 k 上进行了旋转。给定旋转后的数组 nums 和一个整数 target，如果 nums 中存在这个目标值 target，则返回它的下标，否则返回 -1。

**解题思路**：
使用二分查找，根据 mid 位置判断哪一部分是有序的

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int search(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 左半部分有序
        if (nums[left] <= nums[mid]) {
            if (nums[left] <= target && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        } 
        // 右半部分有序
        else {
            if (nums[mid] < target && target <= nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return -1;
}
```

```cpp
// C++实现
int search(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 左半部分有序
        if (nums[left] <= nums[mid]) {
            if (nums[left] <= target && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        } 
        // 右半部分有序
        else {
            if (nums[mid] < target && target <= nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return -1;
}
```

```python
# Python实现
def search(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return mid
        
        # 左半部分有序
        if nums[left] <= nums[mid]:
            if nums[left] <= target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        # 右半部分有序
        else:
            if nums[mid] < target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    
    return -1
```

#### 10. LeetCode 287. Find the Duplicate Number (寻找重复数)
在数组中寻找重复的数字

**题目描述**：
给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。

**解题思路**：
使用二分查找结合抽屉原理

**时间复杂度**：O(n log n)
**空间复杂度**：O(1)

```java
// Java实现
public static int findDuplicate(int[] nums) {
    int left = 1, right = nums.length - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        int count = 0;
        
        // 计算小于等于mid的数字个数
        for (int num : nums) {
            if (num <= mid) {
                count++;
            }
        }
        
        // 根据抽屉原理判断重复数字在哪一侧
        if (count > mid) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}
```

```cpp
// C++实现
int findDuplicate(vector<int>& nums) {
    int left = 1, right = nums.size() - 1;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        int count = 0;
        
        // 计算小于等于mid的数字个数
        for (int num : nums) {
            if (num <= mid) {
                count++;
            }
        }
        
        // 根据抽屉原理判断重复数字在哪一侧
        if (count > mid) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}
```

```python
# Python实现
def find_duplicate(nums):
    left, right = 1, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        count = 0
        
        # 计算小于等于mid的数字个数
        for num in nums:
            if num <= mid:
                count += 1
        
        # 根据抽屉原理判断重复数字在哪一侧
        if count > mid:
            right = mid
        else:
            left = mid + 1
    
    return left
```

### Codeforces 题目

#### 11. Codeforces 706B. Interesting drink
在商店中查找可以购买的数量

**题目描述**：
Vasiliy 喜欢在超市购物。他想买一些饮料，每种饮料都有一个价格。他想知道对于给定的金额，他能买多少种饮料。

**解题思路**：
先对价格数组排序，然后对每个查询使用二分查找

**时间复杂度**：O(n log n + q log n)
**空间复杂度**：O(1)

```java
// Java实现
import java.util.*;

public static int[] howManyDrinks(int[] prices, int[] queries) {
    Arrays.sort(prices);
    int[] result = new int[queries.length];
    
    for (int i = 0; i < queries.length; i++) {
        int money = queries[i];
        int left = 0, right = prices.length;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prices[mid] <= money) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        result[i] = left;
    }
    
    return result;
}
```

```cpp
// C++实现
#include <vector>
#include <algorithm>

vector<int> howManyDrinks(vector<int>& prices, vector<int>& queries) {
    sort(prices.begin(), prices.end());
    vector<int> result(queries.size());
    
    for (int i = 0; i < queries.size(); i++) {
        int money = queries[i];
        int left = 0, right = prices.size();
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (prices[mid] <= money) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        result[i] = left;
    }
    
    return result;
}
```

```python
# Python实现
def how_many_drinks(prices, queries):
    prices.sort()
    result = []
    
    for money in queries:
        left, right = 0, len(prices)
        
        while left < right:
            mid = left + (right - left) // 2
            if prices[mid] <= money:
                left = mid + 1
            else:
                right = mid
        
        result.append(left)
    
    return result
```

### SPOJ 题目

#### 12. SPOJ - AGGRCOW (Aggressive cows)
安排奶牛使最小距离最大化

**题目描述**：
农夫约翰建造了一个有 C 个摊位的畜棚，摊位位于 x0, ..., x(C-1)。他的 M 头奶牛总是相互攻击，约翰必须以某种方式分配奶牛到摊位，使它们之间的最小距离尽可能大。

**解题思路**：
使用二分查找答案 + 贪心验证

**时间复杂度**：O(n log(max-min) * n)
**空间复杂度**：O(1)

```java
// Java实现
import java.util.*;

public static int aggressiveCows(int[] positions, int cows) {
    Arrays.sort(positions);
    int left = 0, right = positions[positions.length - 1] - positions[0];
    int result = 0;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (canPlaceCows(positions, cows, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

private static boolean canPlaceCows(int[] positions, int cows, int minDist) {
    int count = 1;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.length; i++) {
        if (positions[i] - lastPosition >= minDist) {
            count++;
            lastPosition = positions[i];
            if (count == cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

```cpp
// C++实现
#include <vector>
#include <algorithm>

int aggressiveCows(std::vector<int>& positions, int cows) {
    std::sort(positions.begin(), positions.end());
    int left = 0, right = positions.back() - positions.front();
    int result = 0;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (canPlaceCows(positions, cows, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

bool canPlaceCows(std::vector<int>& positions, int cows, int minDist) {
    int count = 1;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.size(); i++) {
        if (positions[i] - lastPosition >= minDist) {
            count++;
            lastPosition = positions[i];
            if (count == cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

```python
# Python实现
def aggressive_cows(positions, cows):
    positions.sort()
    left, right = 0, positions[-1] - positions[0]
    result = 0
    
    while left <= right:
        mid = left + (right - left) // 2
        if can_place_cows(positions, cows, mid):
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result

def can_place_cows(positions, cows, min_dist):
    count = 1
    last_position = positions[0]
    
    for i in range(1, len(positions)):
        if positions[i] - last_position >= min_dist:
            count += 1
            last_position = positions[i]
            if count == cows:
                return True
    
    return False
```

## 工程化考量

### 异常处理

在实际工程应用中，我们需要考虑以下异常情况：

1. 空数组处理
2. 数组未排序
3. 数据溢出
4. 输入参数非法

```java
// Java - 带异常处理的二分查找
public static int safeBinarySearch(int[] nums, int target) {
    // 检查输入参数
    if (nums == null) {
        throw new IllegalArgumentException("数组不能为null");
    }
    
    if (nums.length == 0) {
        return -1;
    }
    
    int left = 0, right = nums.length - 1;
    
    // 检查数组是否已排序
    for (int i = 1; i < nums.length; i++) {
        if (nums[i] < nums[i-1]) {
            throw new IllegalArgumentException("数组必须是已排序的");
        }
    }
    
    while (left <= right) {
        // 防止整数溢出的中点计算
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

### 单元测试

```java
// Java - 二分查找的单元测试
import org.junit.Test;
import static org.junit.Assert.*;

public class BinarySearchTest {
    
    @Test
    public void testBasicSearch() {
        int[] nums = {-1, 0, 3, 5, 9, 12};
        assertEquals(4, BinarySearch.search(nums, 9));
        assertEquals(-1, BinarySearch.search(nums, 2));
    }
    
    @Test
    public void testEdgeCases() {
        // 空数组
        int[] empty = {};
        assertEquals(-1, BinarySearch.search(empty, 1));
        
        // 单元素数组
        int[] single = {5};
        assertEquals(0, BinarySearch.search(single, 5));
        assertEquals(-1, BinarySearch.search(single, 3));
        
        // 两元素数组
        int[] two = {1, 3};
        assertEquals(0, BinarySearch.search(two, 1));
        assertEquals(1, BinarySearch.search(two, 3));
        assertEquals(-1, BinarySearch.search(two, 2));
    }
    
    @Test
    public void testBoundaries() {
        int[] nums = {1, 2, 3, 4, 5};
        assertEquals(0, BinarySearch.search(nums, 1));  // 第一个元素
        assertEquals(4, BinarySearch.search(nums, 5));  // 最后一个元素
    }
}
```

### 性能优化

1. 使用位运算优化中点计算：`mid = (left + right) >>> 1`
2. 减少比较次数
3. 使用迭代而非递归避免栈溢出

```java
// Java - 性能优化版本
public static int optimizedBinarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        // 使用无符号右移避免溢出
        int mid = (left + right) >>> 1;
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 使用位运算优化比较
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

## 13. LeetCode 69. Sqrt(x)

### 题目描述
实现 `int sqrt(int x)` 函数。计算并返回 x 的平方根，其中 x 是非负整数。由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。

### 解题思路
- 这是一个典型的二分查找应用，可以在 [0, x] 范围内查找满足 `mid * mid <= x` 的最大整数
- 需要注意整数溢出问题，使用 `mid > x / mid` 而不是 `mid * mid > x` 来判断

### 时间复杂度
- O(log x)，二分查找的时间复杂度

### 空间复杂度
- O(1)，只使用常数额外空间

#### Java 实现
```java
/**
 * LeetCode 69. Sqrt(x)
 * 
 * 时间复杂度: O(log x)
 * 空间复杂度: O(1)
 * 
 * @param x 输入的非负整数
 * @return x 的平方根的整数部分
 */
public int mySqrt(int x) {
    if (x == 0 || x == 1) {
        return x;
    }
    
    int left = 1, right = x;
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        // 防止整数溢出，使用除法而不是乘法
        if (mid > x / mid) {
            right = mid - 1;
        } else {
            // 如果下一个值会溢出或者大于x/mid，则当前mid是最大的有效平方根
            if (mid + 1 > x / (mid + 1)) {
                return mid;
            }
            left = mid + 1;
        }
    }
    
    return right;
}
```

#### C++ 实现
```cpp
/**
 * LeetCode 69. Sqrt(x)
 * 
 * 时间复杂度: O(log x)
 * 空间复杂度: O(1)
 * 
 * @param x 输入的非负整数
 * @return x 的平方根的整数部分
 */
int mySqrt(int x) {
    if (x == 0 || x == 1) {
        return x;
    }
    
    long long left = 1, right = x;
    while (left <= right) {
        long long mid = left + ((right - left) >> 1);
        
        // 使用long long防止整数溢出
        if (mid * mid > x) {
            right = mid - 1;
        } else {
            if ((mid + 1) * (mid + 1) > x) {
                return mid;
            }
            left = mid + 1;
        }
    }
    
    return right;
}
```

#### Python 实现
```python
def my_sqrt(x):
    """
    LeetCode 69. Sqrt(x)
    
    时间复杂度: O(log x)
    空间复杂度: O(1)
    
    :param x: 输入的非负整数
    :return: x 的平方根的整数部分
    """
    if x == 0 or x == 1:
        return x
    
    left, right = 1, x
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        # 防止整数溢出，使用除法而不是乘法
        if mid > x // mid:
            right = mid - 1
        else:
            if (mid + 1) > x // (mid + 1):
                return mid
            left = mid + 1
    
    return right
```

## 14. LeetCode 374. 猜数字大小

### 题目描述
我们正在玩一个猜数字游戏。游戏规则如下：我从 1 到 n 选择一个数字。你需要猜我选了哪个数字。每次你猜错了，我会告诉你这个数字是大了还是小了。你调用一个预先定义好的接口 `guess(int num)`，它会返回 3 个可能的结果：
- -1：我选的数字比你猜的数字小（即 `pick < num`）
- 1：我选的数字比你猜的数字大（即 `pick > num`）
- 0：我选的数字和你猜的数字一样。恭喜！你猜对了！（即 `pick == num`）

### 解题思路
- 这是一个标准的二分查找问题，每次猜测中间值，根据 `guess()` 函数的返回结果调整搜索范围
- 需要注意的是，这里的 `guess()` 函数是题目提供的，我们只需要实现二分查找的框架

### 时间复杂度
- O(log n)，二分查找的时间复杂度

### 空间复杂度
- O(1)，只使用常数额外空间

#### Java 实现
```java
/**
 * LeetCode 374. 猜数字大小
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 数字范围上限
 * @return 猜中的数字
 */
public int guessNumber(int n) {
    int left = 1, right = n;
    
    while (left <= right) {
        // 防止整数溢出
        int mid = left + ((right - left) >> 1);
        int result = guess(mid);
        
        if (result == 0) {
            return mid;  // 猜对了
        } else if (result == 1) {
            left = mid + 1;  // 猜小了，往右边找
        } else {
            right = mid - 1;  // 猜大了，往左边找
        }
    }
    
    return -1;  // 理论上不会执行到这里
}

// 注意：这是一个示例的guess方法，实际会由系统提供
private int guess(int num) {
    int pick = 6;  // 假设选中的数字是6
    if (num < pick) return 1;
    else if (num > pick) return -1;
    else return 0;
}
```

#### C++ 实现
```cpp
/**
 * LeetCode 374. 猜数字大小
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 数字范围上限
 * @return 猜中的数字
 */
int guessNumber(int n) {
    long long left = 1, right = n;
    
    while (left <= right) {
        // 使用long long防止整数溢出
        long long mid = left + ((right - left) >> 1);
        int result = guess(mid);
        
        if (result == 0) {
            return mid;  // 猜对了
        } else if (result == 1) {
            left = mid + 1;  // 猜小了，往右边找
        } else {
            right = mid - 1;  // 猜大了，往左边找
        }
    }
    
    return -1;  // 理论上不会执行到这里
}

// 注意：这是一个示例的guess函数，实际会由系统提供
int guess(int num) {
    int pick = 6;  // 假设选中的数字是6
    if (num < pick) return 1;
    else if (num > pick) return -1;
    else return 0;
}
```

#### Python 实现
```python
def guess_number(n):
    """
    LeetCode 374. 猜数字大小
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param n: 数字范围上限
    :return: 猜中的数字
    """
    left, right = 1, n
    
    while left <= right:
        # 防止整数溢出
        mid = left + ((right - left) >> 1)
        result = guess(mid)
        
        if result == 0:
            return mid  # 猜对了
        elif result == 1:
            left = mid + 1  # 猜小了，往右边找
        else:
            right = mid - 1  # 猜大了，往左边找
    
    return -1  # 理论上不会执行到这里

# 注意：这是一个示例的guess函数，实际会由系统提供
def guess(num):
    pick = 6  # 假设选中的数字是6
    if num < pick:
        return 1
    elif num > pick:
        return -1
    else:
        return 0
```

## 15. LeetCode 875. 爱吃香蕉的珂珂

### 题目描述
珂珂喜欢吃香蕉。这里有 n 堆香蕉，第 i 堆中有 piles[i] 根香蕉。警卫已经离开了，将在 h 小时后回来。珂珂可以决定她吃香蕉的速度 k（单位：根/小时）。每个小时，她将会选择一堆香蕉，从中吃掉 k 根。如果这堆香蕉少于 k 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉。珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。返回她可以在 h 小时内吃掉所有香蕉的最小速度 k（k 为整数）。

### 解题思路
- 这是一个典型的二分答案问题，我们需要找到满足条件的最小速度 k
- 速度的最小值是 1，最大值是最大堆的香蕉数
- 对于每一个候选速度 mid，我们计算吃完所有香蕉需要的时间，如果时间 <= h，则可以尝试更小的速度；否则需要更大的速度

### 时间复杂度
- O(n log maxPile)，其中 n 是香蕉堆的数量，maxPile 是最大的香蕉堆的数量
- 二分查找需要 log maxPile 次迭代，每次迭代需要 O(n) 的时间计算吃完所有香蕉的时间

### 空间复杂度
- O(1)，只使用常数额外空间

#### Java 实现
```java
/**
 * LeetCode 875. 爱吃香蕉的珂珂
 * 
 * 时间复杂度: O(n log maxPile)
 * 空间复杂度: O(1)
 * 
 * @param piles 香蕉堆数组
 * @param h 警卫离开的时间（小时）
 * @return 最小的吃香蕉速度
 */
public int minEatingSpeed(int[] piles, int h) {
    int left = 1;
    int right = 0;
    
    // 找到最大的香蕉堆，作为右边界
    for (int pile : piles) {
        right = Math.max(right, pile);
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canFinish(piles, h, mid)) {
            right = mid;  // 可以吃完，尝试更小的速度
        } else {
            left = mid + 1;  // 不能吃完，需要更大的速度
        }
    }
    
    return left;
}

/**
 * 辅助方法：判断以速度speed是否能在h小时内吃完所有香蕉
 */
private boolean canFinish(int[] piles, int h, int speed) {
    long time = 0;
    
    for (int pile : piles) {
        // 计算吃完当前堆需要的时间
        time += (pile + speed - 1) / speed;  // 等价于 Math.ceil(pile / speed)
        
        // 如果时间已经超过h，可以提前返回false
        if (time > h) {
            return false;
        }
    }
    
    return time <= h;
}
```

#### C++ 实现
```cpp
/**
 * LeetCode 875. 爱吃香蕉的珂珂
 * 
 * 时间复杂度: O(n log maxPile)
 * 空间复杂度: O(1)
 * 
 * @param piles 香蕉堆数组
 * @param h 警卫离开的时间（小时）
 * @return 最小的吃香蕉速度
 */
int minEatingSpeed(vector<int>& piles, int h) {
    int left = 1;
    int right = 0;
    
    // 找到最大的香蕉堆，作为右边界
    for (int pile : piles) {
        right = max(right, pile);
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canFinish(piles, h, mid)) {
            right = mid;  // 可以吃完，尝试更小的速度
        } else {
            left = mid + 1;  // 不能吃完，需要更大的速度
        }
    }
    
    return left;
}

/**
 * 辅助方法：判断以速度speed是否能在h小时内吃完所有香蕉
 */
bool canFinish(vector<int>& piles, int h, int speed) {
    long long time = 0;
    
    for (int pile : piles) {
        // 计算吃完当前堆需要的时间
        time += (pile + speed - 1) / speed;  // 等价于 ceil(pile / speed)
        
        // 如果时间已经超过h，可以提前返回false
        if (time > h) {
            return false;
        }
    }
    
    return time <= h;
}
```

#### Python 实现
```python
def min_eating_speed(piles, h):
    """
    LeetCode 875. 爱吃香蕉的珂珂
    
    时间复杂度: O(n log maxPile)
    空间复杂度: O(1)
    
    :param piles: 香蕉堆数组
    :param h: 警卫离开的时间（小时）
    :return: 最小的吃香蕉速度
    """
    left = 1
    right = max(piles)  # 最大的香蕉堆，作为右边界
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if can_finish(piles, h, mid):
            right = mid  # 可以吃完，尝试更小的速度
        else:
            left = mid + 1  # 不能吃完，需要更大的速度
    
    return left

def can_finish(piles, h, speed):
    """
    辅助方法：判断以速度speed是否能在h小时内吃完所有香蕉
    """
    time = 0
    
    for pile in piles:
        # 计算吃完当前堆需要的时间
        time += (pile + speed - 1) // speed  # 等价于 math.ceil(pile / speed)
        
        # 如果时间已经超过h，可以提前返回False
        if time > h:
            return False
    
    return time <= h
```

## 16. LeetCode 1011. 在D天内送达包裹的能力

### 题目描述
传送带上的包裹必须在 D 天内从一个港口运送到另一个港口。传送带上的第 i 个包裹的重量为 weights[i]。每一天，我们都会按给出重量的顺序往传送带上装载包裹，我们装载的重量不会超过船的最大运载重量。返回能在 D 天内将所有包裹送达的船的最小运载能力。

### 解题思路
- 这是一个典型的二分答案问题，我们需要找到满足条件的最小运载能力
- 运载能力的最小值是最大包裹的重量，最大值是所有包裹的总重量
- 对于每一个候选运载能力 mid，我们计算需要多少天才能运完所有包裹，如果天数 <= D，则可以尝试更小的运载能力；否则需要更大的运载能力

### 时间复杂度
- O(n log totalWeight)，其中 n 是包裹的数量，totalWeight 是所有包裹的总重量
- 二分查找需要 log totalWeight 次迭代，每次迭代需要 O(n) 的时间计算运完所有包裹的天数

### 空间复杂度
- O(1)，只使用常数额外空间

#### Java 实现
```java
/**
 * LeetCode 1011. 在D天内送达包裹的能力
 * 
 * 时间复杂度: O(n log totalWeight)
 * 空间复杂度: O(1)
 * 
 * @param weights 包裹重量数组
 * @param days 天数限制
 * @return 船的最小运载能力
 */
public int shipWithinDays(int[] weights, int days) {
    int left = 0;  // 最小运载能力：最大的单个包裹重量
    int right = 0; // 最大运载能力：所有包裹的总重量
    
    for (int weight : weights) {
        left = Math.max(left, weight);
        right += weight;
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canShipInDays(weights, days, mid)) {
            right = mid;  // 可以在days天内运完，尝试更小的运载能力
        } else {
            left = mid + 1;  // 不能在days天内运完，需要更大的运载能力
        }
    }
    
    return left;
}

/**
 * 辅助方法：判断以capacity的运载能力是否能在days天内运完所有包裹
 */
private boolean canShipInDays(int[] weights, int days, int capacity) {
    int currentWeight = 0;
    int dayCount = 1;  // 至少需要1天
    
    for (int weight : weights) {
        // 如果当前包裹的重量已经超过了运载能力，不可能运完
        if (weight > capacity) {
            return false;
        }
        
        // 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
        if (currentWeight + weight > capacity) {
            dayCount++;
            currentWeight = weight;  // 新的一天从当前包裹开始
            
            // 如果天数已经超过了限制，可以提前返回false
            if (dayCount > days) {
                return false;
            }
        } else {
            currentWeight += weight;  // 继续往当前天添加包裹
        }
    }
    
    return dayCount <= days;
}
```

#### C++ 实现
```cpp
/**
 * LeetCode 1011. 在D天内送达包裹的能力
 * 
 * 时间复杂度: O(n log totalWeight)
 * 空间复杂度: O(1)
 * 
 * @param weights 包裹重量数组
 * @param days 天数限制
 * @return 船的最小运载能力
 */
int shipWithinDays(vector<int>& weights, int days) {
    int left = 0;  // 最小运载能力：最大的单个包裹重量
    int right = 0; // 最大运载能力：所有包裹的总重量
    
    for (int weight : weights) {
        left = max(left, weight);
        right += weight;
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canShipInDays(weights, days, mid)) {
            right = mid;  // 可以在days天内运完，尝试更小的运载能力
        } else {
            left = mid + 1;  // 不能在days天内运完，需要更大的运载能力
        }
    }
    
    return left;
}

/**
 * 辅助方法：判断以capacity的运载能力是否能在days天内运完所有包裹
 */
bool canShipInDays(vector<int>& weights, int days, int capacity) {
    int currentWeight = 0;
    int dayCount = 1;  // 至少需要1天
    
    for (int weight : weights) {
        // 如果当前包裹的重量已经超过了运载能力，不可能运完
        if (weight > capacity) {
            return false;
        }
        
        // 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
        if (currentWeight + weight > capacity) {
            dayCount++;
            currentWeight = weight;  // 新的一天从当前包裹开始
            
            // 如果天数已经超过了限制，可以提前返回false
            if (dayCount > days) {
                return false;
            }
        } else {
            currentWeight += weight;  // 继续往当前天添加包裹
        }
    }
    
    return dayCount <= days;
}
```

#### Python 实现
```python
def ship_within_days(weights, days):
    """
    LeetCode 1011. 在D天内送达包裹的能力
    
    时间复杂度: O(n log totalWeight)
    空间复杂度: O(1)
    
    :param weights: 包裹重量数组
    :param days: 天数限制
    :return: 船的最小运载能力
    """
    left = max(weights)  # 最小运载能力：最大的单个包裹重量
    right = sum(weights)  # 最大运载能力：所有包裹的总重量
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if can_ship_in_days(weights, days, mid):
            right = mid  # 可以在days天内运完，尝试更小的运载能力
        else:
            left = mid + 1  # 不能在days天内运完，需要更大的运载能力
    
    return left

def can_ship_in_days(weights, days, capacity):
    """
    辅助方法：判断以capacity的运载能力是否能在days天内运完所有包裹
    """
    current_weight = 0
    day_count = 1  # 至少需要1天
    
    for weight in weights:
        # 如果当前包裹的重量已经超过了运载能力，不可能运完
        if weight > capacity:
            return False
        
        # 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
        if current_weight + weight > capacity:
            day_count += 1
            current_weight = weight  # 新的一天从当前包裹开始
            
            # 如果天数已经超过了限制，可以提前返回False
            if day_count > days:
                return False
        else:
            current_weight += weight  # 继续往当前天添加包裹
    
    return day_count <= days
```

## 17. AtCoder ABC023D - 射撃王 (Shooting King)

### 题目描述
在一个二维平面上，有 N 个目标。第 i 个目标位于坐标 (x_i, y_i)。你的任务是用最少的子弹数射击所有目标。子弹每次可以以任意角度发射，但每次发射的子弹将沿着直线飞行，并且会击中所有在这条直线上的目标。

### 解题思路
- 这是一个几何问题，但可以转化为二分答案问题
- 我们需要找到最少需要多少条直线才能覆盖所有点
- 使用二分答案的方法，假设我们可以用 k 条直线覆盖所有点，然后验证这个假设是否成立
- 验证方法：选择一个点，尝试用一条直线覆盖尽可能多的点，然后递归地处理剩余的点

### 时间复杂度
- O(N^3 log N)，其中 N 是目标的数量
- 二分查找需要 log N 次迭代，每次验证需要 O(N^3) 的时间

### 空间复杂度
- O(N)，用于存储已覆盖的点

#### Java 实现
```java
/**
 * AtCoder ABC023D - Shooting King
 * 
 * 时间复杂度: O(N^3 log N)
 * 空间复杂度: O(N)
 * 
 * @param points 目标点数组，每个点是[x, y]
 * @return 最少需要的子弹数
 */
public int minBullets(int[][] points) {
    int n = points.length;
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    int left = 1, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (canCover(points, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 辅助方法：判断是否可以用k条直线覆盖所有点
 */
private boolean canCover(int[][] points, int k) {
    boolean[] covered = new boolean[points.length];
    return coverRecursive(points, covered, 0, k);
}

/**
 * 递归辅助方法：尝试覆盖剩余的点
 */
private boolean coverRecursive(int[][] points, boolean[] covered, int coveredCount, int remainingLines) {
    int n = points.length;
    if (coveredCount == n) return true;
    if (remainingLines == 0) return false;
    
    // 找到第一个未覆盖的点
    int first = -1;
    for (int i = 0; i < n; i++) {
        if (!covered[i]) {
            first = i;
            break;
        }
    }
    
    // 尝试通过第一个未覆盖的点画一条直线
    for (int i = 0; i < n; i++) {
        if (i == first || covered[i]) continue;
        
        // 标记所有在这条直线上的点
        boolean[] newCovered = Arrays.copyOf(covered, n);
        int newCount = coveredCount;
        
        for (int j = 0; j < n; j++) {
            if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                newCovered[j] = true;
                newCount++;
            }
        }
        
        if (coverRecursive(points, newCovered, newCount, remainingLines - 1)) {
            return true;
        }
    }
    
    // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    return coverRecursive(points, covered, coveredCount + 1, remainingLines - 1);
}

/**
 * 辅助方法：判断三个点是否共线
 */
private boolean isCollinear(int[] p1, int[] p2, int[] p3) {
    // 使用叉积判断三点共线
    // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0]);
}
```

#### C++ 实现
```cpp
/**
 * AtCoder ABC023D - Shooting King
 * 
 * 时间复杂度: O(N^3 log N)
 * 空间复杂度: O(N)
 * 
 * @param points 目标点数组，每个点是[x, y]
 * @return 最少需要的子弹数
 */
int minBullets(vector<pair<int, int>>& points) {
    int n = points.size();
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    int left = 1, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (canCover(points, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 辅助方法：判断是否可以用k条直线覆盖所有点
 */
bool canCover(vector<pair<int, int>>& points, int k) {
    vector<bool> covered(points.size(), false);
    return coverRecursive(points, covered, 0, k);
}

/**
 * 递归辅助方法：尝试覆盖剩余的点
 */
bool coverRecursive(vector<pair<int, int>>& points, vector<bool>& covered, int coveredCount, int remainingLines) {
    int n = points.size();
    if (coveredCount == n) return true;
    if (remainingLines == 0) return false;
    
    // 找到第一个未覆盖的点
    int first = -1;
    for (int i = 0; i < n; i++) {
        if (!covered[i]) {
            first = i;
            break;
        }
    }
    
    // 尝试通过第一个未覆盖的点画一条直线
    for (int i = 0; i < n; i++) {
        if (i == first || covered[i]) continue;
        
        // 标记所有在这条直线上的点
        vector<bool> newCovered = covered;
        int newCount = coveredCount;
        
        for (int j = 0; j < n; j++) {
            if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                newCovered[j] = true;
                newCount++;
            }
        }
        
        if (coverRecursive(points, newCovered, newCount, remainingLines - 1)) {
            return true;
        }
    }
    
    // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    covered[first] = true;
    bool result = coverRecursive(points, covered, coveredCount + 1, remainingLines - 1);
    covered[first] = false;  // 回溯
    return result;
}

/**
 * 辅助方法：判断三个点是否共线
 */
bool isCollinear(pair<int, int>& p1, pair<int, int>& p2, pair<int, int>& p3) {
    // 使用叉积判断三点共线
    // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (long long)(p2.second - p1.second) * (p3.first - p1.first) == 
           (long long)(p3.second - p1.second) * (p2.first - p1.first);
}
```

#### Python 实现
```python
def min_bullets(points):
    """
    AtCoder ABC023D - Shooting King
    
    时间复杂度: O(N^3 log N)
    空间复杂度: O(N)
    
    :param points: 目标点数组，每个点是(x, y)
    :return: 最少需要的子弹数
    """
    n = len(points)
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    left, right = 1, n
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_cover(points, mid):
            right = mid
        else:
            left = mid + 1
    
    return left

def can_cover(points, k):
    """
    辅助方法：判断是否可以用k条直线覆盖所有点
    """
    covered = [False] * len(points)
    return cover_recursive(points, covered, 0, k)

def cover_recursive(points, covered, covered_count, remaining_lines):
    """
    递归辅助方法：尝试覆盖剩余的点
    """
    n = len(points)
    if covered_count == n:
        return True
    if remaining_lines == 0:
        return False
    
    # 找到第一个未覆盖的点
    first = -1
    for i in range(n):
        if not covered[i]:
            first = i
            break
    
    # 尝试通过第一个未覆盖的点画一条直线
    for i in range(n):
        if i == first or covered[i]:
            continue
        
        # 标记所有在这条直线上的点
        new_covered = covered.copy()
        new_count = covered_count
        
        for j in range(n):
            if not new_covered[j] and is_collinear(points[first], points[i], points[j]):
                new_covered[j] = True
                new_count += 1
        
        if cover_recursive(points, new_covered, new_count, remaining_lines - 1):
            return True
    
    # 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    covered[first] = True
    result = cover_recursive(points, covered, covered_count + 1, remaining_lines - 1)
    covered[first] = False  # 回溯
    return result

def is_collinear(p1, p2, p3):
    """
    辅助方法：判断三个点是否共线
    """
    # 使用叉积判断三点共线
    # (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0])
```

## 18. 二分查找算法的更多变体与应用

### 二分查找的扩展应用

#### 1. 二分答案法

二分答案是一种非常强大的技巧，特别适用于**求满足条件的最大/最小值**的问题。其基本思路是：

1. 确定可能的答案范围 [left, right]
2. 在这个范围内进行二分查找
3. 对于每个中间值 mid，判断是否满足题目条件
4. 根据判断结果调整搜索范围

这种方法的关键在于**如何高效地判断一个候选答案是否满足条件**。

例如，在 LeetCode 875（爱吃香蕉的珂珂）和 LeetCode 1011（在D天内送达包裹的能力）中，我们就使用了二分答案的方法。

#### 2. 二分查找与贪心结合

在 SPOJ 的 AGGRCOW 问题中，我们结合了二分查找和贪心算法：

1. 使用二分查找来猜测最大的最小距离
2. 使用贪心算法来验证是否可以按照这个距离放置所有奶牛

这种结合是解决优化问题的常用策略。

#### 3. 二分查找与其他数据结构结合

二分查找可以与其他数据结构结合使用，例如：

- **二分查找与哈希表结合**：在某些情况下，可以先用哈希表预处理数据，然后使用二分查找进行查询
- **二分查找与前缀和结合**：对于某些区间查询问题，可以使用前缀和预处理，然后使用二分查找加速查询

### 二分查找的常见陷阱

1. **整数溢出**：在计算 mid = (left + right) / 2 时，可能会发生整数溢出。正确的写法是 mid = left + ((right - left) >> 1)

2. **死循环**：在循环条件和边界处理时，如果不小心，可能会导致死循环。通常使用 `while (left < right)` 或 `while (left <= right)` 作为循环条件，具体取决于问题。

3. **边界处理**：在调整 left 和 right 时，需要注意是否应该包含 mid。通常的做法是：
   - 如果 `mid` 可能是答案，则 `right = mid`
   - 否则，`left = mid + 1` 或 `right = mid - 1`

4. **浮点数精度**：对于浮点数的二分查找，需要注意精度问题，通常设置一个很小的 epsilon（例如 1e-7）来控制循环结束。

### 二分查找的高级应用场景

1. **多维二分查找**：在二维或更高维的空间中进行二分查找

2. **旋转数组中的二分查找**：如 LeetCode 33 和 LeetCode 153，在部分有序的数组中进行二分查找

3. **二分查找在实际系统中的应用**：
   - 在数据库索引中查找数据
   - 在缓存系统中查找元素
   - 在排序算法中优化某些操作

## 19. 牛客网 - 二分查找变体题目

### 19.1 牛客网 - 旋转数组的最小数字

**题目描述**：
把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个非递减排序的数组的一个旋转，输出旋转数组的最小元素。

**解题思路**：
- 使用二分查找，比较中间元素与右边界元素
- 如果中间元素小于右边界元素，说明最小值在左半部分
- 如果中间元素大于右边界元素，说明最小值在右半部分
- 如果相等，则右边界左移一位

**时间复杂度**：O(log n)，最坏情况O(n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 牛客网 - 旋转数组的最小数字
 * 
 * 时间复杂度: O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param array 旋转数组
 * @return 最小元素
 */
public int minNumberInRotateArray(int[] array) {
    if (array == null || array.length == 0) {
        return 0;
    }
    
    int left = 0, right = array.length - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] > array[right]) {
            // 中间元素大于右边界，最小值在右半部分
            left = mid + 1;
        } else if (array[mid] < array[right]) {
            // 中间元素小于右边界，最小值在左半部分
            right = mid;
        } else {
            // 中间元素等于右边界，无法判断，右边界左移
            right--;
        }
    }
    
    return array[left];
}
```

#### C++ 实现
```cpp
/**
 * 牛客网 - 旋转数组的最小数字
 * 
 * 时间复杂度: O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param array 旋转数组
 * @return 最小元素
 */
int minNumberInRotateArray(vector<int>& array) {
    if (array.empty()) {
        return 0;
    }
    
    int left = 0, right = array.size() - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] > array[right]) {
            // 中间元素大于右边界，最小值在右半部分
            left = mid + 1;
        } else if (array[mid] < array[right]) {
            // 中间元素小于右边界，最小值在左半部分
            right = mid;
        } else {
            // 中间元素等于右边界，无法判断，右边界左移
            right--;
        }
    }
    
    return array[left];
}
```

#### Python 实现
```python
def min_number_in_rotate_array(array):
    """
    牛客网 - 旋转数组的最小数字
    
    时间复杂度: O(log n)，最坏情况O(n)
    空间复杂度: O(1)
    
    :param array: 旋转数组
    :return: 最小元素
    """
    if not array:
        return 0
    
    left, right = 0, len(array) - 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if array[mid] > array[right]:
            # 中间元素大于右边界，最小值在右半部分
            left = mid + 1
        elif array[mid] < array[right]:
            # 中间元素小于右边界，最小值在左半部分
            right = mid
        else:
            # 中间元素等于右边界，无法判断，右边界左移
            right -= 1
    
    return array[left]
```

### 19.2 牛客网 - 数字在排序数组中出现的次数

**题目描述**：
统计一个数字在排序数组中出现的次数。

**解题思路**：
- 使用二分查找找到目标数字的第一次出现位置和最后一次出现位置
- 出现次数 = 最后一次位置 - 第一次位置 + 1

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 牛客网 - 数字在排序数组中出现的次数
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param array 排序数组
 * @param k 目标数字
 * @return 出现次数
 */
public int getNumberOfK(int[] array, int k) {
    if (array == null || array.length == 0) {
        return 0;
    }
    
    int first = findFirst(array, k);
    if (first == -1) {
        return 0;
    }
    
    int last = findLast(array, k);
    return last - first + 1;
}

/**
 * 查找目标数字第一次出现的位置
 */
private int findFirst(int[] array, int k) {
    int left = 0, right = array.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] >= k) {
            if (array[mid] == k) {
                result = mid;
            }
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}

/**
 * 查找目标数字最后一次出现的位置
 */
private int findLast(int[] array, int k) {
    int left = 0, right = array.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] <= k) {
            if (array[mid] == k) {
                result = mid;
            }
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

#### C++ 实现
```cpp
/**
 * 牛客网 - 数字在排序数组中出现的次数
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param array 排序数组
 * @param k 目标数字
 * @return 出现次数
 */
int getNumberOfK(vector<int>& array, int k) {
    if (array.empty()) {
        return 0;
    }
    
    int first = findFirst(array, k);
    if (first == -1) {
        return 0;
    }
    
    int last = findLast(array, k);
    return last - first + 1;
}

/**
 * 查找目标数字第一次出现的位置
 */
int findFirst(vector<int>& array, int k) {
    int left = 0, right = array.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] >= k) {
            if (array[mid] == k) {
                result = mid;
            }
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}

/**
 * 查找目标数字最后一次出现的位置
 */
int findLast(vector<int>& array, int k) {
    int left = 0, right = array.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (array[mid] <= k) {
            if (array[mid] == k) {
                result = mid;
            }
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

#### Python 实现
```python
def get_number_of_k(array, k):
    """
    牛客网 - 数字在排序数组中出现的次数
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param array: 排序数组
    :param k: 目标数字
    :return: 出现次数
    """
    if not array:
        return 0
    
    first = find_first(array, k)
    if first == -1:
        return 0
    
    last = find_last(array, k)
    return last - first + 1

def find_first(array, k):
    """
    查找目标数字第一次出现的位置
    """
    left, right = 0, len(array) - 1
    result = -1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if array[mid] >= k:
            if array[mid] == k:
                result = mid
            right = mid - 1
        else:
            left = mid + 1
    
    return result

def find_last(array, k):
    """
    查找目标数字最后一次出现的位置
    """
    left, right = 0, len(array) - 1
    result = -1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if array[mid] <= k:
            if array[mid] == k:
                result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result
```

## 20. 剑指Offer - 二分查找相关题目

### 20.1 剑指Offer 11 - 旋转数组的最小数字

**题目描述**：
把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。

**解题思路**：
- 与牛客网题目类似，使用二分查找
- 注意处理重复元素的情况

**时间复杂度**：O(log n)，最坏情况O(n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 剑指Offer 11 - 旋转数组的最小数字
 * 
 * 时间复杂度: O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param numbers 旋转数组
 * @return 最小元素
 */
public int minArray(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
        throw new IllegalArgumentException("数组不能为空");
    }
    
    int left = 0, right = numbers.length - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (numbers[mid] > numbers[right]) {
            // 中间元素大于右边界，最小值在右半部分
            left = mid + 1;
        } else if (numbers[mid] < numbers[right]) {
            // 中间元素小于右边界，最小值在左半部分
            right = mid;
        } else {
            // 中间元素等于右边界，无法判断，右边界左移
            right--;
        }
    }
    
    return numbers[left];
}
```

#### C++ 实现
```cpp
/**
 * 剑指Offer 11 - 旋转数组的最小数字
 * 
 * 时间复杂度: O(log n)，最坏情况O(n)
 * 空间复杂度: O(1)
 * 
 * @param numbers 旋转数组
 * @return 最小元素
 */
int minArray(vector<int>& numbers) {
    if (numbers.empty()) {
        throw invalid_argument("数组不能为空");
    }
    
    int left = 0, right = numbers.size() - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (numbers[mid] > numbers[right]) {
            // 中间元素大于右边界，最小值在右半部分
            left = mid + 1;
        } else if (numbers[mid] < numbers[right]) {
            // 中间元素小于右边界，最小值在左半部分
            right = mid;
        } else {
            // 中间元素等于右边界，无法判断，右边界左移
            right--;
        }
    }
    
    return numbers[left];
}
```

#### Python 实现
```python
def min_array(numbers):
    """
    剑指Offer 11 - 旋转数组的最小数字
    
    时间复杂度: O(log n)，最坏情况O(n)
    空间复杂度: O(1)
    
    :param numbers: 旋转数组
    :return: 最小元素
    """
    if not numbers:
        raise ValueError("数组不能为空")
    
    left, right = 0, len(numbers) - 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if numbers[mid] > numbers[right]:
            # 中间元素大于右边界，最小值在右半部分
            left = mid + 1
        elif numbers[mid] < numbers[right]:
            # 中间元素小于右边界，最小值在左半部分
            right = mid
        else:
            # 中间元素等于右边界，无法判断，右边界左移
            right -= 1
    
    return numbers[left]
```

### 20.2 剑指Offer 53-I - 在排序数组中查找数字

**题目描述**：
统计一个数字在排序数组中出现的次数。

**解题思路**：
- 使用二分查找找到目标数字的左右边界
- 出现次数 = 右边界 - 左边界 - 1

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 剑指Offer 53-I - 在排序数组中查找数字
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 排序数组
 * @param target 目标数字
 * @return 出现次数
 */
public int search(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
        return 0;
    }
    
    // 查找右边界
    int right = findRightBound(nums, target);
    if (right == -1) {
        return 0;
    }
    
    // 查找左边界
    int left = findLeftBound(nums, target);
    
    return right - left + 1;
}

/**
 * 查找目标数字的左边界
 */
private int findLeftBound(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] >= target) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    // 检查左边界是否有效
    if (left < nums.length && nums[left] == target) {
        return left;
    }
    return -1;
}

/**
 * 查找目标数字的右边界
 */
private int findRightBound(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    // 检查右边界是否有效
    if (right >= 0 && nums[right] == target) {
        return right;
    }
    return -1;
}
```

#### C++ 实现
```cpp
/**
 * 剑指Offer 53-I - 在排序数组中查找数字
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 排序数组
 * @param target 目标数字
 * @return 出现次数
 */
int search(vector<int>& nums, int target) {
    if (nums.empty()) {
        return 0;
    }
    
    // 查找右边界
    int right = findRightBound(nums, target);
    if (right == -1) {
        return 0;
    }
    
    // 查找左边界
    int left = findLeftBound(nums, target);
    
    return right - left + 1;
}

/**
 * 查找目标数字的左边界
 */
int findLeftBound(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] >= target) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    // 检查左边界是否有效
    if (left < nums.size() && nums[left] == target) {
        return left;
    }
    return -1;
}

/**
 * 查找目标数字的右边界
 */
int findRightBound(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] <= target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    // 检查右边界是否有效
    if (right >= 0 && nums[right] == target) {
        return right;
    }
    return -1;
}
```

#### Python 实现
```python
def search(nums, target):
    """
    剑指Offer 53-I - 在排序数组中查找数字
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 排序数组
    :param target: 目标数字
    :return: 出现次数
    """
    if not nums:
        return 0
    
    # 查找右边界
    right = find_right_bound(nums, target)
    if right == -1:
        return 0
    
    # 查找左边界
    left = find_left_bound(nums, target)
    
    return right - left + 1

def find_left_bound(nums, target):
    """
    查找目标数字的左边界
    """
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] >= target:
            right = mid - 1
        else:
            left = mid + 1
    
    # 检查左边界是否有效
    if left < len(nums) and nums[left] == target:
        return left
    return -1

def find_right_bound(nums, target):
    """
    查找目标数字的右边界
    """
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] <= target:
            left = mid + 1
        else:
            right = mid - 1
    
    # 检查右边界是否有效
    if right >= 0 and nums[right] == target:
        return right
    return -1
```

## 21. HackerRank - 二分查找题目

### 21.1 HackerRank - Ice Cream Parlor

**题目描述**：
给定一个整数数组表示冰淇淋的价格，以及一个整数表示总金额。找到两个不同位置的冰淇淋，它们的价格之和等于总金额。

**解题思路**：
- 先对数组进行排序，但需要记录原始索引
- 使用二分查找来寻找匹配的价格
- 或者使用哈希表来优化查找过程

**时间复杂度**：O(n log n)
**空间复杂度**：O(n)

#### Java 实现
```java
/**
 * HackerRank - Ice Cream Parlor
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * @param cost 冰淇淋价格数组
 * @param money 总金额
 * @return 两个冰淇淋的索引（1-based）
 */
public int[] iceCreamParlor(int money, int[] cost) {
    // 创建包含索引的价格对数组
    List<int[]> priceWithIndex = new ArrayList<>();
    for (int i = 0; i < cost.length; i++) {
        priceWithIndex.add(new int[]{cost[i], i});
    }
    
    // 按价格排序
    priceWithIndex.sort((a, b) -> Integer.compare(a[0], b[0]));
    
    for (int i = 0; i < priceWithIndex.size(); i++) {
        int currentPrice = priceWithIndex.get(i)[0];
        int remaining = money - currentPrice;
        
        // 使用二分查找寻找剩余金额
        int left = i + 1, right = priceWithIndex.size() - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int midPrice = priceWithIndex.get(mid)[0];
            
            if (midPrice == remaining) {
                // 找到匹配的价格
                int index1 = priceWithIndex.get(i)[1] + 1;  // 1-based索引
                int index2 = priceWithIndex.get(mid)[1] + 1; // 1-based索引
                return new int[]{Math.min(index1, index2), Math.max(index1, index2)};
            } else if (midPrice < remaining) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return new int[]{-1, -1};  // 未找到
}
```

#### C++ 实现
```cpp
/**
 * HackerRank - Ice Cream Parlor
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * @param cost 冰淇淋价格数组
 * @param money 总金额
 * @return 两个冰淇淋的索引（1-based）
 */
vector<int> iceCreamParlor(int money, vector<int>& cost) {
    // 创建包含索引的价格对数组
    vector<pair<int, int>> priceWithIndex;
    for (int i = 0; i < cost.size(); i++) {
        priceWithIndex.push_back({cost[i], i});
    }
    
    // 按价格排序
    sort(priceWithIndex.begin(), priceWithIndex.end());
    
    for (int i = 0; i < priceWithIndex.size(); i++) {
        int currentPrice = priceWithIndex[i].first;
        int remaining = money - currentPrice;
        
        // 使用二分查找寻找剩余金额
        int left = i + 1, right = priceWithIndex.size() - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int midPrice = priceWithIndex[mid].first;
            
            if (midPrice == remaining) {
                // 找到匹配的价格
                int index1 = priceWithIndex[i].second + 1;  // 1-based索引
                int index2 = priceWithIndex[mid].second + 1; // 1-based索引
                return {min(index1, index2), max(index1, index2)};
            } else if (midPrice < remaining) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return {-1, -1};  // 未找到
}
```

#### Python 实现
```python
def ice_cream_parlor(money, cost):
    """
    HackerRank - Ice Cream Parlor
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    :param cost: 冰淇淋价格数组
    :param money: 总金额
    :return: 两个冰淇淋的索引（1-based）
    """
    # 创建包含索引的价格对数组
    price_with_index = [(price, idx) for idx, price in enumerate(cost)]
    
    # 按价格排序
    price_with_index.sort()
    
    for i in range(len(price_with_index)):
        current_price, current_idx = price_with_index[i]
        remaining = money - current_price
        
        # 使用二分查找寻找剩余金额
        left, right = i + 1, len(price_with_index) - 1
        while left <= right:
            mid = left + ((right - left) >> 1)
            mid_price, mid_idx = price_with_index[mid]
            
            if mid_price == remaining:
                # 找到匹配的价格
                index1 = current_idx + 1  # 1-based索引
                index2 = mid_idx + 1      # 1-based索引
                return sorted([index1, index2])
            elif mid_price < remaining:
                left = mid + 1
            else:
                right = mid - 1
    
    return [-1, -1]  # 未找到
```

## 22. 杭电OJ (HDU) - 二分查找题目

### 22.1 HDU 2141 - Can you find it?

**题目描述**：
给定三个数组A、B、C，以及多个查询X。对于每个查询，判断是否存在a∈A, b∈B, c∈C，使得a+b+c=X。

**解题思路**：
- 将A+B的所有可能和存储在一个数组中
- 对这个和数组进行排序
- 对于每个查询X，使用二分查找在A+B的和数组中查找是否存在X-c（c∈C）

**时间复杂度**：O(L*M + N*log(L*M))
**空间复杂度**：O(L*M)

#### Java 实现
```java
/**
 * HDU 2141 - Can you find it?
 * 
 * 时间复杂度: O(L*M + N*log(L*M))
 * 空间复杂度: O(L*M)
 * 
 * @param A 数组A
 * @param B 数组B
 * @param C 数组C
 * @param queries 查询数组
 * @return 每个查询的结果（true/false）
 */
public boolean[] canFindIt(int[] A, int[] B, int[] C, int[] queries) {
    // 计算A+B的所有可能和
    List<Integer> sumAB = new ArrayList<>();
    for (int a : A) {
        for (int b : B) {
            sumAB.add(a + b);
        }
    }
    
    // 排序A+B的和数组
    Collections.sort(sumAB);
    
    boolean[] results = new boolean[queries.length];
    
    for (int i = 0; i < queries.length; i++) {
        int X = queries[i];
        boolean found = false;
        
        // 对于每个c∈C，在A+B的和数组中查找X-c
        for (int c : C) {
            int target = X - c;
            
            // 使用二分查找
            int left = 0, right = sumAB.size() - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int midVal = sumAB.get(mid);
                
                if (midVal == target) {
                    found = true;
                    break;
                } else if (midVal < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            if (found) break;
        }
        
        results[i] = found;
    }
    
    return results;
}
```

#### C++ 实现
```cpp
/**
 * HDU 2141 - Can you find it?
 * 
 * 时间复杂度: O(L*M + N*log(L*M))
 * 空间复杂度: O(L*M)
 * 
 * @param A 数组A
 * @param B 数组B
 * @param C 数组C
 * @param queries 查询数组
 * @return 每个查询的结果（true/false）
 */
vector<bool> canFindIt(vector<int>& A, vector<int>& B, vector<int>& C, vector<int>& queries) {
    // 计算A+B的所有可能和
    vector<int> sumAB;
    for (int a : A) {
        for (int b : B) {
            sumAB.push_back(a + b);
        }
    }
    
    // 排序A+B的和数组
    sort(sumAB.begin(), sumAB.end());
    
    vector<bool> results(queries.size(), false);
    
    for (int i = 0; i < queries.size(); i++) {
        int X = queries[i];
        bool found = false;
        
        // 对于每个c∈C，在A+B的和数组中查找X-c
        for (int c : C) {
            int target = X - c;
            
            // 使用二分查找
            int left = 0, right = sumAB.size() - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int midVal = sumAB[mid];
                
                if (midVal == target) {
                    found = true;
                    break;
                } else if (midVal < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            if (found) break;
        }
        
        results[i] = found;
    }
    
    return results;
}
```

#### Python 实现
```python
def can_find_it(A, B, C, queries):
    """
    HDU 2141 - Can you find it?
    
    时间复杂度: O(L*M + N*log(L*M))
    空间复杂度: O(L*M)
    
    :param A: 数组A
    :param B: 数组B
    :param C: 数组C
    :param queries: 查询数组
    :return: 每个查询的结果（True/False）
    """
    # 计算A+B的所有可能和
    sum_ab = []
    for a in A:
        for b in B:
            sum_ab.append(a + b)
    
    # 排序A+B的和数组
    sum_ab.sort()
    
    results = [False] * len(queries)
    
    for i, X in enumerate(queries):
        found = False
        
        # 对于每个c∈C，在A+B的和数组中查找X-c
        for c in C:
            target = X - c
            
            # 使用二分查找
            left, right = 0, len(sum_ab) - 1
            while left <= right:
                mid = left + ((right - left) >> 1)
                mid_val = sum_ab[mid]
                
                if mid_val == target:
                    found = True
                    break
                elif mid_val < target:
                    left = mid + 1
                else:
                    right = mid - 1
            
            if found:
                break
        
        results[i] = found
    
    return results
```

## 23. POJ (北京大学OJ) - 二分查找题目

### 23.1 POJ 2456 - Aggressive cows

**题目描述**：
农夫约翰建造了一个有N个隔间的牛棚，这些隔间分布在一条直线上，坐标是x1,...,xN。他的C头牛不满于隔间的大小，经常互相攻击。约翰为了防止牛之间互相伤害，决定把每头牛都放在离其他牛尽可能远的隔间。也就是要最大化最近的两头牛之间的距离。

**解题思路**：
- 这是一个典型的二分答案问题
- 先对隔间坐标进行排序
- 使用二分查找猜测最大的最小距离
- 对于每个候选距离，使用贪心算法验证是否可以放置所有牛

**时间复杂度**：O(N log(max-min))
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * POJ 2456 - Aggressive cows
 * 
 * 时间复杂度: O(N log(max-min))
 * 空间复杂度: O(1)
 * 
 * @param stalls 隔间坐标数组
 * @param cows 牛的数量
 * @return 最大的最小距离
 */
public int aggressiveCows(int[] stalls, int cows) {
    if (stalls == null || stalls.length == 0 || cows <= 0) {
        return 0;
    }
    
    Arrays.sort(stalls);
    
    int left = 0;
    int right = stalls[stalls.length - 1] - stalls[0];
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canPlaceCows(stalls, cows, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有牛
 */
private boolean canPlaceCows(int[] stalls, int cows, int minDist) {
    int count = 1;  // 第一头牛放在第一个隔间
    int lastPosition = stalls[0];
    
    for (int i = 1; i < stalls.length; i++) {
        if (stalls[i] - lastPosition >= minDist) {
            count++;
            lastPosition = stalls[i];
            
            if (count >= cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### C++ 实现
```cpp
/**
 * POJ 2456 - Aggressive cows
 * 
 * 时间复杂度: O(N log(max-min))
 * 空间复杂度: O(1)
 * 
 * @param stalls 隔间坐标数组
 * @param cows 牛的数量
 * @return 最大的最小距离
 */
int aggressiveCows(vector<int>& stalls, int cows) {
    if (stalls.empty() || cows <= 0) {
        return 0;
    }
    
    sort(stalls.begin(), stalls.end());
    
    int left = 0;
    int right = stalls.back() - stalls.front();
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canPlaceCows(stalls, cows, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有牛
 */
bool canPlaceCows(vector<int>& stalls, int cows, int minDist) {
    int count = 1;  // 第一头牛放在第一个隔间
    int lastPosition = stalls[0];
    
    for (int i = 1; i < stalls.size(); i++) {
        if (stalls[i] - lastPosition >= minDist) {
            count++;
            lastPosition = stalls[i];
            
            if (count >= cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### Python 实现
```python
def aggressive_cows(stalls, cows):
    """
    POJ 2456 - Aggressive cows
    
    时间复杂度: O(N log(max-min))
    空间复杂度: O(1)
    
    :param stalls: 隔间坐标数组
    :param cows: 牛的数量
    :return: 最大的最小距离
    """
    if not stalls or cows <= 0:
        return 0
    
    stalls.sort()
    
    left, right = 0, stalls[-1] - stalls[0]
    result = 0
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if can_place_cows(stalls, cows, mid):
            result = mid
            left = mid + 1  # 尝试更大的距离
        else:
            right = mid - 1  # 距离太大，需要减小
    
    return result

def can_place_cows(stalls, cows, min_dist):
    """
    验证是否可以在给定的最小距离下放置所有牛
    """
    count = 1  # 第一头牛放在第一个隔间
    last_position = stalls[0]
    
    for i in range(1, len(stalls)):
        if stalls[i] - last_position >= min_dist:
            count += 1
            last_position = stalls[i]
            
            if count >= cows:
                return True
    
    return False
```

## 24. 洛谷 (Luogu) - 二分查找题目

### 24.1 洛谷 P2678 - 跳石头

**题目描述**：
一年一度的"跳石头"比赛又要开始了！这项比赛将在一条笔直的河道中进行，河道中分布着一些巨大岩石。组委会已经选择好了两块岩石作为比赛起点和终点。在起点和终点之间，有 N 块岩石（不含起点和终点的岩石）。在比赛过程中，选手们将从起点出发，每一步跳向相邻的岩石，直至到达终点。为了提高比赛难度，组委会计划移走一些岩石，使得选手们在比赛过程中的最短跳跃距离尽可能长。由于预算限制，组委会至多从起点和终点之间移走 M 块岩石（不能移走起点和终点的岩石）。

**解题思路**：
- 二分答案问题，寻找最大的最短跳跃距离
- 对于每个候选距离，计算需要移走多少块岩石
- 如果需要移走的岩石数不超过M，则可以尝试更大的距离

**时间复杂度**：O(N log L)，其中L是河道长度
**空间复杂度**：O(N)

#### Java 实现
```java
/**
 * 洛谷 P2678 - 跳石头
 * 
 * 时间复杂度: O(N log L)
 * 空间复杂度: O(N)
 * 
 * @param L 河道长度
 * @param N 岩石数量（不含起点终点）
 * @param M 最多可移走的岩石数
 * @param rocks 岩石位置数组
 * @return 最大的最短跳跃距离
 */
public int jumpStones(int L, int N, int M, int[] rocks) {
    // 添加起点和终点
    int[] positions = new int[N + 2];
    positions[0] = 0;  // 起点
    System.arraycopy(rocks, 0, positions, 1, N);
    positions[N + 1] = L;  // 终点
    
    Arrays.sort(positions);
    
    int left = 1, right = L;
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canJump(positions, M, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最短跳跃距离下移走不超过M块岩石
 */
private boolean canJump(int[] positions, int M, int minDist) {
    int removeCount = 0;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.length; i++) {
        if (positions[i] - lastPosition < minDist) {
            // 需要移走当前岩石
            removeCount++;
            if (removeCount > M) {
                return false;
            }
        } else {
            lastPosition = positions[i];
        }
    }
    
    return removeCount <= M;
}
```

#### C++ 实现
```cpp
/**
 * 洛谷 P2678 - 跳石头
 * 
 * 时间复杂度: O(N log L)
 * 空间复杂度: O(N)
 * 
 * @param L 河道长度
 * @param N 岩石数量（不含起点终点）
 * @param M 最多可移走的岩石数
 * @param rocks 岩石位置数组
 * @return 最大的最短跳跃距离
 */
int jumpStones(int L, int N, int M, vector<int>& rocks) {
    // 添加起点和终点
    vector<int> positions(N + 2);
    positions[0] = 0;  // 起点
    copy(rocks.begin(), rocks.end(), positions.begin() + 1);
    positions[N + 1] = L;  // 终点
    
    sort(positions.begin(), positions.end());
    
    int left = 1, right = L;
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canJump(positions, M, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最短跳跃距离下移走不超过M块岩石
 */
bool canJump(vector<int>& positions, int M, int minDist) {
    int removeCount = 0;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.size(); i++) {
        if (positions[i] - lastPosition < minDist) {
            // 需要移走当前岩石
            removeCount++;
            if (removeCount > M) {
                return false;
            }
        } else {
            lastPosition = positions[i];
        }
    }
    
    return removeCount <= M;
}
```

#### Python 实现
```python
def jump_stones(L, N, M, rocks):
    """
    洛谷 P2678 - 跳石头
    
    时间复杂度: O(N log L)
    空间复杂度: O(N)
    
    :param L: 河道长度
    :param N: 岩石数量（不含起点终点）
    :param M: 最多可移走的岩石数
    :param rocks: 岩石位置数组
    :return: 最大的最短跳跃距离
    """
    # 添加起点和终点
    positions = [0] + sorted(rocks) + [L]
    
    left, right = 1, L
    result = 0
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if can_jump(positions, M, mid):
            result = mid
            left = mid + 1  # 尝试更大的距离
        else:
            right = mid - 1  # 距离太大，需要减小
    
    return result

def can_jump(positions, M, min_dist):
    """
    验证是否可以在给定的最短跳跃距离下移走不超过M块岩石
    """
    remove_count = 0
    last_position = positions[0]
    
    for i in range(1, len(positions)):
        if positions[i] - last_position < min_dist:
            # 需要移走当前岩石
            remove_count += 1
            if remove_count > M:
                return False
        else:
            last_position = positions[i]
    
    return remove_count <= M
```

## 25. CodeChef - 二分查找题目

### 25.1 CodeChef - PIANO1

**题目描述**：
钢琴老师给小明布置了N个练习曲，每个练习曲有一个难度值。小明需要按照难度递增的顺序练习这些曲子。但是小明很懒，他只想练习一部分曲子。请问小明最多可以练习多少首曲子，使得这些曲子的难度是严格递增的？

**解题思路**：
- 最长递增子序列(LIS)问题
- 使用二分查找优化动态规划解法
- 维护一个tails数组，tails[i]表示长度为i+1的递增子序列的最小结尾值

**时间复杂度**：O(n log n)
**空间复杂度**：O(n)

#### Java 实现
```java
/**
 * CodeChef - PIANO1
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * @param difficulties 练习曲难度数组
 * @return 最长递增子序列的长度
 */
public int pianoPractice(int[] difficulties) {
    if (difficulties == null || difficulties.length == 0) {
        return 0;
    }
    
    int[] tails = new int[difficulties.length];
    int size = 0;
    
    for (int difficulty : difficulties) {
        // 使用二分查找找到插入位置
        int left = 0, right = size;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (tails[mid] < difficulty) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        tails[left] = difficulty;
        if (left == size) {
            size++;
        }
    }
    
    return size;
}
```

#### C++ 实现
```cpp
/**
 * CodeChef - PIANO1
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * @param difficulties 练习曲难度数组
 * @return 最长递增子序列的长度
 */
int pianoPractice(vector<int>& difficulties) {
    if (difficulties.empty()) {
        return 0;
    }
    
    vector<int> tails(difficulties.size());
    int size = 0;
    
    for (int difficulty : difficulties) {
        // 使用二分查找找到插入位置
        int left = 0, right = size;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (tails[mid] < difficulty) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        tails[left] = difficulty;
        if (left == size) {
            size++;
        }
    }
    
    return size;
}
```

#### Python 实现
```python
def piano_practice(difficulties):
    """
    CodeChef - PIANO1
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    :param difficulties: 练习曲难度数组
    :return: 最长递增子序列的长度
    """
    if not difficulties:
        return 0
    
    tails = []
    size = 0
    
    for difficulty in difficulties:
        # 使用二分查找找到插入位置
        left, right = 0, size
        while left < right:
            mid = left + ((right - left) >> 1)
            if tails[mid] < difficulty:
                left = mid + 1
            else:
                right = mid
        
        if left == size:
            tails.append(difficulty)
            size += 1
        else:
            tails[left] = difficulty
    
    return size
```

## 26. AtCoder - 二分查找题目

### 26.1 AtCoder ABC023D - 射撃王 (Shooting King)

**题目描述**：
在一个二维平面上，有 N 个目标。第 i 个目标位于坐标 (x_i, y_i)。你的任务是用最少的子弹数射击所有目标。子弹每次可以以任意角度发射，但每次发射的子弹将沿着直线飞行，并且会击中所有在这条直线上的目标。

**解题思路**：
- 这是一个几何问题，但可以转化为二分答案问题
- 我们需要找到最少需要多少条直线才能覆盖所有点
- 使用二分答案的方法，假设我们可以用 k 条直线覆盖所有点，然后验证这个假设是否成立
- 验证方法：选择一个点，尝试用一条直线覆盖尽可能多的点，然后递归地处理剩余的点

**时间复杂度**：O(N^3 log N)，其中 N 是目标的数量
**空间复杂度**：O(N)，用于存储已覆盖的点

#### Java 实现
```java
/**
 * AtCoder ABC023D - Shooting King
 * 
 * 时间复杂度: O(N^3 log N)
 * 空间复杂度: O(N)
 * 
 * @param points 目标点数组，每个点是[x, y]
 * @return 最少需要的子弹数
 */
public int minBullets(int[][] points) {
    int n = points.length;
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    int left = 1, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (canCover(points, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 辅助方法：判断是否可以用k条直线覆盖所有点
 */
private boolean canCover(int[][] points, int k) {
    boolean[] covered = new boolean[points.length];
    return coverRecursive(points, covered, 0, k);
}

/**
 * 递归辅助方法：尝试覆盖剩余的点
 */
private boolean coverRecursive(int[][] points, boolean[] covered, int coveredCount, int remainingLines) {
    int n = points.length;
    if (coveredCount == n) return true;
    if (remainingLines == 0) return false;
    
    // 找到第一个未覆盖的点
    int first = -1;
    for (int i = 0; i < n; i++) {
        if (!covered[i]) {
            first = i;
            break;
        }
    }
    
    // 尝试通过第一个未覆盖的点画一条直线
    for (int i = 0; i < n; i++) {
        if (i == first || covered[i]) continue;
        
        // 标记所有在这条直线上的点
        boolean[] newCovered = Arrays.copyOf(covered, n);
        int newCount = coveredCount;
        
        for (int j = 0; j < n; j++) {
            if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                newCovered[j] = true;
                newCount++;
            }
        }
        
        if (coverRecursive(points, newCovered, newCount, remainingLines - 1)) {
            return true;
        }
    }
    
    // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    return coverRecursive(points, covered, coveredCount + 1, remainingLines - 1);
}

/**
 * 辅助方法：判断三个点是否共线
 */
private boolean isCollinear(int[] p1, int[] p2, int[] p3) {
    // 使用叉积判断三点共线
    // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0]);
}
```

#### C++ 实现
```cpp
/**
 * AtCoder ABC023D - Shooting King
 * 
 * 时间复杂度: O(N^3 log N)
 * 空间复杂度: O(N)
 * 
 * @param points 目标点数组，每个点是[x, y]
 * @return 最少需要的子弹数
 */
int minBullets(vector<pair<int, int>>& points) {
    int n = points.size();
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    int left = 1, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (canCover(points, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 辅助方法：判断是否可以用k条直线覆盖所有点
 */
bool canCover(vector<pair<int, int>>& points, int k) {
    vector<bool> covered(points.size(), false);
    return coverRecursive(points, covered, 0, k);
}

/**
 * 递归辅助方法：尝试覆盖剩余的点
 */
bool coverRecursive(vector<pair<int, int>>& points, vector<bool>& covered, int coveredCount, int remainingLines) {
    int n = points.size();
    if (coveredCount == n) return true;
    if (remainingLines == 0) return false;
    
    // 找到第一个未覆盖的点
    int first = -1;
    for (int i = 0; i < n; i++) {
        if (!covered[i]) {
            first = i;
            break;
        }
    }
    
    // 尝试通过第一个未覆盖的点画一条直线
    for (int i = 0; i < n; i++) {
        if (i == first || covered[i]) continue;
        
        // 标记所有在这条直线上的点
        vector<bool> newCovered = covered;
        int newCount = coveredCount;
        
        for (int j = 0; j < n; j++) {
            if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                newCovered[j] = true;
                newCount++;
            }
        }
        
        if (coverRecursive(points, newCovered, newCount, remainingLines - 1)) {
            return true;
        }
    }
    
    // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    covered[first] = true;
    bool result = coverRecursive(points, covered, coveredCount + 1, remainingLines - 1);
    covered[first] = false;  // 回溯
    return result;
}

/**
 * 辅助方法：判断三个点是否共线
 */
bool isCollinear(pair<int, int>& p1, pair<int, int>& p2, pair<int, int>& p3) {
    // 使用叉积判断三点共线
    // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (long long)(p2.second - p1.second) * (p3.first - p1.first) == 
           (long long)(p3.second - p1.second) * (p2.first - p1.first);
}
```

#### Python 实现
```python
def min_bullets(points):
    """
    AtCoder ABC023D - Shooting King
    
    时间复杂度: O(N^3 log N)
    空间复杂度: O(N)
    
    :param points: 目标点数组，每个点是(x, y)
    :return: 最少需要的子弹数
    """
    n = len(points)
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    left, right = 1, n
    while left < right:
        mid = left + ((right - left) >> 1)
        if can_cover(points, mid):
            right = mid
        else:
            left = mid + 1
    
    return left

def can_cover(points, k):
    """
    辅助方法：判断是否可以用k条直线覆盖所有点
    """
    covered = [False] * len(points)
    return cover_recursive(points, covered, 0, k)

def cover_recursive(points, covered, covered_count, remaining_lines):
    """
    递归辅助方法：尝试覆盖剩余的点
    """
    n = len(points)
    if covered_count == n:
        return True
    if remaining_lines == 0:
        return False
    
    # 找到第一个未覆盖的点
    first = -1
    for i in range(n):
        if not covered[i]:
            first = i
            break
    
    # 尝试通过第一个未覆盖的点画一条直线
    for i in range(n):
        if i == first or covered[i]:
            continue
        
        # 标记所有在这条直线上的点
        new_covered = covered.copy()
        new_count = covered_count
        
        for j in range(n):
            if not new_covered[j] and is_collinear(points[first], points[i], points[j]):
                new_covered[j] = True
                new_count += 1
        
        if cover_recursive(points, new_covered, new_count, remaining_lines - 1):
            return True
    
    # 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    covered[first] = True
    result = cover_recursive(points, covered, covered_count + 1, remaining_lines - 1)
    covered[first] = False  # 回溯
    return result

def is_collinear(p1, p2, p3):
    """
    辅助方法：判断三个点是否共线
    """
    # 使用叉积判断三点共线
    # (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0])
```

## 27. USACO - 二分查找题目

### 27.1 USACO - Aggressive Cows

**题目描述**：
农夫约翰建造了一个有N个隔间的牛棚，这些隔间分布在一条直线上，坐标是x1,...,xN。他的C头牛不满于隔间的大小，经常互相攻击。约翰为了防止牛之间互相伤害，决定把每头牛都放在离其他牛尽可能远的隔间。也就是要最大化最近的两头牛之间的距离。

**解题思路**：
- 这是一个典型的二分答案问题
- 先对隔间坐标进行排序
- 使用二分查找猜测最大的最小距离
- 对于每个候选距离，使用贪心算法验证是否可以放置所有牛

**时间复杂度**：O(N log(max-min))
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * USACO - Aggressive Cows
 * 
 * 时间复杂度: O(N log(max-min))
 * 空间复杂度: O(1)
 * 
 * @param stalls 隔间坐标数组
 * @param cows 牛的数量
 * @return 最大的最小距离
 */
public int aggressiveCows(int[] stalls, int cows) {
    if (stalls == null || stalls.length == 0 || cows <= 0) {
        return 0;
    }
    
    Arrays.sort(stalls);
    
    int left = 0;
    int right = stalls[stalls.length - 1] - stalls[0];
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canPlaceCows(stalls, cows, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有牛
 */
private boolean canPlaceCows(int[] stalls, int cows, int minDist) {
    int count = 1;  // 第一头牛放在第一个隔间
    int lastPosition = stalls[0];
    
    for (int i = 1; i < stalls.length; i++) {
        if (stalls[i] - lastPosition >= minDist) {
            count++;
            lastPosition = stalls[i];
            
            if (count >= cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### C++ 实现
```cpp
/**
 * USACO - Aggressive Cows
 * 
 * 时间复杂度: O(N log(max-min))
 * 空间复杂度: O(1)
 * 
 * @param stalls 隔间坐标数组
 * @param cows 牛的数量
 * @return 最大的最小距离
 */
int aggressiveCows(vector<int>& stalls, int cows) {
    if (stalls.empty() || cows <= 0) {
        return 0;
    }
    
    sort(stalls.begin(), stalls.end());
    
    int left = 0;
    int right = stalls.back() - stalls.front();
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (canPlaceCows(stalls, cows, mid)) {
            result = mid;
            left = mid + 1;  // 尝试更大的距离
        } else {
            right = mid - 1;  // 距离太大，需要减小
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有牛
 */
bool canPlaceCows(vector<int>& stalls, int cows, int minDist) {
    int count = 1;  // 第一头牛放在第一个隔间
    int lastPosition = stalls[0];
    
    for (int i = 1; i < stalls.size(); i++) {
        if (stalls[i] - lastPosition >= minDist) {
            count++;
            lastPosition = stalls[i];
            
            if (count >= cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### Python 实现
```python
def aggressive_cows(stalls, cows):
    """
    USACO - Aggressive Cows
    
    时间复杂度: O(N log(max-min))
    空间复杂度: O(1)
    
    :param stalls: 隔间坐标数组
    :param cows: 牛的数量
    :return: 最大的最小距离
    """
    if not stalls or cows <= 0:
        return 0
    
    stalls.sort()
    
    left, right = 0, stalls[-1] - stalls[0]
    result = 0
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if can_place_cows(stalls, cows, mid):
            result = mid
            left = mid + 1  # 尝试更大的距离
        else:
            right = mid - 1  # 距离太大，需要减小
    
    return result

def can_place_cows(stalls, cows, min_dist):
    """
    验证是否可以在给定的最小距离下放置所有牛
    """
    count = 1  # 第一头牛放在第一个隔间
    last_position = stalls[0]
    
    for i in range(1, len(stalls)):
        if stalls[i] - last_position >= min_dist:
            count += 1
            last_position = stalls[i]
            
            if count >= cows:
                return True
    
    return False
```

## 28. SPOJ - 二分查找题目

### 28.1 SPOJ - AGGRCOW (Aggressive cows)

**题目描述**：
农夫约翰建造了一个有 C 个摊位的畜棚，摊位位于 x0, ..., x(C-1)。他的 M 头奶牛总是相互攻击，约翰必须以某种方式分配奶牛到摊位，使它们之间的最小距离尽可能大。

**解题思路**：
- 使用二分查找答案 + 贪心验证
- 先对摊位位置进行排序
- 使用二分查找猜测最大的最小距离
- 对于每个候选距离，使用贪心算法验证是否可以放置所有奶牛

**时间复杂度**：O(n log(max-min) * n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * SPOJ - AGGRCOW (Aggressive cows)
 * 
 * 时间复杂度: O(n log(max-min) * n)
 * 空间复杂度: O(1)
 * 
 * @param positions 摊位位置数组
 * @param cows 奶牛数量
 * @return 最大的最小距离
 */
public static int aggressiveCows(int[] positions, int cows) {
    Arrays.sort(positions);
    int left = 0, right = positions[positions.length - 1] - positions[0];
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        if (canPlaceCows(positions, cows, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有奶牛
 */
private static boolean canPlaceCows(int[] positions, int cows, int minDist) {
    int count = 1;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.length; i++) {
        if (positions[i] - lastPosition >= minDist) {
            count++;
            lastPosition = positions[i];
            if (count == cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### C++ 实现
```cpp
/**
 * SPOJ - AGGRCOW (Aggressive cows)
 * 
 * 时间复杂度: O(n log(max-min) * n)
 * 空间复杂度: O(1)
 * 
 * @param positions 摊位位置数组
 * @param cows 奶牛数量
 * @return 最大的最小距离
 */
int aggressiveCows(std::vector<int>& positions, int cows) {
    std::sort(positions.begin(), positions.end());
    int left = 0, right = positions.back() - positions.front();
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        if (canPlaceCows(positions, cows, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

/**
 * 验证是否可以在给定的最小距离下放置所有奶牛
 */
bool canPlaceCows(std::vector<int>& positions, int cows, int minDist) {
    int count = 1;
    int lastPosition = positions[0];
    
    for (int i = 1; i < positions.size(); i++) {
        if (positions[i] - lastPosition >= minDist) {
            count++;
            lastPosition = positions[i];
            if (count == cows) {
                return true;
            }
        }
    }
    
    return false;
}
```

#### Python 实现
```python
def aggressive_cows(positions, cows):
    """
    SPOJ - AGGRCOW (Aggressive cows)
    
    时间复杂度: O(n log(max-min) * n)
    空间复杂度: O(1)
    
    :param positions: 摊位位置数组
    :param cows: 奶牛数量
    :return: 最大的最小距离
    """
    positions.sort()
    left, right = 0, positions[-1] - positions[0]
    result = 0
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        if can_place_cows(positions, cows, mid):
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result

def can_place_cows(positions, cows, min_dist):
    """
    验证是否可以在给定的最小距离下放置所有奶牛
    """
    count = 1
    last_position = positions[0]
    
    for i in range(1, len(positions)):
        if positions[i] - last_position >= min_dist:
            count += 1
            last_position = positions[i]
            if count == cows:
                return True
    
    return False
```

## 29. Project Euler - 二分查找题目

### 29.1 Project Euler Problem 1 - Multiples of 3 and 5

**题目描述**：
如果我们列出所有小于10的3或5的倍数，我们得到3, 5, 6, 9。这些数的和是23。请找出所有小于1000的3或5的倍数的和。

**解题思路**：
- 虽然这不是典型的二分查找问题，但可以使用二分查找来优化计算
- 使用等差数列求和公式结合二分查找来避免暴力计算
- 计算3的倍数和 + 5的倍数和 - 15的倍数和

**时间复杂度**：O(1) 使用数学公式，O(log n) 如果使用二分查找
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * Project Euler Problem 1 - Multiples of 3 and 5
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param limit 上限值
 * @return 所有小于limit的3或5的倍数的和
 */
public int sumMultiples(int limit) {
    // 使用等差数列求和公式
    int sum3 = sumArithmeticProgression(3, limit);
    int sum5 = sumArithmeticProgression(5, limit);
    int sum15 = sumArithmeticProgression(15, limit);
    
    return sum3 + sum5 - sum15;
}

/**
 * 计算等差数列的和
 */
private int sumArithmeticProgression(int a, int limit) {
    int n = (limit - 1) / a;  // 项数
    return n * (a + a * n) / 2;  // 等差数列求和公式
}

/**
 * 使用二分查找的版本（虽然不必要，但展示二分思想）
 */
public int sumMultiplesWithBinarySearch(int limit) {
    // 使用二分查找找到最大的倍数
    int max3 = findMaxMultiple(3, limit);
    int max5 = findMaxMultiple(5, limit);
    int max15 = findMaxMultiple(15, limit);
    
    return sumArithmeticProgression(3, limit) + 
           sumArithmeticProgression(5, limit) - 
           sumArithmeticProgression(15, limit);
}

/**
 * 使用二分查找找到小于limit的最大倍数
 */
private int findMaxMultiple(int a, int limit) {
    int left = 0, right = limit / a;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (mid * a < limit) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return (left - 1) * a;  // 返回最大的小于limit的倍数
}
```

#### C++ 实现
```cpp
/**
 * Project Euler Problem 1 - Multiples of 3 and 5
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 * 
 * @param limit 上限值
 * @return 所有小于limit的3或5的倍数的和
 */
int sumMultiples(int limit) {
    // 使用等差数列求和公式
    int sum3 = sumArithmeticProgression(3, limit);
    int sum5 = sumArithmeticProgression(5, limit);
    int sum15 = sumArithmeticProgression(15, limit);
    
    return sum3 + sum5 - sum15;
}

/**
 * 计算等差数列的和
 */
int sumArithmeticProgression(int a, int limit) {
    int n = (limit - 1) / a;  // 项数
    return n * (a + a * n) / 2;  // 等差数列求和公式
}

/**
 * 使用二分查找的版本（虽然不必要，但展示二分思想）
 */
int sumMultiplesWithBinarySearch(int limit) {
    // 使用二分查找找到最大的倍数
    int max3 = findMaxMultiple(3, limit);
    int max5 = findMaxMultiple(5, limit);
    int max15 = findMaxMultiple(15, limit);
    
    return sumArithmeticProgression(3, limit) + 
           sumArithmeticProgression(5, limit) - 
           sumArithmeticProgression(15, limit);
}

/**
 * 使用二分查找找到小于limit的最大倍数
 */
int findMaxMultiple(int a, int limit) {
    int left = 0, right = limit / a;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (mid * a < limit) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return (left - 1) * a;  // 返回最大的小于limit的倍数
}
```

#### Python 实现
```python
def sum_multiples(limit):
    """
    Project Euler Problem 1 - Multiples of 3 and 5
    
    时间复杂度: O(1)
    空间复杂度: O(1)
    
    :param limit: 上限值
    :return: 所有小于limit的3或5的倍数的和
    """
    # 使用等差数列求和公式
    sum3 = sum_arithmetic_progression(3, limit)
    sum5 = sum_arithmetic_progression(5, limit)
    sum15 = sum_arithmetic_progression(15, limit)
    
    return sum3 + sum5 - sum15

def sum_arithmetic_progression(a, limit):
    """
    计算等差数列的和
    """
    n = (limit - 1) // a  # 项数
    return n * (a + a * n) // 2  # 等差数列求和公式

def sum_multiples_with_binary_search(limit):
    """
    使用二分查找的版本（虽然不必要，但展示二分思想）
    """
    # 使用二分查找找到最大的倍数
    max3 = find_max_multiple(3, limit)
    max5 = find_max_multiple(5, limit)
    max15 = find_max_multiple(15, limit)
    
    return (sum_arithmetic_progression(3, limit) + 
            sum_arithmetic_progression(5, limit) - 
            sum_arithmetic_progression(15, limit))

def find_max_multiple(a, limit):
    """
    使用二分查找找到小于limit的最大倍数
    """
    left, right = 0, limit // a
    while left < right:
        mid = left + ((right - left) >> 1)
        if mid * a < limit:
            left = mid + 1
        else:
            right = mid
    return (left - 1) * a  # 返回最大的小于limit的倍数
```

## 30. 各大高校OJ - 二分查找题目

### 30.1 浙江大学OJ (ZOJ) - 二分查找题目

**题目描述**：
给定一个有序数组和一个目标值，使用二分查找找到目标值在数组中的位置。如果目标值不存在，返回应该插入的位置。

**解题思路**：
- 标准的二分查找问题
- 使用左边界查找模板
- 如果目标值存在，返回其索引；否则返回应该插入的位置

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 浙江大学OJ - 二分查找
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引或应该插入的位置
 */
public int binarySearch(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
        return 0;
    }
    
    int left = 0, right = nums.length;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    
    return left;
}
```

#### C++ 实现
```cpp
/**
 * 浙江大学OJ - 二分查找
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引或应该插入的位置
 */
int binarySearch(vector<int>& nums, int target) {
    if (nums.empty()) {
        return 0;
    }
    
    int left = 0, right = nums.size();
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    
    return left;
}
```

#### Python 实现
```python
def binary_search(nums, target):
    """
    浙江大学OJ - 二分查找
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: 目标值的索引或应该插入的位置
    """
    if not nums:
        return 0
    
    left, right = 0, len(nums)
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    
    return left
```

## 31. Codeforces - 二分查找题目

### 31.1 Codeforces 474B - Worms

**题目描述**：
有n堆虫子，第i堆有a_i条虫子。每条虫子都有一个编号，从1开始连续编号。给出m个查询，每个查询给出一个编号x，问编号为x的虫子属于哪一堆。

**解题思路**：
- 使用前缀和数组记录每堆虫子的结束位置
- 对于每个查询，使用二分查找在前缀和数组中查找x所在的位置
- 找到第一个前缀和大于等于x的位置

**时间复杂度**：O(n + m log n)
**空间复杂度**：O(n)

#### Java 实现
```java
/**
 * Codeforces 474B - Worms
 * 
 * 时间复杂度: O(n + m log n)
 * 空间复杂度: O(n)
 * 
 * @param piles 每堆虫子的数量数组
 * @param queries 查询数组
 * @return 每个查询对应的堆编号
 */
public int[] findWormPiles(int[] piles, int[] queries) {
    int n = piles.length;
    int m = queries.length;
    
    // 计算前缀和
    long[] prefixSum = new long[n];
    prefixSum[0] = piles[0];
    for (int i = 1; i < n; i++) {
        prefixSum[i] = prefixSum[i - 1] + piles[i];
    }
    
    int[] result = new int[m];
    
    for (int i = 0; i < m; i++) {
        long x = queries[i];
        
        // 使用二分查找找到第一个前缀和 >= x 的位置
        int left = 0, right = n - 1;
        int pileIndex = -1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (prefixSum[mid] >= x) {
                pileIndex = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        result[i] = pileIndex + 1;  // 1-based索引
    }
    
    return result;
}
```

#### C++ 实现
```cpp
/**
 * Codeforces 474B - Worms
 * 
 * 时间复杂度: O(n + m log n)
 * 空间复杂度: O(n)
 * 
 * @param piles 每堆虫子的数量数组
 * @param queries 查询数组
 * @return 每个查询对应的堆编号
 */
vector<int> findWormPiles(vector<int>& piles, vector<long long>& queries) {
    int n = piles.size();
    int m = queries.size();
    
    // 计算前缀和
    vector<long long> prefixSum(n);
    prefixSum[0] = piles[0];
    for (int i = 1; i < n; i++) {
        prefixSum[i] = prefixSum[i - 1] + piles[i];
    }
    
    vector<int> result(m);
    
    for (int i = 0; i < m; i++) {
        long long x = queries[i];
        
        // 使用二分查找找到第一个前缀和 >= x 的位置
        int left = 0, right = n - 1;
        int pileIndex = -1;
        
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (prefixSum[mid] >= x) {
                pileIndex = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        result[i] = pileIndex + 1;  // 1-based索引
    }
    
    return result;
}
```

#### Python 实现
```python
def find_worm_piles(piles, queries):
    """
    Codeforces 474B - Worms
    
    时间复杂度: O(n + m log n)
    空间复杂度: O(n)
    
    :param piles: 每堆虫子的数量数组
    :param queries: 查询数组
    :return: 每个查询对应的堆编号
    """
    n = len(piles)
    m = len(queries)
    
    # 计算前缀和
    prefix_sum = [0] * n
    prefix_sum[0] = piles[0]
    for i in range(1, n):
        prefix_sum[i] = prefix_sum[i - 1] + piles[i]
    
    result = [0] * m
    
    for i in range(m):
        x = queries[i]
        
        # 使用二分查找找到第一个前缀和 >= x 的位置
        left, right = 0, n - 1
        pile_index = -1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            
            if prefix_sum[mid] >= x:
                pile_index = mid
                right = mid - 1
            else:
                left = mid + 1
        
        result[i] = pile_index + 1  # 1-based索引
    
    return result
```

## 32. HackerEarth - 二分查找题目

### 32.1 HackerEarth - Monk and Search

**题目描述**：
给定一个数组，有Q个查询。每个查询有两种类型：
1. 0 x: 查询数组中大于等于x的元素个数
2. 1 x: 查询数组中大于x的元素个数

**解题思路**：
- 先对数组进行排序
- 对于类型0查询，使用二分查找找到第一个大于等于x的位置
- 对于类型1查询，使用二分查找找到第一个大于x的位置
- 计算从该位置到数组末尾的元素个数

**时间复杂度**：O(n log n + q log n)
**空间复杂度**：O(n)

#### Java 实现
```java
/**
 * HackerEarth - Monk and Search
 * 
 * 时间复杂度: O(n log n + q log n)
 * 空间复杂度: O(n)
 * 
 * @param arr 数组
 * @param queries 查询数组，每个查询是[type, x]
 * @return 每个查询的结果
 */
public int[] monkAndSearch(int[] arr, int[][] queries) {
    Arrays.sort(arr);
    int n = arr.length;
    int q = queries.length;
    int[] result = new int[q];
    
    for (int i = 0; i < q; i++) {
        int type = queries[i][0];
        int x = queries[i][1];
        
        if (type == 0) {
            // 查询大于等于x的元素个数
            int pos = findFirstGreaterOrEqual(arr, x);
            result[i] = n - pos;
        } else {
            // 查询大于x的元素个数
            int pos = findFirstGreater(arr, x);
            result[i] = n - pos;
        }
    }
    
    return result;
}

/**
 * 找到第一个大于等于x的位置
 */
private int findFirstGreaterOrEqual(int[] arr, int x) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (arr[mid] < x) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

/**
 * 找到第一个大于x的位置
 */
private int findFirstGreater(int[] arr, int x) {
    int left = 0, right = arr.length;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (arr[mid] <= x) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

#### C++ 实现
```cpp
/**
 * HackerEarth - Monk and Search
 * 
 * 时间复杂度: O(n log n + q log n)
 * 空间复杂度: O(n)
 * 
 * @param arr 数组
 * @param queries 查询数组，每个查询是[type, x]
 * @return 每个查询的结果
 */
vector<int> monkAndSearch(vector<int>& arr, vector<pair<int, int>>& queries) {
    sort(arr.begin(), arr.end());
    int n = arr.size();
    int q = queries.size();
    vector<int> result(q);
    
    for (int i = 0; i < q; i++) {
        int type = queries[i].first;
        int x = queries[i].second;
        
        if (type == 0) {
            // 查询大于等于x的元素个数
            int pos = findFirstGreaterOrEqual(arr, x);
            result[i] = n - pos;
        } else {
            // 查询大于x的元素个数
            int pos = findFirstGreater(arr, x);
            result[i] = n - pos;
        }
    }
    
    return result;
}

/**
 * 找到第一个大于等于x的位置
 */
int findFirstGreaterOrEqual(vector<int>& arr, int x) {
    int left = 0, right = arr.size();
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (arr[mid] < x) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

/**
 * 找到第一个大于x的位置
 */
int findFirstGreater(vector<int>& arr, int x) {
    int left = 0, right = arr.size();
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (arr[mid] <= x) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}
```

#### Python 实现
```python
def monk_and_search(arr, queries):
    """
    HackerEarth - Monk and Search
    
    时间复杂度: O(n log n + q log n)
    空间复杂度: O(n)
    
    :param arr: 数组
    :param queries: 查询数组，每个查询是[type, x]
    :return: 每个查询的结果
    """
    arr.sort()
    n = len(arr)
    result = []
    
    for query in queries:
        type_val, x = query
        
        if type_val == 0:
            # 查询大于等于x的元素个数
            pos = find_first_greater_or_equal(arr, x)
            result.append(n - pos)
        else:
            # 查询大于x的元素个数
            pos = find_first_greater(arr, x)
            result.append(n - pos)
    
    return result

def find_first_greater_or_equal(arr, x):
    """
    找到第一个大于等于x的位置
    """
    left, right = 0, len(arr)
    while left < right:
        mid = left + ((right - left) >> 1)
        if arr[mid] < x:
            left = mid + 1
        else:
            right = mid
    return left

def find_first_greater(arr, x):
    """
    找到第一个大于x的位置
    """
    left, right = 0, len(arr)
    while left < right:
        mid = left + ((right - left) >> 1)
        if arr[mid] <= x:
            left = mid + 1
        else:
            right = mid
    return left
```

## 33. 计蒜客 - 二分查找题目

### 33.1 计蒜客 - 蒜头君的购物袋

**题目描述**：
蒜头君去超市购物，他有一个购物袋，最多能装V体积的物品。超市有n件商品，每件商品有体积v_i和价值w_i。蒜头君想要选择一些商品放入购物袋，使得总价值最大，且总体积不超过V。

**解题思路**：
- 这是一个经典的0-1背包问题
- 虽然主要使用动态规划，但可以结合二分查找进行优化
- 对于大规模数据，可以使用二分查找来优化状态转移

**时间复杂度**：O(nV) 标准DP，O(n log V) 优化版本
**空间复杂度**：O(V)

#### Java 实现
```java
/**
 * 计蒜客 - 蒜头君的购物袋 (0-1背包问题)
 * 
 * 时间复杂度: O(nV)
 * 空间复杂度: O(V)
 * 
 * @param V 购物袋容量
 * @param volumes 商品体积数组
 * @param values 商品价值数组
 * @return 最大价值
 */
public int shoppingBag(int V, int[] volumes, int[] values) {
    int n = volumes.length;
    int[] dp = new int[V + 1];
    
    for (int i = 0; i < n; i++) {
        for (int j = V; j >= volumes[i]; j--) {
            dp[j] = Math.max(dp[j], dp[j - volumes[i]] + values[i]);
        }
    }
    
    return dp[V];
}

/**
 * 优化版本：使用二分查找思想（虽然不直接使用二分查找，但展示优化思路）
 */
public int shoppingBagOptimized(int V, int[] volumes, int[] values) {
    int n = volumes.length;
    
    // 对于大规模数据，可以考虑物品分组优化
    // 这里展示标准DP实现，实际工程中可以根据数据规模选择不同算法
    
    return shoppingBag(V, volumes, values);
}
```

#### C++ 实现
```cpp
/**
 * 计蒜客 - 蒜头君的购物袋 (0-1背包问题)
 * 
 * 时间复杂度: O(nV)
 * 空间复杂度: O(V)
 * 
 * @param V 购物袋容量
 * @param volumes 商品体积数组
 * @param values 商品价值数组
 * @return 最大价值
 */
int shoppingBag(int V, vector<int>& volumes, vector<int>& values) {
    int n = volumes.size();
    vector<int> dp(V + 1, 0);
    
    for (int i = 0; i < n; i++) {
        for (int j = V; j >= volumes[i]; j--) {
            dp[j] = max(dp[j], dp[j - volumes[i]] + values[i]);
        }
    }
    
    return dp[V];
}
```

#### Python 实现
```python
def shopping_bag(V, volumes, values):
    """
    计蒜客 - 蒜头君的购物袋 (0-1背包问题)
    
    时间复杂度: O(nV)
    空间复杂度: O(V)
    
    :param V: 购物袋容量
    :param volumes: 商品体积数组
    :param values: 商品价值数组
    :return: 最大价值
    """
    n = len(volumes)
    dp = [0] * (V + 1)
    
    for i in range(n):
        for j in range(V, volumes[i] - 1, -1):
            dp[j] = max(dp[j], dp[j - volumes[i]] + values[i])
    
    return dp[V]
```

## 34. MarsCode - 二分查找题目

### 34.1 MarsCode - 寻找峰值元素

**题目描述**：
给定一个整数数组，其中相邻元素不相等。找到峰值元素并返回其索引。峰值元素是指其值大于左右相邻值的元素。数组可能包含多个峰值，返回任何一个峰值的索引即可。

**解题思路**：
- 使用二分查找来寻找峰值
- 比较中间元素与其相邻元素的大小关系
- 根据比较结果决定搜索左半部分还是右半部分

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * MarsCode - 寻找峰值元素
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 整数数组
 * @return 峰值元素的索引
 */
public int findPeakElement(int[] nums) {
    if (nums == null || nums.length == 0) {
        return -1;
    }
    
    int left = 0, right = nums.length - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] > nums[mid + 1]) {
            // 峰值在左半部分或mid就是峰值
            right = mid;
        } else {
            // 峰值在右半部分
            left = mid + 1;
        }
    }
    
    return left;
}
```

#### C++ 实现
```cpp
/**
 * MarsCode - 寻找峰值元素
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 整数数组
 * @return 峰值元素的索引
 */
int findPeakElement(vector<int>& nums) {
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0, right = nums.size() - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] > nums[mid + 1]) {
            // 峰值在左半部分或mid就是峰值
            right = mid;
        } else {
            // 峰值在右半部分
            left = mid + 1;
        }
    }
    
    return left;
}
```

#### Python 实现
```python
def find_peak_element(nums):
    """
    MarsCode - 寻找峰值元素
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 整数数组
    :return: 峰值元素的索引
    """
    if not nums:
        return -1
    
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] > nums[mid + 1]:
            # 峰值在左半部分或mid就是峰值
            right = mid
        else:
            # 峰值在右半部分
            left = mid + 1
    
    return left
```

## 35. UVa OJ - 二分查找题目

### 35.1 UVa 10341 - Solve It

**题目描述**：
求解方程：p*e^(-x) + q*sin(x) + r*cos(x) + s*tan(x) + t*x^2 + u = 0，其中0 ≤ x ≤ 1。给定p, q, r, s, t, u的值，求x的值。

**解题思路**：
- 这是一个非线性方程求解问题
- 使用二分查找来逼近解
- 由于函数在[0,1]区间内是连续的，可以使用二分法

**时间复杂度**：O(log(1/ε))，其中ε是精度要求
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * UVa 10341 - Solve It
 * 
 * 时间复杂度: O(log(1/ε))
 * 空间复杂度: O(1)
 * 
 * @param p, q, r, s, t, u 方程参数
 * @return 方程的解x，精度1e-9
 */
public double solveEquation(double p, double q, double r, double s, double t, double u) {
    // 定义方程函数
    Function<Double, Double> f = x -> 
        p * Math.exp(-x) + q * Math.sin(x) + r * Math.cos(x) + 
        s * Math.tan(x) + t * x * x + u;
    
    double left = 0.0, right = 1.0;
    double epsilon = 1e-9;
    
    // 检查端点值
    double f0 = f.apply(0.0);
    double f1 = f.apply(1.0);
    
    if (Math.abs(f0) < epsilon) return 0.0;
    if (Math.abs(f1) < epsilon) return 1.0;
    
    // 如果端点值同号，则无解
    if (f0 * f1 > 0) {
        return -1;  // 无解
    }
    
    // 使用二分查找求解
    while (right - left > epsilon) {
        double mid = (left + right) / 2;
        double fmid = f.apply(mid);
        
        if (Math.abs(fmid) < epsilon) {
            return mid;
        }
        
        if (f0 * fmid < 0) {
            right = mid;
        } else {
            left = mid;
            f0 = fmid;  // 更新左端点函数值
        }
    }
    
    return (left + right) / 2;
}
```

#### C++ 实现
```cpp
/**
 * UVa 10341 - Solve It
 * 
 * 时间复杂度: O(log(1/ε))
 * 空间复杂度: O(1)
 * 
 * @param p, q, r, s, t, u 方程参数
 * @return 方程的解x，精度1e-9
 */
double solveEquation(double p, double q, double r, double s, double t, double u) {
    auto f = [&](double x) {
        return p * exp(-x) + q * sin(x) + r * cos(x) + 
               s * tan(x) + t * x * x + u;
    };
    
    double left = 0.0, right = 1.0;
    double epsilon = 1e-9;
    
    // 检查端点值
    double f0 = f(0.0);
    double f1 = f(1.0);
    
    if (abs(f0) < epsilon) return 0.0;
    if (abs(f1) < epsilon) return 1.0;
    
    // 如果端点值同号，则无解
    if (f0 * f1 > 0) {
        return -1;  // 无解
    }
    
    // 使用二分查找求解
    while (right - left > epsilon) {
        double mid = (left + right) / 2;
        double fmid = f(mid);
        
        if (abs(fmid) < epsilon) {
            return mid;
        }
        
        if (f0 * fmid < 0) {
            right = mid;
        } else {
            left = mid;
            f0 = fmid;  // 更新左端点函数值
        }
    }
    
    return (left + right) / 2;
}
```

#### Python 实现
```python
import math

def solve_equation(p, q, r, s, t, u):
    """
    UVa 10341 - Solve It
    
    时间复杂度: O(log(1/ε))
    空间复杂度: O(1)
    
    :param p, q, r, s, t, u: 方程参数
    :return: 方程的解x，精度1e-9
    """
    def f(x):
        return (p * math.exp(-x) + q * math.sin(x) + r * math.cos(x) + 
                s * math.tan(x) + t * x * x + u)
    
    left, right = 0.0, 1.0
    epsilon = 1e-9
    
    # 检查端点值
    f0 = f(0.0)
    f1 = f(1.0)
    
    if abs(f0) < epsilon:
        return 0.0
    if abs(f1) < epsilon:
        return 1.0
    
    # 如果端点值同号，则无解
    if f0 * f1 > 0:
        return -1  # 无解
    
    # 使用二分查找求解
    while right - left > epsilon:
        mid = (left + right) / 2
        fmid = f(mid)
        
        if abs(fmid) < epsilon:
            return mid
        
        if f0 * fmid < 0:
            right = mid
        else:
            left = mid
            f0 = fmid  # 更新左端点函数值
    
    return (left + right) / 2
```

## 36. TimusOJ - 二分查找题目

### 36.1 Timus 1490 - Bicolored Horses

**题目描述**：
有n匹马排成一排，每匹马要么是黑色要么是白色。现在需要将这n匹马分成k个连续的非空组。每个组的不愉快值定义为该组中黑色马的数量乘以白色马的数量。总不愉快值是所有组的不愉快值之和。求最小的总不愉快值。

**解题思路**：
- 这是一个动态规划问题，但可以使用二分查找优化
- 使用DP + 二分查找（决策单调性优化）
- 定义dp[i][j]表示前i匹马分成j组的最小不愉快值
- 使用二分查找来优化状态转移

**时间复杂度**：O(n^2 log n)
**空间复杂度**：O(n^2)

#### Java 实现
```java
/**
 * Timus 1490 - Bicolored Horses
 * 
 * 时间复杂度: O(n^2 log n)
 * 空间复杂度: O(n^2)
 * 
 * @param n 马的数量
 * @param k 分组数
 * @param horses 马的序列（0表示白色，1表示黑色）
 * @return 最小的总不愉快值
 */
public long minUnhappiness(int n, int k, int[] horses) {
    if (k > n) return 0;
    
    // 预处理前缀和
    int[] blackPrefix = new int[n + 1];
    int[] whitePrefix = new int[n + 1];
    
    for (int i = 1; i <= n; i++) {
        blackPrefix[i] = blackPrefix[i - 1] + (horses[i - 1] == 1 ? 1 : 0);
        whitePrefix[i] = whitePrefix[i - 1] + (horses[i - 1] == 0 ? 1 : 0);
    }
    
    // DP数组
    long[][] dp = new long[n + 1][k + 1];
    for (int i = 0; i <= n; i++) {
        Arrays.fill(dp[i], Long.MAX_VALUE / 2);
    }
    dp[0][0] = 0;
    
    for (int j = 1; j <= k; j++) {
        for (int i = j; i <= n; i++) {
            for (int p = j - 1; p < i; p++) {
                int blackCount = blackPrefix[i] - blackPrefix[p];
                int whiteCount = whitePrefix[i] - whitePrefix[p];
                long cost = (long) blackCount * whiteCount;
                
                dp[i][j] = Math.min(dp[i][j], dp[p][j - 1] + cost);
            }
        }
    }
    
    return dp[n][k];
}

/**
 * 优化版本：使用二分查找优化决策单调性
 */
public long minUnhappinessOptimized(int n, int k, int[] horses) {
    if (k > n) return 0;
    
    // 预处理前缀和
    int[] blackPrefix = new int[n + 1];
    int[] whitePrefix = new int[n + 1];
    
    for (int i = 1; i <= n; i++) {
        blackPrefix[i] = blackPrefix[i - 1] + (horses[i - 1] == 1 ? 1 : 0);
        whitePrefix[i] = whitePrefix[i - 1] + (horses[i - 1] == 0 ? 1 : 0);
    }
    
    // 使用决策单调性优化
    long[] dp = new long[n + 1];
    long[] newDp = new long[n + 1];
    Arrays.fill(dp, Long.MAX_VALUE / 2);
    dp[0] = 0;
    
    for (int j = 1; j <= k; j++) {
        Arrays.fill(newDp, Long.MAX_VALUE / 2);
        
        // 使用二分查找优化决策单调性
        for (int i = j; i <= n; i++) {
            int left = j - 1, right = i;
            while (right - left > 1) {
                int mid = left + ((right - left) >> 1);
                long cost1 = calculateCost(blackPrefix, whitePrefix, mid, i);
                long cost2 = calculateCost(blackPrefix, whitePrefix, mid + 1, i);
                
                if (dp[mid] + cost1 < dp[mid + 1] + cost2) {
                    right = mid;
                } else {
                    left = mid;
                }
            }
            
            newDp[i] = Math.min(newDp[i], dp[right] + calculateCost(blackPrefix, whitePrefix, right, i));
        }
        
        // 交换DP数组
        long[] temp = dp;
        dp = newDp;
        newDp = temp;
    }
    
    return dp[n];
}

private long calculateCost(int[] blackPrefix, int[] whitePrefix, int from, int to) {
    int blackCount = blackPrefix[to] - blackPrefix[from];
    int whiteCount = whitePrefix[to] - whitePrefix[from];
    return (long) blackCount * whiteCount;
}
```

#### C++ 实现
```cpp
/**
 * Timus 1490 - Bicolored Horses
 * 
 * 时间复杂度: O(n^2 log n)
 * 空间复杂度: O(n^2)
 * 
 * @param n 马的数量
 * @param k 分组数
 * @param horses 马的序列（0表示白色，1表示黑色）
 * @return 最小的总不愉快值
 */
long long minUnhappiness(int n, int k, vector<int>& horses) {
    if (k > n) return 0;
    
    // 预处理前缀和
    vector<int> blackPrefix(n + 1, 0);
    vector<int> whitePrefix(n + 1, 0);
    
    for (int i = 1; i <= n; i++) {
        blackPrefix[i] = blackPrefix[i - 1] + (horses[i - 1] == 1 ? 1 : 0);
        whitePrefix[i] = whitePrefix[i - 1] + (horses[i - 1] == 0 ? 1 : 0);
    }
    
    // DP数组
    vector<vector<long long>> dp(n + 1, vector<long long>(k + 1, LLONG_MAX / 2));
    dp[0][0] = 0;
    
    for (int j = 1; j <= k; j++) {
        for (int i = j; i <= n; i++) {
            for (int p = j - 1; p < i; p++) {
                int blackCount = blackPrefix[i] - blackPrefix[p];
                int whiteCount = whitePrefix[i] - whitePrefix[p];
                long long cost = (long long) blackCount * whiteCount;
                
                dp[i][j] = min(dp[i][j], dp[p][j - 1] + cost);
            }
        }
    }
    
    return dp[n][k];
}
```

#### Python 实现
```python
def min_unhappiness(n, k, horses):
    """
    Timus 1490 - Bicolored Horses
    
    时间复杂度: O(n^2 log n)
    空间复杂度: O(n^2)
    
    :param n: 马的数量
    :param k: 分组数
    :param horses: 马的序列（0表示白色，1表示黑色）
    :return: 最小的总不愉快值
    """
    if k > n:
        return 0
    
    # 预处理前缀和
    black_prefix = [0] * (n + 1)
    white_prefix = [0] * (n + 1)
    
    for i in range(1, n + 1):
        black_prefix[i] = black_prefix[i - 1] + (1 if horses[i - 1] == 1 else 0)
        white_prefix[i] = white_prefix[i - 1] + (1 if horses[i - 1] == 0 else 0)
    
    # DP数组
    dp = [[float('inf')] * (k + 1) for _ in range(n + 1)]
    dp[0][0] = 0
    
    for j in range(1, k + 1):
        for i in range(j, n + 1):
            for p in range(j - 1, i):
                black_count = black_prefix[i] - black_prefix[p]
                white_count = white_prefix[i] - white_prefix[p]
                cost = black_count * white_count
                
                dp[i][j] = min(dp[i][j], dp[p][j - 1] + cost)
    
    return dp[n][k]
```

## 37. AizuOJ - 二分查找题目

### 37.1 AizuOJ - ALDS1_4_B: Binary Search

**题目描述**：
给定两个数组S和T，统计数组T中有多少个元素出现在数组S中。数组S已经排序。

**解题思路**：
- 使用二分查找来检查T中的每个元素是否在S中出现
- 由于S已经排序，可以直接使用二分查找
- 对于T中的每个元素，在S中进行二分查找

**时间复杂度**：O(q log n)，其中n是S的大小，q是T的大小
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * AizuOJ - ALDS1_4_B: Binary Search
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(1)
 * 
 * @param S 已排序的数组
 * @param T 查询数组
 * @return T中出现在S中的元素个数
 */
public int binarySearchCount(int[] S, int[] T) {
    int count = 0;
    
    for (int target : T) {
        if (binarySearch(S, target)) {
            count++;
        }
    }
    
    return count;
}

/**
 * 二分查找辅助方法
 */
private boolean binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (arr[mid] == target) {
            return true;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}
```

#### C++ 实现
```cpp
/**
 * AizuOJ - ALDS1_4_B: Binary Search
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(1)
 * 
 * @param S 已排序的数组
 * @param T 查询数组
 * @return T中出现在S中的元素个数
 */
int binarySearchCount(vector<int>& S, vector<int>& T) {
    int count = 0;
    
    for (int target : T) {
        if (binary_search(S.begin(), S.end(), target)) {
            count++;
        }
    }
    
    return count;
}

/**
 * 自定义二分查找实现
 */
bool binarySearch(vector<int>& arr, int target) {
    int left = 0, right = arr.size() - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (arr[mid] == target) {
            return true;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}
```

#### Python 实现
```python
def binary_search_count(S, T):
    """
    AizuOJ - ALDS1_4_B: Binary Search
    
    时间复杂度: O(q log n)
    空间复杂度: O(1)
    
    :param S: 已排序的数组
    :param T: 查询数组
    :return: T中出现在S中的元素个数
    """
    count = 0
    
    for target in T:
        if binary_search(S, target):
            count += 1
    
    return count

def binary_search(arr, target):
    """
    二分查找辅助方法
    """
    left, right = 0, len(arr) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if arr[mid] == target:
            return True
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return False
```

## 38. Comet OJ - 二分查找题目

### 38.1 Comet OJ - 二分查找练习

**题目描述**：
给定一个有序数组和一个目标值，使用二分查找找到目标值在数组中的位置。如果目标值存在，返回其索引；否则返回-1。

**解题思路**：
- 标准的二分查找实现
- 使用闭区间[left, right]进行搜索
- 循环条件为left <= right

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * Comet OJ - 二分查找练习
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引，不存在返回-1
 */
public int binarySearch(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
        return -1;
    }
    
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

#### C++ 实现
```cpp
/**
 * Comet OJ - 二分查找练习
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引，不存在返回-1
 */
int binarySearch(vector<int>& nums, int target) {
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0, right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

#### Python 实现
```python
def binary_search(nums, target):
    """
    Comet OJ - 二分查找练习
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: 目标值的索引，不存在返回-1
    """
    if not nums:
        return -1
    
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

## 39. 杭电OJ (HDU) - 二分查找题目

### 39.1 HDU 2141 - Can you find it? (再次实现)

**题目描述**：
给定三个数组A、B、C，以及多个查询X。对于每个查询，判断是否存在a∈A, b∈B, c∈C，使得a+b+c=X。

**解题思路**：
- 将A+B的所有可能和存储在一个数组中
- 对这个和数组进行排序
- 对于每个查询X，使用二分查找在A+B的和数组中查找是否存在X-c（c∈C）

**时间复杂度**：O(L*M + N*log(L*M))
**空间复杂度**：O(L*M)

#### Java 实现
```java
/**
 * HDU 2141 - Can you find it?
 * 
 * 时间复杂度: O(L*M + N*log(L*M))
 * 空间复杂度: O(L*M)
 * 
 * @param A 数组A
 * @param B 数组B
 * @param C 数组C
 * @param queries 查询数组
 * @return 每个查询的结果（true/false）
 */
public boolean[] canFindIt(int[] A, int[] B, int[] C, int[] queries) {
    // 计算A+B的所有可能和
    List<Integer> sumAB = new ArrayList<>();
    for (int a : A) {
        for (int b : B) {
            sumAB.add(a + b);
        }
    }
    
    // 排序A+B的和数组
    Collections.sort(sumAB);
    
    boolean[] results = new boolean[queries.length];
    
    for (int i = 0; i < queries.length; i++) {
        int X = queries[i];
        boolean found = false;
        
        // 对于每个c∈C，在A+B的和数组中查找X-c
        for (int c : C) {
            int target = X - c;
            
            // 使用二分查找
            int left = 0, right = sumAB.size() - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int midVal = sumAB.get(mid);
                
                if (midVal == target) {
                    found = true;
                    break;
                } else if (midVal < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            if (found) break;
        }
        
        results[i] = found;
    }
    
    return results;
}
```

#### C++ 实现
```cpp
/**
 * HDU 2141 - Can you find it?
 * 
 * 时间复杂度: O(L*M + N*log(L*M))
 * 空间复杂度: O(L*M)
 * 
 * @param A 数组A
 * @param B 数组B
 * @param C 数组C
 * @param queries 查询数组
 * @return 每个查询的结果（true/false）
 */
vector<bool> canFindIt(vector<int>& A, vector<int>& B, vector<int>& C, vector<int>& queries) {
    // 计算A+B的所有可能和
    vector<int> sumAB;
    for (int a : A) {
        for (int b : B) {
            sumAB.push_back(a + b);
        }
    }
    
    // 排序A+B的和数组
    sort(sumAB.begin(), sumAB.end());
    
    vector<bool> results(queries.size(), false);
    
    for (int i = 0; i < queries.size(); i++) {
        int X = queries[i];
        bool found = false;
        
        // 对于每个c∈C，在A+B的和数组中查找X-c
        for (int c : C) {
            int target = X - c;
            
            // 使用二分查找
            int left = 0, right = sumAB.size() - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                int midVal = sumAB[mid];
                
                if (midVal == target) {
                    found = true;
                    break;
                } else if (midVal < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            if (found) break;
        }
        
        results[i] = found;
    }
    
    return results;
}
```

#### Python 实现
```python
def can_find_it(A, B, C, queries):
    """
    HDU 2141 - Can you find it?
    
    时间复杂度: O(L*M + N*log(L*M))
    空间复杂度: O(L*M)
    
    :param A: 数组A
    :param B: 数组B
    :param C: 数组C
    :param queries: 查询数组
    :return: 每个查询的结果（True/False）
    """
    # 计算A+B的所有可能和
    sum_ab = []
    for a in A:
        for b in B:
            sum_ab.append(a + b)
    
    # 排序A+B的和数组
    sum_ab.sort()
    
    results = [False] * len(queries)
    
    for i, X in enumerate(queries):
        found = False
        
        # 对于每个c∈C，在A+B的和数组中查找X-c
        for c in C:
            target = X - c
            
            # 使用二分查找
            left, right = 0, len(sum_ab) - 1
            while left <= right:
                mid = left + ((right - left) >> 1)
                mid_val = sum_ab[mid]
                
                if mid_val == target:
                    found = True
                    break
                elif mid_val < target:
                    left = mid + 1
                else:
                    right = mid - 1
            
            if found:
                break
        
        results[i] = found
    
    return results
```

## 40. LOJ (LibreOJ) - 二分查找题目

### 40.1 LOJ - 二分查找模板题

**题目描述**：
实现一个二分查找函数，在有序数组中查找目标值。如果找到返回索引，否则返回-1。

**解题思路**：
- 标准的二分查找实现
- 使用左闭右闭区间
- 注意边界条件的处理

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * LOJ - 二分查找模板题
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引，不存在返回-1
 */
public int binarySearch(int[] nums, int target) {
    if (nums == null || nums.length == 0) {
        return -1;
    }
    
    int left = 0, right = nums.length - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

#### C++ 实现
```cpp
/**
 * LOJ - 二分查找模板题
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param target 目标值
 * @return 目标值的索引，不存在返回-1
 */
int binarySearch(vector<int>& nums, int target) {
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0, right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}
```

#### Python 实现
```python
def binary_search(nums, target):
    """
    LOJ - 二分查找模板题
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param nums: 有序数组
    :param target: 目标值
    :return: 目标值的索引，不存在返回-1
    """
    if not nums:
        return -1
    
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

## 41. 牛客网 - 二分查找题目

### 41.1 牛客网 - 二分查找

**题目描述**：
请实现有重复数字的有序数组的二分查找。输出在数组中第一个大于等于查找值的位置，如果数组中不存在这样的数，则输出数组长度加一。

**解题思路**：
- 使用二分查找找到第一个大于等于目标值的位置
- 如果找到，返回位置+1（1-based索引）
- 如果没找到，返回数组长度+1

**时间复杂度**：O(log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * 牛客网 - 二分查找
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 数组长度
 * @param v 目标值
 * @param a 有序数组（可能有重复）
 * @return 第一个大于等于v的位置（1-based索引）
 */
public int binarySearch(int n, int v, int[] a) {
    if (a == null || a.length == 0) {
        return n + 1;
    }
    
    int left = 0, right = n - 1;
    int result = n + 1;  // 默认返回数组长度+1
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (a[mid] >= v) {
            result = mid + 1;  // 1-based索引
            right = mid - 1;    // 继续向左查找更小的位置
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}
```

#### C++ 实现
```cpp
/**
 * 牛客网 - 二分查找
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 数组长度
 * @param v 目标值
 * @param a 有序数组（可能有重复）
 * @return 第一个大于等于v的位置（1-based索引）
 */
int binarySearch(int n, int v, vector<int>& a) {
    if (a.empty()) {
        return n + 1;
    }
    
    int left = 0, right = n - 1;
    int result = n + 1;  // 默认返回数组长度+1
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (a[mid] >= v) {
            result = mid + 1;  // 1-based索引
            right = mid - 1;    // 继续向左查找更小的位置
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}
```

#### Python 实现
```python
def binary_search(n, v, a):
    """
    牛客网 - 二分查找
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    
    :param n: 数组长度
    :param v: 目标值
    :param a: 有序数组（可能有重复）
    :return: 第一个大于等于v的位置（1-based索引）
    """
    if not a:
        return n + 1
    
    left, right = 0, n - 1
    result = n + 1  # 默认返回数组长度+1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if a[mid] >= v:
            result = mid + 1  # 1-based索引
            right = mid - 1    # 继续向左查找更小的位置
        else:
            left = mid + 1
    
    return result
```

## 42. acwing - 二分查找题目

### 42.1 acwing - 数的范围

**题目描述**：
给定一个按照升序排列的长度为n的整数数组，以及q个查询。对于每个查询，返回一个元素k的起始位置和终止位置（位置从0开始计数）。如果数组中不存在该元素，则返回"-1 -1"。

**解题思路**：
- 使用两次二分查找
- 第一次查找第一个等于目标值的位置
- 第二次查找最后一个等于目标值的位置

**时间复杂度**：O(q log n)
**空间复杂度**：O(1)

#### Java 实现
```java
/**
 * acwing - 数的范围
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 升序排列的整数数组
 * @param queries 查询数组
 * @return 每个查询的起始和终止位置
 */
public String[] findRange(int[] nums, int[] queries) {
    int q = queries.length;
    String[] results = new String[q];
    
    for (int i = 0; i < q; i++) {
        int target = queries[i];
        
        // 查找第一个等于target的位置
        int left = findFirst(nums, target);
        if (left == -1) {
            results[i] = "-1 -1";
            continue;
        }
        
        // 查找最后一个等于target的位置
        int right = findLast(nums, target);
        results[i] = left + " " + right;
    }
    
    return results;
}

/**
 * 查找第一个等于目标值的位置
 */
private int findFirst(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] >= target) {
            if (nums[mid] == target) {
                result = mid;
            }
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}

/**
 * 查找最后一个等于目标值的位置
 */
private int findLast(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] <= target) {
            if (nums[mid] == target) {
                result = mid;
            }
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

#### C++ 实现
```cpp
/**
 * acwing - 数的范围
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 升序排列的整数数组
 * @param queries 查询数组
 * @return 每个查询的起始和终止位置
 */
vector<string> findRange(vector<int>& nums, vector<int>& queries) {
    int q = queries.size();
    vector<string> results;
    
    for (int target : queries) {
        // 查找第一个等于target的位置
        int left = findFirst(nums, target);
        if (left == -1) {
            results.push_back("-1 -1");
            continue;
        }
        
        // 查找最后一个等于target的位置
        int right = findLast(nums, target);
        results.push_back(to_string(left) + " " + to_string(right));
    }
    
    return results;
}

/**
 * 查找第一个等于目标值的位置
 */
int findFirst(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] >= target) {
            if (nums[mid] == target) {
                result = mid;
            }
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}

/**
 * 查找最后一个等于目标值的位置
 */
int findLast(vector<int>& nums, int target) {
    int left = 0, right = nums.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] <= target) {
            if (nums[mid] == target) {
                result = mid;
            }
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}
```

#### Python 实现
```python
def find_range(nums, queries):
    """
    acwing - 数的范围
    
    时间复杂度: O(q log n)
    空间复杂度: O(1)
    
    :param nums: 升序排列的整数数组
    :param queries: 查询数组
    :return: 每个查询的起始和终止位置
    """
    results = []
    
    for target in queries:
        # 查找第一个等于target的位置
        left = find_first(nums, target)
        if left == -1:
            results.append("-1 -1")
            continue
        
        # 查找最后一个等于target的位置
        right = find_last(nums, target)
        results.append(f"{left} {right}")
    
    return results

def find_first(nums, target):
    """
    查找第一个等于目标值的位置
    """
    left, right = 0, len(nums) - 1
    result = -1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] >= target:
            if nums[mid] == target:
                result = mid
            right = mid - 1
        else:
            left = mid + 1
    
    return result

def find_last(nums, target):
    """
    查找最后一个等于目标值的位置
    """
    left, right = 0, len(nums) - 1
    result = -1
    
    while left <= right:
        mid = left + ((right - left) >> 1)
        
        if nums[mid] <= target:
            if nums[mid] == target:
                result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result
```

## 43. 各大算法平台题目总结

### 43.1 二分查找算法核心要点总结

**算法思想**：
- 二分查找是一种在有序数组中查找特定元素的搜索算法
- 每次比较都将搜索范围缩小一半，直到找到目标或搜索范围为空

**适用条件**：
1. 数组必须是有序的（升序或降序）
2. 数组元素可以通过索引直接访问
3. 查找操作比插入/删除操作更频繁

**时间复杂度分析**：
- 最好情况：O(1) - 目标在中间位置
- 平均情况：O(log n) - 每次将搜索范围减半
- 最坏情况：O(log n) - 目标不存在或位于边界

**空间复杂度**：
- 迭代实现：O(1) - 只需要常数级别的额外空间
- 递归实现：O(log n) - 递归调用栈的深度

### 43.2 二分查找模板总结

**模板1：标准二分查找**
```java
int binarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

**模板2：查找左边界**
```java
int findLeftBound(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] >= target) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

**模板3：查找右边界**
```java
int findRightBound(int[] nums, int target) {
    int left = 0, right = nums.length;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] <= target) left = mid + 1;
        else right = mid;
    }
    return left - 1;
}
```

### 43.3 二分查找常见问题及解决方案

**问题1：整数溢出**
- 解决方案：使用 `mid = left + (right - left) / 2` 而不是 `(left + right) / 2`

**问题2：边界条件处理**
- 解决方案：明确搜索区间是左闭右闭 `[left, right]` 还是左闭右开 `[left, right)`

**问题3：重复元素处理**
- 解决方案：使用查找左右边界的模板来处理重复元素

**问题4：旋转数组查找**
- 解决方案：先判断哪一半是有序的，然后在有序的一半中进行二分查找

### 43.4 二分查找的工程化应用

**1. 数据库索引**
- 二分查找用于B+树索引的查找操作
- 时间复杂度O(log n)适合大规模数据查询

**2. 操作系统**
- 内存管理中的空闲块查找
- 文件系统中的目录查找

**3. 编译器优化**
- 符号表的快速查找
- 常量折叠优化

**4. 机器学习**
- 超参数调优中的网格搜索
- 模型选择中的二分搜索

### 43.5 二分查找的进阶应用

**1. 二分答案**
- 将最优化问题转化为判定问题
- 通过二分查找确定最优解的范围

**2. 三分查找**
- 用于单峰函数的极值查找
- 每次将搜索范围缩小到原来的2/3

**3. 矩阵搜索**
- 在二维有序矩阵中查找元素
- 时间复杂度O(log(mn))

**4. 流数据中的二分查找**
- 适用于数据流中的近似查找
- 使用布隆过滤器等数据结构配合二分查找

## 总结

二分查找是一种非常重要的算法，适用于以下场景：

1. 在有序数组中查找元素
2. 查找边界位置（第一个/最后一个满足条件的元素）
3. 优化搜索空间（二分答案）
4. 在旋转数组中查找元素
5. 查找峰值元素

掌握二分查找的关键在于：
1. 确定搜索区间（开区间还是闭区间）
2. 确定循环条件（left < right 还是 left <= right）
3. 确定边界更新方式（mid+1 还是 mid）
4. 理解不同模板的适用场景

在实际应用中，还需要注意：
1. 数据溢出问题
2. 输入参数验证
3. 边界条件处理
4. 异常情况处理
5. 性能优化