/**
 * 分治法求解数组最大值问题 (C++版本)
 * 
 * 问题描述:
 * 给定一个数组，找出其中的最大值。
 * 
 * 解法思路:
 * 使用分治法，将数组不断二分，直到只有一个元素时直接返回，
 * 然后比较左右两部分的最大值，返回较大者。
 * 
 * 算法特点:
 * 1. 分治策略：将大问题分解为小问题
 * 2. 递归实现：通过递归不断分解问题
 * 3. 合并结果：比较子问题的解得到原问题的解
 * 
 * 时间复杂度分析:
 * T(n) = 2*T(n/2) + O(1)
 * 根据主定理，时间复杂度为 O(n)
 * 
 * 空间复杂度分析:
 * 递归调用栈的深度为 O(log n)
 * 空间复杂度为 O(log n)
 * 
 * 相关题目扩展:
 * 1. LeetCode 53. 最大子数组和 (分治解法)
 * 2. 求解数组中最大值和最小值
 * 3. 求解数组中第k大元素
 * 4. 分治法求解最大子矩阵和
 * 
 * 工程化考量:
 * 1. 异常处理：检查空数组情况
 * 2. 边界处理：处理只有一个元素的数组
 * 3. 性能优化：对于小规模数据可直接遍历
 * 4. 可配置性：可扩展为求解任意范围内的最值
 * 
 * 与标准库对比:
 * C++标准库中std::max_element()等方法
 * 通常使用迭代而非递归，避免栈溢出风险
 * 
 * 语言特性差异:
 * Java: 使用Math.max()函数
 * C++: 使用std::max()函数
 * Python: 使用内置max()函数或自定义比较
 * 
 * 极端场景考虑:
 * 1. 空数组：需要特殊处理
 * 2. 单元素数组：直接返回
 * 3. 大规模数组：可能栈溢出，需改用迭代
 * 4. 所有元素相同：任一元素都是最大值
 * 
 * 调试技巧:
 * 1. 打印递归调用过程中的中间结果
 * 2. 使用断言验证左右子数组的最值正确性
 * 3. 性能测试：比较不同规模数据的执行时间
 */

// 由于当前环境可能不支持完整的C++标准库，这里提供概念性代码
// 实际编译时需要包含正确的头文件

/*
 * 分治法求解数组指定范围内的最大值
 * @param arr 数组
 * @param l 左边界（包含）
 * @param r 右边界（包含）
 * @return 指定范围内的最大值
 */
int f(int arr[], int l, int r) {
    // 基本情况：只有一个元素时直接返回
    if (l == r) {
        return arr[l];
    }
    
    // 分解：计算中点，将数组分为两部分
    int m = (l + r) / 2;
    
    // 递归求解：分别求左右两部分的最大值
    int lmax = f(arr, l, m);
    int rmax = f(arr, m + 1, r);
    
    // 合并：返回左右两部分最大值中的较大者
    return (lmax > rmax) ? lmax : rmax;
}

/*
 * 入口方法，求数组中的最大值
 * @param arr 输入数组
 * @param size 数组大小
 * @return 数组中的最大值
 */
int maxValue(int arr[], int size) {
    // 异常处理：检查数组是否为空
    if (arr == 0 || size <= 0) {
        return -1; // 简化处理，实际应抛出异常
    }
    
    // 调用分治方法求解
    return f(arr, 0, size - 1);
}

/*
 * 以下是完整的C++实现（需要适当编译环境）
 */
 
#include <iostream>
#include <vector>
#include <algorithm>
#include <stdexcept>
#include <climits>
#include <cstdlib>
#include <ctime>
#include <cfloat>
#include <cmath>

using namespace std;

class GetMaxValue {
public:
    // 入口方法，求数组中的最大值
    static int maxValue(const vector<int>& arr) {
        // 异常处理：检查数组是否为空
        if (arr.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        // 调用分治方法求解
        return f(arr, 0, arr.size() - 1);
    }

private:
    // 分治法求解数组指定范围内的最大值
    static int f(const vector<int>& arr, int l, int r) {
        // 基本情况：只有一个元素时直接返回
        if (l == r) {
            return arr[l];
        }
        
        // 分解：计算中点，将数组分为两部分
        int m = (l + r) / 2;
        
        // 递归求解：分别求左右两部分的最大值
        int lmax = f(arr, l, m);
        int rmax = f(arr, m + 1, r);
        
        // 合并：返回左右两部分最大值中的较大者
        return max(lmax, rmax);
    }

public:
    // ==================== 题目1：LeetCode 53. 最大子数组和 (分治解法) ====================
    /**
     * 题目来源：LeetCode 53. Maximum Subarray
     * 题目链接：https://leetcode.com/problems/maximum-subarray/
     * 中文链接：https://leetcode.cn/problems/maximum-subarray/
     * 
     * 题目描述：
     * 给定一个整数数组 nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 
     * 示例 1：
     * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
     * 输出：6
     * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6。
     * 
     * 时间复杂度：O(n*log n) - 分治法
     * 空间复杂度：O(log n) - 递归栈
     * 
     * 最优解：Kadane算法，时间O(n)，空间O(1)
     */
    static int maxSubArray(const vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        return maxSubArrayDivide(nums, 0, nums.size() - 1);
    }

private:
    static int maxSubArrayDivide(const vector<int>& nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }

        int mid = left + (right - left) / 2;
        int leftMax = maxSubArrayDivide(nums, left, mid);
        int rightMax = maxSubArrayDivide(nums, mid + 1, right);

        // 计算跨越中点的最大子数组和
        int leftCrossMax = INT_MIN;
        int leftSum = 0;
        for (int i = mid; i >= left; i--) {
            leftSum += nums[i];
            leftCrossMax = max(leftCrossMax, leftSum);
        }

        int rightCrossMax = INT_MIN;
        int rightSum = 0;
        for (int i = mid + 1; i <= right; i++) {
            rightSum += nums[i];
            rightCrossMax = max(rightCrossMax, rightSum);
        }

        int crossMax = leftCrossMax + rightCrossMax;
        return max({leftMax, rightMax, crossMax});
    }

public:
    /**
     * 最优解：Kadane算法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    static int maxSubArrayOptimal(const vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }

        int maxSum = nums[0];
        int curSum = nums[0];

        for (size_t i = 1; i < nums.size(); i++) {
            curSum = max(nums[i], curSum + nums[i]);
            maxSum = max(maxSum, curSum);
        }

        return maxSum;
    }

    // ==================== 题目2：LeetCode 169. 多数元素 (分治解法) ====================
    /**
     * 题目来源：LeetCode 169. Majority Element
     * 题目链接：https://leetcode.com/problems/majority-element/
     * 中文链接：https://leetcode.cn/problems/majority-element/
     * 
     * 题目描述：
     * 给定一个大小为 n 的数组 nums，返回其中的多数元素。
     * 多数元素是指在数组中出现次数大于 ⌊n/2⌋ 的元素。
     * 
     * 时间复杂度：O(n*log n) - 分治法
     * 空间复杂度：O(log n)
     * 
     * 最优解：摩尔投票算法，时间O(n)，空间O(1)
     */
    static int majorityElement(const vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        return majorityElementDivide(nums, 0, nums.size() - 1);
    }

private:
    static int majorityElementDivide(const vector<int>& nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }

        int mid = left + (right - left) / 2;
        int leftMajor = majorityElementDivide(nums, left, mid);
        int rightMajor = majorityElementDivide(nums, mid + 1, right);

        if (leftMajor == rightMajor) {
            return leftMajor;
        }

        int leftCount = countInRange(nums, leftMajor, left, right);
        int rightCount = countInRange(nums, rightMajor, left, right);

        return leftCount > rightCount ? leftMajor : rightMajor;
    }

    static int countInRange(const vector<int>& nums, int target, int left, int right) {
        int count = 0;
        for (int i = left; i <= right; i++) {
            if (nums[i] == target) {
                count++;
            }
        }
        return count;
    }

public:
    /**
     * 最优解：摩尔投票算法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    static int majorityElementOptimal(const vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }

        int candidate = nums[0];
        int count = 1;

        for (size_t i = 1; i < nums.size(); i++) {
            if (count == 0) {
                candidate = nums[i];
                count = 1;
            } else if (nums[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }

    // ==================== 题目3：LeetCode 215. 数组中的第K个最大元素 ====================
    /**
     * 题目来源：LeetCode 215. Kth Largest Element in an Array
     * 题目链接：https://leetcode.com/problems/kth-largest-element-in-an-array/
     * 中文链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
     * 
     * 题目描述：
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * 
     * 快速选择算法（基于分治思想）
     * 平均时间复杂度：O(n)
     * 最坏时间复杂度：O(n^2)
     * 空间复杂度：O(log n)
     */
    static int findKthLargest(vector<int>& nums, int k) {
        if (nums.empty() || k < 1 || k > (int)nums.size()) {
            throw invalid_argument("参数非法");
        }
        srand(time(NULL));
        return quickSelect(nums, 0, nums.size() - 1, nums.size() - k);
    }

private:
    static int quickSelect(vector<int>& nums, int left, int right, int k) {
        if (left == right) {
            return nums[left];
        }

        int pivotIndex = left + rand() % (right - left + 1);
        pivotIndex = partition(nums, left, right, pivotIndex);

        if (k == pivotIndex) {
            return nums[k];
        } else if (k < pivotIndex) {
            return quickSelect(nums, left, pivotIndex - 1, k);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, k);
        }
    }

    static int partition(vector<int>& nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];
        swap(nums[pivotIndex], nums[right]);

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums[storeIndex], nums[i]);
                storeIndex++;
            }
        }

        swap(nums[storeIndex], nums[right]);
        return storeIndex;
    }

public:
    // ==================== 题目4：LeetCode 240. 搜索二维矩阵 II ====================
    /**
     * 题目来源：LeetCode 240. Search a 2D Matrix II
     * 链接：https://leetcode.com/problems/search-a-2d-matrix-ii/
     * 中文：https://leetcode.cn/problems/search-a-2d-matrix-ii/
     * 
     * 最优解：从右上角或左下角搜索
     * 时间复杂度：O(m+n)
     * 空间复杂度：O(1)
     */
    static bool searchMatrix(const vector<vector<int>>& matrix, int target) {
        if (matrix.empty() || matrix[0].empty()) {
            return false;
        }

        int row = 0;
        int col = matrix[0].size() - 1;

        while (row < (int)matrix.size() && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] > target) {
                col--;
            } else {
                row++;
            }
        }

        return false;
    }
    
    // ==================== 补充题目5：归并排序 ====================
    /**
     * 题目来源：经典排序算法
     * 
     * 题目描述：
     * 实现归并排序算法，将一个数组排序。
     * 
     * 解题思路：
     * 1. 将数组分成两半，分别排序
     * 2. 合并两个已排序的子数组
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    static void mergeSort(vector<int>& nums) {
        if (nums.empty()) {
            return;
        }
        vector<int> temp(nums.size());
        mergeSortHelper(nums, 0, nums.size() - 1, temp);
    }

private:
    static void mergeSortHelper(vector<int>& nums, int left, int right, vector<int>& temp) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(nums, left, mid, temp);
            mergeSortHelper(nums, mid + 1, right, temp);
            merge(nums, left, mid, right, temp);
        }
    }

    static void merge(vector<int>& nums, int left, int mid, int right, vector<int>& temp) {
        int i = left;       // 左半部分起始索引
        int j = mid + 1;    // 右半部分起始索引
        int k = left;       // 临时数组起始索引

        // 合并两个子数组
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }

        // 处理剩余元素
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        while (j <= right) {
            temp[k++] = nums[j++];
        }

        // 将临时数组的元素复制回原数组
        for (int p = left; p <= right; p++) {
            nums[p] = temp[p];
        }
    }

public:
    // ==================== 补充题目6：二分查找 ====================
    /**
     * 题目来源：经典搜索算法
     * 
     * 题目描述：
     * 在一个排序数组中查找目标值，如果找到返回索引，否则返回-1。
     * 
     * 解题思路：
     * 1. 将区间不断二分，比较中间元素与目标值
     * 2. 根据比较结果调整搜索区间
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(log n) - 递归实现
     */
    static int binarySearch(const vector<int>& nums, int target) {
        if (nums.empty()) {
            return -1;
        }
        return binarySearchHelper(nums, target, 0, nums.size() - 1);
    }

private:
    static int binarySearchHelper(const vector<int>& nums, int target, int left, int right) {
        if (left > right) {
            return -1;  // 未找到目标值
        }

        int mid = left + (right - left) / 2;  // 避免整数溢出

        if (nums[mid] == target) {
            return mid;  // 找到目标值
        } else if (nums[mid] > target) {
            return binarySearchHelper(nums, target, left, mid - 1);  // 在左半部分搜索
        } else {
            return binarySearchHelper(nums, target, mid + 1, right);  // 在右半部分搜索
        }
    }

public:
    /**
     * 二分查找最优解：迭代实现
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    static int binarySearchOptimal(const vector<int>& nums, int target) {
        if (nums.empty()) {
            return -1;
        }

        int left = 0;
        int right = nums.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return -1;  // 未找到目标值
    }

    // ==================== 补充题目7：快速幂算法 ====================
    /**
     * 题目来源：经典算法题
     * 
     * 题目描述：
     * 计算 a 的 n 次方，要求时间复杂度优于 O(n)。
     * 
     * 解题思路：
     * 1. 使用分治法，将 a^n 分解为 (a^(n/2))^2
     * 2. 递归计算子问题
     * 
     * 时间复杂度：O(log n)
     * 空间复杂度：O(log n) - 递归栈
     */
    static double quickPow(double a, int n) {
        // 处理特殊情况
        if (n == 0) {
            return 1.0;
        }
        if (a == 0.0) {
            return 0.0;
        }

        // 处理负数指数
        if (n < 0) {
            a = 1.0 / a;
            // 处理整数溢出
            if (n == INT_MIN) {
                return a * quickPow(a, -(n + 1));
            }
            n = -n;
        }

        return quickPowHelper(a, n);
    }

private:
    static double quickPowHelper(double a, int n) {
        if (n == 0) {
            return 1.0;
        }

        double half = quickPowHelper(a, n / 2);
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * a;
        }
    }

public:
    /**
     * 快速幂最优解：迭代实现
     * 时间复杂度：O(log n)
     * 空间复杂度：O(1)
     */
    static double quickPowOptimal(double a, int n) {
        // 处理特殊情况
        if (n == 0) {
            return 1.0;
        }
        if (a == 0.0) {
            return 0.0;
        }

        long long exponent = n;  // 避免整数溢出
        if (n < 0) {
            a = 1.0 / a;
            exponent = -exponent;
        }

        double result = 1.0;
        double current = a;

        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result *= current;
            }
            current *= current;
            exponent /= 2;
        }

        return result;
    }

    // ==================== 补充题目8：最大子矩阵和 ====================
    /**
     * 题目来源：经典算法题
     * 
     * 题目描述：
     * 给定一个二维矩阵，找出其中和最大的子矩阵。
     * 
     * 解题思路：
     * 1. 将问题转化为一维最大子数组和问题
     * 2. 枚举左右边界，计算每一行的前缀和
     * 3. 使用Kadane算法求解一维最大子数组和
     * 
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n)
     */
    static int maxSubMatrix(const vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            throw invalid_argument("矩阵不能为空");
        }

        int n = matrix.size();      // 行数
        int m = matrix[0].size();   // 列数
        int maxSum = INT_MIN;

        // 枚举左右边界
        for (int left = 0; left < m; left++) {
            vector<int> rowSum(n, 0);  // 记录每一行的和
            
            for (int right = left; right < m; right++) {
                // 计算每一行在左右边界内的和
                for (int i = 0; i < n; i++) {
                    rowSum[i] += matrix[i][right];
                }
                
                // 使用Kadane算法求解一维最大子数组和
                int currentSum = 0;
                int currentMax = INT_MIN;
                
                for (int sum : rowSum) {
                    currentSum = max(sum, currentSum + sum);
                    currentMax = max(currentMax, currentSum);
                }
                
                maxSum = max(maxSum, currentMax);
            }
        }
        
        return maxSum;
    }

    // ==================== 补充题目9：Strassen矩阵乘法 ====================
    /**
     * 题目来源：经典算法题
     * 
     * 题目描述：
     * 实现Strassen矩阵乘法算法，优化传统的O(n^3)矩阵乘法。
     * 
     * 解题思路：
     * 1. 将矩阵分成四个子矩阵
     * 2. 计算7个中间矩阵（而非传统算法的8个乘法）
     * 3. 通过中间矩阵计算结果矩阵的四个子矩阵
     * 
     * 时间复杂度：O(n^log2(7)) ≈ O(n^2.81)
     * 空间复杂度：O(n^2)
     */
    static vector<vector<int>> strassenMultiply(vector<vector<int>>& A, vector<vector<int>>& B) {
        int n = A.size();
        // 确保矩阵是方阵且大小为2的幂
        int size = 1;
        while (size < n) {
            size <<= 1;
        }
        
        // 扩展矩阵到2的幂大小
        vector<vector<int>> A_ext(size, vector<int>(size, 0));
        vector<vector<int>> B_ext(size, vector<int>(size, 0));
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A_ext[i][j] = A[i][j];
                B_ext[i][j] = B[i][j];
            }
        }
        
        vector<vector<int>> C_ext = strassenMultiplyHelper(A_ext, B_ext, size);
        
        // 裁剪回原始大小
        vector<vector<int>> C(n, vector<int>(n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = C_ext[i][j];
            }
        }
        
        return C;
    }

private:
    static vector<vector<int>> strassenMultiplyHelper(const vector<vector<int>>& A, const vector<vector<int>>& B, int size) {
        if (size == 1) {
            vector<vector<int>> C(1, vector<int>(1));
            C[0][0] = A[0][0] * B[0][0];
            return C;
        }
        
        int newSize = size / 2;
        
        // 分割矩阵
        vector<vector<int>> A11(newSize, vector<int>(newSize));
        vector<vector<int>> A12(newSize, vector<int>(newSize));
        vector<vector<int>> A21(newSize, vector<int>(newSize));
        vector<vector<int>> A22(newSize, vector<int>(newSize));
        
        vector<vector<int>> B11(newSize, vector<int>(newSize));
        vector<vector<int>> B12(newSize, vector<int>(newSize));
        vector<vector<int>> B21(newSize, vector<int>(newSize));
        vector<vector<int>> B22(newSize, vector<int>(newSize));
        
        // 填充子矩阵
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                A11[i][j] = A[i][j];
                A12[i][j] = A[i][j + newSize];
                A21[i][j] = A[i + newSize][j];
                A22[i][j] = A[i + newSize][j + newSize];
                
                B11[i][j] = B[i][j];
                B12[i][j] = B[i][j + newSize];
                B21[i][j] = B[i + newSize][j];
                B22[i][j] = B[i + newSize][j + newSize];
            }
        }
        
        // 计算7个中间矩阵
        vector<vector<int>> temp1 = addMatrices(A11, A22);
        vector<vector<int>> temp2 = addMatrices(B11, B22);
        vector<vector<int>> M1 = strassenMultiplyHelper(temp1, temp2, newSize);
        
        vector<vector<int>> temp3 = addMatrices(A21, A22);
        vector<vector<int>> M2 = strassenMultiplyHelper(temp3, B11, newSize);
        
        vector<vector<int>> temp4 = subtractMatrices(B12, B22);
        vector<vector<int>> M3 = strassenMultiplyHelper(A11, temp4, newSize);
        
        vector<vector<int>> temp5 = subtractMatrices(B21, B11);
        vector<vector<int>> M4 = strassenMultiplyHelper(A22, temp5, newSize);
        
        vector<vector<int>> temp6 = addMatrices(A11, A12);
        vector<vector<int>> M5 = strassenMultiplyHelper(temp6, B22, newSize);
        
        vector<vector<int>> temp7 = subtractMatrices(A21, A11);
        vector<vector<int>> temp8 = addMatrices(B11, B12);
        vector<vector<int>> M6 = strassenMultiplyHelper(temp7, temp8, newSize);
        
        vector<vector<int>> temp9 = subtractMatrices(A12, A22);
        vector<vector<int>> temp10 = addMatrices(B21, B22);
        vector<vector<int>> M7 = strassenMultiplyHelper(temp9, temp10, newSize);
        
        // 计算结果矩阵的子矩阵
        vector<vector<int>> temp11 = addMatrices(M1, M4);
        vector<vector<int>> temp12 = subtractMatrices(temp11, M5);
        vector<vector<int>> C11 = addMatrices(temp12, M7);
        vector<vector<int>> C12 = addMatrices(M3, M5);
        vector<vector<int>> C21 = addMatrices(M2, M4);
        vector<vector<int>> temp13 = addMatrices(M1, M3);
        vector<vector<int>> temp14 = subtractMatrices(temp13, M2);
        vector<vector<int>> C22 = addMatrices(temp14, M6);
        
        // 合并结果矩阵
        vector<vector<int>> C(size, vector<int>(size));
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C[i][j] = C11[i][j];
                C[i][j + newSize] = C12[i][j];
                C[i + newSize][j] = C21[i][j];
                C[i + newSize][j + newSize] = C22[i][j];
            }
        }
        
        return C;
    }
    
    static vector<vector<int>> addMatrices(const vector<vector<int>>& A, const vector<vector<int>>& B) {
        int n = A.size();
        vector<vector<int>> C(n, vector<int>(n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }
    
    static vector<vector<int>> subtractMatrices(const vector<vector<int>>& A, const vector<vector<int>>& B) {
        int n = A.size();
        vector<vector<int>> C(n, vector<int>(n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

public:
    // ==================== 补充题目10：最近点对问题 ====================
    /**
     * 题目来源：经典算法题
     * 
     * 题目描述：
     * 给定平面上的n个点，找出距离最近的一对点。
     * 
     * 解题思路：
     * 1. 按x坐标排序所有点
     * 2. 使用分治法递归求解左右两部分的最近点对
     * 3. 处理跨越中间线的最近点对
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    static double closestPair(vector<pair<double, double>>& points) {
        if (points.size() < 2) {
            throw invalid_argument("至少需要两个点");
        }
        
        // 按x坐标排序
        sort(points.begin(), points.end());
        
        // 创建一个按y坐标排序的数组，用于后续处理
        vector<pair<double, double>> pointsSortedByY = points;
        sort(pointsSortedByY.begin(), pointsSortedByY.end(), [](const pair<double, double>& a, const pair<double, double>& b) {
            return a.second < b.second;
        });
        
        return closestPairHelper(points, 0, points.size() - 1, pointsSortedByY);
    }

private:
    static double closestPairHelper(vector<pair<double, double>>& points, int left, int right, vector<pair<double, double>>& pointsSortedByY) {
        // 基本情况：少量点时直接计算
        if (right - left <= 3) {
            double minDist = std::numeric_limits<double>::max();
            for (int i = left; i <= right; i++) {
                for (int j = i + 1; j <= right; j++) {
                    minDist = min(minDist, distance(points[i], points[j]));
                }
            }
            return minDist;
        }
        
        // 分解问题
        int mid = left + (right - left) / 2;
        double midX = points[mid].first;
        
        // 递归求解左右两部分的最近点对
        vector<pair<double, double>> leftPointsSortedByY;
        vector<pair<double, double>> rightPointsSortedByY;
        
        for (auto& point : pointsSortedByY) {
            if (point.first <= midX) {
                leftPointsSortedByY.push_back(point);
            } else {
                rightPointsSortedByY.push_back(point);
            }
        }
        
        double leftMinDist = closestPairHelper(points, left, mid, leftPointsSortedByY);
        double rightMinDist = closestPairHelper(points, mid + 1, right, rightPointsSortedByY);
        
        // 合并结果
        double minDist = min(leftMinDist, rightMinDist);
        
        // 处理跨越中间线的点对
        vector<pair<double, double>> strip;
        for (auto& point : pointsSortedByY) {
            if (abs(point.first - midX) < minDist) {
                strip.push_back(point);
            }
        }
        
        // 在带状区域中寻找可能的更近点对
        for (size_t i = 0; i < strip.size(); i++) {
            for (size_t j = i + 1; j < strip.size() && (strip[j].second - strip[i].second) < minDist; j++) {
                minDist = min(minDist, distance(strip[i], strip[j]));
            }
        }
        
        return minDist;
    }
    
    static double distance(const pair<double, double>& p1, const pair<double, double>& p2) {
        double dx = p1.first - p2.first;
        double dy = p1.second - p2.second;
        return std::sqrt(dx * dx + dy * dy);
    }

public:
    // ==================== 补充题目11：Karatsuba大整数乘法 ====================
    /**
     * 题目来源：经典算法题
     * 
     * 题目描述：
     * 实现Karatsuba算法进行大整数乘法运算
     * 
     * 解题思路：
     * 1. 将两个大整数分别拆分为高位和低位两部分
     * 2. 使用分治思想，将一次4次乘法减少为3次乘法
     * 3. 通过巧妙的组合方式计算结果
     * 
     * 时间复杂度：O(n^log₂3) ≈ O(n^1.585)
     * 空间复杂度：O(n)
     * 
     * 是否最优解：比传统O(n²)算法更优，但存在更优的FFT算法O(n log n)
     */
    static string karatsubaMultiply(const string& num1, const string& num2) {
        // 处理特殊情况
        if (num1 == "0" || num2 == "0") {
            return "0";
        }
        
        // 调用递归辅助函数
        return karatsubaHelper(num1, num2);
    }

private:
    static string karatsubaHelper(const string& num1, const string& num2) {
        // 基本情况：小数字直接计算
        if (num1.length() < 10 || num2.length() < 10) {
            return multiplyStrings(num1, num2);
        }
        
        // 使两个数字长度相等，用0填充较短的数字
        size_t n = max(num1.length(), num2.length());
        size_t half = (n + 1) / 2;
        
        // 补齐长度
        string n1 = string(max(0, (int)(n - num1.length())), '0') + num1;
        string n2 = string(max(0, (int)(n - num2.length())), '0') + num2;
        
        // 将数字分为两部分
        string a = n1.substr(0, n - half);
        string b = n1.substr(n - half);
        string c = n2.substr(0, n - half);
        string d = n2.substr(n - half);
        
        // 递归计算三个乘积
        string ac = karatsubaHelper(a, c);
        string bd = karatsubaHelper(b, d);
        string abcd = karatsubaHelper(addStrings(a, b), addStrings(c, d));
        
        // 计算ad + bc = (a+b)(c+d) - ac - bd
        string adPlusBc = subtractStrings(subtractStrings(abcd, ac), bd);
        
        // 组合结果
        // result = ac * 10^(2*half) + (ad+bc) * 10^half + bd
        string result = addStrings(addStrings(ac + string(2 * half, '0'), adPlusBc + string(half, '0')), bd);
        
        // 移除前导零
        return removeLeadingZeros(result);
    }
    
    static string multiplyStrings(const string& num1, const string& num2) {
        vector<int> result(num1.length() + num2.length(), 0);
        
        for (int i = num1.length() - 1; i >= 0; i--) {
            for (int j = num2.length() - 1; j >= 0; j--) {
                int mul = (num1[i] - '0') * (num2[j] - '0');
                int p1 = i + j, p2 = i + j + 1;
                int sum = mul + result[p2];
                
                result[p2] = sum % 10;
                result[p1] += sum / 10;
            }
        }
        
        string sb = "";
        for (int p : result) {
            if (!(sb.length() == 0 && p == 0)) {
                sb += to_string(p);
            }
        }
        return sb.length() == 0 ? "0" : sb;
    }
    
    static string addStrings(const string& num1, const string& num2) {
        string sb = "";
        int carry = 0;
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        
        while (i >= 0 || j >= 0 || carry != 0) {
            int sum = carry;
            if (i >= 0) sum += num1[i--] - '0';
            if (j >= 0) sum += num2[j--] - '0';
            sb = to_string(sum % 10) + sb;
            carry = sum / 10;
        }
        
        return sb;
    }
    
    static string subtractStrings(const string& num1, const string& num2) {
        string sb = "";
        int carry = 0;
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        
        while (i >= 0 || j >= 0) {
            int digit1 = (i >= 0) ? num1[i--] - '0' : 0;
            int digit2 = (j >= 0) ? num2[j--] - '0' : 0;
            int diff = digit1 - digit2 - carry;
            
            if (diff < 0) {
                diff += 10;
                carry = 1;
            } else {
                carry = 0;
            }
            
            sb = to_string(diff) + sb;
        }
        
        return removeLeadingZeros(sb);
    }
    
    static string removeLeadingZeros(const string& str) {
        size_t i = 0;
        while (i < str.length() - 1 && str[i] == '0') {
            i++;
        }
        return str.substr(i);
    }

public:
    // 测试补充题目的方法
    static void testAdditionalProblems() {
        cout << "\n========== 补充题目测试 ==========\n";
        
        // 测试归并排序
        cout << "\n1. 归并排序测试: " << endl;
        vector<int> numsMerge = {9, 3, 7, 1, 5, 8, 2, 6, 4};
        cout << "排序前: ";
        printVector(numsMerge);
        mergeSort(numsMerge);
        cout << "排序后: ";
        printVector(numsMerge);
        
        // 测试二分查找
        cout << "\n2. 二分查找测试: " << endl;
        vector<int> numsBinary = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        cout << "查找5: 索引 = " << binarySearch(numsBinary, 5) << endl;
        cout << "查找10: 索引 = " << binarySearchOptimal(numsBinary, 10) << endl;
        
        // 测试快速幂
        cout << "\n3. 快速幂测试: " << endl;
        cout << "2^10 = " << quickPow(2, 10) << endl;
        cout << "2^-2 = " << quickPowOptimal(2, -2) << endl;
        
        // 测试最大子矩阵和
        cout << "\n4. 最大子矩阵和测试: " << endl;
        vector<vector<int>> matrix = {
            {1, 2, -1, -4, -20},
            {-8, -3, 4, 2, 1},
            {3, 8, 10, 1, 3},
            {-4, -1, 1, 7, -6}
        };
        cout << "最大子矩阵和 = " << maxSubMatrix(matrix) << endl;
        
        // 测试最近点对问题
        cout << "\n5. 最近点对问题测试: " << endl;
        vector<pair<double, double>> points = {{0, 0}, {3, 0}, {0, 4}, {1, 1}, {2, 2}};
        cout << "最近点对距离 = " << closestPair(points) << endl;
        
        // 测试Karatsuba大整数乘法
        cout << "\n6. Karatsuba大整数乘法测试: " << endl;
        cout << "123456789 * 987654321 = " << karatsubaMultiply("123456789", "987654321") << endl;
        cout << "0 * 12345 = " << karatsubaMultiply("0", "12345") << endl;
        cout << "9999999999 * 9999999999 = " << karatsubaMultiply("9999999999", "9999999999") << endl;
    }
    
private:
    static void printVector(const vector<int>& vec) {
        for (int num : vec) {
            cout << num << " ";
        }
        cout << endl;
    }
};

// 测试方法
int main() {
    cout << "========== 原始测试：分治求数组最大值 ==========" << endl;
    
    // 测试用例1：普通数组
    vector<int> arr1 = { 3, 8, 7, 6, 4, 5, 1, 2 };
    cout << "数组最大值 : " << GetMaxValue::maxValue(arr1) << endl;
    
    // 测试用例2：单元素数组
    vector<int> arr2 = { 42 };
    cout << "单元素数组最大值 : " << GetMaxValue::maxValue(arr2) << endl;
    
    // 测试用例3：负数数组
    vector<int> arr3 = { -5, -2, -8, -1 };
    cout << "负数数组最大值 : " << GetMaxValue::maxValue(arr3) << endl;
    
    // 测试用例4：相同元素数组
    vector<int> arr4 = { 5, 5, 5, 5 };
    cout << "相同元素数组最大值 : " << GetMaxValue::maxValue(arr4) << endl;
    
    // 测试用例5：大规模数组
    vector<int> arr5(10000);
    for (int i = 0; i < 10000; i++) {
        arr5[i] = i;
    }
    cout << "大规模数组最大值 : " << GetMaxValue::maxValue(arr5) << endl;
    
    // 异常测试：空数组
    try {
        vector<int> arr6;
        GetMaxValue::maxValue(arr6);
    } catch (const invalid_argument& e) {
        cout << "空数组异常处理: " << e.what() << endl;
    }
    
    cout << "\n========== 题目1测试：LeetCode 53 最大子数组和 ==========" << endl;
    vector<int> nums1 = {-2,1,-3,4,-1,2,1,-5,4};
    cout << "分治法结果: " << GetMaxValue::maxSubArray(nums1) << endl;
    cout << "最优解(Kadane)结果: " << GetMaxValue::maxSubArrayOptimal(nums1) << endl;
    
    vector<int> nums2 = {5,4,-1,7,8};
    cout << "测试用例2: " << GetMaxValue::maxSubArrayOptimal(nums2) << endl;
    
    cout << "\n========== 题目2测试：LeetCode 169 多数元素 ==========" << endl;
    vector<int> nums3 = {3,2,3};
    cout << "分治法结果: " << GetMaxValue::majorityElement(nums3) << endl;
    cout << "最优解(摩尔投票)结果: " << GetMaxValue::majorityElementOptimal(nums3) << endl;
    
    vector<int> nums4 = {2,2,1,1,1,2,2};
    cout << "测试用例2: " << GetMaxValue::majorityElementOptimal(nums4) << endl;
    
    cout << "\n========== 题目3测试：LeetCode 215 第K大元素 ==========" << endl;
    vector<int> nums5 = {3,2,1,5,6,4};
    cout << "第2大元素: " << GetMaxValue::findKthLargest(nums5, 2) << endl;
    
    vector<int> nums6 = {3,2,3,1,2,4,5,5,6};
    cout << "第4大元素: " << GetMaxValue::findKthLargest(nums6, 4) << endl;
    
    cout << "\n========== 题目4测试：LeetCode 240 搜索矩阵 ==========" << endl;
    vector<vector<int>> matrix = {
        {1,4,7,11,15},
        {2,5,8,12,19},
        {3,6,9,16,22},
        {10,13,14,17,24},
        {18,21,23,26,30}
    };
    cout << "搜索5: " << (GetMaxValue::searchMatrix(matrix, 5) ? "true" : "false") << endl;
    cout << "搜索20: " << (GetMaxValue::searchMatrix(matrix, 20) ? "true" : "false") << endl;
    
    // 补充题目测试
    GetMaxValue::testAdditionalProblems();
    
    return 0;
}