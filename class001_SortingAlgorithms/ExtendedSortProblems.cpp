/**
 * 排序算法扩展题目 - C++版本
 * 包含更多LeetCode、牛客网、剑指Offer等平台的排序相关题目
 * 每个题目都包含多种解法和详细分析
 * 
 * 时间复杂度分析：详细分析每种解法的时间复杂度
 * 空间复杂度分析：分析内存使用情况
 * 最优解判断：确定是否为最优解，如果不是则寻找最优解
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <functional>
#include <string>
#include <sstream>
#include <random>
#include <chrono>
#include <map>
#include <unordered_map>
#include <cmath>
#include <limits>
#include <cassert>

using namespace std;

class ExtendedSortProblems {
public:
    /**
     * 题目1: 88. 合并两个有序数组
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/merge-sorted-array/
     * 难度: 简单
     * 
     * 时间复杂度: O(m + n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     */
    static void mergeSortedArrays(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        if (m < 0 || n < 0) {
            throw invalid_argument("Invalid input parameters");
        }
        
        int p1 = m - 1; // nums1有效部分的末尾
        int p2 = n - 1; // nums2的末尾
        int p = m + n - 1; // 合并后的末尾
        
        // 从后向前合并，避免覆盖nums1中的元素
        while (p1 >= 0 && p2 >= 0) {
            if (nums1[p1] > nums2[p2]) {
                nums1[p] = nums1[p1];
                p1--;
            } else {
                nums1[p] = nums2[p2];
                p2--;
            }
            p--;
        }
        
        // 如果nums2还有剩余元素，直接复制到nums1前面
        while (p2 >= 0) {
            nums1[p] = nums2[p2];
            p2--;
            p--;
        }
    }
    
    /**
     * 题目2: 973. 最接近原点的K个点
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/k-closest-points-to-origin/
     * 难度: 中等
     * 
     * 时间复杂度: O(n) 平均
     * 空间复杂度: O(1)
     * 是否最优解: 是
     */
    static vector<vector<int>> kClosest(vector<vector<int>>& points, int k) {
        if (points.empty() || k <= 0 || k > points.size()) {
            throw invalid_argument("Invalid input parameters");
        }
        
        // 使用快速选择算法找到第k小的距离
        quickSelect(points, 0, points.size() - 1, k);
        
        // 返回前k个点
        return vector<vector<int>>(points.begin(), points.begin() + k);
    }
    
private:
    static void quickSelect(vector<vector<int>>& points, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择pivot
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(left, right);
        int pivotIndex = dis(gen);
        int pivotDist = distance(points[pivotIndex]);
        
        // 分区操作
        int i = left;
        for (int j = left; j <= right; j++) {
            if (distance(points[j]) <= pivotDist) {
                swap(points[i], points[j]);
                i++;
            }
        }
        
        // 根据分区结果决定下一步
        if (i == k) {
            return;
        } else if (i < k) {
            quickSelect(points, i, right, k);
        } else {
            quickSelect(points, left, i - 1, k);
        }
    }
    
    static int distance(const vector<int>& point) {
        return point[0] * point[0] + point[1] * point[1];
    }
    
public:
    /**
     * 题目3: 1054. 距离相等的条形码
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/distant-barcodes/
     * 难度: 中等
     * 
     * 时间复杂度: O(n log k) - k为不同条形码的数量
     * 空间复杂度: O(n)
     * 是否最优解: 是
     */
    static vector<int> rearrangeBarcodes(vector<int>& barcodes) {
        if (barcodes.empty()) return {};
        
        // 统计频率
        unordered_map<int, int> freqMap;
        for (int code : barcodes) {
            freqMap[code]++;
        }
        
        // 最大堆，按频率排序
        auto comp = [](const pair<int, int>& a, const pair<int, int>& b) {
            return a.second < b.second;
        };
        priority_queue<pair<int, int>, vector<pair<int, int>>, decltype(comp)> maxHeap(comp);
        
        for (const auto& entry : freqMap) {
            maxHeap.push(entry);
        }
        
        vector<int> result(barcodes.size());
        int index = 0;
        
        // 间隔填充，先填偶数位置，再填奇数位置
        while (!maxHeap.empty()) {
            auto current = maxHeap.top();
            maxHeap.pop();
            int code = current.first;
            int freq = current.second;
            
            // 填充所有当前条形码
            for (int i = 0; i < freq; i++) {
                if (index >= result.size()) {
                    index = 1; // 切换到奇数位置
                }
                result[index] = code;
                index += 2;
            }
        }
        
        return result;
    }
    
    /**
     * 题目4: 324. 摆动排序 II
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/wiggle-sort-ii/
     * 难度: 中等
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是（对于通用情况）
     */
    static void wiggleSort(vector<int>& nums) {
        if (nums.size() <= 1) return;
        
        // 复制并排序数组
        vector<int> sorted = nums;
        sort(sorted.begin(), sorted.end());
        
        int n = nums.size();
        int mid = (n + 1) / 2; // 中间位置（向上取整）
        
        // 双指针填充：左半部分从大到小，右半部分从大到小
        int left = mid - 1;
        int right = n - 1;
        int index = 0;
        
        while (index < n) {
            if (index % 2 == 0) {
                // 偶数位置：取左半部分（较小的数）
                nums[index] = sorted[left];
                left--;
            } else {
                // 奇数位置：取右半部分（较大的数）
                nums[index] = sorted[right];
                right--;
            }
            index++;
        }
    }
    
    /**
     * 题目5: 280. 摆动排序
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/wiggle-sort/
     * 难度: 中等
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 是否最优解: 是
     */
    static void wiggleSort280(vector<int>& nums) {
        if (nums.size() <= 1) return;
        
        // 一次遍历，根据需要交换相邻元素
        for (int i = 0; i < nums.size() - 1; i++) {
            if ((i % 2 == 0 && nums[i] > nums[i + 1]) || 
                (i % 2 == 1 && nums[i] < nums[i + 1])) {
                // 交换相邻元素
                swap(nums[i], nums[i + 1]);
            }
        }
    }
    
    /**
     * 题目6: 493. 翻转对
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/reverse-pairs/
     * 难度: 困难
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是
     */
    static int reversePairs493(vector<int>& nums) {
        if (nums.size() <= 1) return 0;
        
        vector<int> temp(nums.size());
        return mergeSortCountPairs(nums, 0, nums.size() - 1, temp);
    }
    
private:
    static int mergeSortCountPairs(vector<int>& nums, int left, int right, vector<int>& temp) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        int count = 0;
        
        count += mergeSortCountPairs(nums, left, mid, temp);
        count += mergeSortCountPairs(nums, mid + 1, right, temp);
        count += countPairs(nums, left, mid, right);
        merge(nums, left, mid, right, temp);
        
        return count;
    }
    
    static int countPairs(vector<int>& nums, int left, int mid, int right) {
        int count = 0;
        int j = mid + 1;
        
        // 统计满足 nums[i] > 2 * nums[j] 的对数
        for (int i = left; i <= mid; i++) {
            while (j <= right && (long long)nums[i] > 2LL * nums[j]) {
                j++;
            }
            count += (j - (mid + 1));
        }
        
        return count;
    }
    
    static void merge(vector<int>& nums, int left, int mid, int right, vector<int>& temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = nums[i];
        }
        
        int i = left, k = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                nums[k++] = temp[i++];
            } else {
                nums[k++] = temp[j++];
            }
        }
        
        while (i <= mid) {
            nums[k++] = temp[i++];
        }
        while (j <= right) {
            nums[k++] = temp[j++];
        }
    }
    
public:
    /**
     * 题目7: 剑指Offer 45. 把数组排成最小的数
     * 来源: 剑指Offer
     * 难度: 中等
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是
     */
    static string minNumber(vector<int>& nums) {
        if (nums.empty()) return "";
        
        // 将数字转换为字符串
        vector<string> strNums;
        for (int num : nums) {
            strNums.push_back(to_string(num));
        }
        
        // 自定义排序：比较 s1+s2 和 s2+s1
        sort(strNums.begin(), strNums.end(), [](const string& s1, const string& s2) {
            return s1 + s2 < s2 + s1;
        });
        
        // 拼接结果
        string result;
        for (const string& str : strNums) {
            result += str;
        }
        
        return result;
    }
    
    /**
     * 题目8: HackerRank - Counting Inversions
     * 来源: HackerRank
     * 链接: https://www.hackerrank.com/challenges/ctci-merge-sort
     * 难度: 困难
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 是否最优解: 是
     */
    static long countInversions(vector<int>& arr) {
        if (arr.size() <= 1) return 0;
        
        vector<int> temp(arr.size());
        return mergeSortCountInversions(arr, 0, arr.size() - 1, temp);
    }
    
private:
    static long mergeSortCountInversions(vector<int>& arr, int left, int right, vector<int>& temp) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        long count = 0;
        
        count += mergeSortCountInversions(arr, left, mid, temp);
        count += mergeSortCountInversions(arr, mid + 1, right, temp);
        count += mergeAndCount(arr, left, mid, right, temp);
        
        return count;
    }
    
    static long mergeAndCount(vector<int>& arr, int left, int mid, int right, vector<int>& temp) {
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }
        
        int i = left, j = mid + 1, k = left;
        long count = 0;
        
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
                count += (mid - i + 1); // 统计逆序对
            }
        }
        
        while (i <= mid) {
            arr[k++] = temp[i++];
        }
        while (j <= right) {
            arr[k++] = temp[j++];
        }
        
        return count;
    }
    
public:
    /**
     * 题目9: 牛客网 NC140 排序
     * 来源: 牛客网
     * 链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
     * 难度: 简单
     * 
     * 时间复杂度: 根据算法选择
     * 空间复杂度: 根据算法选择
     */
    static vector<int> sortArrayNC140(vector<int>& arr) {
        if (arr.size() <= 1) return arr;
        
        // 根据数据规模选择算法
        if (arr.size() < 50) {
            // 小数组使用插入排序
            insertionSort(arr);
        } else {
            // 中等以上使用快速排序
            quickSort(arr, 0, arr.size() - 1);
        }
        
        return arr;
    }
    
private:
    static void insertionSort(vector<int>& arr) {
        for (int i = 1; i < arr.size(); i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    static void quickSort(vector<int>& arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
    
    static int partition(vector<int>& arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr[i], arr[j]);
            }
        }
        
        swap(arr[i + 1], arr[high]);
        return i + 1;
    }
    
    /**
     * 题目10: 外部排序模拟 - 多路归并
     * 来源: 算法导论
     * 难度: 困难
     * 
     * 时间复杂度: O(n log k) - k为归并路数
     * 空间复杂度: O(k) - 缓冲区大小
     */
    class ExternalSort {
    public:
        /**
         * 模拟多路归并排序
         * @param chunks 多个有序数据块
         * @return 合并后的有序数组
         */
        static vector<int> multiwayMerge(vector<vector<int>>& chunks) {
            if (chunks.empty()) return {};
            
            // 使用优先队列进行多路归并
            auto comp = [](const Element& a, const Element& b) {
                return a.value > b.value;
            };
            priority_queue<Element, vector<Element>, decltype(comp)> minHeap(comp);
            
            // 初始化每个数据块的指针
            vector<int> pointers(chunks.size(), 0);
            int totalSize = 0;
            
            // 将每个数据块的第一个元素加入堆
            for (int i = 0; i < chunks.size(); i++) {
                totalSize += chunks[i].size();
                if (!chunks[i].empty()) {
                    minHeap.push(Element(chunks[i][0], i, 0));
                    pointers[i] = 1;
                }
            }
            
            vector<int> result;
            result.reserve(totalSize);
            
            // 多路归并
            while (!minHeap.empty()) {
                Element minElem = minHeap.top();
                minHeap.pop();
                result.push_back(minElem.value);
                
                int chunkIndex = minElem.chunkIndex;
                int elementIndex = minElem.elementIndex + 1;
                
                if (elementIndex < chunks[chunkIndex].size()) {
                    minHeap.push(Element(chunks[chunkIndex][elementIndex], chunkIndex, elementIndex));
                }
            }
            
            return result;
        }
        
        struct Element {
            int value;
            int chunkIndex;
            int elementIndex;
            
            Element(int v, int c, int e) : value(v), chunkIndex(c), elementIndex(e) {}
        };
    };
    
public:
    /**
     * 测试所有扩展题目
     */
    static void testAllProblems() {
        cout << "=== 扩展排序题目测试开始 ===" << endl;
        
        // 测试题目1: 合并两个有序数组
        cout << "\n题目1: 合并两个有序数组" << endl;
        vector<int> nums1 = {1,2,3,0,0,0};
        vector<int> nums2 = {2,5,6};
        mergeSortedArrays(nums1, 3, nums2, 3);
        cout << "合并结果: ";
        for (int num : nums1) cout << num << " ";
        cout << endl;
        
        // 测试题目2: 最接近原点的K个点
        cout << "\n题目2: 最接近原点的K个点" << endl;
        vector<vector<int>> points = {{1,3}, {-2,2}, {5,8}, {0,1}};
        vector<vector<int>> result2 = kClosest(points, 2);
        cout << "最接近的2个点: ";
        for (auto& point : result2) {
            cout << "[" << point[0] << "," << point[1] << "] ";
        }
        cout << endl;
        
        // 测试题目3: 距离相等的条形码
        cout << "\n题目3: 距离相等的条形码" << endl;
        vector<int> barcodes = {1,1,1,2,2,2};
        vector<int> result3 = rearrangeBarcodes(barcodes);
        cout << "重新排列结果: ";
        for (int code : result3) cout << code << " ";
        cout << endl;
        
        // 测试题目4: 摆动排序II
        cout << "\n题目4: 摆动排序II" << endl;
        vector<int> nums4 = {1,5,1,1,6,4};
        wiggleSort(nums4);
        cout << "摆动排序结果: ";
        for (int num : nums4) cout << num << " ";
        cout << endl;
        
        // 测试题目5: 摆动排序
        cout << "\n题目5: 摆动排序" << endl;
        vector<int> nums5 = {3,5,2,1,6,4};
        wiggleSort280(nums5);
        cout << "摆动排序结果: ";
        for (int num : nums5) cout << num << " ";
        cout << endl;
        
        // 测试题目6: 翻转对
        cout << "\n题目6: 翻转对" << endl;
        vector<int> nums6 = {1,3,2,3,1};
        int count6 = reversePairs493(nums6);
        cout << "翻转对数量: " << count6 << endl;
        
        // 测试题目7: 把数组排成最小的数
        cout << "\n题目7: 把数组排成最小的数" << endl;
        vector<int> nums7 = {10,2};
        string result7 = minNumber(nums7);
        cout << "最小数字: " << result7 << endl;
        
        // 测试题目8: 逆序对计数
        cout << "\n题目8: 逆序对计数" << endl;
        vector<int> nums8 = {2,4,1,3,5};
        long count8 = countInversions(nums8);
        cout << "逆序对数量: " << count8 << endl;
        
        // 测试题目9: 牛客网排序
        cout << "\n题目9: 牛客网排序" << endl;
        vector<int> nums9 = {3,1,4,1,5,9,2,6};
        vector<int> result9 = sortArrayNC140(nums9);
        cout << "排序结果: ";
        for (int num : result9) cout << num << " ";
        cout << endl;
        
        // 测试题目10: 外部排序
        cout << "\n题目10: 外部排序模拟" << endl;
        vector<vector<int>> chunks = {
            {1,3,5},
            {2,4,6},
            {0,7,8}
        };
        vector<int> result10 = ExternalSort::multiwayMerge(chunks);
        cout << "多路归并结果: ";
        for (int num : result10) cout << num << " ";
        cout << endl;
        
        cout << "\n=== 扩展排序题目测试结束 ===" << endl;
    }
};

int main() {
    try {
        ExtendedSortProblems::testAllProblems();
    } catch (const exception& e) {
        cerr << "测试过程中出现错误: " << e.what() << endl;
    }
    
    return 0;
}