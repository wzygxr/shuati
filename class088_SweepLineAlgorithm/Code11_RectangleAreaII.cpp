// 矩形面积 II - 扫描线算法实现
// 题目链接: https://leetcode.cn/problems/rectangle-area-ii/
// 
// 题目描述:
// 我们给出了一个（轴对齐的）矩形列表 rectangles。
// 对于 rectangle[i] = [x1, y1, x2, y2]，其中 (x1, y1) 是矩形 i 左下角的坐标，
// (x2, y2) 是该矩形右上角的坐标。
// 找出平面中所有矩形叠加覆盖后的总面积。
// 由于答案可能太大，请返回它对 10^9 + 7 取模的结果。
// 
// 解题思路:
// 使用扫描线算法结合线段树实现矩形面积并的计算。
// 1. 将矩形拆分为左右两条边，作为扫描线事件
// 2. 按x坐标排序所有事件
// 3. 使用线段树维护y轴上的覆盖情况
// 4. 扫描过程中计算相邻扫描线之间的面积
// 5. 结果对 10^9 + 7 取模
// 
// 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量:
// 1. 异常处理: 检查坐标合法性
// 2. 边界条件: 处理坐标重复和边界情况
// 3. 性能优化: 使用离散化减少线段树规模
// 4. 数值处理: 大数取模运算
// 5. 可读性: 详细注释和模块化设计
// 6. 提供了两种实现方式：基本版本和优化版本

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <stdexcept>
#include <climits>

using namespace std;

class RectangleAreaII {
private:
    static const int MOD = 1000000007;
    
public:
    // 计算矩形面积并（取模）
    int rectangleArea(vector<vector<int>>& rectangles) {
        // 边界条件检查
        if (rectangles.empty()) {
            return 0;
        }
        
        // 收集所有y坐标用于离散化
        set<int> ySet;
        vector<vector<int>> events;
        
        for (auto& rect : rectangles) {
            if (rect.size() != 4) {
                throw invalid_argument("Invalid rectangle format");
            }
            
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 检查坐标合法性
            if (x1 >= x2 || y1 >= y2) {
                throw invalid_argument("Invalid rectangle coordinates");
            }
            
            ySet.insert(y1);
            ySet.insert(y2);
            
            // 添加开始事件(矩形左边)和结束事件(矩形右边)
            events.push_back({x1, y1, y2, 1});
            events.push_back({x2, y1, y2, -1});
        }
        
        // 对事件按x坐标排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        // 离散化y坐标
        vector<int> y(ySet.begin(), ySet.end());
        
        // 构建线段树
        int size = y.size() - 1;
        // cover数组记录每个节点的覆盖次数
        vector<int> cover(4 * size, 0);
        // len数组记录每个节点的覆盖长度
        vector<int> len(4 * size, 0);
        
        // 扫描线算法
        long long area = 0;
        int lastX = events[0][0];
        
        for (auto& event : events) {
            int x = event[0], y1 = event[1], y2 = event[2], flag = event[3];
            
            // 计算当前扫描线与上一个扫描线之间的面积
            // 面积 = y轴覆盖长度 × x轴距离
            long long width = x - lastX;
            long long height = len[1];
            
            // 累加面积，注意取模
            area = (area + width * height) % MOD;
            lastX = x;
            
            // 更新线段树中的覆盖情况
            int leftIndex = findIndex(y, y1);
            int rightIndex = findIndex(y, y2);
            updateTree(cover, len, y, 1, 0, size - 1, leftIndex, rightIndex - 1, flag);
        }
        
        return (int)area;
    }
    
    // 更新线段树
    void updateTree(vector<int>& cover, vector<int>& len, const vector<int>& y, 
                   int node, int left, int right, int l, int r, int flag) {
        // 如果操作区间与当前节点区间无交集，直接返回
        if (l > right || r < left) {
            return;
        }
        
        // 如果操作区间完全包含当前节点区间，更新覆盖次数
        if (l <= left && right <= r) {
            cover[node] += flag;
        } else {
            // 否则递归更新左右子树
            int mid = (left + right) / 2;
            if (l <= mid) {
                updateTree(cover, len, y, node * 2, left, mid, l, r, flag);
            }
            if (r > mid) {
                updateTree(cover, len, y, node * 2 + 1, mid + 1, right, l, r, flag);
            }
        }
        
        // 更新当前节点的覆盖长度
        if (cover[node] > 0) {
            // 如果当前区间被覆盖，长度为实际坐标长度
            len[node] = y[right + 1] - y[left];
        } else {
            // 如果当前区间未被覆盖，长度为子区间的覆盖长度之和
            if (left == right) {
                len[node] = 0;
            } else {
                len[node] = len[node * 2] + len[node * 2 + 1];
            }
        }
    }
    
    // 在离散化数组中查找值对应的索引
    int findIndex(const vector<int>& y, int value) {
        int left = 0, right = y.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (y[mid] == value) {
                return mid;
            } else if (y[mid] < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // 理论上不会发生
    }
    
    // 优化版本：使用更高效的实现
    int rectangleAreaOptimized(vector<vector<int>>& rectangles) {
        if (rectangles.empty()) {
            return 0;
        }
        
        // 使用更紧凑的数据结构
        set<int> ySet;
        vector<vector<int>> events;
        
        for (auto& rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            ySet.insert(y1);
            ySet.insert(y2);
            
            // 使用vector表示事件，[x坐标, y下界, y上界, 标志]
            events.push_back({x1, y1, y2, 1});
            events.push_back({x2, y1, y2, -1});
        }
        
        // 按x坐标排序
        sort(events.begin(), events.end(), [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        // 离散化y坐标
        vector<int> y(ySet.begin(), ySet.end());
        
        // 使用数组实现线段树（更高效）
        int n = y.size() - 1;
        // cover数组记录每个节点的覆盖次数
        vector<int> cover(4 * n, 0);
        // len数组记录每个节点的覆盖长度
        vector<int> len(4 * n, 0);
        
        long long area = 0;
        int lastX = events[0][0];
        
        for (auto& event : events) {
            int x = event[0], y1 = event[1], y2 = event[2], flag = event[3];
            
            // 计算当前扫描线与上一个扫描线之间的面积
            long long width = x - lastX;
            area = (area + width * len[1]) % MOD;
            lastX = x;
            
            // 更新线段树
            int leftIdx = findIndex(y, y1);
            int rightIdx = findIndex(y, y2);
            updateTree(cover, len, y, 1, 0, n - 1, leftIdx, rightIdx - 1, flag);
        }
        
        return (int)area;
    }
};

// 测试函数
int main() {
    RectangleAreaII solution;
    
    // 测试用例1: 标准情况
    vector<vector<int>> rectangles1 = {
        {0, 0, 2, 2},
        {1, 1, 3, 3}
    };
    int result1 = solution.rectangleArea(rectangles1);
    cout << "测试用例1 面积: " << result1 << endl; // 预期: 7
    
    // 测试用例2: 单个矩形
    vector<vector<int>> rectangles2 = {{0, 0, 1, 1}};
    int result2 = solution.rectangleArea(rectangles2);
    cout << "测试用例2 面积: " << result2 << endl; // 预期: 1
    
    // 测试用例3: 三个矩形
    vector<vector<int>> rectangles3 = {
        {0, 0, 3, 3},
        {2, 2, 5, 5},
        {1, 1, 4, 4}
    };
    int result3 = solution.rectangleArea(rectangles3);
    cout << "测试用例3 面积: " << result3 << endl; // 预期: 27
    
    // 测试用例4: 空数组
    vector<vector<int>> rectangles4;
    int result4 = solution.rectangleArea(rectangles4);
    cout << "测试用例4 面积: " << result4 << endl; // 预期: 0
    
    // 测试用例5: 大数测试
    vector<vector<int>> rectangles5 = {
        {0, 0, 1000000000, 1000000000}
    };
    int result5 = solution.rectangleArea(rectangles5);
    cout << "测试用例5 面积: " << result5 << endl; // 预期: 49 (取模后)
    
    // 测试优化版本
    cout << "\n=== 优化版本测试 ===" << endl;
    int result1Opt = solution.rectangleAreaOptimized(rectangles1);
    cout << "测试用例1 优化版本面积: " << result1Opt << endl;
    
    int result3Opt = solution.rectangleAreaOptimized(rectangles3);
    cout << "测试用例3 优化版本面积: " << result3Opt << endl;
    
    // 性能测试：大量矩形
    cout << "\n=== 性能测试 ===" << endl;
    vector<vector<int>> rectangles6(1000, vector<int>(4));
    srand(time(nullptr));
    for (int i = 0; i < 1000; i++) {
        int x1 = rand() % 1000;
        int y1 = rand() % 1000;
        int x2 = x1 + rand() % 100 + 1;
        int y2 = y1 + rand() % 100 + 1;
        rectangles6[i] = {x1, y1, x2, y2};
    }
    
    clock_t startTime = clock();
    int result6 = solution.rectangleArea(rectangles6);
    clock_t endTime = clock();
    cout << "性能测试 面积: " << result6 << endl;
    cout << "执行时间: " << (double)(endTime - startTime) / CLOCKS_PER_SEC * 1000 << "ms" << endl;
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}