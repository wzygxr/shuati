// 计算右侧小于当前元素的个数，C++版
// 测试链接 : https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

/**
 * 计算右侧小于当前元素的个数详解:
 * 
 * 问题描述:
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解法思路:
 * 1. 暴力解法: 对每个元素，遍历其右侧所有元素，统计比它小的元素个数，时间复杂度O(N^2)
 * 2. 归并排序思想: 
 *    - 在归并排序过程中，当合并两个有序数组时，如果从左侧数组选择元素，则右侧数组中已处理的元素
 *      都是小于当前元素的，可以统计数量
 *    - 由于排序会改变元素位置，需要记录原始索引
 * 
 * 时间复杂度: O(N * logN) - 归并排序的时间复杂度
 * 空间复杂度: O(N) - 辅助数组和结果数组的空间复杂度
 * 
 * 相关题目:
 * 1. LeetCode 493. 翻转对
 * 2. LeetCode 327. 区间和的个数
 * 3. 剑指Offer 51. 数组中的逆序对
 * 4. 牛客网 - 计算数组的小和
 */

const int MAXN = 100001;
int help[MAXN];

// 提交以下代码到LeetCode
/*
题目3: 计算右侧小于当前元素的个数

题目描述:
给定一个整数数组 nums，按要求返回一个新数组 counts。
数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例:
输入: [5,2,6,1]
输出: [2,1,1,0]
解释:
- 5 的右侧有 2 个小于 5 的元素 (2 和 1)
- 2 的右侧有 1 个小于 2 的元素 (1)
- 6 的右侧有 1 个小于 6 的元素 (1)
- 1 的右侧没有小于 1 的元素

核心算法思想: 归并排序+索引映射

暴力解法:
- 对于每个元素，遍历其右侧所有元素，统计小于它的数量
- 时间复杂度: O(n²)，空间复杂度: O(1)

最优解法:
- 利用归并排序的分治特性，在合并过程中统计右侧小于当前元素的数量
- 关键点: 通过维护索引数组，在排序过程中保持元素的原始位置信息
- 时间复杂度: O(n log n)，空间复杂度: O(n)

归并排序解法详解:
1. 创建索引数组，记录每个元素在原始数组中的位置
2. 进行归并排序，在合并过程中统计右侧小于当前元素的数量
3. 当左半部分元素被选中时，右半部分已处理的元素都是小于它的
4. 利用这个特性，可以在O(1)时间内累加右侧小元素的数量

C++语言特性注意事项:
1. C++中的vector需要预先分配空间或使用push_back()进行动态扩容
2. C++没有自动内存管理，需要注意内存泄漏问题
3. C++的整数类型有大小限制，对于极端情况可能需要处理溢出
4. 在C++中，递归调用的栈深度可能受到编译器限制

工程化考量:
1. 异常处理: 处理空数组的情况，避免数组越界
2. 内存优化: 使用预分配空间的vector避免频繁重新分配
3. 性能优化: 使用值传递而非引用传递小对象，减少栈开销
4. 线程安全: 当前实现不是线程安全的，需要在多线程环境中添加同步机制
*/

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
private:
    // 结果数组，存储每个位置的答案
    vector<int> res;
    // 临时数组，用于归并排序过程中的数组合并
    vector<int> temp;
    // 索引数组，保存元素在原始数组中的位置
    vector<int> index;
    // 临时索引数组，用于归并过程中保存索引信息
    vector<int> temp_index;

    /**
     * 合并两个有序子数组，并在合并过程中统计右侧小于当前元素的数量
     * 
     * @param nums 输入数组的副本，用于排序比较
     * @param left 当前处理区间的左边界
     * @param mid 当前处理区间的中点
     * @param right 当前处理区间的右边界
     */
    void merge(vector<int>& nums, int left, int mid, int right) {
        // 初始化指针
        int i = left;     // 左子数组的指针
        int j = mid + 1;  // 右子数组的指针
        int k = left;     // 临时数组的指针
        
        // 合并两个有序子数组，同时统计右侧小于当前元素的数量
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                // 左子数组元素较小，将其放入临时数组
                // 此时右子数组中已经处理的元素(j - mid - 1个)都小于nums[i]
                temp[k] = nums[i];
                temp_index[k] = index[i];
                // 统计右侧小于当前元素的数量
                res[index[i]] += j - mid - 1;
                k++;
                i++;
            } else {
                // 右子数组元素较小，直接放入临时数组
                // 此时不需要更新统计结果
                temp[k] = nums[j];
                temp_index[k] = index[j];
                k++;
                j++;
            }
        }
        
        // 处理左子数组中剩余的元素
        while (i <= mid) {
            temp[k] = nums[i];
            temp_index[k] = index[i];
            // 此时右子数组中所有元素(j - mid - 1个)都小于nums[i]
            res[index[i]] += j - mid - 1;
            k++;
            i++;
        }
        
        // 处理右子数组中剩余的元素
        while (j <= right) {
            temp[k] = nums[j];
            temp_index[k] = index[j];
            k++;
            j++;
        }
        
        // 将临时数组中的排序结果复制回原数组
        for (int m = left; m <= right; m++) {
            nums[m] = temp[m];
            index[m] = temp_index[m];
        }
    }

    /**
     * 归并排序的递归实现
     * 
     * @param nums 输入数组的副本
     * @param left 当前处理区间的左边界
     * @param right 当前处理区间的右边界
     */
    void mergeSort(vector<int>& nums, int left, int right) {
        // 递归终止条件：区间长度为0或1
        if (left < right) {
            // 计算中间位置，避免整数溢出
            int mid = left + (right - left) / 2;
            
            // 分治处理左右两个子数组
            mergeSort(nums, left, mid);
            mergeSort(nums, mid + 1, right);
            
            // 合并两个有序子数组
            merge(nums, left, mid, right);
        }
    }

public:
    /**
     * 计算每个元素右侧小于它的元素个数
     * 
     * @param nums 输入整数数组
     * @return 包含每个元素右侧小于它的元素个数的数组
     */
    vector<int> countSmaller(vector<int>& nums) {
        int n = nums.size();
        
        // 处理边界情况：空数组
        if (n == 0) {
            return {};
        }
        
        // 初始化数组
        res.resize(n, 0);            // 结果数组，初始值全为0
        temp.resize(n);              // 临时数组，用于归并过程
        index.resize(n);             // 索引数组
        temp_index.resize(n);        // 临时索引数组
        
        // 初始化索引数组，记录元素在原始数组中的位置
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }
        
        // 创建原数组的副本，避免修改原始数组
        vector<int> nums_copy(nums.begin(), nums.end());
        
        // 执行归并排序并统计
        mergeSort(nums_copy, 0, n - 1);
        
        return res;
    }
};

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: 基本情况
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = solution.countSmaller(nums1);
    cout << "输入: nums = [5,2,6,1]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [2,1,1,0]
    
    // 测试用例2: 空数组
    vector<int> nums2 = {};
    vector<int> result2 = solution.countSmaller(nums2);
    cout << "输入: nums = []" << endl;
    cout << "输出: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: []
    
    // 测试用例3: 单元素数组
    vector<int> nums3 = {1};
    vector<int> result3 = solution.countSmaller(nums3);
    cout << "输入: nums = [1]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i];
        if (i < result3.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [0]
    
    // 测试用例4: 递增数组
    vector<int> nums4 = {1, 2, 3, 4, 5};
    vector<int> result4 = solution.countSmaller(nums4);
    cout << "输入: nums = [1,2,3,4,5]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result4.size(); i++) {
        cout << result4[i];
        if (i < result4.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [0,0,0,0,0]
    
    // 测试用例5: 递减数组
    vector<int> nums5 = {5, 4, 3, 2, 1};
    vector<int> result5 = solution.countSmaller(nums5);
    cout << "输入: nums = [5,4,3,2,1]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result5.size(); i++) {
        cout << result5[i];
        if (i < result5.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [4,3,2,1,0]
    
    // 测试用例6: 重复元素
    vector<int> nums6 = {2, 2, 2, 2};
    vector<int> result6 = solution.countSmaller(nums6);
    cout << "输入: nums = [2,2,2,2]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result6.size(); i++) {
        cout << result6[i];
        if (i < result6.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [3,2,1,0]
    
    // 测试用例7: 大数值测试
    vector<int> nums7 = {2147483647, -2147483648, 0};
    vector<int> result7 = solution.countSmaller(nums7);
    cout << "输入: nums = [2147483647,-2147483648,0]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result7.size(); i++) {
        cout << result7[i];
        if (i < result7.size() - 1) cout << ",";
    }
    cout << "]" << endl;  // 预期输出: [2,0,0]
    
    return 0;
}

/*
===========================================================================
C++语言特有关注事项
===========================================================================
1. 内存管理：
   - C++需要显式管理内存，使用vector时要注意正确初始化和释放
   - 使用resize()预分配空间可以避免频繁的内存重新分配，提高性能
   - 代码中已使用resize()为所有向量预分配了空间

2. 整数溢出：
   - 计算mid = left + (right - left) / 2 而非 (left + right) / 2，避免整数溢出
   - 对于非常大的数组，需要考虑int类型是否足够表示索引（最大约20亿）
   - 当数组大小超过INT_MAX时，应考虑使用size_t类型

3. 递归深度：
   - C++的递归深度受系统栈大小限制，默认为几MB
   - 对于超大数组（如长度超过1e5），递归归并可能导致栈溢出
   - 可以考虑实现非递归版本的归并排序来避免这个问题

4. 引用传递：
   - 在函数参数中使用引用传递可以避免大型对象的拷贝，提高效率
   - 但需要注意引用的生命周期，避免悬垂引用
   - 代码中对nums参数使用了引用传递，但创建了副本以避免修改原数组

5. 类设计：
   - Solution类封装了算法的核心功能，符合面向对象设计原则
   - 辅助函数被设为私有，只暴露必要的接口
   - 使用成员变量存储中间结果，避免函数间传递大量数据

6. 异常处理：
   - C++中vector的resize()在内存分配失败时会抛出bad_alloc异常
   - 当前代码没有显式处理异常，可以根据需要添加try-catch块
   - 对于边界条件（如空数组），已进行了特殊处理

7. 模板支持：
   - 可以将代码修改为模板函数，支持不同类型的输入（如long long, float等）
   - 这需要修改类定义和相关函数签名

8. 编译优化：
   - 可以使用编译选项如-O2来启用优化
   - 对于频繁调用的小函数，可以使用inline关键字减少函数调用开销
===========================================================================
工程化考量
===========================================================================
1. 异常处理：
   - 已处理空数组的情况
   - 可以考虑添加对输入参数的有效性检查，如数组大小限制
   - 可以添加assert断言来验证关键逻辑的正确性

2. 性能优化：
   - 使用nums_copy避免修改原始输入，保持函数的幂等性
   - 预先分配所有需要的数组空间，减少动态分配开销
   - 对于小规模子数组（如长度<10），可以使用插入排序替代归并排序
   - 在merge操作中使用原地合并技术可以进一步优化空间复杂度

3. 线程安全：
   - 当前实现不是线程安全的，不适合在多线程环境中使用
   - 成员变量res, temp, index, temp_index在多线程环境下可能导致竞态条件
   - 在多线程环境中，可以：
     a) 为每个线程创建独立的Solution实例
     b) 将中间变量移至函数内部，避免使用成员变量
     c) 添加互斥锁保护共享资源

4. 代码可维护性：
   - 使用清晰的函数命名和详细的注释说明算法思路
   - 类的设计遵循单一职责原则
   - 变量命名遵循C++的命名约定
   - 代码结构清晰，逻辑分明

5. 可扩展性：
   - 该算法框架可以扩展到类似问题，如计算右侧大于当前元素的个数
   - 可以通过模板参数支持不同类型的数据
   - 可以将归并排序部分抽象为通用算法组件

6. 测试策略：
   - 已添加多种测试用例，覆盖常见场景和边界情况
   - 可以使用Google Test或Catch2等测试框架进行更系统的测试
   - 建议添加性能测试，比较不同实现的效率

7. 内存效率：
   - 空间复杂度为O(N)，对于大规模数据可能需要优化
   - 可以考虑使用更紧凑的数据结构，如预分配的数组而非vector
   - 对于超大数组，可以考虑分块处理，减少内存占用

8. 跨平台兼容性：
   - 代码使用标准C++，具有良好的跨平台兼容性
   - 避免使用平台特定的扩展或非标准库
   - 在不同编译器下可能需要调整编译选项

9. 文档和注释：
   - 函数接口有清晰的注释说明参数和返回值
   - 复杂算法步骤有详细的注释解释
   - 可以考虑添加更详细的算法时间和空间复杂度分析

10. 代码优化：
    - 可以考虑使用更高效的排序算法或数据结构，如二叉搜索树、树状数组等
    - 可以优化合并过程，减少不必要的操作
    - 对于特定应用场景，可以考虑启发式优化
===========================================================================
相关题目与平台信息
===========================================================================
1. LeetCode 315. Count of Smaller Numbers After Self
   - 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度等级：困难
   - 标签：归并排序、树状数组、线段树

2. LeetCode 493. 翻转对 (Reverse Pairs)
   - 题目链接：https://leetcode.cn/problems/reverse-pairs/
   - 难度等级：困难
   - 解题思路：同样使用归并排序的过程统计满足条件的对

3. LeetCode 327. 区间和的个数 (Count of Range Sum)
   - 题目链接：https://leetcode.cn/problems/count-of-range-sum/
   - 难度等级：困难
   - 解题思路：前缀和结合归并排序，统计满足条件的区间和

4. 剑指Offer 51. 数组中的逆序对
   - 题目链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   - 难度等级：困难
   - 解题思路：归并排序过程中统计逆序对数量

5. 牛客网 - 计算数组的小和
   - 题目链接：https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
   - 解题思路：归并排序过程中计算小和

6. POJ 2299. Ultra-QuickSort
   - 题目链接：http://poj.org/problem?id=2299
   - 计算逆序对数量，与本题使用类似的归并排序框架

7. SPOJ - INVCNT
   - 题目链接：https://www.spoj.com/problems/INVCNT/
   - 逆序对计数问题，可使用归并排序解决

8. HDU 1394. Minimum Inversion Number
   - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
   - 最小逆序对数量，扩展的逆序对问题

9. LintCode 1297. 统计右侧小于当前元素的个数
   - 题目链接：https://www.lintcode.com/problem/1297/
   - 与LeetCode 315题相同

10. 字节跳动面试题 - 数组统计问题
    - 实际面试中可能会对本题进行变体，如不同的统计条件
    - 考察归并排序思想的灵活应用

11. 微软面试题 - 元素相对顺序问题
    - 可能要求在保持相对顺序的情况下进行统计或变换
    - 与本题的索引维护思想相关

12. Google面试题 - 二维数组统计
    - 将问题扩展到二维数组，统计每个元素右下方小于它的元素个数
    - 更复杂的归并排序或分治思想应用

13. 腾讯面试题 - 数据流中的逆序对
    - 处理动态数据流，实时统计逆序对数量
    - 可能需要使用更高效的数据结构，如树状数组或线段树

14. 阿里巴巴面试题 - 大规模数据统计
    - 要求处理超大规模数据，考察算法优化和并行处理能力
    - 可能需要结合归并排序和分布式计算思想

15. 美团面试题 - 数组变换统计
    - 在数组变换过程中统计满足特定条件的元素对数量
    - 考察对归并排序思想的深入理解和应用

16. 京东面试题 - 字符串逆序对
    - 将问题应用到字符串，统计满足条件的字符对
    - 归并排序思想在不同数据类型上的应用

17. 百度面试题 - 多维逆序对
    - 扩展到多维空间，统计多维逆序对
    - 更复杂的分治策略和数据结构应用

18. 小米面试题 - 排序过程分析
    - 分析排序算法执行过程中的各种统计量
    - 与本题的归并排序过程统计思想一致
*/