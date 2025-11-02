// 699. 掉落的方块 - 线段树实现
// 题目来源：LeetCode 699 https://leetcode.cn/problems/falling-squares/
// 
// 题目描述：
// 在二维平面上的 x 轴上，放置着一些方块。
// 给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：
// 第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
// 每个方块都从一个比目前所有的落地方块更高的高度掉落而下。
// 方块沿 y 轴负方向下落，直到着陆到 另一个正方形的顶边 或者是 x 轴上。
// 一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。
// 一旦着陆，它就会固定在原地，无法移动。
// 在每个方块掉落后，你必须记录目前所有已经落稳的 方块堆叠的最高高度。
// 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。
// 
// 解题思路：
// 使用线段树配合离散化来解决掉落方块问题
// 1. 收集所有方块的左右边界坐标作为关键点
// 2. 对关键点进行离散化处理，建立坐标映射关系
// 3. 使用线段树维护区间最大高度，支持区间更新和区间查询
// 4. 对于每个掉落的方块，先查询其底部区间当前的最大高度，然后将整个区间更新为新高度
// 5. 记录每次掉落后的全局最大高度
// 
// 核心思想：
// 1. 离散化：由于方块的坐标可能很大，直接使用原始坐标会导致空间浪费。
//    通过离散化将大范围的坐标映射到较小的连续整数范围，提高效率。
// 2. 线段树：用于维护区间最大高度信息，支持高效的区间更新和查询操作。
// 3. 懒惰传播：在区间更新时使用懒惰标记，避免不必要的重复计算。
// 4. 着陆高度计算：新方块的着陆高度等于其底部区间当前的最大高度。
// 5. 堆叠高度计算：新方块的堆叠高度 = 着陆高度 + 方块高度。
// 
// 时间复杂度分析：
// - 收集关键点：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理方块：O(n log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)

#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <iostream>
using namespace std;

// 线段树实现，用于解决掉落的方块问题
class SegmentTree {
private:
    vector<int> tree;  // 存储区间最大高度
    vector<int> lazy;  // 懒惰标记：区间覆盖的高度
    int n;             // 线段树维护的区间长度
    
    /**
     * 上推操作，更新父节点信息
     * 将左右子节点的最大值更新到父节点
     * 在线段树中，父节点的值通常由子节点的值计算得出
     * 对于本问题，父节点维护的区间最大高度等于左右子节点维护区间最大高度的最大值
     * 
     * 时间复杂度: O(1)
     */
    void push_up(int node) {
        // 更新当前节点的最大高度为左右子节点最大高度的最大值
        tree[node] = max(tree[2 * node], tree[2 * node + 1]);
    }
    
    /**
     * 下传操作，传递懒惰标记
     * 将当前节点的懒惰标记传递给左右子节点
     * 懒惰传播是线段树优化的重要技术，用于延迟更新操作
     * 只有在真正需要访问子节点时才将更新操作传递下去，避免不必要的计算
     * 
     * 时间复杂度: O(1)
     */
    void push_down(int node, int ln, int rn) {
        // 只有当当前节点有懒惰标记时才需要下传
        if (lazy[node] != 0) {
            // 更新左子节点的懒惰标记和最大值
            // 将当前节点的覆盖高度传递给左子节点
            lazy[2 * node] = lazy[node];
            // 左子节点的最大高度更新为覆盖高度
            tree[2 * node] = lazy[node];
            
            // 更新右子节点的懒惰标记和最大值
            // 将当前节点的覆盖高度传递给右子节点
            lazy[2 * node + 1] = lazy[node];
            // 右子节点的最大高度更新为覆盖高度
            tree[2 * node + 1] = lazy[node];
            
            // 清除当前节点的懒惰标记
            // 标记已传递，当前节点的懒惰标记清零
            lazy[node] = 0;
        }
    }

public:
    /**
     * 初始化线段树
     * 线段树是一种完全二叉树，可以用数组来存储
     * 对于节点i，其左子节点为2*i，右子节点为2*i+1
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    SegmentTree(int n) {
        this->n = n;
        // 存储区间最大高度
        // tree[i]表示节点i维护的区间的最大高度
        tree.resize(4 * n);
        // 懒惰标记：区间覆盖的高度
        // lazy[i]表示节点i维护的区间需要覆盖的高度
        lazy.resize(4 * n);
    }
    
    /**
     * 区间修改：将区间[jobl,jobr]的高度更新为jobh（覆盖）
     * 利用懒惰传播优化，避免对每个元素逐一更新
     * 
     * 时间复杂度: O(log n)
     */
    void update(int jobl, int jobr, int jobh, int l, int r, int node) {
        // 优化1：如果当前节点维护的区间完全被操作区间覆盖
        if (jobl <= l && r <= jobr) {
            // 当前区间完全被操作区间覆盖，更新懒惰标记和最大值
            // 这是懒惰传播的关键：只标记不立即执行
            lazy[node] = jobh;
            tree[node] = jobh;
            return;
        }
        
        // 计算中点，将区间分为两部分
        int mid = (l + r) / 2;
        // 下传懒惰标记
        // 在递归处理子节点之前，需要确保当前节点的懒惰标记已经传递
        push_down(node, mid - l + 1, r - mid);
        
        // 递归更新左子树
        // 只有当操作区间与左子树区间有交集时才继续处理
        if (jobl <= mid) {
            update(jobl, jobr, jobh, l, mid, 2 * node);
        }
        // 递归更新右子树
        // 只有当操作区间与右子树区间有交集时才继续处理
        if (jobr > mid) {
            update(jobl, jobr, jobh, mid + 1, r, 2 * node + 1);
        }
        
        // 上推更新父节点
        // 将子节点的更新结果合并到当前节点
        push_up(node);
    }
    
    /**
     * 查询区间最大值
     * 在查询过程中需要确保懒惰标记已经正确传递
     * 
     * 时间复杂度: O(log n)
     */
    int query(int jobl, int jobr, int l, int r, int node) {
        // 优化1：如果当前节点维护的区间完全包含在查询区间内
        if (jobl <= l && r <= jobr) {
            // 当前区间完全包含在查询区间内，直接返回最大值
            // 这是线段树查询的优化点：如果当前区间完全在查询区间内，直接返回结果
            return tree[node];
        }
        
        // 计算中点，将区间分为两部分
        int mid = (l + r) / 2;
        // 下传懒惰标记
        // 在查询时必须确保懒惰标记已经传递，以保证结果正确
        push_down(node, mid - l + 1, r - mid);
        
        int res = 0;
        // 递归查询左子树
        // 只有当查询区间与左子树区间有交集时才继续查询
        if (jobl <= mid) {
            res = max(res, query(jobl, jobr, l, mid, 2 * node));
        }
        // 递归查询右子树
        // 只有当查询区间与右子树区间有交集时才继续查询
        if (jobr > mid) {
            res = max(res, query(jobl, jobr, mid + 1, r, 2 * node + 1));
        }
        return res;
    }
};

class Solution {
public:
    /**
     * 计算掉落方块后的最大高度
     * 通过离散化和线段树来高效解决掉落方块问题
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    vector<int> fallingSquares(vector<vector<int>>& positions) {
        // 第一步：收集所有关键x坐标（方块的左右边界）
        set<int> positions_set;
        for (auto& pos : positions) {
            positions_set.insert(pos[0]);              // 左边界
            positions_set.insert(pos[0] + pos[1] - 1); // 右边界
        }
        
        // 第二步：排序并建立离散化映射：实际坐标 -> 索引
        // 将set转换为vector并排序，然后建立坐标映射关系
        vector<int> sorted_positions(positions_set.begin(), positions_set.end());
        // 实际坐标 -> 索引
        map<int, int> pos_to_idx;
        for (int i = 0; i < sorted_positions.size(); i++) {
            pos_to_idx[sorted_positions[i]] = i + 1;
        }
        
        // 第三步：构建线段树
        // 初始化线段树，维护区间最大高度信息
        SegmentTree seg_tree(sorted_positions.size());
        
        // 第四步：处理每个方块并收集结果
        vector<int> result;
        int max_height = 0;  // 全局最大高度
        
        // 处理每个方块
        for (auto& pos : positions) {
            // 计算方块的左右边界
            int left = pos[0];                    // 方块左边界
            int size = pos[1];                    // 方块边长
            int right = left + size - 1;          // 方块右边界
            
            // 查询当前方块底部区间最大高度（即着陆高度）
            // 方块会落在其底部区间当前最大高度之上
            int current_height = seg_tree.query(pos_to_idx[left], pos_to_idx[right], 
                                              1, sorted_positions.size(), 1);
            // 新的高度 = 着陆高度 + 方块高度
            int new_height = current_height + size;
            
            // 更新方块所在区间高度为新高度
            // 将方块覆盖的区间更新为新的堆叠高度
            seg_tree.update(pos_to_idx[left], pos_to_idx[right], new_height,
                          1, sorted_positions.size(), 1);
            
            // 更新全局最大高度
            // 记录到目前为止所有方块堆叠的最大高度
            max_height = max(max_height, new_height);
            result.push_back(max_height);
        }
        
        return result;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1: positions = [[1,2],[2,3],[6,1]]
    // 期望输出: [2,5,5]
    // 解释：第一个方块[1,2]高度为2，第二个方块[2,4]底部高度为2，总高度为5，第三个方块[6,6]底部高度为0，总高度为1
    vector<vector<int>> positions1 = {{1,2},{2,3},{6,1}};
    vector<int> result1 = solution.fallingSquares(positions1);
    
    cout << "测试用例1结果: ";
    for (int val : result1) {
        cout << val << " ";
    }
    cout << endl;
    
    // 测试用例2: positions = [[100,100],[200,100]]
    // 期望输出: [100,100]
    // 解释：两个方块不重叠，各自高度为100
    vector<vector<int>> positions2 = {{100,100},{200,100}};
    vector<int> result2 = solution.fallingSquares(positions2);
    
    cout << "测试用例2结果: ";
    for (int val : result2) {
        cout << val << " ";
    }
    cout << endl;
    
    return 0;
}