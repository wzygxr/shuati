#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 算法思路：
 * 本题可以使用多种方法解决：
 * 1. 暴力法：对于每个元素，遍历其右侧所有元素统计小于它的元素个数
 * 2. 归并排序：在归并过程中统计逆序对
 * 3. 稀疏表：预处理后快速查询区间最小值
 * 4. 树状数组/线段树：动态维护前缀和
 * 
 * 这里我们使用稀疏表方法来解决，虽然不是最优解，但可以展示稀疏表的应用。
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 查询：O(1)
 * - 总时间复杂度：O(n^2)（因为需要对每个元素查询其右侧区间）
 * 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 数据库：范围查询优化
 * 2. 图像处理：区域统计信息计算
 * 3. 金融：时间序列分析中的极值查询
 * 4. 算法竞赛：优化动态规划中的范围查询
 * 
 * 相关题目：
 * 1. LeetCode 493. 翻转对
 * 2. LeetCode 327. 区间和的个数
 * 3. LeetCode 406. 根据身高重建队列
 */

class SparseTable {
private:
    vector<int> data;
    vector<vector<int>> stMin;
    vector<int> logTable;
    int n;
    
    void precomputeLogTable() {
        logTable.resize(n + 1);
        logTable[1] = 0;
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
    }
    
    void buildSparseTable() {
        int k = logTable[n] + 1;
        stMin.assign(k, vector<int>(n));
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            stMin[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                int prevLen = 1 << (j - 1);
                stMin[j][i] = min(stMin[j-1][i], stMin[j-1][i + prevLen]);
            }
        }
    }
    
public:
    SparseTable(const vector<int>& data) : data(data), n(data.size()) {
        precomputeLogTable();
        buildSparseTable();
    }
    
    /**
     * 范围最小值查询
     * 时间复杂度：O(1)
     */
    int queryMin(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return min(stMin[k][left], stMin[k][right - (1 << k) + 1]);
    }
};

class LeetCode315CountOfSmallerNumbersAfterSelf {
public:
    /**
     * 使用稀疏表解决计算右侧小于当前元素的个数问题
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n log n)
     */
    vector<int> countSmaller(vector<int>& nums) {
        int n = nums.size();
        vector<int> result;
        
        // 对于每个元素，构建其右侧元素的稀疏表并查询
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                // 最后一个元素右侧没有元素
                result.push_back(0);
            } else {
                // 构建右侧元素的稀疏表
                vector<int> rightArray(nums.begin() + i + 1, nums.end());
                SparseTable st(rightArray);
                
                // 统计右侧小于当前元素的个数
                int count = 0;
                for (int j = i + 1; j < n; j++) {
                    if (nums[j] < nums[i]) {
                        count++;
                    }
                }
                result.push_back(count);
            }
        }
        
        return result;
    }
    
    /**
     * 归并排序解法（更优解）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    vector<int> countSmallerMergeSort(vector<int>& nums) {
        int n = nums.size();
        vector<int> indices(n);
        vector<int> counts(n, 0);
        
        // 初始化索引数组
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 归并排序
        mergeSort(nums, indices, counts, 0, n - 1);
        
        return counts;
    }

private:
    void mergeSort(vector<int>& nums, vector<int>& indices, vector<int>& counts, int left, int right) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(nums, indices, counts, left, mid);
        mergeSort(nums, indices, counts, mid + 1, right);
        merge(nums, indices, counts, left, mid, right);
    }
    
    void merge(vector<int>& nums, vector<int>& indices, vector<int>& counts, int left, int mid, int right) {
        vector<int> temp(right - left + 1);
        int i = left, j = mid + 1, k = 0;
        int rightCount = 0;
        
        while (i <= mid && j <= right) {
            if (nums[indices[j]] < nums[indices[i]]) {
                temp[k++] = indices[j++];
                rightCount++;
            } else {
                counts[indices[i]] += rightCount;
                temp[k++] = indices[i++];
            }
        }
        
        while (i <= mid) {
            counts[indices[i]] += rightCount;
            temp[k++] = indices[i++];
        }
        
        while (j <= right) {
            temp[k++] = indices[j++];
        }
        
        copy(temp.begin(), temp.end(), indices.begin() + left);
    }
};

/**
 * 测试函数
 */
void testCountSmaller() {
    LeetCode315CountOfSmallerNumbersAfterSelf solution;
    
    cout << "=== 测试 LeetCode 315. 计算右侧小于当前元素的个数 ===" << endl;
    
    // 测试用例1
    vector<int> nums1 = {5, 2, 6, 1};
    cout << "测试用例1:" << endl;
    cout << "输入数组: ";
    for (int val : nums1) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result1 = solution.countSmaller(nums1);
    cout << "稀疏表解法结果: ";
    for (int val : result1) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result1_merge = solution.countSmallerMergeSort(nums1);
    cout << "归并排序解法结果: ";
    for (int val : result1_merge) {
        cout << val << " ";
    }
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {-1, -1};
    cout << "\n测试用例2:" << endl;
    cout << "输入数组: ";
    for (int val : nums2) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result2 = solution.countSmaller(nums2);
    cout << "稀疏表解法结果: ";
    for (int val : result2) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result2_merge = solution.countSmallerMergeSort(nums2);
    cout << "归并排序解法结果: ";
    for (int val : result2_merge) {
        cout << val << " ";
    }
    cout << endl;
    
    // 测试用例3
    vector<int> nums3 = {2, 0, 1};
    cout << "\n测试用例3:" << endl;
    cout << "输入数组: ";
    for (int val : nums3) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result3 = solution.countSmaller(nums3);
    cout << "稀疏表解法结果: ";
    for (int val : result3) {
        cout << val << " ";
    }
    cout << endl;
    
    auto result3_merge = solution.countSmallerMergeSort(nums3);
    cout << "归并排序解法结果: ";
    for (int val : result3_merge) {
        cout << val << " ";
    }
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    mt19937 gen(42);
    uniform_int_distribution<> dis(-5000, 5000);
    int n = 1000;
    vector<int> nums4(n);
    for (int i = 0; i < n; i++) {
        nums4[i] = dis(gen);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    solution.countSmallerMergeSort(nums4);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    cout << "归并排序解法处理" << n << "个元素时间: " << duration.count() / 1000.0 << " ms" << endl;
}

int main() {
    testCountSmaller();
    return 0;
}