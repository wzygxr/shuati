/**
 * LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
 * 
 * 题目描述：
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 解题思路：
 * 使用树状数组（Fenwick Tree）+ 离散化来高效统计右侧小于当前元素的个数。
 * 核心思想：
 * 1. 从右向左遍历数组
 * 2. 对于每个元素 nums[i]，需要统计右侧已经遍历过的元素中小于 nums[i] 的个数
 * 3. 使用树状数组维护已经遍历过的元素的出现次数
 * 4. 通过离散化处理大数值范围问题
 * 
 * 时间复杂度分析：
 * - 离散化处理：O(n log n)
 * - 树状数组操作：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 离散化数组：O(n)
 * - 树状数组：O(n)
 * - 结果数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个元素等情况
 * 2. 数值范围处理：使用离散化处理大数值范围
 * 3. 异常处理：检查输入参数合法性
 * 4. 性能优化：使用树状数组提高统计效率
 * 
 * 算法技巧：
 * - 离散化：将大范围的数值映射到小范围的索引
 * - 树状数组：高效统计元素出现次数
 * - 逆序遍历：从右向左处理，便于统计右侧元素
 * 
 * 适用场景：
 * - 需要统计数组中每个元素右侧小于它的元素个数
 * - 数值范围较大，需要离散化处理
 * - 对时间复杂度要求较高的场景
 * 
 * 测试用例：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <stdexcept>

using namespace std;

class Solution {
private:
    /**
     * 树状数组（Fenwick Tree）实现
     * 用于高效统计元素出现次数
     */
    class FenwickTree {
    private:
        vector<int> tree;
        int size;
        
        /**
         * 计算lowbit（最低位的1）
         * 
         * @param x 输入数字
         * @return lowbit值
         */
        int lowbit(int x) {
            return x & -x;
        }
        
    public:
        /**
         * 构造函数
         * 
         * @param size 树状数组大小
         */
        FenwickTree(int size) : size(size) {
            tree.resize(size + 1, 0);
        }
        
        /**
         * 更新操作：在指定位置增加一个值
         * 
         * @param index 位置索引
         * @param delta 增加值
         */
        void update(int index, int delta) {
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        /**
         * 查询前缀和：从1到index的和
         * 
         * @param index 位置索引
         * @return 前缀和
         */
        int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
    };
    
public:
    /**
     * 计算右侧小于当前元素的个数
     * 
     * @param nums 输入数组
     * @return 结果数组，counts[i] 表示 nums[i] 右侧小于 nums[i] 的元素个数
     * 
     * 算法步骤：
     * 1. 离散化处理数组元素
     * 2. 从右向左遍历数组
     * 3. 对于每个元素，查询树状数组中比它小的元素个数
     * 4. 更新树状数组，记录当前元素的出现
     */
    vector<int> countSmaller(vector<int>& nums) {
        if (nums.empty()) {
            return {};
        }
        
        int n = nums.size();
        vector<int> result(n, 0);
        
        // 1. 离散化处理
        vector<int> sortedNums = nums;
        sort(sortedNums.begin(), sortedNums.end());
        
        map<int, int> rankMap;
        int rank = 1;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sortedNums[i] != sortedNums[i - 1]) {
                rankMap[sortedNums[i]] = rank++;
            }
        }
        
        // 2. 使用树状数组统计
        FenwickTree tree(rank - 1);
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            int currentRank = rankMap[nums[i]];
            
            // 查询比当前元素小的元素个数（即排名比当前小的元素）
            int count = tree.query(currentRank - 1);
            result[i] = count;
            
            // 更新树状数组，记录当前元素的出现
            tree.update(currentRank, 1);
        }
        
        return result;
    }
    
    /**
     * 方法二：使用归并排序统计逆序对（备选方案）
     * 
     * 解题思路：
     * 1. 使用归并排序的过程统计右侧小于当前元素的个数
     * 2. 在合并两个有序数组时，统计右侧较小元素的个数
     * 3. 这种方法同样具有 O(n log n) 的时间复杂度
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    vector<int> countSmallerMergeSort(vector<int>& nums) {
        if (nums.empty()) {
            return {};
        }
        
        int n = nums.size();
        vector<int> indexes(n);
        vector<int> counts(n, 0);
        
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
        
        vector<int> tempIndexes(n);
        mergeSort(nums, indexes, counts, tempIndexes, 0, n - 1);
        
        return counts;
    }
    
private:
    void mergeSort(vector<int>& nums, vector<int>& indexes, vector<int>& counts, 
                   vector<int>& tempIndexes, int start, int end) {
        if (start >= end) {
            return;
        }
        
        int mid = start + (end - start) / 2;
        mergeSort(nums, indexes, counts, tempIndexes, start, mid);
        mergeSort(nums, indexes, counts, tempIndexes, mid + 1, end);
        merge(nums, indexes, counts, tempIndexes, start, mid, end);
    }
    
    void merge(vector<int>& nums, vector<int>& indexes, vector<int>& counts,
               vector<int>& tempIndexes, int start, int mid, int end) {
        int left = start, right = mid + 1;
        int rightCount = 0;
        int index = 0;
        
        while (left <= mid && right <= end) {
            if (nums[indexes[right]] < nums[indexes[left]]) {
                tempIndexes[index] = indexes[right];
                rightCount++;
                right++;
            } else {
                tempIndexes[index] = indexes[left];
                counts[indexes[left]] += rightCount;
                left++;
            }
            index++;
        }
        
        while (left <= mid) {
            tempIndexes[index] = indexes[left];
            counts[indexes[left]] += rightCount;
            left++;
            index++;
        }
        
        while (right <= end) {
            tempIndexes[index] = indexes[right];
            right++;
            index++;
        }
        
        for (int i = 0; i < index; i++) {
            indexes[start + i] = tempIndexes[i];
        }
    }
};

/**
 * 测试函数：验证算法正确性
 * 
 * 测试用例设计：
 * 1. 正常情况测试
 * 2. 边界情况测试
 * 3. 空数组测试
 * 4. 重复元素测试
 */
void testCountSmaller() {
    Solution solution;
    
    // 测试用例1：正常情况
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = solution.countSmaller(nums1);
    cout << "测试用例1结果：";
    for (int num : result1) {
        cout << num << " ";
    }
    cout << "(期望：2 1 1 0)" << endl;
    
    // 测试用例2：边界情况
    vector<int> nums2 = {-1};
    vector<int> result2 = solution.countSmaller(nums2);
    cout << "测试用例2结果：";
    for (int num : result2) {
        cout << num << " ";
    }
    cout << "(期望：0)" << endl;
    
    // 测试用例3：空数组
    vector<int> nums3 = {};
    vector<int> result3 = solution.countSmaller(nums3);
    cout << "测试用例3结果：";
    for (int num : result3) {
        cout << num << " ";
    }
    cout << "(期望：空)" << endl;
    
    // 测试用例4：重复元素
    vector<int> nums4 = {2, 2, 2, 2};
    vector<int> result4 = solution.countSmaller(nums4);
    cout << "测试用例4结果：";
    for (int num : result4) {
        cout << num << " ";
    }
    cout << "(期望：0 0 0 0)" << endl;
    
    // 测试用例5：大数值
    vector<int> nums5 = {2147483647, -2147483648, 0, 1};
    vector<int> result5 = solution.countSmaller(nums5);
    cout << "测试用例5结果：";
    for (int num : result5) {
        cout << num << " ";
    }
    cout << "(期望：3 0 0 0)" << endl;
    
    cout << "所有测试用例执行完成！" << endl;
}

int main() {
    try {
        testCountSmaller();
    } catch (const exception& e) {
        cerr << "错误：" << e.what() << endl;
        return 1;
    }
    
    return 0;
}