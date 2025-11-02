#include <iostream>
#include <vector>
#include <queue>
#include <string>
using namespace std;

// UVA 122. Trees on the Level
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=58
// 题目大意: 按照层序遍历的方式构建二叉树并输出节点值。输入格式为(value, path)，其中path是由L和R组成的字符串，
// 表示从根节点到该节点的路径，L表示左子节点，R表示右子节点。

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    
    TreeNode(int v = 0) : val(v), left(nullptr), right(nullptr) {}
};

// 节点信息结构体，用于存储节点值和路径
struct TreeNodeInfo {
    int val;
    string path;
    
    TreeNodeInfo(int v, const string& p) : val(v), path(p) {}
};

/**
 * 根据路径插入节点
 * @param root 根节点
 * @param val 节点值
 * @param path 路径字符串
 * @return 是否插入成功
 */
bool insertNode(TreeNode* root, int val, const string& path) {
    TreeNode* current = root;
    
    // 根据路径找到要插入的位置
    for (char direction : path) {
        if (direction == 'L') {
            if (current->left == nullptr) {
                current->left = new TreeNode(0); // 临时节点
            }
            current = current->left;
        } else if (direction == 'R') {
            if (current->right == nullptr) {
                current->right = new TreeNode(0); // 临时节点
            }
            current = current->right;
        } else {
            // 无效路径字符
            return false;
        }
    }
    
    // 检查节点是否已经被赋值
    if (current->val != 0) {
        // 节点已经被赋值，说明重复
        return false;
    }
    
    // 赋值
    current->val = val;
    return true;
}

/**
 * 层序遍历
 * @param root 根节点
 * @return 遍历结果，如果树不完整则返回空向量
 */
vector<int> bfs(TreeNode* root) {
    vector<int> result;
    queue<TreeNode*> q;
    q.push(root);
    
    while (!q.empty()) {
        TreeNode* current = q.front();
        q.pop();
        
        // 如果节点值为0，说明是临时节点，树不完整
        if (current->val == 0) {
            return vector<int>(); // 返回空向量表示树不完整
        }
        
        result.push_back(current->val);
        
        if (current->left != nullptr) {
            q.push(current->left);
        }
        if (current->right != nullptr) {
            q.push(current->right);
        }
    }
    
    return result;
}

/**
 * 构建二叉树并进行层序遍历
 * 思路:
 * 1. 解析输入的节点信息，按照路径构建二叉树
 * 2. 对构建的二叉树进行层序遍历
 * 3. 如果构建过程中发现节点重复或缺失，返回空向量
 * 时间复杂度: O(n) - n是节点数量
 * 空间复杂度: O(n) - 存储节点和队列
 */
vector<int> levelOrderTraversal(const vector<TreeNodeInfo>& nodes) {
    // 创建根节点
    TreeNode* root = new TreeNode(0); // 临时根节点
    
    // 根据路径信息构建树
    for (const TreeNodeInfo& nodeInfo : nodes) {
        if (!insertNode(root, nodeInfo.val, nodeInfo.path)) {
            // 如果插入失败，返回空向量
            return vector<int>();
        }
    }
    
    // 进行层序遍历
    vector<int> result = bfs(root);
    
    // 释放内存
    // 注意：在实际应用中，应该实现完整的内存管理
    
    return result;
}

// 测试方法
int main() {
    // 示例测试
    vector<TreeNodeInfo> nodes;
    nodes.push_back(TreeNodeInfo(5, ""));
    nodes.push_back(TreeNodeInfo(3, "L"));
    nodes.push_back(TreeNodeInfo(4, "LL"));
    nodes.push_back(TreeNodeInfo(7, "LR"));
    
    vector<int> result = levelOrderTraversal(nodes);
    if (result.empty()) {
        cout << "not complete" << endl;
    } else {
        for (size_t i = 0; i < result.size(); i++) {
            if (i > 0) cout << " ";
            cout << result[i];
        }
        cout << endl;
    }
    
    return 0;
}