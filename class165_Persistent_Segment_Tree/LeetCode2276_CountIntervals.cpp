#include <iostream>
using namespace std;

/**
 * LeetCode 2276. Count Integers in Intervals
 * 
 * 题目描述:
 * 设计一个区间集合，支持以下操作：
 * 1. add(left, right): 添加区间[left, right]到集合中
 * 2. count(): 返回出现在至少一个区间中的整数个数
 * 
 * 解题思路:
 * 使用动态开点线段树解决区间覆盖问题。
 * 1. 使用线段树维护区间覆盖情况
 * 2. 通过懒标记优化区间更新操作
 * 3. 维护区间内被覆盖的整数个数
 * 
 * 时间复杂度: O(n log C), 其中C是值域范围
 * 空间复杂度: O(n log C)
 * 
 * 示例:
 * 输入:
 * ["CountIntervals", "add", "add", "count", "add", "count"]
 * [[], [2, 3], [7, 10], [], [5, 8], []]
 * 
 * 输出:
 * [null, null, null, 6, null, 8]
 * 
 * 解释:
 * CountIntervals countIntervals = new CountIntervals(); // 用一个区间空集初始化对象
 * countIntervals.add(2, 3);  // 将 [2, 3] 添加到区间集合中
 * countIntervals.add(7, 10); // 将 [7, 10] 添加到区间集合中
 * countIntervals.count();    // 返回 6
 * // 整数 2 和 3 出现在区间 [2, 3] 中
 * // 整数 7、8、9、10 出现在区间 [7, 10] 中
 * countIntervals.add(5, 8);  // 将 [5, 8] 添加到区间集合中
 * countIntervals.count();    // 返回 8
 * // 整数 2 和 3 出现在区间 [2, 3] 中
 * // 整数 5 和 6 出现在区间 [5, 8] 中
 * // 整数 7 和 8 出现在区间 [5, 8] 和区间 [7, 10] 的并集内
 * // 整数 9 和 10 出现在区间 [7, 10] 中
 */
class CountIntervals {
private:
    // 线段树节点
    struct Node {
        int val = 0;      // 区间内被覆盖的整数个数
        int lazy = 0;     // 懒标记，0表示无标记，1表示全覆盖
        Node* left = nullptr;    // 左子节点
        Node* right = nullptr;   // 右子节点
    };
    
    Node* root;  // 线段树根节点
    static const int MAX_RANGE = 1000000000;  // 值域范围
    
    /**
     * 下传懒标记
     * @param node 当前节点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     */
    void pushDown(Node* node, int l, int r) {
        // 如果当前节点没有左右子节点，则创建
        if (node->left == nullptr) {
            node->left = new Node();
        }
        if (node->right == nullptr) {
            node->right = new Node();
        }
        
        // 如果有懒标记
        if (node->lazy > 0) {
            // 将懒标记传递给左右子节点
            node->left->lazy = node->lazy;
            node->right->lazy = node->lazy;
            
            // 计算左右子节点区间长度
            int mid = l + (r - l) / 2;
            
            // 更新左右子节点的值
            node->left->val = mid - l + 1;
            node->right->val = r - mid;
            
            // 清除当前节点的懒标记
            node->lazy = 0;
        }
    }
    
    /**
     * 更新节点值
     * @param node 当前节点
     */
    void pushUp(Node* node) {
        node->val = node->left->val + node->right->val;
    }
    
    /**
     * 线段树区间更新操作
     * @param node 当前节点
     * @param l 当前节点表示的区间左端点
     * @param r 当前节点表示的区间右端点
     * @param ql 查询区间左端点
     * @param qr 查询区间右端点
     */
    void update(Node* node, int l, int r, int ql, int qr) {
        // 如果当前区间被查询区间完全包含
        if (ql <= l && r <= qr) {
            // 设置懒标记为全覆盖
            node->lazy = 1;
            // 更新节点值为区间长度
            node->val = r - l + 1;
            return;
        }
        
        // 动态开点
        pushDown(node, l, r);
        
        int mid = l + (r - l) / 2;
        // 如果查询区间与左子树有交集
        if (ql <= mid) {
            update(node->left, l, mid, ql, qr);
        }
        // 如果查询区间与右子树有交集
        if (qr > mid) {
            update(node->right, mid + 1, r, ql, qr);
        }
        
        // 更新当前节点的值
        pushUp(node);
    }
    
public:
    CountIntervals() {
        root = new Node();
    }
    
    /**
     * 添加区间[left, right]到集合中
     * @param left 区间左端点
     * @param right 区间右端点
     */
    void add(int left, int right) {
        update(root, 1, MAX_RANGE, left, right);
    }
    
    /**
     * 返回出现在至少一个区间中的整数个数
     * @return 整数个数
     */
    int count() {
        return root->val;
    }
};

/**
 * Your CountIntervals object will be instantiated and called as such:
 * CountIntervals* obj = new CountIntervals();
 * obj->add(left,right);
 * int param_2 = obj->count();
 */