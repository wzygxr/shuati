// LeetCode 1522. N叉树的直径
// 题目：给定一棵N叉树，你需要计算它的直径长度。
// 一棵N叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 两结点之间的路径长度是以它们之间边的数目表示。
// 来源：LeetCode
// 链接：https://leetcode.cn/problems/diameter-of-n-ary-tree/

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <stack>
#include <unordered_map>

using namespace std;

// N叉树节点定义
class Node {
public:
    int val;
    vector<Node*> children;
    
    Node() {}
    
    Node(int _val) {
        val = _val;
    }
    
    Node(int _val, vector<Node*> _children) {
        val = _val;
        children = _children;
    }
};

class LeetCode1522_DiameterOfNAryTree {
public:
    /**
     * 计算N叉树的直径
     * @param root N叉树根节点
     * @return 树的直径（边数）
     * 
     * 时间复杂度：O(n)，其中n是N叉树的节点数，每个节点只访问一次
     * 空间复杂度：O(h)，其中h是N叉树的高度，递归调用栈的深度
     */
    int diameter(Node* root) {
        maxDiameter = 0;  // 重置全局变量
        depth(root);     // 计算每个节点的深度并更新最大直径
        return maxDiameter;
    }
    
    /**
     * 使用优先队列优化的深度计算方法
     * @param root N叉树根节点
     * @return 树的直径
     */
    int diameterOptimized(Node* root) {
        maxDiameter = 0;
        depthOptimized(root);
        return maxDiameter;
    }
    
    /**
     * 迭代实现（避免递归深度过大）
     * @param root N叉树根节点
     * @return 树的直径
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int diameterIterative(Node* root) {
        if (root == nullptr) {
            return 0;
        }
        
        // 使用后序遍历计算每个节点的深度
        unordered_map<Node*, int> depthMap;
        stack<Node*> stk;
        stk.push(root);
        
        int maxDiameter = 0;
        
        while (!stk.empty()) {
            Node* node = stk.top();
            
            // 检查是否所有子节点都已经处理过
            bool allChildrenProcessed = true;
            
            for (Node* child : node->children) {
                if (depthMap.find(child) == depthMap.end()) {
                    stk.push(child);
                    allChildrenProcessed = false;
                    break;
                }
            }
            
            if (allChildrenProcessed) {
                stk.pop();
                
                // 计算当前节点的深度
                vector<int> childDepths;
                for (Node* child : node->children) {
                    childDepths.push_back(depthMap[child]);
                }
                
                // 排序找到最大的两个深度
                sort(childDepths.rbegin(), childDepths.rend());
                
                int max1 = childDepths.size() >= 1 ? childDepths[0] : 0;
                int max2 = childDepths.size() >= 2 ? childDepths[1] : 0;
                
                // 更新最大直径
                maxDiameter = max(maxDiameter, max1 + max2);
                
                // 当前节点的深度 = 最大子节点深度 + 1
                depthMap[node] = max1 + 1;
            }
        }
        
        return maxDiameter;
    }
    
private:
    int maxDiameter; // 全局变量，用于记录最大直径
    
    /**
     * 计算以当前节点为根的子树深度，并更新最大直径
     * @param node 当前节点
     * @return 当前节点为根的子树的最大深度
     */
    int depth(Node* node) {
        // 基本情况：空节点深度为0
        if (node == nullptr) {
            return 0;
        }
        
        // 特殊情况：没有子节点，深度为0
        if (node->children.empty()) {
            return 0;
        }
        
        // 存储所有子节点的深度
        vector<int> depths;
        
        // 递归计算所有子节点的最大深度
        for (Node* child : node->children) {
            int childDepth = depth(child);
            depths.push_back(childDepth);
        }
        
        // 对深度进行排序，找到最大的两个深度
        sort(depths.rbegin(), depths.rend());
        
        // 计算经过当前节点的最长路径
        int pathThroughNode = 0;
        if (depths.size() >= 1) {
            pathThroughNode += depths[0];
        }
        if (depths.size() >= 2) {
            pathThroughNode += depths[1];
        }
        
        // 更新全局最大直径
        maxDiameter = max(maxDiameter, pathThroughNode);
        
        // 返回以当前节点为根的子树的最大深度
        return depths.empty() ? 0 : depths[0] + 1;
    }
    
    /**
     * 使用优先队列优化的深度计算方法
     * @param node 当前节点
     * @return 当前节点为根的子树的最大深度
     */
    int depthOptimized(Node* node) {
        if (node == nullptr) {
            return 0;
        }
        
        if (node->children.empty()) {
            return 0;
        }
        
        // 使用优先队列（最大堆）来维护最大的两个深度
        priority_queue<int> maxHeap;
        
        for (Node* child : node->children) {
            int childDepth = depthOptimized(child);
            maxHeap.push(childDepth);
        }
        
        // 取出最大的两个深度
        int max1 = maxHeap.empty() ? 0 : maxHeap.top();
        if (!maxHeap.empty()) maxHeap.pop();
        int max2 = maxHeap.empty() ? 0 : maxHeap.top();
        
        // 更新最大直径
        maxDiameter = max(maxDiameter, max1 + max2);
        
        // 返回最大深度
        return max1 + 1;
    }
    
    // 测试方法
    void test() {
        LeetCode1522_DiameterOfNAryTree solution;
        
        // 测试用例1: 简单的三叉树
        //     1
        //   / | \
        //  2  3  4
        // 预期输出：2（路径 2-1-3 或 2-1-4 或 3-1-4）
        Node* root1 = new Node(1);
        root1->children.push_back(new Node(2));
        root1->children.push_back(new Node(3));
        root1->children.push_back(new Node(4));
        
        cout << "测试用例1结果: " << solution.diameter(root1) << endl; // 应该输出2
        cout << "测试用例1(优化)结果: " << solution.diameterOptimized(root1) << endl; // 应该输出2
        cout << "测试用例1(迭代)结果: " << solution.diameterIterative(root1) << endl; // 应该输出2
        
        // 测试用例2: 更复杂的N叉树
        //       1
        //     / | \
        //    2  3  4
        //   /|     |
        //  5 6     7
        // 预期输出：4（路径 5-2-1-4-7）
        Node* root2 = new Node(1);
        Node* node2 = new Node(2);
        Node* node3 = new Node(3);
        Node* node4 = new Node(4);
        
        root2->children.push_back(node2);
        root2->children.push_back(node3);
        root2->children.push_back(node4);
        
        node2->children.push_back(new Node(5));
        node2->children.push_back(new Node(6));
        node4->children.push_back(new Node(7));
        
        cout << "测试用例2结果: " << solution.diameter(root2) << endl; // 应该输出4
        cout << "测试用例2(优化)结果: " << solution.diameterOptimized(root2) << endl; // 应该输出4
        cout << "测试用例2(迭代)结果: " << solution.diameterIterative(root2) << endl; // 应该输出4
        
        // 测试用例3: 单节点树
        Node* root3 = new Node(1);
        cout << "测试用例3结果: " << solution.diameter(root3) << endl; // 应该输出0
        cout << "测试用例3(优化)结果: " << solution.diameterOptimized(root3) << endl; // 应该输出0
        cout << "测试用例3(迭代)结果: " << solution.diameterIterative(root3) << endl; // 应该输出0
        
        // 测试用例4: 空树
        cout << "测试用例4结果: " << solution.diameter(nullptr) << endl; // 应该输出0
        cout << "测试用例4(优化)结果: " << solution.diameterOptimized(nullptr) << endl; // 应该输出0
        cout << "测试用例4(迭代)结果: " << solution.diameterIterative(nullptr) << endl; // 应该输出0
        
        // 清理内存
        delete root1;
        delete root2;
        delete root3;
    }
};

int main() {
    LeetCode1522_DiameterOfNAryTree solution;
    solution.test();
    return 0;
}