// LeetCode 297. Serialize and Deserialize Binary Tree
// 题目链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
// 题目描述: 设计一个算法来序列化和反序列化二叉树。
// 序列化是将一个数据结构或者对象转换为连续的比特位的过程，进而可以将转换后的数据存储在一个文件或内存中，
// 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
//
// 解题思路:
// 1. 前序遍历序列化：使用特殊字符表示空节点
// 2. 递归反序列化：根据前序遍历顺序重建二叉树
// 3. 使用队列辅助反序列化：更直观的迭代方法
//
// 时间复杂度: 
//   - 序列化: O(n) - 每个节点访问一次
//   - 反序列化: O(n) - 每个节点处理一次
// 空间复杂度: O(n) - 需要存储序列化字符串或使用递归栈
// 是否为最优解: 是，这是序列化二叉树的标准方法

#include <string>
#include <queue>
#include <sstream>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

class Codec {
private:
    // 序列化分隔符
    const string SEPARATOR = ",";
    const string NULL_NODE = "null";

public:
    // 方法1: 前序遍历序列化（递归）
    // 核心思想: 使用前序遍历，空节点用"null"表示
    string serializePreorder(TreeNode* root) {
        if (root == nullptr) {
            return "";
        }
        
        stringstream ss;
        serializeHelper(root, ss);
        return ss.str();
    }
    
private:
    void serializeHelper(TreeNode* node, stringstream& ss) {
        if (node == nullptr) {
            ss << NULL_NODE << SEPARATOR;
            return;
        }
        
        // 前序遍历：根->左->右
        ss << node->val << SEPARATOR;
        serializeHelper(node->left, ss);
        serializeHelper(node->right, ss);
    }

public:
    // 方法2: 层序遍历序列化（BFS）
    // 核心思想: 使用队列进行层序遍历，更符合直观理解
    string serializeLevelOrder(TreeNode* root) {
        if (root == nullptr) {
            return "";
        }
        
        stringstream ss;
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            TreeNode* node = q.front();
            q.pop();
            
            if (node == nullptr) {
                ss << NULL_NODE << SEPARATOR;
                continue;
            }
            
            ss << node->val << SEPARATOR;
            q.push(node->left);
            q.push(node->right);
        }
        
        return ss.str();
    }

    // 反序列化方法（前序遍历版本）
    TreeNode* deserializePreorder(string data) {
        if (data.empty()) {
            return nullptr;
        }
        
        queue<string> nodes;
        stringstream ss(data);
        string token;
        
        while (getline(ss, token, SEPARATOR[0])) {
            if (!token.empty()) {
                nodes.push(token);
            }
        }
        
        return deserializeHelper(nodes);
    }
    
private:
    TreeNode* deserializeHelper(queue<string>& nodes) {
        if (nodes.empty()) {
            return nullptr;
        }
        
        string val = nodes.front();
        nodes.pop();
        
        if (val == NULL_NODE) {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(stoi(val));
        node->left = deserializeHelper(nodes);
        node->right = deserializeHelper(nodes);
        
        return node;
    }

public:
    // 反序列化方法（层序遍历版本）
    TreeNode* deserializeLevelOrder(string data) {
        if (data.empty()) {
            return nullptr;
        }
        
        queue<string> nodes;
        stringstream ss(data);
        string token;
        
        while (getline(ss, token, SEPARATOR[0])) {
            if (!token.empty()) {
                nodes.push(token);
            }
        }
        
        if (nodes.empty()) {
            return nullptr;
        }
        
        string rootVal = nodes.front();
        nodes.pop();
        
        if (rootVal == NULL_NODE) {
            return nullptr;
        }
        
        TreeNode* root = new TreeNode(stoi(rootVal));
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty() && !nodes.empty()) {
            TreeNode* node = q.front();
            q.pop();
            
            // 处理左子节点
            if (!nodes.empty()) {
                string leftVal = nodes.front();
                nodes.pop();
                
                if (leftVal != NULL_NODE) {
                    node->left = new TreeNode(stoi(leftVal));
                    q.push(node->left);
                }
            }
            
            // 处理右子节点
            if (!nodes.empty()) {
                string rightVal = nodes.front();
                nodes.pop();
                
                if (rightVal != NULL_NODE) {
                    node->right = new TreeNode(stoi(rightVal));
                    q.push(node->right);
                }
            }
        }
        
        return root;
    }

    // 提交如下的方法（使用层序遍历版本，更直观）
    string serialize(TreeNode* root) {
        return serializeLevelOrder(root);
    }
    
    TreeNode* deserialize(string data) {
        return deserializeLevelOrder(data);
    }
};

// 测试用例
// int main() {
//     Codec codec;
//
//     // 测试用例1:
//     //       1
//     //      / \
//     //     2   3
//     //        / \
//     //       4   5
//     TreeNode* root1 = new TreeNode(1);
//     root1->left = new TreeNode(2);
//     root1->right = new TreeNode(3);
//     root1->right->left = new TreeNode(4);
//     root1->right->right = new TreeNode(5);
//     
//     // 序列化
//     string serialized = codec.serialize(root1);
//     // 反序列化
//     TreeNode* deserialized = codec.deserialize(serialized);
//     
//     // 验证结果...
//     
//     return 0;
// }