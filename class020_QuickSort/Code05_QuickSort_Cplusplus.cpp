// C++版本快速排序实现
// 包含多种快速排序的实现方式

/*
 * 补充题目列表:
 * 
 * 1. LeetCode 912. 排序数组
 *    链接: https://leetcode.cn/problems/sort-an-array/
 *    题目描述: 给你一个整数数组 nums，请你将该数组升序排列。
 *    时间复杂度: O(n log n)，空间复杂度: O(log n)
 *    最优解: 快速排序或归并排序
 * 
 * 2. 洛谷 P1177 【模板】快速排序
 *    链接: https://www.luogu.com.cn/problem/P1177
 *    题目描述: 利用快速排序算法将读入的N个数从小到大排序后输出。
 *    时间复杂度: O(n log n)，空间复杂度: O(log n)
 *    最优解: 快速排序算法实现
 * 
 * 3. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *    时间复杂度: O(n) 平均，空间复杂度: O(log n)
 *    最优解: 快速选择算法
 * 
 * 4. LeetCode 75. 颜色分类
 *    链接: https://leetcode.cn/problems/sort-colors/
 *    题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *    时间复杂度: O(n)，空间复杂度: O(1)
 *    最优解: 三路快排思想
 * 
 * 5. LeetCode 283. 移动零
 *    链接: https://leetcode.cn/problems/move-zeroes/
 *    题目描述: 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *    时间复杂度: O(n)，空间复杂度: O(1)
 *    最优解: 双指针法
 * 
 * 6. Codeforces 401C. Team
 *    链接: https://codeforces.com/problemset/problem/401/C
 *    题目描述: 构造一个01序列，满足特定的约束条件。
 *    时间复杂度: O(n+m)，空间复杂度: O(n+m)
 *    最优解: 贪心构造
 * 
 * 7. AtCoder ABC121C. Energy Drink Collector
 *    链接: https://atcoder.jp/contests/abc121/tasks/abc121_c
 *    题目描述: 购买能量饮料以获得最少的总花费。
 *    时间复杂度: O(n log n)，空间复杂度: O(1)
 *    最优解: 贪心+排序
 * 
 * 8. 牛客网 - 快速排序
 *    链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
 *    题目描述: 实现快速排序算法
 *    时间复杂度: O(n log n)，空间复杂度: O(log n)
 *    最优解: 标准快速排序实现
 * 
 * 9. PAT 1101 Quick Sort
 *    链接: https://pintia.cn/problem-sets/994805342720868352/problems/994805366343188480
 *    题目描述: 快速排序中的主元(pivot)是左面都比它小、右边都比它大的位置对应的数字。找出所有满足条件的主元。
 *    时间复杂度: O(n)，空间复杂度: O(n)
 *    最优解: 预处理左右边界最大值数组
 * 
 * 10. 剑指 Offer 40. 最小的k个数
 *     链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *     题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
 *     时间复杂度: O(n) 平均，空间复杂度: O(log n)
 *     最优解: 快速选择算法
 * 
 * 11. 杭电 OJ 1425. sort
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=1425
 *     题目描述: 对整数数组进行快速排序
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序或堆排序
 * 
 * 12. POJ 2388. Who's in the Middle
 *     链接: http://poj.org/problem?id=2388
 *     题目描述: 找出一组数的中位数，快速选择的经典应用
 *     时间复杂度: O(n) 平均，空间复杂度: O(log n)
 *     最优解: 快速选择算法找中位数
 * 
 * 13. AizuOJ ALDS1_6_C. Quick Sort
 *     链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_6_C
 *     题目描述: 实现快速排序算法并输出每一步的分区结果
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序算法实现
 * 
 * 14. LeetCode 169. 多数元素
 *     链接: https://leetcode.cn/problems/majority-element/
 *     题目描述: 给定一个大小为 n 的数组，找到其中的多数元素
 *     时间复杂度: O(n)，空间复杂度: O(1)
 *     最优解: Boyer-Moore投票算法（与快速选择思想相关）
 * 
 * 15. LeetCode 274. H 指数
 *     链接: https://leetcode.cn/problems/h-index/
 *     题目描述: 计算研究人员的 h 指数
 *     时间复杂度: O(n) 平均，空间复杂度: O(n)
 *     最优解: 计数排序或快速选择
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 最好情况: O(n log n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n log n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n^2) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 小数组使用插入排序 - 减少递归开销
 * 4. 尾递归优化 - 减少栈空间使用
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组、null输入等边界情况
 * 2. 性能优化: 对于小数组使用插入排序优化
 * 3. 内存使用: 原地排序减少额外空间开销
 * 4. 稳定性: 标准快排不稳定，如需稳定排序需特殊处理
 * 
 * 与Java版本的差异:
 * 1. C++使用指针和数组，没有边界检查，性能更高
 * 2. C++使用rand()函数生成随机数，Java使用Math.random()
 * 3. C++可以直接操作内存，Java通过虚拟机管理内存
 * 4. C++需要手动管理内存，Java有垃圾回收机制
 * 5. C++模板支持泛型编程，Java使用泛型
 * 
 * 调试技巧:
 * 1. 使用gdb调试器跟踪递归过程
 * 2. 添加调试输出打印数组状态
 * 3. 使用断言验证分区正确性
 * 4. 测试边界情况（空数组、单元素等）
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <ctime>
#include <cstdlib>

// 使用std命名空间
using namespace std;

class QuickSortSolution {
public:
    /**
     * 方法1: 基础快速排序
     * 使用双指针分区法实现快速排序
     * @param arr 待排序数组
     * @param l 排序区间的左边界（包含）
     * @param r 排序区间的右边界（包含）
     */
    void quickSort1(vector<int>& arr, int l, int r) {
        // 递归终止条件：当左边界大于等于右边界时，表示区间内没有元素或只有一个元素，无需排序
        if (l >= r) return;
        
        // 随机选择基准值，避免最坏情况
        // rand() % (r - l + 1)生成[0, r-l]之间的随机整数
        // 加上l后得到[l, r]之间的随机索引
        int i = l + rand() % (r - l + 1);
        
        // 将随机选择的基准值交换到第一个位置，便于后续分区操作
        swap(arr[l], arr[i]);
        
        // 基准值
        int pivot = arr[l];
        
        // 双指针分区：left从左向右扫描，right从右向左扫描
        int left = l, right = r;
        
        // 双指针分区操作
        while (left < right) {
            // 从右向左找小于基准值的元素
            while (left < right && arr[right] >= pivot) right--;
            
            // 从左向右找大于基准值的元素
            while (left < right && arr[left] <= pivot) left++;
            
            // 如果left < right，说明找到了需要交换的元素对
            if (left < right) {
                // 交换元素
                swap(arr[left], arr[right]);
            }
        }
        
        // 将基准值放到正确位置（left == right时的位置）
        swap(arr[l], arr[left]);
        
        // 递归排序左右两部分
        quickSort1(arr, l, left - 1);
        quickSort1(arr, left + 1, r);
    }
    
    /**
     * 方法2: 三路快速排序（处理重复元素）
     * 将数组划分为三部分：< pivot、= pivot、> pivot
     * 特别适合处理有大量重复元素的数组
     * @param arr 待排序数组
     * @param l 排序区间的左边界（包含）
     * @param r 排序区间的右边界（包含）
     */
    void quickSort2(vector<int>& arr, int l, int r) {
        // 递归终止条件
        if (l >= r) return;
        
        // 随机选择基准值
        int i = l + rand() % (r - l + 1);
        swap(arr[l], arr[i]);
        
        // 基准值
        int pivot = arr[l];
        
        // 三路分区指针：
        // lt: 小于区域的右边界
        // gt: 大于区域的左边界
        // i_idx: 当前处理元素的索引
        int lt = l;      // arr[l+1...lt] < pivot
        int gt = r + 1;  // arr[gt...r] > pivot
        int i_idx = l + 1; // arr[lt+1...i-1] == pivot
        
        // 三路分区过程
        while (i_idx < gt) {
            if (arr[i_idx] < pivot) {
                // 当前元素小于基准值，将其交换到小于区域
                swap(arr[++lt], arr[i_idx++]);
            } else if (arr[i_idx] > pivot) {
                // 当前元素大于基准值，将其交换到大于区域
                swap(arr[--gt], arr[i_idx]);
                // 注意这里i_idx不自增，因为交换过来的元素还未处理
            } else {
                // 当前元素等于基准值，保持在等于区域
                i_idx++;
            }
        }
        
        // 将基准值放到等于区域的左边界
        swap(arr[l], arr[lt]);
        
        // 递归排序小于区域和大于区域
        quickSort2(arr, l, lt - 1);
        quickSort2(arr, gt, r);
    }
    
    /**
     * 方法3: 快速选择算法（用于查找第k小元素）
     * 与快速排序的区别：只处理包含目标元素的子数组
     * 平均时间复杂度：O(n)
     * @param arr 数组
     * @param l 当前处理区间的左边界（包含）
     * @param r 当前处理区间的右边界（包含）
     * @param k 目标元素在排序后数组中的索引位置
     * @return 第k小的元素值
     */
    int quickSelect(vector<int>& arr, int l, int r, int k) {
        // 递归终止条件：当区间只有一个元素时，就是要找的位置
        if (l >= r) return arr[l];
        
        // 随机选择基准值
        int i = l + rand() % (r - l + 1);
        
        // 将基准值交换到末尾位置，便于后续分区操作
        swap(arr[i], arr[r]);
        
        // 基准值
        int pivot = arr[r];
        
        // 小于等于基准值区域的右边界（不包含）
        int left = l;
        
        // 遍历数组，将小于等于基准值的元素放到左侧
        for (int j = l; j < r; j++) {
            if (arr[j] <= pivot) {
                swap(arr[left++], arr[j]);
            }
        }
        
        // 将基准值放到正确位置
        swap(arr[left], arr[r]);
        
        // 根据基准值位置决定下一步操作
        if (left == k) {
            // 如果基准值位置正好是目标位置，直接返回
            return arr[left];
        } else if (left < k) {
            // 如果基准值位置小于目标位置，在右半部分继续查找
            return quickSelect(arr, left + 1, r, k);
        } else {
            // 如果基准值位置大于目标位置，在左半部分继续查找
            return quickSelect(arr, l, left - 1, k);
        }
    }
    
    /**
     * 插入排序算法，用于小数组优化
     * 对小规模数组使用插入排序比快速排序更高效
     * @param arr 数组
     * @param l 排序区间的左边界（包含）
     * @param r 排序区间的右边界（包含）
     */
    void insertionSort(vector<int>& arr, int l, int r) {
        // 从第二个元素开始，逐个插入到已排序序列中
        for (int i = l + 1; i <= r; i++) {
            int key = arr[i]; // 当前要插入的元素
            int j = i - 1;    // 已排序序列的最后一个位置
            
            // 在已排序序列中找到合适的插入位置
            while (j >= l && arr[j] > key) {
                arr[j + 1] = arr[j]; // 元素后移
                j--;
            }
            
            // 插入元素
            arr[j + 1] = key;
        }
    }

    /**
     * 【优化版本1】小数组插入排序优化
     * 当数组长度小于阈值时，使用插入排序，减少递归开销
     * @param arr 数组
     * @param l 排序区间的左边界（包含）
     * @param r 排序区间的右边界（包含）
     */
    void quickSortOptimized(vector<int>& arr, int l, int r) {
        // 小数组阈值，经验值为10-20
        const int INSERTION_SORT_THRESHOLD = 15;
        
        // 对小数组使用插入排序
        if (r - l <= INSERTION_SORT_THRESHOLD) {
            insertionSort(arr, l, r);
            return;
        }
        
        // 对大数组继续使用快速排序
        int i = l + rand() % (r - l + 1);
        swap(arr[l], arr[i]);
        
        int pivot = arr[l];
        int lt = l;      // arr[l+1...lt] < pivot
        int gt = r + 1;  // arr[gt...r] > pivot
        int i_idx = l + 1; // arr[lt+1...i-1] == pivot
        
        while (i_idx < gt) {
            if (arr[i_idx] < pivot) {
                swap(arr[++lt], arr[i_idx++]);
            } else if (arr[i_idx] > pivot) {
                swap(arr[--gt], arr[i_idx]);
            } else {
                i_idx++;
            }
        }
        swap(arr[l], arr[lt]);
        
        quickSortOptimized(arr, l, lt - 1);
        quickSortOptimized(arr, gt, r);
    }
    
    /**
     * 【LeetCode 215解法】数组中的第K个最大元素
     * @param nums 输入数组
     * @param k 第k大的元素
     * @return 第k大的元素值
     */
    int findKthLargest(vector<int>& nums, int k) {
        // 第K大元素等价于第nums.size()-k小的元素
        vector<int> arr(nums.begin(), nums.end());
        return quickSelect(arr, 0, (int)arr.size() - 1, (int)arr.size() - k);
    }
    
    /**
     * 【剑指Offer 40解法】最小的k个数
     * @param arr 输入数组
     * @param k 需要返回的最小元素个数
     * @return 包含最小k个数的数组
     */
    vector<int> getLeastNumbers(vector<int>& arr, int k) {
        // 边界条件处理
        if (k <= 0) return vector<int>();
        if (k >= (int)arr.size()) return arr;
        
        vector<int> nums(arr.begin(), arr.end());
        
        // 使用快速选择找到第k小的元素
        quickSelect(nums, 0, (int)nums.size() - 1, k - 1);
        
        // 收集前k个最小元素
        vector<int> result(nums.begin(), nums.begin() + k);
        return result;
    }
    
    /**
     * 【LeetCode 75解法】颜色分类（三路快排应用）
     * @param nums 包含0、1、2的数组，分别代表红、白、蓝三种颜色
     */
    void sortColors(vector<int>& nums) {
        // 三路快排思想：0放左边，1放中间，2放右边
        int zero = -1;      // [0...zero] == 0
        int two = (int)nums.size(); // [two...n-1] == 2
        int i = 0;          // 当前处理的位置
        
        while (i < two) {
            if (nums[i] == 0) {
                // 当前元素为0，交换到0区域的下一个位置
                swap(nums[++zero], nums[i++]);
            } else if (nums[i] == 1) {
                // 当前元素为1，保持在中间区域
                i++;
            } else { // nums[i] == 2
                // 当前元素为2，交换到2区域的前一个位置
                swap(nums[i], nums[--two]);
            }
        }
    }
    
    /**
     * 【LeetCode 283解法】移动零（分区思想应用）
     * @param nums 输入数组
     */
    void moveZeroes(vector<int>& nums) {
        // 双指针：将非零元素移动到数组前面
        int nonZeroPos = 0; // 指向下一个非零元素应该放的位置
        
        // 第一次遍历：将所有非零元素移到前面
        for (int i = 0; i < (int)nums.size(); i++) {
            if (nums[i] != 0) {
                if (i != nonZeroPos) {
                    swap(nums[i], nums[nonZeroPos]);
                }
                nonZeroPos++;
            }
        }
    }
    
    /**
     * 【LeetCode 912解法】排序数组（标准快速排序实现）
     * @param nums 待排序数组
     * @return 排序后的数组
     */
    vector<int> sortArray(vector<int>& nums) {
        vector<int> result(nums.begin(), nums.end());
        quickSortOptimized(result, 0, (int)result.size() - 1);
        return result;
    }
    
    /**
     * 【POJ 2388 解法】Who's in the Middle（中位数问题）
     * @param nums 输入数组
     * @return 中位数
     */
    int findMedian(vector<int>& nums) {
        int n = (int)nums.size();
        vector<int> arr(nums.begin(), nums.end());
        // 中位数就是第 (n-1)/2 小的元素（0-based索引）
        return quickSelect(arr, 0, n - 1, (n - 1) / 2);
    }
};

/**
 * 测试函数
 * @return 程序退出状态
 */
int main() {
    // 初始化随机数种子
    srand((unsigned int)time(nullptr));
    
    QuickSortSolution solution;
    
    // 测试基础快速排序
    vector<int> arr1;
    arr1.push_back(5);
    arr1.push_back(2);
    arr1.push_back(3);
    arr1.push_back(1);
    arr1.push_back(4);
    
    cout << "原始数组: ";
    for (size_t i = 0; i < arr1.size(); i++) cout << arr1[i] << " ";
    cout << endl;
    
    solution.quickSort1(arr1, 0, (int)arr1.size() - 1);
    cout << "排序后数组: ";
    for (size_t i = 0; i < arr1.size(); i++) cout << arr1[i] << " ";
    cout << endl;
    
    // 测试三路快速排序
    vector<int> arr2;
    arr2.push_back(5);
    arr2.push_back(2);
    arr2.push_back(3);
    arr2.push_back(1);
    arr2.push_back(4);
    arr2.push_back(2);
    arr2.push_back(3);
    
    cout << "\n原始数组: ";
    for (size_t i = 0; i < arr2.size(); i++) cout << arr2[i] << " ";
    cout << endl;
    
    solution.quickSort2(arr2, 0, (int)arr2.size() - 1);
    cout << "三路快排后: ";
    for (size_t i = 0; i < arr2.size(); i++) cout << arr2[i] << " ";
    cout << endl;
    
    // 测试快速选择算法
    vector<int> arr3;
    arr3.push_back(3);
    arr3.push_back(2);
    arr3.push_back(1);
    arr3.push_back(5);
    arr3.push_back(6);
    arr3.push_back(4);
    
    int k = 2; // 查找第2大的元素（即索引为4的元素）
    int result = solution.quickSelect(arr3, 0, (int)arr3.size() - 1, (int)arr3.size() - k);
    cout << "\n数组: ";
    for (size_t i = 0; i < arr3.size(); i++) cout << arr3[i] << " ";
    cout << endl;
    cout << "第" << k << "大的元素是: " << result << endl;
    
    // 测试优化版本的快速排序
    vector<int> arr5;
    arr5.push_back(9);
    arr5.push_back(8);
    arr5.push_back(7);
    arr5.push_back(6);
    arr5.push_back(5);
    arr5.push_back(4);
    arr5.push_back(3);
    arr5.push_back(2);
    arr5.push_back(1);
    arr5.push_back(0);
    arr5.push_back(5);
    arr5.push_back(6);
    arr5.push_back(7);
    arr5.push_back(8);
    arr5.push_back(9);
    
    cout << "\n原始数组: ";
    for (size_t i = 0; i < arr5.size(); i++) cout << arr5[i] << " ";
    cout << endl;
    
    solution.quickSortOptimized(arr5, 0, (int)arr5.size() - 1);
    cout << "优化快排后: ";
    for (size_t i = 0; i < arr5.size(); i++) cout << arr5[i] << " ";
    cout << endl;
    
    // 检查数组是否有序
    bool isSorted = true;
    for (size_t i = 1; i < arr5.size(); i++) {
        if (arr5[i] < arr5[i-1]) {
            isSorted = false;
            break;
        }
    }
    cout << "数组是否有序: " << (isSorted ? "是" : "否") << endl;
    
    // 测试颜色分类
    vector<int> colors;
    colors.push_back(2);
    colors.push_back(0);
    colors.push_back(2);
    colors.push_back(1);
    colors.push_back(1);
    colors.push_back(0);
    
    cout << "\n原始颜色数组: ";
    for (size_t i = 0; i < colors.size(); i++) cout << colors[i] << " ";
    cout << endl;
    
    solution.sortColors(colors);
    cout << "颜色分类后: ";
    for (size_t i = 0; i < colors.size(); i++) cout << colors[i] << " ";
    cout << endl;
    
    // 测试移动零
    vector<int> zeros;
    zeros.push_back(0);
    zeros.push_back(1);
    zeros.push_back(0);
    zeros.push_back(3);
    zeros.push_back(12);
    
    cout << "\n原始数组: ";
    for (size_t i = 0; i < zeros.size(); i++) cout << zeros[i] << " ";
    cout << endl;
    
    solution.moveZeroes(zeros);
    cout << "移动零后: ";
    for (size_t i = 0; i < zeros.size(); i++) cout << zeros[i] << " ";
    cout << endl;
    
    return 0;
}