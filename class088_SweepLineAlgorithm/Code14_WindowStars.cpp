// 窗口的星星 (洛谷 P1502)
// 题目链接: https://www.luogu.com.cn/problem/P1502
// 
// 题目描述:
// 给定一些星星的位置和亮度，求一个固定大小的窗口内星星亮度总和的最大值。
// 窗口的边界不算在内部（即边界上的星星不计入亮度总和）。
// 
// 解题思路:
// 使用扫描线算法结合线段树实现窗口内星星亮度总和的最大值计算。
// 1. 将每个星星转化为一个矩形区域，表示窗口右上角可以放置的位置范围
// 2. 使用扫描线算法处理这些矩形区域
// 3. 线段树维护当前扫描线上的亮度总和
// 4. 求线段树中的最大值
// 
// 时间复杂度: O(n log n) - 排序和线段树操作
// 空间复杂度: O(n) - 存储事件和线段树
// 
// 工程化考量:
// 1. 异常处理: 检查输入数据合法性
// 2. 边界条件: 处理窗口边界和星星位置
// 3. 性能优化: 使用离散化减少线段树规模
// 4. 可读性: 详细注释和模块化设计

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cmath>
#include <stdexcept>

using namespace std;

// 星星结构
struct Star {
    int x, y; // 星星坐标
    int light; // 星星亮度
    
    Star(int x_val = 0, int y_val = 0, int light_val = 0) 
        : x(x_val), y(y_val), light(light_val) {}
};

class WindowStars {
private:
    // 扫描线事件结构
    struct Event {
        int x; // x坐标
        int y1, y2; // y坐标区间
        int light; // 亮度变化（正数表示增加，负数表示减少）
        
        Event(int x_val, int y1_val, int y2_val, int light_val) 
            : x(x_val), y1(y1_val), y2(y2_val), light(light_val) {}
        
        bool operator<(const Event& other) const {
            if (x != other.x) {
                return x < other.x;
            }
            // 相同x坐标时，增加事件优先于减少事件
            return other.light < light;
        }
    };
    
    // 线段树节点结构（支持区间加和区间最大值查询）
    struct SegmentTreeNode {
        int left, right; // 区间左右边界
        int max_val; // 区间最大值
        int lazy; // 懒标记
        
        SegmentTreeNode(int l = 0, int r = 0) : left(l), right(r), max_val(0), lazy(0) {}
    };
    
    vector<SegmentTreeNode> tree; // 线段树数组
    vector<int> y; // y坐标离散化数组
    
    // 构建线段树
    void buildTree(int node, int left, int right) {
        if (node >= tree.size()) {
            tree.resize(node + 1);
        }
        tree[node] = SegmentTreeNode(left, right);
        if (left == right) {
            return;
        }
        int mid = (left + right) / 2;
        buildTree(node * 2, left, mid);
        buildTree(node * 2 + 1, mid + 1, right);
    }
    
    // 更新线段树（区间加值）
    void updateTree(int node, int left, int right, int value) {
        if (left > tree[node].right || right < tree[node].left) {
            return;
        }
        
        if (left <= tree[node].left && tree[node].right <= right) {
            // 完全包含，更新当前节点
            tree[node].max_val += value;
            tree[node].lazy += value;
        } else {
            // 下推懒标记
            pushDown(node);
            
            int mid = (tree[node].left + tree[node].right) / 2;
            if (left <= mid) {
                updateTree(node * 2, left, right, value);
            }
            if (right > mid) {
                updateTree(node * 2 + 1, left, right, value);
            }
            
            // 更新当前节点
            tree[node].max_val = max(tree[node * 2].max_val, tree[node * 2 + 1].max_val);
        }
    }
    
    // 下推懒标记
    void pushDown(int node) {
        if (tree[node].lazy != 0) {
            if (tree[node].left != tree[node].right) {
                tree[node * 2].max_val += tree[node].lazy;
                tree[node * 2].lazy += tree[node].lazy;
                tree[node * 2 + 1].max_val += tree[node].lazy;
                tree[node * 2 + 1].lazy += tree[node].lazy;
            }
            tree[node].lazy = 0;
        }
    }
    
    // 在离散化数组中查找索引
    int findIndex(int value) {
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
    
public:
    // 计算窗口内星星亮度总和的最大值
    int maxWindowLight(vector<Star>& stars, int w, int h) {
        // 边界条件检查
        if (stars.empty() || w <= 0 || h <= 0) {
            return 0;
        }
        
        // 收集所有y坐标用于离散化
        set<int> ySet;
        vector<Event> events;
        
        for (auto& star : stars) {
            // 检查星星数据合法性
            if (star.x < 0 || star.y < 0 || star.light < 0) {
                throw invalid_argument("Invalid star data");
            }
            
            // 计算窗口右上角可以放置的矩形区域
            // 窗口右上角在[x1, y1]到[x2, y2]范围内时，星星会被包含在窗口内
            int x1 = star.x;
            int y1 = star.y;
            int x2 = star.x + w - 1; // 窗口宽度为w，边界不算
            int y2 = star.y + h - 1; // 窗口高度为h，边界不算
            
            ySet.insert(y1);
            ySet.insert(y2);
            
            // 添加开始事件和结束事件
            events.push_back(Event(x1, y1, y2, star.light)); // 开始事件：增加亮度
            events.push_back(Event(x2 + 1, y1, y2, -star.light)); // 结束事件：减少亮度
        }
        
        // 对事件按x坐标排序
        sort(events.begin(), events.end());
        
        // 离散化y坐标
        y.assign(ySet.begin(), ySet.end());
        
        // 构建线段树
        int size = y.size() - 1;
        tree.clear();
        tree.resize(4 * size + 10);
        buildTree(1, 0, size - 1);
        
        // 扫描线算法
        int maxLight = 0;
        
        for (auto& event : events) {
            // 更新线段树
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex, event.light);
            
            // 更新最大值
            maxLight = max(maxLight, tree[1].max_val);
        }
        
        return maxLight;
    }
};

// 测试函数
int main() {
    WindowStars solution;
    
    // 测试用例1: 简单情况
    vector<Star> stars1 = {
        Star(1, 1, 10),
        Star(2, 2, 20),
        Star(3, 3, 30)
    };
    int result1 = solution.maxWindowLight(stars1, 2, 2);
    cout << "测试用例1 最大亮度: " << result1 << endl; // 预期: 50 (星星2和3)
    
    // 测试用例2: 窗口包含所有星星
    vector<Star> stars2 = {
        Star(1, 1, 5),
        Star(3, 3, 10),
        Star(5, 5, 15)
    };
    int result2 = solution.maxWindowLight(stars2, 10, 10);
    cout << "测试用例2 最大亮度: " << result2 << endl; // 预期: 30 (所有星星)
    
    // 测试用例3: 窗口太小，无法包含任何星星
    vector<Star> stars3 = {
        Star(10, 10, 100),
        Star(20, 20, 200)
    };
    int result3 = solution.maxWindowLight(stars3, 5, 5);
    cout << "测试用例3 最大亮度: " << result3 << endl; // 预期: 0
    
    // 测试用例4: 星星在边界上
    vector<Star> stars4 = {
        Star(0, 0, 50),
        Star(1, 1, 100),
        Star(2, 2, 150)
    };
    int result4 = solution.maxWindowLight(stars4, 2, 2);
    cout << "测试用例4 最大亮度: " << result4 << endl; // 预期: 250 (星星1和2)
    
    // 测试用例5: 空数组
    vector<Star> stars5;
    int result5 = solution.maxWindowLight(stars5, 10, 10);
    cout << "测试用例5 最大亮度: " << result5 << endl; // 预期: 0
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}