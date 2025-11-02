#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>
#include <string>
#include <set>
#include <map>
#include <stdexcept>
#include <functional>
using namespace std;

// Forward declarations
int quickSelect(vector<int>& nums, int left, int right, int k);
int partition(vector<int>& nums, int left, int right);
void swapVec(vector<int>& arr, int i, int j);
void testAdditionalProblems();

/**
 * 选择排序、冒泡排序、插入排序的验证与扩展练习
 *
 * 选择排序(Selection Sort):
 * - 工作原理：每次从未排序的部分中找到最小元素，放到已排序部分的末尾
 * - 时间复杂度：O(n²) - 最好、平均、最坏情况都相同
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：不稳定
 * - 适用场景：数据量小且对稳定性无要求
 *
 * 冒泡排序(Bubble Sort):
 * - 工作原理：相邻元素两两比较，如果顺序错误就交换，每轮将最大元素"冒泡"到末尾
 * - 时间复杂度：O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：稳定
 * - 适用场景：数据量小且要求稳定性
 *
 * 插入排序(Insertion Sort):
 * - 工作原理：将未排序元素插入到已排序序列的适当位置
 * - 时间复杂度：O(n²) - 最坏情况，O(n) - 最好情况(已排序)
 * - 空间复杂度：O(1) - 原地排序
 * - 稳定性：稳定
 * - 适用场景：小规模数据或基本有序的数据
 */

/**
 * 交换数组中的两个元素
 * @param arr 数组
 * @param i 第一个元素的索引
 * @param j 第二个元素的索引
 */
void swapVec(std::vector<int>& arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

/**
 * 选择排序 - Selection Sort
 * 时间复杂度: O(n²) - 无论什么情况都需要进行n(n-1)/2次比较
 * 空间复杂度: O(1) - 只使用了常数级别的额外空间
 * 稳定性: 不稳定 - 相等元素的相对位置可能改变
 * 
 * 算法思路：
 * 1. 在未排序序列中找到最小元素
 * 2. 将其与未排序序列的第一个元素交换位置
 * 3. 重复步骤1-2，直到所有元素排序完成
 * 
 * 优点：
 * - 实现简单
 * - 原地排序，空间复杂度低
 * - 交换次数少，最多进行n-1次交换
 * 
 * 缺点：
 * - 时间复杂度高，不适合大数据量
 * - 不稳定
 * - 无法利用数据的有序性优化
 * 
 * 适用场景：
 * - 数据量小的情况
 * - 对内存使用要求严格的场景
 * - 不要求稳定性的场景
 * 
 * @param arr 待排序数组
 */
void selectionSort(std::vector<int>& arr) {
    // 边界检查：空数组或单元素数组无需排序
    if (arr.empty() || arr.size() < 2) {
        return;
    }
    
    int n = arr.size();
    // 外层循环控制排序的轮数，需要进行n-1轮
    // 每轮都会确定一个元素的最终位置（当前未排序部分的最小元素）
    for (int i = 0; i < n - 1; ++i) {
        // 假设当前位置i就是未排序部分的最小值位置
        // 从当前位置开始，在未排序部分[i, n-1]中寻找真正的最小值
        int minIndex = i;
        
        // 内层循环在未排序部分[i+1, n-1]中寻找真正的最小值
        // j从i+1开始，因为位置i已经是当前假设的最小值位置
        for (int j = i + 1; j < n; ++j) {
            // 如果找到更小的元素，更新最小值索引
            // 这里使用<而不是<=是为了保持算法的不稳定性
            if (arr[j] < arr[minIndex]) {
                minIndex = j;
            }
        }
        
        // 如果最小值不在当前位置，则交换
        // 这样可以减少不必要的交换操作（当minIndex == i时不需要交换）
        if (minIndex != i) {
            swapVec(arr, i, minIndex);
        }
    }
}

/**
 * 冒泡排序 - Bubble Sort
 * 时间复杂度: O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
 * 空间复杂度: O(1) - 只使用了常数级别的额外空间
 * 稳定性: 稳定 - 相等元素不会交换位置
 * 
 * 算法思路：
 * 1. 比较相邻的两个元素，如果前面的比后面的大就交换
 * 2. 每一轮都会将当前未排序部分的最大元素"冒泡"到末尾
 * 3. 重复步骤1-2，直到所有元素排序完成
 * 
 * 优点：
 * - 实现简单，容易理解
 * - 稳定排序
 * - 原地排序
 * - 能够检测数组是否已经有序
 * 
 * 缺点：
 * - 时间复杂度高，不适合大数据量
 * - 元素交换次数多
 * 
 * 优化：
 * - 设置标志位，如果某一轮没有发生交换，说明数组已经有序，可以提前结束
 * 
 * 适用场景：
 * - 数据量小的情况
 * - 要求稳定性的场景
 * - 教学演示
 * 
 * @param arr 待排序数组
 */
void bubbleSort(std::vector<int>& arr) {
    // 边界检查：空数组或单元素数组无需排序
    if (arr.empty() || arr.size() < 2) {
        return;
    }
    
    int n = arr.size();
    // 外层循环控制排序的轮数，最多需要进行n-1轮
    // 每轮都会确定一个元素的最终位置（当前未排序部分的最大元素）
    // end表示每轮比较的上界，随着排序的进行逐渐减小
    for (int end = n - 1; end > 0; --end) {
        // 优化标志：记录本轮是否发生交换
        // 如果一轮比较中没有发生任何交换，说明数组已经有序
        bool swapped = false;
        
        // 内层循环进行相邻元素的比较和交换
        // 每轮比较范围逐渐缩小，因为末尾的元素已经有序
        // i从0开始到end-1，比较arr[i]和arr[i+1]
        for (int i = 0; i < end; ++i) {
            // 如果前面的元素比后面的大，则交换
            // 这会将较大的元素逐步向右移动（"冒泡"）
            if (arr[i] > arr[i + 1]) {
                swapVec(arr, i, i + 1);
                swapped = true;
            }
        }
        
        // 如果本轮没有发生交换，说明数组已经有序，可以提前结束
        // 这是冒泡排序的一个重要优化，可以将最好情况的时间复杂度降到O(n)
        if (!swapped) {
            break;
        }
    }
}

/**
 * 插入排序 - Insertion Sort
 * 时间复杂度: O(n²) - 最坏情况，O(n) - 最好情况(已排序)
 * 空间复杂度: O(1) - 只使用了常数级别的额外空间
 * 稳定性: 稳定 - 相等元素不会交换位置
 * 
 * 算法思路：
 * 1. 将数组分为已排序和未排序两部分，初始时已排序部分只有第一个元素
 * 2. 依次取出未排序部分的元素，在已排序部分找到合适的插入位置
 * 3. 将元素插入到正确位置，重复步骤2-3直到所有元素排序完成
 * 
 * 优点：
 * - 实现简单
 * - 稳定排序
 * - 原地排序
 * - 对于小规模或基本有序的数据效率很高
 * - 在线算法：可以在接收数据的同时进行排序
 * 
 * 缺点：
 * - 时间复杂度高，不适合大数据量
 * - 对于逆序数据效率较低
 * 
 * 适用场景：
 * - 小规模数据排序
 * - 基本有序的数据
 * - 在线数据排序
 * - 作为高级排序算法的子过程（如快速排序的小数组优化）
 * 
 * @param arr 待排序数组
 */
void insertionSort(std::vector<int>& arr) {
    // 边界检查：空数组或单元素数组无需排序
    if (arr.empty() || arr.size() < 2) {
        return;
    }
    
    int n = arr.size();
    // 从第二个元素开始，因为第一个元素可以看作已排序
    // i表示当前要插入的元素位置
    for (int i = 1; i < n; ++i) {
        // 从当前位置向前比较，找到合适的插入位置
        // 当前元素为arr[i]，需要在arr[0...i-1]中找到插入位置
        // j从i-1开始向前遍历已排序部分
        for (int j = i - 1; j >= 0; --j) {
            if (arr[j] > arr[j + 1]) {
                // 如果前一个元素大于当前元素，则交换
                // 这实际上是在将当前元素向前移动
                swapVec(arr, j, j + 1);
            } else {
                // 找到合适的位置，跳出内层循环
                // 当arr[j] <= arr[j+1]时，说明已找到插入位置
                break;
            }
        }
    }
}

/**
 * 插入排序的优化版本 - 使用赋值代替交换，减少操作次数
 * 
 * 优化原理：
 * 在标准插入排序中，每次比较都可能涉及一次完整的交换操作（3次赋值）
 * 而在优化版本中，我们先保存当前要插入的元素，然后只进行元素后移操作
 * 最后再将保存的元素插入到正确位置，这样可以减少赋值操作的次数
 * 
 * 性能提升：
 * - 对于随机数据，大约可以减少30%-50%的赋值操作
 * - 对于接近有序的数据，性能提升更显著
 * 
 * @param arr 待排序数组
 */
void insertionSortOptimized(std::vector<int>& arr) {
    // 边界检查：空数组或单元素数组无需排序
    if (arr.empty() || arr.size() < 2) {
        return;
    }
    
    int n = arr.size();
    // 从第二个元素开始处理
    for (int i = 1; i < n; ++i) {
        // 保存当前要插入的元素
        int current = arr[i];
        // j指向已排序部分的最后一个位置
        int j = i - 1;
        
        // 将大于current的元素向后移动
        // 当已排序部分的元素大于current时，将其向后移动一位
        while (j >= 0 && arr[j] > current) {
            arr[j + 1] = arr[j];
            j--;
        }
        
        // 将current插入到正确位置
        // 此时j+1就是current应该插入的位置
        arr[j + 1] = current;
    }
}

/**
 * 二分插入排序 - 使用二分查找优化插入排序
 * 
 * 优化原理：
 * 在已排序的部分查找插入位置时，使用二分查找代替线性扫描
 * 可以将查找过程的时间复杂度从O(n)降低到O(log n)
 * 但整体排序的时间复杂度仍然是O(n²)，因为元素移动的操作无法避免
 * 
 * 适用场景：
 * - 数据量较大但仍在可接受范围内的情况
 * - 比较操作成本较高的场景
 * 
 * @param arr 待排序数组
 */
void binaryInsertionSort(std::vector<int>& arr) {
    // 边界检查：空数组或单元素数组无需排序
    if (arr.empty() || arr.size() < 2) {
        return;
    }
    
    int n = arr.size();
    // 从第二个元素开始处理
    for (int i = 1; i < n; ++i) {
        // 保存当前要插入的元素
        int current = arr[i];
        // 使用二分查找找到插入位置
        // 在已排序部分arr[0...i-1]中查找插入位置
        int left = 0, right = i - 1;
        
        // 二分查找过程
        // 查找第一个大于current的元素位置
        while (left <= right) {
            // 使用left + (right - left) / 2而不是(left + right) / 2
            // 可以避免当left和right都很大时可能发生的整数溢出
            int mid = left + (right - left) / 2;
            if (arr[mid] > current) {
                // current应该插入到mid或其左侧
                right = mid - 1;
            } else {
                // current应该插入到mid右侧
                left = mid + 1;
            }
        }
        
        // 找到了插入位置left，需要将[left, i-1]的元素后移
        // 将arr[left...i-1]的元素向后移动一位到arr[left+1...i]
        for (int j = i - 1; j >= left; --j) {
            arr[j + 1] = arr[j];
        }
        
        // 将current插入到正确位置
        arr[left] = current;
    }
}

// =========================================================================
// 经典算法题目的最优解法实现
// =========================================================================

/**
 * LeetCode 75. 颜色分类 - 最优解法（三指针法/荷兰国旗算法）
 * 题目链接：https://leetcode.cn/problems/sort-colors/
 * 
 * 时间复杂度：O(n) - 仅需一次遍历
 * 空间复杂度：O(1) - 原地排序
 * 
 * 算法思想：
 * 使用三个指针将数组分为三个区域：
 * - [0, p0): 已排序的0区域
 * - [p0, curr): 已排序的1区域
 * - [p2, n-1]: 已排序的2区域
 * - [curr, p2): 待处理的区域
 * 
 * 算法步骤：
 * 1. 初始化p0=0（0的右边界），curr=0（当前处理位置），p2=n-1（2的左边界）
 * 2. 当curr <= p2时循环：
 *    a. 如果nums[curr] == 0，交换nums[curr]和nums[p0]，p0++, curr++
 *    b. 如果nums[curr] == 1，curr++
 *    c. 如果nums[curr] == 2，交换nums[curr]和nums[p2]，p2--（curr不变）
 * 
 * 为什么是最优解：
 * - 相比基础排序算法的O(n²)时间复杂度，三指针法只需要O(n)时间
 * - 空间复杂度为O(1)，不需要额外空间
 * - 只需要一次遍历，效率高
 * - 直接利用了问题特性（只有0、1、2三种元素）
 * 
 * @param nums 待排序数组，元素只能是0、1、2
 */
void sortColors(std::vector<int>& nums) {
    // 防御性编程：检查输入合法性
    if (nums.empty() || nums.size() < 2) {
        return;
    }
    
    int n = nums.size();
    int p0 = 0;       // 0的右边界（初始为0）
    int curr = 0;     // 当前遍历的位置
    int p2 = n - 1;   // 2的左边界（初始为数组末尾）
    
    // 遍历数组直到curr超过p2
    // 循环条件是curr <= p2，因为p2位置的元素尚未处理
    while (curr <= p2) {
        if (nums[curr] == 0) {
            // 当前元素为0，放到0的区域
            // 交换后，p0位置的元素一定是0，curr位置的元素是原来p0位置的元素（0、1或2）
            // 由于p0 <= curr，p0位置的元素已经被处理过，所以可以安全地递增curr
            swapVec(nums, curr, p0);
            curr++;
            p0++;
        } else if (nums[curr] == 2) {
            // 当前元素为2，放到2的区域
            // 交换后，p2位置的元素是原来curr位置的元素（未知），所以curr不能递增
            swapVec(nums, curr, p2);
            p2--;
            // 注意curr不变，因为交换过来的元素还未处理
        } else {
            // 当前元素为1，保持不动，继续处理下一个元素
            // 1的区域自然扩展
            curr++;
        }
    }
}

/**
 * LeetCode 88. 合并两个有序数组 - 最优解法（从后向前合并）
 * 题目链接：https://leetcode.cn/problems/merge-sorted-array/
 * 
 * 时间复杂度：O(m+n) - 仅需一次遍历
 * 空间复杂度：O(1) - 原地操作
 * 
 * 算法思想：
 * 从两个数组的末尾开始比较，将较大的元素放到nums1的末尾位置
 * 这样可以避免覆盖nums1中的原始数据，不需要额外空间
 * 
 * 算法步骤：
 * 1. 初始化三个指针：i=m-1（nums1有效元素的末尾），j=n-1（nums2的末尾），k=m+n-1（nums1的末尾）
 * 2. 比较nums1[i]和nums2[j]，将较大的元素放到nums1[k]的位置
 * 3. 递减相应的指针，重复步骤2直到处理完所有元素
 * 4. 如果nums2还有剩余元素，直接复制到nums1的前面（nums1剩余的元素已经在正确位置）
 * 
 * @param nums1 第一个数组，长度为m+n，前m个元素有效
 * @param m nums1中有效元素的个数
 * @param nums2 第二个数组，长度为n
 * @param n nums2中元素的个数
 */
void merge(std::vector<int>& nums1, int m, const std::vector<int>& nums2, int n) {
    // 防御性编程：检查输入合法性
    if (nums2.empty() || n == 0) {
        return;  // nums2为空，无需合并
    }
    if (nums1.empty()) {
        throw std::invalid_argument("nums1 cannot be empty");
    }
    if (nums1.size() < m + n) {
        throw std::invalid_argument("nums1 does not have enough space");
    }
    
    int i = m - 1;     // nums1有效元素的最后一个位置
    int j = n - 1;     // nums2的最后一个位置
    int k = m + n - 1; // nums1的最后一个位置
    
    // 从后向前合并，比较并放置较大的元素
    // 当两个数组都还有元素时进行比较
    while (i >= 0 && j >= 0) {
        if (nums1[i] > nums2[j]) {
            // nums1的元素较大，放到nums1的末尾
            nums1[k] = nums1[i];
            i--;
        } else {
            // nums2的元素较大或相等，放到nums1的末尾
            nums1[k] = nums2[j];
            j--;
        }
        k--;
    }
    
    // 如果nums2还有剩余元素，直接复制到nums1的前面
    // 注意：如果nums1还有剩余元素，它们已经在正确的位置上，无需处理
    while (j >= 0) {
        nums1[k] = nums2[j];
        j--;
        k--;
    }
}

/**
 * LeetCode 283. 移动零 - 最优解法（双指针法）
 * 题目链接：https://leetcode.cn/problems/move-zeroes/
 * 
 * 时间复杂度：O(n) - 仅需一次遍历
 * 空间复杂度：O(1) - 原地操作
 * 
 * 算法思想：
 * 使用两个指针，一个指向当前应该放置非零元素的位置，另一个遍历整个数组
 * 当遇到非零元素时，将其移动到第一个指针指向的位置，然后第一个指针前进
 * 
 * 算法步骤：
 * 1. 初始化一个指针nonZeroPos=0，表示下一个非零元素应该放置的位置
 * 2. 遍历数组，对于每个元素：
 *    a. 如果元素非零，将其移动到nonZeroPos位置，然后nonZeroPos++
 * 3. 遍历结束后，将nonZeroPos到数组末尾的所有元素设置为0
 * 
 * @param nums 待处理数组
 */
void moveZeroes(std::vector<int>& nums) {
    // 防御性编程：检查输入合法性
    if (nums.empty() || nums.size() <= 1) {
        return;
    }
    
    int nonZeroPos = 0;  // 下一个非零元素应该放置的位置
    
    // 第一步：将所有非零元素移动到数组前面
    // 遍历整个数组
    for (int i = 0; i < nums.size(); ++i) {
        if (nums[i] != 0) {
            // 将非零元素移动到nonZeroPos位置
            nums[nonZeroPos++] = nums[i];
        }
    }
    
    // 第二步：将剩余位置填充为0
    // 将nonZeroPos到数组末尾的所有位置设置为0
    for (int i = nonZeroPos; i < nums.size(); ++i) {
        nums[i] = 0;
    }
}

/**
 * LeetCode 283. 移动零 - 优化版本（一次遍历，更少的赋值操作）
 * 
 * 优化思路：
 * 当遇到非零元素时，直接与nonZeroPos位置交换，这样可以减少一些不必要的赋值操作
 * 特别是当数组中大部分元素都是非零时，这种方法更高效
 * 
 * @param nums 待处理数组
 */
void moveZeroesOptimized(std::vector<int>& nums) {
    // 防御性编程：检查输入合法性
    if (nums.empty() || nums.size() <= 1) {
        return;
    }
    
    int nonZeroPos = 0;  // 下一个非零元素应该放置的位置
    
    // 遍历数组
    for (int i = 0; i < nums.size(); ++i) {
        if (nums[i] != 0) {
            // 当两个指针不同时才交换，避免不必要的操作
            // 如果i == nonZeroPos，说明前面没有0，无需交换
            if (i != nonZeroPos) {
                swapVec(nums, i, nonZeroPos);
            }
            nonZeroPos++;
        }
    }
}

/**
 * LeetCode 215. 数组中的第K个最大元素 - 快速选择算法
 * 题目链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * 
 * 时间复杂度：O(n) - 平均情况，O(n²) - 最坏情况
 * 空间复杂度：O(log n) - 递归调用栈的深度，最坏情况为O(n)
 * 
 * 算法思想：
 * 基于快速排序的分区思想，每次分区后只递归处理包含第k大元素的那一半
 * 这样可以避免对整个数组进行排序
 * 
 * 算法步骤：
 * 1. 选择一个基准元素，将数组分为两部分：大于基准的和小于基准的
 * 2. 如果基准元素的位置正好是第k大的位置，返回该元素
 * 3. 否则，递归处理包含第k大元素的那一半
 * 
 * @param nums 数组
 * @param k 第k大元素（从1开始计数）
 * @return 第k大元素的值
 */
int findKthLargest(std::vector<int>& nums, int k) {
    // 防御性编程：检查输入合法性
    if (nums.empty() || k <= 0 || k > nums.size()) {
        throw std::invalid_argument("Invalid input");
    }
    
    // 第k大元素在排序后的数组中的索引是nums.size() - k
    // 例如：数组[1,2,3,4,5]中第2大的元素是4，其索引为5-2=3
    return quickSelect(nums, 0, nums.size() - 1, nums.size() - k);
}

/**
 * 快速选择算法的核心实现
 * 
 * @param nums 数组
 * @param left 左边界
 * @param right 右边界
 * @param k 目标索引（第k小的元素）
 * @return 第k小的元素值
 */
int quickSelect(std::vector<int>& nums, int left, int right, int k) {
    // 分区操作，返回基准元素的最终位置
    // pivotIndex是基准元素在数组中的最终位置
    int pivotIndex = partition(nums, left, right);
    
    // 如果基准元素的位置正好是k，返回该元素
    if (pivotIndex == k) {
        return nums[pivotIndex];
    }
    // 如果基准元素的位置大于k，递归处理左半部分
    else if (pivotIndex > k) {
        return quickSelect(nums, left, pivotIndex - 1, k);
    }
    // 如果基准元素的位置小于k，递归处理右半部分
    else {
        return quickSelect(nums, pivotIndex + 1, right, k);
    }
}

/**
 * 快速排序的分区操作
 * 
 * @param nums 数组
 * @param left 左边界
 * @param right 右边界
 * @return 基准元素的最终位置
 */
int partition(std::vector<int>& nums, int left, int right) {
    // 选择最右边的元素作为基准
    // 这是一种简单的选择策略，也可以使用随机选择来避免最坏情况
    int pivot = nums[right];
    // i表示小于基准元素的区域的边界
    // 初始时小于基准的区域为空，所以i = left - 1
    int i = left - 1;
    
    // 遍历[left, right-1]范围内的元素
    for (int j = left; j < right; ++j) {
        // 如果当前元素小于基准元素，将其交换到小于区域
        if (nums[j] <= pivot) {
            // 扩展小于基准的区域
            i++;
            swapVec(nums, i, j);
        }
    }
    
    // 将基准元素放到正确的位置
    // 此时i+1是基准元素应该放置的位置
    swapVec(nums, i + 1, right);
    return i + 1;
}

// =========================================================================
// 算法调试与辅助函数
// =========================================================================

/**
 * 检查数组是否已排序
 * @param arr 数组
 * @return 是否已排序
 */
bool isSorted(const std::vector<int>& arr) {
    for (int i = 1; i < arr.size(); ++i) {
        if (arr[i] < arr[i - 1]) {
            return false;
        }
    }
    return true;
}

/**
 * 生成随机测试数组
 * @param size 数组大小
 * @return 随机数组
 */
std::vector<int> generateRandomArray(int size) {
    std::vector<int> arr;
    if (size <= 0) {
        return arr;
    }
    
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> distrib(0, size * 10);
    
    arr.reserve(size);
    for (int i = 0; i < size; ++i) {
        arr.push_back(distrib(gen));
    }
    
    return arr;
}

/**
 * 复制数组
 * @param arr 原数组
 * @return 复制的数组
 */
std::vector<int> copyArray(const std::vector<int>& arr) {
    return arr;
}

/**
 * 打印数组
 * @param arr 数组
 * @param message 描述信息
 */
void printArrayDetails(const std::vector<int>& arr, const std::string& message) {
    std::cout << message << std::endl;
    if (arr.empty()) {
        std::cout << "Array is empty" << std::endl;
        return;
    }
    
    std::cout << "[";
    for (int i = 0; i < arr.size(); ++i) {
        std::cout << arr[i];
        if (i < arr.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
    std::cout << "Length: " << arr.size() << std::endl;
    std::cout << "First element: " << arr[0] << std::endl;
    std::cout << "Last element: " << arr.back() << std::endl;
    std::cout << std::endl;
}

/**
 * 分析排序算法的性能指标
 * @param arr 要排序的数组
 * @param sortMethod 排序方法名称
 */
void analyzeSortPerformance(const std::vector<int>& arr, const std::string& sortMethod) {
    // 创建数组副本，避免修改原数组
    std::vector<int> arrCopy = copyArray(arr);
    
    std::cout << "=== " << sortMethod << " 性能分析 ===" << std::endl;
    std::cout << "数组大小: " << arrCopy.size() << std::endl;
    
    // 测量排序前是否已排序
    bool wasSorted = isSorted(arrCopy);
    std::cout << "排序前是否有序: " << (wasSorted ? "是" : "否") << std::endl;
    
    // 测量排序时间
    auto startTime = std::chrono::high_resolution_clock::now();
    
    // 根据方法名选择排序算法
    if (sortMethod == "选择排序") {
        selectionSort(arrCopy);
    } else if (sortMethod == "冒泡排序") {
        bubbleSort(arrCopy);
    } else if (sortMethod == "插入排序") {
        insertionSort(arrCopy);
    } else if (sortMethod == "优化插入排序") {
        insertionSortOptimized(arrCopy);
    } else if (sortMethod == "二分插入排序") {
        binaryInsertionSort(arrCopy);
    } else {
        std::cout << "未知的排序方法" << std::endl;
        return;
    }
    
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime).count() / 1000.0;
    std::cout << "排序耗时: " << duration << " ms" << std::endl;
    
    // 验证排序结果
    bool isSortedFlag = isSorted(arrCopy);
    std::cout << "排序结果是否正确: " << (isSortedFlag ? "是" : "否") << std::endl;
    std::cout << std::endl;
}

// =========================================================================
// 工程化改造示例 - 将排序算法封装为可复用组件
// =========================================================================

class SortUtils {
public:
    /**
     * 排序算法枚举
     */
    enum class SortAlgorithm {
        SELECTION_SORT,
        BUBBLE_SORT,
        INSERTION_SORT,
        INSERTION_SORT_OPTIMIZED,
        BINARY_INSERTION_SORT
    };
    
    /**
     * 统一的排序接口
     * @param arr 要排序的数组
     * @param algorithm 选择的排序算法
     */
    static void sort(std::vector<int>& arr, SortAlgorithm algorithm) {
        // 防御性编程
        if (arr.empty() || arr.size() < 2) {
            return;
        }
        
        // 根据选择的算法调用相应的排序方法
        switch (algorithm) {
            case SortAlgorithm::SELECTION_SORT:
                selectionSort(arr);
                break;
            case SortAlgorithm::BUBBLE_SORT:
                bubbleSort(arr);
                break;
            case SortAlgorithm::INSERTION_SORT:
                insertionSort(arr);
                break;
            case SortAlgorithm::INSERTION_SORT_OPTIMIZED:
                insertionSortOptimized(arr);
                break;
            case SortAlgorithm::BINARY_INSERTION_SORT:
                binaryInsertionSort(arr);
                break;
            default:
                throw std::invalid_argument("Unsupported sorting algorithm");
        }
    }
    
    /**
     * 根据数据特征自动选择最合适的排序算法
     * @param arr 要排序的数组
     */
    static void autoSelectSort(std::vector<int>& arr) {
        // 防御性编程
        if (arr.empty() || arr.size() < 2) {
            return;
        }
        
        // 分析数据特征
        int n = arr.size();
        bool isNearlySorted = _isNearlySorted(arr);
        bool hasFewUnique = _hasFewUniqueValues(arr);
        
        // 根据数据特征选择算法
        if (isNearlySorted) {
            // 接近有序的数据使用插入排序
            sort(arr, SortAlgorithm::INSERTION_SORT_OPTIMIZED);
        } else if (n < 1000) {
            // 小规模数据使用插入排序
            sort(arr, SortAlgorithm::INSERTION_SORT_OPTIMIZED);
        } else {
            // 其他情况使用二分插入排序
            sort(arr, SortAlgorithm::BINARY_INSERTION_SORT);
        }
    }
    
private:
    /**
     * 判断数组是否接近有序
     * @param arr 要检查的数组
     * @return 如果数组接近有序返回true，否则返回false
     */
    static bool _isNearlySorted(const std::vector<int>& arr) {
        int inversionCount = 0;
        int threshold = arr.size() / 2;  // 阈值：逆序对数量不超过数组长度的一半
        
        // 计算逆序对数量
        for (int i = 0; i < arr.size() - 1; ++i) {
            for (int j = i + 1; j < arr.size(); ++j) {
                if (arr[i] > arr[j]) {
                    inversionCount++;
                    // 如果超过阈值，提前返回
                    if (inversionCount >= threshold) {
                        return false;
                    }
                }
            }
        }
        
        return inversionCount < threshold;
    }
    
    /**
     * 判断数组是否有少量唯一值
     * @param arr 要检查的数组
     * @return 如果数组有少量唯一值返回true，否则返回false
     */
    static bool _hasFewUniqueValues(const std::vector<int>& arr) {
        // 简单实现：检查是否有超过25%的重复元素
        std::set<int> uniqueValues(arr.begin(), arr.end());
        return uniqueValues.size() < arr.size() * 0.25;
    }
};

// =========================================================================
// 测试函数
// =========================================================================

/**
 * 测试排序算法的正确性
 */
void testSortAlgorithms() {
    std::cout << "=== 选择排序、冒泡排序、插入排序测试 ===" << std::endl;
    
    // 测试用例设计
    std::vector<std::vector<int>> testCases = {
        {},                           // 空数组
        {1},                          // 单元素
        {1, 2, 3},                    // 已排序
        {3, 2, 1},                    // 逆序
        {1, 1, 1},                    // 全相同
        {5, 2, 8, 1, 9},             // 普通情况
        {3, 1, 4, 1, 5, 9, 2, 6}     // 重复元素
    };
    
    std::vector<std::string> algorithmNames = {
        "选择排序", 
        "冒泡排序", 
        "插入排序",
        "优化插入排序",
        "二分插入排序"
    };
    
    std::vector<void(*)(std::vector<int>&)> sortFunctions = {
        selectionSort,
        bubbleSort,
        insertionSort,
        insertionSortOptimized,
        binaryInsertionSort
    };
    
    for (int i = 0; i < testCases.size(); ++i) {
        std::cout << "\n测试用例 " << i + 1 << ": ";
        printArrayDetails(testCases[i], "");
        
        for (int j = 0; j < algorithmNames.size(); ++j) {
            std::vector<int> arrCopy = copyArray(testCases[i]);
            std::vector<int> expected = copyArray(testCases[i]);
            std::sort(expected.begin(), expected.end());  // 使用系统排序作为基准
            
            sortFunctions[j](arrCopy);
            
            bool correct = (arrCopy == expected);
            std::cout << algorithmNames[j] << ": " 
                      << (correct ? "✓" : "✗") << std::endl;
        }
    }
}

/**
 * 性能测试：比较各种排序算法在不同数据规模下的表现
 */
void performanceTest() {
    std::cout << "\n=== 性能测试 ===" << std::endl;
    
    std::vector<int> sizes = {100, 500, 1000};
    std::vector<std::string> algorithmNames = {
        "选择排序", 
        "冒泡排序", 
        "插入排序",
        "优化插入排序",
        "二分插入排序"
    };
    
    for (int size : sizes) {
        std::cout << "\n数组大小: " << size << std::endl;
        std::vector<int> data = generateRandomArray(size);
        
        for (const std::string& algorithm : algorithmNames) {
            analyzeSortPerformance(data, algorithm);
        }
    }
}

/**
 * 主函数
 */
int main() {
    // 测试排序算法的正确性
    testSortAlgorithms();
    
    // 性能测试
    performanceTest();
    
    // 测试经典题目解法
    std::cout << "\n=== 经典题目解法测试 ===" << std::endl;
    
    // 测试颜色分类
    std::vector<int> colors = {2, 0, 2, 1, 1, 0};
    std::cout << "颜色分类测试: " << std::endl;
    printArrayDetails(colors, "排序前");
    sortColors(colors);
    printArrayDetails(colors, "排序后");
    
    // 测试移动零
    std::vector<int> zeros = {0, 1, 0, 3, 12};
    std::cout << "移动零测试: " << std::endl;
    printArrayDetails(zeros, "操作前");
    moveZeroesOptimized(zeros);
    printArrayDetails(zeros, "操作后");
    
    // 测试找第K大元素
    std::vector<int> kthTest = {3, 2, 1, 5, 6, 4};
    int k = 2;
    std::cout << "找第" << k << "大元素测试: " << std::endl;
    printArrayDetails(kthTest, "原始数组");
    std::vector<int> kthCopy = copyArray(kthTest);
    int result = findKthLargest(kthCopy, k);
    std::cout << "结果: " << result << std::endl;
    
    // 测试额外的题目
    testAdditionalProblems();
    
    return 0;
}

// =========================================================================
// 更多经典题目的实现 - 涉及各大算法平台的高频题目
// =========================================================================

/**
 * LeetCode 347. 前 K 个高频元素
 * 题目链接：https://leetcode.cn/problems/top-k-frequent-elements/
 * 
 * 题目描述：给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素
 * 示例：
 * 输入: nums = [1,1,1,2,2,3], k = 2
 * 输出: [1,2]
 * 
 * 解题思路：
 * 1. 使用哈希表统计每个元素的频率
 * 2. 使用堆（优先队列）或桶排序找到频率最高的 k 个元素
 * 
 * 时间复杂度：O(n log k) - 使用最小堆
 * 空间复杂度：O(n) - 哈希表存储频率
 * 
 * 最优解：桶排序，时间复杂度 O(n)，空间复杂度 O(n)
 * 
 * @param nums 输入数组
 * @param k 需要返回的高频元素个数
 * @return 前k个高频元素
 */
std::vector<int> topKFrequent(const std::vector<int>& nums, int k) {
    // 防御性编程
    if (nums.empty() || k <= 0 || k > nums.size()) {
        throw std::invalid_argument("Invalid input");
    }
    
    // 步骤1：统计频率
    // 使用map记录每个元素的出现次数
    std::map<int, int> freqMap;
    for (int num : nums) {
        freqMap[num]++;
    }
    
    // 步骤2：桶排序 - 按频率分组
    // bucket[i] 存储频率为 i 的所有元素
    // 桶的数量为nums.size()+1，因为频率最大为nums.size()
    std::vector<std::vector<int>> bucket(nums.size() + 1);
    for (const auto& pair : freqMap) {
        int num = pair.first;
        int freq = pair.second;
        bucket[freq].push_back(num);
    }
    
    // 步骤3：从高频到低频收集结果
    std::vector<int> result;
    // 从最大频率开始向下遍历
    for (int i = bucket.size() - 1; i >= 0 && result.size() < k; i--) {
        if (!bucket[i].empty()) {
            // 将当前频率的所有元素添加到结果中
            for (int num : bucket[i]) {
                result.push_back(num);
                // 当收集到k个元素时停止
                if (result.size() == k) break;
            }
        }
    }
    
    return result;
}

/**
 * LeetCode 1122. 数组的相对排序
 * 题目链接：https://leetcode.cn/problems/relative-sort-array/
 * 
 * 题目描述：给你两个数组，arr1 和 arr2，arr2 中的元素各不相同，arr2 中的每个元素都出现在 arr1 中。
 * 对 arr1 中的元素进行排序，使 arr1 中项的相对顺序和 arr2 中的相对顺序相同。
 * 未在 arr2 中出现过的元素需要按照升序放在 arr1 的末尾。
 * 
 * 示例：
 * 输入：arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
 * 输出：[2,2,2,1,4,3,3,9,6,7,19]
 * 
 * 解题思路：
 * 1. 使用计数排序思想，统计 arr1 中每个元素的出现次数
 * 2. 按照 arr2 的顺序填充结果数组
 * 3. 将不在 arr2 中的元素排序后放在末尾
 * 
 * 时间复杂度：O(n log n) - 主要是排序不在 arr2 中的元素
 * 空间复杂度：O(n) - 哈希表和结果数组
 * 
 * 最优解：计数排序，时间复杂度 O(n + m)，其中 n 是 arr1 的长度，m 是数值范围
 * 
 * @param arr1 第一个数组
 * @param arr2 第二个数组，元素各不相同且都出现在arr1中
 * @return 按照arr2相对顺序排序的arr1
 */
std::vector<int> relativeSortArray(const std::vector<int>& arr1, const std::vector<int>& arr2) {
    // 防御性编程
    if (arr1.empty()) {
        return {};
    }
    if (arr2.empty()) {
        // 如果arr2为空，直接返回排序后的arr1
        std::vector<int> result = arr1;
        std::sort(result.begin(), result.end());
        return result;
    }
    
    // 步骤1：统计 arr1 中每个元素的频率
    std::map<int, int> countMap;
    for (int num : arr1) {
        countMap[num]++;
    }
    
    // 步骤2：按照 arr2 的顺序填充结果
    std::vector<int> result;
    // 按照arr2中元素的顺序处理
    for (int num : arr2) {
        if (countMap.find(num) != countMap.end()) {
            // 将countMap[num]个num添加到结果中
            int count = countMap[num];
            for (int i = 0; i < count; i++) {
                result.push_back(num);
            }
            // 从countMap中删除已处理的元素
            countMap.erase(num);
        }
    }
    
    // 步骤3：将不在 arr2 中的元素排序后放在末尾
    std::vector<int> remaining;
    // 收集剩余的元素
    for (const auto& pair : countMap) {
        int num = pair.first;
        int count = pair.second;
        for (int i = 0; i < count; i++) {
            remaining.push_back(num);
        }
    }
    // 对剩余元素进行排序
    std::sort(remaining.begin(), remaining.end());
    // 将排序后的剩余元素添加到结果末尾
    for (int num : remaining) {
        result.push_back(num);
    }
    
    return result;
}

/**
 * 剑指 Offer 40. 最小的k个数
 * 题目链接：https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 * 
 * 题目描述：输入整数数组 arr ，找出其中最小的 k 个数
 * 示例：
 * 输入：arr = [3,2,1], k = 2
 * 输出：[1,2] 或者 [2,1]
 * 
 * 解题思路：
 * 方法1：排序后取前k个 - 时间复杂度 O(n log n)
 * 方法2：堆（最大堆）- 时间复杂度 O(n log k)
 * 方法3：快速选择算法 - 平均时间复杂度 O(n)
 * 
 * 最优解：快速选择算法
 * 时间复杂度：O(n) - 平均情况
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * @param arr 输入数组
 * @param k 需要返回的最小元素个数
 * @return 最小的k个数
 */
std::vector<int> getLeastNumbers(std::vector<int>& arr, int k) {
    // 防御性编程
    if (arr.empty() || k <= 0 || k > arr.size()) {
        return {};
    }
    
    // 使用快速选择找到第k小的元素
    // 由于是找最小的k个数，不需要完全排序
    quickSelect(arr, 0, arr.size() - 1, k - 1);
    
    // 前k个元素就是结果
    std::vector<int> result(arr.begin(), arr.begin() + k);
    // 对结果进行排序以满足题目要求
    std::sort(result.begin(), result.end());
    
    return result;
}

/**
 * LeetCode 506. 相对名次
 * 题目链接：https://leetcode.cn/problems/relative-ranks/
 * 
 * 题目描述：给你一个长度为 n 的整数数组 score ，其中 score[i] 表示第 i 位运动员在比赛中的得分。
 * 所有得分都互不相同。运动员将根据得分决定名次，其中名次第 1 的运动员得分最高，名次第 2 的运动员得分第 2 高，依此类推。
 * 
 * 示例：
 * 输入：score = [5,4,3,2,1]
 * 输出：["Gold Medal","Silver Medal","Bronze Medal","4","5"]
 * 
 * 解题思路：
 * 1. 创建索引数组，按分数排序
 * 2. 根据排序后的索引分配名次
 * 
 * 时间复杂度：O(n log n) - 排序的时间复杂度
 * 空间复杂度：O(n) - 存储索引和结果
 * 
 * @param score 分数数组
 * @return 每个运动员的名次
 */
std::vector<std::string> findRelativeRanks(const std::vector<int>& score) {
    // 防御性编程
    if (score.empty()) {
        return {};
    }
    
    int n = score.size();
    // 创建索引数组，用于排序后找到原始位置
    // indices[i]表示原始数组中第i个位置的索引
    std::vector<int> indices(n);
    for (int i = 0; i < n; i++) {
        indices[i] = i;
    }
    
    // 按分数从高到低排序索引
    // 使用lambda表达式定义比较函数
    // 按score[indices[i]]的值进行降序排列
    std::sort(indices.begin(), indices.end(), [&score](int a, int b) {
        return score[a] > score[b];
    });
    
    // 根据排序后的索引分配名次
    std::vector<std::string> result(n);
    for (int i = 0; i < n; i++) {
        int idx = indices[i];
        // 根据排名分配奖牌或名次
        if (i == 0) {
            result[idx] = "Gold Medal";
        } else if (i == 1) {
            result[idx] = "Silver Medal";
        } else if (i == 2) {
            result[idx] = "Bronze Medal";
        } else {
            // 第4名及以后用数字表示
            result[idx] = std::to_string(i + 1);
        }
    }
    
    return result;
}

/**
 * LeetCode 922. 按奇偶排序数组 II
 * 题目链接：https://leetcode.cn/problems/sort-array-by-parity-ii/
 * 
 * 题目描述：给定一个非负整数数组 nums，nums 中一半整数是奇数，一半整数是偶数。
 * 对数组进行排序，以便当 nums[i] 为奇数时，i 也是奇数；当 nums[i] 为偶数时， i 也是偶数。
 * 
 * 示例：
 * 输入：nums = [4,2,5,7]
 * 输出：[4,5,2,7]
 * 
 * 解题思路：
 * 方法1：使用两个数组分别存储奇数和偶数，然后按要求放回
 * 方法2：双指针原地交换
 * 
 * 最优解：双指针原地交换
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * @param nums 输入数组
 * @return 满足条件的数组
 */
std::vector<int> sortArrayByParityII(std::vector<int>& nums) {
    // 防御性编程
    if (nums.empty()) {
        return nums;
    }
    
    int n = nums.size();
    int evenIdx = 0; // 偶数位置指针，用于寻找应该放偶数但放了奇数的位置
    int oddIdx = 1;  // 奇数位置指针，用于寻找应该放奇数但放了偶数的位置
    
    // 当两个指针都在有效范围内时继续循环
    while (evenIdx < n && oddIdx < n) {
        // 找到偶数位置上的奇数
        // 在偶数位置（0,2,4...）上寻找奇数
        while (evenIdx < n && nums[evenIdx] % 2 == 0) {
            evenIdx += 2;
        }
        // 找到奇数位置上的偶数
        // 在奇数位置（1,3,5...）上寻找偶数
        while (oddIdx < n && nums[oddIdx] % 2 == 1) {
            oddIdx += 2;
        }
        // 交换
        // 如果找到了两个错误位置，进行交换
        if (evenIdx < n && oddIdx < n) {
            swapVec(nums, evenIdx, oddIdx);
            // 交换后继续寻找下一个错误位置
            evenIdx += 2;
            oddIdx += 2;
        }
    }
    
    return nums;
}

/**
 * 测试额外的题目解法
 */
void testAdditionalProblems() {
    std::cout << "\n=== 额外算法题目测试 ===\n" << std::endl;
    
    // 测试 topKFrequent
    std::cout << "--- LeetCode 347. 前 K 个高频元素 ---" << std::endl;
    std::vector<int> freqTest = {1, 1, 1, 2, 2, 3};
    std::vector<int> freqResult = topKFrequent(freqTest, 2);
    std::cout << "输入: [1,1,1,2,2,3], k=2" << std::endl;
    std::cout << "输出: [";
    for (size_t i = 0; i < freqResult.size(); i++) {
        std::cout << freqResult[i];
        if (i < freqResult.size() - 1) std::cout << ",";
    }
    std::cout << "]" << std::endl << std::endl;
    
    // 测试 relativeSortArray
    std::cout << "--- LeetCode 1122. 数组的相对排序 ---" << std::endl;
    std::vector<int> arr1 = {2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19};
    std::vector<int> arr2 = {2, 1, 4, 3, 9, 6};
    std::vector<int> relativeResult = relativeSortArray(arr1, arr2);
    std::cout << "输入: arr1=[2,3,1,3,2,4,6,7,9,2,19], arr2=[2,1,4,3,9,6]" << std::endl;
    std::cout << "输出: [";
    for (size_t i = 0; i < relativeResult.size(); i++) {
        std::cout << relativeResult[i];
        if (i < relativeResult.size() - 1) std::cout << ",";
    }
    std::cout << "]" << std::endl << std::endl;
    
    // 测试 getLeastNumbers
    std::cout << "--- 剑指 Offer 40. 最小的k个数 ---" << std::endl;
    std::vector<int> leastTest = {3, 2, 1, 5, 6, 4};
    std::vector<int> leastResult = getLeastNumbers(leastTest, 2);
    std::cout << "输入: [3,2,1,5,6,4], k=2" << std::endl;
    std::cout << "输出: [";
    for (size_t i = 0; i < leastResult.size(); i++) {
        std::cout << leastResult[i];
        if (i < leastResult.size() - 1) std::cout << ",";
    }
    std::cout << "]" << std::endl << std::endl;
    
    // 测试 findRelativeRanks
    std::cout << "--- LeetCode 506. 相对名次 ---" << std::endl;
    std::vector<int> scoreTest = {5, 4, 3, 2, 1};
    std::vector<std::string> rankResult = findRelativeRanks(scoreTest);
    std::cout << "输入: [5,4,3,2,1]" << std::endl;
    std::cout << "输出: [";
    for (size_t i = 0; i < rankResult.size(); i++) {
        std::cout << "\"" << rankResult[i] << "\"";
        if (i < rankResult.size() - 1) std::cout << ",";
    }
    std::cout << "]" << std::endl << std::endl;
    
    // 测试 sortArrayByParityII
    std::cout << "--- LeetCode 922. 按奇偶排序数组 II ---" << std::endl;
    std::vector<int> parityTest = {4, 2, 5, 7};
    std::vector<int> parityResult = sortArrayByParityII(parityTest);
    std::cout << "输入: [4,2,5,7]" << std::endl;
    std::cout << "输出: [";
    for (size_t i = 0; i < parityResult.size(); i++) {
        std::cout << parityResult[i];
        if (i < parityResult.size() - 1) std::cout << ",";
    }
    std::cout << "]" << std::endl << std::endl;
}