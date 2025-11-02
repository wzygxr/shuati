#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <cmath>
#include <stdexcept>

using namespace std;

/**
 * 矩形面积并 (POJ 1151, HDU 1542)
 * 题目链接: http://poj.org/problem?id=1151
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1542
 * 
 * 题目描述:
 * 给定多个矩形区域的地图，计算这些地图覆盖的总面积。
 * 每个矩形由其左下角坐标(x1, y1)和右上角坐标(x2, y2)表示。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现矩形面积并的计算。
 * 1. 将矩形拆分为上下两条边，作为扫描线事件
 * 2. 按y坐标排序所有事件
 * 3. 使用线段树维护x轴上的覆盖情况
 * 4. 扫描过程中计算相邻扫描线之间的面积
 * 
 * 时间复杂度: O(n log n) - 排序和线段树操作
 * 空间复杂度: O(n) - 存储事件和线段树
 * 
 * 工程化考量:
 * 1. 异常处理: 检查坐标合法性
 * 2. 边界条件: 处理坐标重复和边界情况
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 可读性: 详细注释和模块化设计
 */

class RectangleAreaUnion {
private:
    // 线段树节点结构
    struct SegmentTreeNode {
        int left, right; // 区间左右边界
        int cover; // 当前区间被覆盖的次数
        double len; // 当前区间被覆盖的长度
        
        SegmentTreeNode(int l = 0, int r = 0) : left(l), right(r), cover(0), len(0) {}
    };
    
    // 扫描线事件结构
    struct Event {
        double x; // x坐标
        double y1, y2; // y坐标区间
        int flag; // 1表示矩形开始，-1表示矩形结束
        
        Event(double x_val, double y1_val, double y2_val, int f) 
            : x(x_val), y1(y1_val), y2(y2_val), flag(f) {}
        
        bool operator<(const Event& other) const {
            return x < other.x;
        }
    };
    
    vector<SegmentTreeNode> tree; // 线段树数组
    vector<double> y; // y坐标离散化数组
    
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
    
    // 更新线段树
    void updateTree(int node, int left, int right, int flag) {
        if (left > tree[node].right || right < tree[node].left) {
            return;
        }
        
        if (left <= tree[node].left && tree[node].right <= right) {
            tree[node].cover += flag;
        } else {
            int mid = (tree[node].left + tree[node].right) / 2;
            if (left <= mid) {
                updateTree(node * 2, left, right, flag);
            }
            if (right > mid) {
                updateTree(node * 2 + 1, left, right, flag);
            }
        }
        
        // 更新当前节点的覆盖长度
        if (tree[node].cover > 0) {
            tree[node].len = y[tree[node].right + 1] - y[tree[node].left];
        } else {
            if (tree[node].left == tree[node].right) {
                tree[node].len = 0;
            } else {
                tree[node].len = tree[node * 2].len + tree[node * 2 + 1].len;
            }
        }
    }
    
    // 在离散化数组中查找索引
    int findIndex(double value) {
        int left = 0, right = y.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (fabs(y[mid] - value) < 1e-9) {
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
    /**
     * 计算矩形面积并
     * @param rectangles 矩形数组，每个矩形为{x1, y1, x2, y2}
     * @return 总面积
     */
    double calculateArea(vector<vector<double>>& rectangles) {
        // 边界条件检查
        if (rectangles.empty()) {
            return 0.0;
        }
        
        int n = rectangles.size();
        
        // 收集所有y坐标用于离散化
        set<double> ySet;
        vector<Event> events;
        
        for (auto& rect : rectangles) {
            if (rect.size() != 4) {
                throw invalid_argument("Invalid rectangle format");
            }
            
            double x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 检查坐标合法性
            if (x1 >= x2 || y1 >= y2) {
                throw invalid_argument("Invalid rectangle coordinates");
            }
            
            ySet.insert(y1);
            ySet.insert(y2);
            
            // 添加开始事件和结束事件
            events.push_back(Event(x1, y1, y2, 1));
            events.push_back(Event(x2, y1, y2, -1));
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
        double area = 0.0;
        double lastX = events[0].x;
        
        for (auto& event : events) {
            // 计算当前扫描线与上一个扫描线之间的面积
            double currentX = event.x;
            area += tree[1].len * (currentX - lastX);
            lastX = currentX;
            
            // 更新线段树
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex - 1, event.flag);
        }
        
        return area;
    }
};

// 测试函数
int main() {
    RectangleAreaUnion solution;
    
    // 测试用例1: 两个不重叠的矩形
    vector<vector<double>> rectangles1 = {
        {0, 0, 1, 1},
        {2, 2, 3, 3}
    };
    double area1 = solution.calculateArea(rectangles1);
    cout << "测试用例1 面积: " << area1 << endl; // 预期: 2.0
    
    // 测试用例2: 两个重叠的矩形
    vector<vector<double>> rectangles2 = {
        {0, 0, 2, 2},
        {1, 1, 3, 3}
    };
    double area2 = solution.calculateArea(rectangles2);
    cout << "测试用例2 面积: " << area2 << endl; // 预期: 7.0
    
    // 测试用例3: 三个矩形，部分重叠
    vector<vector<double>> rectangles3 = {
        {0, 0, 2, 2},
        {1, 1, 3, 3},
        {0.5, 0.5, 1.5, 1.5}
    };
    double area3 = solution.calculateArea(rectangles3);
    cout << "测试用例3 面积: " << area3 << endl; // 预期: 8.75
    
    // 测试用例4: 空矩形数组
    vector<vector<double>> rectangles4;
    double area4 = solution.calculateArea(rectangles4);
    cout << "测试用例4 面积: " << area4 << endl; // 预期: 0.0
    
    // 测试用例5: 单个矩形
    vector<vector<double>> rectangles5 = {{0, 0, 5, 5}};
    double area5 = solution.calculateArea(rectangles5);
    cout << "测试用例5 面积: " << area5 << endl; // 预期: 25.0
    
    return 0;
}