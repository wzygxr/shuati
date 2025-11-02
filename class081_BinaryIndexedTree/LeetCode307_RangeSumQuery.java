package class108;

import java.util.*;

/**
 * LeetCode 307. 区域和检索 - 数组可修改
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/description/
 * 
 * 题目描述:
 * 给你一个数组 nums ，请你完成两类查询。
 * 其中一类查询要求更新数组 nums 下标对应的值
 * 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和，其中 left <= right
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
 * 
 * 示例:
 * 输入：
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * 输出：
 * [null, 9, null, 8]
 * 
 * 解释：
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1,2,5]
 * numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8
 * 
 * 解题思路:
 * 使用树状数组实现单点修改和区间查询
 * 时间复杂度：
 * - 单点修改: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 */

class NumArray {
    // 树状数组最大容量
    private int MAXN;
    
    // 树状数组，存储前缀和信息
    private int[] tree;
    
    // 原始数组
    private int[] nums;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    private int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    private void add(int i, int v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i < MAXN) {
            tree[i] += v;
            // 移动到父节点
            i += lowbit(i);
        }
    }
    
    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    private int sum(int i) {
        int ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
    /**
     * 构造函数：用整数数组 nums 初始化对象
     * 
     * @param nums 初始数组
     */
    public NumArray(int[] nums) {
        this.nums = nums;
        this.MAXN = nums.length + 1;
        this.tree = new int[MAXN];
        
        // 初始化树状数组
        for (int i = 0; i < nums.length; i++) {
            add(i + 1, nums[i]);
        }
    }
    
    /**
     * 更新操作：将 nums[index] 的值更新为 val
     * 
     * @param index 要更新的位置
     * @param val 新的值
     */
    public void update(int index, int val) {
        // 计算差值
        int delta = val - nums[index];
        // 更新原始数组
        nums[index] = val;
        // 更新树状数组
        add(index + 1, delta);
    }
    
    /**
     * 区间查询：返回数组 nums 中索引 left 和索引 right 之间（包含）的nums元素的和
     * 
     * @param left 区间起始位置
     * @param right 区间结束位置
     * @return 区间和
     */
    public int sumRange(int left, int right) {
        return sum(right + 1) - sum(left);
    }
}


/**
 * 以下是C++实现的树状数组（Fenwick Tree）代码
 * 包含所有已实现的算法问题的C++版本
 * 
 * // LeetCode 307 - 区域和检索 - 数组可修改（C++实现）
 * class NumArray {
 * private:
 *     vector<int> tree; // 树状数组
 *     vector<int> nums; // 原始数组
 *     int n;            // 数组大小
 * 
 *     // lowbit函数
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     // 单点增加
 *     void add(int i, int v) {
 *         while (i < tree.size()) {
 *             tree[i] += v;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     // 前缀和查询
 *     int sum(int i) {
 *         int ans = 0;
 *         while (i > 0) {
 *             ans += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return ans;
 *     }
 * 
 * public:
 *     // 构造函数
 *     NumArray(vector<int>& nums) {
 *         this->nums = nums;
 *         this->n = nums.size();
 *         this->tree.resize(n + 1, 0);
 * 
 *         // 初始化树状数组
 *         for (int i = 0; i < n; i++) {
 *             add(i + 1, nums[i]);
 *         }
 *     }
 * 
 *     // 更新操作
 *     void update(int index, int val) {
 *         int delta = val - nums[index];
 *         nums[index] = val;
 *         add(index + 1, delta);
 *     }
 * 
 *     // 区间查询
 *     int sumRange(int left, int right) {
 *         return sum(right + 1) - sum(left);
 *     }
 * };
 * 
 * // POJ 2299 Ultra-QuickSort - 求逆序数（C++实现）
 * class UltraQuickSort {
 * private:
 *     vector<long long> tree;
 *     int size;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         while (i <= size) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     long long query(int i) {
 *         long long sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     long long countInversions(vector<int>& nums) {
 *         int n = nums.size();
 *         if (n <= 1) return 0;
 * 
 *         // 离散化
 *         vector<int> sortedNums = nums;
 *         sort(sortedNums.begin(), sortedNums.end());
 *         sortedNums.erase(unique(sortedNums.begin(), sortedNums.end()), sortedNums.end());
 * 
 *         unordered_map<int, int> valueToRank;
 *         int rank = 1;
 *         for (int num : sortedNums) {
 *             valueToRank[num] = rank++;
 *         }
 * 
 *         // 计算逆序数
 *         long long inversions = 0;
 *         size = rank - 1;
 *         tree.resize(size + 1, 0);
 * 
 *         // 从右向左遍历
 *         for (int i = n - 1; i >= 0; i--) {
 *             int currentRank = valueToRank[nums[i]];
 *             inversions += query(currentRank - 1);
 *             update(currentRank, 1);
 *         }
 * 
 *         return inversions;
 *     }
 * };
 * 
 * // POJ 2352 Stars - 统计左下方星星个数（C++实现）
 * class Stars {
 * private:
 *     vector<int> tree;
 *     int size;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         if (i == 0) i = 1; // 处理x=0的情况
 *         while (i <= size) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     int query(int i) {
 *         if (i == 0) return 0;
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     vector<int> countStars(vector<vector<int>>& stars, int maxX) {
 *         int n = stars.size();
 *         vector<int> result(n, 0);
 * 
 *         // 排序
 *         sort(stars.begin(), stars.end(), [](const vector<int>& a, const vector<int>& b) {
 *             if (a[1] != b[1]) return a[1] < b[1];
 *             return a[0] < b[0];
 *         });
 * 
 *         size = maxX + 1;
 *         tree.resize(size + 1, 0);
 * 
 *         for (auto& star : stars) {
 *             int x = star[0];
 *             int level = query(x);
 *             result[level]++;
 *             update(x, 1);
 *         }
 * 
 *         return result;
 *     }
 * };
 * 
 * // LeetCode 493 - 翻转对（C++实现）
 * class ReversePairs {
 * private:
 *     vector<int> tree;
 *     int size;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         while (i <= size) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     int query(int i) {
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     int reversePairs(vector<int>& nums) {
 *         int n = nums.size();
 *         if (n <= 1) return 0;
 * 
 *         // 离散化
 *         set<long long> allNumbers;
 *         for (int num : nums) {
 *             allNumbers.insert((long long)num);
 *             allNumbers.insert((long long)num * 2);
 *         }
 * 
 *         vector<long long> sortedList(allNumbers.begin(), allNumbers.end());
 *         unordered_map<long long, int> valueToRank;
 *         int rank = 1;
 *         for (long long num : sortedList) {
 *             valueToRank[num] = rank++;
 *         }
 * 
 *         int count = 0;
 *         size = sortedList.size();
 *         tree.resize(size + 1, 0);
 * 
 *         // 从右向左遍历
 *         for (int i = n - 1; i >= 0; i--) {
 *             // 二分查找
 *             int left = 0, right = sortedList.size();
 *             while (left < right) {
 *                 int mid = left + (right - left) / 2;
 *                 if (sortedList[mid] >= (double)nums[i] / 2) {
 *                     right = mid;
 *                 } else {
 *                     left = mid + 1;
 *                 }
 *             }
 *             count += left > 0 ? query(left) : 0;
 * 
 *             // 更新树状数组
 *             int currentRank = valueToRank[(long long)nums[i]];
 *             update(currentRank, 1);
 *         }
 * 
 *         return count;
 *     }
 * };
 * 
 * // LeetCode 315 - 计算右侧小于当前元素的个数（C++实现）
 * class Solution315 {
 * private:
 *     vector<int> tree;
 *     int size;
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void update(int i, int val) {
 *         while (i <= size) {
 *             tree[i] += val;
 *             i += lowbit(i);
 *         }
 *     }
 * 
 *     int query(int i) {
 *         int sum = 0;
 *         while (i > 0) {
 *             sum += tree[i];
 *             i -= lowbit(i);
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     vector<int> countSmaller(vector<int>& nums) {
 *         int n = nums.size();
 *         vector<int> result(n);
 * 
 *         if (n == 0) return result;
 * 
 *         // 离散化
 *         vector<int> sortedNums = nums;
 *         sort(sortedNums.begin(), sortedNums.end());
 *         sortedNums.erase(unique(sortedNums.begin(), sortedNums.end()), sortedNums.end());
 * 
 *         unordered_map<int, int> valueToRank;
 *         int rank = 1;
 *         for (int num : sortedNums) {
 *             valueToRank[num] = rank++;
 *         }
 * 
 *         size = sortedNums.size();
 *         tree.resize(size + 1, 0);
 * 
 *         // 从右向左处理
 *         for (int i = n - 1; i >= 0; i--) {
 *             int currentRank = valueToRank[nums[i]];
 *             result[i] = query(currentRank - 1);
 *             update(currentRank, 1);
 *         }
 * 
 *         return result;
 *     }
 * };
 * 
 * // LeetCode 308 - 二维区域和检索 - 可变（C++实现）
 * class NumMatrix308 {
 * private:
 *     vector<vector<int>> tree; // 二维树状数组
 *     vector<vector<int>> nums; // 原始矩阵
 *     int m, n;                 // 行数和列数
 * 
 *     int lowbit(int x) {
 *         return x & (-x);
 *     }
 * 
 *     void updateTree(int i, int j, int val) {
 *         for (int x = i; x <= m; x += lowbit(x)) {
 *             for (int y = j; y <= n; y += lowbit(y)) {
 *                 tree[x][y] += val;
 *             }
 *         }
 *     }
 * 
 *     int queryTree(int i, int j) {
 *         int sum = 0;
 *         for (int x = i; x > 0; x -= lowbit(x)) {
 *             for (int y = j; y > 0; y -= lowbit(y)) {
 *                 sum += tree[x][y];
 *             }
 *         }
 *         return sum;
 *     }
 * 
 * public:
 *     NumMatrix308(vector<vector<int>>& matrix) {
 *         if (matrix.empty() || matrix[0].empty()) return;
 * 
 *         m = matrix.size();
 *         n = matrix[0].size();
 *         tree.resize(m + 1, vector<int>(n + 1, 0));
 *         nums = vector<vector<int>>(m, vector<int>(n, 0));
 * 
 *         for (int i = 0; i < m; i++) {
 *             for (int j = 0; j < n; j++) {
 *                 update(i, j, matrix[i][j]);
 *             }
 *         }
 *     }
 * 
 *     void update(int row, int col, int val) {
 *         int delta = val - nums[row][col];
 *         nums[row][col] = val;
 *         updateTree(row + 1, col + 1, delta);
 *     }
 * 
 *     int sumRegion(int row1, int col1, int row2, int col2) {
 *         return queryTree(row2 + 1, col2 + 1) -
 *                queryTree(row1, col2 + 1) -
 *                queryTree(row2 + 1, col1) +
 *                queryTree(row1, col1);
 *     }
 * };
 */


/**
 * 以下是Python实现的树状数组（Fenwick Tree）代码
 * 包含所有已实现的算法问题的Python版本
 * 
 * # LeetCode 307 - 区域和检索 - 数组可修改（Python实现）
 * class NumArray:
 *     def __init__(self, nums):
 *         self.nums = nums
 *         self.n = len(nums)
 *         self.tree = [0] * (self.n + 1)  # 树状数组从索引1开始
 * 
 *         # 初始化树状数组
 *         for i in range(self.n):
 *             self.add(i + 1, nums[i])
 *     
 *     def lowbit(self, x):
 *         return x & (-x)
 *     
 *     def add(self, i, val):
 *         while i < len(self.tree):
 *             self.tree[i] += val
 *             i += self.lowbit(i)
 *     
 *     def sum_prefix(self, i):
 *         ans = 0
 *         while i > 0:
 *             ans += self.tree[i]
 *             i -= self.lowbit(i)
 *         return ans
 *     
 *     def update(self, index, val):
 *         delta = val - self.nums[index]
 *         self.nums[index] = val
 *         self.add(index + 1, delta)
 *     
 *     def sumRange(self, left, right):
 *         return self.sum_prefix(right + 1) - self.sum_prefix(left)
 * 
 * # POJ 2299 Ultra-QuickSort - 求逆序数（Python实现）
 * class UltraQuickSort:
 *     def countInversions(self, nums):
 *         n = len(nums)
 *         if n <= 1:
 *             return 0
 * 
 *         # 离散化
 *         sorted_nums = sorted(list(set(nums)))
 *         value_to_rank = {val: i + 1 for i, val in enumerate(sorted_nums)}
 * 
 *         # 树状数组类
 *         class FenwickTree:
 *             def __init__(self, size):
 *                 self.size = size
 *                 self.tree = [0] * (size + 1)
 *             
 *             def lowbit(self, x):
 *                 return x & (-x)
 *             
 *             def update(self, i, val):
 *                 while i <= self.size:
 *                     self.tree[i] += val
 *                     i += self.lowbit(i)
 *             
 *             def query(self, i):
 *                 sum_ = 0
 *                 while i > 0:
 *                     sum_ += self.tree[i]
 *                     i -= self.lowbit(i)
 *                 return sum_
 * 
 *         # 计算逆序数
 *         inversions = 0
 *         fenwick_tree = FenwickTree(len(sorted_nums))
 * 
 *         # 从右向左遍历
 *         for i in range(n - 1, -1, -1):
 *             current_rank = value_to_rank[nums[i]]
 *             inversions += fenwick_tree.query(current_rank - 1)
 *             fenwick_tree.update(current_rank, 1)
 * 
 *         return inversions
 * 
 * # POJ 2352 Stars - 统计左下方星星个数（Python实现）
 * class Stars:
 *     def countStars(self, stars, maxX):
 *         n = len(stars)
 *         result = [0] * n
 * 
 *         # 排序
 *         stars.sort(key=lambda x: (x[1], x[0]))
 * 
 *         # 树状数组类
 *         class FenwickTree:
 *             def __init__(self, size):
 *                 self.size = size
 *                 self.tree = [0] * (size + 1)
 *             
 *             def lowbit(self, x):
 *                 return x & (-x)
 *             
 *             def update(self, i, val):
 *                 if i == 0:
 *                     i = 1  # 处理x=0的情况
 *                 while i <= self.size:
 *                     self.tree[i] += val
 *                     i += self.lowbit(i)
 *             
 *             def query(self, i):
 *                 if i == 0:
 *                     return 0
 *                 sum_ = 0
 *                 while i > 0:
 *                     sum_ += self.tree[i]
 *                     i -= self.lowbit(i)
 *                 return sum_
 * 
 *         fenwick_tree = FenwickTree(maxX + 1)
 * 
 *         for star in stars:
 *             x = star[0]
 *             level = fenwick_tree.query(x)
 *             result[level] += 1
 *             fenwick_tree.update(x, 1)
 * 
 *         return result
 * 
 * # LeetCode 493 - 翻转对（Python实现）
 * class ReversePairs:
 *     def reversePairs(self, nums):
 *         n = len(nums)
 *         if n <= 1:
 *             return 0
 * 
 *         # 离散化
 *         all_numbers = set()
 *         for num in nums:
 *             all_numbers.add(num)
 *             all_numbers.add(2 * num)
 *         
 *         sorted_list = sorted(all_numbers)
 *         value_to_rank = {val: i + 1 for i, val in enumerate(sorted_list)}
 * 
 *         # 树状数组类
 *         class FenwickTree:
 *             def __init__(self, size):
 *                 self.size = size
 *                 self.tree = [0] * (size + 1)
 *             
 *             def lowbit(self, x):
 *                 return x & (-x)
 *             
 *             def update(self, i, val):
 *                 while i <= self.size:
 *                     self.tree[i] += val
 *                     i += self.lowbit(i)
 *             
 *             def query(self, i):
 *                 sum_ = 0
 *                 while i > 0:
 *                     sum_ += self.tree[i]
 *                     i -= self.lowbit(i)
 *                 return sum_
 * 
 *         count = 0
 *         fenwick_tree = FenwickTree(len(sorted_list))
 * 
 *         # 从右向左遍历
 *         for i in range(n - 1, -1, -1):
 *             # 二分查找
 *             target = nums[i] / 2
 *             left, right = 0, len(sorted_list)
 *             while left < right:
 *                 mid = left + (right - left) // 2
 *                 if sorted_list[mid] >= target:
 *                     right = mid
 *                 else:
 *                     left = mid + 1
 *             count += left > 0 and fenwick_tree.query(left) or 0
 * 
 *             # 更新树状数组
 *             current_rank = value_to_rank[nums[i]]
 *             fenwick_tree.update(current_rank, 1)
 * 
 *         return count
 * 
 * # LeetCode 315 - 计算右侧小于当前元素的个数（Python实现）
 * class Solution315:
 *     def countSmaller(self, nums):
 *         n = len(nums)
 *         result = [0] * n
 * 
 *         if n == 0:
 *             return []
 * 
 *         # 离散化
 *         sorted_nums = sorted(list(set(nums)))
 *         value_to_rank = {val: i + 1 for i, val in enumerate(sorted_nums)}
 * 
 *         # 树状数组类
 *         class FenwickTree:
 *             def __init__(self, size):
 *                 self.size = size
 *                 self.tree = [0] * (size + 1)
 *             
 *             def lowbit(self, x):
 *                 return x & (-x)
 *             
 *             def update(self, i, val):
 *                 while i <= self.size:
 *                     self.tree[i] += val
 *                     i += self.lowbit(i)
 *             
 *             def query(self, i):
 *                 sum_ = 0
 *                 while i > 0:
 *                     sum_ += self.tree[i]
 *                     i -= self.lowbit(i)
 *                 return sum_
 * 
 *         fenwick_tree = FenwickTree(len(sorted_nums))
 * 
 *         # 从右向左处理
 *         for i in range(n - 1, -1, -1):
 *             current_rank = value_to_rank[nums[i]]
 *             result[i] = fenwick_tree.query(current_rank - 1)
 *             fenwick_tree.update(current_rank, 1)
 * 
 *         return result
 * 
 * # LeetCode 308 - 二维区域和检索 - 可变（Python实现）
 * class NumMatrix308:
 *     def __init__(self, matrix):
 *         if not matrix or not matrix[0]:
 *             return
 *         
 *         self.m = len(matrix)
 *         self.n = len(matrix[0])
 *         self.tree = [[0] * (self.n + 1) for _ in range(self.m + 1)]
 *         self.nums = [[0] * self.n for _ in range(self.m)]
 * 
 *         for i in range(self.m):
 *             for j in range(self.n):
 *                 self.update(i, j, matrix[i][j])
 *     
 *     def lowbit(self, x):
 *         return x & (-x)
 *     
 *     def updateTree(self, i, j, val):
 *         x = i
 *         while x <= self.m:
 *             y = j
 *             while y <= self.n:
 *                 self.tree[x][y] += val
 *                 y += self.lowbit(y)
 *             x += self.lowbit(x)
 *     
 *     def queryTree(self, i, j):
 *         sum_ = 0
 *         x = i
 *         while x > 0:
 *             y = j
 *             while y > 0:
 *                 sum_ += self.tree[x][y]
 *                 y -= self.lowbit(y)
 *             x -= self.lowbit(x)
 *         return sum_
 *     
 *     def update(self, row, col, val):
 *         delta = val - self.nums[row][col]
 *         self.nums[row][col] = val
 *         self.updateTree(row + 1, col + 1, delta)
 *     
 *     def sumRegion(self, row1, col1, row2, col2):
 *         return (self.queryTree(row2 + 1, col2 + 1) -
 *                 self.queryTree(row1, col2 + 1) -
 *                 self.queryTree(row2 + 1, col1) +
 *                 self.queryTree(row1, col1))
 */



/**
 * POJ 2299 Ultra-QuickSort（求逆序数）
 * 题目链接: http://poj.org/problem?id=2299
 * 
 * 题目描述:
 * 给定一个序列，求该序列的逆序数。逆序数是指序列中满足 i < j 且 a[i] > a[j] 的有序对 (i, j) 的个数。
 * 
 * 解题思路:
 * 使用树状数组结合离散化来高效计算逆序数
 * 1. 离散化：将原始数组中的值映射到连续的较小范围内
 * 2. 从右向左遍历数组，对于每个元素：
 *    - 查询树状数组中比当前元素小的元素个数，即为该元素贡献的逆序数
 *    - 将当前元素插入到树状数组中
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class UltraQuickSort {
    /**
     * 树状数组实现
     */
    class FenwickTree {
        private int[] tree;
        private int size;
        
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1];
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int i, int val) {
            while (i <= size) {
                tree[i] += val;
                i += lowbit(i);
            }
        }
        
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }
    
    /**
     * 计算逆序数
     * @param nums 输入数组
     * @return 逆序数
     */
    public long countInversions(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return 0;
        }
        
        // 离散化
        int[] sortedNums = Arrays.copyOf(nums, n);
        Arrays.sort(sortedNums);
        
        Map<Integer, Integer> valueToRank = new HashMap<>();
        int rank = 1;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sortedNums[i] != sortedNums[i - 1]) {
                valueToRank.put(sortedNums[i], rank++);
            }
        }
        
        // 计算逆序数
        long inversions = 0;
        FenwickTree fenwickTree = new FenwickTree(rank - 1);
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            int currentRank = valueToRank.get(nums[i]);
            // 比当前元素小的元素个数就是当前元素贡献的逆序数
            inversions += fenwickTree.query(currentRank - 1);
            // 将当前元素加入树状数组
            fenwickTree.update(currentRank, 1);
        }
        
        return inversions;
    }
}


/**
 * POJ 2352 Stars（统计左下方星星个数）
 * 题目链接: http://poj.org/problem?id=2352
 * 
 * 题目描述:
 * 在一个平面直角坐标系中，给定n个星星的坐标，每个星星的等级等于坐标严格小于它的星星的数量。
 * 求各个等级的星星数量。
 * 
 * 解题思路:
 * 1. 按照y坐标从小到大排序，y相同则按x从小到大排序
 * 2. 使用树状数组来维护x坐标的出现次数
 * 3. 对于每个星星，查询树状数组中x坐标小于当前星星x坐标的星星数量，即为该星星的等级
 * 4. 更新树状数组，将当前星星的x坐标计数加1
 * 
 * 时间复杂度：O(n log max_x)
 * 空间复杂度：O(max_x)
 */
class Stars {
    /**
     * 树状数组实现
     */
    class FenwickTree {
        private int[] tree;
        private int size;
        
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1]; // 注意x坐标可能从0开始
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int i, int val) {
            // 注意：如果x可能为0，需要将其映射到1，因为树状数组从1开始
            if (i == 0) {
                i = 1;
            }
            while (i <= size) {
                tree[i] += val;
                i += lowbit(i);
            }
        }
        
        public int query(int i) {
            // 注意：如果x可能为0，需要将其映射到1
            if (i == 0) {
                return 0;
            }
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }
    
    /**
     * 统计各个等级的星星数量
     * @param stars 星星坐标数组，每个元素为[x, y]
     * @param maxX 最大x坐标值
     * @return 等级分布数组，result[i]表示等级为i的星星数量
     */
    public int[] countStars(int[][] stars, int maxX) {
        int n = stars.length;
        int[] result = new int[n]; // 最多可能有n个不同等级
        
        // 按照y坐标从小到大排序，y相同则按x从小到大排序
        Arrays.sort(stars, (a, b) -> {
            if (a[1] != b[1]) {
                return a[1] - b[1];
            }
            return a[0] - b[0];
        });
        
        FenwickTree fenwickTree = new FenwickTree(maxX + 1); // x坐标可能从0开始
        
        for (int[] star : stars) {
            int x = star[0];
            // 查询等级：x坐标严格小于当前x的星星数量
            int level = fenwickTree.query(x);
            result[level]++;
            // 更新树状数组
            fenwickTree.update(x, 1);
        }
        
        return result;
    }
}


/**
 * LeetCode 493. 翻转对
 * 题目链接: https://leetcode.cn/problems/reverse-pairs/description/
 * 
 * 题目描述:
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 解题思路:
 * 使用树状数组结合离散化来高效计算重要翻转对
 * 1. 离散化：将原始数组和2倍原始数组的值合并后进行离散化
 * 2. 从右向左遍历数组，对于每个元素：
 *    - 查询树状数组中小于nums[i]/2的元素个数，即为该元素贡献的翻转对数量
 *    - 将当前元素插入到树状数组中
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 */
class ReversePairs {
    /**
     * 树状数组实现
     */
    class FenwickTree {
        private int[] tree;
        private int size;
        
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1];
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int i, int val) {
            while (i <= size) {
                tree[i] += val;
                i += lowbit(i);
            }
        }
        
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }
    
    /**
     * 计算重要翻转对的数量
     * @param nums 输入数组
     * @return 重要翻转对的数量
     */
    public int reversePairs(int[] nums) {
        int n = nums.length;
        if (n <= 1) {
            return 0;
        }
        
        // 离散化：需要处理nums[i]和2*nums[i]
        Set<Long> allNumbers = new HashSet<>();
        for (int num : nums) {
            allNumbers.add((long)num);
            allNumbers.add(2L * num);
        }
        
        // 将所有数字排序并映射到排名
        List<Long> sortedList = new ArrayList<>(allNumbers);
        Collections.sort(sortedList);
        
        Map<Long, Integer> valueToRank = new HashMap<>();
        for (int i = 0; i < sortedList.size(); i++) {
            valueToRank.put(sortedList.get(i), i + 1); // 排名从1开始
        }
        
        int count = 0;
        FenwickTree fenwickTree = new FenwickTree(sortedList.size());
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            // 查找有多少个已处理的元素小于nums[i]/2
            // 使用二分查找找到第一个大于等于nums[i]/2的元素的位置
            int left = 0;
            int right = sortedList.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (sortedList.get(mid) >= (double)nums[i] / 2) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            // 所有小于nums[i]/2的元素个数为left
            count += left > 0 ? fenwickTree.query(left) : 0;
            
            // 将当前元素插入树状数组
            int rank = valueToRank.get((long)nums[i]);
            fenwickTree.update(rank, 1);
        }
        
        return count;
    }
}

/**
 * Your NumArray object will be instantiated and called as such:
 * NumArray obj = new NumArray(nums);
 * obj.update(index,val);
 * int param_2 = obj.sumRange(left,right);
 */


/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/description/
 * 
 * 题目描述:
 * 给你一个整数数组 nums，按要求返回一个新数组 counts。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例 1:
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 示例 2:
 * 输入：nums = [-1]
 * 输出：[0]
 * 
 * 示例 3:
 * 输入：nums = [-1,-1]
 * 输出：[0,0]
 * 
 * 解题思路:
 * 使用树状数组（Fenwick Tree）结合离散化来高效求解逆序对问题
 * 1. 离散化：将原始数组中的值映射到连续的较小范围内
 * 2. 从右向左遍历数组，对于每个元素：
 *    - 查询树状数组中小于当前元素的元素个数
 *    - 将当前元素插入到树状数组中
 * 
 * 时间复杂度：
 * - 离散化：O(n log n)
 * - 树状数组操作：O(n log n)
 * 总时间复杂度：O(n log n)
 * 
 * 空间复杂度：
 * - 树状数组：O(n)
 * - 离散化数组：O(n)
 * 总空间复杂度：O(n)
 * 
 * 优化分析：
 * 这是本题的最优解法之一。其他可能的解法包括归并排序（时间复杂度O(n log n)，但需要更多辅助空间）
 * 和二分搜索树（最坏情况可能退化到O(n²)）。树状数组结合离散化的方法在时间和空间上都是最优的。
 */
class Solution315 {
    /**
     * 树状数组实现
     */
    class FenwickTree {
        private int[] tree;
        private int size;
        
        /**
         * 构造函数
         * @param size 树状数组大小
         */
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1]; // 树状数组从索引1开始
        }
        
        /**
         * lowbit操作：获取x的二进制表示中最低位1所代表的值
         * @param x 输入整数
         * @return 最低位1所代表的值
         */
        private int lowbit(int x) {
            return x & (-x);
        }
        
        /**
         * 单点更新：在索引i的位置增加val
         * @param i 索引位置（从1开始）
         * @param val 增加的值
         */
        public void update(int i, int val) {
            while (i <= size) {
                tree[i] += val;
                i += lowbit(i);
            }
        }
        
        /**
         * 前缀和查询：查询[1, i]区间的元素和
         * @param i 结束索引（从1开始）
         * @return [1, i]区间的元素和
         */
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }
    
    /**
     * 计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组，其中每个元素表示右侧小于当前元素的个数
     */
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();
        
        if (n == 0) {
            return result;
        }
        
        // 离散化处理
        // 1. 复制数组并排序去重
        int[] sortedNums = Arrays.copyOf(nums, n);
        Arrays.sort(sortedNums);
        
        // 2. 创建值到排名的映射
        Map<Integer, Integer> valueToRank = new HashMap<>();
        int rank = 1;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sortedNums[i] != sortedNums[i - 1]) {
                valueToRank.put(sortedNums[i], rank++);
            }
        }
        
        // 3. 使用树状数组从右向左处理
        FenwickTree fenwickTree = new FenwickTree(rank - 1);
        for (int i = n - 1; i >= 0; i--) {
            // 获取当前元素的排名
            int currentRank = valueToRank.get(nums[i]);
            
            // 查询小于当前元素的数量（即排名小于currentRank的元素个数）
            int smallerCount = fenwickTree.query(currentRank - 1);
            
            // 添加结果（注意需要后续反转）
            result.add(smallerCount);
            
            // 将当前元素加入树状数组
            fenwickTree.update(currentRank, 1);
        }
        
        // 反转结果数组，因为我们是从右向左处理的
        Collections.reverse(result);
        
        return result;
    }
}


/**
 * LeetCode 308. 二维区域和检索 - 可变
 * 题目链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/description/
 * 
 * 题目描述:
 * 给你一个二维矩阵 matrix ，需要支持以下操作：
 * 1. update(row, col, val)：将 matrix[row][col] 的值更新为 val。
 * 2. sumRegion(row1, col1, row2, col2)：返回矩阵中从左上角 (row1, col1) 到右下角 (row2, col2) 的矩形区域内所有元素的和。
 * 
 * 示例 1:
 * 输入:
 * ["NumMatrix", "sumRegion", "update", "sumRegion"]
 * [[[[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]], [2, 1, 4, 3], [3, 2, 2], [2, 1, 4, 3]]
 * 输出:
 * [null, 8, null, 10]
 * 
 * 解题思路:
 * 使用二维树状数组来实现二维区域的单点更新和区间查询
 * 
 * 时间复杂度：
 * - 单点更新: O(log m * log n)
 * - 区间查询: O(log m * log n)
 * 
 * 空间复杂度: O(m * n)
 */
class NumMatrix308 {
    private int[][] tree; // 二维树状数组
    private int[][] nums; // 原始矩阵
    private int m; // 行数
    private int n; // 列数
    
    /**
     * 构造函数
     * @param matrix 输入矩阵
     */
    public NumMatrix308(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        
        m = matrix.length;
        n = matrix[0].length;
        tree = new int[m + 1][n + 1]; // 树状数组从索引1开始
        nums = new int[m][n]; // 保存原始矩阵用于计算差分
        
        // 初始化树状数组
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                update(i, j, matrix[i][j]);
            }
        }
    }
    
    /**
     * lowbit操作
     * @param x 输入整数
     * @return 最低位1所代表的值
     */
    private int lowbit(int x) {
        return x & (-x);
    }
    
    /**
     * 单点更新：在(i+1,j+1)位置增加val
     * @param i 行索引（从0开始）
     * @param j 列索引（从0开始）
     * @param val 增加的值
     */
    private void updateTree(int i, int j, int val) {
        for (int x = i; x <= m; x += lowbit(x)) {
            for (int y = j; y <= n; y += lowbit(y)) {
                tree[x][y] += val;
            }
        }
    }
    
    /**
     * 查询从(1,1)到(i,j)的前缀和
     * @param i 结束行索引（从1开始）
     * @param j 结束列索引（从1开始）
     * @return 前缀和
     */
    private int queryTree(int i, int j) {
        int sum = 0;
        for (int x = i; x > 0; x -= lowbit(x)) {
            for (int y = j; y > 0; y -= lowbit(y)) {
                sum += tree[x][y];
            }
        }
        return sum;
    }
    
    /**
     * 更新矩阵中的元素值
     * @param row 行索引
     * @param col 列索引
     * @param val 新的值
     */
    public void update(int row, int col, int val) {
        // 计算差值
        int delta = val - nums[row][col];
        // 更新原始矩阵
        nums[row][col] = val;
        // 更新树状数组
        updateTree(row + 1, col + 1, delta);
    }
    
    /**
     * 查询矩阵区域和
     * @param row1 左上角行索引
     * @param col1 左上角列索引
     * @param row2 右下角行索引
     * @param col2 右下角列索引
     * @return 区域和
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        // 使用容斥原理计算区域和
        return queryTree(row2 + 1, col2 + 1) 
             - queryTree(row1, col2 + 1) 
             - queryTree(row2 + 1, col1) 
             + queryTree(row1, col1);
    }
}