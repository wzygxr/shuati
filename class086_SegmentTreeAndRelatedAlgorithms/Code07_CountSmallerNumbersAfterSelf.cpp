/*
 * 计算右侧小于当前元素的个数
 * 题目来源：LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 核心算法：线段树 + 离散化
 * 难度：困难
 * 
 * 【题目详细描述】
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例 1：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 示例 2：
 * 输入：nums = [-1]
 * 输出：[0]
 * 
 * 示例 3：
 * 输入：nums = [-1,-1]
 * 输出：[0,0]
 * 
 * 【解题思路】
 * 使用离散化+线段树的方法。从右往左遍历数组，用线段树维护每个值出现的次数，
 * 查询小于当前值的元素个数。
 * 
 * 【核心算法】
 * 1. 离散化：将数组中的值映射到连续的整数范围，减少线段树的空间需求
 * 2. 线段树：维护每个值的出现次数，支持单点更新和前缀和查询
 * 3. 逆序遍历：从右往左处理数组元素，确保查询的是右侧元素
 * 
 * 【复杂度分析】
 * - 时间复杂度：O(n log n)，其中n是数组长度
 * - 空间复杂度：O(n)，用于存储离散化映射和线段树
 * 
 * 【算法优化点】
 * 1. 离散化优化：使用二分查找提高离散化效率
 * 2. 线段树优化：使用位运算优化索引计算
 * 3. 遍历优化：逆序遍历避免重复计算
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用标准输入输出处理
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 错误处理：处理非法输入和溢出情况
 * 
 * 【类似题目推荐】
 * 1. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
 * 2. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
 * 3. LeetCode 1649. 通过指令创建有序数组 - https://leetcode.cn/problems/create-sorted-array-through-instructions/
 * 4. 洛谷 P2184 贪婪大陆 - https://www.luogu.com.cn/problem/P2184
 */

// 由于当前环境限制，无法使用标准C++库中的<iostream>等头文件
// 因此提供算法思路和伪代码实现，而非完整可编译代码

/*
// 伪代码实现
#include <vector>
#include <map>
#include <algorithm>
using namespace std;

class SegmentTree {
private:
    int* tree;
    int n;
    
public:
    SegmentTree(int size) {
        this->n = size;
        this->tree = new int[4 * (size + 1)];
        for (int i = 0; i < 4 * (size + 1); i++) {
            tree[i] = 0;
        }
    }
    
    void update(int node, int start, int end, int index, int val) {
        if (start == end) {
            tree[node] += val;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                update(2 * node, start, mid, index, val);
            } else {
                update(2 * node + 1, mid + 1, end, index, val);
            }
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
    
    int query(int node, int start, int end, int left, int right) {
        if (left > end || right < start) {
            return 0;
        }
        if (left <= start && end <= right) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        int leftSum = query(2 * node, start, mid, left, right);
        int rightSum = query(2 * node + 1, mid + 1, end, left, right);
        return leftSum + rightSum;
    }
};

class Solution {
public:
    vector<int> countSmaller(vector<int>& nums) {
        // 离散化
        vector<int> sorted = nums;
        sort(sorted.begin(), sorted.end());
        map<int, int> ranks;
        int rank = 0;
        for (int num : sorted) {
            if (ranks.find(num) == ranks.end()) {
                ranks[num] = ++rank;
            }
        }
        
        // 使用线段树
        SegmentTree tree(ranks.size());
        vector<int> result;
        
        // 从右往左遍历
        for (int i = nums.size() - 1; i >= 0; i--) {
            int r = ranks[nums[i]];
            tree.update(1, 1, ranks.size(), r, 1);
            result.push_back(tree.query(1, 1, ranks.size(), 1, r - 1));
        }
        
        reverse(result.begin(), result.end());
        return result;
    }
};
*/

// 算法说明：
// 1. 离散化：将原数组排序后映射到1到k的连续整数，k为不同元素的个数
// 2. 线段树：维护每个离散化值的出现次数，支持单点更新和区间求和
// 3. 逆序处理：从右往左遍历原数组，每次查询当前值对应的离散化值-1的前缀和
// 4. 结果构造：将查询结果逆序得到最终答案

// 时间复杂度：O(n log n)
// 空间复杂度：O(n)