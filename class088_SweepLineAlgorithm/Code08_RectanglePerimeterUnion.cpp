#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <map>
using namespace std;

/**
 * 矩形周长并 (POJ 1177, HDU 1828)
 * 题目链接: POJ 1177: http://poj.org/problem?id=1177
 * 题目链接: HDU 1828: http://acm.hdu.edu.cn/showproblem.php?pid=1828
 * 
 * 解题思路:
 * 使用扫描线算法，将矩形边界作为事件点，从左到右扫描
 * 维护当前扫描线覆盖的垂直区间，计算水平周长和垂直周长
 * 
 * 时间复杂度: O(n log n) - 排序和线段树操作
 * 空间复杂度: O(n) - 存储事件点和线段树
 */

class RectanglePerimeterUnion {
private:
    struct Event {
        int x;           // 事件点的x坐标
        int y1, y2;      // 垂直区间的上下边界
        int type;        // 事件类型: 1表示进入，-1表示离开
        Event(int x, int y1, int y2, int type) : x(x), y1(y1), y2(y2), type(type) {}
        
        bool operator<(const Event& other) const {
            if (x != other.x) return x < other.x;
            return type > other.type; // 进入事件优先处理
        }
    };
    
    struct SegmentTreeNode {
        int cover;       // 当前区间被覆盖的次数
        int length;      // 当前区间被覆盖的长度
        int segCount;    // 当前区间内连续区间的数量
        int leftCover;   // 左端点是否被覆盖
        int rightCover;  // 右端点是否被覆盖
        
        SegmentTreeNode() : cover(0), length(0), segCount(0), leftCover(0), rightCover(0) {}
    };
    
    vector<Event> events;
    vector<int> yCoords;
    vector<SegmentTreeNode> segTree;
    
    void build(int idx, int l, int r) {
        if (l == r) {
            segTree[idx] = SegmentTreeNode();
            return;
        }
        int mid = (l + r) / 2;
        build(idx * 2, l, mid);
        build(idx * 2 + 1, mid + 1, r);
    }
    
    void pushUp(int idx, int l, int r) {
        if (segTree[idx].cover > 0) {
            segTree[idx].length = yCoords[r + 1] - yCoords[l];
            segTree[idx].segCount = 1;
            segTree[idx].leftCover = segTree[idx].rightCover = 1;
        } else {
            if (l == r) {
                segTree[idx].length = 0;
                segTree[idx].segCount = 0;
                segTree[idx].leftCover = segTree[idx].rightCover = 0;
            } else {
                segTree[idx].length = segTree[idx * 2].length + segTree[idx * 2 + 1].length;
                segTree[idx].segCount = segTree[idx * 2].segCount + segTree[idx * 2 + 1].segCount;
                if (segTree[idx * 2].rightCover && segTree[idx * 2 + 1].leftCover) {
                    segTree[idx].segCount--;
                }
                segTree[idx].leftCover = segTree[idx * 2].leftCover;
                segTree[idx].rightCover = segTree[idx * 2 + 1].rightCover;
            }
        }
    }
    
    void update(int idx, int l, int r, int ql, int qr, int val) {
        if (ql <= l && r <= qr) {
            segTree[idx].cover += val;
            pushUp(idx, l, r);
            return;
        }
        int mid = (l + r) / 2;
        if (ql <= mid) update(idx * 2, l, mid, ql, qr, val);
        if (qr > mid) update(idx * 2 + 1, mid + 1, r, ql, qr, val);
        pushUp(idx, l, r);
    }
    
public:
    int calculatePerimeter(vector<vector<int>>& rectangles) {
        if (rectangles.empty()) return 0;
        
        // 收集所有y坐标并去重排序
        set<int> ySet;
        for (auto& rect : rectangles) {
            ySet.insert(rect[1]);
            ySet.insert(rect[3]);
        }
        yCoords.assign(ySet.begin(), ySet.end());
        
        // 创建y坐标到索引的映射
        map<int, int> yIndex;
        for (int i = 0; i < yCoords.size(); i++) {
            yIndex[yCoords[i]] = i;
        }
        
        // 创建事件
        for (auto& rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            events.emplace_back(x1, y1, y2, 1);
            events.emplace_back(x2, y1, y2, -1);
        }
        
        // 按x坐标排序事件
        sort(events.begin(), events.end());
        
        // 初始化线段树
        int n = yCoords.size() - 1;
        segTree.resize(4 * n);
        build(1, 0, n - 1);
        
        int perimeter = 0;
        int lastLength = 0;
        int lastSegCount = 0;
        
        for (int i = 0; i < events.size(); i++) {
            Event& e = events[i];
            int y1Idx = yIndex[e.y1];
            int y2Idx = yIndex[e.y2] - 1;
            
            if (y1Idx <= y2Idx) {
                update(1, 0, n - 1, y1Idx, y2Idx, e.type);
            }
            
            // 计算水平周长
            if (i > 0) {
                perimeter += 2 * lastSegCount * (e.x - events[i - 1].x);
            }
            
            // 计算垂直周长
            perimeter += abs(segTree[1].length - lastLength);
            
            lastLength = segTree[1].length;
            lastSegCount = segTree[1].segCount;
        }
        
        return perimeter;
    }
};

int main() {
    RectanglePerimeterUnion solver;
    
    // 测试用例1: 单个矩形
    vector<vector<int>> test1 = {{0, 0, 10, 10}};
    cout << "单个矩形周长: " << solver.calculatePerimeter(test1) << endl; // 期望: 40
    
    // 测试用例2: 两个相邻矩形
    vector<vector<int>> test2 = {{0, 0, 10, 10}, {10, 0, 20, 10}};
    cout << "两个相邻矩形周长: " << solver.calculatePerimeter(test2) << endl; // 期望: 60
    
    // 测试用例3: 两个重叠矩形
    vector<vector<int>> test3 = {{0, 0, 10, 10}, {5, 5, 15, 15}};
    cout << "两个重叠矩形周长: " << solver.calculatePerimeter(test3) << endl; // 期望: 60
    
    return 0;
}