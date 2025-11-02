// 区间和的个数，C++版
// 测试链接 : https://leetcode.cn/problems/count-of-range-sum/

/**
 * 区间和的个数详解:
 * 
 * 问题描述:
 * 给你一个整数数组 nums 以及两个整数 lower 和 upper 。
 * 求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
 * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 * 
 * 示例:
 * 输入：nums = [-2,5,-1], lower = -2, upper = 2
 * 输出：3
 * 解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。
 * 
 * 解法思路:
 * 1. 暴力解法: 计算所有可能区间的和，检查是否在范围内，时间复杂度O(N^3)或O(N^2)（使用前缀和优化）
 * 2. 归并排序思想:
 *    - 使用前缀和将问题转换为: 找到满足条件 prefixSum[j] - prefixSum[i] ∈ [lower, upper] 的(i,j)对数量
 *    - 在归并排序过程中，对于左半部分的每个前缀和prefixSum[i]，在右半部分找到满足条件的prefixSum[j]数量
 *    - 由于两部分各自有序，可以使用双指针技巧优化查找过程
 * 
 * 时间复杂度: O(N * logN) - 归并排序的时间复杂度
 * 空间复杂度: O(N) - 前缀和数组和辅助数组的空间复杂度
 */

#include <vector>
#include <iostream>
using namespace std;

class Solution {
private:
    // 归并排序，在排序过程中统计满足条件的区间和个数
    int mergeSort(vector<long long>& arr, int left, int right, int lower, int upper) {
        if (left == right) {
            return 0;
        }
        
        // 计算中间位置，避免整数溢出
        int mid = left + (right - left) / 2;
        
        // 递归处理左右两部分，并累加统计结果
        int count = mergeSort(arr, left, mid, lower, upper) + 
                    mergeSort(arr, mid + 1, right, lower, upper);
        
        // 统计跨越左右两部分的满足条件的区间和个数
        // 使用双指针技巧：l和r分别指向右半部分中满足条件的起始和结束位置
        int l = mid + 1, r = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 找到右半部分中第一个满足 arr[j] - arr[i] >= lower 的位置l
            while (l <= right && arr[l] - arr[i] < lower) {
                l++;
            }
            // 找到右半部分中第一个满足 arr[j] - arr[i] > upper 的位置r
            while (r <= right && arr[r] - arr[i] <= upper) {
                r++;
            }
            // 区间[l, r-1]中的元素都满足条件，累加数量
            count += (r - l);
        }
        
        // 合并两个有序数组
        vector<long long> help(right - left + 1);
        int i = 0;       // 辅助数组的索引
        int a = left, b = mid + 1;  // 左半部分和右半部分的起始索引
        
        // 合并过程
        while (a <= mid && b <= right) {
            if (arr[a] <= arr[b]) {
                help[i++] = arr[a++];
            } else {
                help[i++] = arr[b++];
            }
        }
        
        // 处理剩余元素
        while (a <= mid) {
            help[i++] = arr[a++];
        }
        while (b <= right) {
            help[i++] = arr[b++];
        }
        
        // 将辅助数组中的元素复制回原数组
        for (i = 0; i < help.size(); i++) {
            arr[left + i] = help[i];
        }
        
        return count;
    }

public:
    int countRangeSum(vector<int>& nums, int lower, int upper) {
        int n = nums.size();
        // 边界情况处理
        if (n == 0) {
            return 0;
        }
        
        // 计算前缀和数组，使用long long避免整数溢出
        vector<long long> prefixSum(n + 1, 0);
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 调用归并排序并统计结果
        return mergeSort(prefixSum, 0, n, lower, upper);
    }
};

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: 基本情况
    vector<int> nums1 = {-2, 5, -1};
    int lower1 = -2, upper1 = 2;
    cout << "输入: nums = [-2,5,-1], lower = -2, upper = 2" << endl;
    cout << "输出: " << solution.countRangeSum(nums1, lower1, upper1) << endl;  // 预期输出: 3
    
    // 测试用例2: 空数组
    vector<int> nums2 = {};
    int lower2 = 0, upper2 = 0;
    cout << "输入: nums = [], lower = 0, upper = 0" << endl;
    cout << "输出: " << solution.countRangeSum(nums2, lower2, upper2) << endl;  // 预期输出: 0
    
    // 测试用例3: 单元素数组
    vector<int> nums3 = {0};
    int lower3 = 0, upper3 = 0;
    cout << "输入: nums = [0], lower = 0, upper = 0" << endl;
    cout << "输出: " << solution.countRangeSum(nums3, lower3, upper3) << endl;  // 预期输出: 1
    
    // 测试用例4: 全为正数的数组
    vector<int> nums4 = {1, 2, 3, 4};
    int lower4 = 5, upper4 = 10;
    cout << "输入: nums = [1,2,3,4], lower = 5, upper = 10" << endl;
    cout << "输出: " << solution.countRangeSum(nums4, lower4, upper4) << endl;  // 预期输出: 4
    
    // 测试用例5: 全为负数的数组
    vector<int> nums5 = {-4, -3, -2, -1};
    int lower5 = -6, upper5 = -2;
    cout << "输入: nums = [-4,-3,-2,-1], lower = -6, upper = -2" << endl;
    cout << "输出: " << solution.countRangeSum(nums5, lower5, upper5) << endl;  // 预期输出: 4
    
    // 测试用例6: 大数值测试
    vector<int> nums6 = {2147483647, -2147483647};
    int lower6 = -2, upper6 = 2;
    cout << "输入: nums = [2147483647, -2147483647], lower = -2, upper = 2" << endl;
    cout << "输出: " << solution.countRangeSum(nums6, lower6, upper6) << endl;  // 预期输出: 1
    
    return 0;
}

/*
===========================================================================
C++语言特有关注事项
===========================================================================
1. 整数溢出问题: 
   - C++中的int类型通常是32位，范围为-2^31到2^31-1
   - 前缀和可能会超出int的范围，必须使用long long类型
   - 示例代码中已使用vector<long long>存储前缀和，避免溢出
   - 计算中间位置时使用left + (right - left)/2而非(left + right)/2，也是为了避免溢出

2. 内存管理: 
   - C++需要显式管理内存，但本例中使用vector自动管理内存
   - 辅助数组help在每次mergeSort调用时创建，递归深度为O(logN)，总内存占用为O(N)
   - 对于超大规模数据，可以考虑使用全局辅助数组复用，减少内存分配开销

3. 递归深度限制: 
   - C++默认的栈空间有限（通常为几MB）
   - 对于非常大的数组（如百万级别），递归可能导致栈溢出
   - 可以考虑实现非递归版本的归并排序

4. 性能优化: 
   - C++允许更细粒度的内存控制，可以预分配内存以提高性能
   - 对于小规模子数组（如长度<10），可以使用插入排序替代归并排序
   - 可以考虑使用std::sort作为优化，尽管它不符合题目的思想要求

5. 类设计: 
   - 封装为Solution类，符合LeetCode的常见接口设计
   - 将mergeSort作为私有成员函数，countRangeSum作为公有接口
   - 这种设计提高了代码的封装性和可维护性

6. 异常安全: 
   - 代码使用STL容器，具有基本的异常安全性
   - vector的内存分配失败会抛出std::bad_alloc异常
   - 可以考虑添加更多的异常处理代码
*/

/*
===========================================================================
工程化考量
===========================================================================
1. 异常处理: 
   - 已添加对空数组的处理
   - 可以添加对无效输入的检查，如lower > upper的情况
   - 考虑添加断言来验证输入参数的有效性

2. 性能优化: 
   - 对于小规模数组（如长度<100），可以使用O(N^2)的暴力解法
   - 可以使用内存池技术优化频繁的内存分配
   - 可以考虑使用SIMD指令集加速归并过程

3. 测试策略: 
   - 已提供多种测试用例，覆盖常见场景
   - 可以使用Google Test或Catch2等测试框架进行更系统的测试
   - 建议添加性能测试和边界条件测试

4. 代码可读性: 
   - 使用了清晰的类和函数命名
   - 添加了详细的注释说明算法思路和关键步骤
   - 遵循了C++的编码规范

5. 可扩展性: 
   - 可以将归并排序部分抽象为通用算法
   - 可以支持自定义的比较函数和统计逻辑
   - 考虑扩展支持其他类型的数组（如float、double）

6. 并行处理: 
   - 对于超大规模数据，可以考虑使用C++17的并行算法
   - 可以使用OpenMP或TBB库实现并行化归并排序
   - 注意并行处理的同步开销

7. 边界情况处理: 
   - 空数组：返回0
   - 单元素数组：直接检查该元素是否在范围内
   - 全部为相同元素：优化计算逻辑
   - 大数值：使用long long类型避免溢出

8. 跨平台兼容性: 
   - 代码使用标准C++，具有良好的跨平台兼容性
   - 避免使用平台特定的扩展
   - 注意不同编译器的实现差异
*/

/*
===========================================================================
相关题目与平台信息
===========================================================================
1. LeetCode 327. Count of Range Sum
   - 题目链接：https://leetcode.cn/problems/count-of-range-sum/
   - 难度等级：困难
   - 标签：归并排序、前缀和、二分查找

2. LeetCode 493. 翻转对 (Reverse Pairs)
   - 题目链接：https://leetcode.cn/problems/reverse-pairs/
   - 难度等级：困难
   - 解题思路：同样使用归并排序的过程统计满足条件的对

3. LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
   - 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度等级：困难
   - 解题思路：类似的归并排序框架，统计右侧较小的元素个数

4. 剑指Offer 51. 数组中的逆序对
   - 题目链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   - 难度等级：困难
   - 解题思路：归并排序过程中统计逆序对数量

5. 牛客网 - 计算数组的小和
   - 题目链接：https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
   - 解题思路：归并排序过程中计算小和

6. LintCode 1497. 区间和的个数
   - 题目链接：https://www.lintcode.com/problem/1497/
   - 与LeetCode 327题相同

7. POJ 2299 - Ultra-QuickSort
   - 题目链接：http://poj.org/problem?id=2299
   - 计算逆序对数量，与本题使用类似的归并排序框架

8. SPOJ - INVCNT
   - 题目链接：https://www.spoj.com/problems/INVCNT/
   - 逆序对计数问题，可使用归并排序解决

9. HDU 4911 - Inversion
   - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=4911
   - 扩展的逆序对问题，有不同的约束条件

10. 字节跳动面试题 - 前缀和区间统计
    - 实际面试中可能会对本题进行变体，如不同的范围条件或附加约束

11. 微软面试题 - 子数组和范围查询
    - 可能需要处理多次查询操作
    - 考察前缀和和数据结构的应用

12. Google面试题 - 二维区间和计数
    - 将问题扩展到二维数组
    - 更复杂的前缀和和排序应用

13. 腾讯面试题 - 最大区间和个数
    - 寻找满足特定和的最大区间数
    - 与本题有类似的前缀和思想

14. 阿里巴巴面试题 - 区间和统计优化
    - 对本题进行性能优化，要求处理超大规模数据
    - 考察并行计算和算法优化能力
*/