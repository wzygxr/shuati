// LeetCode 107. 二叉树的层序遍历 II
// 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
// 题目大意: 给你二叉树的根节点 root ，返回其节点值 自底向上的层序遍历 。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）

#include <iostream>
#include <vector>
#include <queue>
#include <stack>
#include <algorithm>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    /**
     * 方法1: 先正常层序遍历，再反转结果
     * 思路:
     * 1. 使用队列进行正常的层序遍历，从上到下收集每层节点值
     * 2. 遍历完成后，将结果列表反转，得到自底向上的遍历结果
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次，反转操作是O(L)，L为层数
     * 空间复杂度: O(n) - 存储队列和结果
     */
    vector<vector<int>> levelOrderBottom1(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            
            while (!q.empty()) {
                int size = q.size();
                vector<int> level;
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode* cur = q.front();
                    q.pop();
                    level.push_back(cur->val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                
                // 将当前层的结果添加到最终答案中
                ans.push_back(level);
            }
            
            // 反转结果，得到自底向上的遍历
            reverse(ans.begin(), ans.end());
        }
        return ans;
    }
    
    /**
     * 方法2: 使用栈存储中间结果
     * 思路:
     * 1. 使用队列进行正常的层序遍历
     * 2. 使用栈存储每层的结果
     * 3. 遍历完成后，从栈中弹出结果，得到自底向上的遍历结果
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列、栈和结果
     */
    vector<vector<int>> levelOrderBottom2(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            stack<vector<int>> stk;
            q.push(root);
            
            while (!q.empty()) {
                int size = q.size();
                vector<int> level;
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode* cur = q.front();
                    q.pop();
                    level.push_back(cur->val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                
                // 将当前层的结果压入栈中
                stk.push(level);
            }
            
            // 从栈中弹出结果，得到自底向上的遍历
            while (!stk.empty()) {
                ans.push_back(stk.top());
                stk.pop();
            }
        }
        return ans;
    }
    
    /**
     * 方法3: 在遍历过程中直接在列表开头插入
     * 思路:
     * 1. 使用队列进行正常的层序遍历
     * 2. 每层遍历完成后，将结果插入到结果列表的开头
     * 3. 这样最终结果就是自底向上的遍历
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列和结果
     * 注意: 在列表开头插入元素的时间复杂度是O(L)，L为当前列表长度，总体时间复杂度仍为O(n)
     */
    vector<vector<int>> levelOrderBottom3(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            
            while (!q.empty()) {
                int size = q.size();
                vector<int> level;
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode* cur = q.front();
                    q.pop();
                    level.push_back(cur->val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                
                // 将当前层的结果插入到结果列表的开头
                ans.insert(ans.begin(), level);
            }
        }
        return ans;
    }
};

// 辅助函数：打印二维向量
void printVector(const vector<vector<int>>& vec) {
    for (const auto& v : vec) {
        cout << "[";
        for (size_t i = 0; i < v.size(); ++i) {
            cout << v[i];
            if (i < v.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
}

// 测试代码
int main() {
    // 测试用例1: [3,9,20,null,null,15,7]
    TreeNode* root1 = new TreeNode(3);
    root1->left = new TreeNode(9);
    root1->right = new TreeNode(20);
    root1->right->left = new TreeNode(15);
    root1->right->right = new TreeNode(7);
    
    Solution solution;
    cout << "方法1结果:" << endl;
    printVector(solution.levelOrderBottom1(root1));
    
    cout << "方法2结果:" << endl;
    printVector(solution.levelOrderBottom2(root1));
    
    cout << "方法3结果:" << endl;
    printVector(solution.levelOrderBottom3(root1));
    
    // 测试用例2: [1]
    TreeNode* root2 = new TreeNode(1);
    cout << "单节点树结果:" << endl;
    printVector(solution.levelOrderBottom1(root2));
    
    // 释放内存
    delete root1->right->right;
    delete root1->right->left;
    delete root1->right;
    delete root1->left;
    delete root1;
    delete root2;
    
    return 0;
}