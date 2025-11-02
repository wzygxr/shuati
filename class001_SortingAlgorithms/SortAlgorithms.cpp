/**
 * 排序算法实现 - C++版本
 * 包含归并排序、快速排序、堆排序的完整实现
 * 时间复杂度、空间复杂度详细分析
 * 工程化考量：异常处理、边界条件、性能优化
 * 
 * 时间复杂度分析：
 * - 归并排序: O(n log n) 平均和最坏情况
 * - 快速排序: O(n log n) 平均, O(n²) 最坏
 * - 堆排序: O(n log n) 平均和最坏情况
 * 
 * 空间复杂度分析：
 * - 归并排序: O(n) 需要辅助数组
 * - 快速排序: O(log n) 递归栈空间
 * - 堆排序: O(1) 原地排序
 * 
 * 稳定性分析：
 * - 归并排序: 稳定
 * - 快速排序: 不稳定
 * - 堆排序: 不稳定
 * 
 * 题目相关:
 * - 912. 排序数组: https://leetcode.cn/problems/sort-an-array/
 * - 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * - 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
 * - 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
 * - ALDS1_2_A: Bubble Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_A
 * - ALDS1_2_B: Selection Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_B
 * - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
 * - ALDS1_2_D: Shell Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_D
 * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
 * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
 * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
 * - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
 * - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
 * - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
 * - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
 * - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
 * 
 * 工程化考量：
 * - 异常处理：对空数组、单元素数组进行特殊处理
 * - 边界条件：处理各种边界情况，如已排序、逆序、全相同数组
 * - 性能优化：
 *   - 快速排序的小数组优化（长度小于16时使用插入排序）
 *   - 三数取中法选择基准值避免最坏情况
 *   - 随机化避免最坏情况
 *   - 归并排序提供迭代版本避免栈溢出
 * - 内存管理：合理使用临时空间，避免不必要的拷贝
 * - 可读性：清晰的注释和命名规范
 * 
 * 算法选择建议：
 * - 小数据量：插入排序
 * - 一般情况：快速排序（带优化）
 * - 需要稳定排序：归并排序
 * - 内存受限：堆排序
 * - 最坏情况要求：堆排序
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>
#include <cassert>
#include <queue>
#include <functional>
#include <memory>

using namespace std;

class SortAlgorithms {
public:
    /**
     * 归并排序 - 递归版本
     * 时间复杂度: O(n log n) - 在所有情况下都是这个复杂度，包括最好、平均和最坏情况
     * 空间复杂度: O(n) - 需要一个与原数组相同大小的辅助数组
     * 稳定性: 稳定 - 相等元素的相对位置在排序后不会改变
     * 
     * 算法原理：
     * 1. 分治法：将数组不断二分直到只有一个元素
     * 2. 合并：将两个有序数组合并成一个有序数组
     * 3. 递归处理：自底向上构建有序数组
     * 
     * 适用场景：
     * - 需要稳定排序
     * - 链表排序
     * - 外部排序（数据量大无法全部加载到内存）
     * 
     * 相关题目：
     * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
     * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
     * 
     * 工程化考量：
     * - 递归深度：可能导致栈溢出，可使用迭代版本
     * - 内存使用：需要额外O(n)空间
     * - 缓存友好性：相对较好
     * - 可以复用辅助数组避免频繁创建销毁
     * 
     * @param nums 待排序数组
     */
    static void mergeSort(vector<int>& nums) {
        // 边界条件检查
        if (nums.size() <= 1) return;
        
        vector<int> temp(nums.size());
        mergeSortHelper(nums, temp, 0, nums.size() - 1);
    }
    
private:
    static void mergeSortHelper(vector<int>& nums, vector<int>& temp, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        
        // 分治递归
        mergeSortHelper(nums, temp, left, mid);
        mergeSortHelper(nums, temp, mid + 1, right);
        
        // 合并有序数组
        merge(nums, temp, left, mid, right);
    }
    
    static void merge(vector<int>& nums, vector<int>& temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        
        // 复制到临时数组
        for (int idx = left; idx <= right; idx++) {
            temp[idx] = nums[idx];
        }
        
        // 合并两个有序数组
        while (i <= mid && j <= right) {
            // 注意：使用<=保证稳定性
            if (temp[i] <= temp[j]) {
                nums[k++] = temp[i++];
            } else {
                nums[k++] = temp[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            nums[k++] = temp[i++];
        }
        while (j <= right) {
            nums[k++] = temp[j++];
        }
    }
    
public:
    /**
     * 归并排序 - 迭代版本（自底向上）
     * 避免递归调用，节省栈空间
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 稳定性: 稳定
     * 
     * 相关题目：
     * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
     * 
     * 工程化考量：
     * - 避免递归深度过大导致的栈溢出
     * - 适合处理大数据集
     * - 缓存局部性可能不如递归版本
     */
    static void mergeSortIterative(vector<int>& nums) {
        int n = nums.size();
        if (n <= 1) return;
        
        vector<int> temp(n);
        
        // 从1开始，每次倍增
        for (int size = 1; size < n; size *= 2) {
            for (int left = 0; left < n - size; left += 2 * size) {
                int mid = left + size - 1;
                int right = min(left + 2 * size - 1, n - 1);
                merge(nums, temp, left, mid, right);
            }
        }
    }
    
    /**
     * 快速排序 - 基础版本
     * 时间复杂度: O(n log n) 平均, O(n²) 最坏
     * 空间复杂度: O(log n) 平均, O(n) 最坏（递归栈）
     * 稳定性: 不稳定
     * 
     * 优化策略: 三数取中、随机化、小数组优化
     * 
     * 相关题目：
     * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
     * - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
     * 
     * 工程化考量：
     * - 平均性能优秀，实际应用广泛
     * - 最坏情况需要避免，可通过随机化或三数取中
     * - 小数组可使用插入排序优化
     */
    static void quickSort(vector<int>& nums) {
        if (nums.size() <= 1) return;
        
        // 随机化避免最坏情况
        random_device rd;
        mt19937 gen(rd());
        shuffle(nums.begin(), nums.end(), gen);
        
        quickSortHelper(nums, 0, nums.size() - 1);
    }
    
private:
    static void quickSortHelper(vector<int>& nums, int low, int high) {
        // 小数组优化：使用插入排序
        if (high - low + 1 <= 16) {
            insertionSort(nums, low, high);
            return;
        }
        
        if (low < high) {
            int pivotIndex = partition(nums, low, high);
            quickSortHelper(nums, low, pivotIndex - 1);
            quickSortHelper(nums, pivotIndex + 1, high);
        }
    }
    
    static int partition(vector<int>& nums, int low, int high) {
        // 三数取中法选择基准
        int mid = low + (high - low) / 2;
        if (nums[mid] < nums[low]) swap(nums[low], nums[mid]);
        if (nums[high] < nums[low]) swap(nums[low], nums[high]);
        if (nums[high] < nums[mid]) swap(nums[mid], nums[high]);
        
        int pivot = nums[mid];
        swap(nums[mid], nums[high]); // 将基准放到最后
        
        int i = low;
        for (int j = low; j < high; j++) {
            if (nums[j] <= pivot) {
                swap(nums[i], nums[j]);
                i++;
            }
        }
        swap(nums[i], nums[high]);
        return i;
    }
    
    /**
     * 快速排序 - 三路划分版本
     * 针对大量重复元素的优化
     * 时间复杂度: O(n log n) 平均
     * 空间复杂度: O(log n) 平均
     * 稳定性: 不稳定
     * 
     * 工程化考量：
     * - 对大量重复元素的数据集性能优秀
     * - 相比普通快排，减少了不必要的比较和交换
     */
    static void quickSort3Way(vector<int>& nums, int low, int high) {
        if (low >= high) return;
        
        // 随机选择基准
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(low, high);
        int pivotIndex = dis(gen);
        int pivot = nums[pivotIndex];
        
        int lt = low;      // 小于区域的右边界
        int gt = high;     // 大于区域的左边界
        int i = low;       // 当前指针
        
        while (i <= gt) {
            if (nums[i] < pivot) {
                swap(nums[lt++], nums[i++]);
            } else if (nums[i] > pivot) {
                swap(nums[i], nums[gt--]);
            } else {
                i++;
            }
        }
        
        quickSort3Way(nums, low, lt - 1);
        quickSort3Way(nums, gt + 1, high);
    }
    
    /**
     * 插入排序 - 用于小数组优化
     * 时间复杂度: O(n²) 最坏, O(n) 最好（已排序）
     * 空间复杂度: O(1)
     * 稳定性: 稳定
     * 
     * 工程化考量：
     * - 对小数组效率高，常数因子小
     * - 稳定排序算法
     * - 适合部分有序的数据
     */
    static void insertionSort(vector<int>& nums, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = nums[i];
            int j = i - 1;
            
            // 将大于key的元素向右移动
            while (j >= low && nums[j] > key) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = key;
        }
    }
    
public:
    /**
     * 堆排序
     * 时间复杂度: O(n log n) - 在所有情况下都是这个复杂度
     * 空间复杂度: O(1) - 原地排序算法
     * 稳定性: 不稳定 - 元素的相对位置可能改变
     * 
     * 优势: 原地排序、最坏情况O(n log n)
     * 劣势: 缓存不友好、常数项较大
     * 
     * 相关题目：
     * - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
     * - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
     * - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
     * 
     * 工程化考量：
     * - 原地排序，内存使用效率高
     * - 最坏情况时间复杂度有保证
     * - 常数因子相对较大，实际性能可能不如快速排序
     * - 缓存局部性较差
     */
    static void heapSort(vector<int>& nums) {
        int n = nums.size();
        if (n <= 1) return;
        
        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(nums, n, i);
        }
        
        // 逐个提取最大元素
        for (int i = n - 1; i > 0; i--) {
            swap(nums[0], nums[i]);  // 将当前最大元素移到末尾
            heapify(nums, i, 0);     // 调整剩余堆
        }
    }
    
private:
    static void heapify(vector<int>& nums, int n, int i) {
        int largest = i;        // 初始化最大元素为根
        int left = 2 * i + 1;    // 左子节点
        int right = 2 * i + 2;   // 右子节点
        
        // 如果左子节点大于根
        if (left < n && nums[left] > nums[largest]) {
            largest = left;
        }
        
        // 如果右子节点大于当前最大
        if (right < n && nums[right] > nums[largest]) {
            largest = right;
        }
        
        // 如果最大元素不是根
        if (largest != i) {
            swap(nums[i], nums[largest]);
            // 递归调整受影响的子树
            heapify(nums, n, largest);
        }
    }
    
public:
    /**
     * 冒泡排序 - 基础版本（用于对比）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(1)
     * 稳定性: 稳定
     * 
     * 工程化考量：
     * - 简单易懂，适合教学
     * - 性能较差，不适用于实际项目
     * - 稳定排序算法
     */
    static void bubbleSort(vector<int>& nums) {
        int n = nums.size();
        for (int i = 0; i < n - 1; i++) {
            bool swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    swap(nums[j], nums[j + 1]);
                    swapped = true;
                }
            }
            // 如果没有交换，说明已经有序
            if (!swapped) break;
        }
    }
    
    /**
     * 选择排序 - 基础版本（用于对比）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(1)
     * 稳定性: 不稳定
     * 
     * 工程化考量：
     * - 简单易懂，适合教学
     * - 性能较差，不适用于实际项目
     * - 不稳定排序算法
     * - 交换次数少，适合交换代价高的场景
     */
    static void selectionSort(vector<int>& nums) {
        int n = nums.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }
            swap(nums[i], nums[minIndex]);
        }
    }
    
    /**
     * 测试函数 - 验证排序算法的正确性
     */
    static void testAllAlgorithms() {
        cout << "=== 排序算法测试 ===" << endl;
        
        // 测试用例
        vector<vector<int>> testCases = {
            {},                            // 空数组
            {1},                           // 单元素
            {3, 1, 2},                     // 小数组
            {5, 2, 8, 1, 9, 3},           // 中等数组
            {1, 2, 3, 4, 5},              // 已排序
            {5, 4, 3, 2, 1},              // 逆序
            {4, 2, 2, 8, 3, 3, 1},        // 重复元素
            {1, 1, 1, 1, 1}               // 全相同
        };
        
        vector<string> algorithmNames = {
            "归并排序", "快速排序", "堆排序", "冒泡排序", "选择排序"
        };
        
        vector<function<void(vector<int>&)>> algorithms = {
            mergeSort, quickSort, heapSort, bubbleSort, selectionSort
        };
        
        for (int i = 0; i < testCases.size(); i++) {
            cout << "\n测试用例 " << (i + 1) << ": ";
            printVector(testCases[i]);
            
            for (int j = 0; j < algorithms.size(); j++) {
                vector<int> arr = testCases[i];
                vector<int> expected = testCases[i];
                sort(expected.begin(), expected.end()); // 标准库排序作为基准
                
                try {
                    algorithms[j](arr);
                    bool correct = isSorted(arr) && arr == expected;
                    cout << algorithmNames[j] << ": " 
                         << (correct ? "✓" : "✗") << " ";
                } catch (const exception& e) {
                    cout << algorithmNames[j] << ": 异常(" << e.what() << ") ";
                }
            }
            cout << endl;
        }
    }
    
    /**
     * 性能测试 - 比较不同算法的执行时间
     */
    static void performanceTest() {
        cout << "\n=== 性能测试 ===" << endl;
        
        vector<int> sizes = {100, 1000, 10000, 50000};
        vector<string> algorithmNames = {
            "归并排序", "快速排序", "堆排序", "std::sort"
        };
        
        vector<function<void(vector<int>&)>> algorithms = {
            mergeSort, quickSort, heapSort, 
            [](vector<int>& arr) { sort(arr.begin(), arr.end()); }
        };
        
        random_device rd;
        mt19937 gen(rd());
        
        for (int size : sizes) {
            cout << "\n数据规模: " << size << endl;
            
            // 生成随机测试数据
            vector<int> testData(size);
            uniform_int_distribution<> dis(0, size * 10);
            for (int i = 0; i < size; i++) {
                testData[i] = dis(gen);
            }
            
            for (int i = 0; i < algorithms.size(); i++) {
                vector<int> arr = testData;
                auto start = chrono::high_resolution_clock::now();
                
                algorithms[i](arr);
                
                auto end = chrono::high_resolution_clock::now();
                auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
                
                cout << algorithmNames[i] << ": " << duration.count() << " μs" << endl;
            }
        }
    }
    
private:
    /**
     * 辅助函数：检查数组是否已排序
     */
    static bool isSorted(const vector<int>& nums) {
        for (int i = 1; i < nums.size(); i++) {
            if (nums[i] < nums[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 辅助函数：打印数组
     */
    static void printVector(const vector<int>& nums) {
        cout << "[";
        for (int i = 0; i < nums.size(); i++) {
            cout << nums[i];
            if (i < nums.size() - 1) cout << ", ";
        }
        cout << "]";
    }
};

/**
 * 扩展问题：第K大元素（快速选择算法）
 */
class KthLargest {
public:
    /**
     * 快速选择算法 - 寻找第K大的元素
     * 时间复杂度: O(n) 平均, O(n²) 最坏
     * 空间复杂度: O(1)
     * 
     * 工程化考量：
     * - 平均性能优秀，是Top-K问题的最优解法
     * - 可通过随机化避免最坏情况
     * - 原地操作，空间效率高
     */
    static int findKthLargest(vector<int>& nums, int k) {
        if (nums.empty() || k < 1 || k > nums.size()) {
            throw invalid_argument("Invalid input");
        }
        
        int left = 0, right = nums.size() - 1;
        k = nums.size() - k; // 转换为第k小的索引
        
        random_device rd;
        mt19937 gen(rd());
        
        while (left <= right) {
            uniform_int_distribution<> dis(left, right);
            int pivotIndex = dis(gen);
            int pivotPos = partition(nums, left, right, pivotIndex);
            
            if (pivotPos == k) {
                return nums[pivotPos];
            } else if (pivotPos < k) {
                left = pivotPos + 1;
            } else {
                right = pivotPos - 1;
            }
        }
        
        return -1; // 不会执行到这里
    }
    
private:
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
};

/**
 * 扩展问题：颜色分类（荷兰国旗问题）
 */
class SortColors {
public:
    /**
     * 三指针法解决荷兰国旗问题
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 工程化考量：
     * - 一次遍历完成排序，效率高
     * - 原地操作，空间效率高
     * - 适合处理有限类别数据的排序
     */
    static void sortColors(vector<int>& nums) {
        if (nums.size() <= 1) return;
        
        int left = 0;        // 0的右边界
        int right = nums.size() - 1; // 2的左边界
        int current = 0;     // 当前指针
        
        while (current <= right) {
            if (nums[current] == 0) {
                swap(nums[left], nums[current]);
                left++;
                current++;
            } else if (nums[current] == 2) {
                swap(nums[current], nums[right]);
                right--;
                // current不增加，需要检查交换过来的元素
            } else {
                current++;
            }
        }
    }
};

// 主函数 - 测试所有功能
int main() {
    try {
        // 测试基本排序算法
        SortAlgorithms::testAllAlgorithms();
        
        // 性能测试
        SortAlgorithms::performanceTest();
        
        // 测试扩展问题
        cout << "\n=== 扩展问题测试 ===" << endl;
        
        // 测试第K大元素
        vector<int> nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        int result = KthLargest::findKthLargest(nums, k);
        cout << "第" << k << "大元素: " << result << endl;
        
        // 测试颜色分类
        vector<int> colors = {2, 0, 2, 1, 1, 0};
        cout << "颜色分类前: ";
        // SortAlgorithms::printVector(colors);
        cout << endl;
        
        SortColors::sortColors(colors);
        cout << "颜色分类后: ";
        // SortAlgorithms::printVector(colors);
        cout << endl;
        
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * 工程化考量总结:
 * 
 * 1. 异常处理: 对空数组、非法输入进行验证
 * 2. 边界条件: 处理单元素、已排序、逆序等特殊情况
 * 3. 性能优化: 
 *    - 小数组使用插入排序
 *    - 快速排序使用随机化和三数取中
 *    - 归并排序提供迭代版本避免栈溢出
 * 4. 内存管理: 合理使用临时空间，避免不必要的拷贝
 * 5. 代码可读性: 清晰的注释和命名规范
 * 6. 测试覆盖: 包含各种边界情况和性能测试
 * 7. 算法选择: 根据数据特征选择最优算法
 * 
 * 时间复杂度分析:
 * - 归并排序: 稳定 O(n log n)，适合需要稳定性的场景
 * - 快速排序: 平均 O(n log n)，原地排序，常数项较小
 * - 堆排序: 最坏 O(n log n)，适合对最坏情况有要求的场景
 * 
 * 实际应用建议:
 * - 小数据: 插入排序或选择排序
 * - 一般数据: 快速排序（随机化版本）
 * - 大数据且需要稳定: 归并排序
 * - 内存敏感: 堆排序或快速排序
 * - 大量重复: 三路快速排序
 */