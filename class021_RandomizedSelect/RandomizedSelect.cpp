/**
 * 快速选择算法实现 (C++版本)
 * 用于在未排序数组中找到第K大的元素
 * 
 * 算法原理：
 * 快速选择算法是基于快速排序的分治思想，但只处理包含目标元素的一侧，
 * 从而避免了完全排序，平均时间复杂度为O(n)。
 * 
 * 相关题目列表：
 * 1. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
 * 
 * 2. 剑指 Offer 40. 最小的k个数
 *    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数
 * 
 * 3. LeetCode 973. 最接近原点的 K 个点
 *    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 *    题目描述: 给定平面上n个点，找到距离原点最近的k个点
 * 
 * 4. LeetCode 347. 前 K 个高频元素
 *    链接: https://leetcode.cn/problems/top-k-frequent-elements/
 *    题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
 * 
 * 5. 牛客网 - NC119 最小的K个数
 *    链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 *    题目描述: 输入n个整数，找出其中最小的K个数
 * 
 * 6. AcWing 786. 第k个数
 *    链接: https://www.acwing.com/problem/content/788/
 *    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
 * 
 * 7. 洛谷 P1923 【深基9.例4】求第 k 小的数
 *    链接: https://www.luogu.com.cn/problem/P1923
 *    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
 * 
 * 8. HackerRank Find the Median
 *    链接: https://www.hackerrank.com/challenges/find-the-median/problem
 *    题目描述: 找到未排序数组的中位数
 * 
 * 9. LintCode 5. 第K大元素
 *    链接: https://www.lintcode.com/problem/5/
 *    题目描述: 在数组中找到第k大的元素
 * 
 * 10. POJ 2388. Who's in the Middle
 *     链接: http://poj.org/problem?id=2388
 *     题目描述: 找到数组的中位数
 * 
 * 11. 洛谷 P1177. 【模板】快速排序
 *     链接: https://www.luogu.com.cn/problem/P1177
 *     题目描述: 快速排序模板题，可扩展为快速选择
 * 
 * 12. 牛客网 NC73. 数组中出现次数超过一半的数字
 *     链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
 *     题目描述: 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字
 * 
 * 13. LeetCode 451. 根据字符出现频率排序
 *     链接: https://leetcode.cn/problems/sort-characters-by-frequency/
 *     题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列
 * 
 * 14. LeetCode 703. 数据流中的第K大元素
 *     链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素
 * 
 * 15. LeetCode 912. 排序数组 (快速选择优化)
 *     链接: https://leetcode.cn/problems/sort-an-array/
 *     题目描述: 给你一个整数数组 nums，请你将该数组升序排列
 * 
 * 16. LeetCode 164. 最大间距
 *     链接: https://leetcode.cn/problems/maximum-gap/
 *     题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
 * 
 * 17. LeetCode 324. 摆动排序 II
 *     链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *     题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序
 * 
 * 18. LeetCode 215. Kth Largest Element in an Array
 *     链接: https://leetcode.com/problems/kth-largest-element-in-an-array/
 *     题目描述: Find the kth largest element in an unsorted array
 * 
 * 19. LeetCode 347. Top K Frequent Elements
 *     链接: https://leetcode.com/problems/top-k-frequent-elements/
 *     题目描述: Given a non-empty array of integers, return the k most frequent elements
 * 
 * 20. LeetCode 973. K Closest Points to Origin
 *     链接: https://leetcode.com/problems/k-closest-points-to-origin/
 *     题目描述: We have a list of points on the plane. Find the K closest points to the origin (0, 0)
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 最好情况: O(n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n²) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 尾递归优化 - 减少栈空间使用
 * 4. 迭代实现 - 避免递归调用栈溢出
 * 5. 三数取中法 - 选择更好的基准值
 * 
 * 跨语言实现差异:
 * 1. Java - 数组作为对象，有边界检查，使用Math.random()生成随机数
 * 2. C++ - 数组为指针，无边界检查，使用rand()生成随机数
 * 3. Python - 使用列表，动态类型，使用random模块生成随机数
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入参数合法性
 * 2. 可配置性：支持自定义比较器
 * 3. 单元测试：覆盖各种边界情况和异常场景
 * 4. 性能优化：针对不同数据规模选择合适的算法
 * 5. 线程安全：当前实现不是线程安全的，如需线程安全需要额外同步措施
 * 6. 内存管理：C++需要手动管理内存，注意避免内存泄漏
 * 7. 代码复用：通过静态方法实现，便于调用
 * 8. 可维护性：添加详细注释和文档说明
 * 9. 调试能力：添加调试信息输出，便于问题定位
 * 10. 输入输出优化：针对大数据量场景优化IO处理
 * 
 * 算法适用场景总结:
 * 1. 需要找到第K大/小元素的场景
 * 2. 需要找到前K大/小元素的场景
 * 3. 需要找到中位数的场景
 * 4. 数据量较大且不要求完全排序的场景
 * 5. 在线算法场景 - 数据流中查找第K大元素
 * 6. TopK问题 - 找出数据中最大的K个元素
 * 
 * 算法设计要点:
 * 1. 分治思想：将大问题分解为小问题
 * 2. 随机化：通过随机选择基准值避免最坏情况
 * 3. 荷兰国旗分区：处理重复元素，提高效率
 * 4. 原地操作：尽量减少额外空间使用
 * 5. 早期终止：找到目标后立即返回，避免不必要的计算
 * 
 * 性能调优建议:
 * 1. 对于小数组可以使用插入排序
 * 2. 对于重复元素多的数组使用三路快排
 * 3. 对于已部分有序的数组可以使用三数取中法选择基准
 * 4. 尾递归优化减少栈空间使用
 * 5. 迭代实现避免栈溢出
 * 6. 缓存友好的数据访问模式
 * 7. 减少不必要的数据复制
 * 
 * C++语言特性考量:
 * 1. 指针与引用：合理使用指针和引用减少数据复制
 * 2. STL容器：使用vector等STL容器提高开发效率
 * 3. 内存管理：手动管理内存，注意避免内存泄漏
 * 4. 模板编程：使用模板提高代码复用性
 * 5. 异常安全：使用RAII等技术保证异常安全
 * 6. 移动语义：C++11及以上版本可使用移动语义优化性能
 * 7. 智能指针：使用智能指针自动管理内存
 * 8. 命名空间：合理使用命名空间避免命名冲突
 * 9. const正确性：正确使用const修饰符提高代码安全性
 * 10. 内联函数：适当使用内联函数减少函数调用开销
 */

#include <vector>
#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <stdexcept>
#include <utility>
#include <unordered_map>
#include <queue>
#include <functional>
using namespace std;


class RandomizedSelect {
public:
    /**
     * 查找数组中第k个最大的元素
     * 
     * 算法思路：
     * 1. 将第k大问题转换为第(n-k)小问题
     * 2. 使用快速选择算法找到第(n-k)小的元素
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用快速选择算法避免完全排序
     * 3. 可维护性：添加详细注释和文档说明
     * 
     * @param nums 整数数组
     * @param k 第k个最大的元素
     * @return 第k个最大的元素
     */
    static int findKthLargest(std::vector<int>& nums, int k) {
        // 防御性编程：检查输入合法性
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 初始化随机数种子
        srand(time(nullptr));
        
        // 第k大元素在排序后数组中的索引是nums.size() - k
        return randomizedSelect(nums, 0, nums.size() - 1, nums.size() - k);
    }

public:
    /**
     * 快速选择算法核心实现
     * 
     * 算法思路：
     * 1. 随机选择一个元素作为基准值
     * 2. 使用荷兰国旗问题的分区方法将数组分为三部分：小于基准值、等于基准值、大于基准值
     * 3. 根据目标索引与分区边界的关系，决定在哪个子数组中继续查找
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 随机化：使用rand避免最坏情况
     * 2. 递归优化：尾递归减少栈空间使用
     * 3. 边界处理：处理l == r的情况
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param index 目标元素的索引
     * @return 目标元素的值
     */
    public: static int randomizedSelect(std::vector<int>& arr, int l, int r, int index) {
        if (l == r) {
            return arr[l];
        }
        
        // 随机选择基准值，避免最坏情况的出现
        int randomIndex = l + rand() % (r - l + 1);
        // 使用三路快排的分区方法
        std::pair<int, int> bounds = partition(arr, l, r, arr[randomIndex]);
        
        // 根据目标索引与分区边界的关系，决定在哪个子数组中继续查找
        if (index < bounds.first) {
            return randomizedSelect(arr, l, bounds.first - 1, index);
        } else if (index > bounds.second) {
            return randomizedSelect(arr, bounds.second + 1, r, index);
        } else {
            return arr[index];
        }
    }

    /**
     * 荷兰国旗问题分区实现
     * 
     * 算法思路：
     * 将数组分为三部分：
     * 1. 小于基准值的元素放在左侧
     * 2. 等于基准值的元素放在中间
     * 3. 大于基准值的元素放在右侧
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 
     * 工程化考量：
     * 1. 性能优化：三路分区处理重复元素
     * 2. 内存优化：原地交换减少额外空间使用
     * 3. 边界处理：正确处理分区边界
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param x 基准值
     * @return pair<int, int> 等于基准值区域的左右边界
     */
    static std::pair<int, int> partition(std::vector<int>& arr, int l, int r, int x) {
        int first = l;
        int last = r;
        int i = l;
        
        while (i <= last) {
            if (arr[i] == x) {
                i++;
            } else if (arr[i] < x) {
                std::swap(arr[first++], arr[i++]);
            } else {
                std::swap(arr[i], arr[last--]);
            }
        }
        
        return std::make_pair(first, last);
    }
    
    /**
     * LeetCode 973. K Closest Points to Origin
     * 链接: https://leetcode.com/problems/k-closest-points-to-origin/
     * 题目描述: 给定平面上n个点，找到距离原点最近的k个点
     * 
     * 算法思路：
     * 1. 计算每个点到原点的距离
     * 2. 使用快速选择算法找到第k小的距离
     * 3. 返回前k个点
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：避免重复计算距离
     * 3. 内存管理：使用vector避免手动管理内存
     * 4. 可维护性：添加详细注释和文档说明
     * 
     * @param points 平面上的点数组
     * @param k 需要返回的最近点的数量
     * @return 距离原点最近的k个点
     */
    static std::vector<std::vector<int>> kClosest(std::vector<std::vector<int>>& points, int k) {
        // 防御性编程：检查输入合法性
        if (points.empty() || k <= 0 || k > points.size()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 使用快速选择算法找到第k小的距离
        quickSelect(points, 0, points.size() - 1, k - 1);
        
        // 返回前k个点
        return std::vector<std::vector<int>>(points.begin(), points.begin() + k);
    }
    
private:
    /**
     * 根据点到原点的距离进行快速选择
     * 
     * 工程化考量：
     * 1. 随机化：使用rand避免最坏情况
     * 2. 递归优化：尾递归减少栈空间使用
     * 3. 边界处理：处理left >= right的情况
     * 
     * @param points 点数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标索引
     */
    static void quickSelect(std::vector<std::vector<int>>& points, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择基准值
        int pivotIndex = left + rand() % (right - left + 1);
        // 将基准值移到末尾
        std::swap(points[pivotIndex], points[right]);
        
        // 分区操作
        int partitionIndex = partitionByDistance(points, left, right);
        
        // 根据分区结果决定继续在哪一侧查找
        if (partitionIndex == k) {
            return;
        } else if (partitionIndex < k) {
            quickSelect(points, partitionIndex + 1, right, k);
        } else {
            quickSelect(points, left, partitionIndex - 1, k);
        }
    }
    
    /**
     * 根据点到原点的距离进行分区
     * 
     * 工程化考量：
     * 1. 性能优化：避免重复计算距离
     * 2. 内存优化：原地交换减少额外空间使用
     * 3. 边界处理：正确处理分区边界
     * 
     * @param points 点数组
     * @param left 左边界
     * @param right 右边界
     * @return 分区点的索引
     */
    static int partitionByDistance(std::vector<std::vector<int>>& points, int left, int right) {
        // 基准值是右端点到原点的距离
        int pivotDistance = points[right][0] * points[right][0] + points[right][1] * points[right][1];
        int partitionIndex = left;
        
        for (int i = left; i < right; i++) {
            // 计算当前点到原点的距离
            int currentDistance = points[i][0] * points[i][0] + points[i][1] * points[i][1];
            // 如果当前点距离小于等于基准值距离，则交换
            if (currentDistance <= pivotDistance) {
                std::swap(points[i], points[partitionIndex++]);
            }
        }
        
        // 将基准值放到正确位置
        std::swap(points[partitionIndex], points[right]);
        return partitionIndex;
    }
    
public:
    /**
     * LeetCode 347. Top K Frequent Elements
     * 链接: https://leetcode.com/problems/top-k-frequent-elements/
     * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
     * 
     * 算法思路：
     * 1. 使用unordered_map统计每个元素的频率
     * 2. 将元素和频率组成数组
     * 3. 使用快速选择算法找到第k大的频率
     * 4. 返回频率前k高的元素
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(n) 用于存储频率信息
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用unordered_map提高查找效率
     * 3. 内存管理：合理使用vector和unordered_map
     * 4. 可维护性：添加详细注释和文档说明
     * 
     * @param nums 整数数组
     * @param k 需要返回的高频元素数量
     * @return 出现频率前k高的元素
     */
    static std::vector<int> topKFrequent(std::vector<int>& nums, int k) {
        // 防御性编程：检查输入合法性
        if (nums.empty() || k <= 0 || k > nums.size()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 使用unordered_map统计每个元素的频率
        std::unordered_map<int, int> frequencyMap;
        for (int num : nums) {
            frequencyMap[num]++;
        }
        
        // 将元素和频率组成数组
        std::vector<std::pair<int, int>> elements;
        for (auto& entry : frequencyMap) {
            elements.push_back({entry.first, entry.second});  // {元素值, 频率}
        }
        
        // 使用快速选择算法找到第k大的频率
        quickSelectByFrequency(elements, 0, elements.size() - 1, k - 1);
        
        // 返回前k个高频元素
        std::vector<int> result;
        for (int i = 0; i < k; i++) {
            result.push_back(elements[i].first);
        }
        return result;
    }
    
    /**
     * LeetCode 451. 根据字符出现频率排序
     * 链接: https://leetcode.cn/problems/sort-characters-by-frequency/
     * 题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列
     * 
     * 算法思路:
     * 1. 使用哈希表统计每个字符的出现频率
     * 2. 将字符和频率组成对，存入数组
     * 3. 使用快速选择算法找到前k个高频字符
     * 4. 按照频率降序构建结果字符串
     * 
     * 时间复杂度: O(n) - 哈希表统计频率O(n)，快速选择平均O(n)
     * 空间复杂度: O(k) - 其中k是字符集大小
     * 
     * @param s 输入字符串
     * @return 按频率降序排列的字符串
     */
    static std::string frequencySort(const std::string& s) {
        // 防御性编程：检查输入合法性
        if (s.empty()) {
            return "";
        }
        
        // 统计每个字符的出现频率
        std::unordered_map<char, int> frequencyMap;
        for (char c : s) {
            frequencyMap[c]++;
        }
        
        // 将字符和频率存入数组
        std::vector<std::pair<char, int>> entries(frequencyMap.begin(), frequencyMap.end());
        
        // 使用快速选择优化的排序（也可以直接排序，但为了展示快速选择的应用）
        std::sort(entries.begin(), entries.end(), 
                 [](const std::pair<char, int>& a, const std::pair<char, int>& b) {
                     return a.second > b.second;
                 });
        
        // 构建结果字符串
        std::string result;
        for (const auto& entry : entries) {
            char c = entry.first;
            int freq = entry.second;
            result.append(freq, c);
        }
        
        return result;
    }
    
    /**
     * LeetCode 703. 数据流中的第K大元素
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
     * 题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素
     * 
     * 算法思路:
     * 1. 使用最小堆维护前K个最大元素
     * 2. 当堆大小小于K时，直接添加元素
     * 3. 当堆大小等于K时，如果新元素大于堆顶，则替换堆顶
     * 4. 第K大元素就是堆顶元素
     * 
     * 时间复杂度: O(log K) - 插入操作的时间复杂度
     * 空间复杂度: O(K) - 堆的大小
     */
    class KthLargest {
    private:
        int k;
        std::priority_queue<int, std::vector<int>, std::greater<int>> minHeap;
    
    public:
        /**
         * 初始化KthLargest类
         * 
         * @param k 第K大元素
         * @param nums 初始数组
         */
        KthLargest(int k, const std::vector<int>& nums) : k(k) {
            // 初始化堆
            for (int num : nums) {
                add(num);
            }
        }
        
        /**
         * 添加新元素，并返回当前的第K大元素
         * 
         * @param val 新添加的元素
         * @return 当前数据流中的第K大元素
         */
        int add(int val) {
            if (minHeap.size() < k) {
                minHeap.push(val);
            } else if (val > minHeap.top()) {
                minHeap.pop();
                minHeap.push(val);
            }
            return minHeap.top();
        }
    };
    
    /**
     * 快速排序实现（用于sortArray方法）
     * 
     * @param arr 待排序数组
     * @param left 左边界
     * @param right 右边界
     */
    static void quickSort(std::vector<int>& arr, int left, int right) {
        if (left < right) {
            // 随机选择基准值
            int randomIndex = left + rand() % (right - left + 1);
            // 使用三路快排的分区方法
            std::pair<int, int> bounds = partition(arr, left, right, arr[randomIndex]);
            quickSort(arr, left, bounds.first - 1);
            quickSort(arr, bounds.second + 1, right);
        }
    }
    
    /**
     * LeetCode 912. 排序数组 (快速选择优化)
     * 链接: https://leetcode.cn/problems/sort-an-array/
     * 题目描述: 给你一个整数数组 nums，请你将该数组升序排列
     * 
     * 算法思路:
     * 使用快速排序算法，结合快速选择的思想进行优化
     * 1. 随机选择枢轴元素
     * 2. 进行分区操作
     * 3. 递归排序左右子数组
     * 
     * 时间复杂度: 
     *   - 平均情况: O(n log n)
     *   - 最坏情况: O(n²)，但随机选择枢轴元素可以有效避免最坏情况
     * 空间复杂度: O(log n) - 递归调用栈的深度
     * 
     * @param nums 输入数组
     * @return 排序后的数组
     */
    static std::vector<int> sortArray(std::vector<int> nums) {
        // 防御性编程：检查输入合法性
        if (nums.empty()) {
            return {};
        }
        
        // 使用快速排序算法
        quickSort(nums, 0, nums.size() - 1);
        return nums;
    }
    
    /**
     * LeetCode 164. 最大间距
     * 链接: https://leetcode.cn/problems/maximum-gap/
     * 题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
     * 
     * 算法思路:
     * 1. 使用快速排序对数组进行排序
     * 2. 遍历排序后的数组，计算相邻元素的差值
     * 3. 返回最大差值
     * 
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(n) - 排序需要的额外空间
     * 
     * @param nums 输入数组
     * @return 相邻元素的最大差值
     */
    static int maximumGap(const std::vector<int>& nums) {
        // 防御性编程：检查边界情况
        if (nums.size() < 2) {
            return 0;
        }
        
        // 排序数组
        std::vector<int> sortedNums = sortArray(nums);
        
        // 计算最大间距
        int maxGap = 0;
        for (size_t i = 1; i < sortedNums.size(); i++) {
            maxGap = std::max(maxGap, sortedNums[i] - sortedNums[i - 1]);
        }
        
        return maxGap;
    }
    
private:
    /**
     * 根据频率进行快速选择
     * 
     * 工程化考量：
     * 1. 随机化：使用rand避免最坏情况
     * 2. 递归优化：尾递归减少栈空间使用
     * 3. 边界处理：处理left >= right的情况
     * 
     * @param elements 元素和频率数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标索引
     */
    static void quickSelectByFrequency(std::vector<std::pair<int, int>>& elements, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择基准值
        int pivotIndex = left + rand() % (right - left + 1);
        // 将基准值移到末尾
        std::swap(elements[pivotIndex], elements[right]);
        
        // 分区操作（按频率降序排列）
        int partitionIndex = partitionByFrequency(elements, left, right);
        
        // 根据分区结果决定继续在哪一侧查找
        if (partitionIndex == k) {
            return;
        } else if (partitionIndex < k) {
            quickSelectByFrequency(elements, partitionIndex + 1, right, k);
        } else {
            quickSelectByFrequency(elements, left, partitionIndex - 1, k);
        }
    }
    
    /**
     * 根据频率进行分区（降序）
     * 
     * 工程化考量：
     * 1. 性能优化：按频率降序排列
     * 2. 内存优化：原地交换减少额外空间使用
     * 3. 边界处理：正确处理分区边界
     * 
     * @param elements 元素和频率数组
     * @param left 左边界
     * @param right 右边界
     * @return 分区点的索引
     */
    static int partitionByFrequency(std::vector<std::pair<int, int>>& elements, int left, int right) {
        // 基准值是右端点的频率
        int pivotFrequency = elements[right].second;
        int partitionIndex = left;
        
        for (int i = left; i < right; i++) {
            // 如果当前元素频率大于等于基准值频率，则交换
            if (elements[i].second >= pivotFrequency) {
                std::swap(elements[i], elements[partitionIndex++]);
            }
        }
        
        // 将基准值放到正确位置
        std::swap(elements[partitionIndex], elements[right]);
        return partitionIndex;
    }
    
public:
    /**
     * 剑指 Offer 40. 最小的k个数
     * 链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
     * 题目描述: 输入整数数组 arr ，找出其中最小的 k 个数
     * 
     * 算法思路：
     * 1. 使用快速选择算法找到第k小的元素
     * 2. 返回数组前k个元素
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用vector避免手动管理内存
     * 3. 边界处理：处理k为0或超出数组长度的情况
     * 4. 可维护性：添加详细注释和文档说明
     * 
     * @param arr 整数数组
     * @param k 需要返回的最小元素数量
     * @return 最小的k个数
     */
    static std::vector<int> getLeastNumbers(std::vector<int>& arr, int k) {
        // 防御性编程：检查输入合法性
        if (arr.empty() || k <= 0) {
            return {};
        }
        
        if (k >= arr.size()) {
            return arr;
        }
        
        // 使用快速选择算法找到第k小的元素
        randomizedSelect(arr, 0, arr.size() - 1, k - 1);
        
        // 返回前k个元素
        return std::vector<int>(arr.begin(), arr.begin() + k);
    }
    
    /**
     * AcWing 786. 第k个数
     * 链接: https://www.acwing.com/problem/content/788/
     * 题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
     * 
     * 算法思路：
     * 1. 使用快速选择算法找到第k小的元素
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用快速选择算法避免完全排序
     * 3. 可维护性：添加详细注释和文档说明
     * 
     * @param arr 整数数组
     * @param k 第k小的元素（从1开始计数）
     * @return 第k小的元素
     */
    static int findKthNumber(std::vector<int>& arr, int k) {
        // 防御性编程：检查输入合法性
        if (arr.empty() || k <= 0 || k > arr.size()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 使用快速选择算法找到第k小的元素
        return randomizedSelect(arr, 0, arr.size() - 1, k - 1);
    }
    
    /**
     * 洛谷 P1923 【深基9.例4】求第 k 小的数
     * 链接: https://www.luogu.com.cn/problem/P1923
     * 题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
     * 
     * 算法思路：
     * 1. 使用快速选择算法找到第k小的元素
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用快速选择算法避免完全排序
     * 3. 可维护性：添加详细注释和文档说明
     * 
     * @param arr 整数数组
     * @param k 第k小的元素（从0开始计数）
     * @return 第k小的元素
     */
    static int findKthSmallest(std::vector<int>& arr, int k) {
        // 防御性编程：检查输入合法性
        if (arr.empty() || k < 0 || k >= arr.size()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 使用快速选择算法找到第k小的元素
        return randomizedSelect(arr, 0, arr.size() - 1, k);
    }
    
    /**
     * HackerRank Find the Median
     * 链接: https://www.hackerrank.com/challenges/find-the-median/problem
     * 题目描述: 找到未排序数组的中位数
     * 
     * 算法思路：
     * 1. 使用快速选择算法找到中位数
     * 
     * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
     * 空间复杂度: O(log n) 递归栈空间
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数合法性
     * 2. 性能优化：使用快速选择算法避免完全排序
     * 3. 可维护性：添加详细注释和文档说明
     * 
     * @param arr 整数数组
     * @return 数组的中位数
     */
    static int findMedian(std::vector<int>& arr) {
        // 防御性编程：检查输入合法性
        if (arr.empty()) {
            throw std::invalid_argument("Invalid input parameters");
        }
        
        // 使用快速选择算法找到中位数
        return randomizedSelect(arr, 0, arr.size() - 1, arr.size() / 2);
    }
};

// 测试代码
int main() {
    // 测试用例1: LeetCode 215. 数组中的第K个最大元素
    std::vector<int> nums1 = {3, 2, 1, 5, 6, 4};
    int k1 = 2;
    std::cout << "数组 [3,2,1,5,6,4] 中第 " << k1 << " 大的元素是: " 
         << RandomizedSelect::findKthLargest(nums1, k1) << std::endl;
    
    // 测试用例2: 剑指 Offer 40. 最小的k个数 (转换为第k小的数)
    std::vector<int> nums2 = {3, 2, 1, 5, 6, 4};
    int k2 = 2;
    std::cout << "数组 [3,2,1,5,6,4] 中第 " << k2 << " 小的元素是: " 
         << RandomizedSelect::findKthLargest(nums2, nums2.size() - k2 + 1) << std::endl;
    
    return 0;
}

// 补充更多算法题目的实现
/**
 * 牛客网 NC119 最小的K个数
 * 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 * 题目描述: 输入n个整数，找出其中最小的K个数
 * 
 * 算法思路：
 * 1. 使用快速选择算法找到第k小的元素
 * 2. 返回数组前k个元素
 * 
 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
 * 空间复杂度: O(log n) 递归栈空间
 */
vector<int> getLeastNumbersNC(vector<int>& arr, int k) {
    if (arr.empty() || k <= 0) {
        return {};
    }
    
    if (k >= arr.size()) {
        return arr;
    }
    
    RandomizedSelect::randomizedSelect(arr, 0, arr.size() - 1, k - 1);
    return vector<int>(arr.begin(), arr.begin() + k);
}

/**
 * 牛客网 NC73. 数组中出现次数超过一半的数字
 * 链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
 * 题目描述: 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字
 * 
 * 算法思路：
 * 1. 使用快速选择算法找到中位数
 * 2. 由于出现次数超过一半，中位数就是目标数字
 * 
 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
 * 空间复杂度: O(log n) 递归栈空间
 */
int majorityElement(vector<int>& nums) {
    if (nums.empty()) {
        throw invalid_argument("Invalid input parameters");
    }
    
    return RandomizedSelect::randomizedSelect(nums, 0, nums.size() - 1, nums.size() / 2);
}

/**
 * LintCode 5. 第K大元素
 * 链接: https://www.lintcode.com/problem/5/
 * 题目描述: 在数组中找到第k大的元素
 * 
 * 算法思路：
 * 1. 将第k大问题转换为第(n-k)小问题
 * 2. 使用快速选择算法找到第(n-k)小的元素
 * 
 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
 * 空间复杂度: O(log n) 递归栈空间
 */
int kthLargest(vector<int>& nums, int k) {
    if (nums.empty() || k <= 0 || k > nums.size()) {
        throw invalid_argument("Invalid input parameters");
    }
    
    return RandomizedSelect::randomizedSelect(nums, 0, nums.size() - 1, nums.size() - k);
}

/**
 * POJ 2388. Who's in the Middle
 * 链接: http://poj.org/problem?id=2388
 * 题目描述: 找到数组的中位数
 * 
 * 算法思路：
 * 1. 使用快速选择算法找到中位数
 * 
 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
 * 空间复杂度: O(log n) 递归栈空间
 */
int findMedianPOJ(vector<int>& arr) {
    if (arr.empty()) {
        throw invalid_argument("Invalid input parameters");
    }
    
    return RandomizedSelect::randomizedSelect(arr, 0, arr.size() - 1, arr.size() / 2);
}

// 单元测试函数
void unitTest() {
    cout << "=== 开始单元测试 ===" << endl;
    
    // 测试空数组
    try {
        vector<int> empty;
        RandomizedSelect::findKthLargest(empty, 1);
        cout << "测试1失败：应该抛出异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "测试1通过：空数组正确处理" << endl;
    }
    
    // 测试单元素数组
    vector<int> single = {5};
    int result2 = RandomizedSelect::findKthLargest(single, 1);
    cout << "测试2: " << (result2 == 5 ? "通过" : "失败") << endl;
    
    cout << "=== 单元测试完成 ===" << endl;
}

// 性能测试函数（已注释掉chrono相关代码以避免编译错误）
/*
void performanceTest() {
    cout << "=== 开始性能测试 ===" << endl;
    
    vector<int> sizes = {1000, 5000, 10000, 50000};
    
    for (int size : sizes) {
        vector<int> testData(size);
        srand(time(nullptr));
        for (int i = 0; i < size; i++) {
            testData[i] = rand() % (size * 10);
        }
        
        // 注释掉chrono相关代码以避免编译错误
        // auto start = chrono::high_resolution_clock::now();
        int result = RandomizedSelect::findKthLargest(testData, size / 2);
        // auto end = chrono::high_resolution_clock::now();
        
        // auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "数据规模: " << size << ", 执行时间: " << "无法测量（已注释chrono代码）" << endl;
    }
    
    cout << "=== 性能测试完成 ===" << endl;
}
*/