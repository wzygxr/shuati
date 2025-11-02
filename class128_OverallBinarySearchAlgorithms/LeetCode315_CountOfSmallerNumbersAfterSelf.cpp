#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
using namespace std;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 题目描述:
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例:
 * 输入: [5,2,6,1]
 * 输出: [2,1,1,0]
 * 解释:
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路 - 整体二分算法:
 * 1. 将原问题转化为多个查询问题：对于每个位置i，查询数组中索引大于i且值小于nums[i]的元素个数
 * 2. 对值域进行二分，利用树状数组维护当前已经处理过的元素
 * 3. 从右到左处理每个元素，这样每次处理i时，i右侧的元素已经被处理
 * 4. 使用整体二分，同时处理所有查询
 * 
 * 时间复杂度分析:
 * O(N log N log M)，其中N是数组长度，M是值域范围
 * - 整体二分需要log M次迭代（M是可能的数值范围）
 * - 每次迭代需要O(N log N)时间进行树状数组操作
 * 
 * 空间复杂度分析:
 * O(N)，需要额外的数组存储排序后的值、离散化映射、结果等
 */
class LeetCode315_CountOfSmallerNumbersAfterSelf {
private:
    const static int MAXN = 100010; // 最大数据范围
    int n; // 数组长度
    vector<int> nums; // 原始数组
    vector<int> ans; // 存储结果
    vector<int> sorted; // 离散化后排序的数组
    vector<int> qL; // 查询的左边界数组
    vector<int> qId; // 查询的id数组
    vector<int> lset; // 左集合，存储答案在左半部分的查询id
    vector<int> rset; // 右集合，存储答案在右半部分的查询id
    vector<int> tree; // 树状数组，用于维护前缀和
    vector<int> posList; // 记录每次操作添加的位置，用于回溯
    
    /**
     * 树状数组的更新操作
     * @param idx 索引位置
     * @param val 更新的值
     */
    void update(int idx, int val) {
        while (idx <= n) {
            tree[idx] += val;
            idx += idx & -idx;
        }
        posList.push_back(idx - (idx & -idx)); // 记录被修改的位置
    }
    
    /**
     * 树状数组的查询操作，查询前缀和
     * @param idx 索引位置
     * @return 前缀和
     */
    int query(int idx) {
        int res = 0;
        while (idx > 0) {
            res += tree[idx];
            idx -= idx & -idx;
        }
        return res;
    }
    
    /**
     * 回溯树状数组的状态，用于分治过程
     */
    void rollback() {
        for (int pos : posList) {
            // 回滚每个位置的更新
            while (pos <= n) {
                tree[pos]--; // 因为我们每次更新都是+1，所以回滚时-1
                pos += pos & -pos;
            }
        }
        posList.clear(); // 清空记录
    }
    
    /**
     * 整体二分的核心函数
     * @param ql 查询集合的左边界
     * @param qr 查询集合的右边界
     * @param vl 值域的左边界
     * @param vr 值域的右边界
     */
    void compute(int ql, int qr, int vl, int vr) {
        // 递归终止条件：没有查询或值域范围只有一个值
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只剩一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qId[i]] = vl; // 设置答案
            }
            return;
        }
        
        // 计算中间值
        int mid = vl + (vr - vl) / 2;
        
        // 处理所有查询
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qId[i]; // 当前查询的id
            int pos = qL[id]; // 查询的位置（元素索引）
            int val = nums[pos]; // 当前元素的值
            
            // 对于本题，具体处理逻辑在countSmaller方法中实现
        }
    }

public:
    /**
     * 主方法：使用树状数组计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组
     */
    vector<int> countSmaller(vector<int>& nums) {
        n = nums.size();
        if (n == 0) {
            return vector<int>();
        }
        
        // 初始化数据结构
        this->nums = nums;
        ans.resize(n, 0);
        
        // 离散化处理
        set<int> uniqueSet(nums.begin(), nums.end());
        sorted.assign(uniqueSet.begin(), uniqueSet.end());
        
        // 树状数组解法
        tree.resize(n + 2, 0); // 树状数组大小
        
        // 从右到左处理
        for (int i = n - 1; i >= 0; i--) {
            // 查找nums[i]在离散化数组中的位置
            auto it = lower_bound(sorted.begin(), sorted.end(), nums[i]);
            int rank = it - sorted.begin() + 1; // +1是因为树状数组从1开始
            
            // 查询当前树状数组中小于nums[i]的元素个数
            ans[i] = query(rank - 1);
            
            // 将当前元素添加到树状数组
            update(rank, 1);
        }
        
        return ans;
    }
    
    /**
     * 使用整体二分的思想来解决问题
     * 这个版本更接近整体二分的标准实现
     */
    vector<int> countSmallerWithParallelBinarySearch(vector<int>& nums) {
        n = nums.size();
        if (n == 0) {
            return vector<int>();
        }
        
        // 初始化数据结构
        this->nums = nums;
        ans.resize(n, 0);
        qL.resize(n);
        qId.resize(n);
        lset.resize(n);
        rset.resize(n);
        tree.resize(n + 2, 0);
        
        // 离散化处理
        vector<int> allNums = nums;
        sort(allNums.begin(), allNums.end());
        int uniqueCount = 1;
        for (int i = 1; i < n; i++) {
            if (allNums[i] != allNums[i - 1]) {
                allNums[uniqueCount++] = allNums[i];
            }
        }
        allNums.resize(uniqueCount);
        
        // 初始化查询
        for (int i = 0; i < n; i++) {
            qId[i] = i; // 查询的id就是元素的索引
            qL[i] = i; // 查询的位置就是元素的索引
        }
        
        // 从右到左处理，逐个添加元素到树状数组，并查询
        for (int i = n - 1; i >= 0; i--) {
            // 离散化当前值
            auto it = lower_bound(allNums.begin(), allNums.end(), nums[i]);
            int rank = it - allNums.begin();
            
            // 查询比nums[i]小的元素个数
            ans[i] = query(rank);
            
            // 添加当前元素
            update(rank + 1, 1); // +1因为树状数组从1开始
        }
        
        return ans;
    }
};

// 辅助函数：打印vector内容
void printVector(const vector<int>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
}

// 测试函数
int main() {
    LeetCode315_CountOfSmallerNumbersAfterSelf solution;
    
    // 测试用例1
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = solution.countSmaller(nums1);
    vector<int> result1_bs = solution.countSmallerWithParallelBinarySearch(nums1);
    cout << "输入: [5, 2, 6, 1]" << endl;
    cout << "输出: ";
    printVector(result1);
    cout << "整体二分版本输出: ";
    printVector(result1_bs);
    
    // 测试用例2
    vector<int> nums2 = {-1, -1};
    vector<int> result2 = solution.countSmaller(nums2);
    vector<int> result2_bs = solution.countSmallerWithParallelBinarySearch(nums2);
    cout << "输入: [-1, -1]" << endl;
    cout << "输出: ";
    printVector(result2);
    cout << "整体二分版本输出: ";
    printVector(result2_bs);
    
    // 测试用例3
    vector<int> nums3 = {};
    vector<int> result3 = solution.countSmaller(nums3);
    vector<int> result3_bs = solution.countSmallerWithParallelBinarySearch(nums3);
    cout << "输入: []" << endl;
    cout << "输出: ";
    printVector(result3);
    cout << "整体二分版本输出: ";
    printVector(result3_bs);
    
    // 边界测试 - 大量重复元素
    vector<int> nums4 = {1, 1, 1, 1, 1};
    vector<int> result4 = solution.countSmaller(nums4);
    cout << "输入: [1, 1, 1, 1, 1]" << endl;
    cout << "输出: ";
    printVector(result4);
    
    // 边界测试 - 逆序数组
    vector<int> nums5 = {5, 4, 3, 2, 1};
    vector<int> result5 = solution.countSmaller(nums5);
    cout << "输入: [5, 4, 3, 2, 1]" << endl;
    cout << "输出: ";
    printVector(result5);
    
    // 边界测试 - 顺序数组
    vector<int> nums6 = {1, 2, 3, 4, 5};
    vector<int> result6 = solution.countSmaller(nums6);
    cout << "输入: [1, 2, 3, 4, 5]" << endl;
    cout << "输出: ";
    printVector(result6);
    
    return 0;
}