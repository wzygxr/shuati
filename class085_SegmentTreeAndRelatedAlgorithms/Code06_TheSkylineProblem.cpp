// 218. 天际线问题 - 线段树 + 离散化实现
// 题目来源：LeetCode 218 https://leetcode.cn/problems/the-skyline-problem/
// 
// 题目描述：
// 城市的 天际线 是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
// 给你所有建筑物的位置和高度，请返回 由这些建筑物形成的 天际线。
// 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
// lefti 是第 i 座建筑物左边缘的 x 坐标。
// righti 是第 i 座建筑物右边缘的 x 坐标。
// heighti 是第 i 座建筑物的高度。
// 你可以假设所有的建筑都是完美的长方形，在高度为 0 的绝对平坦的表面上。
// 天际线 应该表示为由 "关键点" 组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标 进行 排序。
// 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0 ，仅用于标记天际线的终点。
// 此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
// 注意：输出天际线中不得有连续的相同高度的水平线。
// 
// 解题思路：
// 使用线段树配合离散化来解决天际线问题
// 1. 收集所有建筑物的左右边界坐标作为关键点
// 2. 对关键点进行离散化处理，建立坐标映射关系
// 3. 使用线段树维护区间最大高度，支持区间更新和单点查询
// 4. 遍历所有建筑物，将每个建筑物的高度更新到对应区间
// 5. 遍历所有离散化后的坐标点，查询高度变化的关键点
// 
// 核心思想：
// 1. 离散化：由于建筑物的坐标可能很大，直接使用原始坐标会导致空间浪费。
//    通过离散化将大范围的坐标映射到较小的连续整数范围，提高效率。
// 2. 线段树：用于维护区间最大高度信息，支持高效的区间更新和查询操作。
// 3. 懒惰传播：在区间更新时使用懒惰标记，避免不必要的重复计算。
// 4. 关键点识别：通过比较相邻点的高度变化来识别天际线的关键点。
// 
// 时间复杂度分析：
// - 收集关键点：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理建筑物：O(n log n)
// - 查询结果：O(n log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)

#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <iostream>
using namespace std;

// 线段树实现，用于解决天际线问题
class SegmentTree {
private:
    vector<int> tree;  // 存储区间最大高度
    vector<int> lazy;  // 懒惰标记
    int n;                  // 线段树维护的区间长度
    
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
            lazy[2 * node] = max(lazy[2 * node], lazy[node]);
            // 左子节点的最大高度更新为覆盖高度
            tree[2 * node] = max(tree[2 * node], lazy[node]);
            
            // 更新右子节点的懒惰标记和最大值
            // 将当前节点的覆盖高度传递给右子节点
            lazy[2 * node + 1] = max(lazy[2 * node + 1], lazy[node]);
            // 右子节点的最大高度更新为覆盖高度
            tree[2 * node + 1] = max(tree[2 * node + 1], lazy[node]);
            
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
        // 懒惰标记
        // lazy[i]表示节点i维护的区间需要覆盖的高度
        lazy.resize(4 * n);
    }
    
    /**
     * 区间修改：将区间[jobl,jobr]的高度更新为jobh（取最大值）
     * 利用懒惰传播优化，避免对每个元素逐一更新
     * 
     * 时间复杂度: O(log n)
     */
    void update(int jobl, int jobr, int jobh, int l, int r, int node) {
        // 优化1：如果当前节点维护的区间完全被操作区间覆盖
        if (jobl <= l && r <= jobr) {
            // 当前区间完全被操作区间覆盖，更新懒惰标记和最大值
            // 这是懒惰传播的关键：只标记不立即执行
            lazy[node] = max(lazy[node], jobh);
            tree[node] = max(tree[node], jobh);
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
     * 计算天际线
     * 通过离散化和线段树来高效解决天际线问题
     * 
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    vector<vector<int>> getSkyline(vector<vector<int>>& buildings) {
        // 第一步：收集所有关键x坐标（建筑物的左右边界）
        // 为了处理边界情况，我们还需要添加右边界前一个位置的坐标
        set<int> positions;
        for (auto& building : buildings) {
            positions.insert(building[0]);      // 左边界
            positions.insert(building[1]);      // 右边界
            positions.insert(building[1] - 1);  // 添加右端点前一个位置，用于处理边界情况
        }
        
        // 第二步：排序并建立离散化映射
        // 将set转换为vector并排序，然后建立坐标映射关系
        vector<int> sorted_positions(positions.begin(), positions.end());
        // 实际坐标 -> 索引
        map<int, int> pos_to_idx;
        // 索引 -> 实际坐标
        map<int, int> idx_to_pos;
        for (int i = 0; i < sorted_positions.size(); i++) {
            pos_to_idx[sorted_positions[i]] = i + 1;
            idx_to_pos[i + 1] = sorted_positions[i];
        }
        
        // 第三步：构建线段树
        // 初始化线段树，维护区间最大高度信息
        SegmentTree seg_tree(sorted_positions.size());
        
        // 第四步：处理每个建筑物，将高度更新到对应区间
        // 遍历所有建筑物，将每个建筑物的高度更新到对应的离散化区间
        for (auto& building : buildings) {
            // 将建筑物的左右边界映射到离散化后的索引
            int left_idx = pos_to_idx[building[0]];      // 左边界映射
            int right_idx = pos_to_idx[building[1] - 1]; // 右边界前一个位置映射
            // 更新区间高度，取最大值
            // 参数说明：
            // left_idx: 操作区间左边界
            // right_idx: 操作区间右边界
            // building[2]: 要更新的高度
            // 1: 当前节点维护的区间左边界
            // sorted_positions.size(): 当前节点维护的区间右边界
            // 1: 当前节点索引
            seg_tree.update(left_idx, right_idx, building[2], 1, sorted_positions.size(), 1);
        }
        
        // 第五步：收集结果
        // 遍历所有离散化后的坐标点，查询高度变化的关键点
        vector<vector<int>> result;
        int pre_height = 0;  // 上一个高度，初始为0表示地面高度
        
        // 遍历所有离散化后的坐标点
        for (int i = 1; i <= sorted_positions.size(); i++) {
            // 查询当前点的高度（即该点的最大建筑物高度）
            int height = seg_tree.query(i, i, 1, sorted_positions.size(), 1);  // 查询当前点的高度
            // 获取实际坐标
            int x_pos = idx_to_pos[i];  // 获取实际坐标
            
            // 如果高度发生变化，则记录关键点
            // 只有当当前高度与上一个高度不同时，才记录为关键点
            if (height != pre_height) {
                result.push_back({x_pos, height});  // 添加关键点[x坐标, y坐标(高度)]
                pre_height = height;                // 更新上一个高度
            }
        }
        
        return result;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
    // 期望输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
    vector<vector<int>> buildings1 = {{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
    vector<vector<int>> result1 = solution.getSkyline(buildings1);
    
    cout << "测试用例1结果: ";
    for (auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    // 测试用例2: buildings = [[0,2,3],[2,5,3]]
    // 期望输出: [[0,3],[5,0]]
    vector<vector<int>> buildings2 = {{0,2,3},{2,5,3}};
    vector<vector<int>> result2 = solution.getSkyline(buildings2);
    
    cout << "测试用例2结果: ";
    for (auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    
    return 0;
}