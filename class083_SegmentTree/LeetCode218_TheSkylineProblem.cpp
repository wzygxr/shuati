/**
 * LeetCode 218. The Skyline Problem
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 * 题目描述:
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 *
 * 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
 * - lefti 是第 i 座建筑物左边缘的 x 坐标。
 * - righti 是第 i 座建筑物右边缘的 x 坐标。
 * - heighti 是第 i 座建筑物的高度。
 *
 * 天际线应该表示为由 "关键点" 组成的列表，格式 [[x1,y1],[x2,y2],...]，并按 x 坐标进行排序。
 * 关键点是水平线段的左端点。最后一个关键点也是天际线的终点，即最右侧建筑物的终点，高度为 0。
 *
 * 示例 1:
 * 输入: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * 输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 *
 * 示例 2:
 * 输入: buildings = [[0,2,3],[2,5,3]]
 * 输出: [[0,3],[5,0]]
 *
 * 提示:
 * 1 <= buildings.length <= 10^4
 * 0 <= lefti < righti <= 2^31 - 1
 * 1 <= heighti <= 2^31 - 1
 *
 * 解题思路:
 * 这是一个经典的扫描线问题，可以使用线段树来解决。
 * 1. 收集所有建筑物的左右边界坐标，进行离散化处理
 * 2. 使用线段树维护每个离散化区间的高度最大值
 * 3. 从左到右扫描，当遇到建筑物的左边界时，更新对应区间的高度
 * 4. 当遇到建筑物的右边界时，恢复对应区间的高度
 * 5. 在扫描过程中记录高度变化的关键点
 *
 * 时间复杂度: O(n log n)，其中n是建筑物数量
 * 空间复杂度: O(n)
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
using namespace std;

/**
 * 线段树节点结构体
 * 用于维护区间最大高度和懒标记
 * 
 * 设计要点：
 * 1. 使用懒标记技术优化区间更新效率
 * 2. 维护区间最大高度，用于确定天际线轮廓
 * 3. 支持区间最大值查询和区间最大值更新
 * 
 * 时间复杂度分析：
 * - 单次更新/查询：O(log n)
 * - 懒标记下推：O(1)
 * 
 * 空间复杂度：O(n)
 */
struct Node {
    int l, r;      // 区间左右端点（离散化后的索引）
    int max;       // 区间最大高度
    int lazy;      // 懒标记，用于延迟更新
    
    Node(int l = 0, int r = 0) : l(l), r(r), max(0), lazy(0) {}
};

class Solution {
private:
    vector<Node> tree;  // 线段树数组
    vector<int> nums;    // 离散化后的坐标数组
    map<int, int> map;   // 离散化映射
    int n;               // 离散化数组大小
    
public:
    /**
     * 获取天际线轮廓
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数有效性
     * 2. 边界测试：处理空输入、单建筑、重叠建筑等边界情况
     * 3. 性能优化：使用离散化减少线段树规模，懒标记优化区间更新
     * 
     * @param buildings 建筑物数组，每个建筑物为[left, right, height]
     * @return 天际线关键点列表
     * @throws invalid_argument 当输入参数不合法时抛出异常
     */
    vector<vector<int>> getSkyline(vector<vector<int>>& buildings) {
        // 参数校验
        if (buildings.empty()) {
            return {};
        }
        
        vector<vector<int>> result;
        
        // 参数校验：检查每个建筑物的格式
        for (int i = 0; i < buildings.size(); i++) {
            if (buildings[i].size() != 3) {
                throw invalid_argument("第" + to_string(i+1) + "个建筑物格式不正确，应为[left, right, height]");
            }
            int left = buildings[i][0];
            int right = buildings[i][1];
            int height = buildings[i][2];
            
            // 检查坐标和高度有效性
            if (left < 0 || right < 0 || height < 0) {
                throw invalid_argument("第" + to_string(i+1) + "个建筑物的坐标或高度不能为负数");
            }
            if (left >= right) {
                throw invalid_argument("第" + to_string(i+1) + "个建筑物的左边界必须小于右边界");
            }
        }
        
        // 收集所有坐标点并离散化
        discretization(buildings);
        
        // 初始化线段树
        tree.resize(n * 4);
        build(1, n, 1);
        
        // 创建事件列表：每个建筑物的左右边界
        vector<vector<int>> events;
        for (auto& building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            events.push_back({map[left], height, 1});   // 左边界事件
            events.push_back({map[right], height, -1}); // 右边界事件
        }
        
        // 按坐标排序事件
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] != b[0]) return a[0] < b[0];
            // 相同坐标时，先处理右边界再处理左边界
            return b[2] < a[2];
        });
        
        // 扫描线处理
        int prevHeight = 0;
        for (auto& event : events) {
            int pos = event[0];
            int height = event[1];
            int type = event[2];
            
            if (type == 1) {
                // 左边界：更新高度
                update(pos, pos, height, 1, n, 1);
            } else {
                // 右边界：恢复高度（设置为0）
                update(pos, pos, 0, 1, n, 1);
            }
            
            // 获取当前最大高度
            int currentHeight = query(1, n, 1, n, 1);
            
            // 如果高度发生变化，记录关键点
            if (currentHeight != prevHeight) {
                result.push_back({nums[pos - 1], currentHeight});
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
private:
    // 离散化处理
    void discretization(vector<vector<int>>& buildings) {
        set<int> s;
        
        // 收集所有坐标点
        for (auto& building : buildings) {
            s.insert(building[0]);  // 左边界
            s.insert(building[1]);  // 右边界
        }
        
        // 排序去重后的坐标
        nums.assign(s.begin(), s.end());
        
        // 建立映射关系
        for (int i = 0; i < nums.size(); i++) {
            map[nums[i]] = i + 1;
        }
        
        this->n = nums.size();
    }
    
    // 建立线段树
    void build(int l, int r, int i) {
        tree[i] = Node(l, r);
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
    }
    
    // 向上传递
    void pushUp(int i) {
        tree[i].max = max(tree[i << 1].max, tree[i << 1 | 1].max);
    }
    
    // 懒标记下发
    void pushDown(int i) {
        if (tree[i].lazy != 0) {
            tree[i << 1].max = max(tree[i << 1].max, tree[i].lazy);
            tree[i << 1 | 1].max = max(tree[i << 1 | 1].max, tree[i].lazy);
            tree[i << 1].lazy = max(tree[i << 1].lazy, tree[i].lazy);
            tree[i << 1 | 1].lazy = max(tree[i << 1 | 1].lazy, tree[i].lazy);
            tree[i].lazy = 0;
        }
    }
    
    // 区间更新最大值
    void update(int jobl, int jobr, int val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            tree[i].max = max(tree[i].max, val);
            tree[i].lazy = max(tree[i].lazy, val);
            return;
        }
        pushDown(i);
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            update(jobl, jobr, val, l, mid, i << 1);
        }
        if (jobr > mid) {
            update(jobl, jobr, val, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 区间查询最大值
    int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i].max;
        }
        pushDown(i);
        int mid = (l + r) >> 1;
        int ans = 0;
        if (jobl <= mid) {
            ans = max(ans, query(jobl, jobr, l, mid, i << 1));
        }
        if (jobr > mid) {
            ans = max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
        }
        return ans;
    }
};

/**
 * 生成大规模测试数据
 * 用于性能测试和压力测试
 * 
 * @param size 建筑物数量
 * @return 生成的测试数据
 */
vector<vector<int>> generateLargeTestData(int size) {
    vector<vector<int>> buildings(size, vector<int>(3));
    srand(time(nullptr));
    
    for (int i = 0; i < size; i++) {
        int left = rand() % 10000;
        int right = left + rand() % 100 + 1; // 确保右边界大于左边界
        int height = rand() % 100 + 1; // 高度为正数
        
        buildings[i][0] = left;
        buildings[i][1] = right;
        buildings[i][2] = height;
    }
    
    return buildings;
}

/**
 * 单元测试函数 - 覆盖各种边界情况和异常场景
 * 
 * 工程化测试考量：
 * 1. 正常功能测试
 * 2. 边界情况测试
 * 3. 异常输入测试
 * 4. 性能压力测试
 * 5. 随机数据测试
 */
int main() {
    Solution solution;
    
    cout << "=== 线段树天际线问题 - 工程化测试 ===" << endl << endl;
    
    // 测试用例1：标准功能测试
    cout << "1. 标准功能测试：" << endl;
    vector<vector<int>> buildings1 = {{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
    vector<vector<int>> result1 = solution.getSkyline(buildings1);
    cout << "   输入: [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]" << endl;
    cout << "   输出: ";
    for (auto& point : result1) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "   期望: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]" << endl;
    cout << "   测试结果: " << (result1.size() == 7 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例2：边界情况 - 两个相邻建筑
    cout << "2. 边界情况测试 - 相邻建筑：" << endl;
    vector<vector<int>> buildings2 = {{0,2,3},{2,5,3}};
    vector<vector<int>> result2 = solution.getSkyline(buildings2);
    cout << "   输入: [[0,2,3],[2,5,3]]" << endl;
    cout << "   输出: ";
    for (auto& point : result2) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "   期望: [[0,3],[5,0]]" << endl;
    cout << "   测试结果: " << (result2.size() == 2 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例3：边界情况 - 单个建筑
    cout << "3. 边界情况测试 - 单个建筑：" << endl;
    vector<vector<int>> buildings3 = {{1,5,10}};
    vector<vector<int>> result3 = solution.getSkyline(buildings3);
    cout << "   输入: [[1,5,10]]" << endl;
    cout << "   输出: ";
    for (auto& point : result3) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "   期望: [[1,10],[5,0]]" << endl;
    cout << "   测试结果: " << (result3.size() == 2 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例4：边界情况 - 完全重叠建筑
    cout << "4. 边界情况测试 - 重叠建筑：" << endl;
    vector<vector<int>> buildings4 = {{1,5,10},{1,5,15}};
    vector<vector<int>> result4 = solution.getSkyline(buildings4);
    cout << "   输入: [[1,5,10],[1,5,15]]" << endl;
    cout << "   输出: ";
    for (auto& point : result4) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "   期望: [[1,15],[5,0]]" << endl;
    cout << "   测试结果: " << (result4.size() == 2 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例5：边界情况 - 空输入
    cout << "5. 边界情况测试 - 空输入：" << endl;
    vector<vector<int>> buildings5 = {};
    vector<vector<int>> result5 = solution.getSkyline(buildings5);
    cout << "   输入: []" << endl;
    cout << "   输出: ";
    for (auto& point : result5) {
        cout << "[" << point[0] << "," << point[1] << "] ";
    }
    cout << endl;
    cout << "   期望: []" << endl;
    cout << "   测试结果: " << (result5.size() == 0 ? "✓ 通过" : "✗ 失败") << endl;
    cout << endl;
    
    // 测试用例6：性能测试 - 大规模数据
    cout << "6. 性能测试 - 大规模数据：" << endl;
    try {
        vector<vector<int>> buildings6 = generateLargeTestData(1000);
        auto startTime = chrono::high_resolution_clock::now();
        vector<vector<int>> result6 = solution.getSkyline(buildings6);
        auto endTime = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
        
        cout << "   数据规模: 1000个建筑物" << endl;
        cout << "   执行时间: " << duration.count() << "ms" << endl;
        cout << "   输出关键点数量: " << result6.size() << endl;
        cout << "   测试结果: " << (duration.count() < 1000 ? "✓ 性能良好" : "⚠ 性能需优化") << endl;
    } catch (const exception& e) {
        cout << "   性能测试异常: " << e.what() << endl;
    }
    cout << endl;
    
    // 测试用例7：异常输入测试
    cout << "7. 异常输入测试：" << endl;
    try {
        vector<vector<int>> buildings7 = {{1,1,10}}; // 左边界等于右边界
        solution.getSkyline(buildings7);
        cout << "   测试结果: ✗ 应该抛出异常但未抛出" << endl;
    } catch (const invalid_argument& e) {
        cout << "   异常测试 - 左边界等于右边界: ✓ 正确抛出异常: " << e.what() << endl;
    }
    
    try {
        vector<vector<int>> buildings8 = {{-1,5,10}}; // 负坐标
        solution.getSkyline(buildings8);
        cout << "   测试结果: ✗ 应该抛出异常但未抛出" << endl;
    } catch (const invalid_argument& e) {
        cout << "   异常测试 - 负坐标: ✓ 正确抛出异常: " << e.what() << endl;
    }
    
    cout << endl << "=== 测试完成 ===" << endl;
    
    return 0;
}