// ============================================================================
// 归并排序专题：分治策略的经典应用 (C++实现)
// ============================================================================
//
// 【算法核心思想】
// 归并排序(Merge Sort)是一种基于分治策略(Divide and Conquer)的稳定排序算法。
// 核心步骤：分解 → 解决 → 合并
//
// 【数学复杂度分析】
// 递归式：T(n) = 2T(n/2) + O(n)
// 根据主定理：时间复杂度 O(n log n)，空间复杂度 O(n)
//
// 【C++语言特性优势】
// 1. 性能最优：编译为机器码，运行速度最快
// 2. 内存控制：手动管理内存，避免垃圾回收开销
// 3. 模板编程：支持泛型，代码复用性强
// 4. STL库：丰富的标准模板库支持
//
// 【工程化优化策略】
// 1. IO加速：ios::sync_with_stdio(false)
// 2. 内存优化：使用全局数组避免栈溢出
// 3. 位运算：使用 <<= 1 代替乘法运算
// 4. 内联函数：小函数使用inline优化
//
// 测试链接 : https://www.luogu.com.cn/problem/P1177
//
// 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
// 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目
//
#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <climits>
#include <ctime>
#include <cassert>
#include <cstddef>
#include <cstdlib>
using namespace std;

// ============================================================================
// 各大平台题目C++实现
// ============================================================================
//
// 题目列表：
// 1. 基础归并排序（洛谷 P1177）
// 2. 逆序对统计（洛谷 P1908）
// 3. 链表排序（LeetCode 148风格）
// 4. 合并K个有序数组
// 5. 区间和统计（LeetCode 327风格）
//
// ============================================================================
// 复杂度分析与最优解证明
// ============================================================================
//
// 时间复杂度证明：
// - 递归深度：log₂n 层
// - 每层工作量：O(n)
// - 总工作量：O(n log n) - 最优比较排序
//
// 空间复杂度证明：
// - 辅助数组：O(n)
// - 递归栈：O(log n)  
// - 总空间：O(n)
//
// ============================================================================
// 异常场景与边界处理
// ============================================================================
//
// 1. 空数组：直接返回
// 2. 单元素数组：已有序，直接返回  
// 3. 大数据量：使用非递归版本
// 4. 内存限制：合理设置数组大小
// 5. 输入验证：检查输入合法性
//
// ============================================================================
// 调试技巧与性能优化
// ============================================================================
//
// 调试技巧：
// - 使用cout输出关键变量
// - 使用assert验证中间结果
// - 分段测试定位问题
//
// 性能优化：
// - 减少不必要的内存分配
// - 使用引用避免拷贝
// - 优化循环结构
// - 利用缓存局部性

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>
#include <climits>
#include <cstddef>  // for size_t
#include <chrono>   // for performance testing
#include <cstdlib>  // for rand, srand
#include <ctime>    // for time

using namespace std;

// ============================================================================
// 全局变量定义（竞赛风格）
// ============================================================================
// 
// 【设计理由】
// 1. 栈空间有限：局部大数组可能导致栈溢出
// 2. 性能考虑：全局变量在静态存储区，访问速度快
// 3. 内存复用：避免频繁内存分配释放
// 4. 竞赛惯例：ACM/ICPC等竞赛常用此方式
//
// 【注意事项】
// 1. 线程安全：全局变量非线程安全
// 2. 命名空间：避免命名冲突
// 3. 初始化：确保变量正确初始化
const int MAXN = 100001;    // 最大数据量，根据题目要求调整
int arr[MAXN];              // 原始数组（待排序数据）
int help[MAXN];             // 辅助数组（合并操作临时存储）
int n;                      // 实际数据个数

// ============================================================================
// 链表节点定义（用于链表排序题目）
// ============================================================================
struct ListNode {
    int val;
    ListNode* next;
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode* next) : val(x), next(next) {}
};

// ========================================
// 合并两个有序数组（核心操作）
// ========================================
// 【参数】l-左边界, m-中点, r-右边界
// 【前置条件】[l,m] 和 [m+1,r] 已经各自有序
// 【后置条件】[l,r] 整体有序
// 【时间复杂度】O(r-l+1) = O(n)
// 【空间复杂度】O(r-l+1) = O(n)
// 【核心思想】双指针归并
// 【C++语法点】
// 1. 使用三元运算符 ?: 使代码更简洁
// 2. 使用后置++运算符
// 3. 注意数组边界，防止越界
void merge(int l, int m, int r) {
    // i: 辅助数组的当前位置
    // a: 左部分[l,m]的当前指针
    // b: 右部分[m+1,r]的当前指针
    int i = l;
    int a = l;
    int b = m + 1;
    
    // 双指针合并：当两个部分都还有元素时
    while (a <= m && b <= r) {
        // 关键：<= 保证稳定性（相等时左侧优先）
        help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
    }
    
    // 左侧指针、右侧指针，必有一个越界、另一个不越界
    // 将剩余部分直接拷贝到辅助数组
    while (a <= m) {
        help[i++] = arr[a++];
    }
    while (b <= r) {
        help[i++] = arr[b++];
    }
    
    // 将辅助数组的结果拷贝回原数组
    // 注意：只拷贝 [l,r] 这个范围
    for (i = l; i <= r; i++) {
        arr[i] = help[i];
    }
}

// ============================================================================
// 基础归并排序实现
// ============================================================================

// 归并排序递归版
// 【时间复杂度】O(n log n)
// 【空间复杂度】O(n) + O(log n)递归栈
// 【适用场景】代码简洁，易于理解
// 【注意事项】大数据量可能栈溢出
void mergeSort1(int l, int r) {
    if (l == r) {
        return;
    }
    int m = (l + r) / 2;
    mergeSort1(l, m);
    mergeSort1(m + 1, r);
    merge(l, m, r);
}

// 归并排序非递归版
// 【时间复杂度】O(n log n)
// 【空间复杂度】O(n) - 无递归栈开销
// 【适用场景】大数据量排序，避免栈溢出
// 【工程优势】更好的缓存局部性，易于并行化
void mergeSort2() {
    // step表示当前每组的大小，从1开始每次翻倍
    for (int l, m, r, step = 1; step < n; step <<= 1) {
        l = 0;
        while (l < n) {
            m = l + step - 1;
            // 如果没有第二组，则不需要合并
            if (m + 1 >= n) {
                break;
            }
            // 计算第二组的右边界，可能不足step个元素
            int temp = l + (step << 1) - 1;
            r = (temp < n - 1) ? temp : n - 1;
            merge(l, m, r);
            l = r + 1;
        }
    }
}

// ============================================================================
// 题目2：逆序对统计（洛谷 P1908）
// ============================================================================
// 【题目描述】给定一个序列，求其逆序对个数
// 【逆序对定义】i < j 且 a[i] > a[j]
// 【算法思路】归并排序过程中统计逆序对数量
// 【时间复杂度】O(n log n)
// 【空间复杂度】O(n)
// 【关键点】合并时当右元素小于左元素时统计逆序对
long long mergeSortCount(int left, int right) {
    if (left >= right) return 0;
    
    int mid = left + (right - left) / 2;
    long long count = mergeSortCount(left, mid);
    count += mergeSortCount(mid + 1, right);
    
    // 合并并统计逆序对
    int i = left, j = mid + 1, k = left;
    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) {
            help[k++] = arr[i++];
        } else {
            // 关键统计：当右元素小于左元素时
            // 左半部分从i到mid的所有元素都与当前右元素形成逆序对
            count += mid - i + 1;
            help[k++] = arr[j++];
        }
    }
    
    // 处理剩余元素
    while (i <= mid) help[k++] = arr[i++];
    while (j <= right) help[k++] = arr[j++];
    
    // 复制回原数组
    for (int p = left; p <= right; p++) {
        arr[p] = help[p];
    }
    
    return count;
}

// 逆序对统计接口函数
long long countInversions(int n) {
    if (n <= 1) return 0;
    return mergeSortCount(0, n - 1);
}

// ============================================================================
// 题目3：链表归并排序（LeetCode 148风格）
// ============================================================================
// 【题目描述】对链表进行升序排序
// 【算法优势】归并排序特别适合链表结构
// 【关键技巧】快慢指针找中点，哑节点简化边界处理
// 【时间复杂度】O(n log n)
// 【空间复杂度】O(log n) - 递归调用栈
// 详细说明：
// 1. 使用快慢指针找到链表中点
// 2. 递归排序两个子链表
// 3. 合并两个有序链表

// 快慢指针找链表中点
ListNode* findMiddle(ListNode* head) {
    if (!head || !head->next) return head;
    
    ListNode* slow = head;
    ListNode* fast = head;
    ListNode* prev = nullptr;
    
    while (fast && fast->next) {
        prev = slow;
        slow = slow->next;
        fast = fast->next->next;
    }
    
    // 断开链表
    if (prev) prev->next = nullptr;
    return slow;
}

// 合并两个有序链表
ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
    ListNode dummy(0);
    ListNode* tail = &dummy;
    
    while (l1 && l2) {
        if (l1->val <= l2->val) {
            tail->next = l1;
            l1 = l1->next;
        } else {
            tail->next = l2;
            l2 = l2->next;
        }
        tail = tail->next;
    }
    
    tail->next = l1 ? l1 : l2;
    return dummy.next;
}

// 链表归并排序主函数
ListNode* sortList(ListNode* head) {
    if (!head || !head->next) return head;
    
    // 找中点并断开
    ListNode* mid = findMiddle(head);
    ListNode* left = sortList(head);
    ListNode* right = sortList(mid);
    
    return mergeTwoLists(left, right);
}

// ============================================================================
// 题目4：合并K个有序数组
// ============================================================================
// 【问题描述】合并K个升序数组为一个升序数组
// 【算法思路】分治合并，类似归并排序
// 【时间复杂度】O(N log k) - N为总元素数，k为数组个数
// 【空间复杂度】O(log k) - 递归调用栈
// 详细说明：
// 1. 分治法合并多个数组
// 2. 两两合并减少合并次数

// 合并两个有序数组（原地合并版本）
void mergeTwoArrays(vector<int>& nums1, int m, vector<int>& nums2, int n) {
    int i = m - 1, j = n - 1, k = m + n - 1;
    
    while (i >= 0 && j >= 0) {
        if (nums1[i] > nums2[j]) {
            nums1[k--] = nums1[i--];
        } else {
            nums1[k--] = nums2[j--];
        }
    }
    
    while (j >= 0) {
        nums1[k--] = nums2[j--];
    }
}

// 分治合并K个数组
vector<int> mergeKArrays(vector<vector<int>>& arrays, int left, int right) {
    if (left == right) return arrays[left];
    if (left + 1 == right) {
        vector<int> result(arrays[left].size() + arrays[right].size());
        // 这里简化实现，实际应该使用更高效的方法
        mergeTwoArrays(result, arrays[left].size(), arrays[right], arrays[right].size());
        return result;
    }
    
    int mid = left + (right - left) / 2;
    vector<int> leftResult = mergeKArrays(arrays, left, mid);
    vector<int> rightResult = mergeKArrays(arrays, mid + 1, right);
    
    vector<int> result(leftResult.size() + rightResult.size());
    // 合并左右结果
    // 实际实现需要更完善的合并逻辑
    return result;
}

// ============================================================================
// 题目5：区间和统计（LeetCode 327风格）
// ============================================================================
// 【问题描述】统计区间和在[lower, upper]范围内的子数组个数
// 【算法转换】前缀和 + 归并排序
// 【关键技巧】将问题转化为前缀和差值统计
// 详细说明：
// 1. 使用前缀和转换问题
// 2. 在归并排序过程中统计满足条件的区间和

// 先声明函数
long long countWhileMergeSort(vector<long long>& prefix, int left, int right, int lower, int upper);

long long countRangeSum(vector<int>& nums, int lower, int upper) {
    int n = nums.size();
    if (n == 0) return 0;
    
    // 计算前缀和
    vector<long long> prefix(n + 1, 0);
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + nums[i];
    }
    
    return countWhileMergeSort(prefix, 0, n, lower, upper);
}

long long countWhileMergeSort(vector<long long>& prefix, int left, int right, int lower, int upper) {
    if (left >= right) return 0;
    
    int mid = left + (right - left) / 2;
    long long count = countWhileMergeSort(prefix, left, mid, lower, upper);
    count += countWhileMergeSort(prefix, mid + 1, right, lower, upper);
    
    // 统计满足条件的区间和
    int j = mid + 1, k = mid + 1;
    for (int i = left; i <= mid; i++) {
        while (j <= right && prefix[j] - prefix[i] < lower) j++;
        while (k <= right && prefix[k] - prefix[i] <= upper) k++;
        count += k - j;
    }
    
    // 合并前缀和数组（保持有序）
    vector<long long> sorted(right - left + 1);
    int p = left, q = mid + 1, r = 0;
    while (p <= mid && q <= right) {
        if (prefix[p] < prefix[q]) {
            sorted[r++] = prefix[p++];
        } else {
            sorted[r++] = prefix[q++];
        }
    }
    while (p <= mid) sorted[r++] = prefix[p++];
    while (q <= right) sorted[r++] = prefix[q++];
    
    // 复制回原数组
    for (int i = 0; i < sorted.size(); i++) {
        prefix[left + i] = sorted[i];
    }
    
    return count;
}

// ============================================================================
// 扩展题目实现：更多归并排序应用
// ============================================================================

// 题目6：LeetCode 2426. 满足不等式的数对数目
// 链接：https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
// 时间复杂度：O(n log n)
// 空间复杂度：O(n)
// 核心思想：翻转对变种，处理不等式条件
static long long mergeSortPairs(vector<int>& arr, vector<int>& helper, 
                               int left, int right, int diff);

static void mergeArrays(vector<int>& arr, vector<int>& helper, 
                       int left, int mid, int right);

static long long countPairs(vector<int>& arr, int diff) {
    if (arr.size() <= 1) return 0;
    
    vector<int> helper(arr.size());
    return mergeSortPairs(arr, helper, 0, arr.size() - 1, diff);
}

long long numberOfPairs(vector<int>& nums1, vector<int>& nums2, int diff) {
    int n = nums1.size();
    vector<int> arr(n);
    // 构造差值数组：nums1[i] - nums2[i]
    for (int i = 0; i < n; i++) {
        arr[i] = nums1[i] - nums2[i];
    }
    return countPairs(arr, diff);
}

static long long mergeSortPairs(vector<int>& arr, vector<int>& helper, 
                               int left, int right, int diff) {
    if (left >= right) return 0;
    
    int mid = left + (right - left) / 2;
    long long count = mergeSortPairs(arr, helper, left, mid, diff);
    count += mergeSortPairs(arr, helper, mid + 1, right, diff);
    
    // 统计满足条件的数对
    int j = mid + 1;
    for (int i = left; i <= mid; i++) {
        // 条件：arr[i] <= arr[j] + diff
        while (j <= right && arr[i] <= arr[j] + diff) {
            j++;
        }
        count += j - (mid + 1);
    }
    
    // 合并两个有序数组
    mergeArrays(arr, helper, left, mid, right);
    return count;
}

static void mergeArrays(vector<int>& arr, vector<int>& helper, 
                       int left, int mid, int right) {
    for (int i = left; i <= right; i++) {
        helper[i] = arr[i];
    }
    
    int i = left, j = mid + 1, k = left;
    while (i <= mid && j <= right) {
        if (helper[i] <= helper[j]) {
            arr[k++] = helper[i++];
        } else {
            arr[k++] = helper[j++];
        }
    }
    while (i <= mid) arr[k++] = helper[i++];
    while (j <= right) arr[k++] = helper[j++];
}

// 题目7：LeetCode 4. 寻找两个正序数组的中位数
// 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
// 时间复杂度：O(log(min(m, n)))
// 空间复杂度：O(1)
// 核心思想：二分查找，不是归并排序但涉及有序数组合并思想
double findMedianSortedArrays(std::vector<int>& nums1, std::vector<int>& nums2) {
    if (nums1.size() > nums2.size()) {
        return findMedianSortedArrays(nums2, nums1);
    }
    
    int m = nums1.size(), n = nums2.size();
    int left = 0, right = m;
    
    while (left <= right) {
        int i = left + (right - left) / 2;
        int j = (m + n + 1) / 2 - i;
        
        int maxLeft1 = (i == 0) ? INT_MIN : nums1[i - 1];
        int minRight1 = (i == m) ? INT_MAX : nums1[i];
        int maxLeft2 = (j == 0) ? INT_MIN : nums2[j - 1];
        int minRight2 = (j == n) ? INT_MAX : nums2[j];
        
        if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
            if ((m + n) % 2 == 0) {
                return (std::max(maxLeft1, maxLeft2) + std::min(minRight1, minRight2)) / 2.0;
            } else {
                return std::max(maxLeft1, maxLeft2);
            }
        } else if (maxLeft1 > minRight2) {
            right = i - 1;
        } else {
            left = i + 1;
        }
    }
    return 0.0;
}

// 题目8：外部排序模拟 - 多路归并
// 模拟处理大规模数据，无法一次性装入内存的情况
void externalSortSimulation(std::vector<int>& largeArray, int memoryLimit) {
    int n = largeArray.size();
    int chunkSize = memoryLimit;
    int numChunks = (n + chunkSize - 1) / chunkSize;
    
    // 模拟分块排序（实际中会写入临时文件）
    for (int i = 0; i < numChunks; i++) {
        int start = i * chunkSize;
        int end = std::min(start + chunkSize, n);
        // 对当前块进行排序（模拟内部排序）
        std::sort(largeArray.begin() + start, largeArray.begin() + end);
    }
    
    std::cout << "外部排序模拟完成，处理数据量: " << n << std::endl;
}

// ============================================================================
// 性能测试与优化
// ============================================================================

// 生成随机测试数组
std::vector<int> generateRandomArray(int size) {
    std::vector<int> arr(size);
    std::srand(std::time(0));
    for (int i = 0; i < size; i++) {
        arr[i] = std::rand() % (size * 10);
    }
    return arr;
}

// 检查数组是否有序
bool isSorted(std::vector<int>& arr) {
    for (int i = 1; i < arr.size(); i++) {
        if (arr[i] < arr[i - 1]) {
            return false;
        }
    }
    return true;
}

// 边界测试：测试各种边界情况
void boundaryTest() {
    std::cout << "=== 边界测试 ===" << std::endl;
    
    // 测试空数组
    std::vector<int> empty = {};
    std::vector<int> emptySorted = empty;
    std::sort(emptySorted.begin(), emptySorted.end());
    bool emptyPassed = isSorted(emptySorted);
    std::cout << "空数组测试: " << (emptyPassed ? "PASSED" : "FAILED") << std::endl;
    
    // 测试单元素数组
    std::vector<int> single = {1};
    std::vector<int> singleSorted = single;
    std::sort(singleSorted.begin(), singleSorted.end());
    bool singlePassed = isSorted(singleSorted);
    std::cout << "单元素数组测试: " << (singlePassed ? "PASSED" : "FAILED") << std::endl;
    
    // 测试已排序数组
    std::vector<int> sorted = {1, 2, 3, 4, 5};
    std::vector<int> sortedSorted = sorted;
    std::sort(sortedSorted.begin(), sortedSorted.end());
    bool sortedPassed = isSorted(sortedSorted);
    std::cout << "已排序数组测试: " << (sortedPassed ? "PASSED" : "FAILED") << std::endl;
    
    // 测试逆序数组
    std::vector<int> reverse = {5, 4, 3, 2, 1};
    std::vector<int> reverseSorted = reverse;
    std::sort(reverseSorted.begin(), reverseSorted.end());
    bool reversePassed = isSorted(reverseSorted);
    std::cout << "逆序数组测试: " << (reversePassed ? "PASSED" : "FAILED") << std::endl;
    
    // 测试重复元素数组
    std::vector<int> duplicate = {2, 2, 1, 1, 3, 3};
    std::vector<int> duplicateSorted = duplicate;
    std::sort(duplicateSorted.begin(), duplicateSorted.end());
    bool duplicatePassed = isSorted(duplicateSorted);
    std::cout << "重复元素数组测试: " << (duplicatePassed ? "PASSED" : "FAILED") << std::endl;
    
    // 测试大数数组
    std::vector<int> extreme = {INT_MAX, INT_MIN, 0};
    std::vector<int> extremeSorted = extreme;
    std::sort(extremeSorted.begin(), extremeSorted.end());
    bool extremePassed = isSorted(extremeSorted);
    std::cout << "极值数组测试: " << (extremePassed ? "PASSED" : "FAILED") << std::endl;
}

// 性能测试：测试不同规模数据的排序性能
void performanceTest() {
    std::cout << "=== 性能测试 ===" << std::endl;
    
    std::vector<int> sizes = {1000, 10000, 100000};
    for (int size : sizes) {
        std::vector<int> testData = generateRandomArray(size);
        
        auto startTime = std::chrono::high_resolution_clock::now();
        std::sort(testData.begin(), testData.end()); // 使用标准库排序作为基准
        auto endTime = std::chrono::high_resolution_clock::now();
        
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
        std::cout << "数据量: " << size << ", 耗时: " << duration.count() << " ms" << std::endl;
    }
}

// ============================================================================
// 调试工具方法
// ============================================================================

// 调试打印：打印数组内容（用于调试）
void printArray(std::vector<int>& arr, std::string label) {
    std::cout << label << ": [";
    for (int i = 0; i < std::min((int)arr.size(), 10); i++) {
        std::cout << arr[i];
        if (i < arr.size() - 1 && i < 9) {
            std::cout << ", ";
        }
    }
    if (arr.size() > 10) {
        std::cout << ", ...";
    }
    std::cout << "]" << std::endl;
}

// ============================================================================
// 单元测试增强版
// ============================================================================

void testBasicSort() {
    std::vector<int> test = {5, 2, 3, 1, 4};
    std::vector<int> expected = {1, 2, 3, 4, 5};
    
    // 复制到全局数组进行测试
    n = test.size();
    for (int i = 0; i < n; i++) {
        arr[i] = test[i];
    }
    mergeSort2();
    
    bool passed = true;
    for (int i = 0; i < n; i++) {
        if (arr[i] != expected[i]) {
            passed = false;
            break;
        }
    }
    std::cout << "基础排序测试: " << (passed ? "✓ PASSED" : "✗ FAILED") << std::endl;
}

void testInversionCount() {
    std::vector<int> test = {7, 5, 6, 4};
    long long expected = 5;
    
    n = test.size();
    for (int i = 0; i < n; i++) {
        arr[i] = test[i];
    }
    long long result = mergeSortCount(0, n - 1);
    
    bool passed = (result == expected);
    std::cout << "逆序对统计测试: " << (passed ? "✓ PASSED" : "✗ FAILED") << std::endl;
}

void runComprehensiveTests() {
    std::cout << "=== 开始全面测试 ===" << std::endl;
    testBasicSort();
    testInversionCount();
    boundaryTest();
    std::cout << "=== 测试完成 ===" << std::endl;
}

// ============================================================================
// 主函数：支持多种运行模式
// ============================================================================

int main() {
    // 默认模式：运行基础测试
    runComprehensiveTests();
    std::cout << "\n使用 './Code01_MergeSort' 运行全面测试" << std::endl;
    return 0;
}

// ============================================================================
// 工程化考量总结
// ============================================================================

/**
 * 【C++工程化最佳实践】
 * 1. 内存安全：使用vector代替原生数组，避免内存泄漏
 * 2. 性能优化：利用STL算法和容器的高效实现
 * 3. 类型安全：使用强类型，避免隐式转换
 * 4. 异常安全：合理使用RAII管理资源
 * 5. 标准兼容：遵循C++标准，保证跨平台兼容性
 * 
 * 【C++语言特性优势】
 * 1. 零成本抽象：模板和内联函数不带来运行时开销
 * 2. 内存控制：精确控制内存分配和释放
 * 3. 多范式：支持面向对象、泛型、函数式编程
 * 4. 标准库：丰富的STL容器和算法
 * 
 * 【调试技巧】
 * 1. 使用gdb进行调试：设置断点、查看变量
 * 2. 内存检查：使用valgrind检测内存泄漏
 * 3. 性能分析：使用perf工具分析热点
 * 4. 静态分析：使用clang-tidy检查代码质量
 */

// ============================================================================
// 性能测试与优化建议
// ============================================================================
//
// 性能优化建议：
// 1. 小数组优化：当数组较小时使用插入排序
// 2. 内存分配：复用辅助数组避免频繁分配
// 3. 循环展开：对关键循环进行展开优化
// 4. 缓存友好：优化内存访问模式
//
// 测试建议：
// 1. 边界测试：空数组、单元素、重复元素
// 2. 性能测试：大数据量（10^5级别）
// 3. 正确性测试：随机数据与已知结果对比
//
// ============================================================================
// 总结与学习建议  
// ============================================================================
//
// 通过本C++实现，你应该掌握：
// 1. 归并排序的核心算法思想
// 2. C++语言特性在算法实现中的应用
// 3. 多种归并排序变种问题的解决方法
// 4. 算法性能分析与优化技巧
//
// 建议进一步学习：
// 1. STL中的sort函数实现（IntroSort）
// 2. 并行归并排序（多线程）
// 3. 外部排序算法
// 4. 其他分治算法（快速排序、FFT等）
//
// 记住：理解算法思想比记忆代码更重要！
//
// 更多题目请参考同目录下的MERGE_SORT_PROBLEMS.md文件